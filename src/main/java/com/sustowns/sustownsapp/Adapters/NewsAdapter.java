package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.squareup.picasso.Picasso;
import com.sustowns.sustownsapp.Activities.NewsActivity;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Fragments.NewsFragment;
import com.sustowns.sustownsapp.Models.NewsModel;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    Context context;
    String[] text;
    String[] date;
    String[] content;
    LayoutInflater inflater;
    String user_email,pro_id,user_id,user_role;
    PreferenceUtils preferenceUtils;
    Integer[] images;
    ArrayList<NewsModel> newsModels;
    ProgressDialog progressDialog;
    int mExpandedPosition = -1;

    public NewsAdapter(NewsActivity context, ArrayList<NewsModel> newsModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.newsModels = newsModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        if(newsModels.get(position) != null) {
            Picasso.get()
                    .load(newsModels.get(position).getImagepath())
                   // .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.no_image_available)
                    .into(viewHolder.news_image);
           /* Glide.with(context)
                    .load(newsModels.get(position).getImagepath())
                    .into(viewHolder.news_image);*/
            viewHolder.description_news_full.setVisibility(View.GONE);
            viewHolder.description_news.setVisibility(View.VISIBLE);
            viewHolder.category_news.setText("Category Name : "+newsModels.get(position).getCategoryname());
            viewHolder.title.setText(newsModels.get(position).getNewsname());
            viewHolder.description_news.setText("Description :  " + Html.fromHtml(newsModels.get(position).getDescription()));
            viewHolder.date_news.setText("Date and Time :  " + newsModels.get(position).getPostdate());
/*
            viewHolder.read_more_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   viewHolder.description_news_full.setVisibility(View.VISIBLE);
                   viewHolder.description_news_full.setText("Description : " + newsModels.get(position).getDescription());
                   viewHolder.description_news.setVisibility(View.GONE);
                   viewHolder.read_more_btn.setVisibility(View.GONE);
                }
            });
*/

            final boolean isExpanded = position == mExpandedPosition;
            viewHolder.description_news_full.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            viewHolder.description_news_full.setSelected(true);
            if(isExpanded){
                viewHolder.read_more_btn.setVisibility(View.GONE);
                viewHolder.description_news.setVisibility(View.GONE);
                viewHolder.description_news_full.setText("Description :  " + Html.fromHtml(newsModels.get(position).getDescription()));
            }
            else{
                viewHolder.read_more_btn.setVisibility(View.VISIBLE);
                viewHolder.description_news.setVisibility(View.VISIBLE);

            }
            viewHolder.read_more_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandedPosition = isExpanded ? -1:position;
                    viewHolder.description_news_full.setText("Description :  " + Html.fromHtml(newsModels.get(position).getDescription()));
                    TransitionManager.beginDelayedTransition(NewsFragment.recycler_news);
                    notifyDataSetChanged();
                }
            });
  /*
  if (position == mExpandedPosition) {
                viewHolder.description_news_full.setVisibility(View.VISIBLE);
                viewHolder.read_more_btn.setVisibility(View.GONE);
                viewHolder.description_news.setVisibility(View.GONE);
            } else {
                viewHolder.description_news_full.setVisibility(View.GONE);
                viewHolder.read_more_btn.setVisibility(View.VISIBLE);
                viewHolder.description_news.setVisibility(View.VISIBLE);
                  }
                  */

        }

    }

    @Override
    public int getItemCount() {
        return newsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,description_news,date_news,description_news_full,category_news;
        Button read_more_btn;
        ImageView news_image;
        ScrollView scrollable;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_news);
            description_news = (TextView) view.findViewById(R.id.description_news);
            date_news = (TextView) view.findViewById(R.id.date_news);
            read_more_btn = (Button) view.findViewById(R.id.read_more_btn);
            description_news_full = (TextView) view.findViewById(R.id.description_news_full);
            category_news = (TextView) view.findViewById(R.id.category_news);
            news_image = (ImageView) view.findViewById(R.id.news_image);
            scrollable = (ScrollView) view.findViewById(R.id.childScroll);
            description_news_full.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            description_news_full.setMovementMethod(new ScrollingMovementMethod());


        }
    }
}
