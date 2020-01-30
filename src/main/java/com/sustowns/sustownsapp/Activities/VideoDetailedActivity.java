package com.sustowns.sustownsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.sustowns.sustownsapp.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoDetailedActivity extends YouTubeBaseActivity {
    VideoView videoView;
    TextView text_Contetnt;
    ImageView backPress;
    String content_detailed, youtube_link, video;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    private String API = "AIzaSyCkMFa-P2yzNKMX8fGds-qNH_fwzrwAMYE";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    PreferenceUtils preferenceUtils;
    FrameLayout play_video;
    ImageView play_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_detailed);
        preferenceUtils = new PreferenceUtils(VideoDetailedActivity.this);
        play_btn = (ImageView) findViewById(R.id.play_btn);
        //video = preferenceUtils.getStringFromPreference(PreferenceUtils.VIDEO,"");
        play_video = (FrameLayout) findViewById(R.id.play_video);
        Intent intent = getIntent();
        content_detailed = intent.getStringExtra("content");
        youtube_link = intent.getStringExtra("videoImagePath");
        video = intent.getStringExtra("video");
        backPress = (ImageView) findViewById(R.id.backarrow);
        text_Contetnt = (TextView) findViewById(R.id.text_content);
        text_Contetnt.setText(content_detailed);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        onInitializedListener = new YouTubePlayer.OnInitializedListener(){
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                youTubePlayer.loadVideo(video);
                String youtubeID = video.replace("https://www.youtube.com/embed/", "");
                youTubePlayer.loadVideo(youtubeID);

                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_btn.setVisibility(View.GONE);
                youTubePlayerView.initialize(API,onInitializedListener);
            }
        });
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void Video(){
        onInitializedListener = new YouTubePlayer.OnInitializedListener(){
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

//                youTubePlayer.loadVideo(video);
                String youtubeID = video.replace("https://www.youtube.com/embed/", "");
                youTubePlayer.loadVideo(youtubeID);

                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

    }



}
