package qbai22.com.yandextranslator.network;

import qbai22.com.yandextranslator.model.dictionaryResponce.DictionaryResponce;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by qbai on 17.03.2017.
 */

public interface DictionaryService {
    @GET("lookup")
    Call<DictionaryResponce> getDictionary(@Query("text") String text, @Query("lang") String pairOfLang);
}