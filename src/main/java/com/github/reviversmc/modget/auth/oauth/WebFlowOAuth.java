package com.github.reviversmc.modget.auth.oauth;

import com.github.reviversmc.modget.auth.instance.TokenInstance;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

public interface WebFlowOAuth {

    /**
     * Starts a web flow OAuth process.
     *
     * @param state An unguessable random string used to authenticate the request.
     * @return The URL to be used to start the web flow.
     * @throws MalformedURLException If the URL formation fails.
     */
    URL startWebFlow(String state) throws MalformedURLException;

    /**
     * Completes a web flow OAuth process.
     *
     * @param code The code provided by the callback from {@link WebFlowOAuth#startWebFlow(String)}.
     *             The code is NOT provided by {@link WebFlowOAuth#startWebFlow(String)}.
     *             The callback url should receive the code, which is picked up by a server.
     * @return An {@link Optional} that the token is held in, if successful.
     * @throws IOException If the completion fails.
     */
    Optional<TokenInstance> completeWebFlow(String code) throws IOException;

}
