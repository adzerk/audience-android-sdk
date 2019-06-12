Open-source SDK to integrate Android Apps with Velocidi Private CDP.

## Installation

Velocidi SDK is published to Maven central as a single module. Please reference the badge above or go to the [release page](https://github.com/velocidi/velocidi-android-sdk/releases) to check our latest version.

To install the SDk in your application you just need to add the following dependency to the `app/build.gradle` file.

```gradle
dependencies {
    implementation 'com.velocidi:velocidi-android-sdk:0.0.1'
}
```

You also need to add the following permissions to ensure the good functioning of the SDK.

In your application `AndroidManifest.xml`:

```xml
<!-- Required for internet. -->
<uses-permission android:name="android.permission.INTERNET"/>
```

In your `build.gradle` file:

```gradle
dependencies {
  implementation 'com.google.android.gms:play-services-ads-identifier:16.0.0'
}
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
You can also have a more granular control over the supported channels:
```kotlin
class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val trackEndpoint = Channel(URL("https://tr.cdp.test.com"), true)
        val matchEndpoint = Channel(URL("https://match.cdp.test.com"), true)
        val config = Config(trackEndpoint, matchEndpoint)

        Velocidi.init(config, this)
    }
}
```

### Track

The `track` method allows you to collect user activity performed in your application.
This method is expecting a tracking event with the event details. For more information check our [documentation](https://docs.velocidi.com/knowledgebase/web-and-e-commerce/)

```kotlin
Velocidi.getInstance().track(PageView("MobileApp", "client1"))
```

It also accepts custom tracking events in a json format:

```kotlin
val event =
    """
    {
      "type": "customEvent",
      "siteId": "MobileApp",
      "clientId": "client1"
    }
    """.trimIndent()

Velocidi.getInstance().track(CustomTrackingEventFactory.buildFromJSON(event))
```

### Match

The `match` method allows you to identify a user across mutiple channels.
Internally, the SDK is identifying a user based on its [Advertising ID](http://www.androiddocs.com/google/play-services/id.html).
By performing a match between an Advertising Id and your custom Id (e.g. e-mail hashes or CRM IDs), 
you are telling Velocidi CDP that these are the same user and all the information retrieved with either one of these IDs belongs to the same user.

```kotlin
// Match the device Advertising Id with the user email(useremail@example.com)

Velocidi.match("someProvider", listOf(UserId("eml", "useremail@example.com")))
```

For more information our documention section on [Cross-Channel Matches](https://docs.velocidi.com/knowledgebase/cross-channel-matches/)

## Need Help?

You can find more information about Velocidi Private CDP in our https://docs.velocidi.com/

Please report bugs or issues to https://github.com/velocidi/velocidi-android-sdk/issues or send us an email to engineering@velocidi.com.