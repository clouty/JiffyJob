package com.jiffyjob.nimblylabs.questionnaireFragmentView.Camera2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Common on 9/9/2016.
 */
public class Camera2Util {

    public Camera2Util(Activity activity, View view, AutoFitTextureView textureView) {
        this.activity = activity;
        this.textureView = textureView;
        this.view = view;
    }

    public void init() {
        startBackgroundThread();
        if (!textureView.isAvailable()) {
            //setup textureView when its not yet available
            textureView.setSurfaceTextureListener(mSurfaceTextureListener);
        } else {
            //textureView is available, open camera
            openCamera(textureView.getWidth(), textureView.getHeight());
        }
    }

    public void end() {
        closeCamera();
        stopBackgroundThread();
    }


    //Recording methods
    private void startRecordingVideo() {
        if (cameraDevice == null || textureView == null || previewSize == null) {
            return;
        }

        try {
            setUpMediaRecorder();
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            if (surfaceTexture != null) {
                surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
                previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);

                //Setup surface fpr camera preview
                List<Surface> surfaceList = new ArrayList<>();
                final Surface previewSurface = new Surface(surfaceTexture);
                surfaceList.add(previewSurface);
                previewBuilder.addTarget(previewSurface);

                //Setup surface for mediaRecorder
                Surface recorderSurface = mediaRecorder.getSurface();
                surfaceList.add(recorderSurface);
                previewBuilder.addTarget(recorderSurface);

                cameraDevice.createCaptureSession(surfaceList, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(CameraCaptureSession session) {
                        previewSession = session;
                        updatePreview();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isRecordingVideo = true;
                                // Start recording
                                mediaRecorder.start();
                            }
                        });
                    }

                    @Override
                    public void onConfigureFailed(CameraCaptureSession session) {

                    }
                }, cameraBgHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void stopRecordingVideo() {
        // UI
        isRecordingVideo = false;
        // Stop recording
        mediaRecorder.stop();
        mediaRecorder.reset();

        if (null != activity) {
            Toast.makeText(activity, "Video saved: " + nextVideoAbsolutePath,
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Video saved: " + nextVideoAbsolutePath);
        }
        nextVideoAbsolutePath = null;
    }


    //Initial camera Methods

    /**
     * Get the camera and setup, wait for camera to initialise
     *
     * @param width
     * @param height
     */
    private void openCamera(int width, int height) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        try {
            CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            String frontCameraId = manager.getCameraIdList()[1];

            characteristics = manager.getCameraCharacteristics(frontCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            videoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, videoSize);
            textureView.setAspectRatio(width, height);
            configureTransform(width, height);
            //noinspection MissingPermission
            manager.openCamera(frontCameraId, cameraCallBack, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        closePreviewSession();
        if (cameraDevice != null) {
            cameraDevice.close();
        }
    }

    private void startPreview() {
        if (null == cameraDevice || !textureView.isAvailable() || null == previewSize) {
            return;
        }

        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        if (surfaceTexture != null) {
            try {
                surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
                previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

                Surface previewSurface = new Surface(surfaceTexture);
                previewBuilder.addTarget(previewSurface);

                cameraDevice.createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(CameraCaptureSession session) {
                        previewSession = session;
                        updatePreview();
                    }

                    @Override
                    public void onConfigureFailed(CameraCaptureSession session) {

                    }
                }, cameraBgHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePreview() {
        if (cameraDevice == null) {
            return;
        }
        try {
            setup3AControlsLocked(previewBuilder);
            previewSession.setRepeatingRequest(previewBuilder.build(), null, cameraBgHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closePreviewSession() {
        if (previewSession != null) {
            previewSession.close();
        }
    }


    //Threads
    private void startBackgroundThread() {
        bgWorkerThread = new HandlerThread("cameraBackground");
        bgWorkerThread.start();
        cameraBgHandler = new Handler(bgWorkerThread.getLooper());
    }

    private void stopBackgroundThread() {
        bgWorkerThread.quitSafely();
        try {
            bgWorkerThread.join();
            bgWorkerThread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //Listeners and call backs
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private CameraDevice.StateCallback cameraCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            if (textureView != null) {
                startPreview();
                configureTransform(textureView.getWidth(), textureView.getHeight());
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };

    /**
     * Configures the necessary {@link Matrix} transformation to `textureView`.
     * This method should not to be called until the camera preview size is determined in
     * openCamera, or until the size of `textureView` is fixed.
     *
     * @param viewWidth  The width of `textureView`
     * @param viewHeight The height of `textureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == textureView || null == previewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / previewSize.getHeight(),
                    (float) viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        textureView.setTransform(matrix);
    }


    //Setup tools method
    private void setUpMediaRecorder() throws IOException {
        if (null == activity) {
            return;
        }
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (nextVideoAbsolutePath == null || nextVideoAbsolutePath.isEmpty()) {
            nextVideoAbsolutePath = getVideoFilePath(activity);
        }
        mediaRecorder.setOutputFile(nextVideoAbsolutePath);
        mediaRecorder.setVideoEncodingBitRate(10000000);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(videoSize.getWidth(), videoSize.getHeight());
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (sensorOrientation) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
        }
        mediaRecorder.prepare();
    }

    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }

    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<Size>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    private String getVideoFilePath(Context context) {
        return context.getExternalFilesDir(null).getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4";
    }

    private void setup3AControlsLocked(CaptureRequest.Builder builder) {
        // Enable auto-magical 3A run by camera device
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        Float minFocusDist = characteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);

        // If MINIMUM_FOCUS_DISTANCE is 0, lens is fixed-focus and we need to skip the AF run.
        mNoAFRun = (minFocusDist == null || minFocusDist == 0);

        if (!mNoAFRun) {
            // If there is a "continuous picture" mode available, use it, otherwise default to AUTO.
            if (contains(characteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES), CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)) {
                builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            } else {
                builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
            }
        }

        // If there is an auto-magical flash control mode available, use it, otherwise default to
        // the "on" mode, which is guaranteed to always be available.
        if (contains(characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES), CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)) {
            builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        } else {
            builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_OFF);
        }

        characteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
        characteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
        characteristics.get(CameraCharacteristics.SENSOR_INFO_MAX_FRAME_DURATION);

        characteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
        characteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP);
        builder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, 4);


        // If there is an auto-magical white balance control mode available, use it.
        if (contains(characteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES), CaptureRequest.CONTROL_AWB_MODE_AUTO)) {
            // Allow AWB to run auto-magically if this device supports this
            builder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
        }

        //Using scene mode will disable all other 3 modes
        if (contains(characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES), CaptureRequest.CONTROL_SCENE_MODE_NIGHT_PORTRAIT)) {
            builder.set(CaptureRequest.CONTROL_SCENE_MODE, CaptureRequest.CONTROL_SCENE_MODE_NIGHT_PORTRAIT);
        }
    }

    private static boolean contains(int[] modes, int mode) {
        if (modes == null) {
            return false;
        }
        for (int i : modes) {
            if (i == mode) {
                return true;
            }
        }
        return false;
    }

    private static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    private Activity activity;
    private View view;

    private Handler cameraBgHandler;
    private HandlerThread bgWorkerThread;
    private AutoFitTextureView textureView;
    private CameraDevice cameraDevice;
    private MediaRecorder mediaRecorder;

    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession previewSession;
    private CameraCharacteristics characteristics;

    /**
     * Whether or not the currently configured camera device is fixed-focus.
     */
    private boolean mNoAFRun = false;
    private boolean isRecordingVideo;
    private Integer sensorOrientation;
    private Size previewSize, videoSize;
    private String nextVideoAbsolutePath;

    private static final String TAG = "Camera2Util";
    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

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
