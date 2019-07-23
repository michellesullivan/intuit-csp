package com.zimpatica.intuit.templates;

import com.appian.connectedsystems.templateframework.sdk.ExecutionContext;
import com.appian.connectedsystems.templateframework.sdk.ProxyConfigurationData;

import java.util.Locale;
import java.util.Optional;

public class Test {
    public static void main(String[] args){
        ExecutionContext executionContext = new ExecutionContext() {
            @Override
            public Locale getDesignerLocale() {
                return null;
            }

            @Override
            public Locale getExecutionLocale() {
                return null;
            }

            @Override
            public boolean isDiagnosticsEnabled() {
                return false;
            }

            @Override
            public boolean hasAccessToConnectedSystem() {
                return false;
            }

            @Override
            public ProxyConfigurationData getProxyConfigurationData() {
                return null;
            }

            @Override
            public Optional<String> getAccessToken() {
                return Optional.empty();
            }

            @Override
            public int getAttemptNumber() {
                return 0;
            }
        };
        String accessToken = executionContext.getAccessToken().get();
        Optional<String> optionalAccessToken = executionContext.getAccessToken();
        if(optionalAccessToken.isPresent()) {
            accessToken = optionalAccessToken.get();
        }
        System.out.println(accessToken);
    }
}
