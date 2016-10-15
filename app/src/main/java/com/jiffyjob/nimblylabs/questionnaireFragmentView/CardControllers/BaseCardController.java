package com.jiffyjob.nimblylabs.questionnaireFragmentView.CardControllers;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.Model.QuestionnaireModel;

import java.util.List;

/**
 * Created by NielPC on 8/7/2016.
 * Provide logic for base questionnaire card
 */
public class BaseCardController {

    public BaseCardController(@NonNull ViewPager myViewPager, @NonNull View view, @NonNull List<QuestionnaireModel> modelList, @NonNull int curPosition) {
        this.viewPager = myViewPager;
        this.modelList = modelList;
        this.view = view;
        this.curPosition = curPosition;
    }

    protected void init() {
        TextView questionTV = (TextView) this.view.findViewById(R.id.questionTV);
        QuestionnaireModel model = modelList.get(curPosition);
        questionTV.setText(model.getQuestion());
        updateBackground(view, curPosition);
    }

    /**
     * Update question card with latest view
     * @param view
     * @param curPosition
     */
    public void updateView(@NonNull View view, @NonNull int curPosition) {
        this.view = view;
        this.curPosition = curPosition;
        init();
    }

    /**
     * Set background cards to show user where to swipe
     *
     * @param view
     * @param position
     */
    private void updateBackground(View view, int position) {
        View rightView = view.findViewById(R.id.rightView);
        View leftView = view.findViewById(R.id.leftView);
        if (position == 0) {
            rightView.setVisibility(View.VISIBLE);
            leftView.setVisibility(View.GONE);
        } else if ((this.modelList.size() - 1) == position) {
            rightView.setVisibility(View.GONE);
            leftView.setVisibility(View.VISIBLE);
            Button skipBtn = (Button) view.findViewById(R.id.skipBtn);
            skipBtn.setVisibility(View.GONE);
        } else {
            rightView.setVisibility(View.VISIBLE);
            leftView.setVisibility(View.VISIBLE);
        }
    }

    protected ViewPager viewPager;
    protected View view;
    protected List<QuestionnaireModel> modelList;
    protected int curPosition = 0;
}
