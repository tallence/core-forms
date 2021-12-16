/*
 * Copyright 2018 Tallence AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import Issue from "@coremedia/studio-client.client-core/data/validation/Issue";
import Issues from "@coremedia/studio-client.client-core/data/validation/Issues";
import Severity from "@coremedia/studio-client.client-core/data/validation/Severity";
import EncodingUtil from "@coremedia/studio-client.client-core/util/EncodingUtil";
import ValidationUtil from "@coremedia/studio-client.ext.errors-validation-components/validation/ValidationUtil";
import ValidationState from "@coremedia/studio-client.ext.ui-components/mixins/ValidationState";
import ValidationStateMixin from "@coremedia/studio-client.ext.ui-components/mixins/ValidationStateMixin";
import BindPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPlugin";
import Editor_properties from "@coremedia/studio-client.main.editor-components/Editor_properties";
import StringUtil from "@jangaroo/ext-ts/String";
import { as } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import ShowFormIssuesPlugin from "./ShowFormIssuesPlugin";

interface ShowFormIssuesPluginBaseConfig extends Config<BindPlugin>, Partial<Pick<ShowFormIssuesPluginBase,
  "issuesVE" |
  "propertyName" |
  "propertyPathVE"
>> {
}

/**
 * The plugin is partially copied from the {@link com.coremedia.cms.editor.sdk.validation.ShowIssuesPlugin} and used to
 * set validation sates on form elements. It is not possible to extend the existing plugin because some methods are
 * private. The plugin can be applied to:
 * <ul>
 * <li>form input fields implementing the {@link IValidationStateMixin} interface to show errors on form element
 * propertiers</li>
 * <li>form elements {@link com.tallence.formeditor.studio.AppliedFormElementContainer} to indicate that the form
 * element contains a property with an error</li>
 * </ul>
 *
 *
 */
class ShowFormIssuesPluginBase extends BindPlugin {
  declare Config: ShowFormIssuesPluginBaseConfig;

  #issuesVE: ValueExpression = null;

  /**
   * A ValueExpression evaluation to the issues of the content.
   */
  get issuesVE(): ValueExpression {
    return this.#issuesVE;
  }

  set issuesVE(value: ValueExpression) {
    this.#issuesVE = value;
  }

  #propertyName: string = null;

  /**
   * The property name of the form field. If the value is undefined, the plugin checks if at least one field in the form
   * element has an error.
   */
  get propertyName(): string {
    return this.#propertyName;
  }

  set propertyName(value: string) {
    this.#propertyName = value;
  }

  #propertyPathVE: ValueExpression = null;

  get propertyPathVE(): ValueExpression {
    return this.#propertyPathVE;
  }

  set propertyPathVE(value: ValueExpression) {
    this.#propertyPathVE = value;
  }

  constructor(config: Config<ShowFormIssuesPlugin> = null) {
    super((()=>{
      this.propertyName = config.propertyName;
      this.propertyPathVE = config.propertyPathVE;
      return config;
    })());
  }

  protected getIssuesVE(config: Config<ShowFormIssuesPlugin>): ValueExpression {
    return ValueExpressionFactory.createFromFunction((): Array<any> => {
      if (!config.propertyPathVE || !this.propertyPathVE.getValue()) {
        return [];
      }
      if (config.propertyName) {
        return config.issuesVE.extendBy([config.propertyPathVE.getValue() + "." + config.propertyName]).getValue();
      } else {
        // if the plugin is added to the {@link AppliedFormElementContainer}, all issues that belong to the form
        // element must be filtered
        const issues: Issues = config.issuesVE.getValue();
        const path: string = config.propertyPathVE.getValue();
        return issues.getAll().filter((issue: Issue): boolean =>
          StringUtil.startsWith(issue.property, path, true),
        );
      }
    });
  }

  protected issuesChanged(): void {
    const issues: Array<any> = this.bindTo.getValue() ? this.bindTo.getValue() : [];
    // Find the highest severity level.
    const maxSeverity = ValidationUtil.computeMaximumSeverity(issues);

    //set the severity on the component
    this.#setSeverityOnComponent(maxSeverity, issues);
  }

  /**
   * Copied from {@link com.coremedia.cms.editor.sdk.validation.ShowIssuesPlugin}
   */
  #setSeverityOnComponent(maxSeverity: string, issues: Array<any>): void {
    const issueText = ShowFormIssuesPluginBase.#issuesAsText(issues);
    ShowFormIssuesPluginBase.#setValidationState(as(this.getComponent(), ValidationStateMixin), maxSeverity, issueText);
  }

  /**
   * Copied from {@link com.coremedia.cms.editor.sdk.validation.ShowIssuesPlugin}
   */
  static #setValidationState(component: ValidationStateMixin, newSeverity: string, errorIssuesText?: string): void {
    if (component) {
      const newValidationState: ValidationState = ValidationUtil.TO_VALIDATION_STATE[newSeverity];
      if (newValidationState) {
        component.validationState = newValidationState;
        component.validationMessage = errorIssuesText;
      } else {
        if (component.validationState != ValidationState.SUCCESS) {
          component.validationState = null;
          component.validationMessage = null;
        }
      }
    }
  }

  /**
   * Copied from {@link com.coremedia.cms.editor.sdk.validation.ShowIssuesPlugin}
   */
  static #issuesAsText(issues: Array<any>): string {
    let text = ShowFormIssuesPluginBase.#calculateIssuesText(issues, Severity.ERROR);
    text += ShowFormIssuesPluginBase.#calculateIssuesText(issues, Severity.WARN);
    text += ShowFormIssuesPluginBase.#calculateIssuesText(issues, Severity.INFO);
    return text;
  }

  /**
   * Copied from {@link com.coremedia.cms.editor.sdk.validation.ShowIssuesPlugin}
   */
  static #calculateIssuesText(issues: Array<any>, severity: string): string {
    // adding explicit \n after <br> so stripping html tags will not remove spacings between the characters
    //calculate title
    const result = "<span><b>" + Editor_properties[severity] + "</b></span>"
            + "</br>\n";
    //filter issues
    const filteredIssues = issues.filter((issue: Issue): boolean =>
      issue.severity === severity,
    );

    //combine the text, return the empty string
    return filteredIssues.length > 0 ? result + filteredIssues.map(
      (issue: Issue): string =>
        EncodingUtil.encodeForHTML(ValidationUtil.formatText(issue)),
    ).join("<br/>\n") : "";
  }

}

export default ShowFormIssuesPluginBase;
