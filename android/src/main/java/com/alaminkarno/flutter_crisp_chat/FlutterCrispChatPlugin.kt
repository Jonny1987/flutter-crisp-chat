package com.alaminkarno.flutter_crisp_chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.alaminkarno.flutter_crisp_chat.config.CrispConfig
import im.crisp.client.ChatActivity
import im.crisp.client.Crisp
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FlutterCrispChatPlugin */
class FlutterCrispChatPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    companion object {
        private const val CHANNEL_NAME = "flutter_crisp_chat"
        private const val EVENT_SESSION_LOADED = "onSessionLoaded"
        private const val EVENT_CHAT_OPENED = "onChatOpened"
        private const val EVENT_CHAT_CLOSED = "onChatClosed"
        private const val EVENT_MESSAGE_SENT = "onMessageSent"
        private const val EVENT_MESSAGE_RECEIVED = "onMessageReceived"

        private var staticChannel: MethodChannel? = null
    }

    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private var activity: Activity? = null

    private val crispEventsCallback = object : Crisp.EventsCallback {
        override fun onSessionLoaded(sessionId: String) {
            staticChannel?.invokeMethod(EVENT_SESSION_LOADED, sessionId)
        }
        override fun onChatOpened() {
            staticChannel?.invokeMethod(EVENT_CHAT_OPENED, null)
        }
        override fun onChatClosed() {
            staticChannel?.invokeMethod(EVENT_CHAT_CLOSED, null)
        }
        override fun onMessageSent(message: im.crisp.client.Message) {
            staticChannel?.invokeMethod(EVENT_MESSAGE_SENT, message.toJSON())
        }
        override fun onMessageReceived(message: im.crisp.client.Message) {
            staticChannel?.invokeMethod(EVENT_MESSAGE_RECEIVED, message.toJSON())
        }
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext

        channel = MethodChannel(flutterPluginBinding.binaryMessenger, CHANNEL_NAME)
        channel.setMethodCallHandler(this)
        staticChannel = channel

        // Register the callback automatically
        Crisp.addCallback(crispEventsCallback)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "openCrispChat" -> {
                val args = call.arguments as HashMap<String?, Any?>
                if (args != null) {
                    val config = CrispConfig.fromJson(args)
                    Crisp.configure(context, config.websiteId)
                    setCrispData(config);
                    openActivity()
                } else {
                    result.notImplemented()
                }

            }

            "resetCrispChatSession" -> {
                Crisp.resetChatSession(context)
            }
            
            "setSessionString" -> {
                val args = call.arguments as HashMap<String?, Any?>
                if (args != null) {
                    val key = args["key"] as String
                    val value = args["value"] as String
                    Crisp.setSessionString(key, value)
                }
            }

            "setSessionInt" -> {
                val args = call.arguments as HashMap<String?, Any?>
                if (args != null) {
                    val key = args["key"] as String
                    val value = args["value"] as Int
                    Crisp.setSessionInt(key, value)
                }
            }

            "getSessionIdentifier" -> {
                val sessionId = Crisp.getSessionIdentifier(context)
                if (sessionId != null) {
                    result.success(sessionId)
                } else {
                    result.error("NO_SESSION", "No active session found", null)
                }
            }

            else -> result.notImplemented()
        }
    }

    private fun setCrispData(config: CrispConfig) {
        config.tokenId?.let {
            Crisp.setTokenID(it)
        }
        config.sessionSegment?.let {
            Crisp.setSessionSegment(it)
        }
        config.user?.let { user ->
            user.nickName?.let { Crisp.setUserNickname(it) }
            user.email?.let { Crisp.setUserEmail(it) }
            user.avatar?.let { Crisp.setUserAvatar(it) }
            user.phone?.let { Crisp.setUserPhone(it) }
            user.company?.let { Crisp.setUserCompany(it.toCrispCompany()) }
        }
    }

    private fun openActivity() {
        val intent = Intent(context, ChatActivity::class.java)
        if (activity != null) {
            activity?.startActivity(intent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        staticChannel = null
        context = null
    }
}