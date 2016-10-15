package com.jiffyjob.nimblylabs.questionnaireFragmentView;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.Camera.CameraUtil;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.Camera.PlaybackSurface;
import com.nineoldandroids.animation.Animator;

import java.io.File;
import java.util.List;

/**
 * Created by himur on 9/10/2016.
 */
public class QnsCameraFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera_qn, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() != null) {
            //init camera util
            //AutoFitTextureView textureView = (AutoFitTextureView) getView().findViewById(R.id.textureView);
            //camera2Util = new Camera2Util(getActivity(), getView(), textureView);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            ImageButton recordBtn = (ImageButton) view.findViewById(R.id.recordBtn);
            ImageButton reviewBtn = (ImageButton) view.findViewById(R.id.reviewBtn);
            recordFL = (FrameLayout) view.findViewById(R.id.recordFL);
            submitFL = (FrameLayout) view.findViewById(R.id.submitFL);

            recordBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (cameraUtil.isRecording()) {
                            cameraUtil.stopRecording();
                            stopTimer();
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        cameraUtil.startRecording();
                        startTimer();
                    }
                    return false;
                }
            });

            reviewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePlaybackUI();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isCameraPermissionEnable()) {
            if (getView() != null) {
                cameraUtil = new CameraUtil(getActivity(), getView());
                cameraUtil.init();

                FrameLayout textureView = (FrameLayout) getView().findViewById(R.id.textureView);
                textureView.addView(cameraUtil.getSurfaceView());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getView() != null && cameraUtil != null) {
            FrameLayout textureView = (FrameLayout) getView().findViewById(R.id.textureView);
            textureView.removeView(cameraUtil.getSurfaceView());

            cameraUtil.end();
            cameraUtil = null;
        }
    }

    //Permission request
    public boolean isCameraPermissionEnable() {
        final Activity activity = getActivity();
        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (activity != null) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //permission not granted request for permission, apply to Android M (6.0)  and above
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Should show explain? User deny before call phone
                    String permissionText = activity.getResources().getString(R.string.permissionCamera);
                    Toast.makeText(activity, permissionText, Toast.LENGTH_LONG).show();
                }
                ActivityCompat.requestPermissions(getActivity(), permissions, PERMISSION_CAMERA_CODE);
            }

            String cameraPermission = Manifest.permission.CAMERA;
            String recordPermission = Manifest.permission.RECORD_AUDIO;
            String writeExtStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            int result = activity.checkCallingOrSelfPermission(cameraPermission)
                    + activity.checkCallingOrSelfPermission(recordPermission)
                    + activity.checkCallingOrSelfPermission(writeExtStorage);
            return (result == PackageManager.PERMISSION_GRANTED);
        } else {
            return false;
        }
    }

    /**
     * Create play back UI
     */
    private void updatePlaybackUI() {
        List<File> fileList = cameraUtil.getAllVideos();
        if (fileList.size() > 0 && getView() != null) {
            ImageButton backBtn = (ImageButton) getView().findViewById(R.id.backBtn);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRecordUI();
                }
            });
            YoYo.with(Techniques.FlipInX)
                    .duration(Utilities.getAnimationNormal())
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            progressBar.setVisibility(View.GONE);
                            recordFL.setVisibility(View.GONE);
                            submitFL.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            playbackSetup();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .playOn(submitFL);

            final TextView questionTV = (TextView) getView().findViewById(R.id.questionTV);
            YoYo.with(Techniques.FlipOutX)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            questionTV.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .duration(Utilities.getAnimationNormal())
                    .playOn(questionTV);
        } else {
            Toast.makeText(getActivity(), "No recording found.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create record UI
     */
    private void updateRecordUI() {
        YoYo.with(Techniques.FlipInX)
                .duration(Utilities.getAnimationNormal())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        progressBar.setVisibility(View.VISIBLE);
                        recordFL.setVisibility(View.VISIBLE);
                        submitFL.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        recordSetup();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(recordFL);
        final TextView questionTV = (TextView) getView().findViewById(R.id.questionTV);
        YoYo.with(Techniques.FlipInX)
                .duration(Utilities.getAnimationNormal())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        questionTV.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(questionTV);
    }

    /**
     * Setup play back and play back record when setup is done
     */
    private void playbackSetup() {
        if (getView() != null) {
            FrameLayout textureView = (FrameLayout) getView().findViewById(R.id.textureView);
            textureView.removeAllViews();

            PlaybackSurface playbackSurface = new PlaybackSurface(getActivity());

            List<File> fileList = cameraUtil.getAllVideos();
            playbackSurface.setVideoUrl(fileList.get(fileList.size() - 1).getAbsolutePath());
            textureView.addView(playbackSurface);

            ImageButton backBtn = (ImageButton) getView().findViewById(R.id.backBtn);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRecordUI();
                }
            });
        }
    }

    /**
     * Setup record and resume camera function
     */
    private void recordSetup() {
        FrameLayout textureView = (FrameLayout) getView().findViewById(R.id.textureView);
        textureView.removeAllViews();
        progressBar.setProgress(0);
        onStart();
    }

    private void startTimer() {
        int time = Double.valueOf(millisTimeLeft / 1000).intValue();
        if (time <= 0) {
            return;
        }
        countDownTimer = new CountDownTimer(millisTimeLeft, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisTimeLeft = millisUntilFinished;
                //int timeLeft = Double.valueOf(60 - (millisTimeLeft / 1000)).intValue();
                int timeLeft = Double.valueOf(recordTime - millisTimeLeft).intValue();
                if (timeLeft > 0) {
                    progressBar.setProgress(timeLeft);
                }
            }

            @Override
            public void onFinish() {
                cameraUtil.stopRecording();
                progressBar.setProgress(0);
                millisTimeLeft = recordTime;
            }
        };
        countDownTimer.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();
    }

    public static final int PERMISSION_CAMERA_CODE = 100;
    private final int recordTime = 10000;
    private CountDownTimer countDownTimer;
    private long millisTimeLeft = recordTime;
    private ProgressBar progressBar;
    private FrameLayout recordFL, submitFL;
    //private Camera2Util camera2Util;
    private CameraUtil cameraUtil;
}
