# Modget Auth

This project provides the authentication tools necessary to get a GitHub token.

To use it, add jitpack to the end of your `build.gradle` repositories: 

```gradle
repositories {
    ...
    maven {
        url = "https://jitpack.io"
    }
}
```

And then add modget-auth to your dependencies:
```gradle
implementation "com.github.ReviversMC:modget-auth:${modget_auth_version}"
```

