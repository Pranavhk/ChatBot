package com.example.nikko.psychobot;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static android.R.id.list;

public class ChatActivity extends AppCompatActivity implements AIListener{
    private ListView lv;
    private EditText et_msg;
    private String st_msg;
    private String name = "Nikhil";
    private String bot = "Dr Phil";
    AIService aiService;
    AIDataService aiDataService;
    private List<String> your_array_list;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //getMessages();
            lv = (ListView) findViewById(R.id.lv1);
        et_msg = (EditText) findViewById(R.id.et_message);
        final AIConfiguration config = new AIConfiguration("ec5a0e6f4c7a4c63b01c16e186b4ec87",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);


        aiDataService = new AIDataService(config);
        aiService = AIService.getService(this,config);
        aiService.setListener(this);

            your_array_list = new ArrayList<String>();
            arrayAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    your_array_list );

            lv.setAdapter(arrayAdapter);
        }
        public void addMessage(String name,String message){
            your_array_list.add(name +":  "+ message);
            arrayAdapter.notifyDataSetChanged();
            //lv.getAdapter().notify();

            System.out.println(your_array_list);
        }
    public void sendMessage(View view) throws AIServiceException {
        st_msg = et_msg.getText().toString();
        addMessage(name,st_msg);
        saveMessage(name,st_msg);
        if (TextUtils.isEmpty(st_msg)) {
            et_msg.setError("Say something");
            et_msg.requestFocus();
            return;
        }
        AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(st_msg);
        if(aiRequest==null) {
            throw new IllegalArgumentException("aiRequest must be not null");

        }
        final AsyncTask<AIRequest, Integer, AIResponse> task =
                new AsyncTask<AIRequest, Integer, AIResponse>() {
                    private AIError aiError;


                    @Override
                    protected AIResponse doInBackground(final AIRequest... params) {
                        final AIRequest request = params[0];
                        try {
                            final AIResponse response =    aiDataService.request(request);
                            // Return response
                            return response;
                        } catch (final AIServiceException e) {
                            aiError = new AIError(e);
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(final AIResponse response) {
                        if (response != null) {
                            onResult(response);
                        } else {
                            onError(aiError);
                        }
                    }
                };
        task.execute(aiRequest);
    }




    @Override
    public void onResult(final AIResponse result) {
        Result response = result.getResult();
        String speech = response.getFulfillment().getSpeech();
        System.out.println(speech);
        addMessage(bot,speech);
        et_msg.setText("");
        saveMessage(bot,speech);
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
    public void saveMessage(final String name,final String message){

        String URL = "http://192.168.43.243/psycho_app/save.php";
        RequestQueue queue = Volley.newRequestQueue(ChatActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response!=null)
                {
                    System.out.println(response);
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    if(code.equals("saved"))
                    {
                        System.out.println("Message saved");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name.toLowerCase());
                params.put("message", message.toLowerCase());
                return params;
            }
        };
        queue.add(stringRequest);


    }

    public void getMessages(){

        String URL = "http://192.168.43.243/psycho_app/save.php";
        RequestQueue queue = Volley.newRequestQueue(ChatActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response!=null)
                {
                    System.out.println(response);
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    if(code.equals("saved"))
                    {
                        System.out.println("Message saved");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(stringRequest);


    }

}
