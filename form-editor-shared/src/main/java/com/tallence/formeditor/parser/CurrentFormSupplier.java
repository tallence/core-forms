package com.tallence.formeditor.parser;

import com.coremedia.cap.content.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

/**
 * Provides the current form content based on the {@link RequestContextHolder}.
 * Similar to com.coremedia.blueprint.cae.constants.RequestAttributeConstants
 */
@Component
public class CurrentFormSupplier implements Supplier<Content> {

  private static final Logger LOG = LoggerFactory.getLogger(CurrentFormSupplier.class);

  private static final String CURRENT_FORM = "currentForm";

  @Override
  public Content get() {

    var currentForm = (Content) ofNullable(RequestContextHolder.getRequestAttributes())
            .map(a -> a.getAttribute(CURRENT_FORM, RequestAttributes.SCOPE_REQUEST))
            .filter(l -> l instanceof Content)
            .orElse(null);

    if (currentForm == null) {
      LOG.error("No current form set into the request! Cannot parse all form fields.");
    }
    return currentForm;
  }

  public static void setCurrentFormLocale(Content currentForm) {
    ofNullable(RequestContextHolder.getRequestAttributes())
            .orElseThrow(() -> new IllegalStateException("No RequestAttributes available, cannot set the currect form"))
            .setAttribute(CURRENT_FORM, currentForm, RequestAttributes.SCOPE_REQUEST);
  }
}
