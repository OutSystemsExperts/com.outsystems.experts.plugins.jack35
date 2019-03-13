# Jack35Plugin 
Sample Cordova plugin to demonstrate Integration with Outsystems platform.

This plugin can be used to listen to plugging or unplugging of jack 3.5 connection (ex.: headphones) and control the device volume (for any [stream](#Android-Audio-Streams)).

## CONFIGURATIONS
The Notification that is triggered when the jack is disconnected can be customized.

### Notification Body and Title
The **body** and **title** of the notification are customizable through the use of preferences in the config.xml file of the cordova application (either globaly or for each platform separately).

Example:
```xml
<preference name="NOTIFICATION_TITLE" value="Notification custom Title" /> <!-- defaultValue = "Audio Warning"-->
<preference name="NOTIFICATION_MESSAGE" value="This will be the new message for the notification" /> <!-- defaultValue = "Jack was unplugged!"-->
```
Using the OS platform it can be configured using the extensibilities in the application using the plugin:
```json
{
  "preferences": {
    "global": [
      {
        "name": "NOTIFICATION_TITLE",
        "value": "Notification custom Title"
      },
      {
        "name": "NOTIFICATION_MESSAGE",
        "value": "This will be the new message for the notification"
      }
    ]
}
```
### Notification Icon
The **icon** of the notification is customizable through the usage of a resource file, that should be placed under the cordova app folder `/platforms/android/assets/www/jack35PluginResources`.
By default the icon should have the name `jack35Icon`, but this can be changed through the usage of the preference:

Example:
```xml
<preference name="NOTIFICATION_ICON" value="newIconName" /> <!-- defaultValue = "jack35Icon"-->
```
Using the OS platform it can be configured adding the preference with the new name to the extensibilities AND by adding a an image resource and deploying it to the folder `jack35PluginResources`

## API

Methods:
* **mute**(streamId: int, success: function, error: function)
    * This method mutes the sound of the given stream.
    Example: 
    ```javascript
        cordova.plugins.Jack35Plugin.mute(0,
              function(volume){console.log("Stream Muted")},
              function(error){console.log(error)});
    ```
    
* **getVolume**(streamId: int, success: function, error: function)
    * This method gets the sound volume of the given stream. The success function receives as input the volume as an integer.
    Example: 
    ```javascript
        cordova.plugins.Jack35Plugin.getVolume(0,
              function(volume){console.log(volume)},
              function(error){console.log(error)});
    ```
    
* **setVolume**(streamId: int, volume: int, success: function, error: function)
    * This method sets the sound volume of the given stream. It receives the streamId and the desired volume.
    ```javascript
        cordova.plugins.Jack35Plugin.setVolume(0, 100,
              function(){console.log("Volume changed")},
              function(error){console.log(error)});
    ```
    
* **startListening**(triggerNotification: bool, success: function, error: function)
    * This method sets the listener function for when the jack 3.5 connection is plugged or unplugged. The success function receives this feedback overtime. It receives as parameter a boolean defining if the unplug event should throw a notification or not.
    ```javascript
        cordova.plugins.Jack35Plugin.startListening(true,
              function(event){
                  if(event !== undefined)
                      console.log(event);
                  else
                      console.log("Started listening to jack events")},
              function(error){console.log(error)});
    ```
    
* **stopListening**(success: function, error: function)
    * This method removes the broadcast receiver that was set using **startListening**
    ```javascript
        cordova.plugins.Jack35Plugin.stopListening(function(){console.log("Stopped listening to jack events")},function(error){console.log(error)});
    ```


## Android Audio Streams
The Android ```android.media.AudioSystem``` class shows us what streams are available and the corresponding ids.
```java
public class AudioSystem
    {
    /** Used to identify the default audio stream volume */
    public static final int STREAM_DEFAULT = -1;
    
    /** Used to identify the volume of audio streams for phone calls */
    public static final int STREAM_VOICE_CALL = 0;
    
    /** Used to identify the volume of audio streams for system sounds */
    public static final int STREAM_SYSTEM = 1;
    
    /** Used to identify the volume of audio streams for the phone ring and message alerts */
    public static final int STREAM_RING = 2;
    
    /** Used to identify the volume of audio streams for music playback */
    public static final int STREAM_MUSIC = 3;
    
    /** Used to identify the volume of audio streams for alarms */
    public static final int STREAM_ALARM = 4;
    
    /** Used to identify the volume of audio streams for notifications */
    public static final int STREAM_NOTIFICATION = 5;
    
    /** Used to identify the volume of audio streams for phone calls when connected on bluetooth */
    public static final int STREAM_BLUETOOTH_SCO = 6;
    
    /** Used to identify the volume of audio streams for enforced system sounds in certain
    * countries (e.g camera in Japan) */
    public static final int STREAM_SYSTEM_ENFORCED = 7;
    
    /** Used to identify the volume of audio streams for DTMF tones */
    public static final int STREAM_DTMF = 8;
    
    /** Used to identify the volume of audio streams exclusively transmitted through the
    *  speaker (TTS) of the device */
    public static final int STREAM_TTS = 9;
    
    /** Used to identify the volume of audio streams for accessibility prompts */
    public static final int STREAM_ACCESSIBILITY = 10;
    
    .
    .
    .
}
```

