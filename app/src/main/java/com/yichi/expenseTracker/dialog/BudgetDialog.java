package com.yichi.expenseTracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yichi.tally.R;

public class BudgetDialog extends Dialog implements View.OnClickListener {

    ImageView iv_back;
    Button btn_confirm;
    EditText et_money;
    public  interface OnEnsureListener{
        //回调方法
        public void onEnsure(float money);
    }
    //保存一个实现了 OnEnsureListener 接口的对象实例。
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        iv_back = findViewById(R.id.iv_dialog_budget_back);
        btn_confirm = findViewById(R.id.btn_dialog_budget_confirm);
        et_money = findViewById(R.id.et_dialog_budget_money);

        iv_back.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        if (R.id.iv_dialog_budget_back == id) {
            cancel();
        } else if (R.id.btn_dialog_budget_confirm == id) {
            //获取输入数据(data)
            String inputMoney = et_money.getText().toString();
            if (TextUtils.isEmpty(inputMoney)) {
                Toast.makeText(getContext(),"Input data is empty",Toast.LENGTH_SHORT).show();
                return;
            }
            float money = Float.parseFloat(inputMoney);
            if (money<=0) {
                Toast.makeText(getContext(),"Budget must be greater than 0",Toast.LENGTH_SHORT).show();
                return;
            }
            //如果设置了设置了监听器
            if (onEnsureListener!=null){
                onEnsureListener.onEnsure(money);
            }
            cancel();
        }
    }

    public void setDialogSize(){
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象的参数(wlp)
        WindowManager.LayoutParams attributes = window.getAttributes();
        //获取屏幕宽度
        Display display = window.getWindowManager().getDefaultDisplay();
        // 对话框窗口为屏幕窗口
        attributes.width=(int)(display.getWidth());
        attributes.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
        handler.sendEmptyMessageDelayed(1,100);
    }

    //自动弹出软键盘
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //自动弹出键盘的方法 不需要new是因为 我们要单例模式，同时只能存在一个实例 而系统服务通常以单例模式实现
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //如果键盘打开就关闭，关闭就打开
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
