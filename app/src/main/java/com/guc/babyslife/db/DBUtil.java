package com.guc.babyslife.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.guc.babyslife.greendao.DaoMaster;
import com.guc.babyslife.greendao.DaoSession;
import com.guc.babyslife.greendao.GrowDataDao;
import com.guc.babyslife.model.GrowData;

import java.util.List;

/**
 * Created by guc on 2019/10/21.
 * 描述：数据库工具
 */
public class DBUtil {
    public static final String DB_NAME = "grow_data";
    private static DBUtil sDB;
    private static DaoSession mDaoSession;
    private DaoMaster.DevOpenHelper mOpenHelper;
    private GrowDataDao mGrowDataDao;

    private DBUtil(Context context) {
        mOpenHelper = new DbOpenHelper(context, DB_NAME, null);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        mGrowDataDao = mDaoSession.getGrowDataDao();
    }

    public static DBUtil getInstance(Context context) {
        if (sDB == null) {
            synchronized (DBUtil.class) {
                if (sDB == null) {
                    sDB = new DBUtil(context);
                }
            }
        }
        return sDB;
    }

    /**
     * 通过baby.uuid 查询其成长数据
     *
     * @param uuid uuid
     * @return GrowData
     */
    public List<GrowData> getGrowDataByUuid(@NonNull String uuid) {
        return mGrowDataDao.queryBuilder().where(GrowDataDao.Properties.Uuid.eq(uuid)).orderDesc(GrowDataDao.Properties.AddTime)
                .build().list();
    }

    /**
     * 根据id删除数据
     *
     * @param id dataId
     */
    public void deleteGrowDataById(long id) {
        mGrowDataDao.deleteByKey(id);
    }
    /**
     * 新增数据
     *
     * @param growData 数据
     * @return id
     */
    public long addGrowData(GrowData growData) {
        return mGrowDataDao.insert(growData);
    }
}
