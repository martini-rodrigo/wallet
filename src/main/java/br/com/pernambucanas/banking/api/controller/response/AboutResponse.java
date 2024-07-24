package br.com.pernambucanas.banking.api.controller.response;

public class AboutResponse {

    private final String version;

    public AboutResponse() {
        this.version = getAppVersion();
    }

    public String getVersion() {
        return version;
    }

    private static final String VERSION_APP = "1.0.0";

    private String getAppVersion() {
        return VERSION_APP;
    }
}