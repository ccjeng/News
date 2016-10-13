package com.ccjeng.news.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ccjeng.news.R;
import com.ccjeng.news.view.base.BaseDialog;

/**
 * Created by andycheng on 2016/10/13.
 */

public class WelcomeDialog extends BaseDialog implements View.OnClickListener {
    private TextView tvTitle, tvName;
    private Button btClose;

    public static WelcomeDialog newInstance(String title, String message) {
        WelcomeDialog dialog = new WelcomeDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        dialog.setArguments(args);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_welcome, container);
        Log.d("WelcomeDialog", "onCreateView");
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        tvTitle = (TextView) view.findViewById(R.id.title);
        tvName = (TextView) view.findViewById(R.id.message);
        btClose = (Button) view.findViewById(R.id.bt_close);
        btClose.setOnClickListener(this);

        //Set Original Name
        String title = getArguments().getString("title");
        tvTitle.setText(title);
        String message = getArguments().getString("message");
        tvName.setText(message);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_close: {
                dismiss();
                break;
            }
        }
    }
}
