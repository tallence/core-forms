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

import Struct from "@coremedia/studio-client.cap-rest-client/struct/Struct";
import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import PropertyEditorUtil from "@coremedia/studio-client.main.editor-components/sdk/util/PropertyEditorUtil";
import Container from "@jangaroo/ext-ts/container/Container";
import { bind } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import FormEditorField from "./FormEditorField";

interface FormEditorFieldBaseConfig extends Config<Container> {
}

/**
 * Extend this class to create an editor for a field within a form element.
 */
class FormEditorFieldBase extends Container {
  declare Config: FormEditorFieldBaseConfig;

  #propertyVE: ValueExpression = null;

  #bindTo: ValueExpression = null;

  #forceReadOnlyVE: ValueExpression = null;

  #propertyName: string = null;

  #formElementStructVE: ValueExpression = null;

  constructor(config: Config<FormEditorField> = null) {
    super(config);
    this.#initFormEditorField(config);
  }

  /**
   * Initializes the editor by setting the value expression for the field and adding a value change listener. Since it
   * is not certain whether the constructor or getStructPropertyVE is called first, the initialization is called twice.
   */
  #initFormEditorField(config: Config<FormEditorField>): void {
    if (!this.#propertyVE) {
      this.#propertyName = config.propertyName;
      this.#bindTo = config.bindTo;
      this.#forceReadOnlyVE = config.forceReadOnlyValueExpression;
      this.#formElementStructVE = config.formElementStructVE;
      this.#formElementStructVE.addChangeListener(bind(this, this.#onFormElementStructChange));
      this.#updatePropertyVE(this.#formElementStructVE);
    }
  }

  /**
   * Initializes the form editor field and returns the value expression that is evaluated for the property value of
   * the editor.
   */
  protected getPropertyVE(config: Config<FormEditorField>): ValueExpression {
    this.#initFormEditorField(config);
    return this.#propertyVE;
  }

  /**
   * The method is called as soon as the struct of the form element changes. The value can change when a new form
   * element is selected or added, or when the struct changes. The initStruct method is called so that the editor can
   * ensure that the struct has the correct structure.
   * @param structVE
   */
  #onFormElementStructChange(structVE: ValueExpression): void {
    const struct: Struct = structVE.getValue();
    //
    if (struct) {
      this.#updatePropertyVE(structVE);
      if (!this.#isReadOnly()) {
        this.initStruct(struct);
      }
      if (this.#propertyVE.getValue() == undefined && !this.#isReadOnly()) {
        this.initWithDefault(this.#propertyVE);
      }
    }
  }

  /**
   * As soon as the struct and the selected form element has changed, the property value expression must be updated
   * so that the default can be set.
   */
  #updatePropertyVE(formElementStructVE: ValueExpression): void {
    this.#propertyVE = formElementStructVE.extendBy(this.#propertyName);
  }

  /**
   * If the document is checked by another use or the forceReadOnlyValueExpression is evaluated to true, the default
   * values in the struct can not be set.
   */
  #isReadOnly(): boolean {
    if (this.#forceReadOnlyVE && this.#forceReadOnlyVE.getValue()) {
      return true;
    }
    if (!this.#bindTo) {
      return false;
    }
    return PropertyEditorUtil.isReadOnly(this.#bindTo.getValue());
  }

  /**
   * Override this method to set a default value if the entry in the struct is still undefined. For strings or integer
   * values, the values can be written directly into the value expression.
   */
  protected initWithDefault(ve: ValueExpression): void {
  }

  /**
   * Override this method to perform operations directly on the struct. If this method is overwritten, this method
   * should also be called to init the validator sub struct. Otherwise a null pointer will occur for the validator
   * binding. The struct contains the values of the form element. If the editor for the field inside the form element must store values in a sub-struct, the struct can be
   * initialized by this method.
   */
  protected initStruct(struct: Struct): void {
    if (!struct.get("validator")) {
      struct.getType().addStructProperty("validator");
    }
  }


  override destroy(...args): any {
    this.#formElementStructVE.removeChangeListener(this.#onFormElementStructChange);
    return super.destroy(...args);
  }
}

export default FormEditorFieldBase;
