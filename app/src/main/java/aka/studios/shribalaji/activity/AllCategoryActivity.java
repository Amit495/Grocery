package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.adapter.CategoryAdapter;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
//import com.example.eatnow.databinding.ActivityAllCategoryBinding;
import aka.studios.shribalaji.model.Category;
import aka.studios.shribalaji.retrofit.ClientAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCategoryActivity extends AppCompatActivity {

    private static final String TAG = "AllCategoryActivity";
//    private ActivityAllCategoryBinding binding;
    private ApplicationPreference applicationPreference;
    private ClientAPI clientAPI;

    // [START Category]
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categoryArrayList;
    private GridLayoutManager categoryGridLayoutManager;
    // [END Category]

    private RecyclerView categoryRecyclerView;
    private Toolbar toolbarAllCategory;
    private GifImageView loadingImageView;
    private LottieAnimationView notFoundAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        toolbarAllCategory = (Toolbar) findViewById(R.id.toolbarAllCategory);
        setSupportActionBar(toolbarAllCategory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Categories");

//        binding = ActivityAllCategoryBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(AllCategoryActivity.this);

        categoryRecyclerView = (RecyclerView) findViewById(R.id.categoryRecyclerView);
        loadingImageView = (GifImageView) findViewById(R.id.loadingImageView);
        notFoundAnimation = (LottieAnimationView) findViewById(R.id.notFoundAnimation);

        categoryArrayList = new ArrayList<>();

        // [Start Category]
        categoryGridLayoutManager = new GridLayoutManager(AllCategoryActivity.this, 2);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(categoryGridLayoutManager);
        // [End Category]

        getCategory();
    }

    private void getCategory() {
        loadingImageView.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = clientAPI.getCategories();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        loadingImageView.setVisibility(View.GONE);
                        categoryArrayList.clear();
                        String results = response.body().string();
                        Log.d(TAG, "CategoryResults: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                int id = data.getInt("id");
                                int parent_id = data.getInt("parent_id");
                                String name = data.getString("name");
                                String description = data.getString("description");
                                String url = data.getString("url");
                                String image = data.getString("image");
                                int status = data.getInt("status");

                                Log.d(TAG, "CategoryResultsName: " + name);

                                categoryArrayList.add(new Category(id,
                                        parent_id,
                                        name,
                                        description,
                                        url,
                                        image,
                                        status));
                            }
                            if (categoryArrayList.size() > 0) {
                                categoryRecyclerView.setVisibility(View.VISIBLE);
                                notFoundAnimation.setVisibility(View.GONE);
                                categoryAdapter = new CategoryAdapter(AllCategoryActivity.this, categoryArrayList);
                                categoryAdapter.notifyDataSetChanged();
                                categoryRecyclerView.setAdapter(categoryAdapter);
                            }
                            else {
                                categoryRecyclerView.setVisibility(View.GONE);
                                notFoundAnimation.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadingImageView.setVisibility(View.GONE);
                notFoundAnimation.setVisibility(View.VISIBLE);
            }
        });
    }
}
