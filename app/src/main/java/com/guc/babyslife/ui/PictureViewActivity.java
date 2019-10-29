package com.guc.babyslife.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.guc.babyslife.R;
import com.guc.babyslife.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
        Observable.create((ObservableEmitter<Bitmap> emitter) -> {
            emitter.onNext(ImageUtils.getImageBitmapFromPath(mPicPath));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        mPvPhoto.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        this.finish();
    }
}
