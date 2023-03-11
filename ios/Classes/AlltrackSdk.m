#import "AlltrackSdk.h"
#import "AlltrackSdkDelegate.h"
#import <Alltrack/Alltrack.h>

static NSString * const CHANNEL_API_NAME = @"com.alltrack.sdk/api";

@interface AlltrackSdk ()

@property (nonatomic, retain) FlutterMethodChannel *channel;

@end

@implementation AlltrackSdk

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    FlutterMethodChannel *channel = [FlutterMethodChannel methodChannelWithName:CHANNEL_API_NAME
                                                                binaryMessenger:[registrar messenger]];
    AlltrackSdk *instance = [[AlltrackSdk alloc] init];
    instance.channel = channel;
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)dealloc {
    [self.channel setMethodCallHandler:nil];
    self.channel = nil;
}

- (void)handleMethodCall:(FlutterMethodCall *)call result:(FlutterResult)result {
    if ([@"start" isEqualToString:call.method]) {
        [self start:call withResult:result];
    } else if ([@"onResume" isEqualToString:call.method]) {
        [Alltrack trackSubsessionStart];
    } else if ([@"onPause" isEqualToString:call.method]) {
        [Alltrack trackSubsessionEnd];
    } else if ([@"trackEvent" isEqualToString:call.method]) {
        [self trackEvent:call withResult:result];
    } else if ([@"setEnabled" isEqualToString:call.method]) {
        [self setEnabled:call withResult:result];
    } else if ([@"sendFirstPackages" isEqualToString:call.method]) {
        [self sendFirstPackages:call withResult:result];
    } else if ([@"gdprForgetMe" isEqualToString:call.method]) {
        [self gdprForgetMe:call withResult:result];
    } else if ([@"disableThirdPartySharing" isEqualToString:call.method]) {
        [self disableThirdPartySharing:call withResult:result];
    } else if ([@"getAttribution" isEqualToString:call.method]) {
        [self getAttribution:call withResult:result];
    } else if ([@"getIdfa" isEqualToString:call.method]) {
        [self getIdfa:call withResult:result];
    } else if ([@"getGoogleAdId" isEqualToString:call.method]) {
        [self getGoogleAdId:call withResult:result];
    } else if ([@"getSdkVersion" isEqualToString:call.method]) {
        [self getSdkVersion:call withResult:result];
    } else if ([@"setOfflineMode" isEqualToString:call.method]) {
        [self setOfflineMode:call withResult:result];
    } else if ([@"setPushToken" isEqualToString:call.method]) {
        [self setPushToken:call withResult:result];
    } else if ([@"appWillOpenUrl" isEqualToString:call.method]) {
        [self appWillOpenUrl:call withResult:result];
    } else if ([@"trackAdRevenue" isEqualToString:call.method]) {
        [self trackAdRevenue:call withResult:result];
    } else if ([@"trackAdRevenueNew" isEqualToString:call.method]) {
        [self trackAdRevenueNew:call withResult:result];
    } else if ([@"trackAppStoreSubscription" isEqualToString:call.method]) {
        [self trackAppStoreSubscription:call withResult:result];
    } else if ([@"trackPlayStoreSubscription" isEqualToString:call.method]) {
        [self trackPlayStoreSubscription:call withResult:result];
    } else if ([@"requestTrackingAuthorizationWithCompletionHandler" isEqualToString:call.method]) {
        [self requestTrackingAuthorizationWithCompletionHandler:call withResult:result];
    } else if ([@"getAppTrackingAuthorizationStatus" isEqualToString:call.method]) {
        [self getAppTrackingAuthorizationStatus:call withResult:result];
    } else if ([@"updateConversionValue" isEqualToString:call.method]) {
        [self updateConversionValue:call withResult:result];
    } else if ([@"trackThirdPartySharing" isEqualToString:call.method]) {
        [self trackThirdPartySharing:call withResult:result];
    } else if ([@"trackMeasurementConsent" isEqualToString:call.method]) {
        [self trackMeasurementConsent:call withResult:result];
    } else if ([@"setTestOptions" isEqualToString:call.method]) {
        [self setTestOptions:call withResult:result];
    } else if ([@"addSessionCallbackParameter" isEqualToString:call.method]) {
        NSString *key = call.arguments[@"key"];
        NSString *value = call.arguments[@"value"];
        if (!([self isFieldValid:key]) || !([self isFieldValid:value])) {
            return;
        }
        [Alltrack addSessionCallbackParameter:key value:value];
    } else if ([@"removeSessionCallbackParameter" isEqualToString:call.method]) {
        NSString *key = call.arguments[@"key"];
        if (!([self isFieldValid:key])) {
            return;
        }
        [Alltrack removeSessionCallbackParameter:key];
    } else if ([@"resetSessionCallbackParameters" isEqualToString:call.method]) {
        [Alltrack resetSessionCallbackParameters];
    } else if ([@"addSessionPartnerParameter" isEqualToString:call.method]) {
        NSString *key = call.arguments[@"key"];
        NSString *value = call.arguments[@"value"];
        if (!([self isFieldValid:key]) || !([self isFieldValid:value])) {
            return;
        }
        [Alltrack addSessionPartnerParameter:key value:value];
    } else if ([@"removeSessionPartnerParameter" isEqualToString:call.method]) {
        NSString *key = call.arguments[@"key"];
        if (!([self isFieldValid:key])) {
            return;
        }
        [Alltrack removeSessionPartnerParameter:key];
    } else if ([@"resetSessionPartnerParameters" isEqualToString:call.method]) {
        [Alltrack resetSessionPartnerParameters];
    } else if ([@"isEnabled" isEqualToString:call.method]) {
        BOOL isEnabled = [Alltrack isEnabled];
        result(@(isEnabled));
    } else if ([@"getAdid" isEqualToString:call.method]) {
        result([Alltrack adid]);
    } else if ([@"checkForNewAttStatus" isEqualToString:call.method]) {
         [Alltrack checkForNewAttStatus];
    } else if ([@"getLastDeeplink" isEqualToString:call.method]) {
         [self getLastDeeplink:call withResult:result];
    } else if ([@"updateConversionValueWithErrorCallback" isEqualToString:call.method]) {
        [self updateConversionValueWithErrorCallback:call withResult:result];
    } else if ([@"updateConversionValueWithErrorCallbackSkad4" isEqualToString:call.method]) {
        [self updateConversionValueWithErrorCallbackSkad4:call withResult:result];
    } else {
        result(FlutterMethodNotImplemented);
    }
}

- (void)start:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *appToken = call.arguments[@"appToken"];
    NSString *environment = call.arguments[@"environment"];
    NSString *logLevel = call.arguments[@"logLevel"];
    NSString *sdkPrefix = call.arguments[@"sdkPrefix"];
    NSString *defaultTracker = call.arguments[@"defaultTracker"];
    NSString *externalDeviceId = call.arguments[@"externalDeviceId"];
    NSString *userAgent = call.arguments[@"userAgent"];
    NSString *urlStrategy = call.arguments[@"urlStrategy"];
    NSString *secretId = call.arguments[@"secretId"];
    NSString *info1 = call.arguments[@"info1"];
    NSString *info2 = call.arguments[@"info2"];
    NSString *info3 = call.arguments[@"info3"];
    NSString *info4 = call.arguments[@"info4"];
    NSString *delayStart = call.arguments[@"delayStart"];
    NSString *isDeviceKnown = call.arguments[@"isDeviceKnown"];
    NSString *eventBufferingEnabled = call.arguments[@"eventBufferingEnabled"];
    NSString *sendInBackground = call.arguments[@"sendInBackground"];
    NSString *needsCost = call.arguments[@"needsCost"];
    NSString *coppaCompliantEnabled = call.arguments[@"coppaCompliantEnabled"];
    NSString *linkMeEnabled = call.arguments[@"linkMeEnabled"];
    NSString *allowAdServicesInfoReading = call.arguments[@"allowAdServicesInfoReading"];
    NSString *allowIdfaReading = call.arguments[@"allowIdfaReading"];
    NSString *skAdNetworkHandling = call.arguments[@"skAdNetworkHandling"];
    NSString *dartAttributionCallback = call.arguments[@"attributionCallback"];
    NSString *dartSessionSuccessCallback = call.arguments[@"sessionSuccessCallback"];
    NSString *dartSessionFailureCallback = call.arguments[@"sessionFailureCallback"];
    NSString *dartEventSuccessCallback = call.arguments[@"eventSuccessCallback"];
    NSString *dartEventFailureCallback = call.arguments[@"eventFailureCallback"];
    NSString *dartDeferredDeeplinkCallback = call.arguments[@"deferredDeeplinkCallback"];
    NSString *dartConversionValueUpdatedCallback = call.arguments[@"conversionValueUpdatedCallback"];
    NSString *dartSkad4ConversionValueUpdatedCallback = call.arguments[@"skad4ConversionValueUpdatedCallback"];
    BOOL allowSuppressLogLevel = NO;
    BOOL launchDeferredDeeplink = [call.arguments[@"launchDeferredDeeplink"] boolValue];

    // Suppress log level.
    if ([self isFieldValid:logLevel]) {
        if ([ALTLogger logLevelFromString:[logLevel lowercaseString]] == ALTLogLevelSuppress) {
            allowSuppressLogLevel = YES;
        }
    }

    // Create config object.
    ALTConfig *alltrackConfig = [ALTConfig configWithAppToken:appToken
                                                environment:environment
                                      allowSuppressLogLevel:allowSuppressLogLevel];

    // SDK prefix.
    if ([self isFieldValid:sdkPrefix]) {
        [alltrackConfig setSdkPrefix:sdkPrefix];
    }

    // Log level.
    if ([self isFieldValid:logLevel]) {
        [alltrackConfig setLogLevel:[ALTLogger logLevelFromString:[logLevel lowercaseString]]];
    }

    // Event buffering.
    if ([self isFieldValid:eventBufferingEnabled]) {
        [alltrackConfig setEventBufferingEnabled:[eventBufferingEnabled boolValue]];
    }

    // COPPA compliance.
    if ([self isFieldValid:coppaCompliantEnabled]) {
        [alltrackConfig setCoppaCompliantEnabled:[coppaCompliantEnabled boolValue]];
    }

    // LinkMe.
    if ([self isFieldValid:linkMeEnabled]) {
        [alltrackConfig setLinkMeEnabled:[linkMeEnabled boolValue]];
    }

    // Default tracker.
    if ([self isFieldValid:defaultTracker]) {
        [alltrackConfig setDefaultTracker:defaultTracker];
    }

    // External device ID.
    if ([self isFieldValid:externalDeviceId]) {
        [alltrackConfig setExternalDeviceId:externalDeviceId];
    }

    // User agent.
    if ([self isFieldValid:userAgent]) {
        [alltrackConfig setUserAgent:userAgent];
    }

    // URL strategy.
    if ([self isFieldValid:urlStrategy]) {
        if ([urlStrategy isEqualToString:@"china"]) {
            [alltrackConfig setUrlStrategy:ALTUrlStrategyChina];
        } else if ([urlStrategy isEqualToString:@"india"]) {
            [alltrackConfig setUrlStrategy:ALTUrlStrategyIndia];
        } else if ([urlStrategy isEqualToString:@"cn"]) {
            [alltrackConfig setUrlStrategy:ALTUrlStrategyCn];
        } else if ([urlStrategy isEqualToString:@"data-residency-eu"]) {
            [alltrackConfig setUrlStrategy:ALTDataResidencyEU];
        } else if ([urlStrategy isEqualToString:@"data-residency-tr"]) {
            [alltrackConfig setUrlStrategy:ALTDataResidencyTR];
        } else if ([urlStrategy isEqualToString:@"data-residency-us"]) {
            [alltrackConfig setUrlStrategy:ALTDataResidencyUS];
        }
    }

    // Background tracking.
    if ([self isFieldValid:sendInBackground]) {
        [alltrackConfig setSendInBackground:[sendInBackground boolValue]];
    }

    // Cost data.
    if ([self isFieldValid:needsCost]) {
        [alltrackConfig setNeedsCost:[needsCost boolValue]];
    }

    // Allow AdServices info reading.
    if ([self isFieldValid:allowAdServicesInfoReading]) {
        [alltrackConfig setAllowAdServicesInfoReading:[allowAdServicesInfoReading boolValue]];
    }

    // Allow IDFA reading.
    if ([self isFieldValid:allowIdfaReading]) {
        [alltrackConfig setAllowIdfaReading:[allowIdfaReading boolValue]];
    }
    
    // SKAdNetwork handling.
    if ([self isFieldValid:skAdNetworkHandling]) {
        if ([skAdNetworkHandling boolValue] == NO) {
            [alltrackConfig deactivateSKAdNetworkHandling];
        }
    }

    // Set device known.
    if ([self isFieldValid:isDeviceKnown]) {
        [alltrackConfig setIsDeviceKnown:[isDeviceKnown boolValue]];
    }

    // Delayed start.
    if ([self isFieldValid:delayStart]) {
        [alltrackConfig setDelayStart:[delayStart doubleValue]];
    }
    
    // App secret.
    if ([self isFieldValid:secretId]
        && [self isFieldValid:info1]
        && [self isFieldValid:info2]
        && [self isFieldValid:info3]
        && [self isFieldValid:info4]) {
        [alltrackConfig setAppSecret:[[NSNumber numberWithLongLong:[secretId longLongValue]] unsignedIntegerValue]
                             info1:[[NSNumber numberWithLongLong:[info1 longLongValue]] unsignedIntegerValue]
                             info2:[[NSNumber numberWithLongLong:[info2 longLongValue]] unsignedIntegerValue]
                             info3:[[NSNumber numberWithLongLong:[info3 longLongValue]] unsignedIntegerValue]
                             info4:[[NSNumber numberWithLongLong:[info4 longLongValue]] unsignedIntegerValue]];
    }

    // Callbacks.
    if (dartAttributionCallback != nil
        || dartSessionSuccessCallback != nil
        || dartSessionFailureCallback != nil
        || dartEventSuccessCallback != nil
        || dartEventFailureCallback != nil
        || dartDeferredDeeplinkCallback != nil
        || dartConversionValueUpdatedCallback != nil
        || dartSkad4ConversionValueUpdatedCallback != nil) {
        [alltrackConfig setDelegate:
         [AlltrackSdkDelegate getInstanceWithSwizzleOfAttributionCallback:dartAttributionCallback
                                                 sessionSuccessCallback:dartSessionSuccessCallback
                                                 sessionFailureCallback:dartSessionFailureCallback
                                                   eventSuccessCallback:dartEventSuccessCallback
                                                   eventFailureCallback:dartEventFailureCallback
                                               deferredDeeplinkCallback:dartDeferredDeeplinkCallback
                                         conversionValueUpdatedCallback:dartConversionValueUpdatedCallback
                                    skad4ConversionValueUpdatedCallback:dartSkad4ConversionValueUpdatedCallback
                                           shouldLaunchDeferredDeeplink:launchDeferredDeeplink
                                                       andMethodChannel:self.channel]];
    }

    // Start SDK.
    [Alltrack appDidLaunch:alltrackConfig];
    [Alltrack trackSubsessionStart];
    result(nil);
}

- (void)trackEvent:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *eventToken = call.arguments[@"eventToken"];
    NSString *revenue = call.arguments[@"revenue"];
    NSString *currency = call.arguments[@"currency"];
    NSString *callbackId = call.arguments[@"callbackId"];
    NSString *transactionId = call.arguments[@"transactionId"];
    NSString *strCallbackParametersJson = call.arguments[@"callbackParameters"];
    NSString *strPartnerParametersJson = call.arguments[@"partnerParameters"];

    // Create event object.
    ALTEvent *alltrackEvent = [ALTEvent eventWithEventToken:eventToken];

    // Revenue.
    if ([self isFieldValid:revenue]) {
        double revenueValue = [revenue doubleValue];
        [alltrackEvent setRevenue:revenueValue currency:currency];
    }

    // Transaction ID.
    if ([self isFieldValid:transactionId]) {
        [alltrackEvent setTransactionId:transactionId];
    }

    // Callback ID.
    if ([self isFieldValid:callbackId]) {
        [alltrackEvent setCallbackId:callbackId];
    }

    // Callback parameters.
    if (strCallbackParametersJson != nil) {
        NSData *callbackParametersData = [strCallbackParametersJson dataUsingEncoding:NSUTF8StringEncoding];
        id callbackParametersJson = [NSJSONSerialization JSONObjectWithData:callbackParametersData
                                                                    options:0
                                                                      error:NULL];
        for (id key in callbackParametersJson) {
            NSString *value = [callbackParametersJson objectForKey:key];
            [alltrackEvent addCallbackParameter:key value:value];
        }
    }

    // Partner parameters.
    if (strPartnerParametersJson != nil) {
        NSData *partnerParametersData = [strPartnerParametersJson dataUsingEncoding:NSUTF8StringEncoding];
        id partnerParametersJson = [NSJSONSerialization JSONObjectWithData:partnerParametersData
                                                                   options:0
                                                                     error:NULL];
        for (id key in partnerParametersJson) {
            NSString *value = [partnerParametersJson objectForKey:key];
            [alltrackEvent addPartnerParameter:key value:value];
        }
    }

    // Track event.
    [Alltrack trackEvent:alltrackEvent];
    result(nil);
}

- (void)setEnabled:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *isEnabled = call.arguments[@"isEnabled"];
    if ([self isFieldValid:isEnabled]) {
        [Alltrack setEnabled:[isEnabled boolValue]];
    }
    result(nil);
}

- (void)sendFirstPackages:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    [Alltrack sendFirstPackages];
    result(nil);
}

- (void)gdprForgetMe:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    [Alltrack gdprForgetMe];
    result(nil);
}

- (void)disableThirdPartySharing:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    [Alltrack disableThirdPartySharing];
    result(nil);
}

- (void)setOfflineMode:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *isOffline = call.arguments[@"isOffline"];
    if ([self isFieldValid:isOffline]) {
        [Alltrack setOfflineMode:[isOffline boolValue]];
    }
    result(nil);
}

- (void)setPushToken:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *pushToken = call.arguments[@"pushToken"];
    if ([self isFieldValid:pushToken]) {
        [Alltrack setPushToken:pushToken];
    }
    result(nil);
}

- (void)appWillOpenUrl:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *urlString = call.arguments[@"url"];
    if (urlString == nil) {
        return;
    }
    
    NSURL *url;
    if ([NSString instancesRespondToSelector:@selector(stringByAddingPercentEncodingWithAllowedCharacters:)]) {
        url = [NSURL URLWithString:[urlString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]]];
    } else {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
        url = [NSURL URLWithString:[urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }
#pragma clang diagnostic pop
    [Alltrack appWillOpenUrl:url];
}

- (void)trackAdRevenue:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *source = call.arguments[@"source"];
    NSString *payload = call.arguments[@"payload"];
    if (!([self isFieldValid:source]) || !([self isFieldValid:payload])) {
        return;
    }
    NSData *dataPayload = [payload dataUsingEncoding:NSUTF8StringEncoding];
    [Alltrack trackAdRevenue:source payload:dataPayload];
}

- (void)trackAdRevenueNew:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *source = call.arguments[@"source"];
    NSString *revenue = call.arguments[@"revenue"];
    NSString *currency = call.arguments[@"currency"];
    NSString *adImpressionsCount = call.arguments[@"adImpressionsCount"];
    NSString *adRevenueNetwork = call.arguments[@"adRevenueNetwork"];
    NSString *adRevenueUnit = call.arguments[@"adRevenueUnit"];
    NSString *adRevenuePlacement = call.arguments[@"adRevenuePlacement"];
    NSString *strCallbackParametersJson = call.arguments[@"callbackParameters"];
    NSString *strPartnerParametersJson = call.arguments[@"partnerParameters"];

    // Create ad revenue object.
    ALTAdRevenue *alltrackAdRevenue = [[ALTAdRevenue alloc] initWithSource:source];

    // Revenue.
    if ([self isFieldValid:revenue]) {
        double revenueValue = [revenue doubleValue];
        [alltrackAdRevenue setRevenue:revenueValue currency:currency];
    }

    // Ad impressions count.
    if ([self isFieldValid:adImpressionsCount]) {
        int adImpressionsCountValue = [adImpressionsCount intValue];
        [alltrackAdRevenue setAdImpressionsCount:adImpressionsCountValue];
    }

    // Ad revenue network.
    if ([self isFieldValid:adRevenueNetwork]) {
        [alltrackAdRevenue setAdRevenueNetwork:adRevenueNetwork];
    }
    
    // Ad revenue unit.
    if ([self isFieldValid:adRevenueUnit]) {
        [alltrackAdRevenue setAdRevenueUnit:adRevenueUnit];
    }
    
    // Ad revenue placement.
    if ([self isFieldValid:adRevenuePlacement]) {
        [alltrackAdRevenue setAdRevenuePlacement:adRevenuePlacement];
    }

    // Callback parameters.
    if (strCallbackParametersJson != nil) {
        NSData *callbackParametersData = [strCallbackParametersJson dataUsingEncoding:NSUTF8StringEncoding];
        id callbackParametersJson = [NSJSONSerialization JSONObjectWithData:callbackParametersData
                                                                    options:0
                                                                      error:NULL];
        for (id key in callbackParametersJson) {
            NSString *value = [callbackParametersJson objectForKey:key];
            [alltrackAdRevenue addCallbackParameter:key value:value];
        }
    }

    // Partner parameters.
    if (strPartnerParametersJson != nil) {
        NSData *partnerParametersData = [strPartnerParametersJson dataUsingEncoding:NSUTF8StringEncoding];
        id partnerParametersJson = [NSJSONSerialization JSONObjectWithData:partnerParametersData
                                                                   options:0
                                                                     error:NULL];
        for (id key in partnerParametersJson) {
            NSString *value = [partnerParametersJson objectForKey:key];
            [alltrackAdRevenue addPartnerParameter:key value:value];
        }
    }

    // Track ad revenue.
    [Alltrack trackAdRevenue:alltrackAdRevenue];
    result(nil);
}

- (void)trackPlayStoreSubscription:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    result([FlutterError errorWithCode:@"non_existing_method"
                               message:@"trackPlayStoreSubscription not available for iOS platform"
                               details:nil]);
}

- (void)trackAppStoreSubscription:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *price = call.arguments[@"price"];
    NSString *currency = call.arguments[@"currency"];
    NSString *transactionId = call.arguments[@"transactionId"];
    NSString *receipt = call.arguments[@"receipt"];
    NSString *transactionDate = call.arguments[@"transactionDate"];
    NSString *salesRegion = call.arguments[@"salesRegion"];
    // NSString *billingStore = call.arguments[@"billingStore"];
    NSString *strCallbackParametersJson = call.arguments[@"callbackParameters"];
    NSString *strPartnerParametersJson = call.arguments[@"partnerParameters"];

    // Price.
    NSDecimalNumber *priceValue;
    if ([self isFieldValid:price]) {
        priceValue = [NSDecimalNumber decimalNumberWithString:price];
    }

    // Receipt.
    NSData *receiptValue;
    if ([self isFieldValid:receipt]) {
        receiptValue = [receipt dataUsingEncoding:NSUTF8StringEncoding];
    }

    // Create subscription object.
    ALTSubscription *subscription = [[ALTSubscription alloc] initWithPrice:priceValue
                                                                  currency:currency
                                                             transactionId:transactionId
                                                                andReceipt:receiptValue];

    // Transaction date.
    if ([self isFieldValid:transactionDate]) {
        NSTimeInterval transactionDateInterval = [transactionDate doubleValue];
        NSDate *oTransactionDate = [NSDate dateWithTimeIntervalSince1970:transactionDateInterval];
        [subscription setTransactionDate:oTransactionDate];
    }

    // Sales region.
    if ([self isFieldValid:salesRegion]) {
        [subscription setSalesRegion:salesRegion];
    }

    // Callback parameters.
    if (strCallbackParametersJson != nil) {
        NSData *callbackParametersData = [strCallbackParametersJson dataUsingEncoding:NSUTF8StringEncoding];
        id callbackParametersJson = [NSJSONSerialization JSONObjectWithData:callbackParametersData
                                                                    options:0
                                                                      error:NULL];
        for (id key in callbackParametersJson) {
            NSString *value = [callbackParametersJson objectForKey:key];
            [subscription addCallbackParameter:key value:value];
        }
    }

    // Partner parameters.
    if (strPartnerParametersJson != nil) {
        NSData *partnerParametersData = [strPartnerParametersJson dataUsingEncoding:NSUTF8StringEncoding];
        id partnerParametersJson = [NSJSONSerialization JSONObjectWithData:partnerParametersData
                                                                   options:0
                                                                     error:NULL];
        for (id key in partnerParametersJson) {
            NSString *value = [partnerParametersJson objectForKey:key];
            [subscription addPartnerParameter:key value:value];
        }
    }

    // Track event.
    [Alltrack trackSubscription:subscription];
    result(nil);
}

- (void)getAttribution:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    ALTAttribution *attribution = [Alltrack attribution];
    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    if (attribution == nil) {
        result(dictionary);
    }
    
    [self addValueOrEmpty:attribution.trackerToken withKey:@"trackerToken" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.trackerName withKey:@"trackerName" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.network withKey:@"network" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.campaign withKey:@"campaign" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.creative withKey:@"creative" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.adgroup withKey:@"adgroup" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.clickLabel withKey:@"clickLabel" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.adid withKey:@"adid" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.costType withKey:@"costType" toDictionary:dictionary];
    [self addNumberOrEmpty:attribution.costAmount withKey:@"costAmount" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.costCurrency withKey:@"costCurrency" toDictionary:dictionary];
    result(dictionary);
}

- (void)getIdfa:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *idfa = [Alltrack idfa];
    result(idfa);
}

- (void)getGoogleAdId:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    result([FlutterError errorWithCode:@"non_existing_method"
                               message:@"getGoogleAdId not available for iOS platform"
                               details:nil]);
}

- (void)getSdkVersion:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *sdkVersion = [Alltrack sdkVersion];
    result(sdkVersion);
}

- (void)requestTrackingAuthorizationWithCompletionHandler:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    [Alltrack requestTrackingAuthorizationWithCompletionHandler:^(NSUInteger status) {
        result([NSNumber numberWithUnsignedLong:status]);
    }];
}

- (void)getAppTrackingAuthorizationStatus:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    result([NSNumber numberWithInt:[Alltrack appTrackingAuthorizationStatus]]);
}

- (void)updateConversionValue:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *conversionValue = call.arguments[@"conversionValue"];
    if ([self isFieldValid:conversionValue]) {
        [Alltrack updateConversionValue:[conversionValue intValue]];
    }
    result(nil);
}

- (void)trackThirdPartySharing:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSNumber *isEnabled = call.arguments[@"isEnabled"];
    NSString *strGranularOptions = call.arguments[@"granularOptions"];
    NSString *strPartnerSharingSettings = call.arguments[@"partnerSharingSettings"];

    // Create third party sharing object.
    ALTThirdPartySharing *alltrackThirdPartySharing = [[ALTThirdPartySharing alloc]
                                                     initWithIsEnabledNumberBool:[self isFieldValid:isEnabled] ? isEnabled : nil];

    // Granular options.
    if (strGranularOptions != nil) {
        NSArray *arrayGranularOptions = [strGranularOptions componentsSeparatedByString:@"__ALT__"];
        if (arrayGranularOptions != nil) {
            for (int i = 0; i < [arrayGranularOptions count]; i += 3) {
                [alltrackThirdPartySharing addGranularOption:[arrayGranularOptions objectAtIndex:i]
                                                       key:[arrayGranularOptions objectAtIndex:i+1]
                                                     value:[arrayGranularOptions objectAtIndex:i+2]];
            }
        }
    }

    // Partner sharing settings.
    if (strPartnerSharingSettings != nil) {
        NSArray *arrayPartnerSharingSettings = [strPartnerSharingSettings componentsSeparatedByString:@"__ALT__"];
        if (arrayPartnerSharingSettings != nil) {
            for (int i = 0; i < [arrayPartnerSharingSettings count]; i += 3) {
                [alltrackThirdPartySharing addPartnerSharingSetting:[arrayPartnerSharingSettings objectAtIndex:i]
                                                              key:[arrayPartnerSharingSettings objectAtIndex:i+1]
                                                            value:[[arrayPartnerSharingSettings objectAtIndex:i+2] boolValue]];
            }
        }
    }

    // Track third party sharing.
    [Alltrack trackThirdPartySharing:alltrackThirdPartySharing];
    result(nil);
}

- (void)trackMeasurementConsent:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *measurementConsent = call.arguments[@"measurementConsent"];
    if ([self isFieldValid:measurementConsent]) {
        [Alltrack trackMeasurementConsent:[measurementConsent boolValue]];
    }
    result(nil);
}

- (void)getLastDeeplink:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSURL *lastDeeplink = [Alltrack lastDeeplink];
    if (![self isFieldValid:lastDeeplink]) {
        result(nil);
    } else {
        result([lastDeeplink absoluteString]);
    }
}

- (void)updateConversionValueWithErrorCallback:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *conversionValue = call.arguments[@"conversionValue"];
    if ([self isFieldValid:conversionValue]) {
        [Alltrack updatePostbackConversionValue:[conversionValue intValue]
                            completionHandler:^(NSError * _Nullable error) {
            result([error localizedDescription]);
        }];
    }
}

- (void)updateConversionValueWithErrorCallbackSkad4:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    NSString *conversionValue = call.arguments[@"conversionValue"];
    NSString *coarseValue = call.arguments[@"coarseValue"];
    NSString *lockWindow = call.arguments[@"lockWindow"];
    if ([self isFieldValid:conversionValue] &&
        [self isFieldValid:coarseValue] &&
        [self isFieldValid:lockWindow]) {
        [Alltrack updatePostbackConversionValue:[conversionValue intValue]
                                  coarseValue:coarseValue
                                   lockWindow:(BOOL)lockWindow
                            completionHandler:^(NSError * _Nullable error) {
            result([error localizedDescription]);
        }];
    }
}

- (void)setTestOptions:(FlutterMethodCall *)call withResult:(FlutterResult)result {
    AlltrackTestOptions *testOptions = [[AlltrackTestOptions alloc] init];
    NSString *baseUrl = call.arguments[@"baseUrl"];
    NSString *gdprUrl = call.arguments[@"gdprUrl"];
    NSString *subscriptionUrl = call.arguments[@"subscriptionUrl"];
    NSString *extraPath = call.arguments[@"extraPath"];
    NSString *timerIntervalInMilliseconds = call.arguments[@"timerIntervalInMilliseconds"];
    NSString *timerStartInMilliseconds = call.arguments[@"timerStartInMilliseconds"];
    NSString *sessionIntervalInMilliseconds = call.arguments[@"sessionIntervalInMilliseconds"];
    NSString *subsessionIntervalInMilliseconds = call.arguments[@"subsessionIntervalInMilliseconds"];
    NSString *teardown = call.arguments[@"teardown"];
    NSString *deleteState = call.arguments[@"deleteState"];
    NSString *noBackoffWait = call.arguments[@"noBackoffWait"];
    NSString *adServicesFrameworkEnabled = call.arguments[@"adServicesFrameworkEnabled"];
    
    if ([self isFieldValid:baseUrl]) {
        testOptions.baseUrl = baseUrl;
    }
    if ([self isFieldValid:gdprUrl]) {
        testOptions.gdprUrl = gdprUrl;
    }
    if ([self isFieldValid:subscriptionUrl]) {
        testOptions.subscriptionUrl = subscriptionUrl;
    }
    if ([self isFieldValid:extraPath]) {
        testOptions.extraPath = extraPath;
    }
    if ([self isFieldValid:timerIntervalInMilliseconds]) {
        testOptions.timerIntervalInMilliseconds = [NSNumber numberWithLongLong:[timerIntervalInMilliseconds longLongValue]];
    }
    if ([self isFieldValid:timerStartInMilliseconds]) {
        testOptions.timerStartInMilliseconds = [NSNumber numberWithLongLong:[timerStartInMilliseconds longLongValue]];
    }
    if ([self isFieldValid:sessionIntervalInMilliseconds]) {
        testOptions.sessionIntervalInMilliseconds = [NSNumber numberWithLongLong:[sessionIntervalInMilliseconds longLongValue]];
    }
    if ([self isFieldValid:subsessionIntervalInMilliseconds]) {
        testOptions.subsessionIntervalInMilliseconds = [NSNumber numberWithLongLong:[subsessionIntervalInMilliseconds longLongValue]];
    }
    if ([self isFieldValid:teardown]) {
        testOptions.teardown = [teardown boolValue];
        if (testOptions.teardown) {
            [AlltrackSdkDelegate teardown];
        }
    }
    if ([self isFieldValid:deleteState]) {
        testOptions.deleteState = [deleteState boolValue];
    }
    if ([self isFieldValid:noBackoffWait]) {
        testOptions.noBackoffWait = [noBackoffWait boolValue];
    }
    if ([self isFieldValid:adServicesFrameworkEnabled]) {
        testOptions.adServicesFrameworkEnabled = [adServicesFrameworkEnabled boolValue];
    }
    
    [Alltrack setTestOptions:testOptions];
}

- (BOOL)isFieldValid:(NSObject *)field {
    if (field == nil) {
        return NO;
    }

    // Check if its an instance of the singleton NSNull.
    if ([field isKindOfClass:[NSNull class]]) {
        return NO;
    }

    // If field can be converted to a string, check if it has any content.
    NSString *str = [NSString stringWithFormat:@"%@", field];
    if (str != nil) {
        if ([str length] == 0) {
            return NO;
        }
    }

    return YES;
}

- (void)addValueOrEmpty:(NSObject *)value
                withKey:(NSString *)key
           toDictionary:(NSMutableDictionary *)dictionary {
    if (nil != value) {
        [dictionary setObject:[NSString stringWithFormat:@"%@", value] forKey:key];
    } else {
        [dictionary setObject:@"" forKey:key];
    }
}

- (void)addNumberOrEmpty:(NSNumber *)value
                 withKey:(NSString *)key
            toDictionary:(NSMutableDictionary *)dictionary {
    if (nil != value) {
        [dictionary setObject:[value stringValue] forKey:key];
    } else {
        [dictionary setObject:@"" forKey:key];
    }
}

@end
