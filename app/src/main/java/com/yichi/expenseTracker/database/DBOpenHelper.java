package com.yichi.expenseTracker.database;

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

        String sql = "CREATE TABLE typetb(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "typename VARCHAR(20),imageId INTEGER,selectImageId INTEGER,category INTEGER)";
        db.execSQL(sql);
        insertType(db);

        sql = "CREATE TABLE transactiontb(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "typename VARCHAR(20),selectImageId INTEGER,note VARCHAR(300)," +
                "money FLOAT,time VARCHAR(60),year INTEGER,month INTEGER," +
                "day INTEGER,category INTEGER)";
        db.execSQL(sql);
    }

    private void insertType(SQLiteDatabase db) {
        //向typedb表中插入表元素
        String sql = "INSERT INTO typetb (typename,imageId,selectImageId,category) VALUES (?,?,?,?)";
        db.execSQL(sql,new Object[]{"Else", R.mipmap.ic_else_off,R.mipmap.ic_else_on,0});
        db.execSQL(sql,new Object[]{"Food", R.mipmap.ic_food_off,R.mipmap.ic_food_on,0});
        db.execSQL(sql,new Object[]{"Shopping", R.mipmap.ic_shopping_off,R.mipmap.ic_shopping_on,0});
        db.execSQL(sql,new Object[]{"Clothing", R.mipmap.ic_clothing_off,R.mipmap.ic_clothing_on,0});
        db.execSQL(sql,new Object[]{"Traffic", R.mipmap.ic_traffic_off,R.mipmap.ic_traffirc_on,0});
        db.execSQL(sql,new Object[]{"Necessities", R.mipmap.ic_necessities_off,R.mipmap.ic_necessities_on,0});
        db.execSQL(sql,new Object[]{"Amusement", R.mipmap.ic_amusement_off,R.mipmap.ic_amusement_on,0});
        db.execSQL(sql,new Object[]{"Snack", R.mipmap.ic_snack_off,R.mipmap.ic_snack_on,0});
        db.execSQL(sql,new Object[]{"Cigarettes", R.mipmap.ic_cigarettes_off,R.mipmap.ic_cigarettes_on,0});
        db.execSQL(sql,new Object[]{"Alcohol", R.mipmap.ic_alcohol_off,R.mipmap.ic_alcohol_on,0});
        db.execSQL(sql,new Object[]{"Study", R.mipmap.ic_study_off,R.mipmap.ic_study_on,0});
        db.execSQL(sql,new Object[]{"Health", R.mipmap.ic_health_off,R.mipmap.ic_health_on,0});
        db.execSQL(sql,new Object[] {"Residence", R.mipmap.ic_residence_off,R.mipmap.ic_residence_on,0});
        db.execSQL(sql,new Object[]{"Utilities", R.mipmap.ic_utilities_off,R.mipmap.ic_utilities_on,0});
        db.execSQL(sql,new Object[]{"Communication", R.mipmap.ic_communication_off,R.mipmap.ic_communication_on,0});

        //收入category-1
        db.execSQL(sql,new Object[]{"Else", R.mipmap.ic_else_off,R.mipmap.ic_else_in,1});
        db.execSQL(sql,new Object[]{"Salary", R.mipmap.ic_salary_off,R.mipmap.ic_salary_on,1});
        db.execSQL(sql,new Object[]{"Bonus", R.mipmap.ic_bouns_off,R.mipmap.ic_bouns_on,1});
        db.execSQL(sql,new Object[]{"Loan", R.mipmap.ic_loan_off,R.mipmap.ic_loan_on,1});
        //db.execSQL(sql,new Object[]{"Debt", R.mipmap.in_shouzhai,R.mipmap.in_shouzhai_fs,1});
        db.execSQL(sql,new Object[]{"Interest", R.mipmap.ic_interest_off,R.mipmap.ic_interest_on,1});
        db.execSQL(sql,new Object[]{"Investment", R.mipmap.ic_investment_off,R.mipmap.ic_investment_on,1});
        //db.execSQL(sql,new Object[]{"Transaction", R.mipmap.in_ershoushebei,R.mipmap.in_ershoushebei_fs,1});
        db.execSQL(sql,new Object[]{"Windfall", R.mipmap.ic_windfall_off,R.mipmap.ic_windfall_on,1});



    }

    //数据库版本发生改变时,会发生变化
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
