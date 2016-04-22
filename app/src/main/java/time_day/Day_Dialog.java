package time_day;



import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import java.util.HashMap;
import java.util.Map;

import like_recyle_2.like_recyle_2_MainActivity;
import vote.viewQuestionStats;

public class Day_Dialog extends Dialog  {

	private String TAG="Treedialog";

	Context context;

	String str_period,str_work,str_tips;
	private Toast mToast;



	//

	public Day_Dialog(Context context) {
		super(context);
        this.context = context;
	}
	public Day_Dialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}
	//liek_recyle constructor could afferent parameters
	public Day_Dialog(Context context, int theme, String parameters) {
		super(context, theme);
		this.context = context;
	}

	// 利用interface来构造一个回调函数

	/*回调函数就是一个通过函数指针调用的函数。如果你把函数的指针(地址)作为参数传递给另一个函数，
	当这个指针被用为调用它所指向的函数时，我们就说这是回调函数。
	回调函数不是由该函数的实现方直接调用，而是在特定的事件或条件发生时由另外的一方调用的，
	用于对该事件或条件进行响应。

	Java 中没有指针的概念，通过接口和内部类的方式实现回调的功能:

	1. 定义接口 Callback ,包含回调方法 callback()

	2. 在一个类Caller 中声明一个Callback接口对象 mCallback

	3. 在程序中赋予 Caller对象的接口成员(mCallback) 一个内部类对象如

	new  Callback（）{

		callback（）{

			//函数的具体实现

		}
	这样,在需要的时候,可用Caller对象的mCallback接口成员 调用callback()方法,完成回调.*/

	public interface ICustomDialogEventListener {
		public void customDialogEvent(String period, String work,String tips);
	}
	private ICustomDialogEventListener onCustomDialogEventListener;

	// 在构造函数中，传入回调函数，使用时，回调函数包括函数的具体实现
	public Day_Dialog(Context context, int theme, ICustomDialogEventListener listener) {
		super(context, theme);
		this.context = context;
		onCustomDialogEventListener = listener;
	}
	public Day_Dialog(Context context, ICustomDialogEventListener listener) {
		super(context);
		this.context = context;
		onCustomDialogEventListener = listener;
	}



	//当你想把值传回去的时候，调用回调函数将值设置进去
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.time_material_dialog);
		/*RatingBar rb_normal = (RatingBar) findViewById(R.id.ratingbar_Indicator);
		rb_normal.setOnRatingBarChangeListener(new RatingBarChangeListenerImpl());*/




		LinearLayout mButtonLayout = (LinearLayout) findViewById(R.id.time_buttonLayout2);
		Button mPositiveButton = (Button) mButtonLayout.findViewById(R.id.time_btn_dialog_ok);
		Button mNegativeButton = (Button) mButtonLayout.findViewById(R.id.time_btn_dialog_no);


		mPositiveButton.setOnClickListener(new Button.OnClickListener() {


			public void onClick(View v) {


				EditText period = (EditText) findViewById(R.id.period);
				EditText work = (EditText) findViewById(R.id.work);
				EditText tips = (EditText) findViewById(R.id.tips);
				/*RatingBar rb_normal = (RatingBar) findViewById(R.id.ratingbar_Indicator);
				rb_normal.setOnRatingBarChangeListener(new RatingBarChangeListenerImpl());*/

				//星跳评分监听器;
				/*rb_normal.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
						Toast.makeText(context, "rating:" + String.valueOf(rating),
								Toast.LENGTH_LONG).show();
						movieScore = String.valueOf(rating);
						Log.e(TAG,"onRatingChanged");
						movieScore_fl = rating;
					}
				});*/


				str_period = period.getText().toString();
				str_work = work.getText().toString();
				str_tips = tips.getText().toString();

				//Log.e(TAG,"name:"+movieName+"score: "+movieScore_fl);

				if (str_period== null | str_period.length()<=0) {
					showToast("时间段未填");
				}else if(str_tips==null | str_tips.length()<=0){
					showToast("tips 未填写");
				}else if(str_work==null | str_work.length()<=0){
					showToast("事务内容未填写");
				}else {

					onCustomDialogEventListener.customDialogEvent(str_period,str_work,str_tips);

					if(isWiFiActive(context)){

						http_time_oneItem(str_period,str_work,str_tips);
						showToast("添加成功 !");
					}else {
						showToast("location 添加成功,请在网络良好时进行同步!");
					}


					dismiss();
				}


			}
		});
		mNegativeButton.setOnClickListener(new Button.OnClickListener() {


			public void onClick(View v) {

				showToast("CANCLE");

				dismiss();
			}
		});

/*		mNegativeButton.setVisibility(View.VISIBLE);
		mPositiveButton.setVisibility(View.VISIBLE);*/
	}

	public void showToast(String s) {
		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
		mToast.show();
	}

	//注意onRatingChanged方法中的最后一个参数boolean fromUser:
	//若是由用户触摸手势或方向键轨迹球移动触发RatingBar的等级改变,返回true
	//若是由编程触发RatingBar的等级改变,返回false
	/*private class RatingBarChangeListenerImpl implements RatingBar.OnRatingBarChangeListener {
		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

			Toast.makeText(context, "rating:" + String.valueOf(rating),
					Toast.LENGTH_LONG).show();
			movieScore = String.valueOf(rating);
			Log.e(TAG,"onRatingChanged");
			movieScore_fl = rating;

			Log.e(TAG,"现在的等级为 rating=" + rating + ",是否是用户触发 fromUser=" + fromUser);
		}

	}*/

	//判断wifi是否可用
	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
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
		Volley.newRequestQueue(context).add(postRequest);

	}



}
