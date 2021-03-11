# Velocidi SDK
![Build Status](https://github.com/velocidi/velocidi-android-sdk/workflows/Android%20CI/badge.svg)
[![codecov](https://codecov.io/gh/velocidi/velocidi-android-sdk/branch/master/graph/badge.svg)](https://codecov.io/gh/velocidi/velocidi-android-sdk)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.velocidi/velocidi-android-sdk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.velocidi/velocidi-android-sdk)

Open-source SDK to integrate Android Apps with Velocidi Private CDP.

## Installation

Velocidi SDK is published to Maven central as a single module. Please reference the badge above or go to the [release page](https://github.com/velocidi/velocidi-android-sdk/releases) to check our latest version.

To install the SDk in your application you just need to add the following dependency to the `app/build.gradle` file.

```gradle
dependencies {
    implementation 'com.velocidi:velocidi-android-sdk:0.4.1'
}
```

You also need to add the following permissions to ensure the good functioning of the SDK.

In your application `AndroidManifest.xml`:

```xml
<!-- Required for internet. -->
<uses-permission android:name="android.permission.INTERNET"/>
```

## Usage

### Initialize the SDK

We highly recommend initializing the SDK on the `onCreate` method in your `Application` subclass.
If you don't already have an `Application` subclass, you can also instantiate it in your `MainActivity` although it is not recommended.

```kotlin
class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = Config(URL("https://cdp.test.com"))

        Velocidi.init(config, this)
    }
}
```

### Track

The `track` method allows you to collect user activity performed in your application.
This method is expecting a tracking event with the event details. For more information check our [documentation](https://docs.velocidi.com/collect/events)

```kotlin
Velocidi.getInstance().track(UserId("<Advertising ID>", "gaid"), PageView("MobileApp", "client1"))
```

It also accepts custom tracking events in a JSON format. Custom events should have type `"custom"` and a field `"customType"` with their custom event type:

```kotlin
val event =
    """
    {
      "type": "custom",
      "customType": "customType",
      "siteId": "MobileApp",
      "clientId": "client1"
    }
    """.trimIndent()

Velocidi.getInstance().track(CustomTrackingEventFactory.buildFromJSON(event))
```

### Match

The `match` method allows you to identify a user across multiple channels (Browser, Mobile App, CRM, ...).
By performing a match between multiple ids you are telling Velocidi CDP that these are the same user 
and all the information retrieved with either one of these IDs belongs to the same user.
A typical use case for this is, during the login action, 
to associate the user's email with Google's [Advertising ID](https://support.google.com/googleplay/android-developer/answer/6048248).

**Please note** that Velocidi SDK is not responsible for managing opt-in/opt-out status or 
ensuring the Google's privacy policy are respected. That should be the your app responsibility.


```kotlin
// Match the device Advertising Id with the user's email hash

Velocidi.match("someProvider", listOf(UserId("<Advertising ID>", "gaid"), UserId("<User Email Hash>", "email_sha256")))
```

For more information about Cross-Channel Matches go to https://docs.velocidi.com/collect/matches/ .

## Need Help?

You can find more information about Velocidi Private CDP in our https://docs.velocidi.com/

Please report bugs or issues to https://github.com/velocidi/velocidi-android-sdk/issues or send us an email to engineering@velocidi.com.
