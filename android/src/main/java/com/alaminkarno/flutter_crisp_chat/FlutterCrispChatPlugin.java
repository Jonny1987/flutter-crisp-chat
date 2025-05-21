package com.alaminkarno.flutter_crisp_chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alaminkarno.flutter_crisp_chat.config.CrispConfig;

import java.util.HashMap;
import java.util.List;

import im.crisp.client.external.ChatActivity;
import im.crisp.client.external.Crisp;
import im.crisp.client.external.EventsCallback;
import im.crisp.client.external.data.message.Message;

import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/// [FlutterCrispChatPlugin] using [FlutterPlugin], [MethodCallHandler] and [ActivityAware]
/// to handling Method Channel Callback from Flutter and Open new Activity.

/**
 * FlutterCrispChatPlugin
 */
public class FlutterCrispChatPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {

    private static final String CHANNEL_NAME = "flutter_crisp_chat";

    private static final String EVENT_SESSION_LOADED = "onSessionLoaded";
    private static final String EVENT_CHAT_OPENED = "onChatOpened";
    private static final String EVENT_CHAT_CLOSED = "onChatClosed";
    private static final String EVENT_MESSAGE_SENT = "onMessageSent";
    private static final String EVENT_MESSAGE_RECEIVED = "onMessageReceived";

    private static MethodChannel staticChannel;

    private static final EventsCallback CRISP_EVENTS_CALLBACK = new EventsCallback() {
        @Override
        public void onSessionLoaded(@NonNull final String sessionId) {
            Log.d("FlutterCrispChatPlugin", "Java: Sending event: " + EVENT_SESSION_LOADED + " with sessionId: " + sessionId);
            if (staticChannel != null) {
                staticChannel.invokeMethod(EVENT_SESSION_LOADED, sessionId);
            }
        }

        @Override
        public void onChatOpened() {
            Log.d("FlutterCrispChatPlugin", "Java: Sending event: " + EVENT_CHAT_OPENED);
            if (staticChannel != null) {
                staticChannel.invokeMethod(EVENT_CHAT_OPENED, null);
            } else {
                Log.d("FlutterCrispChatPlugin", "Java: staticChannel is null");
            }
        }

        @Override
        public void onChatClosed() {
            Log.d("FlutterCrispChatPlugin", "Java: Sending event: " + EVENT_CHAT_CLOSED);
            if (staticChannel != null) {
                staticChannel.invokeMethod(EVENT_CHAT_CLOSED, null);
            }
        }

        @Override
        public void onMessageSent(@NonNull final Message message) {
            Log.d("FlutterCrispChatPlugin", "Java: Sending event: " + EVENT_MESSAGE_SENT + " with message: " + message.toJSON());
            if (staticChannel != null) {
                staticChannel.invokeMethod(EVENT_MESSAGE_SENT, message.toJSON());
            }
        }

        @Override
        public void onMessageReceived(@NonNull final Message message) {
            Log.d("FlutterCrispChatPlugin", "Java: Sending event: " + EVENT_MESSAGE_RECEIVED + " with message: " + message.toJSON());
            if (staticChannel != null) {
                staticChannel.invokeMethod(EVENT_MESSAGE_RECEIVED, message.toJSON());
            }
        }
    };

    private MethodChannel channel;
    private Context context;
    private Activity activity;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext();

        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL_NAME);
        channel.setMethodCallHandler(this);
        staticChannel = channel;

        // Register the callback automatically
        Crisp.addCallback(CRISP_EVENTS_CALLBACK);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        this.activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        this.activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        this.activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        this.activity = null;
    }

    /// [onMethodCall] if for handling method call from flutter end.
    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("openCrispChat")) {
            HashMap<String, Object> args = (HashMap<String, Object>) call.arguments;
            if (args != null) {
                CrispConfig config = CrispConfig.fromJson(args);
                if (config.tokenId != null) {
                    Crisp.configure(context, config.websiteId, config.tokenId);
                } else {
                    Crisp.configure(context, config.websiteId);
                }

                Crisp.enableNotifications(context, config.enableNotifications);
                setCrispData(context, config);
                openActivity();
                result.success(null);
            } else {
                result.notImplemented();
            }
        } else if (call.method.equals("resetCrispChatSession")) {
            Crisp.resetChatSession(context);
            result.success(null);
        } else if (call.method.equals("setSessionString")) {
            HashMap<String, Object> args = (HashMap<String, Object>) call.arguments;
            if (args != null) {
                String key = (String) args.get("key");
                String value = (String) args.get("value");
                Crisp.setSessionString(key, value);
                result.success(null);
            } else {
                result.notImplemented();
            }
        } else if (call.method.equals("setSessionInt")) {
            HashMap<String, Object> args = (HashMap<String, Object>) call.arguments;
            if (args != null) {
                String key = (String) args.get("key");
                int value = (int) args.get("value");
                Crisp.setSessionInt(key, value);
                result.success(null);
            } else {
                result.notImplemented();
            }
        } else if (call.method.equals("getSessionIdentifier")) {
            String sessionId = Crisp.getSessionIdentifier(context);
            if (sessionId != null) {
                result.success(sessionId);
            } else {
                result.error("NO_SESSION", "No active session found", null);
            }
        } else if (call.method.equals("setSessionSegments")) {
            HashMap<String, Object> args = (HashMap<String, Object>) call.arguments;
            if (args != null) {
                List<String> segments = (List<String>) args.get("segments");
                boolean overwrite = (boolean) args.get("overwrite");
                Crisp.setSessionSegments(segments, overwrite);
                result.success(null);
            } else {
                result.notImplemented();
            }
        }
        else {
            result.notImplemented();
        }
    }

    private void setCrispData(Context context, CrispConfig config) {
        if (config.tokenId != null) {
            Crisp.setTokenID(context, config.tokenId);
        }
        if (config.sessionSegment != null) {
            Crisp.setSessionSegment(config.sessionSegment);
        }
        if (config.user != null) {
            if (config.user.nickName != null) {
                Crisp.setUserNickname(config.user.nickName);
            }
            if (config.user.email != null) {
                boolean result =  Crisp.setUserEmail(config.user.email);
                if(!result){
                    Log.d("CRSIP_CHAT","Email not set");
                }
            }
            if (config.user.avatar != null) {
               boolean result = Crisp.setUserAvatar(config.user.avatar);
               if(!result){
                   Log.d("CRSIP_CHAT","Avatar not set");
               }
            }
            if (config.user.phone != null) {
                boolean result =  Crisp.setUserPhone(config.user.phone);
                if(!result){
                    Log.d("CRSIP_CHAT","Phone not set");
                }
            }
            if (config.user.company != null) {
                Crisp.setUserCompany(config.user.company.toCrispCompany());
            }
        }

    }

    ///[openActivity] is opening ChatView Activity of CrispChat SDK.
    private void openActivity() {
        Intent intent = new Intent(context, ChatActivity.class);
        if (activity != null) {
            activity.startActivity(intent);
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        staticChannel = null;
        context = null;
    }

}