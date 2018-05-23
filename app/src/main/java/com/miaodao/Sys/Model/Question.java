package com.miaodao.Sys.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daixinglong on 2017/3/31.
 */

public class Question implements Serializable {
    private String Q;
    private List<QuestionSingle> answers = new ArrayList<>();

    public String getQ() {
        return Q;
    }

    public void setQ(String q) {
        Q = q;
    }

    public List<QuestionSingle> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionSingle> answers) {
        this.answers = answers;
    }
}
