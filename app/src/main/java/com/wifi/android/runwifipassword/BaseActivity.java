package com.wifi.android.runwifipassword;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by chuangguo.qi on 2016/11/7.
 */

public class BaseActivity extends Activity {


    private Dialog tip;

    public void showDialogTip(String msg, String confirmStr, String cancelStr,
                              final View.OnClickListener confirmClick,
                              final View.OnClickListener cancelClick) {

        tip = new Dialog(this, R.style.UnibotDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tips,
                null);
        ((TextView) view.findViewById(R.id.content)).setText(msg);
        tip.setContentView(view);
        final TextView btnOk = (TextView) view.findViewById(R.id.sure);
        final TextView btnCancel = (TextView) view.findViewById(R.id.cancel);
        if (!TextUtils.isEmpty(confirmStr)) {
            btnOk.setText(confirmStr);
        }
        if (!TextUtils.isEmpty(cancelStr)) {
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setText(cancelStr);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tip != null && tip.isShowing()) {
                    tip.dismiss();
                }
                if (cancelClick != null) {
                    cancelClick.onClick(btnCancel);
                }
            }
        });

        view.findViewById(R.id.sure).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (tip != null && tip.isShowing()) {
                            tip.dismiss();
                        }
                        if (confirmClick != null) {
                            confirmClick.onClick(btnOk);
                        }
                    }
                });
        tip.setCancelable(false);
        tip.setCanceledOnTouchOutside(false);
        tip.show();

    }
}
