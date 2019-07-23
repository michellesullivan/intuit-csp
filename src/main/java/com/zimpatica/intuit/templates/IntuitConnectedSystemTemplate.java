package com.zimpatica.intuit.templates;

import com.appian.connectedsystems.simplified.sdk.configuration.SimpleConfiguration;
import com.appian.connectedsystems.simplified.sdk.oauth.SimpleOAuthConnectedSystemTemplate;
import com.appian.connectedsystems.templateframework.sdk.ExecutionContext;
import com.appian.connectedsystems.templateframework.sdk.TemplateId;
import com.appian.connectedsystems.templateframework.sdk.oauth.OAuthConfigurationData;

@TemplateId(name="IntuitConnectedSystemTemplate")
public class IntuitConnectedSystemTemplate extends SimpleOAuthConnectedSystemTemplate {

  public static final String CLIENT_ID_KEY = "clientId";
  public static final String CLIENT_SECRET_KEY = "clientSecret";
  public static final String AUTH_URL = "https://appcenter.intuit.com/connect/oauth2";
  public static final String TOKEN_URL = "https://oauth.platform.intuit.com/oauth2/v1/tokens/bearer";
  public static final String SCOPE = "com.intuit.quickbooks.accounting openid profile email phone address";
  public static final String BASE_URL = "baseUrl";

  @Override
  protected SimpleConfiguration getConfiguration(
      SimpleConfiguration simpleConfiguration, ExecutionContext executionContext) {

    return simpleConfiguration.setProperties(
        // Make sure you make public constants for all keys so that associated
        // integrations can easily access this field
        textProperty(CLIENT_ID_KEY)
            .label("Client ID")
            .isImportCustomizable(true)
            .build(),
        encryptedTextProperty(CLIENT_SECRET_KEY).label("Client Secret").isImportCustomizable(true).build(),
        textProperty(BASE_URL).label("Base URL").isImportCustomizable(true).isRequired(true).build()
    );
  }

  @Override
  protected OAuthConfigurationData getOAuthConfiguration(SimpleConfiguration simpleConfiguration) {
    //AuthUrl is provided in documentation at @see <a href="https://developers.google.com/identity/protocols/OAuth2InstalledApp#step-2-send-a-request-to-googles-oauth-20-server">https://developers.google.com/identity/protocols/OAuth2InstalledApp#step-2-send-a-request-to-googles-oauth-20-server</a>
    //OAuth Scope URL is provided in documentation at @see <a href="https://developers.google.com/identity/protocols/googlescopes#drivev3">https://developers.google.com/identity/protocols/googlescopes#drivev3</a>
    //Token URL is provided in documentation at @see <a href="https://developers.google.com/identity/protocols/OAuth2InstalledApp#exchange-authorization-code">https://developers.google.com/identity/protocols/OAuth2InstalledApp#exchange-authorization-code</a>
    return OAuthConfigurationData.builder()
            .authUrl(AUTH_URL)
            .clientId(simpleConfiguration.getValue(CLIENT_ID_KEY))
            .clientSecret(simpleConfiguration.getValue(CLIENT_SECRET_KEY))
            .scope(SCOPE)
            .tokenUrl(TOKEN_URL)
            .build();
  }
}
