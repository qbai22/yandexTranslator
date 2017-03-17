package qbai22.com.yandextranslator.model.dictionaryResponce;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qbai on 17.03.2017.
 */

public class Example {
    @SerializedName("text")
    String exampleText;
    @SerializedName("tr")
    List<ExampleTranslation> exampleTranslations;

    public String getExampleText() {
        return exampleText;
    }

    public void setExampleText(String exampleText) {
        this.exampleText = exampleText;
    }

    public List<ExampleTranslation> getExampleTranslations() {
        return exampleTranslations;
    }

    public void setExampleTranslations(List<ExampleTranslation> exampleTranslations) {
        this.exampleTranslations = exampleTranslations;
    }
}
