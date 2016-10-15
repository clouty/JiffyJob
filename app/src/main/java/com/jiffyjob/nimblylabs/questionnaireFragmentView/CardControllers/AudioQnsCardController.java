package com.jiffyjob.nimblylabs.questionnaireFragmentView.CardControllers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.Model.QuestionnaireModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by NielPC on 8/13/2016.
 * Provide logic for Audio questionnaire card, extends base questionnaire card
 */
public class AudioQnsCardController extends BaseCardController {
    public static AudioQnsCardController GetInstance(@NonNull ViewPager viewPager, @NonNull View view, @NonNull List<QuestionnaireModel> modelList, @NonNull int curPostion) {
        if (audioQnsCardController == null) {
            audioQnsCardController = new AudioQnsCardController(viewPager, view, modelList, curPostion);
        }
        return audioQnsCardController;
    }

    /**
     * @param viewPager   required interaction to skip question
     * @param view        view for the card
     * @param modelList   model required tp skip question
     * @param curPosition current question number
     */
    public AudioQnsCardController(@NonNull ViewPager viewPager, @NonNull View view, @NonNull List<QuestionnaireModel> modelList, @NonNull int curPosition) {
        super(viewPager, view, modelList, curPosition);
    }

    @Override
    protected void init() {
        super.init();
        ImageView questionMarkIV = (ImageView) view.findViewById(R.id.questionMarkIV);
        final ImageView recordBtn = (ImageView) view.findViewById(R.id.recordBtn);
        ProgressBar audioRecordPB = (ProgressBar) view.findViewById(R.id.audioRecordPB);
        TextView questionTV = (TextView) view.findViewById(R.id.questionTV);
        ListView answerLV = (ListView) view.findViewById(R.id.answerLV);
        LinearLayout audioLL = (LinearLayout) view.findViewById(R.id.audioLL);

        //set view, answer or audio
        answerLV.setVisibility(View.GONE);
        audioLL.setVisibility(View.VISIBLE);

        //set question
        final QuestionnaireModel model = modelList.get(this.curPosition);
        questionTV.setText(model.getQuestion());

        //init audio recorder

    }

    public Uri saveCurrentRecordToMediaDB(final String fileName) {
        if (mAudioRecordUri != null) return mAudioRecordUri;

        final Activity activity = (Activity) view.getContext();
        final Resources res = activity.getResources();
        final ContentValues cv = new ContentValues();
        final File file = new File(fileName);
        final long current = System.currentTimeMillis();
        final long modDate = file.lastModified();
        final Date date = new Date(current);
        final String dateTemplate = "dd/MM/yyyy HH:mm:ss";
        final SimpleDateFormat formatter = new SimpleDateFormat(dateTemplate, Locale.getDefault());
        final String title = formatter.format(date);
        final long sampleLengthMillis = 1;
        // Lets label the recorded audio file as NON-MUSIC so that the file
        // won't be displayed automatically, except for in the playlist.
        cv.put(MediaStore.Audio.Media.IS_MUSIC, "1");

        cv.put(MediaStore.Audio.Media.TITLE, title);
        cv.put(MediaStore.Audio.Media.DATA, file.getAbsolutePath());
        cv.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        cv.put(MediaStore.Audio.Media.DATE_MODIFIED, (int) (modDate / 1000));
        cv.put(MediaStore.Audio.Media.DURATION, sampleLengthMillis);
        cv.put(MediaStore.Audio.Media.MIME_TYPE, "audio/*");
        /*cv.put(MediaStore.Audio.Media.ARTIST, res.getString(R.string.audio_db_artist_name));
        cv.put(MediaStore.Audio.Media.ALBUM, res.getString(R.string.audio_db_album_name));*/

        Log.d(TAG, "Inserting audio record: " + cv.toString());

        final ContentResolver resolver = activity.getContentResolver();
        final Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.d(TAG, "ContentURI: " + base);

        mAudioRecordUri = resolver.insert(base, cv);
        if (mAudioRecordUri == null) {
            return null;
        }
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, mAudioRecordUri));
        return mAudioRecordUri;
    }

    private final String TAG = AudioQnsCardController.class.getSimpleName();
    private Uri mAudioRecordUri;
    private static AudioQnsCardController audioQnsCardController;
}
