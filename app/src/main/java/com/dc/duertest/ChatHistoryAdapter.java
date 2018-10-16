package com.dc.duertest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dc.duer.sdk.bean.MsgBean;
import com.dc.duer.sdk.devicemodule.screen.message.RenderCardPayload;
import com.dc.duertest.weight.TextImageView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：
 * 作者：dc on 2018/1/26 16:00
 * 邮箱：597210600@qq.com
 */
public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ChatHistoryItem> {


    private String TAG = "ChatHistoryAdapter";


    private Context context;
    private List<MsgBean> msgBeanList = null;

    public ChatHistoryAdapter(Context context, List<MsgBean> tBean) {
        this.context = context;
        this.msgBeanList = tBean;
    }

    @Override
    public ChatHistoryItem onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatHistoryItem chatHistoryItem = null;
        if (viewType == MsgBean.INPUT_TYPE) {
            chatHistoryItem = new ChatHistoryItem(LayoutInflater.from(context).
                    inflate(R.layout.chat_to_item, parent, false));
        } else {
            chatHistoryItem = new ChatHistoryItem(LayoutInflater.from(context).
                    inflate(R.layout.chat_from_item, parent, false));
        }

        return chatHistoryItem;
    }


    @Override
    public void onBindViewHolder(ChatHistoryItem holder, int position) {
        if (holder instanceof ChatHistoryItem) {
            if (msgBeanList.size() <= 0) return;
            Gson gson = new Gson();
            MsgBean msg = msgBeanList.get(position);
            int type = msg.getType();


            holder.tvTime.setText(msgBeanList.get(position).getChatTime());
            if (type == MsgBean.INPUT_TYPE) {
                String content = msg.getContent();
                holder.tvContent.setVisibility(View.VISIBLE);
                String text = content;
                holder.tvContent.setText(text);
            } else if (type == MsgBean.OUTPUT_TYPE) {
                RenderCardPayload renderCardPayload = msg.getRenderCardPayload();
                if(renderCardPayload == null){
                    // AIUI结果返回
                    String text = msg.getContent();
//                    Log.e(TAG, "textCardBean = " + text);
                    holder.tvContent.setVisibility(View.VISIBLE);
                    holder.tvContentImg.setVisibility(View.GONE);
                    holder.contentTvimg.setVisibility(View.GONE);
                    holder.listItemOutputWeb.setVisibility(View.GONE);
                    holder.tvContent.setText(text);
                } else {
                    RenderCardPayload.Type nameType = renderCardPayload.type;
                    if (nameType == RenderCardPayload.Type.TextCard ) { // textCard
                        String content = renderCardPayload.content;
                        Log.e(TAG, "textCardBean = " + content);
                        holder.tvContent.setVisibility(View.VISIBLE);
                        holder.tvContentImg.setVisibility(View.GONE);
                        holder.contentTvimg.setVisibility(View.GONE);
                        holder.listItemOutputWeb.setVisibility(View.GONE);
                        holder.tvContent.setText(content);
                    } else if (nameType == RenderCardPayload.Type.ImageListCard) {// ImageListCard
                        final List<RenderCardPayload.ImageStructure> imageStructureList = renderCardPayload.imageList;
                        if(imageStructureList != null || imageStructureList.size() > 0){
                            String image = imageStructureList.get(0).src.toString();
                            Log.e(TAG, "imageListCard = " + image);
                            holder.tvContent.setVisibility(View.GONE);
                            holder.tvContentImg.setVisibility(View.VISIBLE);
                            holder.listItemOutputWeb.setVisibility(View.GONE);
                            holder.contentTvimg.setVisibility(View.GONE);
                            Glide.with(context).load(image).into(holder.tvContentImg);
                        }
                        holder.tvContentImg.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, PhotoViewPagerActivity.class);
                                intent.putExtra("photoList", (Serializable) imageStructureList);
                                context.startActivity(intent);
                            }
                        });

                    } else if (nameType == RenderCardPayload.Type.StandardCard) {// StandardCard  百科
                        RenderCardPayload.ImageStructure imageStructure = renderCardPayload.image;
                        String image = imageStructure.src;
                        holder.tvContentImg.setVisibility(View.GONE);
                        holder.listItemOutputWeb.setVisibility(View.GONE);

                        holder.tvContent.setVisibility(View.VISIBLE);
                        holder.contentTvimg.setVisibility(View.VISIBLE);
                        String standard = renderCardPayload.title;

                        Log.e(TAG, "StandardCard = " + standard + "   图片 = " + image);
                        holder.contentTvimg.setTextTitle(standard);
                        holder.contentTvimg.setImageResource(image);
                        holder.tvContent.setText(renderCardPayload.content);

                        final String webPath = renderCardPayload.link.url;
                        holder.contentTvimg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, SpeakStoryActivity.class);
                                intent.putExtra("webPath", webPath);
                                context.startActivity(intent);
                            }
                        });

                    } else if (nameType == RenderCardPayload.Type.ListCard) {// listCart

                        final String webPath = renderCardPayload.link.url;
                        Log.e(TAG, "listCart = " + webPath);

                        holder.contentTvimg.setVisibility(View.GONE);
                        holder.listItemOutputWeb.setVisibility(View.VISIBLE);
                        holder.tvContentImg.setVisibility(View.GONE);
                        holder.tvContent.setVisibility(View.GONE);
                        holder.listItemOutputWeb.loadUrl(webPath);

                        holder.listItemOutputWeb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, SpeakStoryActivity.class);
                                intent.putExtra("webPath", webPath);
                                context.startActivity(intent);
                            }
                        });
                    }
                }
            }
        }

    }


    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        // 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
        MsgBean entity = msgBeanList.get(position);
        if (entity.getType() == MsgBean.INPUT_TYPE) {
            return MsgBean.INPUT_TYPE;
        } else {
            return MsgBean.OUTPUT_TYPE;
        }
    }


    @Override
    public int getItemCount() {
        return msgBeanList.size();
    }


    class ChatHistoryItem extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_user_image)
        ImageView ivUserImage;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_content_img)
        ImageView tvContentImg;
        @BindView(R.id.list_item_output_web)
        WebView listItemOutputWeb;
        @BindView(R.id.content_tvimg)
        TextImageView contentTvimg;


        public ChatHistoryItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //声明WebSettings子类
            WebSettings webSettings = listItemOutputWeb.getSettings();

            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
            webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
            webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
            webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
            webSettings.setAllowFileAccess(true); //设置可以访问文件
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
            webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        }
    }
}
