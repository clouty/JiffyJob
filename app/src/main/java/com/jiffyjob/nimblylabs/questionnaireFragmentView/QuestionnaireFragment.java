package com.jiffyjob.nimblylabs.questionnaireFragmentView;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.Model.QuestionnaireModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NielPC on 8/5/2016.
 */
public class QuestionnaireFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();

        YoYo.with(Techniques.SlideInUp)
                .duration(Utilities.getAnimationFast())
                .playOn(getView());
    }

    private void init() {
        View view = getView();
        if (view != null) {
            showRecordAudioPermission();
            questionnaireVP = (ViewPager) view.findViewById(R.id.questionnaireVP);
            generateQuestions();
            if (qnsViewPagerAdapter == null) {
                qnsViewPagerAdapter = new QnsViewPagerAdapter(getActivity());
                qnsViewPagerAdapter.setItems(questionModelList);
                qnsViewPagerAdapter.setViewPager(questionnaireVP);
                questionnaireVP.setOffscreenPageLimit(0);
                //set myViewPager for navigating when press skip btn
                questionnaireVP.setAdapter(qnsViewPagerAdapter);
                questionnaireVP.setPageTransformer(true, new DepthPageTransformer());
            }
            qnsViewPagerAdapter.notifyDataSetChanged();
        }
    }

    private void generateQuestions() {
        questionModelList.clear();
        QuestionnaireModel qnsModel0 = new QuestionnaireModel();
        qnsModel0.setId(0);
        qnsModel0.setQuestion("Questions1 ???");
        List<String> ansList = new ArrayList<>();
        ansList.add("Ans A");
        ansList.add("Ans B");
        ansList.add("Ans C");
        qnsModel0.setQuestionType(QuestionnaireModel.QuestionType.MultipleChoice);
        qnsModel0.setAnswerOptionList(ansList);

        QuestionnaireModel qnsModel1 = new QuestionnaireModel();
        qnsModel0.setId(1);
        qnsModel1.setQuestion("Questions2 ???");
        List<String> ansList1 = new ArrayList<>();
        ansList1.add("Ans A");
        ansList1.add("Ans B");
        ansList1.add("Ans C");
        qnsModel1.setQuestionType(QuestionnaireModel.QuestionType.MultipleChoice);
        qnsModel1.setAnswerOptionList(ansList1);

        QuestionnaireModel qnsModel3 = new QuestionnaireModel();
        qnsModel0.setId(2);
        qnsModel3.setQuestion("Questions3 ???");
        List<String> ansList3 = new ArrayList<>();
        ansList3.add("Ans A");
        ansList3.add("Ans B");
        ansList3.add("Ans C");
        qnsModel3.setQuestionType(QuestionnaireModel.QuestionType.AudioAns);
        qnsModel3.setAnswerOptionList(ansList3);

        questionModelList.add(qnsModel0);
        questionModelList.add(qnsModel1);
        questionModelList.add(qnsModel3);
    }

    private void showRecordAudioPermission() {
        Context context = getActivity();
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.RECORD_AUDIO)) {
                //Show permission explanation dialog...
                Toast.makeText(context, "App requires audio recording for answering questionnaire.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, 8);
            } else {
                //Never ask again selected, or device policy prohibits the app from having that permission.
                //So, disable that feature, or fall back to another situation...
                Toast.makeText(context, "Unable to record audio, please enable audio recording in setting.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(View view, float position) {
            view = view.findViewById(R.id.mainRL);
            int pageWidth = view.getWidth();
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    private QnsViewPagerAdapter qnsViewPagerAdapter;
    private ViewPager questionnaireVP;
    private List<QuestionnaireModel> questionModelList = new ArrayList<>();
}
