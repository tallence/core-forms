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
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditorFieldBase from "./FormEditorFieldBase";

interface FormEditorFieldConfig extends Config<FormEditorFieldBase>, Partial<Pick<FormEditorField,
  "formElementStructVE" |
  "fieldLabel" |
  "propertyName" |
  "bindTo" |
  "forceReadOnlyValueExpression" |
  "formIssuesVE" |
  "propertyPathVE"
>> {
}

class FormEditorField extends FormEditorFieldBase {
  declare Config: FormEditorFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.formEditorField";

  #formElementStructVE: ValueExpression = null;

  /**
   * A value expression that is evaluated for the associated struct of the form element.
   */
  get formElementStructVE(): ValueExpression {
    return this.#formElementStructVE;
  }

  set formElementStructVE(value: ValueExpression) {
    this.#formElementStructVE = value;
  }

  #fieldLabel: string = null;

  /**
   * The field label for the field editor.
   */
  get fieldLabel(): string {
    return this.#fieldLabel;
  }

  set fieldLabel(value: string) {
    this.#fieldLabel = value;
  }

  #propertyName: string = null;

  /**
   * The property name of the struct entry.
   */
  get propertyName(): string {
    return this.#propertyName;
  }

  set propertyName(value: string) {
    this.#propertyName = value;
  }

  #bindTo: ValueExpression = null;

  /**
   * The content bean value expression.
   */
  get bindTo(): ValueExpression {
    return this.#bindTo;
  }

  set bindTo(value: ValueExpression) {
    this.#bindTo = value;
  }

  #forceReadOnlyValueExpression: ValueExpression = null;

  /**
   * An optional ValueExpression which makes the component read-only if it is evaluated to true.
   */
  get forceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  set forceReadOnlyValueExpression(value: ValueExpression) {
    this.#forceReadOnlyValueExpression = value;
  }

  #formIssuesVE: ValueExpression = null;

  /**
   * A value expression which contains all issues for the form element. This value expression is used by the
   * {@link com.tallence.formeditor.studio.plugins.ShowFormIssuesPlugin} to display validation states and messages.
   */
  get formIssuesVE(): ValueExpression {
    return this.#formIssuesVE;
  }

  set formIssuesVE(value: ValueExpression) {
    this.#formIssuesVE = value;
  }

  #propertyPathVE: ValueExpression = null;

  /**
   * A value expression evaluating to the property path of the applied form element.
   * e.g. 'formData.formElements.320798398'
   */
  get propertyPathVE(): ValueExpression {
    return this.#propertyPathVE;
  }

  set propertyPathVE(value: ValueExpression) {
    this.#propertyPathVE = value;
  }

  constructor(config: Config<FormEditorField> = null) {
    super(ConfigUtils.apply(Config(FormEditorField, { margin: "10 0 0 0" }), config));
  }
}

export default FormEditorField;
