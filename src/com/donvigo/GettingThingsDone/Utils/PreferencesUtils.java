package com.donvigo.GettingThingsDone.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {
	/**
	 * Add string value to application shared preferences
	 * @param context Current application context
     * @param preferenceName Application preference name
	 * @param key Preference key
	 * @param value String value
	 */
	public static void addStringToPreferences(Context context, String preferenceName, String key, String value){
		SharedPreferences settings = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * Add boolean value to application shared preferences
	 * @param context Current application context
     * @param preferenceName Application preference name
	 * @param key Preference key
	 * @param value Boolean value
	 */
	public static void addBooleanToPreferences(Context context, String preferenceName, String key, boolean value){
		SharedPreferences settings = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	/**
	 * Get string value from application shared preferences
	 * @param context Current application context
	 * @param preferenceName Application preference name
	 * @param key Preference key
	 * @param defaultValue Default string value
	 * @return Appropriate string value
	 */
	public static String getStringFromPreferences(Context context, String preferenceName, String key, String defaultValue){
		return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getString(key, defaultValue);
	}

    /**
     * Get boolean value from application shared preferences
     * @param context Current application context
     * @param preferenceName Application preference name
     * @param key Preference key
     * @param defaultValue Default boolean value
     * @return Appropriate boolean value
     */
	public static boolean getBooleanFromPreferences(Context context, String preferenceName, String key, boolean defaultValue){
		return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
	}

	public static String getCityName(Context context){
		return PreferencesUtils.getStringFromPreferences(context, Constants.PREFERENCE_NAME,
				Constants.PREFERENCE_KEY_CITY_NAME, "");
	}

    public static String getCityCode(Context context){
        return PreferencesUtils.getStringFromPreferences(context, Constants.PREFERENCE_NAME,
                Constants.PREFERENCE_KEY_CITY_CODE, "");
    }

    public static void putCityName(Context context, String cityName){
        addStringToPreferences(context, Constants.PREFERENCE_NAME, Constants.PREFERENCE_KEY_CITY_NAME, cityName);
    }

    public static void putCityCode(Context context, String cityCode){
        addStringToPreferences(context, Constants.PREFERENCE_NAME, Constants.PREFERENCE_KEY_CITY_CODE, cityCode);
    }
}
