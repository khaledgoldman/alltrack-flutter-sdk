package com.alltrack.sdk.flutter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.alltrack.sdk.Alltrack;
import com.alltrack.sdk.AlltrackAdRevenue;
import com.alltrack.sdk.AlltrackAttribution;
import com.alltrack.sdk.AlltrackConfig;
import com.alltrack.sdk.AlltrackEvent;
import com.alltrack.sdk.AlltrackEventFailure;
import com.alltrack.sdk.AlltrackEventSuccess;
import com.alltrack.sdk.AlltrackSessionFailure;
import com.alltrack.sdk.AlltrackSessionSuccess;
import com.alltrack.sdk.AlltrackPlayStoreSubscription;
import com.alltrack.sdk.AlltrackThirdPartySharing;
import com.alltrack.sdk.AlltrackTestOptions;
import com.alltrack.sdk.LogLevel;
import com.alltrack.sdk.OnAttributionChangedListener;
import com.alltrack.sdk.OnDeeplinkResponseListener;
import com.alltrack.sdk.OnDeviceIdsRead;
import com.alltrack.sdk.OnEventTrackingFailedListener;
import com.alltrack.sdk.OnEventTrackingSucceededListener;
import com.alltrack.sdk.OnSessionTrackingFailedListener;
import com.alltrack.sdk.OnSessionTrackingSucceededListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;

import static com.alltrack.sdk.flutter.AlltrackUtils.*;

public class AlltrackSdk implements FlutterPlugin, ActivityAware, MethodCallHandler {
    private static String TAG = "AlltrackBridge";
    private static boolean launchDeferredDeeplink = true;
    private MethodChannel channel;
    private Context applicationContext;

    // FlutterPlugin
    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        applicationContext = binding.getApplicationContext();
        channel = new MethodChannel(binding.getBinaryMessenger(), "com.alltrack.sdk/api");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding binding) {
        applicationContext = null;
        if (channel != null) {
            channel.setMethodCallHandler(null);
        }
        channel = null;
    }

    // ActivityAware
    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        Alltrack.onResume();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
    }

    @Override
    public void onReattachedToActivityForConfigChanges(
        ActivityPluginBinding binding) {
    }

    @Override
    public void onDetachedFromActivity() {
        Alltrack.onPause();
    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        switch (call.method) {
            case "start":
                start(call, result);
                break;
            case "onPause":
                onPause(result);
                break;
            case "onResume":
                onResume(result);
                break;
            case "trackEvent":
                trackEvent(call, result);
                break;
            case "isEnabled":
                isEnabled(result);
                break;
            case "setEnabled":
                setEnabled(call, result);
                break;
            case "setOfflineMode":
                setOfflineMode(call, result);
                break;
            case "setPushToken":
                setPushToken(call, result);
                break;
            case "appWillOpenUrl":
                appWillOpenUrl(call, result);
                break;
            case "sendFirstPackages":
                sendFirstPackages(result);
                break;
            case "getAdid":
                getAdid(result);
                break;
            case "getIdfa":
                getIdfa(result);
                break;
            case "getGoogleAdId":
                getGoogleAdId(result);
                break;
            case "getAmazonAdId":
                getAmazonAdId(result);
                break;
            case "getAttribution":
                getAttribution(result);
                break;
            case "getSdkVersion":
                getSdkVersion(result);
                break;
            case "setReferrer":
                setReferrer(call, result);
                break;
            case "gdprForgetMe":
                gdprForgetMe(result);
                break;
            case "disableThirdPartySharing":
                disableThirdPartySharing(result);
                break;
            case "addSessionCallbackParameter":
                addSessionCallbackParameter(call, result);
                break;
            case "addSessionPartnerParameter":
                addSessionPartnerParameter(call, result);
                break;
            case "removeSessionCallbackParameter":
                removeSessionCallbackParameter(call, result);
                break;
            case "removeSessionPartnerParameter":
                removeSessionPartnerParameter(call, result);
                break;
            case "resetSessionCallbackParameters":
                resetSessionCallbackParameters(result);
                break;
            case "resetSessionPartnerParameters":
                resetSessionPartnerParameters(result);
                break;
            case "trackAdRevenue":
                trackAdRevenue(call, result);
                break;
            case "trackAdRevenueNew":
                trackAdRevenueNew(call, result);
                break;
            case "trackAppStoreSubscription":
                trackAppStoreSubscription(result);
                break;
            case "trackPlayStoreSubscription":
                trackPlayStoreSubscription(call, result);
                break;
            case "requestTrackingAuthorizationWithCompletionHandler":
                requestTrackingAuthorizationWithCompletionHandler(result);
                break;
            case "updateConversionValue":
                updateConversionValue(result);
                break;
            case "trackThirdPartySharing":
                trackThirdPartySharing(call, result);
                break;
            case "trackMeasurementConsent":
                trackMeasurementConsent(call, result);
                break;
            case "checkForNewAttStatus":
                checkForNewAttStatus(call, result);
                break;
            case "getAppTrackingAuthorizationStatus":
                getAppTrackingAuthorizationStatus(call, result);
                break;
            case "getLastDeeplink":
                getLastDeeplink(call, result);
                break;
            case "setTestOptions":
                setTestOptions(call, result);
                break;
            default:
                Log.e(TAG, "Not implemented method: " + call.method);
                result.notImplemented();
                break;
        }
    }

    private void start(final MethodCall call, final Result result) {
        Map configMap = (Map) call.arguments;
        if (configMap == null) {
            return;
        }

        String appToken = null;
        String environment = null;
        String logLevel = null;
        boolean isLogLevelSuppress = false;

        // App token.
        if (configMap.containsKey("appToken")) {
            appToken = (String) configMap.get("appToken");
        }

        // Environment.
        if (configMap.containsKey("environment")) {
            environment = (String) configMap.get("environment");
        }

        // Suppress log level.
        if (configMap.containsKey("logLevel")) {
            logLevel = (String) configMap.get("logLevel");
            if (logLevel != null && logLevel.equals("suppress")) {
                isLogLevelSuppress = true;
            }
        }

        // Create configuration object.
        AlltrackConfig alltrackConfig = new AlltrackConfig(applicationContext, appToken, environment, isLogLevelSuppress);

        // SDK prefix.
        if (configMap.containsKey("sdkPrefix")) {
            String sdkPrefix = (String) configMap.get("sdkPrefix");
            alltrackConfig.setSdkPrefix(sdkPrefix);
        }

        // Log level.
        if (configMap.containsKey("logLevel")) {
            logLevel = (String) configMap.get("logLevel");
            if (logLevel != null) {
                switch (logLevel) {
                    case "verbose":
                        alltrackConfig.setLogLevel(LogLevel.VERBOSE);
                        break;
                    case "debug":
                        alltrackConfig.setLogLevel(LogLevel.DEBUG);
                        break;
                    case "warn":
                        alltrackConfig.setLogLevel(LogLevel.WARN);
                        break;
                    case "error":
                        alltrackConfig.setLogLevel(LogLevel.ERROR);
                        break;
                    case "assert":
                        alltrackConfig.setLogLevel(LogLevel.ASSERT);
                        break;
                    case "suppress":
                        alltrackConfig.setLogLevel(LogLevel.SUPRESS);
                        break;
                    case "info":
                    default:
                        alltrackConfig.setLogLevel(LogLevel.INFO);
                        break;
                }
            }
        }

        // Event buffering.
        if (configMap.containsKey("eventBufferingEnabled")) {
            String strEventBufferingEnabled = (String) configMap.get("eventBufferingEnabled");
            boolean eventBufferingEnabled = Boolean.parseBoolean(strEventBufferingEnabled);
            alltrackConfig.setEventBufferingEnabled(eventBufferingEnabled);
        }

        // COPPA compliance.
        if (configMap.containsKey("coppaCompliantEnabled")) {
            String strCoppaCompliantEnabled = (String) configMap.get("coppaCompliantEnabled");
            boolean coppaCompliantEnabled = Boolean.parseBoolean(strCoppaCompliantEnabled);
            alltrackConfig.setCoppaCompliantEnabled(coppaCompliantEnabled);
        }

        // Google Play Store kids apps.
        if (configMap.containsKey("playStoreKidsAppEnabled")) {
            String strPlayStoreKidsAppEnabled = (String) configMap.get("playStoreKidsAppEnabled");
            boolean playStoreKidsAppEnabled = Boolean.parseBoolean(strPlayStoreKidsAppEnabled);
            alltrackConfig.setPlayStoreKidsAppEnabled(playStoreKidsAppEnabled);
        }

        // Main process name.
        if (configMap.containsKey("processName")) {
            String processName = (String) configMap.get("processName");
            alltrackConfig.setProcessName(processName);
        }

        // Default tracker.
        if (configMap.containsKey("defaultTracker")) {
            String defaultTracker = (String) configMap.get("defaultTracker");
            alltrackConfig.setDefaultTracker(defaultTracker);
        }

        // External device ID.
        if (configMap.containsKey("externalDeviceId")) {
            String externalDeviceId = (String) configMap.get("externalDeviceId");
            alltrackConfig.setExternalDeviceId(externalDeviceId);
        }

        // Custom preinstall file path.
        if (configMap.containsKey("preinstallFilePath")) {
            String preinstallFilePath = (String) configMap.get("preinstallFilePath");
            alltrackConfig.setPreinstallFilePath(preinstallFilePath);
        }

        // URL strategy.
        if (configMap.containsKey("urlStrategy")) {
            String urlStrategy = (String) configMap.get("urlStrategy");
            if (urlStrategy.equalsIgnoreCase("china")) {
                alltrackConfig.setUrlStrategy(AlltrackConfig.URL_STRATEGY_CHINA);
            } else if (urlStrategy.equalsIgnoreCase("india")) {
                alltrackConfig.setUrlStrategy(AlltrackConfig.URL_STRATEGY_INDIA);
            } else if (urlStrategy.equalsIgnoreCase("cn")) {
                alltrackConfig.setUrlStrategy(AlltrackConfig.URL_STRATEGY_CN);
            } else if (urlStrategy.equalsIgnoreCase("data-residency-eu")) {
                alltrackConfig.setUrlStrategy(AlltrackConfig.DATA_RESIDENCY_EU);
            } else if (urlStrategy.equalsIgnoreCase("data-residency-tr")) {
                alltrackConfig.setUrlStrategy(AlltrackConfig.DATA_RESIDENCY_TR);
            } else if (urlStrategy.equalsIgnoreCase("data-residency-us")) {
                alltrackConfig.setUrlStrategy(AlltrackConfig.DATA_RESIDENCY_US);
            }
        }

        // User agent.
        if (configMap.containsKey("userAgent")) {
            String userAgent = (String) configMap.get("userAgent");
            alltrackConfig.setUserAgent(userAgent);
        }

        // Background tracking.
        if (configMap.containsKey("sendInBackground")) {
            String strSendInBackground = (String) configMap.get("sendInBackground");
            boolean sendInBackground = Boolean.parseBoolean(strSendInBackground);
            alltrackConfig.setSendInBackground(sendInBackground);
        }

        // Set device known.
        if (configMap.containsKey("isDeviceKnown")) {
            String strIsDeviceKnown = (String) configMap.get("isDeviceKnown");
            boolean isDeviceKnown = Boolean.parseBoolean(strIsDeviceKnown);
            alltrackConfig.setDeviceKnown(isDeviceKnown);
        }

        // Cost data.
        if (configMap.containsKey("needsCost")) {
            String strNeedsCost = (String) configMap.get("needsCost");
            boolean needsCost = Boolean.parseBoolean(strNeedsCost);
            alltrackConfig.setNeedsCost(needsCost);
        }

        // Preinstall tracking.
        if (configMap.containsKey("preinstallTrackingEnabled")) {
            String strPreinstallTrackingEnabled = (String) configMap.get("preinstallTrackingEnabled");
            boolean preinstallTrackingEnabled = Boolean.parseBoolean(strPreinstallTrackingEnabled);
            alltrackConfig.setPreinstallTrackingEnabled(preinstallTrackingEnabled);
        }

        // Delayed start.
        if (configMap.containsKey("delayStart")) {
            String strDelayStart = (String) configMap.get("delayStart");
            if (isNumber(strDelayStart)) {
                double delayStart = Double.parseDouble(strDelayStart);
                alltrackConfig.setDelayStart(delayStart);
            }
        }

        // App secret.
        if (configMap.containsKey("secretId")
                && configMap.containsKey("info1")
                && configMap.containsKey("info2")
                && configMap.containsKey("info3")
                && configMap.containsKey("info4")) {
            try {
                String strSecretId = (String) configMap.get("secretId");
                String strInfo1 = (String) configMap.get("info1");
                String strInfo2 = (String) configMap.get("info2");
                String strInfo3 = (String) configMap.get("info3");
                String strInfo4 = (String) configMap.get("info4");
                long secretId = Long.parseLong(strSecretId, 10);
                long info1 = Long.parseLong(strInfo1, 10);
                long info2 = Long.parseLong(strInfo2, 10);
                long info3 = Long.parseLong(strInfo3, 10);
                long info4 = Long.parseLong(strInfo4, 10);
                alltrackConfig.setAppSecret(secretId, info1, info2, info3, info4);
            } catch (NumberFormatException ignore) {}
        }

        // Launch deferred deep link.
        if (configMap.containsKey("launchDeferredDeeplink")) {
            String strLaunchDeferredDeeplink = (String) configMap.get("launchDeferredDeeplink");
            launchDeferredDeeplink = strLaunchDeferredDeeplink.equals("true");
        }

        // Attribution callback.
        if (configMap.containsKey("attributionCallback")) {
            final String dartMethodName = (String) configMap.get("attributionCallback");
            if (dartMethodName != null) {
                alltrackConfig.setOnAttributionChangedListener(new OnAttributionChangedListener() {
                    @Override
                    public void onAttributionChanged(AlltrackAttribution alltrackAttribution) {
                        HashMap<String, Object> alltrackAttributionMap = new HashMap<String, Object>();
                        alltrackAttributionMap.put("trackerToken", alltrackAttribution.trackerToken);
                        alltrackAttributionMap.put("trackerName", alltrackAttribution.trackerName);
                        alltrackAttributionMap.put("network", alltrackAttribution.network);
                        alltrackAttributionMap.put("campaign", alltrackAttribution.campaign);
                        alltrackAttributionMap.put("adgroup", alltrackAttribution.adgroup);
                        alltrackAttributionMap.put("creative", alltrackAttribution.creative);
                        alltrackAttributionMap.put("clickLabel", alltrackAttribution.clickLabel);
                        alltrackAttributionMap.put("adid", alltrackAttribution.adid);
                        alltrackAttributionMap.put("costType", alltrackAttribution.costType);
                        alltrackAttributionMap.put("costAmount", alltrackAttribution.costAmount != null ?
                                alltrackAttribution.costAmount.toString() : "");
                        alltrackAttributionMap.put("costCurrency", alltrackAttribution.costCurrency);
                        alltrackAttributionMap.put("fbInstallReferrer", alltrackAttribution.fbInstallReferrer);
                        if (channel != null) {
                            channel.invokeMethod(dartMethodName, alltrackAttributionMap);
                        }
                    }
                });
            }
        }

        // Session success callback.
        if (configMap.containsKey("sessionSuccessCallback")) {
            final String dartMethodName = (String) configMap.get("sessionSuccessCallback");
            if (dartMethodName != null) {
                alltrackConfig.setOnSessionTrackingSucceededListener(new OnSessionTrackingSucceededListener() {
                    @Override
                    public void onFinishedSessionTrackingSucceeded(AlltrackSessionSuccess alltrackSessionSuccess) {
                        HashMap<String, String> alltrackSessionSuccessMap = new HashMap<String, String>();
                        alltrackSessionSuccessMap.put("message", alltrackSessionSuccess.message);
                        alltrackSessionSuccessMap.put("timestamp", alltrackSessionSuccess.timestamp);
                        alltrackSessionSuccessMap.put("adid", alltrackSessionSuccess.adid);
                        if (alltrackSessionSuccess.jsonResponse != null) {
                            alltrackSessionSuccessMap.put("jsonResponse", alltrackSessionSuccess.jsonResponse.toString());
                        }
                        if (channel != null) {
                            channel.invokeMethod(dartMethodName, alltrackSessionSuccessMap);
                        }
                    }
                });
            }
        }

        // Session failure callback.
        if (configMap.containsKey("sessionFailureCallback")) {
            final String dartMethodName = (String) configMap.get("sessionFailureCallback");
            if (dartMethodName != null) {
                alltrackConfig.setOnSessionTrackingFailedListener(new OnSessionTrackingFailedListener() {
                    @Override
                    public void onFinishedSessionTrackingFailed(AlltrackSessionFailure alltrackSessionFailure) {
                        HashMap<String, String> alltrackSessionFailureMap = new HashMap<String, String>();
                        alltrackSessionFailureMap.put("message", alltrackSessionFailure.message);
                        alltrackSessionFailureMap.put("timestamp", alltrackSessionFailure.timestamp);
                        alltrackSessionFailureMap.put("adid", alltrackSessionFailure.adid);
                        alltrackSessionFailureMap.put("willRetry", Boolean.toString(alltrackSessionFailure.willRetry));
                        if (alltrackSessionFailure.jsonResponse != null) {
                            alltrackSessionFailureMap.put("jsonResponse", alltrackSessionFailure.jsonResponse.toString());
                        }
                        if (channel != null) {
                            channel.invokeMethod(dartMethodName, alltrackSessionFailureMap);
                        }
                    }
                });
            }
        }

        // Event success callback.
        if (configMap.containsKey("eventSuccessCallback")) {
            final String dartMethodName = (String) configMap.get("eventSuccessCallback");
            if (dartMethodName != null) {
                alltrackConfig.setOnEventTrackingSucceededListener(new OnEventTrackingSucceededListener() {
                    @Override
                    public void onFinishedEventTrackingSucceeded(AlltrackEventSuccess alltrackEventSuccess) {
                        HashMap<String, String> alltrackEventSuccessMap = new HashMap<String, String>();
                        alltrackEventSuccessMap.put("message", alltrackEventSuccess.message);
                        alltrackEventSuccessMap.put("timestamp", alltrackEventSuccess.timestamp);
                        alltrackEventSuccessMap.put("adid", alltrackEventSuccess.adid);
                        alltrackEventSuccessMap.put("eventToken", alltrackEventSuccess.eventToken);
                        alltrackEventSuccessMap.put("callbackId", alltrackEventSuccess.callbackId);
                        if (alltrackEventSuccess.jsonResponse != null) {
                            alltrackEventSuccessMap.put("jsonResponse", alltrackEventSuccess.jsonResponse.toString());
                        }
                        if (channel != null) {
                            channel.invokeMethod(dartMethodName, alltrackEventSuccessMap);
                        }
                    }
                });
            }
        }

        // Event failure callback.
        if (configMap.containsKey("eventFailureCallback")) {
            final String dartMethodName = (String) configMap.get("eventFailureCallback");
            if (dartMethodName != null) {
                alltrackConfig.setOnEventTrackingFailedListener(new OnEventTrackingFailedListener() {
                    @Override
                    public void onFinishedEventTrackingFailed(AlltrackEventFailure alltrackEventFailure) {
                        HashMap<String, String> alltrackEventFailureMap = new HashMap<String, String>();
                        alltrackEventFailureMap.put("message", alltrackEventFailure.message);
                        alltrackEventFailureMap.put("timestamp", alltrackEventFailure.timestamp);
                        alltrackEventFailureMap.put("adid", alltrackEventFailure.adid);
                        alltrackEventFailureMap.put("eventToken", alltrackEventFailure.eventToken);
                        alltrackEventFailureMap.put("callbackId", alltrackEventFailure.callbackId);
                        alltrackEventFailureMap.put("willRetry", Boolean.toString(alltrackEventFailure.willRetry));
                        if (alltrackEventFailure.jsonResponse != null) {
                            alltrackEventFailureMap.put("jsonResponse", alltrackEventFailure.jsonResponse.toString());
                        }
                        if (channel != null) {
                            channel.invokeMethod(dartMethodName, alltrackEventFailureMap);
                        }
                    }
                });
            }
        }

        // Deferred deep link callback.
        if (configMap.containsKey("deferredDeeplinkCallback")) {
            final String dartMethodName = (String) configMap.get("deferredDeeplinkCallback");
            if (dartMethodName != null) {
                alltrackConfig.setOnDeeplinkResponseListener(new OnDeeplinkResponseListener() {
                    @Override
                    public boolean launchReceivedDeeplink(Uri uri) {
                        HashMap<String, String> uriParamsMap = new HashMap<String, String>();
                        uriParamsMap.put("uri", uri.toString());
                        if (channel != null) {
                            channel.invokeMethod(dartMethodName, uriParamsMap);
                        }
                        return launchDeferredDeeplink;
                    }
                });
            }
        }

        // Start SDK.
        Alltrack.onCreate(alltrackConfig);
        Alltrack.onResume();
        result.success(null);
    }

    private void trackEvent(final MethodCall call, final Result result) {
        Map eventMap = (Map) call.arguments;
        if (eventMap == null) {
            return;
        }

        // Event token.
        String eventToken = null;
        if (eventMap.containsKey("eventToken")) {
            eventToken = (String) eventMap.get("eventToken");
        }

        // Create event object.
        AlltrackEvent event = new AlltrackEvent(eventToken);

        // Revenue.
        if (eventMap.containsKey("revenue") || eventMap.containsKey("currency")) {
            double revenue = -1.0;
            String strRevenue = (String) eventMap.get("revenue");

            try {
                revenue = Double.parseDouble(strRevenue);
            } catch (NumberFormatException ignore) {}

            String currency = (String) eventMap.get("currency");
            event.setRevenue(revenue, currency);
        }

        // Revenue deduplication.
        if (eventMap.containsKey("transactionId")) {
            String orderId = (String) eventMap.get("transactionId");
            event.setOrderId(orderId);
        }

        // Callback ID.
        if (eventMap.containsKey("callbackId")) {
            String callbackId = (String) eventMap.get("callbackId");
            event.setCallbackId(callbackId);
        }

        // Callback parameters.
        if (eventMap.containsKey("callbackParameters")) {
            String strCallbackParametersJson = (String) eventMap.get("callbackParameters");
            try {
                JSONObject jsonCallbackParameters = new JSONObject(strCallbackParametersJson);
                JSONArray callbackParametersKeys = jsonCallbackParameters.names();
                for (int i = 0; i < callbackParametersKeys.length(); ++i) {
                    String key = callbackParametersKeys.getString(i);
                    String value = jsonCallbackParameters.getString(key);
                    event.addCallbackParameter(key, value);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse event callback parameter! Details: " + e);
            }
        }

        // Partner parameters.
        if (eventMap.containsKey("partnerParameters")) {
            String strPartnerParametersJson = (String) eventMap.get("partnerParameters");
            try {
                JSONObject jsonPartnerParameters = new JSONObject(strPartnerParametersJson);
                JSONArray partnerParametersKeys = jsonPartnerParameters.names();
                for (int i = 0; i < partnerParametersKeys.length(); ++i) {
                    String key = partnerParametersKeys.getString(i);
                    String value = jsonPartnerParameters.getString(key);
                    event.addPartnerParameter(key, value);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse event partner parameter! Details: " + e);
            }
        }

        // Track event.
        Alltrack.trackEvent(event);
        result.success(null);
    }

    private void setOfflineMode(final MethodCall call, final Result result) {
        Map isOfflineParamsMap = (Map) call.arguments;
        boolean isOffline = (boolean) isOfflineParamsMap.get("isOffline");
        Alltrack.setOfflineMode(isOffline);
        result.success(null);
    }

    private void setPushToken(final MethodCall call, final Result result) {
        Map tokenParamsMap = (Map) call.arguments;
        String pushToken = null;
        if (tokenParamsMap.containsKey("pushToken")) {
            pushToken = tokenParamsMap.get("pushToken").toString();
        }
        Alltrack.setPushToken(pushToken, applicationContext);
        result.success(null);
    }

    private void setEnabled(final MethodCall call, final Result result) {
        Map isEnabledParamsMap = (Map) call.arguments;
        if (!isEnabledParamsMap.containsKey("isEnabled")) {
            result.error("0", "Arguments null or wrong (missing argument of 'isEnabled' method.", null);
            return;
        }

        boolean isEnabled = (boolean) isEnabledParamsMap.get("isEnabled");
        Alltrack.setEnabled(isEnabled);
        result.success(null);
    }

    private void appWillOpenUrl(final MethodCall call, final Result result) {
        Map urlParamsMap = (Map) call.arguments;
        String url = null;
        if (urlParamsMap.containsKey("url")) {
            url = urlParamsMap.get("url").toString();
        }
        Alltrack.appWillOpenUrl(Uri.parse(url), applicationContext);
        result.success(null);
    }

    // Exposed for handling deep linking from native layer of the example app.
    public static void appWillOpenUrl(Uri deeplink, Context context) {
        Alltrack.appWillOpenUrl(deeplink, context);
    }

    private void sendFirstPackages(final Result result) {
        Alltrack.sendFirstPackages();
        result.success(null);
    }

    private void onResume(final Result result) {
        Alltrack.onResume();
        result.success(null);
    }

    private void onPause(final Result result) {
        Alltrack.onPause();
        result.success(null);
    }

    private void isEnabled(final Result result) {
        result.success(Alltrack.isEnabled());
    }

    private void getAdid(final Result result) {
        result.success(Alltrack.getAdid());
    }

    private void getIdfa(final Result result) {
        result.success("Error. No IDFA on Android platform!");
    }

    private void getGoogleAdId(final Result result) {
        Alltrack.getGoogleAdId(applicationContext, new OnDeviceIdsRead() {
            @Override
            public void onGoogleAdIdRead(String googleAdId) {
                result.success(googleAdId);
            }
        });
    }

    private void getAmazonAdId(final Result result) {
        result.success(Alltrack.getAmazonAdId(applicationContext));
    }

    private void gdprForgetMe(final Result result) {
        Alltrack.gdprForgetMe(applicationContext);
        result.success(null);
    }

    private void disableThirdPartySharing(final Result result) {
        Alltrack.disableThirdPartySharing(applicationContext);
        result.success(null);
    }

    private void getAttribution(final Result result) {
        AlltrackAttribution alltrackAttribution = Alltrack.getAttribution();
        if (alltrackAttribution == null) {
            alltrackAttribution = new AlltrackAttribution();
        }

        HashMap<String, String> alltrackAttributionMap = new HashMap<String, String>();
        alltrackAttributionMap.put("trackerToken", alltrackAttribution.trackerToken);
        alltrackAttributionMap.put("trackerName", alltrackAttribution.trackerName);
        alltrackAttributionMap.put("network", alltrackAttribution.network);
        alltrackAttributionMap.put("campaign", alltrackAttribution.campaign);
        alltrackAttributionMap.put("adgroup", alltrackAttribution.adgroup);
        alltrackAttributionMap.put("creative", alltrackAttribution.creative);
        alltrackAttributionMap.put("clickLabel", alltrackAttribution.clickLabel);
        alltrackAttributionMap.put("adid", alltrackAttribution.adid);
        alltrackAttributionMap.put("costType", alltrackAttribution.costType);
        alltrackAttributionMap.put("costAmount", alltrackAttribution.costAmount != null ?
                alltrackAttribution.costAmount.toString() : "");
        alltrackAttributionMap.put("costCurrency", alltrackAttribution.costCurrency);
        alltrackAttributionMap.put("fbInstallReferrer", alltrackAttribution.fbInstallReferrer);
        result.success(alltrackAttributionMap);
    }

    private void getSdkVersion(final Result result) {
        result.success(Alltrack.getSdkVersion());
    }

    private void setReferrer(final MethodCall call, final Result result) {
        String referrer = null;
        if (call.hasArgument("referrer")) {
            referrer = (String) call.argument("referrer");
        }
        Alltrack.setReferrer(referrer, applicationContext);
        result.success(null);
    }

    private void addSessionCallbackParameter(final MethodCall call, final Result result) {
        String key = null;
        String value = null;
        if (call.hasArgument("key") && call.hasArgument("value")) {
            key = (String) call.argument("key");
            value = (String) call.argument("value");
        }
        Alltrack.addSessionCallbackParameter(key, value);
        result.success(null);
    }

    private void addSessionPartnerParameter(final MethodCall call, final Result result) {
        String key = null;
        String value = null;
        if (call.hasArgument("key") && call.hasArgument("value")) {
            key = (String) call.argument("key");
            value = (String) call.argument("value");
        }
        Alltrack.addSessionPartnerParameter(key, value);
        result.success(null);
    }

    private void removeSessionCallbackParameter(final MethodCall call, final Result result) {
        String key = null;
        if (call.hasArgument("key")) {
            key = (String) call.argument("key");
        }
        Alltrack.removeSessionCallbackParameter(key);
        result.success(null);
    }

    private void removeSessionPartnerParameter(final MethodCall call, final Result result) {
        String key = null;
        if (call.hasArgument("key")) {
            key = (String) call.argument("key");
        }
        Alltrack.removeSessionPartnerParameter(key);
        result.success(null);
    }

    private void resetSessionCallbackParameters(final Result result) {
        Alltrack.resetSessionCallbackParameters();
        result.success(null);
    }

    private void resetSessionPartnerParameters(final Result result) {
        Alltrack.resetSessionPartnerParameters();
        result.success(null);
    }

    private void trackAdRevenue(final MethodCall call, final Result result) {
        if (call.hasArgument("source") && call.hasArgument("payload")) {
            // Old (MoPub) API.
            String source = (String) call.argument("source");
            String payload = (String) call.argument("payload");

            try {
                JSONObject jsonPayload = new JSONObject(payload);
                Alltrack.trackAdRevenue(source, jsonPayload);
            } catch (JSONException err) {
                Log.e(TAG, "Given ad revenue payload is not a valid JSON string");
            }
        } 
        result.success(null);
    }

    private void trackAdRevenueNew(final MethodCall call, final Result result) {
        // New API.
        Map adRevenueMap = (Map) call.arguments;
        if (adRevenueMap == null) {
            return;
        }

        // Source.
        String source = null;
        if (adRevenueMap.containsKey("source")) {
            source = (String) adRevenueMap.get("source");
        }

        // Create ad revenue object.
        AlltrackAdRevenue adRevenue = new AlltrackAdRevenue(source);

        // Revenue.
        if (adRevenueMap.containsKey("revenue") || adRevenueMap.containsKey("currency")) {
            double revenue = -1.0;
            String strRevenue = (String) adRevenueMap.get("revenue");

            try {
                revenue = Double.parseDouble(strRevenue);
            } catch (NumberFormatException ignore) {}

            String currency = (String) adRevenueMap.get("currency");
            adRevenue.setRevenue(revenue, currency);
        }

        // Ad impressions count.
        if (adRevenueMap.containsKey("adImpressionsCount")) {
            String strAdImpressionsCount = (String) adRevenueMap.get("adImpressionsCount");
            int adImpressionsCount = Integer.parseInt(strAdImpressionsCount);
            adRevenue.setAdImpressionsCount(adImpressionsCount);
        }

        // Ad revenue network.
        if (adRevenueMap.containsKey("adRevenueNetwork")) {
            String adRevenueNetwork = (String) adRevenueMap.get("adRevenueNetwork");
            adRevenue.setAdRevenueNetwork(adRevenueNetwork);
        }

        // Ad revenue unit.
        if (adRevenueMap.containsKey("adRevenueUnit")) {
            String adRevenueUnit = (String) adRevenueMap.get("adRevenueUnit");
            adRevenue.setAdRevenueUnit(adRevenueUnit);
        }

        // Ad revenue placement.
        if (adRevenueMap.containsKey("adRevenuePlacement")) {
            String adRevenuePlacement = (String) adRevenueMap.get("adRevenuePlacement");
            adRevenue.setAdRevenuePlacement(adRevenuePlacement);
        }

        // Callback parameters.
        if (adRevenueMap.containsKey("callbackParameters")) {
            String strCallbackParametersJson = (String) adRevenueMap.get("callbackParameters");
            try {
                JSONObject jsonCallbackParameters = new JSONObject(strCallbackParametersJson);
                JSONArray callbackParametersKeys = jsonCallbackParameters.names();
                for (int i = 0; i < callbackParametersKeys.length(); ++i) {
                    String key = callbackParametersKeys.getString(i);
                    String value = jsonCallbackParameters.getString(key);
                    adRevenue.addCallbackParameter(key, value);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse ad revenue callback parameter! Details: " + e);
            }
        }

        // Partner parameters.
        if (adRevenueMap.containsKey("partnerParameters")) {
            String strPartnerParametersJson = (String) adRevenueMap.get("partnerParameters");
            try {
                JSONObject jsonPartnerParameters = new JSONObject(strPartnerParametersJson);
                JSONArray partnerParametersKeys = jsonPartnerParameters.names();
                for (int i = 0; i < partnerParametersKeys.length(); ++i) {
                    String key = partnerParametersKeys.getString(i);
                    String value = jsonPartnerParameters.getString(key);
                    adRevenue.addPartnerParameter(key, value);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse ad revenue partner parameter! Details: " + e);
            }
        }

        // Track ad revenue.
        Alltrack.trackAdRevenue(adRevenue);
        result.success(null);
    }

    private void trackAppStoreSubscription(final Result result) {
        result.success("Error. No App Store subscription tracking on Android platform!");
    }

    private void trackPlayStoreSubscription(final MethodCall call, final Result result) {
        Map subscriptionMap = (Map) call.arguments;
        if (subscriptionMap == null) {
            return;
        }

        // Price.
        long price = -1;
        if (subscriptionMap.containsKey("price")) {
            try {
                price = Long.parseLong(subscriptionMap.get("price").toString());
            } catch (NumberFormatException ignore) {}
        }

        // Currency.
        String currency = null;
        if (subscriptionMap.containsKey("currency")) {
            currency = (String) subscriptionMap.get("currency");
        }

        // SKU.
        String sku = null;
        if (subscriptionMap.containsKey("sku")) {
            sku = (String) subscriptionMap.get("sku");
        }

        // Order ID.
        String orderId = null;
        if (subscriptionMap.containsKey("orderId")) {
            orderId = (String) subscriptionMap.get("orderId");
        }

        // Signature.
        String signature = null;
        if (subscriptionMap.containsKey("signature")) {
            signature = (String) subscriptionMap.get("signature");
        }

        // Purchase token.
        String purchaseToken = null;
        if (subscriptionMap.containsKey("purchaseToken")) {
            purchaseToken = (String) subscriptionMap.get("purchaseToken");
        }

        // Create subscription object.
        AlltrackPlayStoreSubscription subscription = new AlltrackPlayStoreSubscription(
                price,
                currency,
                sku,
                orderId,
                signature,
                purchaseToken);

        // Purchase time.
        if (subscriptionMap.containsKey("purchaseTime")) {
            try {
                long purchaseTime = Long.parseLong(subscriptionMap.get("purchaseTime").toString());
                subscription.setPurchaseTime(purchaseTime);
            } catch (NumberFormatException ignore) {}
        }

        // Callback parameters.
        if (subscriptionMap.containsKey("callbackParameters")) {
            String strCallbackParametersJson = (String) subscriptionMap.get("callbackParameters");
            try {
                JSONObject jsonCallbackParameters = new JSONObject(strCallbackParametersJson);
                JSONArray callbackParametersKeys = jsonCallbackParameters.names();
                for (int i = 0; i < callbackParametersKeys.length(); ++i) {
                    String key = callbackParametersKeys.getString(i);
                    String value = jsonCallbackParameters.getString(key);
                    subscription.addCallbackParameter(key, value);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse subscription callback parameter! Details: " + e);
            }
        }

        // Partner parameters.
        if (subscriptionMap.containsKey("partnerParameters")) {
            String strPartnerParametersJson = (String) subscriptionMap.get("partnerParameters");
            try {
                JSONObject jsonPartnerParameters = new JSONObject(strPartnerParametersJson);
                JSONArray partnerParametersKeys = jsonPartnerParameters.names();
                for (int i = 0; i < partnerParametersKeys.length(); ++i) {
                    String key = partnerParametersKeys.getString(i);
                    String value = jsonPartnerParameters.getString(key);
                    subscription.addPartnerParameter(key, value);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse subscription partner parameter! Details: " + e);
            }
        }

        // Track subscription.
        Alltrack.trackPlayStoreSubscription(subscription);
        result.success(null);
    }

    private void requestTrackingAuthorizationWithCompletionHandler(final Result result) {
        result.success("Error. No requestTrackingAuthorizationWithCompletionHandler on Android platform!");
    }

    private void updateConversionValue(final Result result) {
        result.success("Error. No updateConversionValue on Android platform!");
    }

    private void trackThirdPartySharing(final MethodCall call, final Result result) {
        Map thirdPartySharingMap = (Map) call.arguments;
        if (thirdPartySharingMap == null) {
            return;
        }

        Boolean isEnabled = null;
        if (thirdPartySharingMap.containsKey("isEnabled")) {
            isEnabled = (Boolean) thirdPartySharingMap.get("isEnabled");
        }

        // Create third party sharing object.
        AlltrackThirdPartySharing thirdPartySharing = new AlltrackThirdPartySharing(isEnabled);

        // Granular options.
        if (thirdPartySharingMap.containsKey("granularOptions")) {
            String strGranularOptions = (String) thirdPartySharingMap.get("granularOptions");
            String[] arrayGranularOptions = strGranularOptions.split("__ALT__", -1);
            for (int i = 0; i < arrayGranularOptions.length; i += 3) {
                thirdPartySharing.addGranularOption(
                    arrayGranularOptions[i],
                    arrayGranularOptions[i+1],
                    arrayGranularOptions[i+2]);
            }
        }

        // Partner sharing settings.
        if (thirdPartySharingMap.containsKey("partnerSharingSettings")) {
            String strPartnerSharingSettings = (String) thirdPartySharingMap.get("partnerSharingSettings");
            String[] arrayPartnerSharingSettings = strPartnerSharingSettings.split("__ALT__", -1);
            for (int i = 0; i < arrayPartnerSharingSettings.length; i += 3) {
                thirdPartySharing.addPartnerSharingSetting(
                    arrayPartnerSharingSettings[i],
                    arrayPartnerSharingSettings[i+1],
                    Boolean.parseBoolean(arrayPartnerSharingSettings[i+2]));
            }
        }

        // Track third party sharing.
        Alltrack.trackThirdPartySharing(thirdPartySharing);
        result.success(null);
    }

    private void trackMeasurementConsent(final MethodCall call, final Result result) {
        Map measurementConsentMap = (Map) call.arguments;
        if (!measurementConsentMap.containsKey("measurementConsent")) {
            result.error("0", "Arguments null or wrong (missing argument of 'trackMeasurementConsent' method.", null);
            return;
        }

        boolean measurementConsent = (boolean) measurementConsentMap.get("measurementConsent");
        Alltrack.trackMeasurementConsent(measurementConsent);
        result.success(null);
    }

    private void checkForNewAttStatus(final MethodCall call, final Result result) {
        result.success("Error. No checkForNewAttStatus for Android platform!");
    }

    private void getAppTrackingAuthorizationStatus(final MethodCall call, final Result result) {
        result.success("Error. No getAppTrackingAuthorizationStatus for Android platform!");
    }

    private void getLastDeeplink(final MethodCall call, final Result result) {
        result.success("Error. No getLastDeeplink for Android platform!");
    }

    private void setTestOptions(final MethodCall call, final Result result) {
        AlltrackTestOptions testOptions = new AlltrackTestOptions();
        Map testOptionsMap = (Map) call.arguments;

        if (testOptionsMap.containsKey("baseUrl")) {
            testOptions.baseUrl = (String) testOptionsMap.get("baseUrl");
        }
        if (testOptionsMap.containsKey("gdprUrl")) {
            testOptions.gdprUrl = (String) testOptionsMap.get("gdprUrl");
        }
        if (testOptionsMap.containsKey("subscriptionUrl")) {
            testOptions.subscriptionUrl = (String) testOptionsMap.get("subscriptionUrl");
        }
        if (testOptionsMap.containsKey("basePath")) {
            testOptions.basePath = (String) testOptionsMap.get("basePath");
        }
        if (testOptionsMap.containsKey("gdprPath")) {
            testOptions.gdprPath = (String) testOptionsMap.get("gdprPath");
        }
        if (testOptionsMap.containsKey("subscriptionPath")) {
            testOptions.subscriptionPath = (String) testOptionsMap.get("subscriptionPath");
        }
        // Kept for the record. Not needed anymore with test options extraction.
        // if (testOptionsMap.containsKey("useTestConnectionOptions")) {
        //     testOptions.useTestConnectionOptions = testOptionsMap.get("useTestConnectionOptions").toString().equals("true");
        // }
        if (testOptionsMap.containsKey("noBackoffWait")) {
            testOptions.noBackoffWait = testOptionsMap.get("noBackoffWait").toString().equals("true");
        }
        if (testOptionsMap.containsKey("teardown")) {
            testOptions.teardown = testOptionsMap.get("teardown").toString().equals("true");
        }
        if (testOptionsMap.containsKey("tryInstallReferrer")) {
            testOptions.tryInstallReferrer = testOptionsMap.get("tryInstallReferrer").toString().equals("true");
        }
        if (testOptionsMap.containsKey("timerIntervalInMilliseconds")) {
            testOptions.timerIntervalInMilliseconds = Long.parseLong(testOptionsMap.get("timerIntervalInMilliseconds").toString());
        }
        if (testOptionsMap.containsKey("timerStartInMilliseconds")) {
            testOptions.timerStartInMilliseconds = Long.parseLong(testOptionsMap.get("timerStartInMilliseconds").toString());
        }
        if (testOptionsMap.containsKey("sessionIntervalInMilliseconds")) {
            testOptions.sessionIntervalInMilliseconds = Long.parseLong(testOptionsMap.get("sessionIntervalInMilliseconds").toString());
        }
        if (testOptionsMap.containsKey("subsessionIntervalInMilliseconds")) {
            testOptions.subsessionIntervalInMilliseconds = Long.parseLong(testOptionsMap.get("subsessionIntervalInMilliseconds").toString());
        }
        if (testOptionsMap.containsKey("deleteState")) {
            testOptions.context = applicationContext;
        }

        Alltrack.setTestOptions(testOptions);
    }
}
