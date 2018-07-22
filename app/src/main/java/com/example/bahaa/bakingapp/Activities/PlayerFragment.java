package com.example.bahaa.bakingapp.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bahaa.bakingapp.Models.StepsModel;
import com.example.bahaa.bakingapp.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


public class PlayerFragment extends Fragment {

    private StepsModel stepsModel;
    private String VIDEO_URL, thumbStr;
    private TextView description;
    private ImageView thumbnail;
    private boolean hasVideo;
    private Bitmap vThumbnail;
    private Uri bitmapUri;

    //Exo Player portion
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private long playerPosition;
    private String PLAYER_POS_CONST = "playerPos";


    public PlayerFragment() {
        // Required empty public constructor
    }


    public static PlayerFragment newInstance() {

        return new PlayerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_player, container, false);

        stepsModel = (StepsModel) getArguments().getSerializable("stepsObj");
        Log.i("SeriaObj", stepsModel.getDescription());
        VIDEO_URL = stepsModel.getVideoURL();

        description = (TextView) v.findViewById(R.id.description);

        if (description != null) {
            description.setText(stepsModel.getDescription());
        }

        thumbnail = (ImageView) v.findViewById(R.id.thumbnail);

        thumbStr = stepsModel.getThumbnailURL();


        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this.getActivity(), Util.getUserAgent(getActivity(), "BakingApp"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();
        simpleExoPlayerView = (SimpleExoPlayerView) v.findViewById(R.id.video_player);

        if(savedInstanceState != null){
            playerPosition = savedInstanceState.getLong(PLAYER_POS_CONST);
        }

        new RetreiveThumbnailTask().execute();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasVideo) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasVideo) {
            initializePlayer();
            player.seekTo(playerPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (hasVideo) {
            playerPosition = player.getCurrentPosition();
            releasePlayer();
        }
    }
    

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(PLAYER_POS_CONST, playerPosition);
    }

    private void initializePlayer() {

        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);


        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(VIDEO_URL),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    private Bitmap createThumbnail(String videoURL){

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoURL, new HashMap<String, String>());
        return mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "thumbnail", null);
        return Uri.parse(path);
    }



    class RetreiveThumbnailTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... params) {


            if (VIDEO_URL.isEmpty()) {
                hasVideo = false;
                if (!thumbStr.isEmpty()) {
                    simpleExoPlayerView.setVisibility(View.INVISIBLE);

                    vThumbnail = createThumbnail(thumbStr);

                    bitmapUri = getImageUri(getContext(), vThumbnail);

                } else {
                    simpleExoPlayerView.setVisibility(View.INVISIBLE);
                    thumbnail.setImageResource(R.drawable.ic_photo);

                }
            } else {
                hasVideo = true;
            }

            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {

            Picasso.with(getActivity())

                    .load(bitmapUri)
                    .fit().centerInside()
                    .placeholder(R.drawable.ic_download)
                    .error(R.drawable.ic_photo)
                    .into(thumbnail);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }



}
