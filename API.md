# Jack35Plugin 
Sample Cordova plugin to demonstrate Integration with Outsystems platform.

## API

Methods:
* **coolMethod**(message: string, success: function, error: function)
    * This method does nothing :)
    ```javascript
        cordova.plugins.Jack35Plugin.coolMethod("amazing useless message",
              function(){console.log("Volume changed")},
              function(error){console.log(error)});
    ```

