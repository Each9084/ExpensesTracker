package com.yichi.expenseTracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.yichi.expenseTracker.adapter.RecordPagerAdapter;
import com.yichi.expenseTracker.fragment_record.IncomeFragment;
import com.yichi.expenseTracker.fragment_record.OutcomeFragment;
import com.yichi.tally.R;

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

        record_tbs = findViewById(R.id.record_tbs);
        vp_record = findViewById(R.id.vp_record);
        findViewById(R.id.iv_record_back).setOnClickListener(this);

        // 设置ViewPager加载页面
        initPager();
    }


    private void initPager() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        OutcomeFragment outcomeFragment = new OutcomeFragment();
        IncomeFragment incomeFragment = new IncomeFragment();

        // 将编辑数据传递给 Fragments
        if (getIntent().hasExtra("ID")) {
            Bundle bundle = new Bundle();
            bundle.putInt("ID", getIntent().getIntExtra("ID", -1));
            bundle.putFloat("Money", getIntent().getFloatExtra("Money", 0));
            bundle.putString("Type", getIntent().getStringExtra("Type"));
            bundle.putString("Note", getIntent().getStringExtra("Note"));
            bundle.putString("Date", getIntent().getStringExtra("Date"));
            bundle.putInt("Category", getIntent().getIntExtra("Category", 0));

            outcomeFragment.setArguments(bundle);
            incomeFragment.setArguments(bundle);
        }

        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);

        String[] titles = {getString(R.string.expenses), getString(R.string.income)};
        RecordPagerAdapter recordPagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        vp_record.setAdapter(recordPagerAdapter);
        record_tbs.setupWithViewPager(vp_record);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_record_back) {
            finish();
        }
    }
}
