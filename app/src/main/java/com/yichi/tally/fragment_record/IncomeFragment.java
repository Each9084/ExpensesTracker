package com.yichi.tally.fragment_record;

import com.yichi.tally.R;
import com.yichi.tally.database.DBManager;
import com.yichi.tally.database.FeeTypeBean;

import java.util.List;

/**
 *
 */
public class IncomeFragment extends expensesFragment {

    @Override
    public void loadDataToGridView() {
        super.loadDataToGridView();
        //获取数据库当中的数据源
        List<FeeTypeBean> inList = DBManager.getTypeList(1);//inlist 1是收入
        typeList.addAll(inList);
        //通知适配器数据已经改变，需要更新UI显示的方法。
        adapter.notifyDataSetChanged();
        tv_type.setText("其他");
        iv_type.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveTransactionToDB() {
        transactionBean.setCategory(1);//收入是1
        DBManager.insertAnItemToTransaction(transactionBean);

    }
}