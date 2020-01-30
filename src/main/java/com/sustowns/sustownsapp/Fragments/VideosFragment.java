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
import android.widget.VideoView;

import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Adapters.VideoAdapter;
import com.sustowns.sustownsapp.Api.DZ_URL;
import com.sustowns.sustownsapp.Api.UserApi;
import com.sustowns.sustownsapp.Models.VideoModel;
import com.sustowns.sustownsapp.R;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideosFragment extends Fragment {
    RecyclerView recycler_news;
    VideoView videoView;
    ArrayList<VideoModel> videoModels;
    ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
    int index = 0;
    Integer[] images = {R.drawable.simg3,R.drawable.simg9};
    RecyclerView recycler_videos;
    VideoAdapter videoAdapter;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;

    public VideosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rowView = inflater.inflate(R.layout.videos_fragment, container, false);
        preferenceUtils = new PreferenceUtils(getActivity());

        recycler_videos = (RecyclerView) rowView.findViewById(R.id.recycler_videos);
        LinearLayoutManager lManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recycler_videos.setLayoutManager(lManager);

   /*     videoView =(VideoView)rowView.findViewById(R.id.videoView);
        //Set MediaController  to enable play, pause, forward, etc options.
        final MediaController mediaController= new MediaController(getContext());
        mediaController.setAnchorView(videoView);

      //  Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(arrayList.get(index)));
        videoView.requestFocus();
      //  videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);

                    }
                });
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getContext(), "Video over", Toast.LENGTH_SHORT).show();
                if (index++ == arrayList.size()) {
                    index = 0;
                    mp.release();
                    Toast.makeText(getContext(), "Video over", Toast.LENGTH_SHORT).show();
                } else {
                    videoView.setVideoURI(Uri.parse(arrayList.get(index)));
                    videoView.start();
                }


            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("API123", "What " + what + " extra " + extra);
                return false;
            }
        });*/
         getVideosList();
        return rowView;

    }

    public void progressdialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void getVideosList() {
        progressdialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DZ_URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi service = retrofit.create(UserApi.class);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.getVideos();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
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
                                            JSONArray jsonArray = root.getJSONArray("newsvideoresults");
                                            videoModels = new ArrayList<VideoModel>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String newsname = jsonObject.getString("newsname");
                                                String description = jsonObject.getString("description");
                                                String postdate = jsonObject.getString("postdate");
                                                String type = jsonObject.getString("type");
                                                String videopath = jsonObject.getString("videopath");
                                                String videoimgpath = jsonObject.getString("videoimgpath");
                                                preferenceUtils.saveString(PreferenceUtils.VIDEO,videopath);

                                                VideoModel videoModel = new VideoModel();
                                                videoModel.setNewsname(newsname);
                                                videoModel.setDescription(description);
                                                videoModel.setPostdate(postdate);
                                                videoModel.setType(type);
                                                videoModel.setVideopath(videopath);
                                                videoModel.setVideoimgpath(videoimgpath);
                                                videoModels.add(videoModel);
                                                progressDialog.dismiss();

                                                // preferenceUtils.saveString(PreferenceUtils.USER_ID,userid);
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(videoModels != null) {
                                        progressDialog.dismiss();
                                        videoAdapter = new VideoAdapter(getActivity(),videoModels);
                                        recycler_videos.setAdapter(videoAdapter);
                                        videoAdapter.notifyDataSetChanged();
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
