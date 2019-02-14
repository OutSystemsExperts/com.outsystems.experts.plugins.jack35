var exec = require('cordova/exec');

exports.mute = function (streamId, success, error) {
    exec(success, error, 'Jack35Plugin', 'mute', [streamId]);
};

exports.setVolume = function (streamId, volume, success, error) {
    exec(success, error, 'Jack35Plugin', 'setVolume', [streamId, volume]);
};

exports.getVolume = function (streamId, success, error) {
    exec(success, error, 'Jack35Plugin', 'getVolume', [streamId]);
};
