package com.yichi.tally;

import android.app.Application;

import com.yichi.tally.database.DBManager;

/**
 * 表示全局应用的类
 * UniteApp
 * **/
public class GlobalApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DBManager.initDB(getApplicationContext());
    }
}
