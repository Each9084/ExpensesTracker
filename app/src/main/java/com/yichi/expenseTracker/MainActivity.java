package com.yichi.expenseTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yichi.expenseTracker.adapter.TransactionAdapter;
import com.yichi.expenseTracker.database.DBManager;
import com.yichi.expenseTracker.bean.TransactionBean;
import com.yichi.expenseTracker.dialog.BudgetDialog;
import com.yichi.expenseTracker.dialog.SidebarDialog;
import com.yichi.tally.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lv_main; //展示今日收支 （todayLv）
    ImageView iv_search;
    Button btn_edit;//start
    ImageButton ib_more;

    //声明数据源 数据源是从数据库中查询得到的数据的临时存储 避免频繁地访问数据库
    List<TransactionBean> mDatas;
    TransactionAdapter transactionAdapter;

    int year, month, day;
    //头布局相关控件
    View headerView;
    TextView tv_topOut;
    TextView tv_topIn;
    TextView tv_topBudget;
    TextView tv_topCondition;
    ImageView iv_topShowHide;
    Boolean isShow = true;

    //持久化SharedPreferences
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_btn_edit).setOnClickListener(this);//start按钮
        findViewById(R.id.main_btn_sidebar).setOnClickListener(this);//more按钮
        findViewById(R.id.main_iv_search).setOnClickListener(this);//search按钮

        initListView();//(initView)

        //添加ListView到头布局(addLVHeaderView)
        addListViewHeaderView();
        //目前只是拿到了当前时间的实例
        initTime();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        mDatas = new ArrayList<>();

        //设置适配器：加载每一行到列表中
        transactionAdapter = new TransactionAdapter(this, mDatas);
        lv_main.setAdapter(transactionAdapter);
        //loadDBDate(); 要在最后运行 不然会空指针导致异常 onResume就是最后运行的

        //AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("zh"));

    }

    private void initListView() {
        lv_main = findViewById(R.id.lv_main);//找到今日收支的ListView （todayLv）
        btn_edit = findViewById(R.id.main_btn_edit); // 初始化按钮
        ib_more = findViewById(R.id.main_btn_sidebar); // 初始化按钮
        iv_search = findViewById(R.id.main_iv_search); // 初始化按钮

        // 点击事件的
        btn_edit.setOnClickListener(this);
        ib_more.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        //长按删除
        setLVLongClickListener();
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // 头部视图不响应
                    return;
                }
                // 调整位置索引，因为有头布局
                int listPos = position - lv_main.getHeaderViewsCount();
                TransactionBean selectedItem = mDatas.get(listPos);
                // 启动编辑活动
                startEditActivity(selectedItem);
            }
        });

    }

    private void startEditActivity(TransactionBean transaction) {
        Intent editIntent = new Intent(this, RecordActivity.class);
        // 传递需要编辑的数据
        editIntent.putExtra("ID", transaction.getId());
        editIntent.putExtra("Money", transaction.getMoney());
        editIntent.putExtra("Type", transaction.getTypename());
        editIntent.putExtra("Note", transaction.getNote());
        editIntent.putExtra("Date", transaction.getTime());
        editIntent.putExtra("Category", transaction.getCategory());
        startActivity(editIntent);
    }


    //设置ListView的长按事件
    private void setLVLongClickListener() {
        lv_main.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {// 当点击的是头布局
                    return false;
                }
                int pos = position - 1;
                mDatas.get(pos);
                TransactionBean clickBean = mDatas.get(pos);//获取正在被点击的这条信息
                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    //弹出是否删除某一条记录的对话框
    private void showDeleteItemDialog(final TransactionBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.warning)).setMessage(getString(R.string.warning_content_delete_all_record1)+"\n"
                        +getString(R.string.warning_content_delete_all_record2))
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.iconfirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int clickId = clickBean.getId();
                        //执行删除操作
                        DBManager.deleteItemByID(clickId);
                        mDatas.remove(clickBean);//移除集合当中点击的对象
                        transactionAdapter.notifyDataSetChanged();//提示适配器更新数据
                        setTopTextViewShow();//刷新顶部tv
                    }
                });
        builder.create().show();//显示对话框
    }


    //给ListView添加头布局的方法
    private void addListViewHeaderView() {
        //将布局转换为View对象
        headerView = getLayoutInflater().inflate(R.layout.item_main_layout_top, null);
        findViewById(R.id.tv_item_main_layout_top_tv1);
        //在这里将单独的头布局插进去
        lv_main.addHeaderView(headerView);

        tv_topOut = headerView.findViewById(R.id.tv_item_main_layout_top_out);
        tv_topIn = headerView.findViewById(R.id.tv_item_main_layout_top_in);
        tv_topBudget = headerView.findViewById(R.id.tv_item_main_layout_top_budget);
        tv_topCondition = headerView.findViewById(R.id.tv_item_main_layout_top_todayMoney);
        iv_topShowHide = headerView.findViewById(R.id.tv_item_main_layout_top_hide);

        tv_topBudget.setOnClickListener(this);
        headerView.setOnClickListener(this);
        iv_topShowHide.setOnClickListener(this);
    }

    //获取今日的具体时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //当activity获取焦点是，会调用方法，当完成记一笔返回到主页时 更新数据
    @Override
    protected void onResume() {
        super.onResume();
        loadDBDate();
        //设置顶部布局的显示(setTopTvShow)
        setTopTextViewShow();
    }

    //设置头布局当中文本内容的显示
    private void setTopTextViewShow() {
        //获取今日收入和支出总金额，显示到view(outcomeOneDay)
        float expensesOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        //今天的信息
        String infoOneDay = getString(R.string.today_expenses_is)+getString(R.string.gbp) + expensesOneDay
                + getString(R.string.today_income_is)+ getString(R.string.gbp) + incomeOneDay;
        //设置底部tv

        tv_topCondition.setText(infoOneDay);
        //获取本月
        float expensesOnMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        float incomeOnMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        tv_topIn.setText(getString(R.string.gbp) + incomeOnMonth);
        tv_topOut.setText(getString(R.string.gbp) + expensesOnMonth);

        //设置显示预算剩余(bmoney) 这里是start一笔后 resume期间更新tv
        float bMoney = preferences.getFloat("budgetMoney", 0);//获取到预算
        if (bMoney == 0) {
            tv_topBudget.setText(getString(R.string.gbp)+" 0");
        } else {
            float remainingBudget = bMoney - expensesOnMonth;
            tv_topBudget.setText(getString(R.string.gbp) + remainingBudget);
        }

    }

    //只会获得今天的时间 因为initTime里只是获取了今天的时间
    private void loadDBDate() {
        List<TransactionBean> list = DBManager.getListOneDayFromTransaction(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        transactionAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.main_btn_edit) {
            //跳转界面，传递数据
            Intent it_record = new Intent(this, RecordActivity.class);
            startActivity(it_record);
            //侧边栏
        } else if (id == R.id.main_btn_sidebar) {
            showSideBar();
        } else if (id == R.id.main_iv_search) {
            //跳转到搜索界面，传递数据
            Intent it_search = new Intent(this, SearchActivity.class);
            startActivity(it_search);
        } else if (id == R.id.tv_item_main_layout_top_budget) {
            showBudgetDialog();
        } else if (id == R.id.tv_item_main_layout_top_hide) {
            //切换textview显示隐藏(toggleShow)
            switchDisplayStatus();
        }

        if (v == headerView) {
            //头布局被点击
            Intent intent = new Intent();
            intent.setClass(this, monthlyChartActivity.class);
            startActivity(intent);
        }
    }

    //弹出侧边栏
    private void showSideBar() {
        SidebarDialog sidebarDialog = new SidebarDialog(this);
        sidebarDialog.show();
        sidebarDialog.setDialogSize();

    }

    //显示预算设置对话框
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //将预算金额写入到共享参数中
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("budgetMoney", money);
                editor.commit();

                //计算剩余金额
                float expensesOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float remainingBudget = money - expensesOneMonth;
                tv_topBudget.setText(getString(R.string.gbp) + remainingBudget);
            }
        });
    }

    //点击眼睛图标 对三个textView进行隐藏/显示 切换textview显示隐藏(toggleShow)
    private void switchDisplayStatus() {
        //显示转隐藏
        if (isShow) {
            //(passwordMethod)
            PasswordTransformationMethod display = PasswordTransformationMethod.getInstance();
            tv_topIn.setTransformationMethod(display);
            tv_topOut.setTransformationMethod(display);
            // tv_topCondition.setTransformationMethod(display);
            tv_topBudget.setTransformationMethod(display);
            iv_topShowHide.setImageResource(R.mipmap.ih_hide);
            isShow = false;  // 设置标志位隐藏
        } else {//隐藏转显示
            HideReturnsTransformationMethod mask = HideReturnsTransformationMethod.getInstance();
            tv_topIn.setTransformationMethod(mask);
            tv_topOut.setTransformationMethod(mask);
            // tv_topCondition.setTransformationMethod(mask);
            tv_topBudget.setTransformationMethod(mask);
            iv_topShowHide.setImageResource(R.mipmap.ih_show);
            isShow = true;  // 设置标志位隐藏
        }
    }
}