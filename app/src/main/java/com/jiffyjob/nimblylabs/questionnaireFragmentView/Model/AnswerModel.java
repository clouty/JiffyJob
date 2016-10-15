package com.jiffyjob.nimblylabs.questionnaireFragmentView.Model;

/**
 * Created by NielPC on 8/13/2016.
 */
public class AnswerModel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public QuestionnaireModel.QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionnaireModel.QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAudioAnswer() {
        return audioAnswer;
    }

    public void setAudioAnswer(String audioAnswer) {
        this.audioAnswer = audioAnswer;
    }

    private int id;
    private String categoryId;
    private QuestionnaireModel.QuestionType questionType;
    private String answer;
    private String audioAnswer;
}
