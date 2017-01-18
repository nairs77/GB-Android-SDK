package com.gebros.platform.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.gebros.platform.log.GBLog;
import com.joycity.platform.sdk.Joyple;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

//import android.support.annotation.RequiresPermission;

public class GBDeviceUtils {
	private static final String TAG = GBDeviceUtils.class.getCanonicalName();
	
	public static final String OS_TYPE = "Android";
	public static final int ANDROID_API_LEVEL = Build.VERSION.SDK_INT;
	public static final String ANDROID_SDK_VERSION = Build.VERSION.RELEASE;
	public static final String DEVICE_MODEL = Build.MODEL;
	
	public static final boolean SUPPORTS_LOLLIPOP = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
	public static final boolean SUPPORTS_KITKAT = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT;
	public static final boolean SUPPORTS_JELLY_BEAN = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
	public static final boolean SUPPORTS_ICE_CREAM_SANDWITCH = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    public static final boolean SUPPORTS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	public static final boolean SUPPORTS_GINGERBREAD = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;
    public static final boolean SUPPORTS_FROYO = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO;
    public static final boolean SUPPORTS_ECLAIR = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ECLAIR;

	private static final String DEVICE_VALUE_MID_FIX = "; ";
	private static final int ROOTED = 1;
	private static final int NOT_ROOTED = 0;

	static final String[] ROOTING_PATHS = {
			"/sbin/su",
			"/system/bin/su",
			"/system/xbin/su",
			"/data/local/xbin/su",
			"/data/local/bin/su",
			"/data/local/su",
			"/system/sd/xbin/su",
			"/system/bin/failsafe/su",
			"/system/app/SuperUser.apk",
			"/data/data/com.noshufou.android.su",
	};

	private static final int BAD_DEVICE_ID_LENGTH = 1;
	private static final String EMPTY_DEVICE_ID = "-1";
	private static final String EMPTY_PHONE_NUMBER = "-1";
	private static final String BAD_DEVICE_ID_PATTERN = "000000000000000";


//	@RequiresPermission(allOf = {
//			Manifest.permission.READ_PHONE_STATE,
//			Manifest.permission.ACCESS_WIFI_STATE})

	public static String getDeviceId() {
		Context context = Joyple.getApplicationContext();

		if (!GBValidator.isValidPermission(context, Manifest.permission.READ_PHONE_STATE)) {
			GBLog.d(TAG + "Not Declare Permission = %s", Manifest.permission.READ_PHONE_STATE);
			return "";
		}

		TelephonyManager manager = null;

		try {
			manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		String deviceId = (manager.getDeviceId() == null ? EMPTY_DEVICE_ID : manager.getDeviceId());

		if(!isDeviceId(deviceId)) {
			String serialNumber = getDeviceSerialNumber();
			deviceId = Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + (serialNumber == null ? "" : serialNumber);
		}

		if(!isDeviceId(deviceId)) {
			try {
				WifiManager m = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo info = m.getConnectionInfo();
				deviceId = info.getMacAddress();
			} catch (Exception exception) {
				JLog.e(exception, "%s", exception.getMessage());
			}
		}

				
		return deviceId;
	}
	
	public static String getMcc() {
		Context context = Joyple.getApplicationContext();
		try {
			TelephonyManager tel = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			String networkOperator = tel.getSimOperator();
			String mcc = "";
			
			if (networkOperator != null) {
				mcc = networkOperator.substring(0, 3);
			}
			
			return mcc;
		} catch (Exception e) {}
		
		return "";		
	}
	
	public static boolean isServiceLocaleKorea() {
		String mcc = getMcc();
		if (!JoypleValidator.isNullOrEmpty(mcc)) {
			if (mcc.equals("450")) {
				return true;
			} else {
				return false;
			}
		} else {
			String currentLanguage = getLanguage();
			if (!JoypleValidator.isNullOrEmpty(currentLanguage) && currentLanguage.equals("ko")) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static String getVendor() {
		Context context = Joyple.getApplicationContext();
		TelephonyManager manager = null;
		
		try {
			manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		String vendor = manager.getNetworkOperatorName();
				
		return vendor;
	}
	
	public static String getLocale() {
		Context context = Joyple.getApplicationContext();
		TelephonyManager manager = null;
		
		try {
			manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		String locale =  manager.getNetworkCountryIso();
		
		//DeviceUtilsManager.getInstance().setLocale(locale);
		
		return locale;		
	}	
	
	public static String getLanguage() {
		Context context = Joyple.getApplicationContext();
		
		String language = context.getResources().getConfiguration().locale.getLanguage();
		if (language.equals("zh")) {
			language = Locale.getDefault().toString();
		}
		
		if("zh_CN".equals(language)) {
			language = "zh";
		} else if("zh_TW".equals(language)) {
			language = "zt";
		} 
		
		return language;
	}	
	
	public static String getIpAddress() {
		final String IP_NONE = "N/A";
		final String WIFI_DEVICE_PREFIX = "eth";
		  
		String LocalIP = IP_NONE;
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						if( LocalIP.equals(IP_NONE)) {
							/**
							 * After 4.0.3, return IPv6 type address(Default), call getHostAddress().  
							 */
							if (inetAddress instanceof Inet4Address) {
								LocalIP = inetAddress.getHostAddress().toString();
							} else { //IPv6
								LocalIP = inetAddress.getHostAddress().toString();
							}
						} else if( intf.getName().startsWith(WIFI_DEVICE_PREFIX) ) {
							LocalIP = inetAddress.getHostAddress().toString();
						}
					}
				}
			}
		} catch (SocketException e) {}
		  	  
		return LocalIP;
	}

	public static String getGameVersion() {
		PackageInfo pInfo = null;
		Context context = Joyple.getApplicationContext();
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}

		return pInfo.versionName;
	}

	public static boolean isRooted() {
/*
		boolean isValid = false;

		Logger.d(TAG + "Check Rooting or not");

		try {

			Runtime.getRuntime().exec("su");
			isValid = true;

		} catch (Exception e) {
			isValid = false;
		}

		if(!isValid) {
			isValid = checkRootingFiles(ROOTING_PATHS);
		}

		isRooted = isValid;
*/
		String ROOTING_ROOT_PATH = Environment.getExternalStorageDirectory() + "";

		for(String path : ROOTING_PATHS) {
			File file = new File(ROOTING_ROOT_PATH + path);
			if(file != null && file.exists() && file.isFile())
				return true;
		}

		return false;
	}

//	@RequiresPermission(allOf = {
//			Manifest.permission.READ_PHONE_STATE,
//			Manifest.permission.ACCESS_WIFI_STATE})
	public static String getEntireDeviceInfo() {
		StringBuilder deviceInfo = new StringBuilder();
		deviceInfo.append("udid=" + JoypleDeviceUtils.getDeviceId()).append(DEVICE_VALUE_MID_FIX);
		deviceInfo.append("mdn=" + "-1").append(DEVICE_VALUE_MID_FIX);
		deviceInfo.append("os=" + JoypleDeviceUtils.OS_TYPE).append(DEVICE_VALUE_MID_FIX);
		deviceInfo.append("os-version=" + JoypleDeviceUtils.ANDROID_SDK_VERSION).append(DEVICE_VALUE_MID_FIX);
		deviceInfo.append("device=" + JoypleDeviceUtils.DEVICE_MODEL).append(DEVICE_VALUE_MID_FIX);
		deviceInfo.append("rooting=" + (JoypleDeviceUtils.isRooted() ? ROOTED : NOT_ROOTED)).append(DEVICE_VALUE_MID_FIX);
		deviceInfo.append("carrier=" + JoypleDeviceUtils.getVendor()).append(DEVICE_VALUE_MID_FIX);
		deviceInfo.append("locale=" + JoypleDeviceUtils.getLocale()).append(DEVICE_VALUE_MID_FIX);
		deviceInfo.append("language=" + JoypleDeviceUtils.getLanguage());

		return deviceInfo.toString();
	}



	private static boolean isDeviceId(String deviceId) {
		return !(JoypleValidator.isNullOrEmpty(deviceId) || deviceId.length() <= BAD_DEVICE_ID_LENGTH || deviceId.equals(BAD_DEVICE_ID_PATTERN) || deviceId.equals(EMPTY_PHONE_NUMBER));
	}
	
	private static String getDeviceSerialNumber() {
		try {
			return (String) Build.class.getField("SERIAL").get(null);
		} catch (Exception ignored) {
			return null;
		}
	}

	private boolean checkRootingFiles(String[] filePaths) {

		boolean isValid = false;
/*
		Logger.d(TAG + "Check Rooting or not");

		try {

			Runtime.getRuntime().exec("su");
			isValid = true;

		} catch (Exception e) {
			isValid = false;
		}

		if(!isValid) {
			isValid = checkRootingFiles(ROOTING_PATHS);
		}

		isRooted = isValid;
*/
		String ROOTING_ROOT_PATH = Environment.getExternalStorageDirectory() + "";

		for(String path : filePaths) {
			File file = new File(ROOTING_ROOT_PATH + path);
			if(file != null && file.exists() && file.isFile())
				return true;
		}

		return false;
	}
}
