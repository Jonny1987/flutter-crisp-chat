import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'config.dart';
import 'flutter_crisp_chat_platform_interface.dart';

/// An implementation of [FlutterCrispChatPlatform] that uses method channels.
class MethodChannelFlutterCrispChat extends FlutterCrispChatPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_crisp_chat');

  // Private fields to store event callbacks
  void Function(String sessionId)? _onSessionLoaded;
  void Function()? _onChatOpened;
  void Function()? _onChatClosed;
  void Function(dynamic message)? _onMessageSent;
  void Function(dynamic message)? _onMessageReceived;

  MethodChannelFlutterCrispChat() {
    methodChannel.setMethodCallHandler(_handleNativeEvent);
  }

  Future<void> _handleNativeEvent(MethodCall call) async {
    switch (call.method) {
      case 'onSessionLoaded':
        if (_onSessionLoaded != null && call.arguments is String) {
          _onSessionLoaded!(call.arguments as String);
        }
        break;
      case 'onChatOpened':
        if (_onChatOpened != null) {
          _onChatOpened!();
        }
        break;
      case 'onChatClosed':
        if (_onChatClosed != null) {
          _onChatClosed!();
        }
        break;
      case 'onMessageSent':
        if (_onMessageSent != null) {
          _onMessageSent!(call.arguments);
        }
        break;
      case 'onMessageReceived':
        if (_onMessageReceived != null) {
          _onMessageReceived!(call.arguments);
        }
        break;
    }
  }

  // Event callback setters
  @override
  set onSessionLoaded(void Function(String sessionId)? callback) =>
      _onSessionLoaded = callback;
  @override
  set onChatOpened(void Function()? callback) => _onChatOpened = callback;
  @override
  set onChatClosed(void Function()? callback) => _onChatClosed = callback;
  @override
  set onMessageSent(void Function(dynamic message)? callback) =>
      _onMessageSent = callback;
  @override
  set onMessageReceived(void Function(dynamic message)? callback) =>
      _onMessageReceived = callback;

  /// [openCrispChat] is use to invoke the Method Channel and call native
  /// code with arguments `websiteID`.
  @override
  Future<void> openCrispChat({required CrispConfig config}) async {
    await methodChannel.invokeMethod<CrispConfig>(
        'openCrispChat', config.toJson());
  }

  /// [resetCrispChatSession] is use to invoke the Method Channel and call native
  /// code with no arguments and this will reset the crisp chat session.
  @override
  Future<void> resetCrispChatSession() async {
    await methodChannel.invokeMethod('resetCrispChatSession');
  }

  /// [setSessionString] is used to invoke the Method Channel and call native
  /// code with arguments `key` and `value`.
  @override
  void setSessionString({required String key, required String value}) {
    methodChannel.invokeMethod('setSessionString', <String, String>{
      'key': key,
      'value': value,
    });
  }

  /// [setSessionInt] is used to invoke the Method Channel and call native
  /// code with arguments `key` and `value`.
  @override
  void setSessionInt({required String key, required int value}) {
    methodChannel.invokeMethod('setSessionInt', <String, dynamic>{
      'key': key,
      'value': value,
    });
  }

  /// [getSessionIdentifier] retrieves the current session identifier from the native platform.
  @override
  Future<String?> getSessionIdentifier() async {
    try {
      final sessionId =
          await methodChannel.invokeMethod<String>('getSessionIdentifier');
      return sessionId;
    } on PlatformException catch (e) {
      debugPrint("Failed to get session identifier: '${e.message}'.");
      return null;
    }
  }

  /// [setSessionSegments] Sets a collection of session segments
  /// and optionally overwrite existing ones (default is false)
  @override
  void setSessionSegments({
    required List<String> segments,
    bool overwrite = false,
  }) {
    methodChannel.invokeMethod(
      'setSessionSegments',
      <String, dynamic>{
        'segments': segments,
        'overwrite': overwrite,
      },
    );
  }
}
