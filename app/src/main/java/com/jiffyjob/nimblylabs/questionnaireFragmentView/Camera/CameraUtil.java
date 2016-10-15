package com.jiffyjob.nimblylabs.questionnaireFragmentView.Camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jiffyjob.nimblylabs.app.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by himur on 9/11/2016.
 */
public class CameraUtil {

    public CameraUtil(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    public void init() {
        if (camera == null) {
            camera = getCameraInstance();
        }
        cameraSurfaceView = new CameraSurfaceView(activity, camera);
    }

    public void end() {
        cameraSurfaceView.getHolder().removeCallback(cameraSurfaceView);
        if (camera != null) {
            camera.stopPreview();
        }
        releaseMediaRecorder();
        releaseCamera();
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void startRecording() {
        new setupMediaRecordAysnc().execute();
        isRecording = true;
    }

    public void stopRecording() {
        try {
            mediaRecorder.stop();
            isRecording = false;
            releaseMediaRecorder();
            Toast.makeText(activity, "Record completed.", Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            //Remove file when record fail
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            //restart camera after failure
            FrameLayout textureView = (FrameLayout) view.findViewById(R.id.textureView);
            textureView.removeView(getSurfaceView());
            end();

            init();
            textureView.addView(getSurfaceView());
            Toast.makeText(activity, "Fail to record.", Toast.LENGTH_SHORT).show();
        }
    }

    public CameraSurfaceView getSurfaceView() {
        return cameraSurfaceView;
    }

    public List<File> getAllVideos() {
        String urlPath = activity.getExternalFilesDir(null).getAbsolutePath() + "/";
        File filePath = new File(urlPath);
        File[] allFile = filePath.listFiles();
        List<File> foundFileList = new ArrayList<>();
        for (File file : allFile) {
            String fileName = file.getName();
            String fileExt = fileName.substring(fileName.indexOf(".") + 1);
            if (fileExt.equalsIgnoreCase("mp4")) {
                foundFileList.add(file);
            }
        }
        Collections.sort(foundFileList, comparator);
        //Collections.reverse(foundFileList);
        return foundFileList;
    }

    private Camera getCameraInstance() {
        try {
            if (camera != null) {
                camera.release();
            }
            //Making use of camera to create camera instance
            int currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            camera = Camera.open(currentCameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e(TAG, e.getMessage());
        }
        return camera; // returns null if camera is unavailable
    }

    private boolean setUpMediaRecorder() {
        try {
            if (null == activity) {
                return false;
            }
            mediaRecorder = new MediaRecorder();


            // We need to make sure that our preview and recording video size are supported by the
            // camera. Query camera to find all the sizes and choose the optimal size given the
            // dimensions of our preview surface.
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
            List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
            Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                    mSupportedPreviewSizes, cameraSurfaceView.getWidth(), cameraSurfaceView.getHeight());

            // Use the same size for recording profile.
            CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
            profile.videoFrameWidth = optimalSize.width;
            profile.videoFrameHeight = optimalSize.height;

            // likewise for the camera object itself.
            parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
            camera.setParameters(parameters);


            camera.unlock();
            mediaRecorder.setCamera(camera);

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            //mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
            mediaRecorder.setProfile(profile);

            int screenOrientation = (activity).getWindowManager().getDefaultDisplay().getRotation();
            int rotation = CameraUtil.INVERSE_ORIENTATIONS.get(screenOrientation);
            mediaRecorder.setOrientationHint(rotation);

            filePath = getVideoFilePath(activity);
            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.setMaxDuration(600000); // Set max duration 60 sec.
            mediaRecorder.setMaxFileSize(30000000); // Set max file size 30M
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            if (camera != null) {
                camera.lock();
            }
        }
    }

    private String getVideoFilePath(Context context) {
        return context.getExternalFilesDir(null).getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4";
    }

    private class setupMediaRecordAysnc extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return setUpMediaRecorder();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                mediaRecorder.start();
            }
        }
    }


    private Activity activity;
    private View view;
    private Camera camera;
    private CameraSurfaceView cameraSurfaceView;

    private boolean isRecording = false;
    private String nextVideoAbsolutePath;
    private MediaRecorder mediaRecorder;
    private Integer sensorOrientation;
    private String filePath;

    private Comparator comparator = new Comparator<File>() {
        @Override
        public int compare(File lhs, File rhs) {
            return (int) (lhs.lastModified() - rhs.lastModified());
        }
    };

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final String TAG = CameraUtil.class.getSimpleName();

    public static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    public static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }
}
