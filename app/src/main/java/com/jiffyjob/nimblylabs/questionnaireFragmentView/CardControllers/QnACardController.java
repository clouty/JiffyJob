package com.jiffyjob.nimblylabs.questionnaireFragmentView.CardControllers;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.AnswerAdapter;
import com.jiffyjob.nimblylabs.questionnaireFragmentView.Model.QuestionnaireModel;

import java.util.List;

/**
 * Created by NielPC on 8/7/2016.
 * Provide logic for QnA questionnaire card, extends base questionnaire card
 */
public class QnACardController extends BaseCardController {

    public static QnACardController GetInstance(@NonNull ViewPager viewPager, @NonNull View view, @NonNull List<QuestionnaireModel> modelList, @NonNull int curPostion) {
        if (qnACardController == null) {
            qnACardController = new QnACardController(viewPager, view, modelList, curPostion);
        }
        return qnACardController;
    }

    /**
     * @param viewPager   required interaction to skip question
     * @param view        view for the card
     * @param modelList   model required tp skip question
     * @param curPosition current question number
     */
    private QnACardController(@NonNull ViewPager viewPager, @NonNull View view, @NonNull List<QuestionnaireModel> modelList, @NonNull int curPosition) {
        super(viewPager, view, modelList, curPosition);
        init();
    }

    @Override
    protected void init() {
        super.init();
        ImageView questionMarkIV = (ImageView) view.findViewById(R.id.questionMarkIV);
        TextView questionTV = (TextView) view.findViewById(R.id.questionTV);
        ListView answerLV = (ListView) view.findViewById(R.id.answerLV);
        LinearLayout audioLL = (LinearLayout) view.findViewById(R.id.audioLL);

        //set view, answer or audio
        answerLV.setVisibility(View.VISIBLE);
        audioLL.setVisibility(View.GONE);

        //set question
        QuestionnaireModel model = modelList.get(this.curPosition);
        questionTV.setText(model.getQuestion());

        //Set answers for selection
        AnswerAdapter answerAdapter = new AnswerAdapter(view.getContext(), 0, modelList.get(curPosition).getAnswerOptionList());
        answerLV.setAdapter(answerAdapter);
        answerAdapter.notifyDataSetChanged();
    }

    private static QnACardController qnACardController;
}
