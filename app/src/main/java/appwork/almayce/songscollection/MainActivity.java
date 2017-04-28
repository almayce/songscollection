package appwork.almayce.songscollection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Api api;
    private ArrayList<String> songs;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        initUI();

        request();
    }

    @Override
    public void onRefresh() {
        request();
    }

    void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://tomcat.kilograpp.com/songs/")
                .build();
        api = retrofit.create(Api.class);
        songs = new ArrayList<>();
    }

    void initUI() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        GridView gridView = (GridView) findViewById(R.id.gridView);

        swipeRefreshLayout.setOnRefreshListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item, R.id.tvSong, songs);
        gridView.setAdapter(adapter);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grid_item_anim);
        GridLayoutAnimationController controller = new GridLayoutAnimationController(animation, .2f, .2f);
        gridView.setLayoutAnimation(controller);
    }

    void request() {
        swipeRefreshLayout.setRefreshing(true);
        if (isOnline())
            api.getSongsList()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        songs.clear();
                        for (SongsResponse resp : response)
                            songs.add(resp.author + "\n\n" + resp.label);
                        swipeRefreshLayout.setRefreshing(false);
                    });
        else {
            Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
