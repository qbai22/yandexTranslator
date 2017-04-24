package qbai22.com.yandextranslator.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import qbai22.com.yandextranslator.model.Language;

/*
 * Created by Vladimir Kraev
 */

public class TranslationPreferencesUtils {

    private static final String TAG = "trans utils";

    private static final String CURRENT_TEXT = "current text";
    private static final String LANGUAGE_PAIR = "language pair";
    private static final String FROM_LANGUAGE_LABEL = "from lang label";
    private static final String FROM_LANGUAGE_CODE = "from lang code";
    private static final String TO_LANGUAGE_LABEL = "to language label";
    private static final String TO_LANGUAGE_CODE = "to language code";
    private static final String CURRENT_TRANSLATION_ID = "current id";


    public static void setCurrentText(Context context, String text) {
        try {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPref.edit().putString(CURRENT_TEXT, text).apply();
        } catch (Exception e) {
            Log.e(TAG, "setCurrentText: ", e);
        }
    }

    public static String getCurrentText(Context context) {
        try {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            return sharedPref.getString(CURRENT_TEXT, "");
        } catch (Exception e) {
            Log.e(TAG, "getCurrentText: ", e);
        }
        return "";
    }

    //метод обновляет пару языков которую использует ретрофит для запроса
    public static void updateLanguagePairCodes(Context context) {

        String languagePair;
        String fromLanguageCode = getFromLanguage(context).getCode();
        String toLanguageCode = getToLanguage(context).getCode();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        if (fromLanguageCode.equals("auto")) {
            languagePair = toLanguageCode;
            sharedPref.edit().putString(LANGUAGE_PAIR, languagePair).apply();
        } else if (toLanguageCode.equals("auto")) {
            languagePair = fromLanguageCode;
            sharedPref.edit().putString(LANGUAGE_PAIR, languagePair).apply();
        } else {
            languagePair = fromLanguageCode + "-" + toLanguageCode;
            sharedPref.edit().putString(LANGUAGE_PAIR, languagePair).apply();
        }
    }

    public static String getLanguagePair(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(LANGUAGE_PAIR, "en-ru");
    }

    public static void setFromLanguage(Context context, String label, String code) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putString(FROM_LANGUAGE_LABEL, label).apply();
        sharedPref.edit().putString(FROM_LANGUAGE_CODE, code).apply();
    }

    public static Language getFromLanguage(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        return new Language(
                sharedPref.getString(FROM_LANGUAGE_LABEL, "Английский"),
                sharedPref.getString(FROM_LANGUAGE_CODE, "en"));
    }

    public static void setToLanguage(Context context, String label, String code) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putString(TO_LANGUAGE_LABEL, label).apply();
        sharedPref.edit().putString(TO_LANGUAGE_CODE, code).apply();
    }

    public static Language getToLanguage(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        return new Language(
                sharedPref.getString(TO_LANGUAGE_LABEL, "Русский"),
                sharedPref.getString(TO_LANGUAGE_CODE, "ru"));
    }

    public static void setCurrentTranslationId(Context context, String id){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putString(CURRENT_TRANSLATION_ID, id).apply();
    }

    public static String getCurrentTranslationId(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(CURRENT_TRANSLATION_ID, "");
    }
}