package com.miaodao.Fragment.Withdrawals;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.Question;
import com.miaodao.Sys.Model.QuestionSingle;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.DialogHelp;
import com.miaodao.Utils.ExpandableAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fcloud.licai.miaodao.R.id.btn_submit;


/**
 * Created by daixinglong on 2017/3/31.
 */

public class QuestionFragment extends ContentBaseFragment implements View.OnClickListener {

    private final int QUESTION_LIST = 0x0000;
    private final int UPLOAD_QUESTION = 0x0001;
    private List<Question> questions;
    private ExpandableListView questionList;
    private Button btnSubmit;
    private ExpandableAdapter expandableAdapter;


    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_question_list, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText(R.string.withdrawals_message_23);
        questionList = (ExpandableListView) fgView.findViewById(R.id.question_list);
        btnSubmit = (Button) fgView.findViewById(btn_submit);
        expandableAdapter = new ExpandableAdapter(getActivity());
        questionList.setGroupIndicator(null);
        questionList.setAdapter(expandableAdapter);
        getQuestions();
    }

    @Override
    protected void initEvent() {
        btnSubmit.setOnClickListener(this);
        questionList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                showAnswer(groupPosition, childPosition);
                return false;
            }
        });
    }

    /**
     * 处理点击事件
     *
     * @param groupPosition
     * @param childPosition
     */
    private void showAnswer(int groupPosition, int childPosition) {
        Question question = questions.get(groupPosition);
        List<QuestionSingle> answers = question.getAnswers();
        for (QuestionSingle answer : answers) {
            answer.setIsChoose(0);
        }
        QuestionSingle questionSingle = answers.get(childPosition);
        questionSingle.setIsChoose(1);
        expandableAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_submit:
                submitQuestion();
                break;

            default:
                break;

        }
    }


    /**
     * 请求网络，获取问题列表
     */
    private void getQuestions() {
        showProgressDailog();
        Map<String, Object> questions = new HashMap<>();
        questions.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        questions.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        requestForHttp(QUESTION_LIST, AppConfig.getInstance().QUESTION_LIST, questions, false);
    }

    /**
     * 提交问卷答案
     */
    private void submitQuestion() {
        StringBuilder answerSb = new StringBuilder();
        for (Question q : questions) {
            List<QuestionSingle> answers = q.getAnswers();
            for (QuestionSingle singleQ : answers) {
                if (singleQ.getIsChoose() == 1) {
                    answerSb.append(getAnswer(singleQ));
                }
            }
        }

        //判断是否全部填写
        if (answerSb.toString().length() < 4) {
            ToastUtils.shortShow(R.string.withdrawals_message_25);
            return;
        }


        showProgressDailog();
        Map<String, Object> questions = new HashMap<>();
        questions.put("userId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, ""));
        questions.put("tokenId", SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        questions.put("answer", answerSb.toString());
        requestForHttp(UPLOAD_QUESTION, AppConfig.getInstance().UPLOAD_QUESTION_ANSWER, questions, false);
    }


    /**
     * 得到答案
     *
     * @param singleQ
     */
    private String getAnswer(QuestionSingle singleQ) {

        String choice = "";

        switch (singleQ.getKey()) {

            case "A":
                choice = "1";
                break;

            case "B":
                choice = "2";
                break;

            case "C":
                choice = "3";
                break;

            case "D":
                choice = "4";
                break;

            default:
                break;
        }

        return choice;
    }


    @Override
    public void onResponsSuccess(int TAG, Object result) {
        dismissProressDialog();
        btnSubmit.setVisibility(View.VISIBLE);
        Map<String, Object> resultMap = (Map<String, Object>) result;

        switch (TAG) {
            case QUESTION_LIST:
                if (!resultMap.containsKey("data")) return;
                Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
                if (dataMap.containsKey("question")) {
                    List<Map<String, Object>> questionData = (List<Map<String, Object>>) dataMap.get("question");
                    if (questionData == null || questionData.toString() == "null" || questionData.isEmpty())
                        return;
                    showQuestions(questionData);
                }


                break;

            case UPLOAD_QUESTION:
                EventBus.getDefault().post(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
                getActivity().finish();
                break;

            default:
                break;
        }


    }

    /**
     * 显示问题列表
     */
    private void showQuestions(List<Map<String, Object>> questionData) {

        questions = new ArrayList<>();
        for (Map<String, Object> data : questionData) {
            try {
                Question question = new Question();
                question.setQ((String) data.get("Q"));
                List<QuestionSingle> single = new ArrayList<>();
                single.add(new QuestionSingle("A", (String) data.get("A"), 0));
                single.add(new QuestionSingle("B", (String) data.get("B"), 0));
                single.add(new QuestionSingle("C", (String) data.get("C"), 0));
                single.add(new QuestionSingle("D", (String) data.get("D"), 0));
                question.setAnswers(single);
                questions.add(question);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        expandableAdapter.setQuestions(questions);
        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
            questionList.expandGroup(i);
        }
    }


    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
        if (TextUtils.isEmpty(result)) return;
        if (getString(R.string.withdrawals_message_26).equals(result)) {
            goMain();
        } else {
            getQuestions();
        }
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
//        goMain();
    }


    /**
     * 返回上个页面
     */
    private void goMain() {
        DialogHelp instance = DialogHelp.getInstance(getActivity());
        instance.showSuccDialog("提示",
                getString(R.string.withdrawals_message_26),
                new CustomDialog.OnSureInterface() {

                    @Override
                    public void getOnSure() {
                        getActivity().finish();
                    }

                    @Override
                    public void getOnDesmiss() {

                    }
                });


    }

}
