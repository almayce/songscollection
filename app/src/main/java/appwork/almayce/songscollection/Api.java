package appwork.almayce.songscollection;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by almayce on 27.04.17.
 */

public interface Api {
    @GET("api/songs")
    Observable<List<SongsResponse>> getSongsList();
}
