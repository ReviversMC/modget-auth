package com.github.reviversmc.modget.auth.oauth;

import com.github.reviversmc.modget.auth.apicalls.DeviceFlowVerifyCodePojo;
import com.github.reviversmc.modget.auth.instance.TokenInstance;

import java.io.IOException;
import java.util.Optional;

public interface DeviceFlowOAuth {

    /**
     * Starts a Device Flow OAuth process.
     *
     * This method does NOT automatically display the verification code to the user.
     * Instead, this method returns a {@link DeviceFlowVerifyCodePojo},
     * which contains the info needed to be shown to a user.
     *
     * @return A {@link DeviceFlowVerifyCodePojo},which contains a verification code
     * required by {@link DeviceFlowOAuth#completeDeviceFlow(DeviceFlowVerifyCodePojo)}
     */
    DeviceFlowVerifyCodePojo startDeviceFlow() throws IOException;

    /**
     * Completes the Device Flow OAuth process.
     *
     * @param deviceFlowVerifyCodePojo Can be obtained from {@link DeviceFlowOAuth#startDeviceFlow()}
     * @return A {@link TokenInstance} that the token is held in.
     * @throws InterruptedException If the thread is interrupted when sleeping.
     * @throws IOException          If the api calls to GitHub fail.
     */
    Optional<TokenInstance> completeDeviceFlow(DeviceFlowVerifyCodePojo deviceFlowVerifyCodePojo)
            throws InterruptedException, IOException;

}
