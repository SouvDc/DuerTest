package com.dc.duertest.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dc.duertest.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 描述：
 * 作者：dc on 2016/3/3 09:24
 * 邮箱：597210600@qq.com
 */
public class TextImageView extends LinearLayout {

    @BindView(R.id.textimageview_sdv)
    ImageView textimageviewSdv;
    @BindView(R.id.textimageview_tv)
    TextView textimageviewTv;

    private Context ctx = null;


    public TextImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.textimageview, this);
        ButterKnife.bind(this);

    }


    /**
     * @param imageResource 图片资源
     * @return
     * @descriptoin 设置图片资源
     * @author dc
     * @date 2016/3/3 9:54
     */
    public void setImageResource(String imageResource) {
        Glide.with(ctx).load(imageResource).into(textimageviewSdv);
//        textimageviewSdv.setBackgroundResource(resourceId);
    }


    /**
     * @param title 标题内容
     *              size 标题字体大小
     *              colorResourceId 字体颜色
     * @return
     * @descriptoin 设置标题
     * @author dc
     * @date 2016/3/3 9:55
     */
    public void setTextTitle(String title) {
        textimageviewTv.setText(title);
    }
}
