package com.garbagebin.youtube_classes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import garbagebin.com.garbagebin.R;


public class VideoActivity extends Activity {

    private int REQ_PLAYER_CODE 	= 1;
    private static String YT_KEY 	= "AIzaSyCM_E3w5pigljed_02t7T6ZlZ2Cr-iW5QU";
    private static String VIDEO_ID 	;	// Your video id here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent in = getIntent();
        VIDEO_ID = in.getStringExtra("videoid");

        Intent videoIntent = YouTubeStandalonePlayer.createVideoIntent(this, YT_KEY, VIDEO_ID, 0, true, false);

        startActivityForResult(videoIntent, REQ_PLAYER_CODE);
        VideoActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PLAYER_CODE && resultCode != RESULT_OK) {
            YouTubeInitializationResult errorReason = YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(this, 0).show();
            } else {
                String errorMessage = String.format("PLAYER ERROR!!", errorReason.toString());
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        VideoActivity.this.finish();
    }

}