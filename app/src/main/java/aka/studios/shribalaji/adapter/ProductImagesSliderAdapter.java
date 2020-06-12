package aka.studios.shribalaji.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductImagesSliderAdapter extends PagerAdapter {

    Context context;
    ArrayList<Image> imageArrayList;

    public ProductImagesSliderAdapter(Context context, ArrayList<Image> imageArrayList) {
        this.context = context;
        this.imageArrayList = imageArrayList;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.product_image_slider, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.productDetailImageView);

        Picasso.get().load(Common.BASE_URL + imageArrayList.get(position).getImage()).into(imageView);
        view.addView(imageLayout, 0);

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
