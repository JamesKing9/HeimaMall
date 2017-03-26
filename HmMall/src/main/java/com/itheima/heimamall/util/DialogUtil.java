package com.itheima.heimamall.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.EditText;

import com.itheima.heimamall.bean.Goods;
import com.itheima.heimamall.ui.dialog.GoodsDialog;

import java.util.HashMap;

/**
 * Created by lxj on 2016/8/24.
 */
public class DialogUtil {

    private static HashMap<String,Dialog> dialogMap = new HashMap<>();

    /**
     * 显示进度条对话框
     * @param activity
     * @param dialogTag
     * @param msg
     */
    public static void showProgressDialog(Activity activity, String dialogTag, String msg){
       filterDialog(dialogTag);

        ProgressDialog progressDialog =  new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(msg);
        progressDialog.setCanceledOnTouchOutside(false);
        dialogMap.put(dialogTag,progressDialog);
        progressDialog.show();
    }

    private static void filterDialog(String dialogTag){
        Dialog dialog = dialogMap.get(dialogTag);
        if(dialog!=null){
            dismissDialog(dialogTag);
        }
    }

    /**
     * 关闭对话框
     * @param dialogTag
     */
    public static void dismissDialog(String dialogTag){
        Dialog dialog = dialogMap.get(dialogTag);
        if(dialog!=null){
            dialog.dismiss();
            removeDialog(dialogTag);
        }
    }

    public static void removeDialog(String dialogTag){
        dialogMap.remove(dialogTag);
    }


    /**
     * 显示确认对话框
     * @param activity
     * @param dialogTag
     * @param title
     * @param msg
     */
    public static void showConfirmDialog(Activity activity, String dialogTag, String title, String msg, DialogInterface.OnClickListener listener){
        filterDialog(dialogTag);

        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定",listener)
                .setNegativeButton("取消",null)
                .create();
        alertDialog.show();
        dialogMap.put(dialogTag,alertDialog);
    }

    /**
     * 显示列表选择的对话框
     * @param activity
     * @param dialogTag
     * @param items
     * @param listener
     */
    public static void showListDialog(Activity activity, String dialogTag, String[] items, DialogInterface.OnClickListener listener) {
        filterDialog(dialogTag);
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle("选取头像")
                .setItems(items,listener)
                .create();
        alertDialog.show();
        dialogMap.put(dialogTag,alertDialog);
    }

    /**
     * 显示输入框的对话框
     * @param activity
     */
    public static void showEditDialog(Activity activity, String dialogTag, String title, final OnEditDialogConfirmListener
                                      listener) {
        filterDialog(dialogTag);
        final EditText editText = new EditText(activity);
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(listener!=null && !TextUtils.isEmpty(editText.getText().toString())){
                            listener.onEditDialogConfirm(editText.getText().toString());
                        }
                    }
                })
                .create();
        alertDialog.show();
        dialogMap.put(dialogTag,alertDialog);
    }

    /**
     * 创建单选对话框
     */
    public static void showSingleChoiceDialog(Activity activity, String dialogTag, String title
        , String[] items, int selectedItem,DialogInterface.OnClickListener listener) {
        filterDialog(dialogTag);
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setSingleChoiceItems(items,selectedItem,listener)
                .create();
        alertDialog.show();
        dialogMap.put(dialogTag,alertDialog);
    }

    /**
     * 创建选择日期的对话框
     */
    public static void showDatePickerDialog(Activity activity, String dialogTag, String title
        , final OnDatePickerDialogConfirmListener listener) {
        filterDialog(dialogTag);
        final DatePicker datePicker = new DatePicker(activity);
        datePicker.setCalendarViewShown(false);
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(datePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       if(listener!=null){
                           listener.onDatePickerComfirm(datePicker.getYear()
                           ,datePicker.getMonth(),datePicker.getDayOfMonth());
                       }
                    }
                })
                .create();
        alertDialog.show();
        dialogMap.put(dialogTag,alertDialog);
    }

    public static void showGoodsDialog(Activity activity,Goods goods,GoodsDialog.OnGoodsDialogConfirmListener listener){
        GoodsDialog goodsDialog = new GoodsDialog(activity);
        goodsDialog.show();
        goodsDialog.setOnGoodsConfirmListener(listener);
        goodsDialog.bindData(goods);
    }



    public interface OnDatePickerDialogConfirmListener{
        void onDatePickerComfirm(int year,int month,int day);
    }

    public interface OnEditDialogConfirmListener{
        void onEditDialogConfirm(String content);
    }
}
