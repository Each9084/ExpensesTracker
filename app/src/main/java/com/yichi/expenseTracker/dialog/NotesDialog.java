package com.yichi.expenseTracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.yichi.tally.R;

//把带两个按钮（confirm cancel）的对话框封装起来
public class NotesDialog extends Dialog implements View.OnClickListener {
    EditText et;
    Button btn_cancel;
    Button btn_confirm;
    OnEnsureListener onEnsureListener;

    //通过setOnEnsureListener进行设定 这是设定回调接口的方法
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    //(BeizhuDialog)
    public NotesDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //与dialog_note所链接
        setContentView(R.layout.dialog_note);//设置对话框显示布局
        et = findViewById(R.id.et_dialog_note);
        btn_confirm = findViewById(R.id.btn_dialog_note_confirm);
        btn_cancel = findViewById(R.id.btn_dialog_note_cancel);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        et.setOnClickListener(this);


    }

    //写一个接口 表示确定
    public interface OnEnsureListener{
        public void onEnsure();

    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        if (id==R.id.btn_dialog_note_confirm) {
            //当onEnsureListener不为空，说明传入了接口对象
            if (onEnsureListener!=null) {
                onEnsureListener.onEnsure();
            }
        } else if (id == R.id.btn_dialog_note_cancel) {
            cancel();
        }
    }

    //获取输入数据的方法
    public String getEditText(){
       return et.getText().toString().trim();
    }

    //设置Dialog的尺寸 和屏幕尺寸一致
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
