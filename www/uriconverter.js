/*global cordova, module*/

module.exports = {
    toFilePath: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "UriConverter", "getfilepath", [name]);
    },
    toContentUri: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "UriConverter", "getcontenturi", [name]);
    }
};
