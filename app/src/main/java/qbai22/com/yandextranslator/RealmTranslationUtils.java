package qbai22.com.yandextranslator;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import qbai22.com.yandextranslator.model.realm.Translation;

/**
 * Created by qbai on 15.03.2017.
 */

public class RealmTranslationUtils {

    public static Translation createTranslation(boolean isBookmarked,
                                                String inputText,
                                                String translatedText,
                                                String fromLang,
                                                String toLang) {

        Translation translation = new Translation();
        String id = UUID.randomUUID().toString();
        translation.setId(id);
        translation.setBookmarked(isBookmarked);
        translation.setInputText(inputText);
        translation.setTranslatedText(translatedText);
        translation.setFromLangCode(fromLang);
        translation.setToLangCode(toLang);

        return translation;
    }

    public static void copyTranslationToRealm(Translation translation){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(translation);
        realm.commitTransaction();
    }

    public static void createTranslationAndCopy(boolean isBookmarked,
                                                String inputText,
                                                String translatedText,
                                                String fromLang,
                                                String toLang){
        Translation translation = new Translation();
        translation.setBookmarked(isBookmarked);
        translation.setInputText(inputText);
        translation.setTranslatedText(translatedText);
        translation.setFromLangCode(fromLang);
        translation.setToLangCode(toLang);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(translation);
        realm.commitTransaction();
    }

    public static void changeBookmarkStatus(Translation translation, boolean isBookmarked){
        Realm realm = Realm.getDefaultInstance();
        String id = translation.getId();
        realm.where(Translation.class).equalTo("id", id).findFirstAsync().setBookmarked(isBookmarked);
    }

    public static RealmResults<Translation> getAllTranslations(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Translation> results = realm.where(Translation.class).findAllAsync();
        return results;
    }

}
