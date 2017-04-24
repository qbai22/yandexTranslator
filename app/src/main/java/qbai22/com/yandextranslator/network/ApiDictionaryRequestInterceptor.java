package qbai22.com.yandextranslator.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import qbai22.com.yandextranslator.BuildConfig;

/*
 * Created by Vladimir Kraev
 */

public class ApiDictionaryRequestInterceptor implements Interceptor {
    private static final String TAG = "API DICTIONARY INTER ";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("key", BuildConfig.DICTIONARY_API_KEY)
                .addQueryParameter("ui", "ru")
                .build();

        request = request.newBuilder().url(url).build();

        return chain.proceed(request);
    }
}
