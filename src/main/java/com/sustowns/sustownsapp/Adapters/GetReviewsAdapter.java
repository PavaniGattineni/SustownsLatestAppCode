package com.sustowns.sustownsapp.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sustowns.sustownsapp.R;
import com.sustowns.sustownsapp.Models.GetReviewsModel;

import java.util.ArrayList;

public class GetReviewsAdapter extends RecyclerView.Adapter<GetReviewsAdapter.ViewHolder> {
    Context context;
    Integer[] images;
    LayoutInflater inflate;
    ArrayList<GetReviewsModel> getReviewsModels;
    String pro_id,image;

    public GetReviewsAdapter(Context context, ArrayList<GetReviewsModel> getReviewsModels) {
        this.context=context;
        this.getReviewsModels = getReviewsModels;
        this.inflate = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.get_reviews_item, viewGroup, false);
        //  product_sale_activity.onItemClick(i);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.review_cardview.setVisibility(View.VISIBLE);
        viewHolder.category_item_card.setVisibility(View.GONE);
        if(getReviewsModels.get(position) != null) {
            viewHolder.date.setText(getReviewsModels.get(position).getOndate());
            viewHolder.review.setText(getReviewsModels.get(position).getReview());
            viewHolder.name_review.setText("Review By : "+getReviewsModels.get(position).getFullname()+"("+getReviewsModels.get(position).getCity()
                    +","+getReviewsModels.get(position).getCountry()+")");
            viewHolder.ratingBar.setRating(Float.parseFloat(getReviewsModels.get(position).getRatting()));
          /*  viewHolder.reviews_date.setText("Date : "+getReviewsModels.get(position).getOndate());
            viewHolder.review_text.setText(getReviewsModels.get(position).getReview());
            viewHolder.review_by.setText("Review By :  " + getReviewsModels.get(position).getFullname()+"("+getReviewsModels.get(position).getCity()+
                                            ","+getReviewsModels.get(position).getCountry());
           // viewHolder.ratingBar.setText("Date and Time :  " + newsModels.get(position).getPostdate());*/

        }else{
            viewHolder.date.setText("");
            viewHolder.review.setText("");
            viewHolder.name_review.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return getReviewsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        TextView date,review,name_review;
        CardView review_cardview,category_item_card;

        public ViewHolder(View view) {
            super(view);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            date = (TextView) view.findViewById(R.id.reviews_date);
            review = (TextView) view.findViewById(R.id.review_text);
            name_review = (TextView) view.findViewById(R.id.review_by);
            review_cardview = (CardView) view.findViewById(R.id.review_cardview);
            category_item_card = (CardView) view.findViewById(R.id.category_item_card);
        }
    }
}
