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

import session from "@coremedia/studio-client.cap-rest-client/common/session";
import Struct from "@coremedia/studio-client.cap-rest-client/struct/Struct";
import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import Config from "@jangaroo/runtime/Config";
import FormEditorField from "./FormEditorField";
import LinkedContentField from "./LinkedContentField";
import SingleLinkValueExpressionHolder from "./SingleLinkValueExpressionHolder";

interface LinkedContentFieldBaseConfig extends Config<FormEditorField>, Partial<Pick<LinkedContentFieldBase,
  "linkContentType"
>> {
}

class LinkedContentFieldBase extends FormEditorField {
  declare Config: LinkedContentFieldBaseConfig;

  protected static readonly DEFAULT_LINK_CONTENT_TYPE: string = "CMLinkable";

  #linkTargetVE: ValueExpression = null;

  #linkContentType: string = null;

  get linkContentType(): string {
    return this.#linkContentType;
  }

  set linkContentType(value: string) {
    this.#linkContentType = value;
  }

  constructor(config: Config<LinkedContentField> = null) {
    super(config);
  }

  /**
   * Creates the {@link ValueExpression} for the linked content. The value expression needs to be created
   * with the createFromFunction method. Within that function the value expression can be wrapped
   * in a {@link com.coremedia.ui.data.ValueHolder}. After the deletion of the form element, the ValueExpression for the
   * linked content will be undefined. In that case the {@link SingleLinkValueExpressionHolder} will return an empty array,
   * otherwise the single link editor will throw an error.
   * @param config
   * @return
   */
  protected getLinkTargetVE(config: Config<LinkedContentField>): ValueExpression {
    return ValueExpressionFactory.createFromFunction((): any => {
      if (!this.#linkTargetVE) {
        this.#linkTargetVE = this.getPropertyVE(config);
      }
      return new SingleLinkValueExpressionHolder(this.#linkTargetVE);
    });
  }

  /**
   * Initializes the struct for the form element and and adds a link list property to the struct. The link list
   * property field can not automatically add this property.
   * @param struct
   */
  protected override initStruct(struct: Struct): void {
    super.initStruct(struct);
    const contentType = session._.getConnection().getContentRepository().getContentType(this.linkContentType ? this.linkContentType : LinkedContentFieldBase.DEFAULT_LINK_CONTENT_TYPE);
    const formElementsStruct = struct.getType();
    formElementsStruct.addLinkListProperty(this.propertyName, contentType);
  }

}

export default LinkedContentFieldBase;
