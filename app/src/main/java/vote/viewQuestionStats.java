package vote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.wali.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class viewQuestionStats extends Activity implements OnClickListener{

	private static final String TAG = "viewQuestionStats";

	//Buttons
	private View helpBtn; 
	private Button retrieveQuestionBtn;

	
	private EditText questionNumField; 

	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewquestionstats);

	    retrieveQuestionBtn = (Button) findViewById(R.id.retrieveQuestionBtn);
		retrieveQuestionBtn.setOnClickListener(this); 
		
		helpBtn = (View) findViewById(R.id.help);
		helpBtn.setOnClickListener(this);
			
		questionNumField = (EditText) findViewById(R.id.questionNumField);
		questionNumField.setOnClickListener(this); 
	}

	public static final String removeBOM(String data) {
		if (TextUtils.isEmpty(data)) {
			return data;
		}

		if (data.startsWith("\ufeff")) {
			return data.substring(1);
		} else {
			return data;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {

		// Send to people
		case R.id.retrieveQuestionBtn:

			// go to the site and retrieve the question
			// pull id from field
			String id = questionNumField.getText().toString(); 
			


			//Package up for sending
			Map<String, String> data = new HashMap<String, String>();
			data.put("ID", id);
			
			//Send data
			//JsonResult parser = new JsonConnection(url).post(data);

			String url = "http://192.168.1.17:9001/hello";
			Log.e(TAG,url);
			StringRequest postRequest = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								// consume an optional byte order mark (BOM) if it exists
								response=removeBOM(response);
								Log.e(TAG,response);
								//JSONObject jsonResponse = new JSONObject(response).getJSONObject("Site");
								JSONObject jsonResponse = new JSONObject(response);
								//JSONObject jsonResponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1)).getJSONObject("server");
								Log.e(TAG,jsonResponse+"json");
								String site = jsonResponse.getString("report"),
										network = jsonResponse.getString("Network");
										//davystr = jsonResponse.getString("");
								System.out.println("S: "+site+"\nN: "+network);
								Log.e(TAG,response);
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
					params.put("site", "code");
					params.put("network", "tutsplus");
					return params;
				}
			};
			Volley.newRequestQueue(this).add(postRequest);

			/*String error = null;
			
			//Check result
			try {
				error = parser.valueForKey("ERROR");
			} catch (NullPointerException e){
				Toast ts = Toast.makeText(this, "Could not connect to server", Toast.LENGTH_SHORT);
				ts.show();
				break;
			}
			
			//Check for error
			if(error.compareTo("NONE") != 0){
				Toast ts = Toast.makeText(this, error, Toast.LENGTH_SHORT);
				ts.show();
			} else {
				
				//Parse results
				String title = parser.valueForKey("Title");
				String question = parser.valueForKey("Question");
				String possibleResponses = parser.valueForKey("PossibleResponse");
				String response = parser.valueForKey("Responses");
				
				//pack up and create new intent displaying question
	        	Intent ViewingStats = new Intent(this, ViewingStats.class);
	        	Bundle questionBundle = new Bundle();

	        	questionBundle.putString("Title", title); 
	        	questionBundle.putString("Question", question); 
	        	questionBundle.putString("PossibleResponse", possibleResponses); 
	        	questionBundle.putString("ID", id); 
	        	questionBundle.putString("Responses", response);
	        	
	        	ViewingStats.putExtras(questionBundle); 
	        	startActivity(ViewingStats);
	        	
				//DEBUG STUFF
				//String title = parser.valueForKey("Title");
				//Toast ts = Toast.makeText(this, title, Toast.LENGTH_SHORT);
				//ts.show();
				
	        	break;
			}*/
			
			break;
			
	        // Get some help
	        case R.id.help:
	        	Intent help = new Intent(this, Help.class);
	        	startActivity(help);
	        	break;

		default:
			Log.e(TAG, "onClick id not found");
		}

	}



}
