package com.tallence.formeditor.cae;

import com.coremedia.blueprint.common.services.context.CurrentContextService;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * Provides the locale of the {@link CurrentContextService} in order to keep the consumers of this service CAE independent.
 */
@Component
public class FormCaeLocaleSupplier implements Supplier<Locale> {

  private final CurrentContextService currentContextService;

  public FormCaeLocaleSupplier(CurrentContextService currentContextService) {
    this.currentContextService = currentContextService;
  }

  @Override
  public Locale get() {
    return currentContextService.getContext().getLocale();
  }
}
