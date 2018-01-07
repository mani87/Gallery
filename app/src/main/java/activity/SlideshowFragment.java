package activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sunshine.mani.gallery.R;
import com.example.sunshine.mani.gallery.model.Image;

import java.util.ArrayList;


/**
 * Created by mani on 1/7/18.
 */

public class SlideshowFragment extends DialogFragment {
    private String TAG = SlideshowFragment.class.getSimpleName();
    private ArrayList<Image> images;
    private ViewPager mViewPager;
    private MyViewPagerAdpater myViewPagerAdpater;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;

    static SlideshowFragment newInstance() {
        SlideshowFragment f = new SlideshowFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDate = (TextView) v.findViewById(R.id.date);

        images = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position :" + selectedPosition);
        Log.e(TAG, "imagesize :" + images.size());

        myViewPagerAdpater = new MyViewPagerAdpater();

        mViewPager.setAdapter(myViewPagerAdpater);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());

        Image image = images.get(position);
        lblTitle.setText(image.getName());
        lblDate.setText(image.getTimestamp());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }


    public class MyViewPagerAdpater extends PagerAdapter {

        private LayoutInflater mLayoutInflater;

        public MyViewPagerAdpater() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mLayoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView mImageView = (ImageView) view.findViewById(R.id.image_preview);
            Image image = images.get(position);

            Glide.with(getActivity()).load(image.getLarge())
                    .thumbnail(0.5f)
                    .crossFade()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mImageView);

            container.addView(view);
            displayMetaInfo(position);
            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
