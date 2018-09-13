package com.dc.duertest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dc.duertest.bean.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deng jia on 2018/7/25.
 */

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ChatHistoryItem>{
    //定义变量
    private Context mContext;
    private List<Msg> mHistory = new ArrayList<>();

    public ChatHistoryAdapter(Context context, List<Msg> history) {
        mContext = context;
        mHistory = history;
    }
    @Override
    public ChatHistoryItem onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(mContext);
        textView.setSingleLine(false);
        textView.setPadding(0, 10, 0, 10);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parent.addView(textView, layoutParams);
        if (viewType == 0) {
            textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            textView.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        } else {
            textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
        }
        return new ChatHistoryItem(textView);
    }

    @Override
    public void onBindViewHolder(ChatHistoryItem holder, int position) {
        holder.textView.setText(Html.fromHtml(mHistory.get(position).getMessage()));
    }

    @Override
    public int getItemViewType(int position) {
        return mHistory.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }

    class ChatHistoryItem extends RecyclerView.ViewHolder {
        TextView textView;

        public ChatHistoryItem(TextView itemView) {
            super(itemView);
            this.textView = itemView;
        }
    }
}
