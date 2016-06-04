package com.randd.bongdavn.models;

/**
 * Created by Thanh Le on 09/05/16.
 */
public class Case {
    private int ID;
    private int IDQuestion;
    private String CaseName;
    private byte [] CaseImage;
    private int result;

    public Case(int ID, int IDQuestion, String caseName, byte[] caseImage, int result) {
        this.ID = ID;
        this.IDQuestion = IDQuestion;
        CaseName = caseName;
        CaseImage = caseImage;
        this.result = result;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIDQuestion() {
        return IDQuestion;
    }

    public void setIDQuestion(int IDQuestion) {
        this.IDQuestion = IDQuestion;
    }

    public String getCaseName() {
        return CaseName;
    }

    public void setCaseName(String caseName) {
        CaseName = caseName;
    }

    public byte[] getCaseImage() {
        return CaseImage;
    }

    public void setCaseImage(byte[] caseImage) {
        CaseImage = caseImage;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
