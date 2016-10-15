package com.jiffyjob.nimblylabs.questionnaireFragmentView.Camera;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by himur on 9/12/2016.
 */
public class PlaybackSurface extends SurfaceView implements SurfaceHolder.Callback {
    /**
     * VideoUrl have to be set before adding surface to textureView.
     *
     * @param context Context of activity
     */
    public PlaybackSurface(Context context) {
        super(context);
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setVideoUrl(@NonNull String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (videoUrl != null) {
            try {
                surfaceHolder = holder;
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                surfaceHolder.setKeepScreenOn(true);
                //mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(videoUrl), holder);
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.setDisplay(holder);
                mediaPlayer.setDataSource(getContext(), Uri.parse(videoUrl));
                mediaPlayer.setLooping(true);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {

            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private String videoUrl;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
}
