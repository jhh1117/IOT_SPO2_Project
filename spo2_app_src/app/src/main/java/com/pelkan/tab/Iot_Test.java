package com.pelkan.tab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Iot_Test extends ActionBarActivity {
    private String jsonResult;
    private TextView testView;
    private String spo2;
    private final Handler handler = new Handler();
    int time = 30;
    private String url = "http://218.150.182.45/mas/iot_test.php";
   // private String url = "http://218.150.182.58:2044/iot_test.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot__test);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (time > 0) {
                    handler.post(new Runnable(){
                        @Override
                        public void run() {
                            accessWebService();
                            time--;
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                time = 30;
            }
        });


        t.start();





    }
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpParams para = new BasicHttpParams();
            para.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpClient httpclient = new DefaultHttpClient(para);
            HttpPost httppost = new HttpPost(url);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                System.out.println("에러 싴발 1");
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaer();
            testView = (TextView)findViewById(R.id.result_data);
            //System.out.println(spo2);
            if(spo2 != null)
                testView.setText(spo2);
            else
                testView.setText("no data!");
        }
    }
    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{url});
    }
    public void ListDrwaer() {
        ArrayList<Product> array = new ArrayList<Product>();

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            System.out.print("결과는 " + jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("data");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                spo2 = jsonChildNode.optString("spo2");
            }
        } catch (JSONException e) {
            System.out.println("에러 싴발 2");
        }
    }
}