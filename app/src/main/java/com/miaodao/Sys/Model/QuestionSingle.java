package com.miaodao.Sys.Model;

import java.io.Serializable;

/**
 * Created by daixinglong on 2017/3/31.
 */

public class QuestionSingle implements Serializable {

    private String key;
    private String answer;
    private int isChoose;

    public QuestionSingle() {
    }

    public QuestionSingle(String key, String answer, int isChoose) {
        this.key = key;
        this.answer = answer;
        this.isChoose = isChoose;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(int isChoose) {
        this.isChoose = isChoose;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
