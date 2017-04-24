package qbai22.com.yandextranslator.fragments.translation;

import android.content.Context;
import android.support.v4.content.Loader;

import qbai22.com.yandextranslator.model.dictionaryResponce.DictionaryResponse;
import qbai22.com.yandextranslator.network.ApiFactory;
import qbai22.com.yandextranslator.network.DictionaryService;
import qbai22.com.yandextranslator.utils.TranslationPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by Vladimir Kraev
 */

//Лоадер для загрузки вариантов перевода через API ЯндексСловаря
public class RetrofitDictionaryLoader extends Loader<DictionaryResponse> {

    Call<DictionaryResponse> mCall;
    DictionaryService mService;
    DictionaryResponse mResponse;
    String mTextToTranslate;
    String mLanguagePair;


    public RetrofitDictionaryLoader(Context context) {
        super(context);
        mService = ApiFactory.getDictionaryService();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mResponse != null){
            deliverResult(mResponse);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();

        mTextToTranslate = TranslationPreferencesUtils.getCurrentText(getContext());
        mLanguagePair = TranslationPreferencesUtils.getLanguagePair(getContext());
        mCall = mService.getDictionary(mTextToTranslate, mLanguagePair);
        mCall.enqueue(new Callback<DictionaryResponse>() {
            @Override
            public void onResponse(Call<DictionaryResponse> call, Response<DictionaryResponse> response) {
                mResponse = response.body();
                deliverResult(mResponse);
            }

            @Override
            public void onFailure(Call<DictionaryResponse> call, Throwable t) {
                deliverResult(null);
            }
        });

    }

    @Override
    protected void onStopLoading() {
        mCall.cancel();
        super.onStopLoading();
    }

}
