package qbai22.com.yandextranslator.network;

import qbai22.com.yandextranslator.model.transatorResponce.TranslatorResponse;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
 * Created by Vladimir Kraev
 */

public interface TranslationService {

    @POST("translate")
    Call<TranslatorResponse> getPairTranslation(@Query("text") String text, @Query("lang") String pairOfLang);
}
