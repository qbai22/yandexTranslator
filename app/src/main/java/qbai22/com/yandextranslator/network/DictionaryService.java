package qbai22.com.yandextranslator.network;

import qbai22.com.yandextranslator.model.dictionaryResponce.DictionaryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
 * Created by Vladimir Kraev
 */

public interface DictionaryService {

    @GET("lookup")
    Call<DictionaryResponse> getDictionary(@Query("text") String text, @Query("lang") String pairOfLang);
}
