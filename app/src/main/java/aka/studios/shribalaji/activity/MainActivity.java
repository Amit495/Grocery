package aka.studios.shribalaji.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.adapter.AdvtBannerSliderAdapter;
import aka.studios.shribalaji.adapter.BannerSliderAdapter;
import aka.studios.shribalaji.adapter.CategoryAdapter;
import aka.studios.shribalaji.adapter.FeaturedProductAdapter;
import aka.studios.shribalaji.adapter.TopProductAdapter;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
//import com.example.eatnow.databinding.ActivityMainBinding;
import aka.studios.shribalaji.model.AdvtBanner;
import aka.studios.shribalaji.model.Banner;
import aka.studios.shribalaji.model.Category;
import aka.studios.shribalaji.model.Product;
import aka.studios.shribalaji.retrofit.ClientAPI;
import com.google.android.material.navigation.NavigationView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private int CURRENT_BANNER_PAGE = 0;
    private int NUM_PAGES_BANNERS = 0;

    private int CURRENT_ADVTBANNER_PAGE = 0;
    private int NUM_PAGES_ADVTBANNERS = 0;

//    private ActivityMainBinding binding;
    private ClientAPI clientAPI;
    private ApplicationPreference applicationPreference;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RecyclerView categoryRecyclerView;
    private RecyclerView featuredRecyclerView;
    private RecyclerView topRecyclerView;
    private LinearLayout mainRelative;
    private ViewPager bannerViewPager;
    private ViewPager bannersViewPager;
    private DotsIndicator dotsIndicator;
    private ProgressBar loadingImageView;
    private RelativeLayout notFoundAnimation;
    private TextView seeAllCat;
    private TextView tryAgain;

    // [START Banner]
    private BannerSliderAdapter bannerSliderAdapter;
    private ArrayList<Banner> bannerArrayList;
    // [END Banner]

    // [START Advt Banner]
    private AdvtBannerSliderAdapter advtBannerSliderAdapter;
    private ArrayList<AdvtBanner> advtBanners;
    // [END Advt Banner]

    // [START Category]
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categoryArrayList;
    private GridLayoutManager categoryGridLayoutManager;
    // [END Category]

    // [START Product]
    private FeaturedProductAdapter featuredProductAdapter;
    private ArrayList<Product> featuredProductArrayList;
    private LinearLayoutManager productLinearLayoutManager;
    // [END Product]

    // [START Top Product]
    private TopProductAdapter topProductAdapter;
    private ArrayList<Product> topProductArrayList;
    private LinearLayoutManager topLinearLayoutManager;
    // [END Top Product]

    private BroadcastReceiver receiver = null;
    int i = 0;
    private TextView badge;
    private ImageView cartImg;

    private String about_us;
    private String contact_us;
    private String privacy_policy;
    private String terms;
    private String developers;

    private ImageView adImage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);


//        binding.mainToolbarText.setText("Shree Balaji Misthan Bhandar");
//        binding.mainRelative.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shree Balaji");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        categoryRecyclerView = (RecyclerView) findViewById(R.id.categoryRecyclerView);
        featuredRecyclerView = (RecyclerView) findViewById(R.id.featuredRecyclerView);
        topRecyclerView = (RecyclerView) findViewById(R.id.topRecyclerView);
        mainRelative = (LinearLayout) findViewById(R.id.mainRelative);
        bannerViewPager = (ViewPager) findViewById(R.id.bannerViewPager);
        bannersViewPager = (ViewPager) findViewById(R.id.bannersViewPager);
        dotsIndicator = (DotsIndicator) findViewById(R.id.dotsIndicator);
        dotsIndicator = (DotsIndicator) findViewById(R.id.dotsIndicator);
        loadingImageView = (ProgressBar) findViewById(R.id.loadingImageView);
        notFoundAnimation = (RelativeLayout) findViewById(R.id.notFoundAnimation);
        seeAllCat = (TextView) findViewById(R.id.seeAllCat);
        tryAgain = (TextView) findViewById(R.id.tryAgain);
        adImage = (ImageView) findViewById(R.id.adImage);

        adImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllCategoryActivity.class);
                startActivity(intent);
            }
        });

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(MainActivity.this);
        bannerArrayList = new ArrayList<>();
        advtBanners = new ArrayList<>();
        categoryArrayList = new ArrayList<>();
        featuredProductArrayList = new ArrayList<>();
        topProductArrayList = new ArrayList<>();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        getBanners();
                        getAdvtBanners();
                        getCategory();
                        getProducts();
                        getTopProducts();
                        getAbouts();

                        boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);

                        if (loggedIn) {
                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
                        }
                    }
                    else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        getBanners();
                        getAdvtBanners();
                        getCategory();
                        getProducts();
                        getTopProducts();
                        getAbouts();

                        boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);

                        if (loggedIn) {
                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
                        }
                    }
                }
                else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // [Start Category]
        categoryGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(categoryGridLayoutManager);
        // [End Category]

        // [Start Product]
        productLinearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        featuredRecyclerView.setHasFixedSize(true);
        featuredRecyclerView.setLayoutManager(productLinearLayoutManager);
        // [End Product]

        // [Start Product]
        topLinearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        topRecyclerView.setHasFixedSize(true);
        topRecyclerView.setLayoutManager(topLinearLayoutManager);
        // [End Product]

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    getBanners();
                    getAdvtBanners();
                    getCategory();
                    getProducts();
                    getTopProducts();
                    getAbouts();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

//        binding.myAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);
//
//                if (!loggedIn) {
//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
//                else {
//                    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
//                    startActivity(intent);
//
//                    String mobile = applicationPreference.getData("mobile");
//                    getCartData(mobile);
//                }
//            }
//        });

        seeAllCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void getBanners() {
        loadingImageView.setVisibility(View.VISIBLE);
        notFoundAnimation.setVisibility(View.GONE);
        mainRelative.setVisibility(View.GONE);

        Call<ResponseBody> call = clientAPI.getBanners();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        loadingImageView.setVisibility(View.GONE);
                        mainRelative.setVisibility(View.VISIBLE);
                        notFoundAnimation.setVisibility(View.GONE);
                        bannerArrayList.clear();
                        String results = response.body().string();
                        Log.d(TAG, "BannerResults: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        Log.d(TAG, "BannerJsonResults: " + results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                int id = data.getInt("id");
                                String title = data.getString("title");
                                String link = data.getString("link");
                                String image = data.getString("image");
                                String status = data.getString("status");

                                bannerArrayList.add(new Banner(id,
                                        title,
                                        link,
                                        image,
                                        status));
                            }

                            bannerSliderAdapter = new BannerSliderAdapter(MainActivity.this, MainActivity.this, bannerArrayList);
                            bannerViewPager.setAdapter(bannerSliderAdapter);
                            dotsIndicator.setViewPager(bannerViewPager);

                            // [START Banner Auto Slide]
                            NUM_PAGES_BANNERS = bannerArrayList.size();
                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable runnable = () -> {
                                if (CURRENT_BANNER_PAGE == NUM_PAGES_BANNERS) {
                                    CURRENT_BANNER_PAGE = 0;
                                }
                                bannerViewPager.setCurrentItem(CURRENT_BANNER_PAGE++, true);
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(runnable);
                                }
                            }, 5000, 8000);

                            bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    CURRENT_BANNER_PAGE = position;
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                            // [END Banner Auto Slide]
                        }
                        else {

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

    private void getAdvtBanners() {
        Call<ResponseBody> call = clientAPI.advtbanners();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        advtBanners.clear();
                        String results = response.body().string();
                        Log.d(TAG, "BannerResults: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        Log.d(TAG, "BannerJsonResults: " + results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                int id = data.getInt("id");
                                String title = data.getString("title");
                                String link = data.getString("link");
                                String image = data.getString("image");
                                String status = data.getString("status");

                                advtBanners.add(new AdvtBanner(id,
                                        title,
                                        link,
                                        image,
                                        status));
                            }

                            advtBannerSliderAdapter = new AdvtBannerSliderAdapter(MainActivity.this, MainActivity.this, advtBanners);
                            bannersViewPager.setAdapter(advtBannerSliderAdapter);

                            // [START Banner Auto Slide]
                            NUM_PAGES_ADVTBANNERS = advtBanners.size();
                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable runnable = () -> {
                                if (CURRENT_ADVTBANNER_PAGE == NUM_PAGES_ADVTBANNERS) {
                                    CURRENT_ADVTBANNER_PAGE = 0;
                                }
//                                binding.bannerViewPager.setCurrentItem(CURRENT_BANNER_PAGE++, true);
                                bannersViewPager.setCurrentItem(CURRENT_ADVTBANNER_PAGE++, true);
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(runnable);
                                }
                            }, 5000, 8000);

                            bannersViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    CURRENT_ADVTBANNER_PAGE = position;
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                            // [END Banner Auto Slide]
                        }
                        else {

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void getCategory() {
        Call<ResponseBody> call = clientAPI.getCategoriesMain();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        categoryArrayList.clear();
                        String results = response.body().string();
                        Log.d(TAG, "CategoryResultsMain: " + results);
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

                            categoryAdapter = new CategoryAdapter(MainActivity.this, categoryArrayList);
                            categoryAdapter.notifyDataSetChanged();
                            categoryRecyclerView.setAdapter(categoryAdapter);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void getProducts() {
        Call<ResponseBody> call = clientAPI.getProducts();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        featuredProductArrayList.clear();
                        String results = response.body().string();
                        Log.d(TAG, "ProductsResults: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                Product product = new Product(data);
                                featuredProductArrayList.add(product);
                            }
                            if (featuredProductArrayList.size() > 0) {
                                featuredProductAdapter = new FeaturedProductAdapter(MainActivity.this, MainActivity.this, featuredProductArrayList);
                                featuredProductAdapter.notifyDataSetChanged();
                                featuredRecyclerView.setAdapter(featuredProductAdapter);
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getTopProducts() {
        Call<ResponseBody> call = clientAPI.getTopProducts();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        topProductArrayList.clear();
                        String results = response.body().string();
                        Log.d(TAG, "ProductsResults: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                Product product = new Product(data);
                                topProductArrayList.add(product);
                            }
                            if (topProductArrayList.size() > 0) {
                                topProductAdapter = new TopProductAdapter(MainActivity.this, MainActivity.this, topProductArrayList);
                                topProductAdapter.notifyDataSetChanged();
                                topRecyclerView.setAdapter(topProductAdapter);
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getBannerId(int position) {
        Banner banner = bannerArrayList.get(position);

        int id = Integer.parseInt(banner.getLink());
//        Toast.makeText(this, banner.getLink(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void getAdvtBannerId(int position) {
        AdvtBanner banner = advtBanners.get(position);

        int id = Integer.parseInt(banner.getLink());
//        Toast.makeText(this, banner.getLink(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void getCartData(String mobile) {
        Call<ResponseBody> call = clientAPI.getCart(mobile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String results = response.body().string();
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            String cartItems = String.valueOf(jsonObject.getInt("cartItems"));

                            if (jsonObject.getInt("cartItems") > 0) {
//                                binding.badge.setVisibility(View.VISIBLE);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        binding.badge.setText(cartItems);
                                        badge.setText(cartItems);
                                        badge.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                            else {
                                badge.setVisibility(View.GONE);
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getAbouts() {
        Call<ResponseBody> call = clientAPI.getAbouts();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String results = response.body().string();
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONObject resultJson = jsonObject.getJSONObject("results");

                            about_us = resultJson.getString("about_us");
                            contact_us = resultJson.getString("contact_us");
                            privacy_policy = resultJson.getString("privacy_policy");
                            terms = resultJson.getString("terms");
                            developers = resultJson.getString("developers");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);
        switch (item.getItemId()) {
            case R.id.account:
                if (!loggedIn) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.order:
                if (!loggedIn) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, MyOrdersActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.addressBook:
                if (!loggedIn) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent addressIntent = new Intent(MainActivity.this, AddressActivity.class);
                    startActivity(addressIntent);
                }
                break;

            case R.id.about:
                Intent aboutUs = new Intent(Intent.ACTION_VIEW, Uri.parse(about_us));
                startActivity(aboutUs);
                break;

            case R.id.contact:
                Intent contactUs = new Intent(Intent.ACTION_VIEW, Uri.parse(contact_us));
                startActivity(contactUs);
                break;

            case R.id.terms:
                Intent termsCon = new Intent(Intent.ACTION_VIEW, Uri.parse(terms));
                startActivity(termsCon);
                break;

            case R.id.privacy:
                Intent policy = new Intent(Intent.ACTION_VIEW, Uri.parse(privacy_policy));
                startActivity(policy);
                break;

            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Shree Balaji");
                //https://play.google.com/store/apps/details?id=wheel.top.best.online.money.make.techyunk.com.earningwheel
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=aka.studios.shribalaji");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        View view = menu.findItem(R.id.mainCart).getActionView();
        badge = (TextView) view.findViewById(R.id.badge);
        cartImg = (ImageView) view.findViewById(R.id.cart_icon);

        cartImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        return true;
    }

    public void addCart(int position) {
        Product product = featuredProductArrayList.get(position);

        String mobile = applicationPreference.getData("mobile");
        getCartData(mobile);

        boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);
        if (!loggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            Call<ResponseBody> call = clientAPI.addToCart(product.getProduct_id(), product.getProduct_name(),
                    product.getSku(), product.getSize(), product.getPrice(), 1, mobile);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        try {
//                            addToCartProgress.setVisibility(View.GONE);
                            String results = response.body().string();
//                            addToCart.setText("Go To Cart");
//                            addToCart.setBackgroundColor(Color.parseColor("#4CAF50"));
                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
                            Toast.makeText(MainActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public void addTopCart(int position) {
        Product product = topProductArrayList.get(position);

        String mobile = applicationPreference.getData("mobile");
        getCartData(mobile);

        boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);
        if (!loggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            Call<ResponseBody> call = clientAPI.addToCart(product.getProduct_id(), product.getProduct_name(),
                    product.getSku(), product.getSize(), product.getPrice(), 1, mobile);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        try {
//                            addToCartProgress.setVisibility(View.GONE);
                            String results = response.body().string();
//                            addToCart.setText("Go To Cart");
//                            addToCart.setBackgroundColor(Color.parseColor("#4CAF50"));
                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
                            Toast.makeText(MainActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public void updateItem(int position, int quantity) {
        Product products = featuredProductArrayList.get(position);

        String mobile = applicationPreference.getData("mobile");
        getCartData(mobile);

//        updateProgressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> incCall = clientAPI.updateCartItem(mobile, products.getProduct_id(), products.getSize(), quantity);
        incCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
//                        updateProgressBar.setVisibility(View.GONE);
                        String results = response.body().string();
                        JSONObject incJson = new JSONObject(results);
                        if (!incJson.getBoolean("error")) {
                            JSONObject data = incJson.getJSONObject("results");

                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
//                            getProducts();
                        }
                        else {
                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                updateProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void updateTopItem(int position, int quantity) {
        Product products = topProductArrayList.get(position);

        String mobile = applicationPreference.getData("mobile");
        getCartData(mobile);

//        updateProgressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> incCall = clientAPI.updateCartItem(mobile, products.getProduct_id(), products.getSize(), quantity);
        incCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
//                        updateProgressBar.setVisibility(View.GONE);
                        String results = response.body().string();
                        JSONObject incJson = new JSONObject(results);
                        if (!incJson.getBoolean("error")) {
                            JSONObject data = incJson.getJSONObject("results");

                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
//                            getProducts();
                        }
                        else {
                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                updateProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
