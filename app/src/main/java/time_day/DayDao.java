package time_day;

/**
 * Created by JTdavy on 2016/4/18.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;





public class DayDao {

    //增删改查
    //javaweb 1.加载jdbc驱动.连接 2.准备sql 3.查询

    private DayDBOpenHelper helper;
    private String TAG="DayDao";

    //任何人使用 dao 都要传递一个上下文
    public DayDao(Context context) {
        helper = new DayDBOpenHelper(context);
    }

    /*
     * 添加一条记录
     *  private int id;
        private String date;
        private String period;
        public String work;
        public String tips;
        public int Degree_of_completion;
     */
    public long add(String data,String period,String work,String tips,String Degree ){
        SQLiteDatabase db = helper.getWritableDatabase();
        //	db.execSQL("insert into personInfo (name,phone,address) values (?,?,?)",
        //		new Object[]{name,phone,address});
        ContentValues values = new ContentValues();	//map集合
        values.put("date", data);
        values.put("period", period);
        values.put("work", work);
        values.put("tips", tips);
        values.put("Degree", Degree);
        long result = db.insert("dayinfo", null, values);
        db.close();
        return result;

    }


    /**
     * 根据名字查找一条记录
     */
    public int find(String name){
        int id = -1;
        SQLiteDatabase db = helper.getReadableDatabase();
        //		Cursor cursor = db.rawQuery("select id from personInfo where name=?",
        //				new String[]{name});
        Cursor cursor = db.query("personInfo", new String[]{"id"}, "name=?", new String[]{name}, null, null, null);
        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    /**
     * 删除一条记录
     */
    public boolean delete(int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        //db.execSQL("delete from personInfo where id=?", new Object[]{id});
        int result = db.delete("dayinfo", "id=?", new String[]{id + ""});
        db.close();
        if(result > 0){
            Log.e(TAG, "delete true");
            return true;

        }else{
            Log.e(TAG, "delete false");
            return false;
        }
    }

    /**
     * 更改一条记录
     */
    public boolean update(String name,String phone,int id){
        SQLiteDatabase db = helper.getWritableDatabase();
//		db.execSQL("update personInfo set name=?,phone=? where id=?",
//				new Object[]{name,phone,id});
        //String table, ContentValues values, String whereClause, String[] whereArgs)
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        int result = db.update("personInfo", values, "id=?", new String[]{id + ""});
        db.close();
        if(result > 0){
            return true;
        }else{
            return false;
        }

    }

    //查找全部

    /*
    values.put("data", data);
    values.put("period", period);
    values.put("work", work);
    values.put("tips", tips);
    values.put("Degree", Degree);
    long result = db.insert("dayinfo", null, values);
    */


    public List<DayInfo> findAll(){
        int money = 0;
        List<DayInfo> dayInfos = new ArrayList<DayInfo>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("dayinfo", new String[]{"id","date","period","work","tips","Degree"},
                null, null, null, null, null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String period = cursor.getString(cursor.getColumnIndex("period"));
            String work = cursor.getString(cursor.getColumnIndex("work"));
            String tips = cursor.getString(cursor.getColumnIndex("tips"));
            String Degree = cursor.getString(cursor.getColumnIndex("Degree"));

            /*String moneyStr = cursor.getString(cursor.getColumnIndex("money"));
            if(TextUtils.isEmpty(moneyStr)){
                money = 0;
            }else{
                money = Integer.parseInt(moneyStr);
            }*/
            DayInfo dayInfo = new DayInfo(id, date, period, work, tips,"0");
            dayInfos.add(dayInfo);
        }
        cursor.close();
        db.close();
        return dayInfos;
    }

}

