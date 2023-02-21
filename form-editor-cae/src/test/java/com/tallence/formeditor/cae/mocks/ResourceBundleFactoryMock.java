package com.tallence.formeditor.cae.mocks;

import com.coremedia.blueprint.cae.web.i18n.LinklistPageResourceBundleFactory;
import com.coremedia.blueprint.coderesources.ThemeService;
import com.coremedia.blueprint.common.contentbeans.Page;
import com.coremedia.blueprint.common.navigation.Navigation;
import com.coremedia.blueprint.localization.LocalizationService;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.cap.user.User;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class ResourceBundleFactoryMock extends LinklistPageResourceBundleFactory {

  public ResourceBundleFactoryMock(SitesService sitesService, LocalizationService localizationService, ThemeService themeService) {
    setLocalizationService(localizationService);
    setSitesService(sitesService);
    setThemeService(themeService);
  }

  @Override
  public ResourceBundle resourceBundle(Page page, @Nullable User developer) {
    return resourceBundle(page.getNavigation(), developer);
  }

  @Override
  public ResourceBundle resourceBundle(Navigation navigation, @Nullable User developer) {

    return new ResourceBundle() {
      @Override
      protected Object handleGetObject(String key) {
        return "mockedValue, arg1: {0}, arg2: {1}";
      }

      @Override
      public Enumeration<String> getKeys() {
        return Collections.enumeration(Collections.singletonList("mockedValue"));
      }
    };
  }
}
