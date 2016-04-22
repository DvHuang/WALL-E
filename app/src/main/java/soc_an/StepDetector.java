package soc_an;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
//�߲�����������ڼ���߲�������
/**
 * �����㷨��̫��������㷨�Ǵӹȸ�Ʋ�����Pedometer�Ͻ�ȡ�Ĳ��ּƲ��㷨
 * 
 */
public class StepDetector implements SensorEventListener {

	public static int CURRENT_SETP = 0;
	public static String CURRENT_int_ort ;
	public static String CURRENT_int_ort2 ;
	public static String  CURRENT_OR="no sensor info";

	public static float SENSITIVITY = 0;   //SENSITIVITY������

	private float mLastValues[] = new float[3 * 2];
	private float mScale[] = new float[2];
	private float mYOffset;
	private static long end = 0;
	private static long start = 0;

	float[] accelerometerValues = new float[3];
	float[] magneticFieldValues = new float[3];
	private static final String TAG = "sensor";

	/**
	 * �����ٶȷ���
	 */
	private float mLastDirections[] = new float[3 * 2];
	private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
	private float mLastDiff[] = new float[3 * 2];
	private int mLastMatch = -1;

	/**
	 * ���������ĵĹ��캯��
	 * 
	 * @param context
	 */
	public StepDetector(Context context) {
		// TODO Auto-generated constructor stub
		super();
		int h = 480;
		mYOffset = h * 0.5f;
		mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
		mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
		if (SettingsActivity.sharedPreferences == null) {
			SettingsActivity.sharedPreferences = context.getSharedPreferences(
					SettingsActivity.SETP_SHARED_PREFERENCES,
					Context.MODE_PRIVATE);
		}
		SENSITIVITY = SettingsActivity.sharedPreferences.getInt(
				SettingsActivity.SENSITIVITY_VALUE, 3);
	}

	// public void setSensitivity(float sensitivity) {
	// SENSITIVITY = sensitivity; // 1.97 2.96 4.44 6.66 10.00 15.00 22.50
	// // 33.75
	// // 50.62
	// }

	// public void onSensorChanged(int sensor, float[] values) {
	@Override
	public void onSensorChanged(SensorEvent event) {
		// Log.i(Constant.STEP_SERVER, "StepDetector");
		Sensor sensor = event.sensor;
		// Log.i(Constant.STEP_DETECTOR, "onSensorChanged");
		synchronized (this) {
			if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magneticFieldValues = event.values.clone();
				calculateOrientation();

			} else {
				accelerometerValues = event.values.clone();
				int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
				if (j == 1) {
					float vSum = 0;
					for (int i = 0; i < 3; i++) {
						final float v = mYOffset + event.values[i] * mScale[j];
						vSum += v;
					}
					int k = 0;
					float v = vSum / 3;

					float direction = (v > mLastValues[k] ? 1: (v < mLastValues[k] ? -1 : 0));
					if (direction == -mLastDirections[k]) {
						// Direction changed
						int extType = (direction > 0 ? 0 : 1); // minumum or
						// maximum?
						mLastExtremes[extType][k] = mLastValues[k];
						float diff = Math.abs(mLastExtremes[extType][k]- mLastExtremes[1 - extType][k]);

						if (diff > SENSITIVITY) {
							boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
							boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
							boolean isNotContra = (mLastMatch != 1 - extType);

							if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
								end = System.currentTimeMillis();
								if (end - start > 500) {// ��ʱ�ж�Ϊ����һ��
									Log.i("StepDetector", "CURRENT_SETP:"
											+ CURRENT_SETP);
									CURRENT_SETP++;
									mLastMatch = extType;
									start = end;
								}
							} else {
								mLastMatch = -1;
							}
						}
						mLastDiff[k] = diff;
					}
					mLastDirections[k] = direction;
					mLastValues[k] = v;
				}
			}
		}
	}
	private  void calculateOrientation() {
		float[] values = new float[3];
		float[] R = new float[9];
		SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
		SensorManager.getOrientation(R, values);

		// 要经过一次数据格式的转换，转换为度
		values[0] = (float) Math.toDegrees(values[0]);
		Log.i(TAG, values[0]+"");
		//values[1] = (float) Math.toDegrees(values[1]);
		//values[2] = (float) Math.toDegrees(values[2]);

		CURRENT_int_ort="z:"+values[0]+"\n"+"x:"+values[1]+"\n"+"y:"+values[2]+"\n";
		CURRENT_int_ort2=CURRENT_int_ort;
		if(values[0] >= -5 && values[0] < 5){
			CURRENT_OR="正北";
			Log.i(TAG, "正北");
		}
		else if(values[0] >= 5 && values[0] < 85){
			CURRENT_OR="东北";
			Log.i(TAG, "东北");
		}
		else if(values[0] >= 85 && values[0] <=95){
			CURRENT_OR="正东";
			Log.i(TAG, "正东");
		}
		else if(values[0] >= 95 && values[0] <175){
			CURRENT_OR="东南";
			Log.i(TAG, "东南");
		}
		else if((values[0] >= 175 && values[0] <= 180) || (values[0]) >= -180 && values[0] < -175){
			CURRENT_OR="正南";
			Log.i(TAG, "正南");
		}
		else if(values[0] >= -175 && values[0] <-95){
			CURRENT_OR="西南";
			Log.i(TAG, "西南");
		}
		else if(values[0] >= -95 && values[0] < -85){
			Log.i(TAG, "正西");
			CURRENT_OR="正西";
		}
		else if(values[0] >= -85 && values[0] <-5){
			Log.i(TAG, "西北");
			CURRENT_OR="西北";
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

}
