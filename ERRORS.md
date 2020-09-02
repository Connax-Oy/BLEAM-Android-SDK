# Errors

Errors can be triggered from 3 library features:
- From BLEAM functional: `BleamSDK.EXTRA_FROM_BLEAM`
- From geofencing functional: `BleamSDK.EXTRA_FROM_GEOFENCING`
- From manual BLEAM launch by location: `BleamSDK.EXTRA_FROM_LOCATION`

## ERROR_WRONG_APP_ID_OR_SECRET
- From: Any
- Triggers if Application ID, Secret or Geofence is wrong
## ERROR_NO_TF_MODEL
- From: BLEAM
- Triggers when geofence has no approved model in BLEAM SetApp
## ERROR_SERVER_CONNECTION
- From: Any
- Triggers when something is wrong on our side
## ERROR_DEVICE_NOT_SUPPORTED
- From: BLEAM
- Triggers if BLE wasn't started successfully
## ERROR_BLUETOOTH_NOT_ENABLED
- From: BLEAM
- Triggers if Bluetooth wasn't enabled
## ERROR_NOT_IN_GEOFENCE
- From: Location
- Triggers if user isn't in geofence or location wasn't found
## ERROR_LOCATION_DISABLED
- From: Geofencing, Location
- Triggers if location permission wasn't granted
## ERROR_NEWER_SDK_NEEDED
- From: BLEAM
- Triggers if tries to interact with newer versions of models/BLEAM Scanners which are not supported in current version of SDK and requires update
## ERROR_WRONG_GEOFENCE
- From: BLEAM
- Triggers if BLEAM was started with wrong `externalId`
