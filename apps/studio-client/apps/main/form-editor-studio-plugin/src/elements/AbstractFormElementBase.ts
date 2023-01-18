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
import {mixin} from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import FormElementStructWrapper from "../model/FormElementStructWrapper";
import AbstractFormElement from "./AbstractFormElement";
import FormElement from "./FormElement";
import Bean from "@coremedia/studio-client.client-core/data/Bean";
import beanFactory from "@coremedia/studio-client.client-core/data/beanFactory";

interface AbstractFormElementBaseConfig extends Config<Container> {
}

class AbstractFormElementBase extends Container implements FormElement {
  declare Config: AbstractFormElementBaseConfig;

  static readonly #FORM_ELEMENT_UPDATE_EVT: string = "formElementUpdated";
  static readonly FORM_ELEMENTS_STRUCT: string = "formElementStructVE";
  static readonly BIND_TO: string = "bindTo";
  static readonly FORCE_READ_ONLY_VE: string = "forceReadOnlyVE";
  static readonly FORM_ISSUES_VE: string = "formIssuesVE";
  static readonly PROPERTY_PATH_VE: string = "propertyPathVE";

  #group: string = null;

  #elementType: string = null;

  #iconCls: string = null;

  #structWrapper: FormElementStructWrapper = null;

  #localBean: Bean = null;

  constructor(config: Config<AbstractFormElement> = null) {
    if (!config.formElementType) {
      throw new Error("Config formElementType is missing.");
    }
    super((() => {
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
    this.getLocalBean().set(AbstractFormElementBase.FORM_ELEMENTS_STRUCT, wrapper.getFormElementVE());
    this.getLocalBean().set(AbstractFormElementBase.BIND_TO, wrapper.getBindTo());
    this.getLocalBean().set(AbstractFormElementBase.FORCE_READ_ONLY_VE, wrapper.getForceReadOnlyValueExpression());
    this.getLocalBean().set(AbstractFormElementBase.PROPERTY_PATH_VE, ValueExpressionFactory.createFromValue(wrapper.getPropertyPath()));
    this.fireEvent(AbstractFormElementBase.#FORM_ELEMENT_UPDATE_EVT);
  }

  getFormElementStructWrapper(): FormElementStructWrapper {
    return this.#structWrapper;
  }

  getLocalBean(): Bean {
    if (!this.#localBean) {
      this.#localBean = beanFactory._.createLocalBean();
      this.#localBean.set(AbstractFormElementBase.FORM_ELEMENTS_STRUCT, ValueExpressionFactory.createFromValue());
      this.#localBean.set(AbstractFormElementBase.BIND_TO, ValueExpressionFactory.createFromValue());
      this.#localBean.set(AbstractFormElementBase.FORCE_READ_ONLY_VE, ValueExpressionFactory.createFromValue());
      this.#localBean.set(AbstractFormElementBase.PROPERTY_PATH_VE, ValueExpressionFactory.createFromValue(""));
      this.#localBean.set(AbstractFormElementBase.FORM_ISSUES_VE, this.getBindTo().extendBy(["issues", "byProperty"]));
    }
    return this.#localBean;
  }

  getFormElementStructVE(): ValueExpression {
    return this.getValueExpression(AbstractFormElementBase.FORM_ELEMENTS_STRUCT);
  }

  getBindTo(): ValueExpression {
    return this.getValueExpression(AbstractFormElementBase.BIND_TO);
  }

  getForceReadOnlyVE(): ValueExpression {
    return this.getValueExpression(AbstractFormElementBase.FORCE_READ_ONLY_VE);
  }

  getFormIssuesVE(): ValueExpression {
    return this.getValueExpression(AbstractFormElementBase.FORM_ISSUES_VE);
  }

  getPropertyPathVE(): ValueExpression {
    return this.getValueExpression(AbstractFormElementBase.PROPERTY_PATH_VE);
  }

  /**
   * Since the editors for form elements are reused, the component is created with default values for all value
   * expressions (see #getLocalBean() creation). As soon as the method updateFormElementStructWrapper is called and the
   * form element is updated, a new value expression is returned. This is necessary so that the binding to the correct
   * value expression works after the update.
   */
  getValueExpression(key: String): ValueExpression {
    var self = this;
    return ValueExpressionFactory.createFromFunction((): ValueExpressionValueHolder => {
      ObservableUtil.dependOn(self, AbstractFormElementBase.#FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(this.getLocalBean().get(key));
    });
  }
}

mixin(AbstractFormElementBase, FormElement);

export default AbstractFormElementBase;
