package com.gebros.platform.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.gebros.platform.ActivityResultHelper;
import com.gebros.platform.GBSdk;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.platform.Platform;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBMessageUtils;

public class MainActivity extends Activity implements Spinner.OnItemSelectedListener {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private boolean isInitialized = false;
    private final PlatformType defaultPlatformType = PlatformType.DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ActivityResultHelper.handleOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

        Spinner spinner = (Spinner)adapterView;
        int selectSpinnerId = spinner.getId();

        if (selectSpinnerId == mSpinGameSelector.getId()) {
            _gameSettings(pos);
        } else if (selectSpinnerId == mSpinPlatformSelector.getId()){
            _platformSettings(pos);
        } else if (selectSpinnerId == mSpinServerSelector.getId()){
            _serverSettings(pos);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void initViews() {
        mSpinGameSelector = (Spinner)findViewById(R.id.spinner_game);
        mSpinPlatformSelector = (Spinner)findViewById(R.id.spinner_platform);
        mSpinServerSelector = (Spinner)findViewById(R.id.spinner_server);

        mSpinGameSelector.setOnItemSelectedListener(this);
        mSpinPlatformSelector.setOnItemSelectedListener(this);
        mSpinServerSelector.setOnItemSelectedListener(this);

        mTxtVersionInfo = (TextView)findViewById(R.id.samaple_version_info_tv);
        mTxtVersionInfo.setText(GBSdk.VERSION);
        mBtnStartApp = (Button)findViewById(R.id.btn_start_app);

        mBtnStartApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTest();
            }
        });
    }

    private void startTest() {
        _configureAppTest();
/*
        final Handler handler = new Handler();

        (new Thread(new Runnable() {
            @Override
            public void run() {
                GB.RequestGlobalServerInfo(mServerAddress, mGameCode, new GBEventReceiver() {
                    //"https://GB-cn-qa.joycityplay.com/gbranch/branch/getzone"
                    @Override
                    public void onSuccessEvent(GBEvent event, JSONObject json) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                _configureAppTest();
                                GBMessageUtils.toast(MainActivity.this, "Connected Server !!!");
                            }
                        });
                    }

                    @Override
                    public void onFailedEvent(GBEvent event, final int errorCode, final String errorMessage) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String errorMsg = String.format("error = %s(%d)", errorMessage, errorCode);
                                GBMessageUtils.toast(MainActivity.this, errorMsg);
                            }
                        });
                    }
                });
            }
        })).start();
*/
    }

    private void _configureAppTest() {
        try {

            //GB.ConfigureSDKInfo(this, mCurrentPlatform, "9519d7e94d0b316d8e5122c67aeddfa4", 8887, GB.LogLevel.DEBUG);
            GBSdk.ConfigureSdkWithInfo(this, mGameCode, mClientSecretKey, mCurrentPlatform,GBLog.LogLevel.DEBUG);
            Intent intent = new Intent(MainActivity.this,TextTabActivity.class);
            startActivity(intent);
        } catch (NullPointerException e) {
            GBLog.d(TAG + " configureAppTest NullPointerException e::::"+e.getMessage()+":::::"+e.toString());
            GBMessageUtils.alert(this, "Oh~ My God, 설정이 안되었어요!!! "+e.getStackTrace());
        }
    }

    private void _gameSettings(int selectedPos) {
        if (selectedPos == 1) {
            mClientSecretKey = "9519d7e94d0b316d8e5122c67aeddfa4";
            mGameCode = 1;

            Toast.makeText(this, "Start GB Sample Test", Toast.LENGTH_SHORT).show();
        } else if (selectedPos == 2) {
            mClientSecretKey = "0dd682d7bc4f3fcb9ff62634ba6fe2a3";
            mGameCode = 69;
            Toast.makeText(this, "Start Game of dice - China Test", Toast.LENGTH_SHORT).show();
        }
    }

    private void _platformSettings(int pos) {
        Platform.Builder builder = null;

        if (pos == 1) {
            builder = new Platform.Builder("203067241", "7f8d88a69c521e7d166fd6d74e4dad5b")
                    .PlatformType(PlatformType.CHINA360)
                    .AppSecret("739a8ddd580435cbd4453758939d06fb");
        } else if (pos == 2) {
            builder = new Platform.Builder("8222038", "hd1PzS22rzfDN1KNFw0Yzlxh")
                    .PlatformType(PlatformType.BAIDU)
                    .AppSecret("H7UDUFHBXMzifbR5urYkc3BWEYXNZUot");
        } else if (pos ==3) {
            builder = new Platform.Builder("2882303761517479617", "5441747927617")
                    .PlatformType(PlatformType.XIAOMI)
                    .AppSecret("npYUNU8M8B9Ev2JA3Ot6qg==");
        } else if (pos ==4) {
            builder = new Platform.Builder("37626", "728110")
                    .PlatformType(PlatformType.UC)
                    .AppSecret("fd15ebd365619778bfa825edc36b2a2d")
                    .MarketTestMode(true);
        } else if (pos ==5) {
            builder = new Platform.Builder("100040646", "")
                    .PlatformType(PlatformType.WANDOUJIA)
                    .AppSecret("137ac27d23002af59ba7756285ba0e14");
        } else if (pos ==6) {
            // TODO : 화웨이에서 제공하는 샘플코드에 있는 값 그대로 셋팅함(사업부에서 아직 필요한 정보 전달 받지 못함)
            builder = new Platform.Builder("10562675", "")
                    .PlatformType(PlatformType.HUAWEI)
                    .AppSecret("89f4650a91e41834b66db30e67dbe701")
                    .CpId("890086000102017612")
                    .BuoSecret("MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAInTw3wV5sFaCFfKo+wMOkmueIoqnLJaNqMnXr7i5YWWiYHom9c7pmqev2igpN3tHxML0N5KsS94hFgnGE1MvFsUrCcqiDOahiJK6Py94vTftjUdTEUgslBhw4LptxatcWQFdsky3YtSr7GqIgXrUVYJqyth5jZG2T8QzriFieV3AgMBAAECgYAnK9pJL+hPV4k7ekowUiFWw5Fao1W5ks4ofI3+7nmRCAG4vCVefIOFleyMjeuNb1D/lsIcC1pkRXpHm4Tao058nwwWsme5qC5TnmJYjeEEQlSSkDeHrE1EzlqDw5CDIUSiBwvG2WG6ioGw5BigpIacb81Ql10e5zMDHviG2R7XAQJBAOltRcFogDx3WPoMxtb5OcabQatFPujvIlhxNM/c8WzFTv6hhxctl/g7kkrst0qccYqvthiSPKtti4Pm1eGa9LcCQQCXJ9I67/rWtG9A//XLu5pzZLe6ycEIHKrBuTFY0wtAZovH/0MU8ddr/PDdRuO8nh5vnp/NP8Rwplt6RgIel1VBAkB/SVflXG+5CTeausbeyO6Jb3YCx/LDIEtxQOIUix2XO4yU6034BKO+9/URKD3W0LJVdvVsNqogrM6MMZ7ltBLHAkAuK3mpivfBcSeyYPyBGHKa4J7GLS4K/kaiGaEbEouTRj9X/X5N/XjhgUdJL4gMave8/GSvWtTz27yBYrTvByaBAkB9jw5c3kbdFf/35wqvOHD2lOlRmcFN18YKBxjWDLWTYfR1ENU3gdsEbWOauSKcvULgmHJjRCUbSBVshd4z9gKb")
                    .PayId("890086000102017612")
                    .PayRsaPrivate("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCw8Vm8Xk2Pse02fJnugrYSEaA4VJCdbF3g7dHq5TIZbXfomTw3J1W6kQpRDzFyhJHiahetMREHdKPXNUDMK5+3DxaOO//teAUpQsLCGPjRFVtYOlXtHt3LRsAJ/CXPZy1HU+LPKbSA+evFk9lW9lGfpBm3SQ5piyDTVMKbn3OpQXvcIgOvjWT2i2ws9FObR3Qx6aR/uK8juticLhZ4fXFY8Zi0N8g/5ZKIkBECBEj3x7BPMexrnDHorSeXgYg/DolN9UXCNz4BADwkGE01faKh9nBHjHxZ1kbj61QYHb7otvFCi8c9tCUpk6NaaFi8MaYbGjEjUbvL8WD7KmSZhtDvAgMBAAECggEASMEEN7rCw3nSYpv7IyHlwSo0KdVDnScItsqyjJXu8pubOS2An+DxlAO9LTVFDKRL47/hulm5ecpQ79U6rnildDyk9pjfE4JNBPkpYWupKzdP1sgtupD9e2682Z4u4ce3y2NHmAy65mlcs2GmdOZVC4IK/NzyKx2EwsBQQHLguM++z9dh7s6lsKvDKEkjOjh/afPNNqYJDzmNWQqIBAm5T+rfw2kkOfrrIMUTTKaZk8UILhafymx1evzEGKRB/oTXFbCjKgJX7pwm5ivTaCQKDuY/4j9ABmLMV8FwlRukhhxWHu0I0ReWPqeavZm/Hne5S+DNAJcEbmT1H8oGhqFsYQKBgQDwUuyL5MbttuODYhmgwatCrBtJNazvp7j1g7dwoMMJ4X9A7wEoZ4WHAvcsje/xfYBovsA6KbnmvWZaYOyzLmiGiSoH9XiXBB66WQzYi51idrui+xViXf1J9qc68Pyxd553hGu7dqyVuFixLTKOgLwuPtMgmV8JG/OQ09u0FCmZ3wKBgQC8fAyhB8Uj/IrtE5WguGtWduOKUfKYK573X2lduMd5F72WQADitylN4huJmzYhqlfi4XaNUgW2BbOCxC1tRMvYFs+pDNPqBWuzV76JhEUtmy+QuwukmBkRpp2YhBEWFth/3iYNcsJxlMMd+tEZyzdgqjhx7jPlriUNny24mW3K8QKBgFQIavYvtO+CN+PE9bkkf0H89D2RKztRpncS653SWOCnnNuTtw/twt8Gxm8p4nDKtY/qw0STOpHYQGXx5lJxM9N3Ot81kkkcA7KdWMSPbST053+B1GRlASBPwwH1KibT8PFRuGoAgTCJGVo6t6bklOiGx0Rq3LROtcg6jGRXeIR5AoGAdbzffq+F3AbjGVjEfNL2adp2JG8wJRIKiOfWv66vG9tSmEp+wyA68uWs4fZMiBcKMzU3guubmapSCftVjQ4Ob3tAWI8kyOh98Fd9NBWlyOlJexT8HuLLthToGN9WdP/sWWOBTaFECQMuJsoIWB5zBzUNcU2kqXJwvuOSAeAFj2ECgYEA4hV3PUuPfo1N8IJafSlns8JsJlf8KXY2FUVYe7rNUoM85no9hLph9LB/GKzOZ3QTxLLuTfkCsXhYk0X9gwKmdmjDToES2Ok4l646W09Lm8Dvdr9wJcHySQ6mS0g5hoStm+dNbJT+X2XzcGo/oZLB7aK52t/xoNmXB/gSmdHt/ow=")
                    .PayRsaPublic("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsPFZvF5Nj7HtNnyZ7oK2EhGgOFSQnWxd4O3R6uUyGW136Jk8NydVupEKUQ8xcoSR4moXrTERB3Sj1zVAzCuftw8Wjjv/7XgFKULCwhj40RVbWDpV7R7dy0bACfwlz2ctR1Pizym0gPnrxZPZVvZRn6QZt0kOaYsg01TCm59zqUF73CIDr41k9otsLPRTm0d0Memkf7ivI7rYnC4WeH1xWPGYtDfIP+WSiJARAgRI98ewTzHsa5wx6K0nl4GIPw6JTfVFwjc+AQA8JBhNNX2iofZwR4x8WdZG4+tUGB2+6LbxQovHPbQlKZOjWmhYvDGmGxoxI1G7y/Fg+ypkmYbQ7wIDAQAB");
        } else {
            builder = new Platform.Builder(PlatformType.DEFAULT);
        }

        if (builder != null)
            mCurrentPlatform = builder.build();

        String[] platforms = getResources().getStringArray(R.array.platforms);
        Toast.makeText(this, platforms[pos], Toast.LENGTH_SHORT).show();
    }

    private void _serverSettings(int pos) {
        if (pos == 1) {
            mServerAddress = "https://GB-cn-qa.joycityplay.com/gbranch/branch/getzone";
            Toast.makeText(this, "Start QA Server", Toast.LENGTH_SHORT).show();
        } else if (pos == 2) {
            mServerAddress = "https://gbranchrev.jc.hogacn.com/branch/getzone";
            Toast.makeText(this, "Start Review Server", Toast.LENGTH_SHORT).show();
        } else if (pos == 3) {
            mServerAddress = "https://gbranch.jc.hogacn.com/branch/getzone";
            Toast.makeText(this, "Start Live Server", Toast.LENGTH_SHORT).show();
        }
    }


    private int mGameCode;
    private String mClientSecretKey;
    private String mServerAddress;

    private Spinner mSpinGameSelector;
    private Spinner mSpinPlatformSelector;
    private Spinner mSpinServerSelector;

    private Button mBtnStartApp;
    private TextView mTxtVersionInfo;
    private Platform mCurrentPlatform;

}
