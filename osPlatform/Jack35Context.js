define("Jack35Context", ["exports"], function(exports) {

    var jackPlugCb = [];
    var jackPlugErrorCb = [];

    var localJackPlugCb = function(data) {
        jackPlugCb.forEach(function(cb) {
            console.log("Success: " + data);
            cb(data);
        })
    };
    var localJackPlugErrorCb = function(data) {
        jackPlugErrorCb.forEach(function(cb) {
            cb(data);
        })
    };


    exports.startListening = function(triggerNotification) {
        cordova.plugins.Jack35Plugin.startListening(triggerNotification,localJackPlugCb, localJackPlugErrorCb);
    };

    exports.addJackPlugCb = function(cb) {
        jackPlugCb.push(cb)
    };

    exports.removeJackPlugCb = function(cb) {
        var index = jackPlugCb.indexOf(cb);
        jackPlugCb.splice(index, 1)
    };

    exports.addJackPlugErrorCb = function(cb) {
        jackPlugErrorCb.push(cb)
    };

    exports.removeJackPlugErrorCb = function(cb) {
        var index = jackPlugErrorCb.indexOf(cb);
        jackPlugErrorCb.splice(index, 1)
    };


});