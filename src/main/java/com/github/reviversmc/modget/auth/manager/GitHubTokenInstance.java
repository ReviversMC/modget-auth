package com.github.reviversmc.modget.auth.manager;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class GitHubTokenInstance implements TokenInstance {

    private final List<String> requiredScopes;
    private String githubToken = null;
    private final OkHttpClient okHttpClient;

    @AssistedInject
    public GitHubTokenInstance(@Assisted List<String> requiredScopes, OkHttpClient okHttpClient) {
        this.requiredScopes = requiredScopes;
        this.okHttpClient = okHttpClient;
    }

    @Override
    public List<String> getGuaranteedScopes() {
        return requiredScopes;
    }

    @Override
    public Optional<String> getToken() {
        if (githubToken != null) return Optional.of(githubToken);
        return Optional.empty();
    }

    @Override
    public boolean setToken(String token) throws IOException {
        if (validateToken(token)) {
            this.githubToken = token;
            return true;
        }
        return false;
    }

    private boolean validateToken(String token) throws IOException {
        Request request = new Request.Builder()
                .addHeader("Authorization", "token " + token)
                .url("https://api.github.com/ratelimit")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        List<String> availableScopes = response.headers("X-OAuth-Scopes");
        response.close();

        for (String required : requiredScopes) {
            if (!availableScopes.contains(required)) return false;
        }

        return true;
    }
}
