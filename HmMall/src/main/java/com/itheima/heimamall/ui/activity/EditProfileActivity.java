package com.itheima.heimamall.ui.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itheima.heimamall.R;
import com.itheima.heimamall.bean.UserResult;
import com.itheima.heimamall.global.Constant;
import com.itheima.heimamall.global.HeimaMallApp;
import com.itheima.heimamall.global.Url;
import com.itheima.heimamall.util.DialogUtil;
import com.itheima.heimamall.util.FileUtil;
import com.itheima.heimamall.util.SharePrefUtil;
import com.itheima.heimamall.util.ToastUtil;
import com.lxj.okhttpengine.OkHttpEngine;
import com.lxj.okhttpengine.PostParams;
import com.lxj.okhttpengine.callback.OkHttpCallback;
import com.pizidea.imagepicker.AndroidImagePicker;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lxj on 2016/8/21.
 */
public class EditProfileActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.ll_avatar)
    LinearLayout llAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.ll_nickname)
    LinearLayout llNickname;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.ll_gender)
    LinearLayout llGender;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.ll_birthday)
    LinearLayout llBirthday;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        showTitle(true, "个人信息", null);

        refreshUI();
    }

    private void refreshUI() {
        //显示图片
        Glide.with(HeimaMallApp.context)
                .load(Url.ImagePrefix+HeimaMallApp.user.avatar)
                .crossFade(1000)
                .placeholder(R.mipmap.avatar_default)
                .into(ivAvatar);
        //显示昵称
        tvNickname.setText(HeimaMallApp.user.nickname);
        if(HeimaMallApp.user.gender>0){
            tvGender.setText(HeimaMallApp.user.gender==1?"男":"女");
        }
        if(HeimaMallApp.user.birthday>0){

            tvBirthday.setText(DateUtils.formatDateTime(this,HeimaMallApp.user.birthday,DateUtils.FORMAT_NUMERIC_DATE));
        }
    }


    @OnClick({R.id.ll_avatar, R.id.ll_nickname, R.id.ll_gender, R.id.ll_birthday})
    public void onClick(View view) {
        final PostParams params = new PostParams();
        params.add("uid", HeimaMallApp.user.uid);
        switch (view.getId()) {
            case R.id.ll_avatar:
                AndroidImagePicker.getInstance().pickAndCrop(this, true, 200, new AndroidImagePicker.OnImageCropCompleteListener() {
                    @Override
                    public void onImageCropComplete(Bitmap bmp, float ratio) {
                        //1.save
                        File file = FileUtil.saveBmpToCacheDir(EditProfileActivity.this, bmp);
                        //2.upload
                        params.add("file",file);
                        updateUser(params,true);
                    }
                });
                break;
            case R.id.ll_nickname:
                DialogUtil.showEditDialog(this, "edit", "请输入昵称", new DialogUtil.OnEditDialogConfirmListener() {
                    @Override
                    public void onEditDialogConfirm(String content) {
                        //1.build PostParams
                        params.add("nickname",content);
                        //2.update
                        updateUser(params,false);

                    }
                });
                break;
            case R.id.ll_gender:
                String[] items = {"男","女"};
                int seletedItem = HeimaMallApp.user.gender==2?1:0;
                DialogUtil.showSingleChoiceDialog(this, "single", "请选择性别", items,seletedItem ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //1.build params
                        int gender = which==0?1:2;
                        params.add("gender",gender+"");
                        //2.update
                        updateUser(params,false);

                        DialogUtil.dismissDialog("single");
                    }
                });
                break;
            case R.id.ll_birthday:

                DialogUtil.showDatePickerDialog(this, "date", "请选择日期", new DialogUtil.OnDatePickerDialogConfirmListener() {
                    @Override
                    public void onDatePickerComfirm(int year, int month, int day) {
                        //1.build params
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        long time = calendar.getTime().getTime();

                        params.add("birthday",time+"");
                        //2.update
                        updateUser(params,false);
                    }
                });

                break;
        }
    }

    /**
     * 执行更新用户信息的请求
     * @param params
     */
    private void updateUser(PostParams params,boolean isUpdateAvatar) {
        OkHttpEngine.create().post(isUpdateAvatar?Url.updateAvatar:Url.updateUser, params, new OkHttpCallback<UserResult>() {
            @Override
            public void onSuccess(UserResult userBaseBean) {
                if(userBaseBean.isSuccess()){
                    HeimaMallApp.user = userBaseBean.data;
                    SharePrefUtil.create_SP_Instance(EditProfileActivity.this).saveObj(Constant.User,HeimaMallApp.user);

                    ToastUtil.showToast("更新成功！");

                    refreshUI();

                    //通知MeFragment的界面数据更新
                    EventBus.getDefault().post(HeimaMallApp.user);
                }else {
                    ToastUtil.showToast(userBaseBean.msg);
                }
            }
            @Override
            public void onFail(IOException e) {
                    ToastUtil.showToast("请求失败！");
            }
        });
    }
}
