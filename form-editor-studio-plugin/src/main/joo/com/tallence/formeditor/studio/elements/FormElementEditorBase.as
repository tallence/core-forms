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
import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;
import com.coremedia.cms.editor.sdk.editorContext;
import com.coremedia.ui.data.ValueExpression;
import com.tallence.formeditor.studio.*;

import ext.container.Container;

public class FormElementEditorBase extends Container {

  public var propertyName:String;
  protected var valueExpression:ValueExpression;
  private var editor:FormEditorDocumentForm;
  public var bindTo:ValueExpression;
  public var group:String;
  protected var forceReadOnlyValueExpression:ValueExpression;
  public var formElementId:String;
  public var setToDefault:Boolean;
  public var structPropertyName:String;
  public var defaultName:String;


  public function FormElementEditorBase(config:FormElementEditorBase = null) {
    config.cls = "applied-form-element-editor";
    super(config);

    setToDefault = config.setToDefault || false;

    propertyName = config.formElementId;
    this.structPropertyName = config.structPropertyName;
  }

  /**
   * It's important that this method is called from the extended afterRender method!
   */
  protected override function afterRender():void {
    // once the default value is written to the remote bean, set the flag to false
    getRemoteValueExpression().addChangeListener(updateSetToDefaultFlag);
  }

  protected static function resolveNameValueExpression(config:FormElementEditorBase):ValueExpression {
  }

  protected static function resolveHintValueExpression(config:FormElementEditorBase):ValueExpression {
  }

  protected static function resolveMandatoryValueExpression(config:FormElementEditorBase):ValueExpression {
  }

  protected static function resolveMaxSizeValueExpression(config:FormElementEditorBase):ValueExpression {
  }

  protected static function resolveMinSizeValueExpression(config:FormElementEditorBase):ValueExpression {
  }


  //noinspection JSMethodCanBeStatic
  /**
   * Override this method if you want to initialize the local bean with some values.
   */
  protected function createLocalModelInitObject():Object {
    return {};
  }

  /**
   * Read the existing form Element expression from it and update it when the expression changes.
   * @return source value expression for the form Element expression (most probably stored in the remote bean)
   */
  protected function getRemoteValueExpression():ValueExpression {
    if (!valueExpression) {
      valueExpression.addChangeListener(formElementChanged);
    }
    return valueExpression;
  }

  /**
   * Event listener when one of the formElements statuses has been changed.
   * We trigger the preview reload manually then.
   */
  private static function formElementChanged():void {
    FormUtils.reloadPreview();
  }

  /* *** Private methods *** */

  private function updateSetToDefaultFlag(valueExpression:ValueExpression):void {
    if (setToDefault && valueExpression.getValue()) {
      setToDefault = false;
      getRemoteValueExpression().removeChangeListener(updateSetToDefaultFlag);
    }
  }

  /**
   * Adds header with a title and a delete button to the formElementEditor.
   *
   * @param title header title
   * @param bindTo the Document
   */
  private function addHeader(title:String, bindTo:ValueExpression):void {

  }

  /**
   * Helper getter.
   * @return FormEditorDocumentForm
   */
  private function getFormEditor():FormEditorDocumentForm {
    if (!editor) {
      editor = this.findParentByType(FormEditorDocumentForm) as FormEditorDocumentForm;
    }
    return editor;
  }

  public override function destroy(...params):void {
    getRemoteValueExpression().removeChangeListener(updateSetToDefaultFlag);
    valueExpression.removeChangeListener(formElementChanged);
    super.destroy();
  }

  /**
   * Initializes the struct the formElement is working on.
   * @param bindTo the document
   * @param formElementType the type of the formElement.
   */
  protected function applyBaseStruct(bindTo:ValueExpression, formElementType:String):void {
    var c:Content = bindTo.getValue();
    var struct:Struct = c.getProperties().get(this.structPropertyName);
    addFormElementsProperty(struct);
  }

  public static function addFormElementsProperty(baseStruct:Struct):void {
  }
}
}
