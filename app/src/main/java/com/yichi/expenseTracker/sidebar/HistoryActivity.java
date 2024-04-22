package com.yichi.expenseTracker.sidebar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yichi.expenseTracker.adapter.TransactionAdapter;
import com.yichi.expenseTracker.database.DBManager;
import com.yichi.expenseTracker.bean.TransactionBean;
import com.yichi.expenseTracker.dialog.CalendarDialog;
import com.yichi.tally.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv_history;
    private ImageView iv_back;
    private ImageView iv_calendar;
    private TextView tv_time;
    private TextView tv_info;
    private List<TransactionBean> mDatas;
    private TransactionAdapter transactionAdapter;
     int year;
     int month;

     //通过构造方法把这两个位置传递进来 ，就是记住之前选择的位置 避免下次进入还会重新生成，
     // 举例：gettimeinstance得到 现在是2020.9月，选择查看2019年2月的记录后，发现其实是3月，点击重选
    //发现又回到了9月 所以针对这个问题，在calendarDialog中进行创建，修改
     //dialog选中的年份的位置(dialogSelPos)
     int selectDialogYearPosition =-1;
    //dialog选中的年份的位置(dialogSelMonth)
     int selectDialogMonthPosition = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initView();
        mDatas = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(this,mDatas);
        lv_history.setAdapter(transactionAdapter);
        initTime();
        tv_time.setText(""+year+month);
        loadData(year,month);
        //设置ListView的长摁界面 删除界面 与主界面功能保持一致(setLVClickListener)
        setHistoryItemLongClickListener();
    }

    //设置ListView中每一个item的长摁删除事件
    private void setHistoryItemLongClickListener() {
        lv_history.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //获取 谁被点击了
                TransactionBean transactionBean = mDatas.get(position);
                //deleteItem
                deleteOneItem(transactionBean);
                return false;
            }
        });
    }

    private void deleteOneItem(TransactionBean transactionBean) {
        int id = transactionBean.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.warning)).setMessage(getString(R.string.warning_content_delete_all_record1))
                .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.iconfirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBManager.deleteItemByID(id);
                                //实时刷新，在数据库通过对应id删除后，
                                // 数据源TransactionBean transactionBean = mDatas.get(position);
                                // 获取了对应position的整个transactionBean对象
                                mDatas.remove(transactionBean);
                                transactionAdapter.notifyDataSetChanged();
                            }
                        });
            builder.create().show();

    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    //指定年份 月份 的收支列表
    private void loadData(int year,int month) {
        List<TransactionBean> list = DBManager.getListOneMonthFromTransaction(year, month);
        mDatas.clear();
        mDatas.addAll(list);
        transactionAdapter.notifyDataSetChanged();
    }

    private void initView() {
        lv_history = findViewById(R.id.lv_history_activity);

        iv_back = findViewById(R.id.iv_history_activity_back);
        iv_calendar = findViewById(R.id.iv_history_activity_calendar);
        /**待升级：感觉 time和info未来应该合在一起**/
        tv_time = findViewById(R.id.tv_history_activity_time);
        tv_info = findViewById(R.id.tv_history_activity_info);

        iv_back.setOnClickListener(this);
        iv_calendar.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_history_activity_back) {
            finish();
        } else if (id == R.id.iv_history_activity_calendar) {
            CalendarDialog dialog = new CalendarDialog(this,selectDialogYearPosition,selectDialogMonthPosition);
            dialog.show();
            dialog.setDialogSize();
            dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                //通过 接口 就会传回来位置，年，月
                @Override
                public void onRefresh(int selectPosition, int year, int month) {
                    tv_time.setText(year+" "+month);
                    loadData(year, month);
                    //有了年 月的记忆位置 就把对应的设置
                    selectDialogYearPosition =selectPosition;
                    selectDialogMonthPosition = month;
                }
            });
        }
    }


    /**待升级：未来封装一下 用的太多了**/

}