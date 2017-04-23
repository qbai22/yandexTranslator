package qbai22.com.yandextranslator.utils;

import android.content.Context;
import android.util.Log;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import qbai22.com.yandextranslator.model.realm.Translation;

/**
 * Created by qbai on 15.03.2017.
 */

public class RealmTranslationHelper {


    public static void changeBookmarkStatus(Realm realm, Boolean isBookmarked, String input, String output) {
        realm.beginTransaction();
        Translation translation = realm.where(Translation.class)
                .equalTo("inputText", input)
                .equalTo("translatedText", output)
                .findFirst();
        if(translation == null){
            realm.cancelTransaction();
            return;
        }
        realm.copyToRealmOrUpdate(translation);
        translation.setBookmarked(isBookmarked);
        realm.commitTransaction();
    }

    //метод используется для настройки состояния кнопки избранного после ответа сервера
    public static boolean isTranslationBookmarked(Realm realm, String input, String output){
        Translation translation = realm.where(Translation.class)
                .equalTo("inputText", input)
                .equalTo("translatedText", output)
                .findFirst();
        if(translation == null){
            return false;
        }
        return translation.isBookmarked();
    }

    public static Translation createTranslation(
            Realm realm,
            final String inputText,
            final String translatedText,
            final String fromLang,
            final String toLang) {

        realm.beginTransaction();
        String id = UUID.randomUUID().toString();
        Translation translation = realm.createObject(Translation.class, id);
        translation.setBookmarked(false);
        translation.setInputText(inputText);
        translation.setTranslatedText(translatedText);
        translation.setFromLangCode(fromLang);
        translation.setToLangCode(toLang);
        realm.commitTransaction();
        return translation;
    }

    public static boolean checkIfExists(Realm realm, String input, String output) {
        //запрещаем записывать пустые переводы
        if (input == null || input.equals("")) return true;
        Translation translation = realm.where(Translation.class)
                .equalTo("inputText", input)
                .equalTo("translatedText", output)
                .findFirst();
        return translation != null;
    }

    public static Translation getCurrentTranslation(Realm realm, Context context){
        String id = TranslationPreferencesUtils.getCurrentTranslationId(context);
        return realm.where(Translation.class).equalTo("id", id).findFirst();
    }


}
