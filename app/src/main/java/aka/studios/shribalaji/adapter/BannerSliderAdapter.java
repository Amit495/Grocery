package aka.studios.shribalaji.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.activity.MainActivity;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.model.Banner;
import com.squareup.picasso.Picasso;

public class BannerSliderAdapter extends PagerAdapter {

    Context context;
    MainActivity activity;
    ArrayList<Banner> bannerArrayList;


    public BannerSliderAdapter(Context context, MainActivity activity, ArrayList<Banner> bannerArrayList) {
        this.context = context;
        this.activity = activity;
        this.bannerArrayList = bannerArrayList;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.banner_slider, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.bannerImageView);

        Picasso.get().load(Common.BASE_URL + bannerArrayList.get(position).getImage()).into(imageView);
        view.addView(imageLayout, 0);

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getBannerId(position);
            }
        });

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
