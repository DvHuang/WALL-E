package time_day;

/**
 * Created by JTdavy on 2016/2/23.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;



public class PersonDao {
    //增删改查
    //javaweb 1.加载jdbc驱动.连接 2.准备sql 3.查询

    private PersonDBOpenHelper helper;
    private String TAG="persondao";

    //任何人使用 dao 都要传递一个上下文
    public PersonDao(Context context) {
        helper = new PersonDBOpenHelper(context);
    }

    /*
     * 添加一条记录
     */
    public long add(String name,String phone,String address){
        SQLiteDatabase db = helper.getWritableDatabase();
        //	db.execSQL("insert into personInfo (name,phone,address) values (?,?,?)",
        //		new Object[]{name,phone,address});
        ContentValues values = new ContentValues();	//map集合
        values.put("name", name);
        values.put("phone", phone);
        values.put("address", address);
        long result = db.insert("personInfo", null, values);
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
        int result = db.delete("personInfo", "id=?", new String[]{id + ""});
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
    public List<PersonInfo> findAll(){
        int money = 0;
        List<PersonInfo> personInfos = new ArrayList<PersonInfo>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("personInfo", new String[]{"id","name","phone","address"},
                null, null, null, null, null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            /*String moneyStr = cursor.getString(cursor.getColumnIndex("money"));
            if(TextUtils.isEmpty(moneyStr)){
                money = 0;
            }else{
                money = Integer.parseInt(moneyStr);
            }*/
            PersonInfo personInfo = new PersonInfo(id, name, phone, address, money);
            personInfos.add(personInfo);
        }
        cursor.close();
        db.close();
        return personInfos;
    }

}
