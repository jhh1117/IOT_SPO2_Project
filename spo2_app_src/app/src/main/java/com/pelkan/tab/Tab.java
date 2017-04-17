
package com.pelkan.tab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import mehdi.sakout.fancybuttons.FancyButton;


public class Tab extends ActionBarActivity implements ActionBar.TabListener {
    SharedPreferences setting;                          //토픽 저장을 위한 변수 및 ViewPager
    SharedPreferences.Editor editor;
    SectionsPagerAdapter mSectionsPagerAdapter;
    static String topic;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        final ActionBar actionBar = getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();
        topic = setting.getString("topic", "");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_launcher);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SectionsFragment1.newInstance(position + 1);
                case 1:
                    return SectionsFragment2.newInstance(position + 1);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
            return rootView;
        }
    }
    //spo2 데이터 출력을 위한 fragment
    public static class SectionsFragment1 extends Fragment {
        private String jsonResult;          //php로 전송된 json으로 디코딩된 결과
        private TextView testView;          //spo2 출력
        private String spo2;
        int result_spo2;
        private final Handler handler = new Handler();
        private View rootView;
        private DonutProgress progress;
        int time = 30;
        int flag;                           //flag가 1이면 푸시알림 간 상태이며, 0이면 가지 않음
        int countdown = 0;                  //휴식을 위한 카운트 다운
        int resume_countdown = 0;           //재개를 위한  카운트 다운
        private String url = "http://218.150.182.58:2044/iot_test.php";
        NotificationManager manager;
        public SectionsFragment1() {

        }
        static SectionsFragment1 newInstance(int SectionNumber){
            SectionsFragment1 fragment = new SectionsFragment1();
            Bundle args = new Bundle();
            args.putInt("section_number", SectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_page1, container, false);
            testView = (TextView) rootView.findViewById(R.id.result_data);
            progress = (DonutProgress) rootView.findViewById(R.id.donut_progress);
            progress.setTextColor(android.R.color.background_light);
            progress.setInnerBackgroundColor(android.R.color.background_light);

            //1초마다 실행되는 쓰레드
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (time > 0) {
                        handler.post(new Runnable(){
                            @Override
                            public void run() {
                                accessWebService();             //spo2 데이터를 받아옴
                                time++;
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

            return rootView;
        }
        //spo2를 받는데 사용되는 쓰레드
        private class JsonReadTask extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;
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
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("topic", topic));
                    httppost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
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
                }
                return answer;
            }

            @Override
            protected void onPostExecute(String result) {
                ListDrwaer();
                testView = (TextView) rootView.findViewById(R.id.result_data);

                if(spo2 != null && spo2 != "null") {        // spo2값이 정상일 경우
                    testView.setText(spo2);
                    result_spo2 = Integer.parseInt(spo2);
                    progress.setProgress(result_spo2);
                    testView.setTextColor(Color.BLUE);
                }
                else {
                    progress.setProgress(0);                    //spo2값이 비정상일 경우
                    testView.setText("센서 미접속");
                }
                if(result_spo2 < 96 && result_spo2 > 0) {   //spo2값이 적정치 이하인 경우 카운트 스타트
                    countdown++;
                    testView.setTextColor(Color.parseColor("#ff7f00"));
                    //비정상
                }else if(result_spo2 == 0){
                    testView.setTextColor(Color.RED);
                    testView.setText("센서 미접속");
                    //단절
                }
                else {
                    countdown = 0;
                }
                if(countdown == 10) {                       //3분간 이하의 값이 유지될 경우  푸시 알림 및 진동
                    flag = 1;
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(1000);
                    manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification.Builder builder= new Notification.Builder(getActivity());
                    builder.setSmallIcon(android.R.drawable.ic_dialog_email);
                    builder.setTicker("잠시 쉬는게 어떨까요?");
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_input_add));

                    builder.setContentTitle("산소포화도가 낮습니다.");
                    builder.setContentText("산소포화도가 낮습니다. 정상 수치로 돌아올때까지 잠시 한숨 돌리는건 어떨까요?");

                    Notification notification= builder.build();

                    manager.notify(1, notification);
                }
                if(flag == 1 && result_spo2 > 95) {
                    resume_countdown++;
                }
                if(resume_countdown == 10) {
                    flag = 0;
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(1000);

                    manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


                    Notification.Builder builder= new Notification.Builder(getActivity());
                    builder.setSmallIcon(android.R.drawable.ic_dialog_email);
                    builder.setTicker("이제 열심히 공부를 해볼까요?");
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_input_add));
                    builder.setContentTitle("산소포화도가 충분합니다.");
                    builder.setContentText("산소포화도가 충분합니다. 다시 공부를 열심히 하는건 어떨까요?");
                    Notification notification= builder.build();

                    manager.notify(1, notification);
                    resume_countdown = 0;
                }

            }
        }
        public void accessWebService() {
            JsonReadTask task = new JsonReadTask();
            task.execute(new String[]{url});
        }
        public void ListDrwaer() {

            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("data");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    if(jsonChildNode.optString("spo2") == "")
                        spo2 = "0";
                    else
                        spo2 = jsonChildNode.optString("spo2");
                }
            } catch (JSONException e) {
                spo2 = "0";
            }

        }
    }

    public static class SectionsFragment2 extends Fragment {
        private String search_url = "http://218.150.182.58:2044/iot_search_day.php";            //url들
        private String search_sum_url = "http://218.150.182.58:2044/iot_search_day_sum.php";
        private String day_url = "http://218.150.182.58:2044/iot_day.php";
        private String day_sum_url = "http://218.150.182.58:2044/iot_day_sum.php";
        private String temp_url = "";
        private String temp_day_url = "http://218.150.182.58:2044/iot_day.php";
        private String week_url = "http://218.150.182.58:2044/iot_week.php";
        private String week_sum_url = "http://218.150.182.58:2044/iot_week_sum.php";
        private String month_url = "http://218.150.182.58:2044/iot_month.php";
        private String month_sum_url = "http://218.150.182.58:2044/iot_month_sum.php";
        String jsonResult;
        int hour;
        int minitue;
        int spo2;
        int day;
        String select_day;
        String select_month;
        LineDataSet dataset = null;
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        LineChart lineChart;
        Calendar oCalendar;
        TextView concent_textView;

        public SectionsFragment2() {

        }
        static SectionsFragment2 newInstance(int SectionNumber){
            SectionsFragment2 fragment = new SectionsFragment2();
            Bundle args = new Bundle();
            args.putInt("section_number", SectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_page2,
                    container, false);
            FancyButton day_sortBtn = (FancyButton)rootView.findViewById(R.id.days);
            FancyButton week_sortBtn = (FancyButton)rootView.findViewById(R.id.weeks);
            FancyButton month_sortBtn = (FancyButton)rootView.findViewById(R.id.months);
            FancyButton search_Btn = (FancyButton)rootView.findViewById(R.id.searchBtn);
            Spinner s = (Spinner)rootView.findViewById(R.id.spinner1);
            Spinner s1 = (Spinner)rootView.findViewById(R.id.spinner2);

            s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    select_month = (String)(parent.getItemAtPosition(position));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    select_day = (String)(parent.getItemAtPosition(position));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            concent_textView = (TextView)rootView.findViewById(R.id.concent_time);
            oCalendar = Calendar.getInstance( );

            lineChart = (LineChart) rootView.findViewById(R.id.chart);

            search_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(select_month.equals("6월")) {
                        labels = new ArrayList<String>();
                        entries = new ArrayList<>();
                        for (int i = 0; i < 24; i++)
                            for (int j = 0; j < 6; j++) {
                                labels.add(String.valueOf(i) + ":" + String.valueOf(j) + "0");
                                entries.add(new Entry(0, (i * 6) + j));
                            }

                        dataset = new LineDataSet(entries, "# of Calls");

                        LineData data = new LineData(labels, dataset);
                        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
                        dataset.setDrawCubic(true);
                        dataset.setDrawFilled(true);

                        lineChart.setData(data);
                        lineChart.animateY(5000);
                        temp_url = search_sum_url;
                        day_url = search_url;

                        accessWebService2();
                        accessWebService2_sum();
                    }else {
                        Toast.makeText(getActivity(),
                                "해당 날짜에는 데이터가 존재하지 않습니다!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            day_sortBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    labels = new ArrayList<String>();
                    entries = new ArrayList<>();
                    for(int i = 0; i < 24; i++)
                        for(int j = 0; j < 6; j++) {
                            labels.add(String.valueOf(i) + ":" + String.valueOf(j) + "0");
                            entries.add(new Entry(0, (i * 6) + j));
                        }

                    dataset = new LineDataSet(entries, "# of Calls");

                    LineData data = new LineData(labels, dataset);
                    dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    dataset.setDrawCubic(true);
                    dataset.setDrawFilled(true);

                    lineChart.setData(data);
                    lineChart.animateY(5000);
                    temp_url = day_sum_url;
                    day_url = temp_day_url;

                    accessWebService2();
                    accessWebService2_sum();
                }
            });
            week_sortBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    labels = new ArrayList<String>();
                    entries = new ArrayList<>();

                    for (int i = 0; i < 7; i++) {
                        labels.add( (oCalendar.get(Calendar.MONTH) + 1) + "월" + " " + (i+20) + "일");
                        entries.add(new Entry(0, i));

                    }

                    dataset = new LineDataSet(entries, "# of Calls");

                    LineData data = new LineData(labels, dataset);
                    dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    dataset.setDrawCubic(true);
                    dataset.setDrawFilled(true);

                    lineChart.setData(data);
                    lineChart.animateY(5000);
                    temp_url = week_sum_url;

                    accessWebService3();
                    accessWebService2_sum();
                }
            });
            month_sortBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    labels = new ArrayList<String>();
                    entries = new ArrayList<>();
                    for (int i = 1; i < oCalendar.getMaximum(Calendar.DAY_OF_MONTH) + 1; i++) {
                        labels.add((oCalendar.get(Calendar.MONTH) + 1) + "월" + " " + i + "일");
                        entries.add(new Entry(0, i));
                    }

                    dataset = new LineDataSet(entries, "# of Calls");

                    LineData data = new LineData(labels, dataset);
                    dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
                    dataset.setDrawCubic(true);
                    dataset.setDrawFilled(true);

                    lineChart.setData(data);
                    lineChart.animateY(5000);
                    temp_url = month_sum_url;
                    accessWebService4();
                    accessWebService2_sum();
                }
            });

            return rootView;
        }

        //일 통계
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
                HttpPost httppost = new HttpPost(day_url);
                try {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("topic", topic));
                    param.add(new BasicNameValuePair("select_day", select_day));
                    httppost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
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
            }
        }
        public void accessWebService2() {
            JsonReadTask task = new JsonReadTask();
            task.execute(new String[]{day_url});
        }
        public void ListDrwaer() {
            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("data");
                String aa;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    if(jsonChildNode.optString("spo2").equals("null")) {
                        spo2 = 0;
                    }
                    else {
                        aa = jsonChildNode.optString("spo2");
                        aa = aa.substring(0, 2);
                        spo2 = Integer.parseInt(aa);
                    }
                    hour = Integer.parseInt(jsonChildNode.optString("hour"));
                    minitue = Integer.parseInt(jsonChildNode.optString("minitue"));

                    entries.set(hour * 6 + minitue / 10, new Entry(spo2, hour * 6 + minitue / 10));
                }
                dataset = new LineDataSet(entries, "# of Calls");

                LineData data = new LineData(labels, dataset);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
                dataset.setDrawCubic(true);
                dataset.setDrawFilled(true);

                lineChart.setData(data);
                lineChart.animateY(5000);
            } catch (JSONException e) {
            }
        }

        //일 종합시간
        private class JsonReadTask_sum extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                HttpParams para = new BasicHttpParams();
                para.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                HttpClient httpclient = new DefaultHttpClient(para);
                HttpPost httppost = new HttpPost(temp_url);
                try {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("topic", topic));
                    param.add(new BasicNameValuePair("select_day", select_day));
                    httppost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
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
                }
                return answer;
            }

            @Override
            protected void onPostExecute(String result) {
                ListDrwaer_sum();
            }
        }
        public void accessWebService2_sum() {
            JsonReadTask_sum task = new JsonReadTask_sum();
            task.execute(new String[]{day_sum_url});
        }
        public void ListDrwaer_sum() {
            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("time");
                String concent_hour = "";
                String concent_minitue ="";

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    concent_hour = jsonChildNode.optString("sum_hour");
                    concent_minitue = jsonChildNode.optString("sum_minitue");
                }
                int hour;
                int minitue;
                hour = Integer.parseInt(concent_hour);
                minitue = Integer.parseInt(concent_minitue);
                hour += minitue / 60;
                minitue = minitue % 60;
                concent_textView.setText(hour + "시간 " + minitue + "분 집중했습니다");
            } catch (JSONException e) {
            }
        }
        //주 통계
        private class JsonReadTask2 extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                HttpParams para = new BasicHttpParams();
                para.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                HttpClient httpclient = new DefaultHttpClient(para);
                HttpPost httppost = new HttpPost(week_url);
                try {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("topic", topic));
                    httppost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
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
                }
                return answer;
            }

            @Override
            protected void onPostExecute(String result) {
                ListDrwaer2();
            }
        }
        public void accessWebService3() {
            JsonReadTask2 task = new JsonReadTask2();
            task.execute(new String[]{week_url});
        }
        public void ListDrwaer2() {
            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("data");
                String aa;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    if(jsonChildNode.optString("spo2").equals("null")) {
                        spo2 = 0;
                    }
                    else {
                        aa = jsonChildNode.optString("spo2");
                        aa = aa.substring(0, 2);
                        spo2 = Integer.parseInt(aa);
                        spo2 = spo2 - 1;
                    }
                    day = Integer.parseInt(jsonChildNode.optString("day"));
                    entries.set(i, new Entry(spo2, i));
                }
                dataset = new LineDataSet(entries, "# of Calls");

                LineData data = new LineData(labels, dataset);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
                dataset.setDrawCubic(true);
                dataset.setDrawFilled(true);

                lineChart.setData(data);
                lineChart.animateY(5000);
            } catch (JSONException e) {
            }
        }

        //월 통계
        private class JsonReadTask3 extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                HttpParams para = new BasicHttpParams();
                para.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                HttpClient httpclient = new DefaultHttpClient(para);
                HttpPost httppost = new HttpPost(month_url);
                try {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("topic", topic));
                    httppost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
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
                }
                return answer;
            }

            @Override
            protected void onPostExecute(String result) {
                ListDrwaer3();
            }
        }
        public void accessWebService4() {
            JsonReadTask3 task = new JsonReadTask3();
            task.execute(new String[]{week_url});
        }
        public void ListDrwaer3() {
            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("data");
                String aa;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    if(jsonChildNode.optString("spo2").equals("null")) {
                        spo2 = 0;
                    }
                    else {
                        aa = jsonChildNode.optString("spo2");
                        aa = aa.substring(0, 2);
                        spo2 = Integer.parseInt(aa);
                        spo2 = spo2 - 1;
                    }
                    day = Integer.parseInt(jsonChildNode.optString("day"));
                    entries.set(i, new Entry(spo2, i));
                }
                dataset = new LineDataSet(entries, "# of Calls");

                LineData data = new LineData(labels, dataset);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
                dataset.setDrawCubic(true);
                dataset.setDrawFilled(true);

                lineChart.setData(data);
                lineChart.animateY(5000);
            } catch (JSONException e) {
            }
        }
    }
}