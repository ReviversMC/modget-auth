package com.github.reviversmc.modget.auth.manager;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;

import java.util.List;

@AssistedFactory
public interface TokenInstanceFactory {

    GitHubTokenInstance create(@Assisted List<String> requiredScopes);

}
