package com.github.reviversmc.modget.auth.manager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TokenInstance {

    /**
     * Gets the scopes guaranteed by this instance, as long as there is a token.
     * @return A {@link List<String>} of guaranteed scopes for a taken.
     */
    List<String> getGuaranteedScopes();

    /**
     * Gets the GitHub token of a user, if present.
     *
     * @return An {@link Optional} with the GitHub token, or an empty {@link Optional}
     */
    Optional<String> getToken();

    /**
     * Sets the GitHub token of a user. Validates the token before setting it.
     *
     * @param token The token to store in memory.
     * @return False if token is invalid or contains insufficient perms, true otherwise.
     * @throws IOException If the api call to validate the token fails.
     */
    boolean setToken(String token) throws IOException;

}
