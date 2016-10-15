package com.jiffyjob.nimblylabs.questionnaireFragmentView.Camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by himur on 9/11/2016.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public CameraSurfaceView(Context context) {
        super(context);
    }

    public CameraSurfaceView(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        mCamera = camera;
    }

    public void refreshCamera(Camera camera) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // create the surface and start camera preview
            int screenOrientation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();
            int rotation = CameraUtil.DEFAULT_ORIENTATIONS.get(screenOrientation);

            CamcorderProfile camcorderProfile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT, CamcorderProfile.QUALITY_HIGH);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
            parameters.setPreviewSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
            parameters.setPictureSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
            mHolder.setKeepScreenOn(true);
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(rotation);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        refreshCamera(mCamera);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // mCamera.release();

    }

    private SurfaceHolder mHolder;
    private Camera mCamera;
}
