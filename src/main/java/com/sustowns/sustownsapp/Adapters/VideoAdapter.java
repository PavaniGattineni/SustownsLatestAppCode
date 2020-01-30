package com.sustowns.sustownsapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sustowns.sustownsapp.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.sustowns.sustownsapp.Activities.PreferenceUtils;
import com.sustowns.sustownsapp.Activities.VideoDetailedActivity;
import com.sustowns.sustownsapp.Models.VideoModel;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    PreferenceUtils preferenceUtils;
    ArrayList<String> arrayList ;
    Integer[] images;
    ArrayList<VideoModel> videoModels;
    ProgressDialog progressDialog;
    int index = 0;
    String video;
    private String API = "AIzaSyCkMFa-P2yzNKMX8fGds-qNH_fwzrwAMYE";
    YouTubePlayer.OnInitializedListener onInitializedListener;

    public VideoAdapter(Context context, ArrayList<VideoModel> videoModels) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.videoModels = videoModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.videos_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if(videoModels.get(position) != null){
            viewHolder.text_image_title.setText(videoModels.get(position).getNewsname());
            Glide.with(context)
                    .load(videoModels.get(position).getVideoimgpath())
                    .into(viewHolder.imageView);
        }
        viewHolder.frame_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  ((VideoDetailedActivity) context).Video();
             /*      viewHolder.play_btn.setVisibility(View.GONE);
                viewHolder.youtube_player.initialize(API,onInitializedListener);*/
                Intent i = new Intent(context, VideoDetailedActivity.class);
                i.putExtra("video",videoModels.get(position).getVideopath());
                i.putExtra("content",videoModels.get(position).getDescription());
                i.putExtra("videoImagePath",videoModels.get(position).getVideoimgpath());
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return videoModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_image_title;
        ImageView imageView,play_btn;
        FrameLayout frame_layout;
        YouTubePlayerView youtube_player;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.photos);
            text_image_title = (TextView) view.findViewById(R.id.text_image_title);
            frame_layout = (FrameLayout) view.findViewById(R.id.frame_layout);
            youtube_player = (YouTubePlayerView) view.findViewById(R.id.youtube_player);
            play_btn = (ImageView) view.findViewById(R.id.play_btn);
        }
    }
}


















      /*  final MediaController mediaController= new MediaController(context);
        mediaController.setAnchorView(viewHolder.videoView);

        //  Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        //Starting VideView By Setting MediaController and URI
        viewHolder.videoView.setMediaController(mediaController);
        viewHolder.videoView.setVideoURI(Uri.parse(arrayList.get(index)));
        viewHolder.videoView.requestFocus();
        //  videoView.start();

        viewHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        viewHolder.videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(viewHolder.videoView);

                    }
                });
            }
        });

        viewHolder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(context, "Video over", Toast.LENGTH_SHORT).show();
                if (index++ == arrayList.size()) {
                    index = 0;
                    mp.release();
                    Toast.makeText(context, "Video over", Toast.LENGTH_SHORT).show();
                } else {
                    viewHolder.videoView.setVideoURI(Uri.parse(arrayList.get(index)));
                    viewHolder.videoView.start();
                }


            }
        });

        viewHolder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("API123", "What " + what + " extra " + extra);
                return false;
            }
        });*/