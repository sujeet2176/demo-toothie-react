package com.demotoothie;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.demotoothie.activities.ControlPanelActivity;
import com.facebook.react.ReactActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "demoToothie";
    }

    private String msgPrefix;
    private String msgComma;
    private String msgDeniedExtStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msgPrefix = getResources().getString(R.string.permission_denied_prefix);
        msgComma = getResources().getString(R.string.permission_denied_comma);
        msgDeniedExtStorage = getResources().getString(R.string.permission_denied_external_storage);
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(allPermissionsListener)
                .check();
    }


    /* Permission */

    private MultiplePermissionsListener allPermissionsListener = new MultiplePermissionsListener() {
        @Override
        public void onPermissionsChecked(MultiplePermissionsReport report) {
            if (!report.areAllPermissionsGranted()) {
                StringBuilder msg = new StringBuilder();
                List<PermissionDeniedResponse> deniedList = report.getDeniedPermissionResponses();

                for (PermissionDeniedResponse response : deniedList) {
                    if (msg.length() != 0)
                        msg.append(msgComma);

                    switch (response.getPermissionName()) {
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        case Manifest.permission.READ_EXTERNAL_STORAGE:
                            msg.append(msgDeniedExtStorage);
                            break;
                    }
                }
                msg.insert(0, msgPrefix);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

        }
    };

}
