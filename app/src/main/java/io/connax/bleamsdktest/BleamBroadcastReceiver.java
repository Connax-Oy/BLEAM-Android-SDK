package io.connax.bleamsdktest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.connax.bleam.BleamSDK;

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
