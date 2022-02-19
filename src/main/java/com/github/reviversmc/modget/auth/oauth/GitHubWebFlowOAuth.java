package com.github.reviversmc.modget.auth.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.reviversmc.modget.auth.apicalls.OAuthAccessTokenPojo;
import com.github.reviversmc.modget.auth.manager.TokenInstance;
import com.github.reviversmc.modget.auth.manager.TokenInstanceFactory;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GitHubWebFlowOAuth implements WebFlowOAuth {

    private final ObjectMapper jsonMapper;
    private final OkHttpClient okHttpClient;
    private final String clientId = System.getenv("modget_app_client_id");
    private final String clientSecret = System.getenv("modget_app_client_secret");
    private final String scopesInURL;
    private final TokenInstance tokenInstance;


    @AssistedInject
    public GitHubWebFlowOAuth(
            @Assisted List<String> tokenScopes,
            ObjectMapper jsonMapper,
            OkHttpClient okHttpClient,
            TokenInstanceFactory tokenInstanceFactory
    ) {

        this.jsonMapper = jsonMapper;
        this.okHttpClient = okHttpClient;

        StringBuilder urlScopeBuilder = new StringBuilder();
        for (String scope : tokenScopes) {
            urlScopeBuilder.append(scope.replace(":", "%3A")).append("%20");
        }

        scopesInURL = urlScopeBuilder.substring(0, urlScopeBuilder.length() - 3); //Get rid of the last "%20".

        this.tokenInstance = tokenInstanceFactory.create(tokenScopes);
    }


    @Override
    public boolean startWebFlow(String state) throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) return false;

        desktop.browse(
                new URI(
                        "https://github.com/login/oauth/authorize?client_id=" + clientId +
                                "&state=" + state +
                                "&scope=" + scopesInURL
                )
        );
        return true;
    }

    @Override
    public Optional<TokenInstance> completeWebFlow(String code) throws IOException {

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json")
                .url(
                        "https://github.com/login/oauth/access_token?client_id=" + clientId +
                                "&client_secret" + clientSecret +
                                "&code" + code
                )
                .build();

        Response response = okHttpClient.newCall(request).execute();

        OAuthAccessTokenPojo accessTokenPojo = jsonMapper.readValue(
                Objects.requireNonNull(response.body()).string(),
                OAuthAccessTokenPojo.class
        );

        response.close();

        if (accessTokenPojo.getError() != null) return Optional.empty();

        return tokenInstance.setToken(accessTokenPojo.getAccessToken()) ?
                Optional.of(tokenInstance) : Optional.empty();

    }
}
