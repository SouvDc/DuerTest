package com.dc.duertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.dc.duer.sdk.devicemodule.screen.message.RenderCardPayload;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 查看大图
 */
public class PhotoViewPagerActivity extends AppCompatActivity {

	private final String TAG = "PhotoViewPagerActivity";
	ArrayList<RenderCardPayload.ImageStructure> photoList = null;
	private int position = 0;
	ImagePagerAdapter imagePagerAdapter = null;
	@BindView(R.id.viewPager)
    ViewPager viewPager;
	@BindView(R.id.photo_count_textView)
    TextView photoCountTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_view_pager);
		ButterKnife.bind(this);
		initDate();
		initView();
	}

	private void initView() {
		photoCountTextView.setText((position + 1) + "/" + (photoList.size()));
		viewPager.setPageMargin(30);
		viewPager.setPageMarginDrawable(R.drawable.horizontal_pager_shape);
		imagePagerAdapter = new ImagePagerAdapter();
		viewPager.setAdapter(imagePagerAdapter);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				photoCountTextView.setText((position + 1) + "/" + (photoList.size()));
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		viewPager.setCurrentItem(position);

	}

	private void initDate() {
		Intent intent = getIntent();
		if (intent == null) {
			finish();
			return;
		}
		position = intent.getIntExtra("position", 0);
		photoList = (ArrayList<RenderCardPayload.ImageStructure>)
				intent.getSerializableExtra("photoList");
		Log.e(TAG, "position:" + position + ",size:" + photoList.size() + ",photoList:" + photoList.toString());

	}

	class ImagePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return photoList == null ? 0 : photoList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public PhotoView instantiateItem(ViewGroup container, int position) {
			PhotoView view = new PhotoView(PhotoViewPagerActivity.this);
			view.enable();
			container.addView(view);
			String photoBig = photoList.get(position).src.toString();
			Log.d(TAG, "photoBig : " + photoBig);
			Glide.with(PhotoViewPagerActivity.this)
					.load(photoBig)
					.asBitmap()
					.fitCenter()       //图片保留原比例居中缩放，等于或小于view的边界范围
					.placeholder(R.drawable.default_avatar_huge)
					.error(R.drawable.default_avatar_huge)
					.into(view);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					PhotoViewPagerActivity.this.finish();
				}
			});
			Log.e(TAG, "instantiateItem");
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			PhotoView view = (PhotoView) object;
			Glide.clear(view);
			view.setImageDrawable(null);
			container.removeView(view);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		photoList.clear();
		photoList = null;

	}
}
