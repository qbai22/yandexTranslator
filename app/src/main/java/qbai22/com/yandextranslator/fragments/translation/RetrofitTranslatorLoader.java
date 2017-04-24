package qbai22.com.yandextranslator.fragments.translation;


import android.content.Context;
import android.support.v4.content.Loader;

import qbai22.com.yandextranslator.model.transatorResponce.TranslatorResponse;
import qbai22.com.yandextranslator.network.ApiFactory;
import qbai22.com.yandextranslator.network.TranslationService;
import qbai22.com.yandextranslator.utils.TranslationPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by Vladimir Kraev
 */

//Лоадер для загрузки перевода через API ЯндексПереводчика
public class RetrofitTranslatorLoader extends Loader<TranslatorResponse> {

    Call<TranslatorResponse> mCall;
    TranslationService mService;
    TranslatorResponse mResponse;
    String mTextToTranslate;
    String mLanguagePair;

    public RetrofitTranslatorLoader(Context context) {
        super(context);
        mService = ApiFactory.getTranslationService();
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
        mCall = mService.getPairTranslation(mTextToTranslate, mLanguagePair);
        mCall.enqueue(new Callback<TranslatorResponse>() {
            @Override
            public void onResponse(Call<TranslatorResponse> call, Response<TranslatorResponse> response) {
                mResponse = response.body();
                deliverResult(mResponse);
            }

            @Override
            public void onFailure(Call<TranslatorResponse> call, Throwable t) {
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
