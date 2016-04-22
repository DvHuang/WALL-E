package soc_an;

/**
 * Created by JTdavy on 2015/12/29.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wali.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class MyActivity extends Activity implements View.OnClickListener
{
    private WifiManager wifiManager;
    List<ScanResult> list;
    private TCPClient mTcpClient;
    private Button send;
    private Button start;
    private TextView recv,step,send_recv;
    private String wifi_scan="";
    private StringBuffer tv;
    private String[] wifi_string_byte;
    private int total_step = 0;   //走的总步数
    private String ORT;   //走的总步数

    private int Count_Num=10;
    private int Count_list=0;
    HashMap<String,Integer> Wifi_map=new HashMap<String,Integer>();
    private long costtime_num;


    private int send_number=0;
    private int recv_number=0;
    private int net_delay=0;

    private  String TAG="Soc_wifi";
    private  String TAG_wifi="wifi_time_on";

    private int color_flag=1;




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        send = (Button)findViewById(R.id.send);
        start=(Button)findViewById(R.id.start);
        send.setOnClickListener(this);
        start.setOnClickListener(this);

        send_recv=(TextView)findViewById(R.id.send_recv);
        recv=(TextView)findViewById(R.id.txtMessagesReceived);
        step=(TextView)findViewById(R.id.step);


        wifi_string_byte=new String [100];





    }
    /*
            作用域   当前类   同一package   子孙类   其他package

            public     √        √          √         √

            protected  √        √          √         ×

            friendly   √        √          ×         ×

            private    √        ×          ×         ×

    */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        Log.i("APP", "on resuame.");

        new Thread(new MyThread()).start();
        new Thread(new stepThread()).start();

        new connectTask().execute("");
        Intent startIntent = new Intent(this, StepCounterService.class);
        startService(startIntent);
    }





    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                Intent startIntent = new Intent(this, StepCounterService.class);
                startService(startIntent);
                break;
            case R.id.start:
                Intent stopIntent = new Intent(this, StepCounterService.class);
                stopService(stopIntent);
                break;
            default:
                break;
        }
    }

    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create liek_recyle TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            System.out.printf("values recv ==", values[0]);
            if (color_flag==1) {
                long end = System.currentTimeMillis();
                Log.e(TAG_wifi, "end:" + end);
                recv.setText(Html.fromHtml("<font color='#ff0000'><big><big>recv</big></big></font>"));

                recv.append(values[0]);
                color_flag=2;
            }else {

                recv.setText(Html.fromHtml("<font color='#00ff00'><big><big>recv</big></big></font>"));
                recv.append(values[0]);
                color_flag=1;
            }
            recv_number+=1;
            //in the arrayList we add the messaged received from server
            // arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            // mAdapter.notifyDataSetChanged();
        }
    }
    private void wifi_init() {

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan(); //必须先启动扫描，之后才能检测到变化！！
        list = wifiManager.getScanResults();

        if (list == null) {
            Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    //long start = System.currentTimeMillis();

                    list=wifiManager.getScanResults();//搜索到的设备列表
                    Count_list+=1;
                    if (mTcpClient != null) {


                        if (Count_list == Count_Num) {
                            Iterator iter = Wifi_map.entrySet().iterator();
                            //遍历hash表，写成规范格式
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                // 获取key
                                // key = (String)entry.getKey();
                                int integ,integ_e;
                                // 获取value
                                integ = (Integer)entry.getValue();
                                integ_e=integ/(Count_Num-1);
                                if ((integ_e)<10){integ_e=0;}
                                //Log.e(TAG,"integ:"+integ);
                                wifi_scan += entry.getKey() + "@/" + integ_e + "#";

                            }

                            long start = System.currentTimeMillis();
                            Log.e(TAG_wifi,"start:"+start);

                            mTcpClient.sendMessage(wifi_scan);
                            Log.e(TAG, "wifi_caan:" + wifi_scan);
                            send_number+=1;

                            Count_list = 0;
                            Wifi_map.clear();
                            wifi_scan="";
                            //Log.e(TAG,"costtime_num:"+costtime_num);
                            costtime_num=0;
                        } else {

                            for (ScanResult scanResult : list) {
                                //将每次得到的强度值加和
                                if (Wifi_map.containsKey(scanResult.BSSID)) {
                                    //Log.e(TAG,"integ:"+Math.abs(scanResult.level));

                                    Wifi_map.put(scanResult.BSSID, 100-Math.abs(scanResult.level) + Wifi_map.get(scanResult.BSSID));
                                } else {
                                    Wifi_map.put(scanResult.BSSID, 100-Math.abs(scanResult.level));
                                }

                            }
                            // Log.e(TAG,"wifi_map:"+Wifi_map);

                        }


                    }else{
                        //提示该功能无法使用，因为没有联网
                    }

//                    long end_ones = System.currentTimeMillis();
//                    long costtime_noes = end_ones - start;
//                    //Log.e(TAG,"costtime_noes:"+costtime_noes);
//                    costtime_num+=costtime_noes;

        /*            list=wifiManager.getScanResults();//搜索到的设备列表
                    for (ScanResult scanResult : list) {
                        wifi_scan += scanResult.BSSID + "@/" + Math.abs(scanResult.level) + "#";
                    }
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage(wifi_scan);
                    }*/

                    break;
                case 11:
//                    String s = String.valueOf( total_step)+ORT;
                    step.setText(total_step+"\n"+ORT);
                    send_recv.setText("send_Number:"+send_number+"\n"+"recv_Number:"+recv_number);
                    break;
                case 12:
                    break;

            }
            super.handleMessage(msg);
        }
    };
    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            wifi_init();
            while (true) {
                try {
                    Thread.sleep(400);// 单位毫秒
                    Message message = new Message();
                    message.what = 10;
                    countStep();

                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    public class stepThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(200);// 单位毫秒
                    Message message = new Message();
                    message.what = 11;
                    countStep();

                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void countStep() {
        if (StepDetector.CURRENT_SETP % 2 == 0) {
            total_step = StepDetector.CURRENT_SETP;
        } else {
            total_step = StepDetector.CURRENT_SETP +1;
        }

        total_step = StepDetector.CURRENT_SETP;
        ORT=StepDetector.CURRENT_OR;
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Intent stopIntent = new Intent(this, StepCounterService.class);
        stopService(stopIntent);
    }

}
