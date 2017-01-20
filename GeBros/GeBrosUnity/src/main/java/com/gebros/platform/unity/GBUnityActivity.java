package com.gebros.platform.unity;

import android.content.Intent;
import android.os.Bundle;

import com.gebros.platform.ActivityResultHelper;
import com.gebros.platform.GBSdk;
import com.gebros.platform.JoypleActivityHelper;
import com.unity3d.player.UnityPlayerActivity;

/**
 * Created by Joycity-Platform on 6/15/16.
 */
public class GBUnityActivity extends UnityPlayerActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Joyple.Initialize(this);
        JoypleActivityHelper.onActivityCreate(this, bundle);
    }

    @Override
    protected void onDestroy() {
        JoypleActivityHelper.onActivityDestroy(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        JoypleActivityHelper.onActivityPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        JoypleActivityHelper.onActivityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        JoypleActivityHelper.onActivityResume(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        JoypleActivityHelper.onActivityRestart(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JoypleActivityHelper.onActivityNewIntent(this, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!ActivityResultHelper.handleOnActivityResult(requestCode, resultCode, data))
            JoypleActivityHelper.onActivityResult(this, requestCode, resultCode, data);
    }
    /*
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//    //    JoyplePermissionHelper.OnRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
    */
}
