package qbai22.com.yandextranslator.model.transatorResponce;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qbai on 15.03.2017.
 */

public class YandexResponce {
    int code;
    String lang;
    List<String> text = new ArrayList<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }
}
