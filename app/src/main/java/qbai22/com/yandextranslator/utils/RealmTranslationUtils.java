package qbai22.com.yandextranslator.utils;

import android.util.Log;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import qbai22.com.yandextranslator.model.realm.Translation;

/**
 * Created by qbai on 15.03.2017.
 */

public class RealmTranslationUtils {

    private Realm mRealm;

    public RealmTranslationUtils(Realm realm) {
        mRealm = realm;
    }

    public static RealmTranslationUtils getUtils(Realm realm) {
        return new RealmTranslationUtils(realm);
    }


    public static RealmResults<Translation> getAllTranslations() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Translation> results = realm.where(Translation.class)
                .findAll();
        realm.close();
        return results;
    }

    public void changeBookmarkStatus(final Translation translation, final boolean isBookmarked) {
        mRealm.beginTransaction();
        String id = translation.getId();
        mRealm.where(Translation.class)
                .equalTo("id", id)
                .findFirst()
                .setBookmarked(isBookmarked);
        mRealm.commitTransaction();
    }

    public void copyToRealm(Translation translation){
        mRealm.beginTransaction();
        mRealm.copyToRealm(translation);
        mRealm.commitTransaction();
    }

    public RealmResults<Translation> getAllBookmarkedTranslations() {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Translation> results = r.where(Translation.class)
                .equalTo("isBookmarked", true)
                .findAll();
        Log.e("111 111", "getAllBookmarkedTranslations: " + results.size());
        return results;
    }

    public Translation createTranslation(final boolean isBookmarked,
                                         final String inputText,
                                         final String translatedText,
                                         final String fromLang,
                                         final String toLang) {

        String id = UUID.randomUUID().toString();
        mRealm.beginTransaction();
        Translation translation = new Translation();
        translation.setId(id);
        translation.setBookmarked(isBookmarked);
        translation.setInputText(inputText);
        translation.setTranslatedText(translatedText);
        translation.setFromLangCode(fromLang);
        translation.setToLangCode(toLang);
        mRealm.commitTransaction();

        return translation;
    }
}
