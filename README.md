# Crisp Chat

A flutter plugin package for using crisp chat natively on Android & iOS.

![Crisp Chat](https://github.com/alamin-karno/flutter-crisp-chat/blob/main/example/screenshots/image-1.png?raw=true)

[![pub version](https://img.shields.io/pub/v/crisp_chat?color=%2300b0ff&label=crisp_chat&style=flat-square)](https://pub.dev/packages/crisp_chat)
[![Last Commit](https://img.shields.io/github/last-commit/alamin-karno/flutter-crisp-chat?color=%23ffa000&style=flat-square)](https://github.com/alamin-karno/flutter-crisp-chat/commits/main/)
[![License](https://img.shields.io/github/license/alamin-karno/flutter-crisp-chat?style=flat-square)](https://github.com/alamin-karno/flutter-crisp-chat?tab=MIT-1-ov-file)
[![GitHub Contributors](https://img.shields.io/github/contributors/alamin-karno/flutter-crisp-chat)](https://github.com/alamin-karno/flutter-crisp-chat/graphs/contributors)
[![Stars](https://img.shields.io/github/stars/alamin-karno/flutter-crisp-chat?style=social)](https://pub.dev/packages/crisp_chat)
[![GitHub Closed Issues](https://img.shields.io/github/issues-closed-raw/alamin-karno/flutter-crisp-chat)](https://github.com/alamin-karno/flutter-crisp-chat/issues?q=is%3Aissue+is%3Aclosed)
[![Sponsors](https://img.shields.io/github/sponsors/alamin-karno)](https://patreon.com/alamin_karno)
[![Buy Me A Coffee](https://img.shields.io/badge/buy%20me%20a%20coffee-donate-yellow.svg)](https://buymeacoffee.com/alaminkarno)

Chat with website visitors, integrate your favorite tools, and deliver a great customer experience. - Crisp. The `Crisp Chat` is a package that provides a simple way to open chat window using native channel. Connect with Crisp Chat, register a user to chat (or not) and render a chat widget. Tested on Android and iOS. 

`Note: If anyone want to contribute on this project is most welcome. If you have any idea or suggestion, please feel free to contact me.`



## Features

- [x] Null-safety enable
- [x] Easy to use
- [x] Customizable
- [x] User configuration with company and geoLocation
- [x] Supports for iOS & Android

## Installation

### 1. Add Crisp dependency
---

First, add `crisp_chat` as a [dependency in your pubspec.yaml file](https://flutter.dev/using-packages/).

To use the Flutter Crisp Chat, simply import the `crisp_chat` package:

Run this on your project terminal:

```yaml
flutter pub add crisp_chat
```

or manually configure pubspec.yml file

```yaml
dependencies:
  flutter:
    sdk: flutter
  crisp_chat: ^2.1.0
```

### 2. Setup platform specific settings
---

#### iOS

Add two rows to the `ios/Runner/Info.plist`:

* key `Privacy - Camera Usage Description` and a usage description.
* key `Privacy - Photo Library Additions Usage Description` and a usage description.
* key `Privacy - Microphone Usage Description` and a usage description.

If editing `Info.plist` as text, add:

```html
<key>NSCameraUsageDescription</key>
<string>your usage description here</string>
<key>NSPhotoLibraryAddUsageDescription</key>
<string>your usage description here</string>
<key>NSMicrophoneUsageDescription</key>
<string>your usage description here</string>
```

#### Android

Add Internet permission on `AndroidManifest.xml` in your `android/app/src/main/AndroidManifest.xml` file.

```html
<uses-permission android:name="android.permission.INTERNET"/>
```

Change the minimum Compile SDK version to 34 (or higher) in your `android/app/build.gradle` file.

```groovy
compileSdkVersion 34
```

Change the minimum Android SDK version to 21 (or higher) in your `android/app/build.gradle` file.


```groovy
minSdkVersion 21
```
---
##### *(Optional)* Add Crisp authority and path to your FileProvider in `AndroidManifest.xml` (If your app declares a FileProvider in its AndroidManifest.xml)

```html
<provider android:name="androidx.core.content.FileProvider"
  android:authorities="${applicationId}.fileprovider;${applicationId}.im.crisp.client.uploadfileprovider"
  android:exported="false"
  android:grantUriPermissions="true"
  tools:replace="android:authorities">
  <meta-data android:name="android.support.FILE_PROVIDER_PATHS" 
    android:resource="@xml/file_paths"
    tools:replace="android:resource" />
</provider>
```

and `res/xml/file_paths.xml` add this 

```html
<files-path name="crisp_sdk_attachments" path="im.crisp.client/attachments/" />
```

### 3. Configure your app to receive Crisp notifications
---

#### a). Create a Firebase project and add it to your Android app
In order to complete this step, follow the Firebase [Get started](https://firebase.google.com/docs/android/setup) guide.

At the end of it, also add the following dependency to your project.
```yaml
flutter pub add firebase_core
flutter pub add firebase_messaging
```

#### b). Enable Push notifications in Crisp dashboard (Android)
- Go to your Firebase **Project settings**,
- Go to the **Cloud Messaging** tab,
- In the Firebase **Cloud Messaging API (V1)** section, copy your *Sender ID (1)*, you will need it later.

![Copy your Firebase Cloud Messaging Sender ID](https://github.com/user-attachments/assets/778fcfdd-a9ad-465b-b425-a0b45bf5f0eb)

- Copy your Firebase Cloud Messaging Sender ID
- Go to the **Service accounts** tab,
- In the **Firebase Admin SDK** section, click on the **Generate new private key (2)** button and save it for later.
  
![Generate and download your Firebase Admin private key](https://github.com/user-attachments/assets/99d7faf3-c1db-41b4-afc9-4bbd64bec1f7)

- Generate and download your Firebase Admin private key
- Go to your **[Crisp Dashboard](https://app.crisp.chat/)**,
- Select your Workspace,
- Go to **Settings** > **Chatbox Settings** > **Push Notifications**,
- Under the **Firebase Cloud Messaging** section :
        - Enable the **Notify users using Android (3)** option,
        - Paste the **Sender ID** you have copied previously into the **Project ID (4)** field,
        - Select or drag your **Firebase Admin private key** file you have downloaded earlier in the **Certificate (5)** box,
        - Click on the Verify **Credentials (6)** button.
  
![Enable Push Notifications in Crisp dashboard](https://github.com/user-attachments/assets/1fe0225e-4a1b-49bd-8814-c4d662fbf703)

- Crisp will notify you that it is checking FCM Credentials in order to send notifications to your users.
  
![Crisp checking FCM Credentials](https://github.com/user-attachments/assets/66623e73-3b92-4c79-b6ed-9db696ff1bd9)

- Finally, if FCM Credentials are valid, Crisp will update the Push Notifications status to **live**.
  
![Crisp checking FCM Credentials](https://github.com/user-attachments/assets/9e6f902a-f37b-4d79-a5d6-8fe25e6a8e7f)

#### c). Handle Push notifications in your Android app

You just have to declare our `CrispNotificationService` in the application tag of your `AndroidManifest.xml`.

```xml
<service
    android:name="im.crisp.client.external.notification.CrispNotificationService"
    android:exported="false">
    <intent-filter>
      <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
  </service>
```

Notifications will be handled by **Crisp** `CrispNotificationService` and a tap on it will launch your `MainActivity` and open **Crisp** `ChatActivity` with the corresponding session.

#### d). Enable Push notifications in Crisp dashboard (iOS)

- Create an **APNs-enabled** private key in your Apple Developer account. See the [Apple documentation](https://developer.apple.com/help/account/manage-keys/create-a-private-key/) for detailed instructions.
- Upload your key and configure push notifications in the Crisp web app at **Settings** > **Chatbox Settings** > **Push Notifications**.
- Add the **“Push Notifications”** capability to your app:
    - Open your project in **Xcode**
    - Select your target
    - Go to the **“Signing & Capabilities”** tab
    - Click the **“+”** button and add **“Push Notifications”**
 
![Push Notifications](https://github.com/user-attachments/assets/8581c872-f836-45f6-9a8c-7a5c5a998cea)


#### e). Ensure Firebase initialization in your Flutter project

```dart
@pragma('vm:entry-point')
Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  await Firebase.initializeApp();

  if (kDebugMode) {
    print("Handling a background message: ${message.messageId}");
  }
}

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
  runApp(const MyApp());
}
```
      
### 4. Usage
---

To open ChatView for crisp, use the `openCrispChat` method of the `FlutterCrispChat` class:

#### First Imported Package:

```dart
import 'package:crisp_chat/crisp_chat.dart';
```

#### Then:

```dart
 final String websiteID = 'YOUR_WEBSITE_KEY';
 late CrispConfig config;

   @override
  void initState() {
    super.initState();
    config = CrispConfig(
      websiteID: websiteID,
    );
  }
```


 ```dart 
   @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Crisp Chat'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: () async {
                  await FlutterCrispChat.openCrispChat(config: config);
                  FlutterCrispChat.setSessionString(
                    key: "a_string",
                    value: "Crisp Chat",
                  );
                  FlutterCrispChat.setSessionInt(
                    key: "a_number",
                    value: 12345,
                  );

                  /// Checking session ID After 5 sec
                  await Future.delayed(const Duration(seconds: 5), () async {
                    String? sessionId =
                    await FlutterCrispChat.getSessionIdentifier();
                    if (sessionId != null) {
                      if (kDebugMode) {
                        print('Session ID: $sessionId');
                      }
                    } else {
                      if (kDebugMode) {
                        print('No active session found!');
                      }
                    }
                  });
                },
                child: const Text('Open Crisp Chat'),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: () async {
                  await FlutterCrispChat.resetCrispChatSession();
                },
                child: const Text('Reset Chat Session'),
              ),
            ],
          ),
        ),
      ),
    );
  }
  ```

To use this code, you will need to provide your own Crisp website ID. You can do this by replacing `YOUR_WEBSITE_KEY` with your own website ID. Once you have done this, you can run the app and press the `"Open Crisp Chat"` button to launch the chat window. You can add more information using `CrispConfig`.

### Get your Website ID:
Go to your [Crisp Dashboard](https://app.crisp.chat/), and copy your Website ID:

![Crisp Dashboard](https://github.com/alamin-karno/flutter-crisp-chat/blob/main/example/screenshots/image.png?raw=true)

## Screenshot (GIF)

|                                                            Android  (GIF)                                                             |                                                           iOS    (GIF)                                                            |
|:-------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/alamin-karno/flutter-crisp-chat/blob/main/example/screenshots/crisp_android.gif?raw=true" width = "250"> | <img src="https://github.com/alamin-karno/flutter-crisp-chat/blob/main/example/screenshots/crisp_ios.gif?raw=true" width = "250"> |
|                                                           Android  (Image)                                                            |                                                          iOS    (Image)                                                           |
| <img src="https://github.com/alamin-karno/flutter-crisp-chat/blob/main/example/screenshots/crisp_android.png?raw=true" width = "250"> | <img src="https://github.com/alamin-karno/flutter-crisp-chat/blob/main/example/screenshots/crisp_ios.png?raw=true" width = "250"> |


## Examples of companies using Crisp Chat

- [Rokomari.com](https://rkmri.co/32ESMmTSAeIe/)
- [L'Algo de Paulo](https://lalgodepaulo.com/)


## Additional information

- [Flutter Crisp Chat (pub.dev)](https://pub.dev/packages/crisp_chat)
- [Flutter Crisp Chat (GitHub)](https://github.com/alamin-karno/flutter-crisp-chat)

### Project Maintainer ❤️

| <img src="https://avatars.githubusercontent.com/alamin-karno" width="100px"> |
|:----------------------------------------------------------------------------:|
|              [**Md. Al-Amin**](https://github.com/alamin-karno)              |

### ✨VALUABLE CONTRIBUTORS✨

[![Contributors](https://contrib.rocks/image?repo=alamin-karno/flutter-crisp-chat)](https://github.com/alamin-karno/flutter-crisp-chat/graphs/contributors)

### Happy Coding 👨‍💻

## Credits
* Crisp Android and iOS SDK is owned and maintained by [Crisp IM SAS](https://crisp.chat/en/).

 You can chat with them on [crisp](https://crisp.chat/) or follow them on Twitter at [Crisp_im](https://twitter.com/crisp_im).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
