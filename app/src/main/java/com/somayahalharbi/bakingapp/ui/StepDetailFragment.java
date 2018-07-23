package com.somayahalharbi.bakingapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

import static android.support.constraint.Constraints.TAG;

//
public class StepDetailFragment extends Fragment {
    public static final String EXTRA_PLAYER_POSITION = "player_position";
    public static final String EXTRA_PLAYER_READY = "player_ready";
    private static MediaSessionCompat mMediaSession;
    public final String EXTRA_ROTATION = "extra_is_rotated";
    private final String STEPS_INDEX = "step_index";
    private final String STEPS_LIST = "steps_list";
    private final String LANDSCAPE_EXTRA = "landscape_mode";
    @BindView(R.id.rv_details)
    protected RelativeLayout mRelativeLayout;
    @BindView(R.id.line_content)
    protected LinearLayout mLinearLayout;
    @BindView(R.id.video_player)
    SimpleExoPlayerView mVideoPlayer;
    @BindView(R.id.no_video_tv)
    TextView noVideoTv;
    @BindView(R.id.description_tv)
    TextView descriptionTv;
    @BindView(R.id.next_step)
    Button nextButton;
    @BindView(R.id.prev_step)
    Button prevButton;
    @BindView(R.id.thumbnail_ImageView)
    ImageView thumbnailImageView;
    private NavigationButtonListener navigationButtonListener;
    private SimpleExoPlayer mExoPlayer;
    private int currentStepIndex;
    private Unbinder viewUnbinder;
    private ArrayList<Step> stepList = new ArrayList<>();
    private boolean isRotated;
    private long mPlayerPosition = 0;
    private boolean isPlayWhenReady;
    private boolean isLandscape;

    /* @Override
     public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);
         if (savedInstanceState != null) {

                 mPlayerPosition = savedInstanceState.getLong(EXTRA_PLAYER_POSITION);
                 isPlayWhenReady = savedInstanceState.getBoolean(EXTRA_PLAYER_READY);
                 if (mPlayerPosition != C.TIME_UNSET && isPlayWhenReady) {
                     Log.d(TAG, "mPlayerPosition onRestore = " + String.valueOf(mPlayerPosition));
                     mExoPlayer.seekTo(mPlayerPosition);


             }
             Log.d(TAG, "mPlayerPosition after savedInstanceState = " + String.valueOf(mPlayerPosition));
             Log.d(TAG, "isPlayWhenReady after savedInstanceState = " + String.valueOf(isPlayWhenReady));

         }
     }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            stepList = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            currentStepIndex = savedInstanceState.getInt(STEPS_INDEX);
            mPlayerPosition = savedInstanceState.getLong(EXTRA_PLAYER_POSITION);
            //isPlayWhenReady = savedInstanceState.getBoolean(EXTRA_PLAYER_READY);
       /* if (mPlayerPosition != C.TIME_UNSET && isPlayWhenReady) {
            Log.d(TAG, "mPlayerPosition onRestore = " + String.valueOf(mPlayerPosition));
            mExoPlayer.seekTo(mPlayerPosition);


        }*/

        }
    }

//TODO: Fix: the video doesn't fill screen on rotation


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
                //isRotated = bundle.getBoolean(EXTRA_ROTATION);
                //isLandscape = bundle.getBoolean(LANDSCAPE_EXTRA);

            }
        }


        // if (isRotated) {


        if (isRotated() && !stepList.get(currentStepIndex).getVideoURL().isEmpty()) {
            //make viewGroup that contains exoplayer match parent
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            //Also cancel padding make it 0 in all edges
            mLinearLayout.setPadding(0, 0, 0, 0);
            mLinearLayout.setLayoutParams(layoutParams);
            mRelativeLayout.setVisibility(View.GONE);
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();



        }

        if (stepList != null && stepList.size() > 0) {
            initializeView();
        }
        if (currentStepIndex == 0) {
            prevButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
        if (currentStepIndex == stepList.size() - 1) {
            nextButton.setVisibility(View.INVISIBLE);
            prevButton.setVisibility(View.VISIBLE);
        }
        //nextButton.setOnClickListener(this);
        // prevButton.setOnClickListener(this);
        return rootView;


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //last position of playing video
        outState.putLong(EXTRA_PLAYER_POSITION, mPlayerPosition);
        //last state of player if ready(true) or not ready(false)
        outState.putBoolean(EXTRA_PLAYER_READY, isPlayWhenReady);
        outState.putParcelableArrayList(STEPS_LIST, stepList);
        outState.putInt(STEPS_INDEX, currentStepIndex);


        Log.d(TAG, "mPlayerPosition before savedInstanceState = " + String.valueOf(mPlayerPosition));
        Log.d(TAG, "isPlayWhenReady before savedInstanceState = " + String.valueOf(isPlayWhenReady));

    }

    private void initializePlayer(Uri mVideoUri) {

        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mVideoPlayer.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(true);
            MediaSource mediaSource = buildMediaSource(mVideoUri);
            mExoPlayer.prepare(mediaSource, false, false);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mPlayerPosition);

        }
    }

    private void initializeView() {
        releaseVideoPlayer();
        //  mImageAlternative.setVisibility(View.GONE);
        //make sure mStepsArrayList not empty
        if (stepList.size() > 0) {
            //get value of videoUrl by pass last value of mIndex
            String videoUrl = stepList.get(currentStepIndex).getVideoURL();//stepList.get(currentStepIndex).getVideoURL();
            //get value of thumbnailURL by pass last value of mIndex
            String thumbnailURL = stepList.get(currentStepIndex).getThumbnailURL();
            /*if boolean No Rotation or mIstablet true will show the Description below video
             * no rotation because no space in screen to display will show Description just in Tablet*/
            if (!isRotated() || isTablet()) {
                descriptionTv.setText(stepList.get(currentStepIndex).getDescription());

            }
            //play video if value of videoUrl not empty if empty just text will display message
            if (videoUrl.isEmpty() && thumbnailURL.isEmpty()) {

                noVideoTv.setVisibility(View.VISIBLE);
                mVideoPlayer.setVisibility(View.GONE);
                thumbnailImageView.setVisibility(View.GONE);
                Log.d(TAG, "TestShow \n Description = " + stepList.get(currentStepIndex).getDescription());


            } else if (!videoUrl.isEmpty()) {
                initializePlayer(Uri.parse(videoUrl));
                noVideoTv.setVisibility(View.GONE);
                mVideoPlayer.setVisibility(View.VISIBLE);
                thumbnailImageView.setVisibility(View.GONE);
                Log.d(TAG, "TestShow  \n videoUrl =" + videoUrl);

            } else if
                    (!thumbnailURL.isEmpty()) {
                noVideoTv.setVisibility(View.GONE);
                mVideoPlayer.setVisibility(View.GONE);
                Picasso
                        .with(getActivity())
                        .load(thumbnailURL)
                        .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                        .into(thumbnailImageView);
                thumbnailImageView.setVisibility(View.VISIBLE);

                /*Glide.with(getActivity())
                        .load(thumbnailURL)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.brownies))
                        .into(mImageAlternative);
*/
                //TODO: change the image placeholder.
                Log.d(TAG, "TestShow \n thumbnailURL =" + thumbnailURL);

            }


        }
    }

    public boolean isTablet() {
        return (getActivity().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public boolean isRotated() {
        return getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;

    }

    @Optional
    @OnClick(R.id.next_step)
    public void nextStep() {
        navigationButtonListener.onNextClicked();

    }

    @Optional
    @OnClick(R.id.prev_step)
    public void prevStep() {
        navigationButtonListener.onPrevClicked();
    }


   /* private void displayData() {
        releaseVideoPlayer();
        playVideo();
        descriptionTv.setText(stepList.get(currentStepIndex).getDescription());

    }*/

  /*  private void playVideo() {
        if (mExoPlayer == null) {
            String videoUrl = stepList.get(currentStepIndex).getVideoURL();

            if (videoUrl != null) {
                mVideoPlayer.setVisibility(View.VISIBLE);
                noVideoTv.setVisibility(View.GONE);
                if (mVideoPlayer == null) {

                    mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                            new DefaultRenderersFactory(getActivity()),
                            new DefaultTrackSelector(), new DefaultLoadControl());
                    mVideoPlayer.setPlayer(mExoPlayer);
                    mExoPlayer.setPlayWhenReady(true);
                    MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl));
                    mExoPlayer.prepare(mediaSource, true, false);
                }

            } else {
                mVideoPlayer.setVisibility(View.GONE);
                noVideoTv.setVisibility(View.VISIBLE);
            }
        }

    }*/

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("Baking_app"),
                new DefaultExtractorsFactory(), null, null);
    }

  /*  @Override
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
                //initializeView();
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
                //initializeView();
            }

            initializeView();

        }

        //StepDetailFragment newFragment = new StepDetailFragment();
        //newFragment.setArguments(bundle);
       // FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.replace(R.id.steps_container, newFragment );
        //transaction.addToBackStack(null);
       // transaction.commit();

    }
*/

    private void releaseVideoPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.release();
        }

        mExoPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewUnbinder != null) {
            viewUnbinder.unbind();
            releaseVideoPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //get current position for video playing  before onPause

        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            isPlayWhenReady = mExoPlayer.getPlayWhenReady();

        }

        //get current ready state of video player before onPause
        //  isPlayWhenReady = mExoPlayer.getPlayWhenReady();

        Log.d(TAG, "mPlayerPosition onPause = " + String.valueOf(mPlayerPosition));

        if (Util.SDK_INT <= 23) {
            releaseVideoPlayer();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            navigationButtonListener = (NavigationButtonListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " Interface Not Implemented");
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releaseVideoPlayer();
        }
    }

    public interface NavigationButtonListener {
        void onNextClicked();

        void onPrevClicked();
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
