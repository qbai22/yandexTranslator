package qbai22.com.yandextranslator.network;

import qbai22.com.yandextranslator.model.transatorResponce.YandexResponce;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by qbai on 15.03.2017.
 */

public interface TranslationService {
    @POST("translate")
    Call<YandexResponce> getPairTranslation(@Query("text") String text, @Query("lang") String pairOfLang);
}
