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

package com.tallence.formeditor.cae.serializer;

import com.coremedia.blueprint.common.contentbeans.CMTeasable;
import com.fasterxml.jackson.core.JsonGenerator;
import com.tallence.formeditor.cae.elements.ConsentFormCheckBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.tallence.formeditor.cae.parser.ConsentFormCheckBoxParser.FORM_LINK_TARGET;

/**
 * Serializer for the {@link ConsentFormCheckBox}, taking care of linkBuilding for the targetUrl.
 */
class ConsentFormCheckBoxSerializer extends AbstractFormElementSerializer<ConsentFormCheckBox> {

  private static final Logger LOG = LoggerFactory.getLogger(ConsentFormCheckBoxSerializer.class);

  private final Function<CMTeasable, String> linkBuilder;

  ConsentFormCheckBoxSerializer(BiFunction<String, Object[], String> messageResolver, ValidationSerializationHelper validationSerializationHelper, Function<CMTeasable, String> linkBuilder) {
    super(ConsentFormCheckBox.class, messageResolver, validationSerializationHelper);
    this.linkBuilder = linkBuilder;
  }

  @Override
  public void serializeTypeSpecificFields(ConsentFormCheckBox field, JsonGenerator gen) throws IOException {
    try {
        gen.writeStringField(FORM_LINK_TARGET, linkBuilder.apply(field.getLinkTarget()));
    } catch (Exception x) {
        gen.writeStringField(FORM_LINK_TARGET, "#");
        LOG.warn("cannot build link for linked content of field element [{}]", field.getTechnicalName());
    }
  }
}
