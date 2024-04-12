package com.yichi.tally.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.yichi.tally.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context,"tally.db",null,1);
    }

    //只有项目第一次运行时,会被调用,创建数据库的方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表类型的表
        String sql = "CREATE TABLE typetb(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "typename VARCHAR(20),imageId INTEGER,selectImageId INTEGER,category INTEGER)";
        db.execSQL(sql);
        insertType(db);

        //创建记账表(accounttb)
        sql = "CREATE TABLE transactiontb(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "typename VARCHAR(20),selectImageId INTEGER,note VARCHAR(300)," +
                "money FLOAT,time VARCHAR(60),year INTEGER,month INTEGER," +
                "day INTEGER,category INTEGER)";
        db.execSQL(sql);
    }

    private void insertType(SQLiteDatabase db) {
        //向typedb表中插入表元素
        String sql = "INSERT INTO typetb (typename,imageId,selectImageId,category) VALUES (?,?,?,?)";
        db.execSQL(sql,new Object[]{"Else", R.mipmap.ic_qita,R.mipmap.ic_qita_fs,0});
        db.execSQL(sql,new Object[]{"Food", R.mipmap.ic_canyin,R.mipmap.ic_canyin_fs,0});
        db.execSQL(sql,new Object[]{"Shopping", R.mipmap.ic_jiaotong,R.mipmap.ic_jiaotong_fs,0});
        db.execSQL(sql,new Object[]{"Clothing", R.mipmap.ic_gouwu,R.mipmap.ic_gouwu_fs,0});
        db.execSQL(sql,new Object[]{"Traffic", R.mipmap.ic_fushi,R.mipmap.ic_fushi_fs,0});
        db.execSQL(sql,new Object[]{"Necessities", R.mipmap.ic_riyongpin,R.mipmap.ic_riyongpin_fs,0});
        db.execSQL(sql,new Object[]{"Amusement", R.mipmap.ic_yule,R.mipmap.ic_yule_fs,0});
        db.execSQL(sql,new Object[]{"Snack", R.mipmap.ic_lingshi,R.mipmap.ic_lingshi_fs,0});
        db.execSQL(sql,new Object[]{"Cigarettes", R.mipmap.ic_yanjiu,R.mipmap.ic_yanjiu_fs,0});
        db.execSQL(sql,new Object[]{"Alcohol", R.mipmap.ic_yanjiu,R.mipmap.ic_yanjiu_fs,0});
        db.execSQL(sql,new Object[]{"Study", R.mipmap.ic_xuexi,R.mipmap.ic_xuexi_fs,0});
        db.execSQL(sql,new Object[]{"Health", R.mipmap.ic_yiliao,R.mipmap.ic_yiliao_fs,0});
        db.execSQL(sql,new Object[]{"Residence", R.mipmap.ic_zhufang,R.mipmap.ic_zhufang_fs,0});
        db.execSQL(sql,new Object[]{"Utilities", R.mipmap.ic_shuidianfei,R.mipmap.ic_shuidianfei_fs,0});
        db.execSQL(sql,new Object[]{"Communication", R.mipmap.ic_tongxun,R.mipmap.ic_tongxun_fs,0});

        //收入category-1
        db.execSQL(sql,new Object[]{"Else", R.mipmap.ic_qita,R.mipmap.ic_qita_fs,1});
        db.execSQL(sql,new Object[]{"Salary", R.mipmap.in_xinzi,R.mipmap.in_xinzi_fs,1});
        db.execSQL(sql,new Object[]{"Bonus", R.mipmap.in_jiangjin,R.mipmap.in_jiangjin_fs,1});
        db.execSQL(sql,new Object[]{"Loan", R.mipmap.in_jieru,R.mipmap.in_jieru_fs,1});
        db.execSQL(sql,new Object[]{"Debt", R.mipmap.in_shouzhai,R.mipmap.in_shouzhai_fs,1});
        db.execSQL(sql,new Object[]{"Interest", R.mipmap.in_lixifuji,R.mipmap.in_lixifuji_fs,1});
        db.execSQL(sql,new Object[]{"Investment", R.mipmap.in_touzi,R.mipmap.in_touzi_fs,1});
        db.execSQL(sql,new Object[]{"Transaction", R.mipmap.in_ershoushebei,R.mipmap.in_ershoushebei_fs,1});
        db.execSQL(sql,new Object[]{"Windfall", R.mipmap.in_yiwai,R.mipmap.in_yiwai_fs,1});



    }

    //数据库版本发生改变时,会发生变化
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
