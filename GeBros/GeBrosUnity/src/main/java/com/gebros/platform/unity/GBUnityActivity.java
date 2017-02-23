package com.gebros.platform.unity;

import android.content.Intent;
import android.os.Bundle;

import com.gebros.platform.ActivityResultHelper;
import com.gebros.platform.GBSdk;
import com.gebros.platform.GBActivityHelper;
import com.unity3d.player.UnityPlayerActivity;

/**
 * Created by gebros.nairs77@gmail.com on 6/15/16.
 */
public class GBUnityActivity extends UnityPlayerActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (!ActivityResultHelper.handleOnActivityResult(requestCode, resultCode, data))
//            GBActivityHelper.onActivityResult(this, requestCode, resultCode, data);
    }
    /*
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//    //    GBPermissionHelper.OnRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
    */
}
