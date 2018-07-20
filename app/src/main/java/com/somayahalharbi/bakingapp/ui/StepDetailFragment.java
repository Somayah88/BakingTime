package com.somayahalharbi.bakingapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class StepDetailFragment extends Fragment implements View.OnClickListener {
    private static MediaSessionCompat mMediaSession;
    private final String STEPS_INDEX = "step_index";
    private final String STEPS_LIST = "steps_list";
    @BindView(R.id.video_player)
    SimpleExoPlayerView mVideoPlayer;
    @BindView(R.id.no_video_tv)
    TextView noVideoTv;
    @BindView(R.id.description_tv)
    TextView descriptionTv;
    @BindView(R.id.next_step)
    ImageView nextButton;
    @BindView(R.id.prev_step)
    ImageView prevButton;
    private ArrayList<Step> stepList = new ArrayList<>();
    private int currentStepIndex;
    private SimpleExoPlayer mExoVideoPlayer;
    private Unbinder viewUnbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        viewUnbinder = ButterKnife.bind(this, rootView);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey(STEPS_INDEX) && bundle.containsKey(STEPS_LIST)) {
                stepList = bundle.getParcelableArrayList(STEPS_LIST);
                currentStepIndex = bundle.getInt(STEPS_INDEX);

            }
        }

        if (stepList != null && stepList.size() > 0)
            displayData();


        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

        return rootView;


    }

    private void displayData() {
        releaseVideoPlayer();
        playVideo();
        descriptionTv.setText(stepList.get(currentStepIndex).getDescription());

    }

    private void playVideo() {
        if (mExoVideoPlayer == null) {
            String videoUrl = stepList.get(currentStepIndex).getVideoURL();

            if (videoUrl != null) {
                mVideoPlayer.setVisibility(View.VISIBLE);
                noVideoTv.setVisibility(View.GONE);
                if (mVideoPlayer == null) {

                    mExoVideoPlayer = ExoPlayerFactory.newSimpleInstance(
                            new DefaultRenderersFactory(getActivity()),
                            new DefaultTrackSelector(), new DefaultLoadControl());
                    mVideoPlayer.setPlayer(mExoVideoPlayer);
                    mExoVideoPlayer.setPlayWhenReady(true);
                    MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl));
                    mExoVideoPlayer.prepare(mediaSource, true, false);
                }

            } else {
                mVideoPlayer.setVisibility(View.GONE);
                noVideoTv.setVisibility(View.VISIBLE);
            }
        }

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("Baking_app"),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.prev_step) {
            if (stepList != null) {
                if (currentStepIndex == 0)
                    return;
                currentStepIndex--;
                if (currentStepIndex <= 0) {
                    prevButton.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                    prevButton.setVisibility(View.VISIBLE);
                }
            }
        } else if (id == R.id.next_step) {
            if (stepList != null) {
                if (currentStepIndex >= stepList.size() - 1)
                    return;
                currentStepIndex++;
                if (currentStepIndex >= stepList.size() - 1) {
                    nextButton.setVisibility(View.INVISIBLE);
                    prevButton.setVisibility(View.VISIBLE);
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                    prevButton.setVisibility(View.VISIBLE);
                }

            }


        }
        displayData();

    }

    private void releaseVideoPlayer() {
        if (mExoVideoPlayer != null)
            mExoVideoPlayer.release();
        mExoVideoPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseVideoPlayer();
        viewUnbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releaseVideoPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releaseVideoPlayer();
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {
        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
