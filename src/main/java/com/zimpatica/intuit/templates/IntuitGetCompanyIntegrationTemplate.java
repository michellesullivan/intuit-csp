package com.zimpatica.intuit.templates;

import java.util.HashMap;
import java.util.Map;

import com.appian.connectedsystems.simplified.sdk.SimpleIntegrationTemplate;
import com.appian.connectedsystems.simplified.sdk.configuration.SimpleConfiguration;
import com.appian.connectedsystems.templateframework.sdk.ExecutionContext;
import com.appian.connectedsystems.templateframework.sdk.IntegrationResponse;
import com.appian.connectedsystems.templateframework.sdk.TemplateId;
import com.appian.connectedsystems.templateframework.sdk.configuration.PropertyPath;
import com.appian.connectedsystems.templateframework.sdk.diagnostics.IntegrationDesignerDiagnostic;
import com.appian.connectedsystems.templateframework.sdk.metadata.IntegrationTemplateRequestPolicy;
import com.appian.connectedsystems.templateframework.sdk.metadata.IntegrationTemplateType;
import java.util.List;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.core.ServiceType;
import com.intuit.ipp.data.CompanyInfo;
import com.intuit.ipp.data.Error;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.exception.InvalidTokenException;
import com.intuit.ipp.security.OAuth2Authorizer;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.services.QueryResult;
import com.intuit.ipp.util.Config;
import java.util.Optional;
import com.google.common.base.Stopwatch;

// Must provide an integration id. This value need only be unique for this connected system
@TemplateId(name="IntuitGetCompanyIntegrationTemplate")
// Set template type to READ since this integration does not have side effects
@IntegrationTemplateType(IntegrationTemplateRequestPolicy.READ)
public class IntuitGetCompanyIntegrationTemplate extends SimpleIntegrationTemplate {

  public static final String REALM_ID = "realmID";
  private static final String URL_1 = "/v3/company/4611879532804685208/companyinfo/";
  private static final String URL_2 = "?minorversion=38";

  @Override
  protected SimpleConfiguration getConfiguration(
    SimpleConfiguration integrationConfiguration,
    SimpleConfiguration connectedSystemConfiguration,
    PropertyPath propertyPath,
    ExecutionContext executionContext) {
    return integrationConfiguration.setProperties(
        // Make sure you make constants for all keys so that you can easily
        // access the values during execution
        textProperty(REALM_ID).label("Realm ID")
            .isRequired(true)
            .description("Company Thing")
            .build());
  }

  @Override
  protected IntegrationResponse execute(
      SimpleConfiguration integrationConfiguration,
      SimpleConfiguration connectedSystemConfiguration,
      ExecutionContext executionContext) {

    String accessToken = executionContext.getAccessToken().get();
    Optional<String> optionalAccessToken = executionContext.getAccessToken();
    if(optionalAccessToken.isPresent()) {
      accessToken = optionalAccessToken.get();
    }
    String realmId = integrationConfiguration.getValue(REALM_ID);
    String baseUrl = "https://sandbox-quickbooks.api.intuit.com";
    IntegrationDesignerDiagnostic.IntegrationDesignerDiagnosticBuilder diagnosticBuilder =  IntegrationDesignerDiagnostic.builder();
    Stopwatch stopwatch = Stopwatch.createStarted();

    String url = baseUrl + URL_1 + realmId + URL_2;
    String failureMsg = "Failed";
    QueryResult queryResult;
    try {
      Config.setProperty(Config.BASE_URL_QBO, url);
      //get DataService
      DataService service = getDataService(realmId, accessToken);
      // get all companyinfo

      String sql = "select * from companyinfo";

      queryResult = service.executeQuery(sql);


    }
    catch (InvalidTokenException e){
      Map<String,Object> requestDiagnostics = getRequestDiagnostics(
              connectedSystemConfiguration, integrationConfiguration);
      return IntegrationExecutionUtils.handleException(e, diagnosticBuilder,
              requestDiagnostics, stopwatch);
    }
    catch (FMSException e) {

      List<Error> list = e.getErrorList();
      Map<String,Object> requestDiagnostics = getRequestDiagnostics(
              connectedSystemConfiguration, integrationConfiguration);

      return IntegrationExecutionUtils.handleFMSException(diagnosticBuilder,
              requestDiagnostics, stopwatch);

    }
    Map<String,Object> requestDiagnostics = getRequestDiagnostics(
            connectedSystemConfiguration, integrationConfiguration);

    Map<String,Object> response =  processResponse(failureMsg, queryResult);
    return IntegrationResponse
            .forSuccess(response)
            .build();


  }
  private Map<String,Object> processResponse(String failureMsg, QueryResult queryResult) {

    Map<String,Object> result = new HashMap<>();
    if (!queryResult.getEntities().isEmpty() && queryResult.getEntities().size() > 0) {

      CompanyInfo companyInfo = (CompanyInfo) queryResult.getEntities().get(0);



      result.put("companyInfo", companyInfo);

      return result;



    }

    result.put("failure", "failure message");
    return result;

  }

  private DataService getDataService(String realmId, String accessToken) throws FMSException {

    //create oauth object

    OAuth2Authorizer oauth = new OAuth2Authorizer(accessToken);

    //create context

    Context context = new Context(oauth, ServiceType.QBO, realmId);



    // create dataservice

    return new DataService(context);

  }
  private static Map<String,Object> getRequestDiagnostics(SimpleConfiguration connectedSystemConfiguration, SimpleConfiguration integrationConfiguration) {
    Map<String,Object> requestDiagnostics = IntegrationExecutionUtils.getRequestDiagnostics(connectedSystemConfiguration);
    String realmId = integrationConfiguration.getValue(REALM_ID);
    requestDiagnostics.put("Realm ID", realmId);
    return requestDiagnostics;
  }
}
