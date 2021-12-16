import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import FormElementStructWrapper from "../model/FormElementStructWrapper";

abstract class FormElement {

  /**
   * Returns the type of the form element.
   */
  abstract getFormElementType(): string;

  /**
   * Returns the icon class of the form element. Each applicable element can be identified by an icon in the form
   * editor.
   */
  abstract getFormElementIconCls(): string;

  /**
   * Returns the group name of the form element. Each applicable element is assigned to a group so that a graphical
   * division can be made in the editor.
   */
  abstract getFormElementGroup(): string;

  /**
   * Updates the struct wrapper. Since the editors of the form elements are reused, it must be possible to update
   * the struct wrapper.
   */
  abstract updateFormElementStructWrapper(wrapper: FormElementStructWrapper): void;

  /**
   * Returns the struct wrapper representing the form element.
   */
  abstract getFormElementStructWrapper(): FormElementStructWrapper;

  /**
   * Returns a value expression evaluating to the struct of the form element.
   */
  abstract getFormElementStructVE(): ValueExpression;

}

export default FormElement;
