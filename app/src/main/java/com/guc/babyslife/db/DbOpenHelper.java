package com.guc.babyslife.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.guc.babyslife.greendao.DaoMaster;
import com.guc.babyslife.greendao.GrowDataDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by guc on 2019/10/28.
 * 描述：GreenDao数据库升级工具类  实现升级后保留原来数据
 */
public class DbOpenHelper extends DaoMaster.DevOpenHelper {
    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //切记不要调用super.onUpgrade(db,oldVersion,newVersion)
        if (oldVersion < newVersion) {
            MigrationHelper.getInstance().migrate(db, GrowDataDao.class);
        }
    }
}
