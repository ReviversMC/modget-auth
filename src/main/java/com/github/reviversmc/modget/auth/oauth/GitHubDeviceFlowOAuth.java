package com.github.reviversmc.modget.auth.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.reviversmc.modget.auth.apicalls.OAuthAccessTokenPojo;
import com.github.reviversmc.modget.auth.apicalls.DeviceFlowVerifyCodePojo;
import com.github.reviversmc.modget.auth.instance.TokenInstance;
import com.github.reviversmc.modget.auth.instance.TokenInstanceFactory;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GitHubDeviceFlowOAuth implements DeviceFlowOAuth {

    private final ObjectMapper jsonMapper;
    private final OkHttpClient okHttpClient;
    private final String clientId;
    private final String scopesInURL;
    private final TokenInstance tokenInstance;

    @AssistedInject
    public GitHubDeviceFlowOAuth(
            @Assisted List<String> tokenScopes,
            ObjectMapper jsonMapper,
            OkHttpClient okHttpClient,
            @Assisted("githubAppClientId") String clientId,
            TokenInstanceFactory tokenInstanceFactory
    ) {
        this.jsonMapper = jsonMapper;
        this.okHttpClient = okHttpClient;
        this.clientId = clientId;
        this.tokenInstance = tokenInstanceFactory.create(tokenScopes);

        StringBuilder urlScopeBuilder = new StringBuilder();
        for (String scope : tokenScopes) {
            urlScopeBuilder.append(scope.replace(":", "%3A")).append("%20");
        }

        scopesInURL = urlScopeBuilder.substring(0, urlScopeBuilder.length() - 3); //Get rid of the last "%20".
    }

    @Override
    public DeviceFlowVerifyCodePojo startDeviceFlow() throws IOException {
        RequestBody verifyCodeRequestBody = new FormBody.Builder()
                .add("client_id", clientId)
                .add("scope", scopesInURL)
                .build();

        Request verifyCodeRequest = new Request.Builder()
                .addHeader("Accept", "application/json")
                .post(verifyCodeRequestBody)
                .url("https://github.com/login/device/code")
                .build();


        Response verifyCodeResponse = okHttpClient.newCall(verifyCodeRequest).execute();

        DeviceFlowVerifyCodePojo deviceFlowVerifyCodePojo = jsonMapper.readValue(
                Objects.requireNonNull(verifyCodeResponse.body()).string(),
                DeviceFlowVerifyCodePojo.class
        );
        verifyCodeResponse.close();
        return deviceFlowVerifyCodePojo;

    }

    @Override
    public Optional<TokenInstance> completeDeviceFlow(DeviceFlowVerifyCodePojo deviceFlowVerifyCodePojo)
            throws InterruptedException, IOException {

        RequestBody accessTokenRequestBody = new FormBody.Builder()
                .add("client_id", clientId)
                .add("device_code", deviceFlowVerifyCodePojo.getDeviceCode())
                .add("grant_type", "urn:ietf:params:oauth:grant-type:device_code")
                .build();

        Request accessTokenRequest = new Request.Builder()
                .addHeader("Accept", "application/json")
                .post(accessTokenRequestBody)
                .url("https://github.com/login/oauth/access_token")
                .build();


        while (true) {
            //noinspection BusyWait, expected behaviour
            Thread.sleep(deviceFlowVerifyCodePojo.getInterval() * 1000L + 1000L);

            Response accessTokenResponse = okHttpClient.newCall(accessTokenRequest).execute();

            OAuthAccessTokenPojo accessTokenPojo = jsonMapper.readValue(
                    Objects.requireNonNull(accessTokenResponse.body()).string(),
                    OAuthAccessTokenPojo.class
            );

            accessTokenResponse.close();

            if (accessTokenPojo.getError() != null) {

                switch (accessTokenPojo.getError()) {

                    case "expired_token":
                        //Not checking for unsupported_grant_type.
                    case "access_denied":
                        return Optional.empty();

                    case "slow_down":
                        //noinspection BusyWait, expected behaviour.
                        Thread.sleep(5500L);

                    default:
                        continue;
                }
            }

            return tokenInstance.setToken(accessTokenPojo.getAccessToken()) ?
                    Optional.of(tokenInstance) : Optional.empty();

        }
    }
}
