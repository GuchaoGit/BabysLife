package com.guc.babyslife.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.guc.babyslife.R;
import com.guc.babyslife.app.Logger;
import com.guc.babyslife.app.Profile;
import com.guc.babyslife.app.ToastUtils;
import com.guc.babyslife.db.DBUtil;
import com.guc.babyslife.model.Baby;
import com.guc.babyslife.model.GrowData;
import com.guc.babyslife.utils.AgeCalculateUtils;
import com.guc.babyslife.utils.FileUtils;
import com.guc.babyslife.utils.ImageUtils;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by guc on 2019/10/21.
 * 描述：Fragment是实现dialog
 */
public class AddRecordDialogFragment extends DialogFragment {
    private static final String TAG = "AddRecordDialogFragment";
    private static final int REQUEST_CODE = 0x101;
    @BindView(R.id.sel_date_tv)
    TextView mSelDateTv;
    @BindView(R.id.height_et)
    EditText mHeightEt;
    @BindView(R.id.iv_photo)
    ImageView mPhotoIv;
    @BindView(R.id.weight_et)
    EditText mWeightEt;
    Unbinder unbinder;
    private int mNowYear, mNowMonth, mNowDay;
    private int mSelYear, mSelMonth, mSelDay;
    private String mSelDate;
    private String mPhotoPath, mOriginPath;
    private DatePickerDialog mDatePickerDialog;
    private Baby mBaby;
    private OnAddSuccessListener mOnAddSuccessListener;

    public static AddRecordDialogFragment getInstance(Baby baby) {
        AddRecordDialogFragment fg = new AddRecordDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("baby", baby);
        fg.setArguments(bundle);
        return fg;
    }

    public void setOnAddSuccessListener(OnAddSuccessListener onAddSuccessListener) {
        this.mOnAddSuccessListener = onAddSuccessListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaby = getArguments().getParcelable("baby");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_add_record, container, false);
        unbinder = ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {
        setCancelable(false);
        Calendar calendar = Calendar.getInstance();
        mNowYear = mSelYear = calendar.get(Calendar.YEAR);
        mNowMonth = mSelMonth = calendar.get(Calendar.MONTH);
        mNowDay = mSelDay = calendar.get(Calendar.DAY_OF_MONTH);
        mSelDate = mSelYear + "-" + (mSelMonth + 1) + "-" + mSelDay;
        mSelDateTv.setText(mSelDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.sel_date_tv, R.id.add_btn, R.id.cancel_btn, R.id.iv_photo,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sel_date_tv:
                showSelBirthDialog();
                break;
            case R.id.add_btn:
                addRecord();
                break;
            case R.id.cancel_btn:
                this.dismiss();
                break;
            case R.id.iv_photo:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //背景图片选择返回
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            //图片的路径
            Uri mPhotoUri = data.getData();
            mPhotoIv.setImageURI(mPhotoUri);
            mOriginPath = ImageUtils.getRealPathFromUri(getContext(), mPhotoUri);
            Logger.e(TAG, mOriginPath);
        }
    }

    private void addRecord() {
        if (mBaby == null) {
            ToastUtils.toast("未获取到儿童信息");
            return;
        }
        if (TextUtils.isEmpty(mSelDate)) {
            ToastUtils.toast("请选择测量日期");
            return;
        }
        if (mSelYear < mBaby.birthYear || (mSelYear == mBaby.birthYear && mSelMonth < mBaby.birthMonth) || (mSelYear == mBaby.birthYear && mSelMonth == mBaby.birthMonth && mSelDay < mBaby.birthDay)) {
            ToastUtils.toast("宝宝尚未出生哟");
            return;
        }
        String height = mHeightEt.getText().toString().trim();
        height = TextUtils.isEmpty(height) ? "0" : height;
        float hf = Float.valueOf(height);
        if (hf <= 0 || hf > 250) {
            ToastUtils.toast("请输入合理的身高");
            return;
        }
        String weight = mWeightEt.getText().toString().trim();
        weight = TextUtils.isEmpty(weight) ? "0" : weight;
        float wf = Float.valueOf(weight);
        if (wf <= 0 || wf > 200) {
            ToastUtils.toast("请输入合理的体重");
            return;
        }
        if (!TextUtils.isEmpty(mOriginPath)) {
            File dstFile = new File(Profile.getInstance().getImagesDir(), UUID.randomUUID().toString());
            boolean success = FileUtils.copyFile(new File(mOriginPath), dstFile);
            if (success) {
                mPhotoPath = dstFile.getAbsolutePath();
                Logger.e(TAG, mPhotoPath);
            } else {
                Logger.e(TAG, "文件复制失败");
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mSelYear);
        calendar.set(Calendar.MONTH, mSelMonth);
        calendar.set(Calendar.DAY_OF_MONTH, mSelDay);
        int age = AgeCalculateUtils.caculateAge(mBaby, calendar);
        String ageDes = AgeCalculateUtils.getAgeDesc(mBaby, calendar);
        GrowData growData = new GrowData(null, mBaby.uuid, age, age, ageDes, hf, wf, System.currentTimeMillis(), mSelYear + "-" + (mSelMonth + 1) + "-" + mSelDay, mPhotoPath);
        long result = DBUtil.getInstance(getContext()).addGrowData(growData);
        if (result > 0) {
            ToastUtils.toast("添加成功");
            if (mOnAddSuccessListener != null) mOnAddSuccessListener.onSuccess();
            this.dismiss();
        } else {
            ToastUtils.toast("添加失败");
        }
    }

    private void showSelBirthDialog() {
        mDatePickerDialog = new DatePickerDialog(getContext(), (DatePicker view, int year, int month, int dayOfMonth) -> {
            if (mNowYear < year || (mNowYear == year && mNowMonth < month) || (mNowYear == year && mNowMonth == month && mNowDay < dayOfMonth)) {
                ToastUtils.toast("不能选择未来的时间");
                return;
            }
            mSelYear = year;
            mSelMonth = month;
            mSelDay = dayOfMonth;
            mSelDate = mSelYear + "-" + (mSelMonth + 1) + "-" + mSelDay;
            mSelDateTv.setText(mSelDate);
        }, mSelYear, mSelMonth, mSelDay);
        mDatePickerDialog.show();
    }

    public interface OnAddSuccessListener {
        void onSuccess();
    }
}
