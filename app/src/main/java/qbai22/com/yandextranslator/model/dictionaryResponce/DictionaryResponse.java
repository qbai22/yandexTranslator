package qbai22.com.yandextranslator.model.dictionaryResponce;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Vladimir Kraev
 */

public class DictionaryResponse {
    Head head;
    List<PartOfSpeach> def = new ArrayList<>();

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public List<PartOfSpeach> getDef() {
        return def;
    }

    public void setDef(List<PartOfSpeach> def) {
        this.def = def;
    }
}
