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

package com.tallence.formeditor.studio.model {
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.impl.BeanImpl;

/**
 * Bean which contains all form issues for the respective form element. Used by the {@link ShowFormIssuesPlugin} plugin
 * to display error messages.
 */
public class FormIssues extends BeanImpl {

  private static const PROPERTY_NAMES:String = "propertyNames";

  /**
   * Creates a new bean containing all form issues.
   *
   * @param propertyNames the property names of the form element
   * @param bindTo the value expression evaluating to the content
   * @param formElementId the id of the form element
   */
  public function FormIssues(propertyNames:Array, bindTo:ValueExpression, formElementId:String) {
    if (propertyNames) {
      propertyNames.forEach(function (propertyName:String):void {
        set(PROPERTY_NAMES, propertyNames);
        propertyNames.forEach(function (propertyName:String):void {
          var ve:ValueExpression = bindTo.extendBy([
            "issues",
            "byProperty",
            "localSettings.formElements." + formElementId + "." + propertyName
          ]);

          set(propertyName, ve.getValue() ? ve.getValue() : []);
        });
      });
    }
  }

  public function getIssues(propertyName:String):Array {
    var issues:Array = get(propertyName);
    return issues ? issues : [];
  }

  public function getAllIssues():Array {
    var allIssues:Array = [];
    var propertyNames:Array = get(PROPERTY_NAMES);
    if (propertyNames) {
      propertyNames.forEach(function (propertyName:String):void {
        var issues:Array = get(propertyName);
        if (issues) {
          allIssues = allIssues.concat(issues);
        }
      });
    }
    return allIssues;
  }


}
}
