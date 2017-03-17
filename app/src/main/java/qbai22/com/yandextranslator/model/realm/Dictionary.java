package qbai22.com.yandextranslator.model.realm;

import io.realm.RealmObject;

/**
 * Created by qbai on 16.03.2017.
 */

public class Dictionary extends RealmObject {
    String syn;

    public String getSyn() {
        return syn;
    }

    public void setSyn(String syn) {
        this.syn = syn;
    }
}
