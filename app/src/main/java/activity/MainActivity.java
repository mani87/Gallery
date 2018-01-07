package activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.sunshine.mani.gallery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.GalleryAdapter;
import app.AppController;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private static final String END_POINT = "https://api.androidhive.info/json/glide.json";
    private ArrayList<com.example.sunshine.mani.gallery.model.Image> imageArray;
    private ProgressDialog progressDialog;
    private GalleryAdapter mGalleryAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        progressDialog = new ProgressDialog(this);
        imageArray = new ArrayList<>();
        mGalleryAdapter = new GalleryAdapter(getApplicationContext(), imageArray);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mGalleryAdapter);

        mRecyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(),mRecyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", imageArray);
                bundle.putInt("position",position);

                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowFragment newFragment = SlideshowFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchImages();
    }

    private void fetchImages() {

        progressDialog.setMessage("JSON data is loading...");
        progressDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(END_POINT, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                progressDialog.hide();

                imageArray.clear();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        com.example.sunshine.mani.gallery.model.Image image = new com.example.sunshine.mani.gallery.model.Image();

                        image.setName(jsonObject.getString("name"));

                        JSONObject imageUrl = jsonObject.getJSONObject("url");

                        image.setSmall(imageUrl.getString("small"));
                        image.setMedium(imageUrl.getString("medium"));
                        image.setLarge(imageUrl.getString("large"));

                        image.setTimestamp(jsonObject.getString("timestamp"));

                        imageArray.add(image);

                    } catch (JSONException e) {
                        Log.e(TAG, "Json Parsing Error " + e.getMessage());
                    }
                }

                mGalleryAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }

        });

        AppController.getInstance().addToRequestQueue(request);
    }


}
