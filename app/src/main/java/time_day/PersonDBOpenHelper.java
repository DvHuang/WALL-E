package time_day;

/**
 * Created by JTdavy on 2016/2/23.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * 1 .写一个类 继承 SQLiteOpenHelper 帮助创建数据库 版本的控制
 * @author Administrator
 *
 */
public class PersonDBOpenHelper extends SQLiteOpenHelper {


    /**
     * 数据库创建帮助类的构造方法
     * @param context
     */
    public PersonDBOpenHelper(Context context) {
        //name 数据库文件的名称
        //factory 访问数据库一个数据库的游标工厂
        // version 数据库的版本
        super(context, "person.db", null, 2);
    }
    /**
     * 当数据库第一次被创建的是 调用的方法.
     * 适合做数据库表结构的初始化
     * 注意：数据库被创建后，onCreate不会再被调用
     * 如果想更改数据库，比如更改表结构，更新版本等
     * 使用下面的onUpgrade()方法,这里要想添加money列，
     * 还必须先改数据库版本，onUpgrade()才会执行
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table personInfo (id integer primary key autoincrement, name varchar(20), phone varchar(20),address varchar(50)) ");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table personInfo add money varchar(20)");
        Log.i("TiShi", "数据库被更改啦");
    }

}
