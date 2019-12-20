package com.guc.babyslife.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guc.babyslife.R;
import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.app.ToastUtils;
import com.guc.babyslife.databinding.BabyDetailBinding;
import com.guc.babyslife.db.DBUtil;
import com.guc.babyslife.model.Baby;
import com.guc.babyslife.model.GrowData;
import com.guc.babyslife.ui.adapter.AdapterRecords;
import com.guc.babyslife.ui.adapter.RecyclerViewBindingAdapter;
import com.guc.babyslife.ui.fragment.AddRecordDialogFragment;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by guc on 2019/10/15.
 * 描述：婴儿详细信息
 */
public class BabyDetailActivity extends BaseActivity implements View.OnClickListener, RecyclerViewBindingAdapter.ItemClickListener, RecyclerViewBindingAdapter.ItemLongClickListener {
    private static final String TAG = "BabyDetailActivity";
    private BabyDetailBinding mBinding;
    private Baby mBaby;
    private AdapterRecords mAdapter;
    private DBUtil mDBUtils;
    private Disposable mDisposable;
    private DividerItemDecoration dividerItemDecoration;

    public static void jump(Context context, Baby baby) {
        Intent intent = new Intent(context, BabyDetailActivity.class);
        intent.putExtra("baby", baby);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaby = getIntent().getParcelableExtra("baby");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_baby_detail);
        mBinding.setDetail(mBaby);
        dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mDBUtils = DBUtil.getInstance(this);
        mAdapter = new AdapterRecords(this, null);
        mBinding.setRecordAdapter(mAdapter);
        mBinding.setDivider(dividerItemDecoration);
        mBinding.setClick(this);
        mBinding.setItemClickListener(this);
        mBinding.setItemLongClickListener(this);
        update();
        initCounterTip();
    }

    private void initCounterTip() {
        Calendar calendarNow = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarNow.get(Calendar.YEAR), mBaby.birthMonth, mBaby.birthDay);
        if (calendarNow.before(calendar)) {
            mBinding.setYear(calendar.get(Calendar.YEAR));
        } else {
            mBinding.setYear(calendar.get(Calendar.YEAR) + 1);
        }

        mBinding.setMonth(mBaby.birthMonth + 1);
        mBinding.setDay(mBaby.birthDay);
    }

    @Override
    public void onClick(View v) {
        AddRecordDialogFragment fragment = AddRecordDialogFragment.getInstance(mBaby);
        fragment.show(getSupportFragmentManager(), TAG);
        fragment.setOnAddSuccessListener(() -> mAdapter.update(mDBUtils.getGrowDataByUuid(mBaby.uuid)));
    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, View view, int position) {
        PictureViewActivity.jump(mContext, mAdapter.getItem(position).getPhoto(), mAdapter.getItem(position).getMeasureDate());
//        ToastUtils.toast("测量日期：" + mAdapter.getItem(position).getMeasureDate());
    }

    @Override
    public void onItemLongClick(RecyclerView.Adapter adapter, View view, int position) {
        new AlertDialog.Builder(mContext).setTitle(R.string.warning)
                .setMessage(R.string.warning_delete_data)
                .setNegativeButton(R.string.cancel, (dialog, v) -> dialog.dismiss())
                .setPositiveButton(R.string.sure, (dialog, v) -> {
                    DBUtil.getInstance(mContext).deleteGrowDataById(mAdapter.getItem(position).getId());
                    ToastUtils.toast("删除成功");
                    update();
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    //更新列表
    private void update() {
        mDisposable = Observable.create((ObservableEmitter<List<GrowData>> emitter) -> {
                    Thread.sleep(10);
                    emitter.onNext(mDBUtils.getGrowDataByUuid(mBaby.uuid));
                }
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((List<GrowData> babies) ->
                        mAdapter.update(babies));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) mDisposable.dispose();
    }
}
