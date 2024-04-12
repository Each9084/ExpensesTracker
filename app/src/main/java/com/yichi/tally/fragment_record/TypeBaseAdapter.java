package com.yichi.tally.fragment_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.LayoutInflaterCompat;

import com.yichi.tally.R;
import com.yichi.tally.database.FeeTypeBean;

import java.util.List;

//自定义的适配器类，用于将数据绑定到 GridView 中的视图上。
public class TypeBaseAdapter extends BaseAdapter {

    Context context;
    //“数据源”,通常指代存储或提供数据的来源
    List<FeeTypeBean>mData;//mDatas
    int selectPosition = 0;//selectPos 选中位置 标识当前选中的条目在数据源中的位置。

    //通过构造方法进行传递

    public TypeBaseAdapter(Context context, List<FeeTypeBean> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }
    //返回指定未知的数据源
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //这个适配器，不考虑复用问题，因为所有item都显示在界面上，不会因为滑动而消失，所以没有多余的convertView
    //不用复写

    // 自己重写的
    //为每个 GridView 条目创建视图，并将数据源中的相应数据填充到视图中。
    //只要设置了适配器与GridView和ListView关联起来 就会被自动调用
    /**
     * convertView 的作用是为了利用滚动列表的性能优化，通过重用已经存在的视图对象，避免反复创建新的视图。当列表滚动时，旧的视图会被传递给 getView() 方法
     * 如果该视图可用（非空），我们可以直接重用它，并更新其中的数据，而不必重新创建一个新的视图。
     * **/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //position 表示当前条目在适配器数据集中的位置，convertView 是可复用的旧视图
        // parent 是最终将要附加到的父视图（例如 GridView）。
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfragment_gv,parent,false);
        //查找布局当中的控件
        ImageView iv= convertView.findViewById(R.id.iv_item_recordfragment);
        TextView tv= convertView.findViewById(R.id.tv_item_recordfragment);
        //获取指定位置数据源,,然后将该数据的信息设置到相应的 ImageView 和 TextView 中，以便在 GridView 中显示。
        FeeTypeBean feeTypeBean = mData.get(position);
        tv.setText(feeTypeBean.getTypename());
        //如果选定的是当前位置 判断当前位置是否为选中位置，如果是选中位置，就设置为带颜色的图片，否则为灰色
        if (selectPosition == position) {
            iv.setImageResource(feeTypeBean.getSelectImageId());
        }else {
            iv.setImageResource(feeTypeBean.getImageId());
        }
        return convertView;
    }
}
