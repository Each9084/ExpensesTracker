package com.yichi.tally.fragment_record;

import android.util.Log;

import com.yichi.tally.R;
import com.yichi.tally.database.DBManager;
import com.yichi.tally.database.FeeTypeBean;
import com.yichi.tally.database.TransactionBean;

import java.util.List;

/**
 *
 */
public class OutcomeFragment extends expensesFragment {
    //重写
    @Override
    public void loadDataToGridView() {
        super.loadDataToGridView();
        //获取数据库当中的数据源
        List<FeeTypeBean> expenses = DBManager.getTypeList(0);//outlist 0是支出
        typeList.addAll(expenses);
        //通知适配器数据已经改变，需要更新UI显示的方法。
        adapter.notifyDataSetChanged();
        tv_type.setText(getString(R.string.Else));
        iv_type.setImageResource(R.mipmap.ic_else_on);
    }

    @Override
    public void saveTransactionToDB() {
        transactionBean.setCategory(0);//支出类别为0
        DBManager.insertAnItemToTransaction(transactionBean);
    }


}