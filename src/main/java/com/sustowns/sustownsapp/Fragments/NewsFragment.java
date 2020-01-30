package com.sustowns.sustownsapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sustowns.sustownsapp.Activities.NewsActivity;
import com.sustowns.sustownsapp.Adapters.NewsAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.NewsModel;
import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsFragment extends Fragment {
    public static RecyclerView recycler_news;
    NewsAdapter newsAdapter;
    String[] text = {"Heading1","Heading2","Heading3","Heading4"};
    String[] date = {"Date & Time :14th june 2019 , 11:11:11","Date & Time :14th june 2019 , 11:11:11","Date & Time :14th june 2019 , 11:11:11","Date & Time :14th june 2019 , 11:11:11","Date & Time :14th june 2019 , 11:11:11"};
    String[] content = {"According to the Indian Meteorological Department (IMD), the “very severe” cyclonic storm Vayu will not make landfall in Gujarat as it has moved towards the sea. However, the cyclone which currently is Category 2 storm may weaken into a Category 1 storm, as per Skymet",
            " According to the prediction of India Meteorological Department (IMD), the cyclonic storm is likely to strike the coast of Gujarat as severe cyclonic storm with a wind speed of 110 to120 kmph tomorrow morning.(13/06/2019)",
    " According to the prediction of India Meteorological Department (IMD), the cyclonic storm is likely to strike the coast of Gujarat as severe cyclonic storm with a wind speed of 110 to120 kmph tomorrow morning.(13/06/2019)",
    " According to the prediction of India Meteorological Department (IMD), the cyclonic storm is likely to strike the coast of Gujarat as severe cyclonic storm with a wind speed of 110 to120 kmph tomorrow morning.(13/06/2019)"};
    ProgressDialog progressDialog;
    ArrayList<NewsModel> newsModels;
    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rowView = inflater.inflate(R.layout.news_fragment, container, false);

        recycler_news = (RecyclerView) rowView.findViewById(R.id.recycler_news);
        LinearLayoutManager lManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recycler_news.setLayoutManager(lManager);

        getNewsList();
        return rowView;
    }


    public void progressdialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void getNewsList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getNews();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        if(progressDialog.isShowing())
                        progressDialog.dismiss();
                        Log.d("Success Call", ">>>>" + call);
                        Log.d("Success Call ", ">>>>" + response.body().toString());

                        System.out.println("----------------------------------------------------");
                        Log.d("Call request", call.request().toString());
                        Log.d("Call request header", call.request().headers().toString());
                        Log.d("Response raw header", response.headers().toString());
                        Log.d("Response raw", String.valueOf(response.raw().body()));
                        Log.d("Response code", String.valueOf(response.code()));
                        System.out.println("----------------------------------------------------");


                        if (response.body().toString() != null) {

                            if (response != null) {
                                String searchResponse = response.body().toString();
                                Log.d("Reg", "Response  >>" + searchResponse.toString());

                                if (searchResponse != null) {
                                    JSONObject root = null;
                                    try {
                                        root = new JSONObject(searchResponse);
                                        String message;
                                        Integer success;
                                        success = root.getInt("success");
                                        //   message = root.getString("message");
                                        if (success == 1) {
                                            if(progressDialog.isShowing())
                                                progressDialog.dismiss();
                                                JSONArray jsonArray = root.getJSONArray("newsresults");
                                            newsModels = new ArrayList<NewsModel>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String categoryname = jsonObject.getString("categoryname");
                                                String newsname = jsonObject.getString("newsname");
                                                String description = jsonObject.getString("description");
                                                String postdate = jsonObject.getString("postdate");
                                                String imagepath = jsonObject.getString("imagepath");

                                                NewsModel newsModel = new NewsModel();
                                                newsModel.setCategoryname(categoryname);
                                                newsModel.setNewsname(newsname);
                                                newsModel.setDescription(description);
                                                newsModel.setPostdate(postdate);
                                                newsModel.setImagepath(imagepath);
                                                newsModels.add(newsModel);
                                                progressDialog.dismiss();

                                                // preferenceUtils.saveString(PreferenceUtils.USER_ID,userid);
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(newsModels != null) {
                                        progressDialog.dismiss();
                                        newsAdapter = new NewsAdapter((NewsActivity) getContext(), newsModels);
                                        recycler_news.setAdapter(newsAdapter);
                                        newsAdapter.notifyDataSetChanged();
                                    }else{
                                        progressDialog.dismiss();
                                        // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
                //  Toast.makeText(ProductsActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
