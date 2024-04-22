package com.yichi.expenseTracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yichi.expenseTracker.bean.BarChartBean;
import com.yichi.expenseTracker.bean.CategoryItemBean;
import com.yichi.expenseTracker.bean.FeeTypeBean;
import com.yichi.expenseTracker.bean.TransactionBean;
import com.yichi.expenseTracker.utils.CalculateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *负责管理数据库的类
 * 主要对于表中的内容进行操作，增删改查
 *
 * 未来把private static final String TRANSACTION_TABLE_NAME = "transactiontb";设置为常量
 * **/
public class DBManager {
    private static SQLiteDatabase db;
    //初始化数据库对象
    public  static  void initDB(Context context){
        //得到帮助类对象
        DBOpenHelper helper = new DBOpenHelper(context);
        //得到数据库对象
        db = helper.getWritableDatabase();
    }
    //获取特定类别（category）的费用类型数据 用于gridview判断但是收入还是支出 读取数据库当中的数据，写入内存集合里
    //category（kind）:表示收入或者支出
    public static List<FeeTypeBean>getTypeList(int category){
        List<FeeTypeBean>list = new ArrayList<>();
        //读取typetb表中的数据
        String sql = "SELECT * FROM typetb WHERE category = " + category;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            //可能会出问题p7 30:47
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"));
            int selectImageId = cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId"));
            int category1 = cursor.getInt(cursor.getColumnIndexOrThrow("category"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            //！！！kind或者kind1 32:10 p6
            FeeTypeBean feeTypeBean = new FeeTypeBean(id, typename, imageId, selectImageId, category1);
            list.add(feeTypeBean);
        }
        cursor.close();
        return list;
    }

    //向记账表当中插入一条元素 DBOpenHelper   (insertItemToAccounttb)
    public static void insertAnItemToTransaction(TransactionBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("selectImageId",bean.getSelectImageId());
        values.put("note",bean.getNote());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("category",bean.getCategory());
        db.insert("transactiontb",null,values);
        Log.i("animee","insertAnItemToTransaction ok!");
    }
    /**
     * 获取记账表当中某一天的所有支出或者收入情况
     *(getAccountListOneDayFromAccounttb)
     * **/
    public static List<TransactionBean>getListOneDayFromTransaction(int year,int month,int day){
        List<TransactionBean>list = new ArrayList<>();

        String sql="SELECT * FROM transactiontb WHERE year=? AND month=? AND day=? ORDER BY id DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int selectImageId = cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId"));
            int category = cursor.getInt(cursor.getColumnIndexOrThrow("category"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));

            TransactionBean bean = new TransactionBean(id,typename,selectImageId,note,money,time,year,month,day,category);
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取某一天的支出或者收入的总金额 传入category（kind） 支出==0 收入==1
     * */
    public static float getSumMoneyOneDay(int year,int month,int day,int category){
        float total = 0.0f;
        String sql = "SELECT SUM(money) FROM transactiontb WHERE year=? AND month=? AND day=? AND category=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", category + ""});
        //因为如果有，只会有一个结果，不需要while
        if (cursor.moveToFirst()) {
            //resultMoney(money)
            float resultMoney = cursor.getFloat(cursor.getColumnIndexOrThrow("SUM(money)"));
            total = resultMoney;
        }
        cursor.close();
        return total;
    }

    /**
     * 获取某一月的支出或者收入的总金额 传入category（kind） 支出==0 收入==1
     * */
    public static float getSumMoneyOneMonth(int year,int month,int category){
        float total = 0.0f;
        String sql = "SELECT SUM(money) FROM transactiontb WHERE year=? AND month=? AND category=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", category + ""});
        //因为如果有，只会有一个结果，不需要while
        if (cursor.moveToFirst()) {
            //resultMoney(money)
            float resultMoney = cursor.getFloat(cursor.getColumnIndexOrThrow("SUM(money)"));
            total = resultMoney;
        }
        cursor.close();
        return total;
    }

    /**
     * 统计某月 支出 或者收入情况有多少条 传入category（kind） 支出==0 收入==1
     * （getCountItemOneMonth）
     * */
    public static int getNumberOfItemOneMonth(int year,int month,int category){
        //（total）
        int allResult=0;
        String sql = "SELECT COUNT(money) FROM transactiontb WHERE year=? AND month=? AND category=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", category + ""});
        if (cursor.moveToFirst()) {
            int number = cursor.getInt(cursor.getColumnIndexOrThrow("COUNT(money)"));
            allResult = number;
        }
    return allResult;

    }

    /**
     * 获取某一年的支出或者收入的总金额 传入category（kind） 支出==0 收入==1
     * */
    public static float getSumMoneyOneYear(int year,int category){
        float total = 0.0f;
        String sql = "SELECT SUM(money) FROM transactiontb WHERE year=? AND category=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "",category + ""});
        //因为如果有，只会有一个结果，不需要while
        if (cursor.moveToFirst()) {
            //resultMoney(money)
            float resultMoney = cursor.getFloat(cursor.getColumnIndexOrThrow("SUM(money)"));
            total = resultMoney;
        }
        cursor.close();
        return total;
    }

    /**
     * 根据传入的id删除transactiontb中的一条数据
     * (deleteItemFromAccounttbById)
     * **/
    public  static int deleteItemByID(int id){
        int delete = db.delete("transactiontb", "id=?", new String[]{id + ""});
        return delete;
    }

    /**
     * 根据备注搜索收入或者支出的情况列表
     * (getAccountListByRemarkFromAccounttbById)
     * **/
    public static List<TransactionBean>searchByNote(String note){
        List<TransactionBean>list = new ArrayList<>();
        //模糊查询
        String sql = "SELECT * FROM transactiontb WHERE note LIKE '%"+note+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int selectImageId = cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId"));
            int category = cursor.getInt(cursor.getColumnIndexOrThrow("category"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));

            String originalNote = cursor.getString(cursor.getColumnIndexOrThrow("note"));
            TransactionBean bean = new TransactionBean(id,typename,selectImageId,originalNote,money,time,year,month,day,category);
            list.add(bean);
        }
        return list;

    }
    /**
     * 获取记账表当中某一月的所有支出或者收入情况
     *(getAccountListOneMonthFromAccounttb)
     * **/
    public static List<TransactionBean>getListOneMonthFromTransaction(int year,int month){
        List<TransactionBean>list = new ArrayList<>();

        String sql="SELECT * FROM transactiontb WHERE year=? AND month=? ORDER BY id ASC";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int selectImageId = cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId"));
            int category = cursor.getInt(cursor.getColumnIndexOrThrow("category"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            TransactionBean bean = new TransactionBean(id,typename,selectImageId,note,money,time,year,month,day,category);
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    /**
     * 查询记账的表当中 有几个年份信息(getYearListFromAccounttb)
     * **/
    public static List<Integer>getYearList(){
        List<Integer>list = new ArrayList<>();
        String sql = "SELECT DISTINCT(year) FROM transactiontb ORDER BY year ASC";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            list.add(year);
        }


        return list;
    }

    /**
     * 删除transactiontb表格中的所有数据
     *
     * **/
    public static void deleteAll(){
        String sql = "DELETE FROM transactiontb";
        db.execSQL(sql);
    }
    /**
     *  查询指定年份和月份收支 每一种类型额总钱数
     *(getChartListFromAccounttb)
     * **/

    public static List<CategoryItemBean>getCategoryMoneyByYearAndMonth(int year, int month, int category){
        List<CategoryItemBean>list = new ArrayList<>();
        //求出支出 或者收入的总钱数
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, category);
        //某一种(total)
        String sql ="SELECT typename,selectImageId,SUM(money)AS allamount FROM transactiontb " +
                "WHERE year=? AND month=? AND category=? GROUP BY typename ORDER BY allamount DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", category + ""});
        while (cursor.moveToNext()) {
            int selectImageId = cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            float allamount = cursor.getFloat(cursor.getColumnIndexOrThrow("allamount"));
            //计算所占百分比 (total) (ratio)/sumMonth
            float proprietary = CalculateUtil.proportionCalculation(allamount,sumMoneyOneMonth);
            CategoryItemBean categoryItemBean = new CategoryItemBean(selectImageId, typename, proprietary, allamount);
            list.add(categoryItemBean);

        }

        return list;
    }

    /**
     *获取这个月当中某一天收入支出最大的金额 (getMaxMoneyOneDayInMonth)
     * 待升级 直接return
     * **/
    public static float getMaxMoneyDayOfMonth(int year,int month,int category){
        String sql = "SELECT SUM(money) FROM transactiontb WHERE year=? AND month=? AND category=? " +
                "GROUP BY day ORDER BY SUM(money) DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", category + ""});
        if (cursor.moveToFirst()) {
            float money =  cursor.getFloat(cursor.getColumnIndexOrThrow("SUM(money)"));
            return money;
        }
        return 0;
    }


    /**根据指定月份每一天 收入和支出 的总钱数的集合(getSumMoneyOneDayInMonth)
     * 是底下各种类的统计
     * */
    public static List<BarChartBean>getSumAmountOneDayOfMonth(int year, int month, int category){
        String sql = "SELECT day,SUM(money) FROM transactiontb WHERE year=? AND month=? AND category=? " +
                "GROUP BY day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", category + ""});
        List<BarChartBean>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            float sumMoney = cursor.getFloat(cursor.getColumnIndexOrThrow("SUM(money)"));
            BarChartBean barChartBean = new BarChartBean(year, month, day, sumMoney);
            list.add(barChartBean);
        }

        return list;
    }

    //新加的
    public static void updateTransactionInDB(int id, TransactionBean bean) {
        ContentValues values = new ContentValues();
        values.put("money", bean.getMoney());
        values.put("note", bean.getNote());
        values.put("typename", bean.getTypename());
        values.put("time", bean.getTime());
        values.put("category", bean.getCategory());
        // 更新条件
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };
        db.update("transactiontb", values, selection, selectionArgs);
    }

}
