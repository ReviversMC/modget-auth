package com.github.reviversmc.modget.auth.apicalls;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Meant for CLI operations
 */
public class DeviceFlowVerifyCodePojo {

    private String deviceCode;
    private String userCode;
    private String verificationUri;
    private int expiresIn;
    private int interval;

    private String error;
    private String errorDescription;
    private String errorUri;

    public DeviceFlowVerifyCodePojo() {

    }

    @JsonProperty("device_code")
    public String getDeviceCode() {
        return deviceCode;
    }

    @JsonProperty("device_code")
    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    @JsonProperty("user_code")
    public String getUserCode() {
        return userCode;
    }

    @JsonProperty("user_code")
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @JsonProperty("verification_uri")
    public String getVerificationUri() {
        return verificationUri;
    }

    @JsonProperty("verification_uri")
    public void setVerificationUri(String verificationUri) {
        this.verificationUri = verificationUri;
    }

    @JsonProperty("expires_in")
    public int getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("expires_in")
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @JsonProperty("error_description")
    public String getErrorDescription() {
        return errorDescription;
    }

    @JsonProperty("error_description")
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @JsonProperty("error_uri")
    public String getErrorUri() {
        return errorUri;
    }

    @JsonProperty("error_uri")
    public void setErrorUri(String errorUri) {
        this.errorUri = errorUri;
    }
}
