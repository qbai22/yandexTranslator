package qbai22.com.yandextranslator.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by qbai on 15.03.2017.
 */

public class Translation extends RealmObject {

    @PrimaryKey
    private String id;
    private boolean isBookmarked;
    private String inputText;
    private String translatedText;
    private String fromLangCode;
    private String toLangCode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromLangCode() {
        return fromLangCode;
    }

    public void setFromLangCode(String fromLangCode) {
        this.fromLangCode = fromLangCode;
    }

    public String getToLangCode() {
        return toLangCode;
    }

    public void setToLangCode(String toLangCode) {
        this.toLangCode = toLangCode;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}
