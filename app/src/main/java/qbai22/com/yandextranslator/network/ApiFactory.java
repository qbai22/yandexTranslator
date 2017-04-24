package qbai22.com.yandextranslator.network;


import okhttp3.OkHttpClient;
import qbai22.com.yandextranslator.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Created by Vladimir Kraev
 */

public class ApiFactory {

    private static OkHttpClient sTranslationClient;
    private static TranslationService sTranslationService;

    private static OkHttpClient sDictionaryClient;
    private static DictionaryService sDictionaryService;

    public static TranslationService getTranslationService() {
        TranslationService service = sTranslationService;
        if (service == null) {
            synchronized (ApiFactory.class) {
                service = sTranslationService;
                if (service == null) {
                    service = sTranslationService = createTranslationService();
                }
            }
        }
        return service;
    }

    public static DictionaryService getDictionaryService() {
        DictionaryService service = sDictionaryService;
        if (service == null) {
            synchronized (ApiFactory.class) {
                service = sDictionaryService;
                if (service == null) {
                    service = sDictionaryService = createDictionaryService();
                }
            }
        }
        return service;
    }

    private static TranslationService createTranslationService() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.TRANSLATOR_API_ENDPOINT)
                .client(getTranslationClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TranslationService.class);
    }

    private static DictionaryService createDictionaryService() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.DICTIONARY_API_ENDPOINT)
                .client(getDictionaryClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DictionaryService.class);

    }

    private static OkHttpClient getTranslationClient() {
        OkHttpClient client = sTranslationClient;
        if (client == null) {
            synchronized (ApiFactory.class) {
                client = sTranslationClient;
                if (client == null) {
                    client = sTranslationClient = buildTranslationClient();
                }
            }
        }
        return client;
    }

    private static OkHttpClient getDictionaryClient() {
        OkHttpClient client = sDictionaryClient;
        if (client == null) {
            synchronized (ApiFactory.class) {
                client = sDictionaryClient;
                if (client == null) {
                    client = sDictionaryClient = buildDictionaryClient();
                }
            }
        }
        return client;
    }

    private static OkHttpClient buildTranslationClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new ApiTranslateRequestInterceptor())
                .build();
    }

    private static OkHttpClient buildDictionaryClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(new ApiDictionaryRequestInterceptor())
                .build();
    }

}
