package com.tallence.formeditor.studio.elements {
import com.coremedia.ui.data.ValueExpression;
import com.tallence.formeditor.studio.model.FormElementStructWrapper;

public interface FormElement {

  /**
   * Returns the type of the form element.
   */
  function getFormElementType():String;

  /**
   * Returns the icon class of the form element. Each applicable element can be identified by an icon in the form
   * editor.
   */
  function getFormElementIconCls():String;

  /**
   * Returns the group name of the form element. Each applicable element is assigned to a group so that a graphical
   * division can be made in the editor.
   */
  function getFormElementGroup():String;

  /**
   * Updates the struct wrapper. Since the editors of the form elements are reused, it must be possible to update
   * the struct wrapper.
   */
  function updateFormElementStructWrapper(wrapper:FormElementStructWrapper):void;

  /**
   * Returns the struct wrapper representing the form element.
   */
  function getFormElementStructWrapper():FormElementStructWrapper;

  /**
   * Returns a value expression evaluating to the struct of the form element.
   */
  function getFormElementStructVE():ValueExpression;

}
}