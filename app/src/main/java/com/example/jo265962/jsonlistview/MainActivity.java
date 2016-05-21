package com.example.jo265962.jsonlistview;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListJsonAdapter adapter;
    private List<JsonRow> data = new ArrayList<>();;
    private Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefresh;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Setting with Loading.., the title bar name to be set from Json
        toolbar.setTitle("Loading...");
        setSupportActionBar(toolbar);

        initViews();
    }

    private void initViews(){
        //Initializing views
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListJsonAdapter(this, data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Fetch Json data from URL
        loadJSON();

    }

    @Override
    protected void onResume() {
        super.onResume();
        handleSwipeRefresh();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("jsonlistview", "New Configuration : " + newConfig.orientation);
    }

   private void dismissProgressDialogs() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    private void loadJSON(){
        //Checking for network availability
        if (Utils.isDataNetworkConnected(this)) {
            //Show the progress dialog
            if (mSwipeRefresh == null) {
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Loading Please wait...");
                pDialog.show();
            }

            //Call Retrofit Request and handle the response
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RequestInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestInterface request = retrofit.create(RequestInterface.class);

            Call<JSONResponse> call = request.getJSON();
            call.enqueue(new Callback<JSONResponse>() {

                @Override
                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();
                    toolbar.setTitle(jsonResponse.getTitle());
                    data = jsonResponse.jsonRows();
                    dismissProgressDialogs();

                    adapter.setJsonData(data);
                    adapter.notifyDataSetChanged();
                    if (mSwipeRefresh != null) {
                        Toast.makeText(MainActivity.this, "Loaded the Json List", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    dismissProgressDialogs();
                    Log.d("Error", t.getMessage());
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "No Internet, Connect Internet", Toast.LENGTH_LONG).show();
        }
    }

    // Register for pull to refresh listener and handle the callback method
    private void handleSwipeRefresh() {
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //Handle the OnRefresh to reload
            @Override
            public void onRefresh() {
                    loadJSON();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
