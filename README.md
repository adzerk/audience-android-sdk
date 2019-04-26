# Velocidi SDK
[![Build Status](https://travis-ci.org/velocidi/velocidi-android-sdk.svg?branch=master)](https://travis-ci.org/velocidi/velocidi-android-sdk)
[![codecov](https://codecov.io/gh/velocidi/velocidi-android-sdk/branch/master/graph/badge.svg)](https://codecov.io/gh/velocidi/velocidi-android-sdk)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.velocidi/velocidi-android-sdk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.velocidi/velocidi-android-sdk)

Open-source SDK to integrate Android Apps with Velocidi Private CDP.

## Installation

Velocidi SDK is published to Maven central as a single module. Please reference the badge above or go to the [release page](https://github.com/velocidi/velocidi-android-sdk/releases) to check our latest version.

To install the SDk in your application you just need to add the following dependency to the `app/build.gradle` file.

```
dependencies {
    implementation 'com.velocidi:velocidi-android-sdk:0.0.1'
}
```

You also need to add the following permissions to ensure the good functioning of the SDK.

In your application `AndroidManifest.xml`:

```
<!-- Required for internet. -->
<uses-permission android:name="android.permission.INTERNET"/>
```

In your `build.gradle` file:

```
dependencies {
  implementation 'com.google.android.gms:play-services-ads-identifier:16.0.0'
}
```

## Usage

### Initialize the SDK

We highly recommend initializing the SDK on the `onCreate` method in your `Application` subclass.
If you don't already have an `Application` subclass, you can also instantiate it in your `MainActivity` although it is not recommended.

```
class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = Config(URL("http://test.com"))

        Velocidi.start(config, this)
    }
}
```
You can also have a more granular control over the supported channels:
```
class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val trackEndpoint = Channel(URL("http://tr.test.com"), true)
        val matchEndpoint = Channel(URL("http://match.test.com"), true)
        val config = Config(trackEndpoint, matchEndpoint)

        Velocidi.start(config, this)
    }
}
```

### Track

The `track` method allows you to collect user activity performed in your application.
This method is expecting a JSONObject with the event details. For more information check our [documentation](https://docs.velocidi.com/knowledgebase/web-and-e-commerce/)

```
val event = """
                {
                  "eventType": "pageView",
                  "siteId": "1",
                  "clientId": "client1",
                  "location": "MainActivity"
                }
            """.trimIndent()
Velocidi.track(JSONObject(event))
```

### Match

The `match` method allows you to identify a user across mutiple channels.
Internally, the SDK is identifying a user based on its [Advertising ID](http://www.androiddocs.com/google/play-services/id.html).
By performing a match between an Advertising Id and your custom Id (e.g. e-mail hashes or CRM IDs), 
you are telling Velocidi CDP that these are the same user and all the information retrieved with either one of these IDs belongs to the same user.

```
// Match the device Advertising Id with the user email(useremail@example.com)

Velocidi.match("someProvider", listOf(UserId("eml", "useremail@example.com")))
```

For more information our documention section on [Cross-Channel Matches](https://docs.velocidi.com/knowledgebase/cross-channel-matches/)

## Need Help?

You can find more information about Velocidi Private CDP in our https://docs.velocidi.com/

Please report bugs or issues to https://github.com/velocidi/velocidi-android-sdk/issues or send us an email to engineering@velocidi.com.