package time_day;

/**
 * Created by JTdavy on 2016/4/18.
 */

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.wali.R;
import com.example.android.wali.wali;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import like_recyle_2.like_recyle_2_MainActivity;
import vote.viewQuestionStats;


public class time_main extends AppCompatActivity {


        //private String TotalUrl="http://120.24.69.247:9001/";
        private String TAG="time_main_Activity";
        private RecyclerView mRecyclerView;

        private List<String> lists;
        private List<Integer> sqit_number=new ArrayList<Integer>();

        //sqlite operation
        private PersonDao personDao;
        private DayDao dayDao;

        private List<PersonInfo> personInfos;
        private List<DayInfo> dayInfos;

        private MyRecyclerAdapter adapter;
        private Toast mToast;
        static public String macaddress;

        private int position_id=0;

        private int date;


        private String[] period={"7:00-","8:00-11:00","13:00-17:00","19:00-21:00","21:00-21:30","21:30-22:30"};
        private String[] tips={"get up","理论","工程","github","日记","run"};
        private String[] work={"习惯","效率","焦距","体系","3+2","fly"};
        //private String[] work={" "," "," "," ","  "," "};


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.time_recyle_activity_main);
            macaddress=getLocalMacAddress();
            Calendar c = Calendar.getInstance();
            date = c.get(Calendar.DAY_OF_WEEK_IN_MONTH);

            dayDao = new DayDao(this);
            //personInfos = personDao.findAll();
//            for (int i = 0; i < period.length; i++) {
//                dayDao.add(""+date, period[i],work[i],tips[i],0);
//            }
            dayInfos =dayDao.findAll();
            Log.e(TAG,"dayinfos=="+dayInfos);
            if (dayInfos.size()<1){

                Log.e(TAG,"dayinfos==null");
                //repest some pariod
                /*
                private int id;
                private String date;
                private String period;
                public String work;
                public String tips;
                public int Degree_of_completion;
                */

                for (int i = 0; i < period.length; i++) {
                    dayDao.add(""+date, period[i],work[i],tips[i],"0");
                }
                dayInfos =dayDao.findAll();

            }

            sql_day_initData(dayInfos);
            //initData();
            mRecyclerView = (RecyclerView) this.findViewById(R.id.time_recyclerView);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            // mRecyclerView.addItemDecoration();//���÷ָ���
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));//����RecyclerView���ֹ�����Ϊ2�д�ֱ�Ų�
            adapter = new MyRecyclerAdapter(this,dayInfos);
            mRecyclerView.setAdapter(adapter);

            adapter.setOnClickListener(new time_day.MyRecyclerAdapter.OnItemClickListener() {
                @Override
                public void ItemClickListener(View view, int postion) {
                    Toast.makeText(time_main.this,"book ："+postion,Toast.LENGTH_SHORT).show();
                }
                @Override
                public void ItemLongClickListener(View view, final int postion) {


                    new SweetAlertDialog(time_main.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("删除，意味着评价为‘厌恶’\n")
                            .setCancelText("cancel plx!")
                            .setConfirmText("delete it!")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    //����ɾ��
                                    //�����б���ɾ��
                                    //这里应该有三个list-number
                                    //1.数据库number，被保存在sqit——number中，根据位置可以找到对应编号
                                    //2.lists-number，adapter内容的lists
                                    //3.adapter number。这里2.3默认为是一致的，实际上，未进行实验证实
                                    int baoliu_number=sqit_number.get(postion);
                                    //lists.remove(postion);

                                    //���ݿ���ɾ��
                                    dayDao.delete(baoliu_number);
                                    Log.e("position", "postion:" + postion + "\n" + "sqit_number.get(pos):" + baoliu_number);
                                    //personInfos.remove(postion);//ע�⣬����Ҫ��position...

                                    adapter.notifyItemRemoved(postion);


                                    sDialog
                                            .setTitleText("Deleted!")

                                                    //.setContentText(personInfos.get(sqit_number.get(postion)).getName() +" has been deleted!")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    sDialog.cancel();
                                }
                            })
                            .show();




                }
            });
        }

        private void initData() {
            lists = new ArrayList();
            for (int i = 0; i < 100; i++) {
                lists.add("" + i);
            }
        }
        private void sql_initData(List<PersonInfo> locList){
            lists = new ArrayList();
            for (; position_id < locList.size(); position_id++) {
                lists.add("" + locList.get(position_id).getName()+locList.get(position_id).getId());
                sqit_number.add(locList.get(position_id).getId());

            }

        }

        private void sql_day_initData(List<DayInfo> locList){
            lists = new ArrayList();
            for (; position_id < locList.size(); position_id++) {
                lists.add("" + locList.get(position_id).getPeriod()+locList.get(position_id).getWork()+locList.get(position_id).getTips()+locList.get(position_id).getDegree_of_completion());
                sqit_number.add(locList.get(position_id).getId());
            }
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.time_day_menu, menu);
            return true;
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button2, so long
            // as you specify a parent activity in AndroidManifest.xml.

            //noinspection SimplifiableIfStatement

            switch (item.getItemId()){

                case R.id.time_add:



                    Dialog dialog2 = new Day_Dialog(time_main.this, R.style.CustomDialog, new Day_Dialog.ICustomDialogEventListener() {
                        @Override
                        public void customDialogEvent(String period, String work,String tips) {

                         /*   String movName = name;
                            String movScore = score;*/
                            Log.e(TAG, "period:" + period + "work:" + work);
                            int id = (int) dayDao.add(""+date, period,work,tips,"0");


                            //更新sqit_number，不然删除时，该数据结构会越界报错
                            sqit_number.add(id);
                            //��д���нӿڵĻص����������յ���ֵ����
                            dayInfos.add(new DayInfo(id,""+date, period,work,tips,"0"));
                            //����������
                            //Log.e(TAG, "lists.size()=" + lists.size()+"\n"+"adapter.size="+adapter.getItemCount());

                            //lists.add("" + movName + id);
                            //Log.e(TAG, "lists.size()=" + lists.size() + "\n" + "adapter.size=" + adapter.getItemCount());

                            /*adapter.notifyItemChanged(lists.size());
                            adapter.notifyItemInserted(lists.size());*/
                            adapter.notifyDataSetChanged();
                            //Log.e(TAG, "lists.size()=" + lists.size() + "\n" + "adapter.size=" + adapter.getItemCount());
                            mRecyclerView.scrollToPosition(dayInfos.size() - 1);

                        }
                    });
                    dialog2.show();
        /*
                    final MaterialDialog mMaterialDialog = new MaterialDialog(this)
                            .setTitle("Add love book");

                    mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                        }
                    }).setNegativeButton("CANCEL", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                        }
                    });

                    mMaterialDialog.show();*/

                    break;
                case R.id.time_sync:
                    personInfos = personDao.findAll();
                    // http_like(personInfos);
                   /* for (; position_id < personInfos.size(); position_id++) {
                        lists.add("" + personInfos.get(position_id).getName()+personInfos.get(position_id).getId());
                        sqit_number.add(personInfos.get(position_id).getId());
                        http_like_oneItem(personInfos.get(position_id).getName(),""+personInfos.get(position_id).getId());

                    }
                    //是否有数据需要上传
                    if (){}
                    else{};*/

                    new SweetAlertDialog(this)
                            .setTitleText("SYNC")
                            .setContentText("同步服务已在后台开启，上传时间视数据量大小而定，完成后会自动关闭!")
                            .show();
                    position_id=0;

                    for (; position_id < dayInfos.size(); position_id++) {
                        lists.add("" + dayInfos.get(position_id).getWork()+dayInfos.get(position_id).getId());
                        sqit_number.add(dayInfos.get(position_id).getId());
                        http_time_oneItem(dayInfos.get(position_id).getPeriod(),dayInfos.get(position_id).getWork(),dayInfos.get(position_id).getTips());

                    }
                    break;
                case R.id.statistics7:
                    showToast("最近七天的统计信息");
                    break;
                case R.id.statistics30:
                    showToast("最近一个月的统计信息");
                    break;
                case R.id.statisticsall:
                    showToast("所有历史信息");
                    break;
                case R.id.time_help:
                    new SweetAlertDialog(this)
                            .setTitleText("Directions")
                            .setContentText("添加喜欢的对象，考虑到服务器计算性能，您虽然可以在这里随意添加对象，但最终只有数据库中存在的对象会被作为分类的依据。\n同时您在与机器人对话过程中提到的事物及评价也会被记录并作为分类依据，感谢您的使用。")
                            .show();

            }
            return super.onOptionsItemSelected(item);
        }


        public void showToast(String s) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(time_main.this, s, Toast.LENGTH_SHORT);
            mToast.show();
        }


        // http_like
        public void http_time_oneItem( final  String n,final String s) {


            //Package up for sending
            Map<String, String> data = new HashMap<String, String>();
            data.put("mac", macaddress);
            data.put("name", n);
            data.put("score", s);

            //Send data
            //JsonResult parser = new JsonConnection(url).post(data);

            //String url = "http://120.24.69.247:9001/like";
            String url= wali.totalurl+"like";
            Log.e(TAG, url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // consume an optional byte order mark (BOM) if it exists
                                response= viewQuestionStats.removeBOM(response);
                                Log.e(TAG,response);
                                //JSONObject jsonResponse = new JSONObject(response).getJSONObject("Site");
                                JSONObject jsonResponse = new JSONObject(response);
                                //JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1)).getJSONObject("server");

                                String item = jsonResponse.getString("FLAG");
                                Log.e(TAG,"S: "+item);

                            } catch (JSONException e) {
                                Log.e(TAG,e+"");
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<>();
                    // the POST parameters:
                    params.put("mac", macaddress);
                    params.put("name", n);
                    params.put("score", s);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(postRequest);

        }


    public void http_time_oneItem( final  String str_period,final String str_work,final String str_tips) {


        //Package up for sending
        Map<String, String> data = new HashMap<String, String>();
        data.put("mac", time_day.time_main.macaddress);
        data.put("period", str_period);
        data.put("work", str_work);
        data.put("tips",str_tips);

        //Send data
        //JsonResult parser = new JsonConnection(url).post(data);

        //String url = "http://120.24.69.247:9001/like";
        String url= wali.totalurl+"time_day";
        Log.e(TAG, url);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // consume an optional byte order mark (BOM) if it exists
                            response= viewQuestionStats.removeBOM(response);
                            Log.e(TAG,response);
                            //JSONObject jsonResponse = new JSONObject(response).getJSONObject("Site");
                            JSONObject jsonResponse = new JSONObject(response);
                            //JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1)).getJSONObject("server");

                            String item = jsonResponse.getString("FLAG");
                            Log.e(TAG,"S: "+item);

                        } catch (JSONException e) {
                            Log.e(TAG,e+"");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("mac", like_recyle_2_MainActivity.macaddress);
                params.put("period", str_period);
                params.put("work", str_work);
                params.put("tips",str_tips);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }


        public  String getLocalMacAddress() {
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        }


}




