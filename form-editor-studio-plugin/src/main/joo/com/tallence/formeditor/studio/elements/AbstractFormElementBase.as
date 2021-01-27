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

package com.tallence.formeditor.studio.elements {
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.coremedia.ui.data.ValueExpressionValueHolder;
import com.coremedia.ui.util.ObservableUtil;
import com.tallence.formeditor.studio.model.FormElementStructWrapper;

import ext.container.Container;

public class AbstractFormElementBase extends Container implements FormElement {

  private static const FORM_ELEMENT_UPDATE_EVT:String = "formElementUpdated";

  private var group:String;
  private var elementType:String;
  private var iconCls:String;
  private var structWrapper:FormElementStructWrapper;
  private var formElementStructVE:ValueExpression;
  private var bindTo:ValueExpression;
  private var forceReadOnlyValueExpression:ValueExpression;
  private var formIssuesVE:ValueExpression;
  private var propertyPathVE:ValueExpression;

  public function AbstractFormElementBase(config:AbstractFormElement = null) {
    if (!config.formElementType) {
      throw new Error("Config formElementType is missing.");
    }
    elementType = config.formElementType;
    iconCls = config.formElementIconCls;
    //using the default value "other".
    group = config.formElementGroup ? config.formElementGroup : "other";
    super(config);
  }

  public function getFormElementType():String {
    return elementType;
  }

  public function getFormElementIconCls():String {
    return iconCls;
  }

  public function getFormElementGroup():String {
    return group;
  }

  public function updateFormElementStructWrapper(wrapper:FormElementStructWrapper):void {
    structWrapper = wrapper;
    formElementStructVE = wrapper.getFormElementVE();
    bindTo = wrapper.getBindTo();
    forceReadOnlyValueExpression = wrapper.getForceReadOnlyValueExpression();
    propertyPathVE = ValueExpressionFactory.createFromValue(wrapper.getPropertyPath());
    fireEvent(FORM_ELEMENT_UPDATE_EVT);
  }

  public function getFormElementStructWrapper():FormElementStructWrapper {
    return structWrapper;
  }

  /**
   * Since the editors for form elements are reused, the component is created without a form element struct value
   * expression. As soon as the method updateFormElementStructWrapper is called and the form element is updated, a new
   * value expression is returned. This is necessary so that the binding to the correct struct works after the update.
   */
  public function getFormElementStructVE():ValueExpression {
    if (!formElementStructVE) {
      formElementStructVE = ValueExpressionFactory.createFromValue();
    }
    var self:AbstractFormElementBase = this;
    return ValueExpressionFactory.createFromFunction(function ():ValueExpressionValueHolder {
      ObservableUtil.dependOn(self, FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(formElementStructVE);
    });
  }

  /**
   * Since the editors for form elements are reused, the component is created without a bindTo value expression. As
   * soon as the method updateFormElementStructWrapper is called and the form element is updated, a new
   * value expression is returned. This is necessary so that the binding to the correct bindTo works after the update.
   */
  public function getBindTo():ValueExpression {
    if (!bindTo) {
      bindTo = ValueExpressionFactory.createFromValue();
    }
    var self:AbstractFormElementBase = this;
    return ValueExpressionFactory.createFromFunction(function ():ValueExpressionValueHolder {
      ObservableUtil.dependOn(self, FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(bindTo);
    });
  }

  /**
   * Since the editors for form elements are reused, the component is created without a forceReadOnlyValueExpression
   * value expression. As soon as the method updateFormElementStructWrapper is called and the form element is updated,
   * a new value expression is returned. This is necessary so that the binding to the correct
   * forceReadOnlyValueExpression works after the update.
   */
  public function getForceReadOnlyVE():ValueExpression {
    if (!forceReadOnlyValueExpression) {
      forceReadOnlyValueExpression = ValueExpressionFactory.createFromValue();
    }
    var self:AbstractFormElementBase = this;
    return ValueExpressionFactory.createFromFunction(function ():ValueExpressionValueHolder {
      ObservableUtil.dependOn(self, FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(forceReadOnlyValueExpression);
    });
  }

  /**
   * Since the editors for form elements are reused, the component is created without a
   * form issues value expression. As soon as the method updateFormElementStructWrapper is called and
   * the form element is updated, a new value expression is returned. This is necessary so that the binding to the
   * correct formIssuesVE works after the update.
   */
  public function getFormIssuesVE():ValueExpression {
    if (!formIssuesVE) {
      formIssuesVE = getBindTo().extendBy(['issues', 'byProperty']);
    }
    var self:AbstractFormElementBase = this;
    return ValueExpressionFactory.createFromFunction(function ():ValueExpressionValueHolder {
      ObservableUtil.dependOn(self, FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(formIssuesVE);
    });
  }

  /**
   * Since the editors for form elements are reused, the component is created without a
   * form issues value expression. As soon as the method updateFormElementStructWrapper is called and
   * the form element is updated, a new value expression is returned. This is necessary so that the binding to the
   * correct propertyPathVE works after the update.
   */
  public function getPropertyPathVE():ValueExpression {
    if (!propertyPathVE) {
      propertyPathVE = ValueExpressionFactory.createFromValue("");
    }
    var self:AbstractFormElementBase = this;
    return ValueExpressionFactory.createFromFunction(function ():ValueExpressionValueHolder {
      ObservableUtil.dependOn(self, FORM_ELEMENT_UPDATE_EVT);
      return new ValueExpressionValueHolder(propertyPathVE);
    });
  }
}
}
