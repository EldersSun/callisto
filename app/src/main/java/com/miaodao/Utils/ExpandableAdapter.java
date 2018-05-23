package com.miaodao.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Model.Question;
import com.miaodao.Sys.Model.QuestionSingle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daixinglong on 2017/3/31.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private List<Question> questions = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;

    public ExpandableAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setQuestions(List<Question> questions) {
        this.questions.clear();
        this.questions = questions;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return questions.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return questions.get(groupPosition).getAnswers().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return questions.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return questions.get(groupPosition).getAnswers().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder parentViewHolder;
        if (convertView == null) {
            parentViewHolder = new ParentViewHolder();
            convertView = inflater.inflate(R.layout.item_question_question, null);
            parentViewHolder.question = (TextView) convertView.findViewById(R.id.tv_question);
            convertView.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
        }
        parentViewHolder.question.setText(questions.get(groupPosition).getQ());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.item_question_answer, null);
            childViewHolder.ivSelect = (ImageView) convertView.findViewById(R.id.iv_select);
            childViewHolder.tvAnswer = (TextView) convertView.findViewById(R.id.tv_answer);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        QuestionSingle questionSingle = questions.get(groupPosition).getAnswers().get(childPosition);
        childViewHolder.tvAnswer.setText(questionSingle.getAnswer());
        if (questionSingle.getIsChoose() == 0) {
            childViewHolder.ivSelect.setImageResource(R.drawable.ic_ticket_btn_normal);
        } else {
            childViewHolder.ivSelect.setImageResource(R.drawable.ic_ticket_btn_check);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class ParentViewHolder {
        TextView question;
    }

    class ChildViewHolder {
        ImageView ivSelect;
        TextView tvAnswer;
    }

}
