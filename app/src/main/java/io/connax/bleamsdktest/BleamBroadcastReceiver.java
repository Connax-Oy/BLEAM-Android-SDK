package io.connax.bleamsdktest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.connax.bleam.BleamSDK;

public class BleamBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case BleamSDK.ACTION_BLEAM:
                if (intent.getBooleanExtra(BleamSDK.EXTRA_SUCCESS, false)) {
                    onBleamSuccess(context,
                            intent.getStringExtra(BleamSDK.EXTRA_EXTERNAL_ID),
                            intent.getIntExtra(BleamSDK.EXTRA_POSITION, -1));
                } else {
                    onBleamFailure(context,
                            intent.getIntExtra(BleamSDK.EXTRA_ERROR_CODE, -1));
                }
                break;
            case BleamSDK.ACTION_GEOFENCE:
                onGeofenceEnter(context,
                        intent.getStringExtra(BleamSDK.EXTRA_EXTERNAL_ID));
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

    public void onBleamFailure(Context context, int errorCode) {
        switch (errorCode) {
            case BleamSDK.ERROR_WRONG_APP_ID_SECRET_OR_GEOFENCE:
                // TODO process "wrong ID" error
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
                break;
            case BleamSDK.ERROR_NOT_IN_GEOFENCE:
                // TODO process "location not found" error
                break;
            case BleamSDK.ERROR_LOCATION_DISABLED:
                // TODO process "no location permission" error
                break;
            default:
                // TODO process or log "something went terribly wrong" error
        }
    }
}
