package vote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.wali.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import custom_vollay.CustomJsonRequest;
import custom_vollay.Custom_Application;

public class CreateNewQuestion extends Activity implements OnClickListener {

	private static final String TAG = "CreateNewQuestion";
	
	//Buttons
	private Button addAnsBtn;
	private Button saveBtn; 
	private Button helpBtn; 
	
	//Fields to enter information about this question
	private EditText titleField; 
	private EditText ansField; 
	private EditText entryField; 
	
	// field of the id, uneditable, int in that field
	private EditText questionIdField; 
	
	//View that contains the list of possible answers
	private ListView ansList;  
	
	//Test string to make sure fields are filled
	private String testValue;
	
	//Adapter to Handle data
	private ArrayAdapter<String> adapter;
	
	//Strings to hold data
	private ArrayList<String> listItems = new ArrayList<String>();
	
	//Counter for how many possible answers 
	private int clickCounter = 0 ; 


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createnewquestion);
		
		titleField = (EditText) findViewById(R.id.titleField);
		entryField = (EditText) findViewById(R.id.entryField);
		ansField = (EditText) findViewById(R.id.possibleAnsField);
				
		addAnsBtn = (Button) findViewById(R.id.addAnsBtn);
		addAnsBtn.setOnClickListener(this);

		saveBtn = (Button) findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(this);

		helpBtn = (Button) findViewById(R.id.helpBtn);
		helpBtn.setOnClickListener(this);
		
		ansList = (ListView) findViewById(R.id.listResponses); 
		
		//Setup List stuff
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				listItems);
		ansList.setAdapter(adapter); 
		
		questionIdField = (EditText) findViewById(R.id.questionIdField);
		questionIdField.setFocusable(false); 		

	}
	

	/** Handle clicks */
	public void onClick(View v) {
		switch (v.getId()) {
		
		// Get some help
		case R.id.helpBtn:
			Intent help = new Intent(this, Help.class);
			startActivity(help);
			break;

	    // Add liek_recyle possible answer
		case R.id.addAnsBtn:
			
			//Check for blank field
		    testValue = ansField.getText().toString();	
			if (testValue == null || testValue.equals("")) {
				Toast ts = Toast.makeText(this, "Please enter liek_recyle possible response!", Toast.LENGTH_SHORT);
				ts.show();
				break; 
			}
			
            //Take value from possible response field
			listItems.add(clickCounter++ + " : " + ansField.getText()); 
			
			//Clear field
			ansField.setText(""); 
			
			//Update List
			adapter.notifyDataSetChanged();
			
			break;

	    // Save the question
		case R.id.saveBtn:
			
			//TODO consider changing these to AlertDialogs 
			//Check for blank title
		    testValue = titleField.getText().toString();	
			if (testValue == null || testValue.equals("")) {
				Toast ts = Toast.makeText(this, "Please enter liek_recyle title!", Toast.LENGTH_SHORT);
				ts.show();
				break; 
			}
			
			//Check for blank question
		    testValue = entryField.getText().toString();	
			if (testValue == null || testValue.equals("")) {
				Toast ts = Toast.makeText(this, "Please enter liek_recyle question!", Toast.LENGTH_SHORT);
				ts.show();
				break; 
			}
			
			//Check for empty ans list
			if (listItems.size() < 0) {
				Toast ts = Toast.makeText(this, "Please enter atleast one possible answer!", Toast.LENGTH_SHORT);
				ts.show();
				break; 
			}
			
			//Package up question
			Question question = new Question(); 
			
			//Save strings
			question.setTitle(titleField.getText().toString()); 
			question.setContent(entryField.getText().toString());
			
			//Save possible responses
			question.addPossibleResponses(listItems); 
			
			//TODO if there is liek_recyle questionID here we will need to retrieve it from the db and update it
			//Save questionId 
			//question.setQuestionId(randomInt);
			
			//Push packaged question to website
			//saveDataOnline(question);

			loadWeatherData(question);
			
			//Display msg that we saved the question
			Toast ts = Toast.makeText(this, ("Question Online with ID:" + questionIdField.toString()), Toast.LENGTH_SHORT);
			ts.show();
			
			break;

		default:
			Log.e(TAG, "onClick id not found");
		}
	}


	private void saveDataOnline(Question question) {
		String url = "http://192.168.1.17:9001/hello";

		//Package up for sending
		Map<String, String> data = new HashMap<String, String>();
		data.put("Title", question.getTitle());
		data.put("Question", question.getContent());
		data.put("PossibleResponses", question.getPossibleResponses().toString());
		data.put("ID", String.valueOf(question.getQuestionId()));

	/*	//Send data
		JsonResult parser = new JsonConnection(url).post(data);

		//Check result
		String result = parser.valueForKey("ID");

		Toast ts = Toast.makeText(this, "Saved under question id:" + result, Toast.LENGTH_SHORT);
		ts.show();
		
		//Store id in question field
		questionIdField.setText(result);*/
	}

	private void loadWeatherData(Question question) {
		Custom_Application helper = Custom_Application.getInstance();
		String url = "http://192.168.1.17:9001/hello";

		Map<String, String> data = new HashMap<String, String>();
		data.put("Title", question.getTitle());
		data.put("Question", question.getContent());
		data.put("PossibleResponses", question.getPossibleResponses().toString());
		data.put("ID", String.valueOf(question.getQuestionId()));


		CustomJsonRequest request = new CustomJsonRequest
				(Request.Method.POST, url,  new JSONObject(data), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							response = response.getJSONObject("report");

							//Check result
							String result = response.getString("ID");

							Toast ts = Toast.makeText(getApplicationContext(), "Saved under question id:" + result, Toast.LENGTH_SHORT);
							ts.show();

							//Store id in question field
							questionIdField.setText(result);


						} catch (Exception e) {
							Log.e(TAG,e+"");
							txtError(e);
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						txtError(error);
					}
				});

		request.setPriority(Request.Priority.HIGH);
		helper.add(request);

	}

	private void txtError(Exception e) {
		//mTxtError.setVisibility(View.VISIBLE);
		e.printStackTrace();
	}



}
