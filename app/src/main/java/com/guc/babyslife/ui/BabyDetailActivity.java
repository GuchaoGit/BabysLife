package com.guc.babyslife.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.guc.babyslife.R;
import com.guc.babyslife.app.BaseActivity;
import com.guc.babyslife.app.Logger;
import com.guc.babyslife.app.ToastUtils;
import com.guc.babyslife.databinding.BabyDetailBinding;
import com.guc.babyslife.db.DBUtil;
import com.guc.babyslife.model.Baby;
import com.guc.babyslife.model.GrowData;
import com.guc.babyslife.ui.adapter.AdapterRecords;
import com.guc.babyslife.ui.adapter.RecyclerViewBindingAdapter;
import com.guc.babyslife.ui.fragment.AddRecordDialogFragment;
import com.guc.babyslife.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
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
    private ArrayList<GrowData> mGrowData;
    private AdapterRecords mAdapter;
    private DBUtil mDBUtils;
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
        RecyclerView r = this.findViewById(R.id.rcv_baby);
        this.registerForContextMenu(r);
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
        switch (v.getId()) {
            case R.id.btn_add://添加
                AddRecordDialogFragment fragment = AddRecordDialogFragment.getInstance(mBaby);
                fragment.show(getSupportFragmentManager(), TAG);
                fragment.setOnAddSuccessListener(() -> mAdapter.update(mDBUtils.getGrowDataByUuid(mBaby.uuid)));
                break;
            case R.id.btn_growth_curve: // 成长曲线
                if (mGrowData == null || mGrowData.size() == 0) {
                    ToastUtils.toast("您还未添加成长记录");
                } else {
                    GrowthCurveActivity.jump(mContext, mBaby, mGrowData);
                }
                break;
        }

    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, View view, int position) {
        GrowData data = mAdapter.getItem(position);
        double bmi = Utils.getBMI(data.getHeight(), data.getWeight());
        String suggest = Utils.getBmiSuggest(bmi);
        ToastUtils.toastLong("您的BMI为：" + bmi + "  " + suggest);
    }

    @Override
    public void onItemLongClick(RecyclerView.Adapter adapter, View view, int position) {
        Logger.e(TAG, "长按");
        mAdapter.setContextMenuPosition(position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Logger.e(TAG, "创建ContextMenu");
        menu.setHeaderTitle("操作");
        menu.setHeaderIcon(R.drawable.icon_opt);
        menu.add(0, 1, Menu.NONE, "编辑");
        menu.add(0, 2, Menu.NONE, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = mAdapter.getContextMenuPosition();
        switch (item.getItemId()) {
            case 1://编辑
                AddRecordDialogFragment fragment = AddRecordDialogFragment.getInstance(mBaby, mAdapter.getItem(position));
                fragment.show(getSupportFragmentManager(), TAG);
                fragment.setOnAddSuccessListener(() -> mAdapter.update(mDBUtils.getGrowDataByUuid(mBaby.uuid)));
                break;
            case 2: //删除
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
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    //更新列表
    private void update() {
        showCoffeeLoading();
        Observable.create((ObservableEmitter<List<GrowData>> emitter) -> {
                    emitter.onNext(mDBUtils.getGrowDataByUuid(mBaby.uuid));
                    Thread.sleep(2000);
                    emitter.onComplete();
                }
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<GrowData>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<GrowData> babies) {
                        mGrowData = new ArrayList<>();
                        mGrowData.addAll(babies);
                        mAdapter.update(mGrowData);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        hideCoffeeLoading();
                    }
                });
    }

}
