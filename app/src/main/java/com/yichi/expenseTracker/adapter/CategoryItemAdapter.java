package com.yichi.expenseTracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yichi.expenseTracker.bean.CategoryItemBean;
import com.yichi.expenseTracker.utils.CalculateUtil;
import com.yichi.tally.R;

import java.util.List;

//账单详情页面，ListView的适配器(ChartItemAdapter)
public class CategoryItemAdapter extends BaseAdapter {

    Context context;
    List<CategoryItemBean> mDatas;
    LayoutInflater layoutInflater;

    public CategoryItemAdapter(Context context, List<CategoryItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        //下面用就可以直接用了
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_chart_fragment_listview, parent, false);
            //减少findByid的次数
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //获取显示内容
        CategoryItemBean categoryItemBean = mDatas.get(position);
        //设置得到的种类图片
        viewHolder.iv_item.setImageResource(categoryItemBean.getSelectImageId());
        viewHolder.tv_category.setText(categoryItemBean.getCategory());

        //获取得到的百分比
        float proportion = categoryItemBean.getProportion();
        //(pert)
        String changeResult = CalculateUtil.changeToPercentage(proportion);
        viewHolder.tv_proportion.setText(changeResult);

        float totalAmount = categoryItemBean.getTotalAmount();
        String totalAmountText = context.getResources().getString(R.string.gbp) + totalAmount;
        viewHolder.tv_totalAmount.setText(totalAmountText);

        return convertView;
    }

    class ViewHolder {
        TextView tv_category;
        TextView tv_proportion;
        TextView tv_totalAmount;
        ImageView iv_item;

        /***
         * 待升级 未来改一下viewHolder的名字
         * ***/
        //减少findById的次数
        public ViewHolder(View v) {
            tv_category = v.findViewById(R.id.tv_category_item_chart_fragment_listview);
            tv_proportion = v.findViewById(R.id.tv_percent_item_chart_fragment_listview);
            tv_totalAmount = v.findViewById(R.id.tv_sum_money_item_chart_fragment_listview);
            iv_item = v.findViewById(R.id.iv_item_chart_fragment_listview);
        }
    }
}
