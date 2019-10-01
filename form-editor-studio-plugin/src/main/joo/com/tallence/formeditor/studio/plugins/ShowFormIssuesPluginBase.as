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

package com.tallence.formeditor.studio.plugins {
import com.coremedia.cms.editor.sdk.validation.ValidationUtil;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.validation.Issue;
import com.coremedia.ui.data.validation.Severity;
import com.coremedia.ui.mixins.IValidationStateMixin;
import com.coremedia.ui.mixins.ValidationState;
import com.coremedia.ui.mixins.ValidationStateMixin;
import com.coremedia.ui.plugins.BindPlugin;
import com.coremedia.ui.util.EncodingUtil;
import com.tallence.formeditor.studio.model.FormIssues;

import mx.resources.ResourceManager;

/**
 * The plugin is partially copied from the {@link com.coremedia.cms.editor.sdk.validation.ShowIssuesPlugin} and used to
 * set validation sates on form elements. It is not possible to extend the existing plugin because some methods are
 * private. The plugin can be applied to:
 * <ul>
 * <li>form input fields implementing the {@link IValidationStateMixin} interface to show errors on form element
 * propertiers</li>
 * <li>form elements {@link com.tallence.formeditor.studio.AppliedFormElementsContainer} to indicate that the form
 * element contains a property with an error</li>
 * </ul>
 *
 *
 */
public class ShowFormIssuesPluginBase extends BindPlugin {

  /**
   * A ValueExpression evaluation to a {@link com.tallence.formeditor.studio.model.FormIssues} bean that contains all
   * issues for all form elements of the content.
   */
  [Bindable]
  public var issuesVE:ValueExpression;

  /**
   * The property name of the form field. If the value is undefined, the plugin checks if at least one field in the form
   * element has an error.
   */
  [Bindable]
  public var propertyName:String;

  public function ShowFormIssuesPluginBase(config:ShowFormIssuesPlugin = null) {
    propertyName = config.propertyName;
    super(config);
  }

  protected function issuesChanged():void {
    var formIssues:FormIssues = bindTo.getValue() as FormIssues;
    var issues:Array;
    if (formIssues === null) {
      issues = [];
    } else if (!propertyName) {
      issues = formIssues.getAllIssues();
    } else {
      issues = formIssues.getIssues(propertyName);
    }
    if (issues) {
      // Find the highest severity level.
      var maxSeverity:String = ValidationUtil.computeMaximumSeverity(issues);

      //set the severity on the component
      this.setSeverityOnComponent(maxSeverity, issues);
    }
  }

  /**
   * Copied from {@link com.coremedia.cms.editor.sdk.validation.ShowIssuesPlugin}
   */
  private function setSeverityOnComponent(maxSeverity:String, issues:Array):void {
    var issueText:String = issuesAsText(issues);
    setValidationState(getComponent() as ValidationStateMixin, maxSeverity, issueText);
  }

  /**
   * Copied from {@link com.coremedia.cms.editor.sdk.validation.ShowIssuesPlugin}
   */
  private static function setValidationState(component:IValidationStateMixin, newSeverity:String, errorIssuesText:String = undefined):void {
    if (component) {
      var newValidationState:ValidationState = ValidationUtil.TO_VALIDATION_STATE[newSeverity];
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
  private static function issuesAsText(issues:Array):String {
    var text:String = calculateIssuesText(issues, Severity.ERROR);
    text += calculateIssuesText(issues, Severity.WARN);
    text += calculateIssuesText(issues, Severity.INFO);
    return text;
  }

  /**
   * Copied from {@link com.coremedia.cms.editor.sdk.validation.ShowIssuesPlugin}
   */
  private static function calculateIssuesText(issues:Array, severity:String):String {
    // adding explicit \n after <br> so stripping html tags will not remove spacings between the characters
    //calculate title
    var result:String = "<span><b>" + ResourceManager.getInstance().getString('com.coremedia.cms.editor.Editor', severity) + "</b></span>"
            + "</br>\n";
    //filter issues
    var filteredIssues:Array = issues.filter(function (issue:Issue):Boolean {
      return issue.severity === severity;
    });

    //combine the text, return the empty string
    return filteredIssues.length > 0 ? result + filteredIssues.map(
            function (issue:Issue):String {
              return EncodingUtil.encodeForHTML(ValidationUtil.formatText(issue));
            }).join("<br/>\n") : "";
  }

}
}
