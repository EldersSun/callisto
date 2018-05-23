package com.miaodao.Sys.Permissions;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;

public class PermissionsTool implements ActivityCompat.OnRequestPermissionsResultCallback {

    private void init(Activity context) {
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant);
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_ACCESS_COARSE_LOCATION, mPermissionGrant);
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_ACCESS_FINE_LOCATION, mPermissionGrant);
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_READ_PHONE_STATE, mPermissionGrant);
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_CALL_PHONE, mPermissionGrant);
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_GET_ACCOUNTS, mPermissionGrant);
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_RECORD_AUDIO, mPermissionGrant);
        PermissionUtils.requestPermission(context, PermissionUtils.CODE_CAMERA, mPermissionGrant);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
//            switch (requestCode) {
//                case PermissionUtils.CODE_RECORD_AUDIO:
//                    Toast.makeText(getActivity(), "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_GET_ACCOUNTS:
//                    Toast.makeText(getActivity(), "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_READ_PHONE_STATE:
//                    Toast.makeText(getActivity(), "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_CALL_PHONE:
//                    Toast.makeText(getActivity(), "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_CAMERA:
//                    Toast.makeText(getActivity(), "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
//                    Toast.makeText(getActivity(), "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
//                    Toast.makeText(getActivity(), "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
//                    Toast.makeText(getActivity(), "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
//                    break;
//                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
//                    Toast.makeText(getActivity(), "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    break;
//            }
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

//        PermissionUtils.requestPermissionsResult(getActivity(), requestCode, permissions, grantResults, mPermissionGrant);

    }
}
