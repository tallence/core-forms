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

package com.tallence.formeditor.studio {
import com.coremedia.cms.editor.sdk.premular.DocumentForm;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;

public class FormEditorDocumentFormBase extends DocumentForm {

  private var dragActiveVE:ValueExpression;

  public function FormEditorDocumentFormBase(config:FormEditorDocumentForm = null) {
    super(config);
  }

  /**
   * Stores the information whether a drag and drop operation is in progress. The ValueExpression must be created in
   * the document form, because the drag/drop state must also be updated for the applicable elements.
   */
  protected function getDragActiveVE():ValueExpression {
    if (!dragActiveVE) {
      dragActiveVE = ValueExpressionFactory.createFromValue(false);
    }
    return dragActiveVE;
  }

}
}