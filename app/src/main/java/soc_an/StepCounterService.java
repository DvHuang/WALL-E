package soc_an;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


//service   �����̨����Ҫ�������е�����
 // �Ʋ�������
 // �����ں�̨�ķ����������˽��沿�ֵĿ�����
 // �Ϳ��Կ�����̨�ķ�����StepService
 // ע���ע�������������������ֻ���Ļ״̬����ʾ֪ͨ����StepActivity����ͨ�ţ��߹��Ĳ����ǵ������ˣ�����
public class StepCounterService extends Service {

	public static Boolean FLAG = false;// �������б�־

	private SensorManager mSensorManager;// ����������
	private soc_an.StepDetector detector;// ��������������

	private PowerManager mPowerManager;// ��Դ�������
	private WakeLock mWakeLock;// ��Ļ��

	private Sensor mSensor;


	//strp wifi
	public static int wifi_categort,last_wifi_category=0;
	public static float wifi_categort_float;

	private int  wifi_step;

	private connectTask task;

	private WifiManager wifiManager;
	List<ScanResult> list;
	private TCPClient mTcpClient;
	private String wifi_scan="";
	private TextView recv,step;

	private int total_step = 0;   //走的总步数
	private String ORT;   //走的总步数
	private boolean wifi_catalog_flag=false;

	private boolean mylocation_isclick=true;
	private boolean hotbook_isclick=true;
	private boolean WiFiThread_flag=false;

	private String wificataloge="0";


	double wifi_d,step_d,deart_d;
	double deart_d_x,deart_d_y;
	double[] wificataloge_x, wificataloge_y;
	int Wifi_Area_Mapping[ ][ ]={{200,625},{200,1800},{200,3000},{200,4100},{200,5300},
			{600,625},{600,1800},{600,3000},{600,4100},{600,5300},
			{1000,625},{1000,1800},{1000,3000},{1000,4100},{1000,5300},
			{1400,625},{1400,1800},{1400,3000},{1400,4100},{1400,5300}};

	private int Count_Num=10;
	private int Count_list=0;
	HashMap<String,Integer> Wifi_map=new HashMap<String,Integer>();
	private long costtime_num;


	public static int send_number=0;
	public static int recv_number=0;
	private int net_delay=0;


	private  String TAG="wifi_time_on";

	private boolean wifiThread_flag;

	private int color_flag=1;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();


		FLAG = true;// ���Ϊ������������

		// �����������࣬ʵ������������
		detector = new StepDetector(this);

		// ��ȡ�������ķ��񣬳�ʼ��������
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		// ע�ᴫ������ע���������2
		mSensorManager.registerListener(detector,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);

		/*��һ��д��
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sm.registerListener(myListener, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
		*/

		mSensorManager.registerListener(detector,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_FASTEST);

		// ��Դ�������
		mPowerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "S");
		mWakeLock.acquire();



		//wifi
		wifiThread_flag=true;
		new Thread(new wifiThread()).start();
		task=new connectTask();
		task.execute("");
		//new connectTask().execute("");

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FLAG = false;// ����ֹͣ
		if (detector != null) {
			mSensorManager.unregisterListener(detector);
		}

		if (mWakeLock != null) {
			mWakeLock.release();
		}
		wifiThread_flag=false;
		task.cancel(true);

		Log.e(TAG,"stop service ");
	}

	 ////wifi线程等待用户自己开启


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
			wifi_categort_float=Float.parseFloat(values[0]);
			wifi_categort =  (int)wifi_categort_float;
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
	public class wifiThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			wifi_init();
			while (wifiThread_flag) {
				try {
					Thread.sleep(400);// 单位毫秒

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
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}





}
