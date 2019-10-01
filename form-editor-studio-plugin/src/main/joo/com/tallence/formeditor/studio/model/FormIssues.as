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
import com.coremedia.cap.common.CapPropertyDescriptor;
import com.coremedia.cap.common.CapPropertyDescriptorType;
import com.coremedia.cap.struct.Struct;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.impl.BeanImpl;

/**
 * Bean which contains all form issues for the respective form element. Used by the
 * {@link com.tallence.formeditor.studio.plugins.ShowFormIssuesPlugin} plugin to display error messages.
 */
public class FormIssues extends BeanImpl {

  private static const PROPERTY_NAMES:String = "propertyNames";

  /**
   * Creates a new bean containing all form issues.
   *
   * @param formElementStruct the form element struct
   * @param structPropertyName a string which holds the document property name of the struct, where the form elements
   * are saved into.
   * @param bindTo the value expression evaluating to the content
   * @param formElementId the id of the form element
   */
  public function FormIssues(formElementStruct:Struct,
                             structPropertyName:String,
                             bindTo:ValueExpression,
                             formElementId:String) {
    var propertyNames:Array = resolveFormElementPropertyNames(formElementStruct);

    if (propertyNames) {
      propertyNames.forEach(function (propertyName:String):void {
        set(PROPERTY_NAMES, propertyNames);
        propertyNames.forEach(function (propertyName:String):void {
          var ve:ValueExpression = bindTo.extendBy([
            "issues",
            "byProperty",
            structPropertyName + ".formElements." + formElementId + "." + propertyName
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

  /**
   * Returns an array of property names. The complete path to the property inside the struct is returned. The path is
   * required by the {@link com.tallence.formeditor.studio.plugins.ShowFormIssuesPlugin} to display the error messages
   * correctly.
   *
   * Example: [
   *  "path",
   *  "path.to",
   *  "path.to.property",
   *  "validator",
   *  "validator.mandatory",
   *  "validator.maxSize",
   *  "validator.minSize",
   *  "type",
   *  "name",
   *  "hint"
   * ]
   */
  private function resolveFormElementPropertyNames(struct:Struct):Array {
    var propertyNames:Array = [];
    struct.getType().getDescriptors().forEach(function (descriptor:CapPropertyDescriptor):void {
      if (isStructDescriptor(descriptor)) {
        propertyNames = propertyNames.concat(getPropertyNames(struct, descriptor.name, descriptor.name));
      } else {
        propertyNames.push(descriptor.name);
      }
    });
    return propertyNames;
  }

  private function getPropertyNames(struct:Struct, subStructPropertyName:String, prefix:String):Array {
    var propertyNames:Array = [];

    // add current property path
    propertyNames.push(prefix);

    // add paths to sub properties
    prefix = prefix + ".";
    var subStruct:Struct = struct.get(subStructPropertyName);

    subStruct.getType().getDescriptors().forEach(function (descriptor:CapPropertyDescriptor):void {
      if (isStructDescriptor(descriptor)) {
        propertyNames = propertyNames.concat(getPropertyNames(subStruct, descriptor.name, prefix + descriptor.name));
      } else {
        propertyNames.push(prefix + descriptor.name);
      }
    });

    return propertyNames;

  }

  /**
   * Returns true if the descriptor is a struct property descriptor. Since struct lists are also descriptors of type
   * {@link CapPropertyDescriptorType.STRUCT}, the maxCardinality and minCardinality are additionally checked.
   * @param descriptor the descriptor to be checked
   * @return true, if it is a struct property descriptor
   */
  private static function isStructDescriptor(descriptor:CapPropertyDescriptor):Boolean {
    return descriptor.type == CapPropertyDescriptorType.STRUCT &&
            descriptor.maxCardinality == 1 &&
            descriptor.minCardinality == 1;
  }


}
}
