# Jack35Plugin
Sample Cordova plugin to demonstrate Integration with Outsystems platform

## Dependencies
To start creating native Plugins for Cordova to integrate with the Outsystems platforms, a few things are needed in advance:
1. [NodeJs](https://nodejs.org/en/)
1. [Cordova](https://cordova.apache.org/#getstarted) @ **7.1.0**
1. [Plugman](https://cordova.apache.org/docs/en/latest/plugin_ref/plugman.html)
1. [Android Studio](https://developer.android.com/studio/) - for android plugin development
1. [XCode](https://developer.apple.com/xcode/) - for iOS plugin development


## Plugin Creation
In here we will demonstrate a sample usage for creating an android plugin.
  
Like many cli tools, you can simply type the command to see the options you have, either for **_plugman_** or _**cordova**_ tools.
```bash
#Use plugman to create the structure of the plugin
plugman create --name Jack35Plugin --plugin_id com.outsystems.experts.plugins.jack35 --plugin_version 0.0.1

cd Jack35Plugin

#Add the platform you want to your plugin
plugman platform add --platform_name android
```
After running these commands, you should have a structure with a sample android Plugin code (.java file) and the bridge to the cordova app (the .js file) with the names [Jack35Plugin.java](src/android/Jack35Plugin.java) and [Jack35Plugin.js](www/Jack35Plugin.js) respectively

## Optional: Application Creation

To develop and debug the plugin, you can (and should) create a cordova application and integrate your plugin in it.

**After your plugin is fully developed, the Outsystems platform will be responsible for creating the app**

```bash
#Use cordova to create the structure of the app with the command
#cordova create <PATH> <appID> <appName>
cordova create Jack35Demo com.outsystems.experts.Jack35Demo Jack35Demo

cd Jack35Demo

#Add the android platform to your cordova app
cordova platform add android@6.4.0

#Add the previously created plugin
cordova plugin add ../Jack35Plugin

#build your app
cordova build
```

At the end you should have a sample application with your plugin inside.
To debug and check how everything is connected, open, in this case, Android Studio in the android project folder (inside the created app, in ```platforms/android```)

Similarly, if you are developing an ios plugin and ios application, everything above applies, replacing the **_android_** keyword by _**ios**_. The version for cordova-ios platform should be 4.5.5.

