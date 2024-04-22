package com.yichi.expenseTracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yichi.expenseTracker.bean.TransactionBean;
import com.yichi.tally.R;

import java.util.Calendar;
import java.util.List;

//记账adapter 自动循环给ListView或者gridview从数据源中获取数据并绑定数据
//（AccountAdapter）
public class TransactionAdapter extends BaseAdapter {
    int year;
    int month;
    int day;
    Context context;
    List<TransactionBean>mDatas;
    //将这些 XML 布局文件实例化为相应的 View 对象，使得这些布局可以在应用程序中进行动态加载和显示。
    LayoutInflater inflater;

    public TransactionAdapter(Context context, List<TransactionBean> mdatas) {
        this.context = context;
        this.mDatas = mdatas;
        //context 是用于获取调用 TransactionAdapter 构造函数的 Activity 或 Fragment 的上下文对象
        inflater = LayoutInflater.from(context);//加载要添加的 View 的布局文件，并将其添加到现有布局中。

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //列表项的数量由数据源的大小决定。
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        //返回指定位置的对象 获取特定对象
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //因为复用 所以重写

        ViewHoder holder = null;
        //如果 convertView 为 null，表示当前没有可重用的视图，需要创建新的视图。
        if (convertView==null) {
            //将一个布局文件 R.layout.item_main_layout 实例化为一个视图对象 getView系统自动调用
            //未来谁调用getView会将当前页面或者父级视图作为 parent 参数传递给 LayoutInflater 的 inflate() 方法。
            convertView = inflater.inflate(R.layout.item_main_layout,parent,false);
            //ViewHolder在列表项视图对象中保存对子视图的引用，避免了在 getView() 方法中频繁调用 findViewById() 方法查找子视图
            /**从而提高了列表的性能和滑动的流畅度*/
            holder = new ViewHoder(convertView);
            //以便在以后可以通过getTag()取到该视图的 holder 对象
            //并将该 ViewHolder 对象与当前的列表项视图对象关联起来
            convertView.setTag(holder);
        }else{      //当前有可重用的视图
            //直接通过 convertView.getTag() 方法获取到（之前为空创建的setTag）该视图的 holder 对象，无需创建新的视图。
            holder = (ViewHoder) convertView.getTag();
        }
        //获取到这一条TransactionBean对象
        TransactionBean bean = mDatas.get(position);
        //然后通过holder依次设置 在holder中整合了findviewid操作，提高效率
        holder.iv_type.setImageResource(bean.getSelectImageId());
        holder.tv_type.setText(bean.getTypename());
        holder.tv_money.setText("£"+bean.getMoney());
        holder.tv_note.setText(bean.getNote());

        if (bean.getYear() == year&&bean.getMonth()== month&&bean.getDay()==day) {
            String splitTime = bean.getTime().split(" ")[3];//"yyyy'Year 'MM'Month' dd'Day' HH:mm"
            holder.tv_time.setText("Today "+splitTime);
        }else {
            holder.tv_time.setText(bean.getTime());
        }

        return convertView;
    }

    //辅助类 用来 存储列表项中的视图控件的引用。
    class ViewHoder{
        //写到外部类    是为了每个ViewHoder 对象都只会持有对应视图控件的引用，
        // 并且这些引用只会在构造函数中被初始化一次，避免了不必要的内存消耗。
        ImageView iv_type;
        TextView tv_type;
        TextView tv_note;
        TextView tv_time;
        TextView tv_money;

        //内部类
        public ViewHoder(View view){
            iv_type = view.findViewById(R.id.iv_item_main_layout);
            tv_type = view.findViewById(R.id.tv_item_main_layout_title);
            tv_note = view.findViewById(R.id.tv_item_main_layout_remark);
            tv_money = view.findViewById(R.id.tv_item_main_layout_money);
            tv_time = view.findViewById(R.id.tv_item_main_layout_time);
        }
    }
}
