package com.jiffyjob.nimblylabs.browseIndividual;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.browseIndividual.Event.JobApplyEvent;
import com.jiffyjob.nimblylabs.browsePage.BrowsePageModel;
import com.jiffyjob.nimblylabs.commonUtilities.Utilities;
import com.jiffyjob.nimblylabs.httpServices.PostHttpService;
import com.nineoldandroids.animation.Animator;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by himur on 1/31/2016.
 */
public class ApplyDialogFragment extends DialogFragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_apply_dialog, container, false);
        init();
        return view;
    }

    public void setIsApplied(boolean isApplied) {
        this.isApplied = isApplied;
    }

    public void setModel(BrowsePageModel model) {
        this.model = model;
    }

    private void init() {
        selfIntroET = (EditText) view.findViewById(R.id.selfIntroET);
        wordCountTV = (TextView) view.findViewById(R.id.wordCountTV);
        errorTV = (TextView) view.findViewById(R.id.errorTV);
        applyBtn = (Button) view.findViewById(R.id.applyBtn);

        if (!isApplied) {
            RelativeLayout selfIntroLayout = (RelativeLayout) view.findViewById(R.id.selfIntroLayout);
            RelativeLayout resultDialogLayout = (RelativeLayout) view.findViewById(R.id.resultDialogLayout);
            selfIntroLayout.setVisibility(View.VISIBLE);
            resultDialogLayout.setVisibility(View.GONE);
        } else {
            RelativeLayout selfIntroLayout = (RelativeLayout) view.findViewById(R.id.selfIntroLayout);
            RelativeLayout resultDialogLayout = (RelativeLayout) view.findViewById(R.id.resultDialogLayout);
            selfIntroLayout.setVisibility(View.GONE);
            resultDialogLayout.setVisibility(View.VISIBLE);
            applyBtn.setText("Kay, got it");
        }

        selfIntroET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateIntroWordCount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isApplied) {
                    View selfIntroLayout = view.findViewById(R.id.rootDialog);
                    YoYo.with(Techniques.SlideOutLeft)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    dismiss();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            })
                            .duration(Utilities.getAnimationFast())
                            .playOn(selfIntroLayout);
                    applyJob();

                } else {
                    View resultDialogLayout = view.findViewById(R.id.rootDialog);
                    YoYo.with(Techniques.SlideOutLeft)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    dismiss();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            })
                            .duration(Utilities.getAnimationFast())
                            .playOn(resultDialogLayout);
                }
            }
        });
    }

    /**
     * JobApplyEvent will be posted when httpServices call back. This event is handle by BrowseIndividualView
     */
    private void applyJob() {
        JobApplyEvent event = new JobApplyEvent();
        String url = context.getResources().getString(R.string.jobApply_service);

        //form JSONObject
        JSONObject applyJobJson = new JSONObject();
        try {
            applyJobJson.put("UserID", "123456789");
            applyJobJson.put("JobID", model.getId2());
            applyJobJson.put("ShortCV", selfIntroET.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostHttpService postHttpService = new PostHttpService(url, event);
        postHttpService.execute(applyJobJson);
    }

    private void updateIntroWordCount() {
        String selfIntroStr = selfIntroET.getText().toString();
        int wordsCount = Utilities.countWords(selfIntroStr);
        String wordsCountStr = String.format("%s/%s", wordsCount + "", maxWordCount + "");
        wordCountTV.setText(wordsCountStr);
        if (wordsCount > maxWordCount) {
            applyBtn.setEnabled(false);
            errorTV.setText("Erm.. You have exceeded word limit.");
            errorTV.setVisibility(View.VISIBLE);
        } else {
            applyBtn.setEnabled(true);
            errorTV.setVisibility(View.INVISIBLE);
        }
    }

    private Context context;
    private static final String userID = ""; //TODO: change this generic userID

    private BrowsePageModel model;
    private TextView errorTV;
    private EditText selfIntroET;
    private TextView wordCountTV;
    private Button applyBtn;
    private final int maxWordCount = 50;

    private boolean isApplied = false;
    private View view;

    private PostHttpService postHttpService;
}
