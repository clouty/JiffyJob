package com.jiffyjob.nimblylabs.questionnaireFragmentView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.CardControllers.AudioQnsCardController;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.CardControllers.QnACardController;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.Model.QuestionnaireModel;

import java.util.List;

/**
 * Created by NielPC on 8/5/2016.
 */
public class QnsViewPagerAdapter extends PagerAdapter {

    public QnsViewPagerAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<QuestionnaireModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.questionnaire_card, container, false);
        QuestionnaireModel model = modelList.get(position);
        QnACardController qnACardController = QnACardController.GetInstance(qnsViewPager, view, modelList, position);
        AudioQnsCardController audioQnsCardController = AudioQnsCardController.GetInstance(qnsViewPager, view, modelList, position);
        initSkipButton(view, position);

        //set is multipleChoice or audioType
        switch (model.getQuestionType()) {
            case MultipleChoice:
            default:
                qnACardController.updateView(view, position);
                break;
            case AudioAns:
                audioQnsCardController.updateView(view, position);
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setViewPager(ViewPager qnsViewPagerAdapter) {
        this.qnsViewPager = qnsViewPagerAdapter;
    }

    private void initSkipButton(View view, final int curPosition) {
        Button skipBtn = (Button) view.findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qnsViewPager != null && (curPosition + 1) < modelList.size()) {
                    qnsViewPager.setCurrentItem(curPosition + 1);
                }
            }
        });
    }


    private ViewPager qnsViewPager;
    private List<QuestionnaireModel> modelList;
    private Context context;
}
