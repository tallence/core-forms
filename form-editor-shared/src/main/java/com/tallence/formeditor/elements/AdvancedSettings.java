package com.tallence.formeditor.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * AdvancedSettings for a {@link FormElement} to control behaviours like dependent visibility or the layout.
 *
 */
public class AdvancedSettings {

  private String customId;
  private Integer columnWidth;
  private boolean breakAfterElement;
  private boolean visibilityDependent;
  private String dependentElementId;
  private List<String> dependentElementValues = new ArrayList<>();

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

  public boolean isBreakAfterElement() {
    return breakAfterElement;
  }

  public void setBreakAfterElement(boolean breakAfterElement) {
    this.breakAfterElement = breakAfterElement;
  }

  public boolean isVisibilityDependent() {
    return visibilityDependent;
  }

  public void setVisibilityDependent(boolean visibilityDependent) {
    this.visibilityDependent = visibilityDependent;
  }

  public String getDependentElementId() {
    return dependentElementId;
  }

  public void setDependentElementId(String dependentElementId) {
    this.dependentElementId = dependentElementId;
  }

  /**
   * @deprecated use {@link #getDependentElementValues} instead
   */
  @Deprecated
  public String getDependentElementValue() {
    return dependentElementValues.isEmpty() ? null : dependentElementValues.get(0);
  }

  /**
   * @deprecated use {@link #setDependentElementValues} instead
   */
  @Deprecated
  public void setDependentElementValue(String dependentElementValue) {
    this.dependentElementValues = Collections.singletonList(dependentElementValue);
  }

  public List<String> getDependentElementValues() {
    return ofNullable(dependentElementValues).orElse(Collections.emptyList());
  }

  public void setDependentElementValues(String dependentElementValue) {
    this.dependentElementValues = ofNullable(dependentElementValue)
            .map(d -> Arrays.asList(d.split(";")))
            .orElse(Collections.emptyList());
  }

}
