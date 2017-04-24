package qbai22.com.yandextranslator.model.dictionaryResponce;

import com.google.gson.annotations.SerializedName;

/*
 * Created by Vladimir Kraev
 */

public class Synonym {
    String text;
    String pos;
    String asp;
    @SerializedName("gen")
    String gender;

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

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getAsp() {
        return asp;
    }

    public void setAsp(String asp) {
        this.asp = asp;
    }
}
