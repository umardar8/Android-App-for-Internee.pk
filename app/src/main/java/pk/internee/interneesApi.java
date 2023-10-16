package pk.internee;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface interneesApi {
    @GET("users")
    Call<List<internees>> getInterness();
}
