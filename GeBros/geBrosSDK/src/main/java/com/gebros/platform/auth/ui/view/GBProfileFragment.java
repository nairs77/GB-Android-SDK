package com.gebros.platform.auth.ui.view;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gebros.platform.auth.ProfileApi;
import com.gebros.platform.auth.model.GBUser;
import com.gebros.platform.auth.model.common.GBAPIError;
import com.gebros.platform.auth.net.GBAppResponse;
import com.gebros.platform.auth.net.GBMultipartRequest;
import com.gebros.platform.auth.net.Response;
import com.gebros.platform.auth.ui.GBContentsAPI;
import com.gebros.platform.auth.ui.common.BaseFragment;
import com.gebros.platform.auth.ui.common.FragmentType;
import com.gebros.platform.auth.ui.image.ImageLoader;
import com.gebros.platform.internal.JR;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.util.GBDeviceUtils;
import com.gebros.platform.util.GBValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jce_platform on 2016. 5. 30..
 */
public class GBProfileFragment extends BaseFragment implements View.OnClickListener {

    public static final int PROFILE_IMG_CHANGE_CODE = 15005;

    private static final int UNKNOWN_MIMETYPE = -230;
    private static final int OVER_MAXSIZE = -231;
    private static final int UPLOAD_FAILED = -232;

    private static final int PICK_FROM_CAMERA = 0;

    private static final int PICK_FROM_ALBUM = 1;

    private static final int CROP_FROM_CAMERA = 2;

    private ScrollView profileScrollLayout;

    private Uri mImageCaptureUri;
    private AlertDialog mDialog;
    private ImageView profilePhotoIv;
    private GBProfileImgView editProfileView;

    private RelativeLayout profileNicknameEditBtn;
    private RelativeLayout profileGreetingEditBtn;

    private TextView profileNicknameTv;
    private TextView profileGreetingTv;

    private String nicknameStr;
    private String greetingStr;
    private TextView appidTv;
    private String appidText;

    private ProgressDialog pd;

    GBUser user;

    public GBProfileFragment() {

        fragmentType = FragmentType.PROFILE_INFO_FRAGMENT;
        layoutId = JR.layout("joyple_profile_main");
        titleRes = JR.string("ui_profile_profile_top_title");
    }

    public static GBProfileFragment newInstance() {

        GBProfileFragment fragment = new GBProfileFragment();
        return fragment;
    }

    public void setFragmentType(FragmentType fragmentType) {
        this.fragmentType = fragmentType;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (GBDeviceUtils.SUPPORTS_GINGERBREAD) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        initialLayout(rootView);

        getProfile();

        return rootView;
    }

    private void getProfile() {
        user = ProfileApi.getLocalUser();
        GBLog.d("GBProfileFragment user::::::::::::::::::"+user);
        setProfileInfo();
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();
        GBLog.d(TAG + "viewID:::::::: %d ", viewId);
        if (viewId == profilePhotoIv.getId()) {
            // 프로필사진변경
            mDialog = createDialog();
            mDialog.show();
        } else if (viewId == profileNicknameEditBtn.getId()) {
            // 닉네임 변경
            fragmentAware.fragmentDataMove(GBProfileFragment.this,
                    new GBNicknameChangeFragment(), nicknameStr);

        } else if (viewId == profileGreetingEditBtn.getId()) {
            // 프로필 한마디 변경
            fragmentAware.fragmentDataMove(GBProfileFragment.this,
                    new GBGreetingChangeFragment(), greetingStr);
        }
    }

    private void initialLayout(View view) {

        profileScrollLayout = (ScrollView) view.findViewById(JR.id("joyple_main_contents_fragment"));
        profilePhotoIv = (ImageView) view.findViewById(JR.id("profile_image_iv"));
        editProfileView = (GBProfileImgView) view.findViewById(JR.dimen("profile_edit_image_cv"));

        profileNicknameTv = (TextView) view.findViewById(JR.id("profile_nickname_value_tv"));
        profileGreetingTv = (TextView) view.findViewById(JR.id("profile_greeting_msg_value_tv"));
        profileNicknameEditBtn = (RelativeLayout) view.findViewById(JR.id("profile_nickname_btn"));
        profileGreetingEditBtn = (RelativeLayout) view.findViewById(JR.id("profile_greeting_msg_btn"));
        appidTv = (TextView) view.findViewById(JR.id("profile_user_number_value_tv"));

        profilePhotoIv.setOnClickListener(this);
        profileNicknameEditBtn.setOnClickListener(this);
        profileGreetingEditBtn.setOnClickListener(this);
    }

    private void setProfileInfo() {

        /**
         * 프로필 이미지
         */

        if(user.getProfileImagePath() != null ) {

            ImageLoader.getInstance(activity.getApplicationContext()).loadThumbnailImage(user.getProfileImagePath(), profilePhotoIv);
        }

        /**
         * 닉네임
         */
        if( user.getNickName() == null ) {
            profileNicknameTv.setText("");
            nicknameStr = "";
        }else {
            nicknameStr = user.getNickName();
            profileNicknameTv.setText(nicknameStr);
        }

        /**
         * 프로필 한마디
         */
        if(user.getGreetingMessage() == null ) {
            profileGreetingTv.setText("");
            greetingStr = "";
        }else {
            greetingStr = user.getGreetingMessage();
            profileGreetingTv.setText(greetingStr);
        }

        /**
         * 앱 아이디
         */
        if(!GBValidator.isNullOrEmpty(user.getUserKey())) {
            appidText = user.getUserKey()+"";
            appidTv.setText(appidText);

        }

    }

    /**
     * 프로필 이미지 변경 API 호출
     *
     * @param
     */

    private void callProfileImageChangeAPI(final File filePath) {

        GBMultipartRequest appResources = new GBMultipartRequest(GBContentsAPI.PROFILE_IMG_CHANGE_URI, filePath.getAbsolutePath());

        appResources.post(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response) throws JSONException {
                pd.dismiss();
                activity.setResult(PROFILE_IMG_CHANGE_CODE);
                String imageUrl = json.getString("profile_img");
                ImageLoader.getInstance(activity).loadThumbnailImage(imageUrl, profilePhotoIv);
            }

            @Override
            public void onError(Response response) {
                pd.dismiss();
                GBAPIError responseError = response.getAPIError();
                String defaultStr = getResources().getString(JR.string("ui_main_default_error")).toString();
                if (responseError.getErrorCode() == UNKNOWN_MIMETYPE) {
                    AsyncErrorDialog dialog = new AsyncErrorDialog(activity, defaultStr + 1);
                    dialog.show();
                } else if (responseError.getErrorCode() == OVER_MAXSIZE) {
                    AsyncErrorDialog dialog = new AsyncErrorDialog(activity, defaultStr + 2);
                    dialog.show();
                } else if (responseError.getErrorCode() == UPLOAD_FAILED) {
                    AsyncErrorDialog dialog = new AsyncErrorDialog(activity, defaultStr + 3);
                    dialog.show();
                } else {
                    AsyncErrorDialog dialog = new AsyncErrorDialog(activity, defaultStr + 4);
                    dialog.show();
                }
                GBLog.i(TAG + "callProfileImageChangeAPI() - onException:%s", responseError.getErrorType());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != FragmentActivity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA: {
                GBLog.i(TAG, "CROP_FROM_CAMERA");
                GBLog.i(TAG, "mImageCaptureUri = " + mImageCaptureUri);

                File img_file = new File(Uri.parse(mImageCaptureUri.toString())
                        .getPath());

                pd = new ProgressDialog(activity);
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();
                pd.setContentView(JR.layout("custom_progress"));

                callProfileImageChangeAPI(img_file);

                break;
            }

            case PICK_FROM_ALBUM: {
                GBLog.i(TAG, "PICK_FROM_ALBUM");
                // 이 후의 처리가 카메라와 같으므로 일단 break없이 진행합니다.
                mImageCaptureUri = data.getData();
                File original_file = getImageFile(mImageCaptureUri);

                mImageCaptureUri = createSaveCropFile();
                File copy_file = new File(mImageCaptureUri.getPath());

                // SD카드에 저장된 파일을 이미지 Crop 을 위해 복사한다.
                copyFile(original_file, copy_file);
            }

            case PICK_FROM_CAMERA: {
                GBLog.i(TAG, "PICK_FROM_CAMERA");
                // 이미지를 가져온 이 후의 리사이즈 할 이미지 크기를 결정합니다.
                // 이 후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                // Crop 한 이미지를 저장할 Path
                intent.putExtra("output", mImageCaptureUri);

                intent.putExtra("outputX", 150);
                intent.putExtra("outputY", 150);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);

                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }

        }

    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumImage() {
        GBLog.i(TAG, "doTakeAlbumImage()");
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * 다이얼로그 생성
     */
    private AlertDialog createDialog() {
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakeAlbumImage();
                setDismiss(mDialog);
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDismiss(mDialog);
            }
        };

        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setTitle(getResources().getString(JR.string("ui_profile_image_select_label_title")));
        ab.setPositiveButton(getResources().getString(JR.string("ui_profile_image_album_label_title")), albumListener);
        ab.setNegativeButton(getResources().getString(android.R.string.cancel), cancelListener);

        return ab.create();

    }

    /**
     * 다이얼로그 종료
     */
    private void setDismiss(AlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * crop 된 이미지가 저장 될 파일을 만든다
     */
    private Uri createSaveCropFile() {
        Uri uri;
        String url = "tmp_" + String.valueOf(System.currentTimeMillis())
                + ".jpg";
        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                url));
        return uri;
    }

    /**
     * 선택된 uri의 사진 path를 가져온다
     */
    private File getImageFile(Uri uri) {
        String path = getPath(activity, uri);
        GBLog.d(TAG, " , getPath ::::::::::::::"+path);//
        return new File(path);
    }

    /**
     * 파일 복사
     */

    public static boolean copyFile(File srcFile, File destFile) {

        boolean result = false;

        try {

            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally {
                in.close();
            }

        } catch (IOException e) {
            result = false;
        }

        return result;
    }

    private static boolean copyToFile(InputStream inputStream, File destFile) {

        try {

            OutputStream out = new FileOutputStream(destFile);
            try {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }

            } finally {
                out.close();
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    private String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
