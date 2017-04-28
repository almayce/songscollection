package appwork.almayce.songscollection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by almayce on 27.04.17.
 */

public class SongsResponse {
    @SerializedName("label")
    @Expose
    public String label = "";
    @SerializedName("author")
    @Expose
    public String author = "";
    @SerializedName("id")
    @Expose
    public int id = 0;

}
