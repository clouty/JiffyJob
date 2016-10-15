package com.jiffyjob.nimblylabs.questionnaireFragmentView.Model;

import java.util.List;

/**
 * Created by NielPC on 8/5/2016.
 * <pre>
 * QuestionType 0 - Multiple choice
 * QuestionType 1 - Audio question
 * </pre>
 */
public class QuestionnaireModel {

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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswerOptionList() {
        return answerOptionList;
    }

    public void setAnswerOptionList(List<String> answerOptionList) {
        this.answerOptionList = answerOptionList;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public enum QuestionType {MultipleChoice, AudioAns}

    private int id;
    private String categoryId;
    private QuestionType questionType;
    private String question;
    private List<String> answerOptionList;
}
