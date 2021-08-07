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

package com.tallence.formeditor.cae.parser;

import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.cae.elements.FileUpload;
import com.tallence.formeditor.cae.validator.FileUploadValidator;
import org.springframework.stereotype.Component;

import static com.coremedia.cap.util.CapStructUtil.*;
import static java.util.Optional.ofNullable;

/**
 * Parser for elements of type {@link FileUpload}
 */
@Component
public class FileUploadParser extends AbstractFormElementParser<FileUpload> {

  public static final String parserKey = "FileUpload";


  @Override
  public FileUpload instantiateType(Struct elementData) {
    return new FileUpload();
  }


  @Override
  public void parseSpecialFields(FileUpload formElement, Struct elementData) {
    ofNullable(getSubstruct(elementData, FORM_DATA_VALIDATOR)).ifPresent(validator -> {
      FileUploadValidator fileUploadValidator = new FileUploadValidator(formElement);
      fileUploadValidator.setMandatory(getBoolean(validator, FORM_VALIDATOR_MANDATORY));

      ofNullable(getInteger(validator, FORM_VALIDATOR_MINSIZE)).ifPresent(fileUploadValidator::setMinSize);
      ofNullable(getInteger(validator, FORM_VALIDATOR_MAXSIZE)).ifPresent(fileUploadValidator::setMaxSize);
      formElement.setValidator(fileUploadValidator);
    });
  }

  @Override
  public String getParserKey() {
    return parserKey;
  }
}
