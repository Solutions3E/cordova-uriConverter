cordova-uriConverter
===================

Cordova Android Plugin for Uri Conversion.

## Installing the plugin

The plugin confirms to the Cordova plugin specification, it can be installed
using the Cordova / Phonegap command line interface.

    phonegap plugin add https://github.com/Solutions3E/cordova-uriConverter.git

    cordova plugin add https://github.com/Solutions3E/cordova-uriConverter.git


## Using the plugin

The plugin creates the object `uriconverter` with the methods `toFilePath(uri,success, fail)` and `toContentUri(uri,success, fail)`

Example - Get File Uri from Content Uri:
```javascript

uriconverter.toFilePath(<content uri>,
                                    function(response){
                                       console.log('File Path:'+response);
                                    },
                                    function(error){
                                        console.log(error);
                                    }
                                );

```

Example - Get Content Uri from File Uri:
```javascript

uriconverter.toContentUri(<file uri>,
                                    function(response){
                                       console.log('Content Uri'+response);
                                    },
                                    function(error){
                                        console.log(error);
                                    }
                                );

```
    
### Note for Android Use

The plugin returns uri's of images and other media files that are stored in a private directory and are not accessible for the other apps installed on the device.  These files will often not be deleted automatically though.





