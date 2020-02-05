package com.tallence.formeditor.cae.elements;

/**
 * AdvancedSettings for a {@link FormElement} to control behaviours like dependent visibility or the layout.
 *
 */
public class AdvancedSettings {

  private String customId;
  private Integer columnWidth;
  private Boolean breakAfterElement;
  private Boolean visibilityDependent;
  private String dependentElementId;
  private String dependentElementValue;

  public String getCustomId() {
    return customId;
  }

  public void setCustomId(String customId) {
    this.customId = customId;
  }

  public Integer getColumnWidth() {
    return columnWidth;
  }

  public void setColumnWidth(Integer columnWidth) {
    this.columnWidth = columnWidth;
  }

  public Boolean getBreakAfterElement() {
    return breakAfterElement;
  }

  public void setBreakAfterElement(Boolean breakAfterElement) {
    this.breakAfterElement = breakAfterElement;
  }

  public Boolean getVisibilityDependent() {
    return visibilityDependent;
  }

  public void setVisibilityDependent(Boolean visibilityDependent) {
    this.visibilityDependent = visibilityDependent;
  }

  public String getDependentElementId() {
    return dependentElementId;
  }

  public void setDependentElementId(String dependentElementId) {
    this.dependentElementId = dependentElementId;
  }

  public String getDependentElementValue() {
    return dependentElementValue;
  }

  public void setDependentElementValue(String dependentElementValue) {
    this.dependentElementValue = dependentElementValue;
  }
}
