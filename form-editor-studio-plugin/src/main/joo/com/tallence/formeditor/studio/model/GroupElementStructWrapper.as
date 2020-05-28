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

public class GroupElementStructWrapper {

  public static const PROP_CHECKED:String = "checkedByDefault";
  private static const PROP_VALUE:String = "value";

  private var groupElementVE:ValueExpression;
  private var id:String;

  public function GroupElementStructWrapper(groupElementVE:ValueExpression, name:String, onSelectionChangeCallback:Function) {
    this.groupElementVE = groupElementVE;
    this.id = name;

    getOptionSelectedByDefaultVE().addChangeListener(function():void {
        if (onSelectionChangeCallback) {
          onSelectionChangeCallback.call(NaN, name, getOptionSelectedByDefaultVE().getValue())
        }
    });
  }

  public function getId():String {
    return id;
  }

  public function getGroupElementVE():ValueExpression {
    return groupElementVE;
  }

  public function getOptionSelectedByDefaultVE():ValueExpression {
    return getGroupElementVE().extendBy(PROP_CHECKED);
  }

  public function getOptionValueVE():ValueExpression {
    return getGroupElementVE().extendBy(PROP_VALUE);
  }


}
}
