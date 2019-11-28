package com.guc.babyslife.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guc.babyslife.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

public class PictureViewActivity extends AppCompatActivity {
    @BindView(R.id.pv_photo)
    PhotoView mPvPhoto;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private String mPicPath;
    private String mMeasureDate;

    public static void jump(Context context, String path, String measureDate) {
        Intent intent = new Intent(context, PictureViewActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("date", measureDate);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        ButterKnife.bind(this);
        mPicPath = getIntent().getStringExtra("path");
        mMeasureDate = getIntent().getStringExtra("date");
        if (!TextUtils.isEmpty(mMeasureDate)) {
            mTvTitle.setText(getString(R.string.measure_date_f, mMeasureDate));
        }
        if (!TextUtils.isEmpty(mPicPath)) {
            loadPicture();
        }
    }

    private void loadPicture() {
        Glide.with(this).load(mPicPath).into(mPvPhoto);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        this.finish();
    }
}
