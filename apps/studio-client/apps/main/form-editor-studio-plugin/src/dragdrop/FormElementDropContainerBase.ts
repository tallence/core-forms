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
import Container from "@jangaroo/ext-ts/container/Container";
import DropTarget from "@jangaroo/ext-ts/dd/DropTarget";
import {as} from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import FormElementsManager from "../helper/FormElementsManager";
import FormElementDropContainer from "./FormElementDropContainer";
import FormElementDropTarget from "./FormElementDropTarget";

interface FormElementDropContainerBaseConfig extends Config<Container> {
}

class FormElementDropContainerBase extends Container {
  declare Config: FormElementDropContainerBaseConfig;

  static readonly MOVE_TARGET_DD_GROUP: string = "AppliedElementDD";

  static readonly TARGET_MODE_ADD: string = "add";

  static readonly TARGET_MODE_MOVE: string = "move";

  #dropTarget: DropTarget = null;

  #formElementsManager: FormElementsManager = null;

  #formElementId: string = null;

  #forceReadOnlyValueExpression: ValueExpression = null;

  #dropActiveVE: ValueExpression = null;

  constructor(config: Config<FormElementDropContainer> = null) {
    super(config);
    this.#formElementsManager = config.formElementsManager;
    this.#formElementId = config.formElementId;
    this.#forceReadOnlyValueExpression = config.forceReadOnlyValueExpression;
  }

  protected override afterRender(): void {
    super.afterRender();
    this.#dropTarget = new FormElementDropTarget(
            this.#formElementsManager,
            this.getDropActiveVE(),
            this.#formElementId,
            this.#forceReadOnlyValueExpression,
            this.getEl(),
            Config(DropTarget, {
              ddGroup: FormElementDropContainerBase.MOVE_TARGET_DD_GROUP,
            }));
  }

  override destroy(...params): void {
    this.#dropTarget.unreg();
    super.destroy(params);
  }

  protected getModifiers(config: Config<FormElementDropContainer>): ValueExpression {
    return ValueExpressionFactory.createFromFunction((): Array<any> => {
      const modifiers = [];
      if (config.formElementsManager.getDragActiveVE().getValue()) {
        modifiers.push("drag-active");
      }
      if (this.getDropActiveVE().getValue()) {
        modifiers.push("drop-active");
      }
      if (as(config.formElementsManager.getFormElementsVE().getValue(), Array).length == 0) {
        modifiers.push("empty");
      }
      return modifiers;
    });
  }

  protected getDropActiveVE(): ValueExpression {
    if (!this.#dropActiveVE) {
      this.#dropActiveVE = ValueExpressionFactory.createFromValue(false);
    }
    return this.#dropActiveVE;
  }
}

export default FormElementDropContainerBase;
