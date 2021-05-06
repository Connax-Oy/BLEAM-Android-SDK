# BLEAM Android SDK

BLEAM Android SDK is an essential part of BLEAM systems. It enables proximity detection by using Bluetooth Low Energy (BLE) capabilities of the end user’s phone. Bluetooth RSSI data received from BLESc beacons on the service station helps BLEAM Android SDK to pinpoint the end user’s phone in relation to a service terminal.

## [Glossary](GLOSSARY.md)

## Installation 

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency
```gradle
dependencies {
    implementation 'com.github.connax:BLEAM-Android-SDK:2.1.2'
}
```

And you're ready to use library!

## Requirements

- Minimum supported Android version is 5.0 (API level 21)
  - Required BLE features has proper interface only in API 21+
- User device must have Google Services preinstalled
  - Library depends on Google Location Services
- User device must have Bluetooth 4.0 or higher
  - BLE is part of Bluetooth 4.0 specification
- You must request `ACCESS_COARSE_LOCATION` permission
  - Both BLE and geofences require location permission
  - Otherwise library won't work at all
- You should request `ACCESS_BACKGROUND_LOCATION` permission on Android 10 or higher
  - Otherwise only manual BLEAM launch is supported

## Permissions

Without these permissions library **won't work at all**

You can either request them by yourself or call SDK Splashscreen

### Request by yourself

- You need grant fine location permission for library to work:
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```
- On Android 10+ you need to grant background location permission:
```xml
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```

### Splashscreen

If you don't want to implement permission requests by yourself and/or want to show that you're using our SDK, you can use splashscreen.
- Just start activity for result (if it wasn't shown before or permission aren't granted):
```java
if (!sdk.wasSplashShowed() || !sdk.arePermissionsGranted()) {
    Intent intent = new Intent(this, SplashActivity.class);
    startActivityForResult(intent, REQUEST_SPLASH);
}
```

- And receive result:
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_SPLASH) {
        if (resultCode == RESULT_OK) {
            // SDK now is fully functional
        } else {
            // Splashscreen terms weren't accepted, or permissions wan't granted
        }
    }
}
```

## Usage example

- Download code from this repo and open in Android Studio
- Change `appId` and `appSecret` in `MainActivity.java`
- Build and run

Usage example contains basic realisation of BroadcastReceiver, Activity with all features linked to buttons and event logging.

Event logging may be used for debugging your geofences and approved models on the go. In your real application you don't need to receive `ACTION_LOGS` broadcast.

## Usage

### 1. Result receiver
To receive BLEAM results you need to create BroadcastReceiver.

- Create receiver using Android Studio context menu: `New -> Other -> BroadcastReceiver`.
- In creation window uncheck `Exported` so you will get broadcasts only from inside application.
- In `AndroidManifest.xml` find your new BroadcastReceiver and add intent-filter:
```xml
<receiver
    android:name=".BleamBroadcastReceiver"
    android:enabled="true"
    android:exported="false">
    <intent-filter>
        <action android:name="io.connax.bleam.ACTION_RESULT" />
        <action android:name="io.connax.bleam.ACTION_GEOFENCE" />
        <action android:name="io.connax.bleam.ACTION_GEOFENCING" />
    </intent-filter>
</receiver>
```
- <details>
   <summary>Make your BroadcastReceiver parse intent information like in these example</summary>

    ```java
    public class BleamBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case BleamSDK.ACTION_RESULT:
                        if (intent.getBooleanExtra(BleamSDK.EXTRA_SUCCESS, false)) {
                            onBleamSuccess(context,
                                    intent.getStringExtra(BleamSDK.EXTRA_EXTERNAL_ID),
                                    intent.getIntExtra(BleamSDK.EXTRA_POSITION, -1));
                        } else {
                            onBleamFailure(context,
                                    intent.getStringExtra(BleamSDK.EXTRA_FROM),
                                    intent.getIntExtra(BleamSDK.EXTRA_ERROR_CODE, -1));
                        }
                        break;
                    case BleamSDK.ACTION_GEOFENCE:
                        onGeofenceEnter(context,
                                intent.getStringExtra(BleamSDK.EXTRA_EXTERNAL_ID));
                        break;
                    case BleamSDK.ACTION_GEOFENCING:
                        onGeofencingState(context,
                                intent.getBooleanExtra(BleamSDK.EXTRA_ENABLED, false));
                }
            }
        }

        public void onBleamSuccess(Context context, String extId, int position) {
            // extId is external id which was provided by you
            // position is position on your service station, starts from 1
            // TODO start payment or notify user -- as you wish
        }

        public void onGeofenceEnter(Context context, String extId) {
            // extId is external id of entered geofence
        }

        public void onGeofencingState(Context context, Boolean enabled) {
            if (enabled) {
                // Geofencing is enabled
            } else {
                // Geofencing is disabled
            }
        }

        public void onBleamFailure(Context context, String from, int errorCode) {
            switch (errorCode) {
                case BleamSDK.ERROR_WRONG_APP_ID_OR_SECRET:
                    // TODO process "wrong App ID or Secret" error
                    break;
                case BleamSDK.ERROR_NO_TF_MODEL:
                    // TODO process "geofence has no approved model" error
                    break;
                case BleamSDK.ERROR_SERVER_CONNECTION:
                    // TODO process "no connection to server" error
                    break;
                case BleamSDK.ERROR_DEVICE_NOT_SUPPORTED:
                    // TODO process "device not supported" error
                    break;
                case BleamSDK.ERROR_BLUETOOTH_NOT_ENABLED:
                    // TODO process "bluetooth disabled" error
                    // Consider prompt user that application needs Bluetooth enabled
                    break;
                case BleamSDK.ERROR_NOT_IN_GEOFENCE:
                    // TODO process "location not found" error
                    break;
                case BleamSDK.ERROR_LOCATION_DISABLED:
                    // TODO process "no location permission" error
                    // Consider prompt user about granting location permission
                    break;
                case BleamSDK.ERROR_NEWER_SDK_NEEDED:
                    // TODO process "SDK outdated" error
                    // Consider prompt user about updating application
                    break;
                case BleamSDK.ERROR_WRONG_GEOFENCE:
                    // TODO process "Wrong geofence" error
                default:
                    // TODO process or log "something went terribly wrong" error
            }
        }
    }
    ```

</details>

- You can ignore `ACTION_GEOFENCE` if you are not interested in it
- Replace TODOs with code for payment initiation or notifying user, and processing errors.

Details about errors are described on [ERRORS](ERRORS.md) page

### 2. Initialize SDK

To use you need to get BleamSDK object at first:
```java
BleamSDK bleamSDK = new BleamSDK(context, "appId", "appSecret");
```

### 3. Launch BLEAM
#### Using built-in geofencing
If you will use built-in geofencing, BLEAM will trigger automatically without interaction. List of geofences will refresh every 24 hours from enabling. If geofencing fails to start, it will try to start again in 24 hours.

- To enable built-in geofencing you just need call this function:
```java
bleamSDK.enableGeofencing()
```
- And if you want to stop geofencing:
```java
bleamSDK.disableGeofencing()
```

- To check if geofencing is enabled you can use function:
```java
bleamSDK.isGeofencingEnabled()
```

#### Using your own geofencing
- If you are using your own geofencing realization you can launch BLEAM using external ID of your geofence:
```java
bleamSDK.startBleam("externalID")
```

#### Manual BLEAM launch
- If you want start BLEAM immediately without knowing your location, you can start it using GPS:
```java
bleamSDK.startBleam()
```
- Recommend to use this to launch BLEAM manually by user interaction.
- You can create alternative BroadcastReceiver inside your Activity to receive result, if BLEAM was launched by user.
- If location was not found in 30 seconds, library will return [ERROR_NOT_IN_GEOFENCE](ERRORS.md#error_not_in_geofence)
