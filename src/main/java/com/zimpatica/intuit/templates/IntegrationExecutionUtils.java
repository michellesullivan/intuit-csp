package com.zimpatica.intuit.templates;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.appian.connectedsystems.simplified.sdk.configuration.SimpleConfiguration;
import com.appian.connectedsystems.templateframework.sdk.IntegrationError;
import com.appian.connectedsystems.templateframework.sdk.IntegrationResponse;
import com.appian.connectedsystems.templateframework.sdk.diagnostics.IntegrationDesignerDiagnostic;
import com.appian.connectedsystems.templateframework.sdk.oauth.ExpiredTokenException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.drive.model.File;
import com.google.common.base.Stopwatch;
import com.intuit.ipp.exception.InvalidTokenException;

public class IntegrationExecutionUtils {
    private IntegrationExecutionUtils() {
    }

    /**
     * Creates common fields of diagnostics objects for both Send File and Create Folder templates.
     * Diagnostic is information that will be displayed in the Request and Response tabs. You can
     * include information that is helpful for the developer to debug, such as HTTP parameters.
     */
    public static Map<String,Object> getRequestDiagnostics(SimpleConfiguration connectedSystemConfiguration) {
        Map<String,Object> requestDiagnostic = new HashMap<>();
        //Request Diagnostic values will be shown on the Request tab on Appian Integration Designer Interface,
        //which will be visible to designers. Only add to diagnostics values that you wish the designer to see.
        String clientId = connectedSystemConfiguration.getValue(
                IntuitConnectedSystemTemplate.CLIENT_ID_KEY);
        requestDiagnostic.put(IntuitConnectedSystemTemplate.CLIENT_ID_KEY, clientId);
        //For sensitive values, mask it so that it won't be visible to designers
        requestDiagnostic.put(IntuitConnectedSystemTemplate.CLIENT_SECRET_KEY, "******************");
        return requestDiagnostic;
    }


    /**
     * Handles Google's {@link GoogleJsonResponseException}. You can add custom logic for handling
     * specific errors, such as building a IntegrationResponse with different error messages for a
     * particular error code.
     */
    public static IntegrationResponse handleException(
            InvalidTokenException e,
            IntegrationDesignerDiagnostic.IntegrationDesignerDiagnosticBuilder diagnosticBuilder,
            Map<String,Object> requestDiagnostics,
            Stopwatch stopwatch) {
        if (true) {
            //Google returns a 401 exception if your credential is not authorized or expired. Throw an
            //ExpiredTokenException when this happens, Appian will try to refresh the token.
            throw new ExpiredTokenException();
        }
        long elapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        IntegrationDesignerDiagnostic diagnostic = diagnosticBuilder.addExecutionTimeDiagnostic(elapsed)
                .addRequestDiagnostic(requestDiagnostics)
                .build();

        IntegrationError error = IntegrationError.builder()
                .title("Exception 401: ")
                .message(e.getMessage())
                .build();

        return IntegrationResponse.forError(error).withDiagnostic(diagnostic).build();
    }
    public static IntegrationResponse handleFMSException(
            IntegrationDesignerDiagnostic.IntegrationDesignerDiagnosticBuilder diagnosticBuilder,
            Map<String,Object> requestDiagnostics,
            Stopwatch stopwatch) {
        if (true) {
            //Google returns a 401 exception if your credential is not authorized or expired. Throw an
            //ExpiredTokenException when this happens, Appian will try to refresh the token.
            throw new ExpiredTokenException();
        }
        long elapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        IntegrationDesignerDiagnostic diagnostic = diagnosticBuilder.addExecutionTimeDiagnostic(elapsed)
                .addRequestDiagnostic(requestDiagnostics)
                .build();

        IntegrationError error = IntegrationError.builder()
                .title("FMS")
                .message("FMS Blach Blach ")
                .build();

        return IntegrationResponse.forError(error).withDiagnostic(diagnostic).build();
    }
}
