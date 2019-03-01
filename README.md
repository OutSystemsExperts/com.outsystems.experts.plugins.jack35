
This repository serves to illustrate an outcome of following the tutorials/documentation about creating native plugins for the OS platform.
Follow a tech talk with a live demonstration of creating a plugin at:
1. [Creating and Using OutSystems Plugins - Mobile - ODC 2018](https://www.youtube.com/watch?v=j0UURdj3zB4)
2. [Step Up Your Game With Native Plugins - Mobile - ODC 2018](https://www.youtube.com/watch?v=pNVIKibSOEU)

You can also read these blogposts/documentation:
* [How to create a cordova plugin from scratch](https://www.outsystems.com/blog/posts/how-to-create-a-cordova-plugin-from-scratch/)
* [Using Cordova Plugins](https://success.outsystems.com/Documentation/10/Extensibility_and_Integration/Mobile_Plugins/Using_Cordova_Plugins)
* [How MABS work](https://www.outsystems.com/blog/posts/how-mobile-apps-build-service-works)


# Jack35Plugin 
This plugin can be used to listen to plugging or unplugging of jack 3.5 connection (ex.: headphones) and control the device volume (for any [stream](#Android-Audio-Streams)).

## Dependencies
To start creating native Plugins for Cordova to integrate with the Outsystems platforms, a few things are needed in advance:
1. [NodeJs](https://nodejs.org/en/)
1. [Cordova](https://cordova.apache.org/#getstarted) @ **7.1.0**
1. [Plugman](https://cordova.apache.org/docs/en/latest/plugin_ref/plugman.html)
1. [Android Studio (AS)](https://developer.android.com/studio/) - for android plugin development
	* you will need the [Java Development Kit](https://openjdk.java.net/install/) installed to run the cordova build comands for the android platform. The AS bundle already brings an embed jdk, you should either install a new jdk or add the AS embed jdk path to your own path. 
1. [XCode](https://developer.apple.com/xcode/) - for iOS plugin development
1. Git (Optional) - if you want to share your plugin to the outsystems platform using the extensibility configuration with a link.

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
#For iOS
# cordova platform add ios@4.5.5

#Add the previously created plugin
cordova plugin add ../Jack35Plugin

#build your app
cordova build
```

At the end you should have a sample application with your plugin inside.
To debug and check how everything is connected, open, in this case, Android Studio in the android project folder (inside the created app, in ```platforms/android```)

You can also test your application imediatly, having a device or android emulator connected, by running the command:

```cordova run android ```

## iOS

Similarly, if you are developing an ios plugin and ios application, everything above applies, replacing the **_android_** keyword by _**ios**_.
 
 The version for cordova-ios platform should be 4.5.5. You should open the **.xcworkspace** file (inside the created app, in ```platforms/ios```) with the Xcode app to see the application code.


## [Jack35 Plugin API](API.md)
For a detailed api for the plugin, check the [api page](API.md);

## Outsystems Platform
In the [osPlatform](osPlatform) folder you can find the .oap file containing the OS plugin and a sample application to use the plugin.

To know more about how you can configure the `Extensibilities Configurations` see the [OS oficial documentation](https://success.outsystems.com/Documentation/10/Extensibility_and_Integration/Mobile_Plugins/Using_Cordova_Plugins)

**Notes**
1. When adding a .zip file to the extensibilities, the zip file size is limited to 20 MB size

