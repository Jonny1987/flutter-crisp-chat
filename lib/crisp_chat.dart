import 'package:flutter/foundation.dart';

import 'src/config.dart';
import 'src/flutter_crisp_chat_platform_interface.dart';
import 'src/helper.dart';

export 'src/config.dart';

/// [FlutterCrispChat] to call the native platform method.
class FlutterCrispChat {
  /// [openCrispChat] to open crisp chat. This method needs
  /// a required argument `CrispConfig` object which will be used to configure
  /// Crisp chat.
  static Future<void> openCrispChat({required CrispConfig config}) {
    // Validate email if provided
    final email = config.user?.email;
    if (email != null && !email.isEmail) {
      throw Exception("User email is incorrect!");
    }

    // Validate company URL if provided
    final url = config.user?.company?.url;
    if (url != null && !url.isUrl) {
      throw Exception("Company URL is incorrect!");
    }

    // Call the platform-specific method to open Crisp chat
    return FlutterCrispChatPlatform.instance.openCrispChat(config: config);
  }

  /// [resetCrispChatSession] is called when to reset the session.
  static Future<void> resetCrispChatSession() {
    return FlutterCrispChatPlatform.instance.resetCrispChatSession();
  }

  /// [setSessionString]  is to set session data string.
  /// [This data only send while [openCrispChat] is called.]
  static void setSessionString({required String key, required String value}) {
    // Ensure non-empty key and value
    if (key.isEmpty || value.isEmpty) {
      throw Exception("Key or value cannot be empty!");
    }
    FlutterCrispChatPlatform.instance.setSessionString(key: key, value: value);
  }

  /// [setSessionInt]  is to set session data int.
  /// [This data only send while [openCrispChat] is called.]
  static void setSessionInt({required String key, required int value}) {
    // Ensure non-empty key
    if (key.isEmpty) {
      throw Exception("Key cannot be empty!");
    }
    FlutterCrispChatPlatform.instance.setSessionInt(key: key, value: value);
  }

  /// [getSessionIdentifier] retrieves the current session identifier.
  /// This method returns the session ID or null if no session is active.
  static Future<String?> getSessionIdentifier() async {
    try {
      final sessionId =
          await FlutterCrispChatPlatform.instance.getSessionIdentifier();
      if (sessionId == null || sessionId.isEmpty) {
        throw Exception("No active session identifier found!");
      }
      return sessionId;
    } catch (e) {
      if (kDebugMode) {
        print("Error retrieving session identifier: $e");
      }
      return null;
    }
  }

  /// [setSessionSegments] Sets a collection of session segments
  /// and optionally overwrite existing ones (default is false)
  static void setSessionSegments({
    required List<String> segments,
    bool overwrite = false,
  }) {
    FlutterCrispChatPlatform.instance.setSessionSegments(
      segments: segments,
      overwrite: overwrite,
    );
  }

  /// Set a callback for when a Crisp session is loaded.
  static set onSessionLoaded(void Function(String sessionId)? callback) =>
      FlutterCrispChatPlatform.instance.onSessionLoaded = callback;

  /// Set a callback for when the Crisp chat is opened.
  static set onChatOpened(void Function()? callback) =>
      FlutterCrispChatPlatform.instance.onChatOpened = callback;

  /// Set a callback for when the Crisp chat is closed.
  static set onChatClosed(void Function()? callback) =>
      FlutterCrispChatPlatform.instance.onChatClosed = callback;

  /// Set a callback for when a message is sent in Crisp chat.
  static set onMessageSent(void Function(dynamic message)? callback) =>
      FlutterCrispChatPlatform.instance.onMessageSent = callback;

  /// Set a callback for when a message is received in Crisp chat.
  static set onMessageReceived(void Function(dynamic message)? callback) =>
      FlutterCrispChatPlatform.instance.onMessageReceived = callback;
}
