    package com.yichi.tally.utils;

    import android.app.Dialog;
    import android.content.Context;
    import android.os.Bundle;
    import android.text.Layout;
    import android.text.TextUtils;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.DatePicker;
    import android.widget.EditText;
    import android.widget.TimePicker;

    import androidx.annotation.NonNull;
    import androidx.viewpager.widget.ViewPager;

    import com.yichi.tally.R;

    //在记录页面弹出对话框 控制时间选择器 timepicker组件的类（SelectTimeDialog）
    public class TimePickerDialog extends Dialog implements View.OnClickListener {
        EditText et_hour;
        EditText et_minute;
        DatePicker datePicker;
        Button btn_cancel;
        Button btn_confirm;

        //(OnEnsureListener)
        public interface OnEnsureListener{
            public void onEnsure(String time,int year,int month,int day);
        }

        OnEnsureListener onEnsureListener;

        public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
            this.onEnsureListener = onEnsureListener;
        }

        public TimePickerDialog(@NonNull Context context) {
            super(context);

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_time);
            et_hour = findViewById(R.id.et_dialog_time_hour);
            et_minute = findViewById(R.id.et_dialog_time_minute);
            btn_cancel = findViewById(R.id.btn_dialog_time_cancel);
            btn_confirm = findViewById(R.id.btn_dialog_time_confirm);
            datePicker = findViewById(R.id.dp_dialog_time_calendar);

            btn_cancel.setOnClickListener(this);
            btn_confirm.setOnClickListener(this);

            hideDatePickerHeader();

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_dialog_time_cancel) {
                cancel();
            } else if (id == R.id.btn_dialog_time_confirm) {
                //选择的年份
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();
                String monthStr = String.valueOf(month);
                if (month<10) {
                    monthStr = "0"+month;
                    //int monthInteger = Integer.parseInt(monthStr);
                }
                String dayStr = String.valueOf(day);
                if (day<10) {
                    dayStr = "0"+day;
                }
                //获取输入的小时和分钟
                String hourStr = et_hour.getText().toString();
                String minuteStr = et_minute.getText().toString();
                int hour=0,minute=0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour=Integer.parseInt(hourStr);
                    hour = hour%24;
                }
                if (!TextUtils.isEmpty(minuteStr)) {
                    minute = Integer.parseInt(minuteStr);
                    minute = minute%60;
                }

                hourStr =String.valueOf(hour);
                minuteStr=String.valueOf(minute);

                if (hour<0) {
                    hourStr = "0"+hour;
                }

                if (minute<0){
                    minuteStr = "0"+minute;
                }

                String timeFormat = "Year:" + year + "Month:" +monthStr+"Day:"+dayStr+" "
                        +hourStr+":"+minuteStr;

                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(timeFormat,year,month,day);
                }
                cancel();
            }
        }

        //隐藏datepicker头布局
        private void hideDatePickerHeader() {
            //(ViewGroup)便于管理
            //basicVg(rootView)
            ViewGroup basicVg = (ViewGroup) datePicker.getChildAt(0);
            if (basicVg == null) {
                return;
            }
            View headerView = basicVg.getChildAt(0);
            //如果没有头布局 就不用执行
            if (headerView == null) {
                return;
            }
            /*//identifier(headerId) 5.0写法 不起作用
            int identifier = getContext().getResources().getIdentifier("day_picker_selector_layout", "id", "android");
            if (identifier == headerView.getId()) {
                headerView.setVisibility(View.GONE);
                //获取根布局的参数 进行设置 paramsView(layoutParamsRoot)
                ViewGroup.LayoutParams paramsViewBasic = basicVg.getLayoutParams();
                paramsViewBasic.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                //传入改变之后的view
                basicVg.setLayoutParams(paramsViewBasic);

                //后面依次向上 secondVp(animator)
                ViewGroup secondVg = (ViewGroup) basicVg.getChildAt(1);
                ViewGroup.LayoutParams paramsViewSecond = secondVg.getLayoutParams();
                paramsViewSecond.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                secondVg.setLayoutParams(paramsViewSecond);

                //得到他里面的第一个
                View child = secondVg.getChildAt(0);
                ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
                layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                child.setLayoutParams(layoutParamsChild);
                return;
            }*/


            //6.0写法 起作用

            int identifier = getContext().getResources().getIdentifier("date_picker_header", "id", "android");
            if (identifier == headerView.getId()){
                headerView.setVisibility(ViewGroup.GONE);
            }

        }
    }

