package com.yichi.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yichi.tally.R;
import com.yichi.tally.adapter.TransactionAdapter;
import com.yichi.tally.database.DBManager;
import com.yichi.tally.database.TransactionBean;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv_search;//(searchLv)
    private EditText et_search;//(searchEt)
    private TextView tv_empty;//(emptyTv)

    List<TransactionBean> mDates; //数据源
    TransactionAdapter adapter;  //适配器对象
    private ImageView iv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ImageView iv_search_back = findViewById(R.id.iv_search_activity_back);
        iv_search = findViewById(R.id.iv_search_activity_search);

        iv_search.setOnClickListener(this);
        iv_search_back.setOnClickListener(this);

        initView();
        mDates = new ArrayList<>();
        adapter = new TransactionAdapter(this,mDates);
        lv_search.setAdapter(adapter);
        lv_search.setEmptyView(tv_empty); //设置无数据时，显示的空间
    }

    private void initView() {
        lv_search = findViewById(R.id.lv_search_activity);
        et_search = findViewById(R.id.et_search_activity);
        tv_empty = findViewById(R.id.tv_search_activity_empty);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_search_activity_back) {
            finish();
        } else if (id == R.id.iv_search_activity_search) {//执行搜索操作
            //得到搜索的信息，trim去除回车 空格 (msg)
            String searchNoteResult = et_search.getText().toString().trim();
            //判断内容是否为空
            if (TextUtils.isEmpty(searchNoteResult)) {
                Toast.makeText(this,"The input content cannot be empty",Toast.LENGTH_SHORT).show();
                return;
            }
            //开始搜索
            List<TransactionBean> list = DBManager.searchByNote(searchNoteResult);
            mDates.clear();
            mDates.addAll(list);
            adapter.notifyDataSetChanged();

        }
    }
}