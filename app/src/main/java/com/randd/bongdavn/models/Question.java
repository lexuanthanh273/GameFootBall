package com.randd.bongdavn.models;

/**
 * Created by Thanh Le on 09/05/16.
 */
public class Question {
    private int ID;
    private int Level;
    private String QuestionName;
    private String TrueCase;

    public Question(int ID, int level, String questionName, String trueCase) {
        this.ID = ID;
        Level = level;
        QuestionName = questionName;
        TrueCase = trueCase;
    }

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQuestionName() {
        return QuestionName;
    }

    public void setQuestionName(String questionName) {
        QuestionName = questionName;
    }

    public String getTrueCase() {
        return TrueCase;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public void setTrueCase(String trueCase) {
        TrueCase = trueCase;
    }
}
