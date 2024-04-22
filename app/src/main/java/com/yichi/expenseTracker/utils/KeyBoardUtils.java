package com.yichi.expenseTracker.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.yichi.tally.R;

public class KeyBoardUtils {
    private final Keyboard k1;//自定义键盘
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnsureListener {
        public void onEnsure();
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        editText.setInputType(InputType.TYPE_NULL); //取消弹出系统键盘
        k1 = new Keyboard(this.editText.getContext(), R.xml.keyboard);

        this.keyboardView.setKeyboard(k1);//设置要显示键盘的样式
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener);//设置键盘按钮点击了监听
    }

    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE:
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL:
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE:

                    onEnsureListener.onEnsure();//通过接口回调的方法，当点击确定时，可以调用这个方法

                    break;
                default:
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    //显示键盘的方法
    public void showKeyBoard(){
        int visibility = keyboardView.getVisibility();
        if (visibility== View.INVISIBLE || visibility == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);

        }
    }

    //隐藏
    public void hideKeyBoard(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE||visibility==View.INVISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }

}
