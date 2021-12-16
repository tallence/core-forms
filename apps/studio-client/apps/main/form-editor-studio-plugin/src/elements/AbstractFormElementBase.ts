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
import ValueExpressionValueHolder from "@coremedia/studio-client.client-core/data/ValueExpressionValueHolder";
import ObservableUtil from "@coremedia/studio-client.ext.ui-components/util/ObservableUtil";
import Container from "@jangaroo/ext-ts/container/Container";
import { mixin } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import FormElementStructWrapper from "../model/FormElementStructWrapper";
import AbstractFormElement from "./AbstractFormElement";
import FormElement from "./FormElement";

interface AbstractFormElementBaseConfig extends Config<Container> {
}

class AbstractFormElementBase extends Container implements FormElement {
  declare Config: AbstractFormElementBaseConfig;

  static readonly #FORM_ELEMENT_UPDATE_EVT: string = "formElementUpdated";

  #group: string = null;

  #elementType: string = null;

  #iconCls: string = null;

  #structWrapper: FormElementStructWrapper = null;

  #formElementStructVE: ValueExpression = null;

  #bindTo: ValueExpression = null;

  #forceReadOnlyValueExpression: ValueExpression = null;

  #formIssuesVE: ValueExpression = null;

  #propertyPathVE: ValueExpression = null;

  constructor(config: Config<AbstractFormElement> = null) {
    if (!config.formElementType) {
      throw new Error("Config formElementType is missing.");
    }
    super((()=>{
      this.#elementType = config.formElementType;
      this.#iconCls = config.formElementIconCls;
      //using the default value "other".
      this.#group = config.formElementGroup ? config.formElementGroup : "other";
      return config;
    })());
  }

  getFormElementType(): string {
    return this.#elementType;
  }

  getFormElementIconCls(): string {
    return this.#iconCls;
  }

  getFormElementGroup(): string {
    return this.#group;
  }

  updateFormElementStructWrapper(wrapper: FormElementStructWrapper): void {
    this.#structWrapper = wrapper;
    this.#formElementStructVE = wrapper.getFormElementVE();
    this.#bindTo = wrapper.getBindTo();
    this.#forceReadOnlyValueExpression = wrapper.getForceReadOnlyValueExpression();
    this.#propertyPathVE = ValueExpressionFactory.createFromValue(wrapper.getPropertyPath());
    this.fireEvent(AbstractFormElementBase.#FORM_ELEMENT_UPDATE_EVT);
  }

  getFormElementStructWrapper(): FormElementStructWrapper {
    return this.#structWrapper;
  }

  /**
   * Since the editors for form elements are reused, the component is created without a form element struct value
   * expression. As soon as the method updateFormElementStructWrapper is called and the form element is updated, a new
   * value expression is returned. This is necessary so that the binding to the correct struct works after the update.
   */
  getFormElementStructVE(): ValueExpression {
    if (!this.#formElementStructVE) {
      this.#formElementStructVE = ValueExpressionFactory.createFromValue();
    }
    const self = this;
    return ValueExpressionFactory.createFromFunction((): ValueExpressionValueHolder => {
      ObservableUtil.dependOn(self, AbstractFormElementBase.#FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(this.#formElementStructVE);
    });
  }

  /**
   * Since the editors for form elements are reused, the component is created without a bindTo value expression. As
   * soon as the method updateFormElementStructWrapper is called and the form element is updated, a new
   * value expression is returned. This is necessary so that the binding to the correct bindTo works after the update.
   */
  getBindTo(): ValueExpression {
    if (!this.#bindTo) {
      this.#bindTo = ValueExpressionFactory.createFromValue();
    }
    const self = this;
    return ValueExpressionFactory.createFromFunction((): ValueExpressionValueHolder => {
      ObservableUtil.dependOn(self, AbstractFormElementBase.#FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(this.#bindTo);
    });
  }

  /**
   * Since the editors for form elements are reused, the component is created without a forceReadOnlyValueExpression
   * value expression. As soon as the method updateFormElementStructWrapper is called and the form element is updated,
   * a new value expression is returned. This is necessary so that the binding to the correct
   * forceReadOnlyValueExpression works after the update.
   */
  getForceReadOnlyVE(): ValueExpression {
    if (!this.#forceReadOnlyValueExpression) {
      this.#forceReadOnlyValueExpression = ValueExpressionFactory.createFromValue();
    }
    const self = this;
    return ValueExpressionFactory.createFromFunction((): ValueExpressionValueHolder => {
      ObservableUtil.dependOn(self, AbstractFormElementBase.#FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(this.#forceReadOnlyValueExpression);
    });
  }

  /**
   * Since the editors for form elements are reused, the component is created without a
   * form issues value expression. As soon as the method updateFormElementStructWrapper is called and
   * the form element is updated, a new value expression is returned. This is necessary so that the binding to the
   * correct formIssuesVE works after the update.
   */
  getFormIssuesVE(): ValueExpression {
    if (!this.#formIssuesVE) {
      this.#formIssuesVE = this.getBindTo().extendBy(["issues", "byProperty"]);
    }
    const self = this;
    return ValueExpressionFactory.createFromFunction((): ValueExpressionValueHolder => {
      ObservableUtil.dependOn(self, AbstractFormElementBase.#FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(this.#formIssuesVE);
    });
  }

  /**
   * Since the editors for form elements are reused, the component is created without a
   * form issues value expression. As soon as the method updateFormElementStructWrapper is called and
   * the form element is updated, a new value expression is returned. This is necessary so that the binding to the
   * correct propertyPathVE works after the update.
   */
  getPropertyPathVE(): ValueExpression {
    if (!this.#propertyPathVE) {
      this.#propertyPathVE = ValueExpressionFactory.createFromValue("");
    }
    const self = this;
    return ValueExpressionFactory.createFromFunction((): ValueExpressionValueHolder => {
      ObservableUtil.dependOn(self, AbstractFormElementBase.#FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(this.#propertyPathVE);
    });
  }
}
mixin(AbstractFormElementBase, FormElement);

export default AbstractFormElementBase;
