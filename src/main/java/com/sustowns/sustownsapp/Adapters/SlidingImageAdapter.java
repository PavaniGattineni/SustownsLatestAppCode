package com.sustowns.sustownsapp.Adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Models.ImageSliderModel;
import com.sustowns.sustownsapp.R;

import java.util.ArrayList;

public class SlidingImageAdapter extends PagerAdapter {


    private ArrayList<ImageSliderModel> imageSliderModels;
    private LayoutInflater inflater;
    private Context context;
 /*   public SlidingImageAdapter(Context context, ArrayList<ImageModel> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }*/
    public SlidingImageAdapter(Context context, ArrayList<ImageSliderModel> imageSliderModels) {
        this.context = context;
        this.imageSliderModels = imageSliderModels;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageSliderModels.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);

        // imageView.setImageResource(imageModelArrayList.get(position).getImage_drawable());
        if (imageSliderModels.get(position) != null) {
            Picasso.get()
                    .load(imageSliderModels.get(position).getImage())
                    //  .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.no_image_available)
                    .into(imageView);
/*
            Glide.with(context)
                    .load(imageSliderModels.get(position).getImage())
                    //.placeholder(R.drawable.image_slider)
                    .into(imageView);
*/
        }else{
            imageView.setImageResource(R.drawable.no_image_available);
            //  imageView.setImageResource(R.drawable.image_slider);
        }
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
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