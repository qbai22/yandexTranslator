package qbai22.com.yandextranslator.network.loader;

public interface Callback<D> {

    void onFailure(Exception ex);

    void onSuccess(D result);
}
