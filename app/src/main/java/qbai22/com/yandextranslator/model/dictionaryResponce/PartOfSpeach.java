package qbai22.com.yandextranslator.model.dictionaryResponce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qbai on 17.03.2017.
 */

public class PartOfSpeach {
    String text;
    @SerializedName("pos")
    String partOfSpeech;
    @SerializedName("ts")
    String transcription;
    @SerializedName("tr")
    List<DictionaryTranslation> translations = new ArrayList<>();

    public String getText() {
        return text;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public List<DictionaryTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<DictionaryTranslation> translations) {
        this.translations = translations;
    }
}
