package qbai22.com.yandextranslator.model.dictionaryResponce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qbai on 17.03.2017.
 */

public class DictionaryTranslation {
    String text;
    @SerializedName("pos")
    String partOfSpeach;
    String asp;
    @SerializedName("gen")
    String gender;
    @SerializedName("syn")
    List<Synonym> synonyms = new ArrayList<>();
    @SerializedName("mean")
    List<Meaning> meanings = new ArrayList<>();
    @SerializedName("ex")
    List<Example> examples = new ArrayList<>();

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPartOfSpeach() {
        return partOfSpeach;
    }

    public void setPartOfSpeach(String partOfSpeach) {
        this.partOfSpeach = partOfSpeach;
    }

    public String getAsp() {
        return asp;
    }

    public void setAsp(String asp) {
        this.asp = asp;
    }

    public List<Synonym> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<Synonym> synonyms) {
        this.synonyms = synonyms;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = meanings;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

}
