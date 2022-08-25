/*
 * Copyright 2020 Tallence AG
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

package com.tallence.formeditor.cae.model;

import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.elements.PageElement;

import java.util.Collections;
import java.util.List;

public class FormEditorConfig {

  private String formActionUrl;

  private List<Page> pages;

  public String getFormActionUrl() {
    return formActionUrl;
  }

  public void setFormActionUrl(String formActionUrl) {
    this.formActionUrl = formActionUrl;
  }

  public void setPages(List<Page> pages) {
    this.pages = pages;
  }

  public void setPage(Page page) {
    this.pages = Collections.singletonList(page);
  }

  public List<Page> getPages() {
    return pages;
  }

  public static class Page {

    private final String id;
    private final String description;
    private final String title;
    private final PageElement.PageType pageType;
    private final List<FormElement<?>> formElements;

    public Page(List<FormElement<?>> formElements) {
      this.id = null;
      this.description = null;
      this.title = null;
      this.pageType = PageElement.PageType.DEFAULT_PAGE;
      this.formElements = formElements;
    }

    public Page(String id, String description, String title, PageElement.PageType pageType, List<FormElement<?>> formElements) {
      this.id = id;
      this.description = description;
      this.title = title;
      this.pageType = pageType;
      this.formElements = formElements;
    }

    public String getId() {
      return id;
    }

    public String getDescription() {
      return description;
    }

    public String getTitle() {
      return title;
    }

    public PageElement.PageType getPageType() {
      return pageType;
    }

    public List<FormElement<?>> getFormElements() {
      return formElements;
    }
  }
}
