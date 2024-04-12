package com.yichi.tally.fragment_record;

import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yichi.tally.R;
import com.yichi.tally.database.DBManager;
import com.yichi.tally.database.FeeTypeBean;
import com.yichi.tally.database.TransactionBean;
import com.yichi.tally.utils.KeyBoardUtils;
import com.yichi.tally.utils.NotesDialog;
import com.yichi.tally.utils.TimePickerDialog;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录页面中的支出模块
 * 记一笔的fragment (BaseRecordFragment)
 * 负责管理OutcomeFragment和IncomeFragment 是他们的父类 有这两个fragment共同的地方 便于代码复用性
 */
public abstract class expensesFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText et_money;//会引起钱数变化
    ImageView iv_type;//选中的是那个图片 左上角else
    TextView tv_type;//选中的是哪个类型 else图片旁边的文字
    TextView tv_notes;//左下角的备注 tv_fragment_record_notes
    TextView tv_time;//右下角的时间
    GridView gv_type;//多种类型 主体item部分
    List<FeeTypeBean>typeList;//未来数据更新？
    TypeBaseAdapter adapter;//管理 GridView 控件的数据展示
    TransactionBean transactionBean;//AccountBean 将需要插入到记账本当中的数据保存成对象的形式 Java Bean。

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionBean = new TransactionBean();//初始化对象 利用其进行后续操作
        transactionBean.setTypename("Else");
        transactionBean.setSelectImageId(R.mipmap.ic_qita_fs);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        initView(view);
        //设置初始化当前时间
        setInitTime();
        //给GridView填充数据的方法
        loadDataToGridView();
        //设置GridView每一个item的点击事件，可以点击其他图标了
        setGridViewListener();
        return view;
    }

    //获取当前时间 显示在 tv_time 上
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'Year 'MM'Month' dd'Day' HH:mm");
        String time = sdf.format(date);
        tv_time.setText(time);
        transactionBean.setTime(time);//设置当前时分秒

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        transactionBean.setYear(year);
        transactionBean.setMonth(month);
        transactionBean.setDay(day);

    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.keyboard_fragment_record);
        et_money = view.findViewById(R.id.et_fragment_record_money);
        iv_type = view.findViewById(R.id.iv_fragment_record_else);
        gv_type = view.findViewById(R.id.gv_fragment_record);
        tv_type = view.findViewById(R.id.tv_fragment_record_type);//选中的是哪个类型，左上角else图标旁边的文字
        tv_notes = view.findViewById(R.id.tv_fragment_record_notes);
        tv_time = view.findViewById(R.id.tv_fragment_record_time);
        gv_type = view.findViewById(R.id.gv_fragment_record);

        tv_notes.setOnClickListener(this);
        tv_time.setOnClickListener(this);

        //让自定义软键盘显示出来
        KeyBoardUtils keyBoardUtils = new KeyBoardUtils(keyboardView, et_money);
        keyBoardUtils.showKeyBoard();
        //设置接口,点击了这个键盘确定按钮 监听确定按钮被点击了
        keyBoardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //点击了确定
                //获取输入钱数
                //R.id.et_fragment_record_money
                String moneyStr = et_money.getText().toString();
                if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                transactionBean.setMoney(money);
                //获取记录的信息,保存到数据库当中
                saveTransactionToDB(); //saveAccountToDB
                //然后返回上一级页面
                getActivity().finish();
            }
        });
    }

    //让子类强制重写该方法
    public abstract void saveTransactionToDB();

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_fragment_record_notes) {
            //弹出备注的对话框(showBZDialog)
            showNoteDialog();
        } else if (id == R.id.tv_fragment_record_time) {
            showTimeDialog();
        }

    }

    //弹出显示时间的对话框
    private void showTimeDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext());
        timePickerDialog.show();
        //设定确定按钮被点击了的监听器
        //按钮点击后的操作，用来设置右下角显示时间
        timePickerDialog.setOnEnsureListener(new TimePickerDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                tv_time.setText(time);
                transactionBean.setYear(year);
                transactionBean.setMonth(month);
                transactionBean.setDay(day);
            }
        });

    }

    //弹出备注的对话框(shwoBeizhuDialog beizhuDialog)
    public void showNoteDialog() {
        NotesDialog dialog = new NotesDialog(getContext());
        dialog.show();
        dialog.setDialogSize();

        //设置按钮被点击了
        dialog.setOnEnsureListener(new NotesDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //（msg）就可以得到我们想要的数据了
                String text = dialog.getEditText();
                if (!TextUtils.isEmpty(text)) {
                    tv_notes.setText(text);
                    transactionBean.setNote(text);
                }
                dialog.cancel();

            }
        });
    }

    //给GridView填充数据的方法
    public void loadDataToGridView() {
        //存储需要显示在 GridView 中的费用类型数据
        typeList = new ArrayList<>();
        //适配器 adapter是一个适配器，用来管理GridView里的数据 用于将数据源 typeList 中的每一条数据绑定到 GridView 中的每个条目上
        //在创建 TypeBaseAdapter 对象时，通过构造函数将数据源 typeList 传入，以便适配器可以访问数据源中的数据。
        adapter = new TypeBaseAdapter(getContext(), typeList);
        gv_type.setAdapter(adapter);
    }

    //设置GridView每一个item的点击事件，可以点击其他图标了
    private void setGridViewListener() {
        //setOnItemClickListener 单个条目响应事件
        gv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取图片位置
                adapter.selectPosition = position;
                adapter.notifyDataSetChanged();//提示：绘制发生变化了
                //点某个item，上方金额左边的标志切换到对应item图标
                //FeeTypeBean 应该是用于表示数据库中的一条记录的实体类 feeTypeBean表示从数据源中获取到的某一条记录对应的实体对象
                //使用 feeTypeBean 来设置 GridView 中每个条目所显示的内容
                FeeTypeBean feeTypeBean = typeList.get(position);//获取对应位置的对象  从数据源 typeList 中获取了指定位置 position 处的对象
                // 并赋给feeTypeBean，我们只需要一个对象进行操作
                //设置左上角选中文字变化
                String typename = feeTypeBean.getTypename();
                tv_type.setText(typename);
                transactionBean.setTypename(typename);//设置记录transaction信息为typename
                //设置左上角选中图标变化
                int selectImageId = feeTypeBean.getSelectImageId();
                iv_type.setImageResource(selectImageId);
                transactionBean.setSelectImageId(selectImageId);

            }
        });
    }




}