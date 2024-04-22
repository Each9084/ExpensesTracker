package com.yichi.expenseTracker.fragment_record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yichi.tally.R;
import com.yichi.expenseTracker.database.DBManager;
import com.yichi.expenseTracker.bean.FeeTypeBean;

import java.util.List;

/**
 * 表示收入类别的 Fragment，继承自 expensesFragment
 */
public class IncomeFragment extends expensesFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState); // 使用父类的布局初始化
        loadDataToGridView(); // 加载数据
        return view;
    }

    @Override
    protected void loadDataToGridView() {
        super.loadDataToGridView(); // 调用父类方法以保持共通逻辑
        // 获取数据库当中的数据源，1 表示收入类型
        List<FeeTypeBean> inList = DBManager.getTypeList(1);
        typeList.clear(); // 清除旧数据
        typeList.addAll(inList); // 添加新数据
        adapter.notifyDataSetChanged(); // 通知适配器数据已经改变，更新UI显示

        // 如果需要，根据传入的参数初始化特定控件
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("Type")) {
            String type = bundle.getString("Type");
            tv_type.setText(type); // 设置类型文字
            int imageResourceId = getContext().getResources().getIdentifier(
                    "ic_" + type.toLowerCase(), "mipmap", getContext().getPackageName());
            iv_type.setImageResource(imageResourceId); // 设置图标
        } else {
            tv_type.setText(getString(R.string.Else));
            iv_type.setImageResource(R.mipmap.ic_else_in); // 默认图标
        }
    }

    @Override
    public void saveTransactionToDB() {
        transactionBean.setCategory(1); // 设置类别为收入
        DBManager.insertAnItemToTransaction(transactionBean); // 保存交易到数据库
    }
}
