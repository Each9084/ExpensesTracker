package com.yichi.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.yichi.tally.adapter.RecordPagerAdapter;
import com.yichi.tally.fragment_record.OutcomeFragment;
import com.yichi.tally.fragment_record.expensesFragment;
import com.yichi.tally.fragment_record.IncomeFragment;

import java.util.ArrayList;

/**
 * 记一笔 页面
 * **/
public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout record_tbs;
    private ViewPager vp_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //通过 RecordPagerAdapter 类的 getPageTitle() 方法来设置每个页面的标题
        record_tbs = findViewById(R.id.record_tbs);
        vp_record = findViewById(R.id.vp_record);

        findViewById(R.id.iv_record_back).setOnClickListener(this);

        //设置ViewPager加载页面
        initPager();
    }

    private void initPager() {
        //初始化ViewPager
        ArrayList<Fragment>fragmentList = new ArrayList<>();
        //创建收入和支出页面，放置在fragment当中
        //expensesFragment expensesFragment = new expensesFragment();//支出OutcomeFragment
        OutcomeFragment outcomeFragment = new OutcomeFragment();
        IncomeFragment incomeFragment = new IncomeFragment();
        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);

        //创建适配器
        RecordPagerAdapter recordPagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        //设置适配器 设置titles 【expenses income】
        vp_record.setAdapter(recordPagerAdapter);
        //将TabLayout和ViewPager进行关联 滑动上部导航栏【支出】会使viewpager滑动改变里面的gridview 实现它们之间的联动效果。
        record_tbs.setupWithViewPager(vp_record);

    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        if (id==R.id.iv_record_back) {
            finish();
        }
    }
}