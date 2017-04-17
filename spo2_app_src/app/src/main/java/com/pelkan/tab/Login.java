package com.pelkan.tab;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Login extends ActionBarActivity {
    private static final String TAG = Login.class.getSimpleName();
    private Button registerButton;
    private EditText emailInput;
    private EditText passwordInput;
    private ProgressDialog progressDialog;
    private boolean regFlag = false;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    //private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = (EditText) findViewById(R.id.emailInput);//姓名输入框
        passwordInput = (EditText) findViewById(R.id.passwordInput);//邮箱输入框
        registerButton = (Button) findViewById(R.id.registerButton);//注册按钮

        progressDialog = new ProgressDialog(this);//进度条
        progressDialog.setCancelable(false);

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        //  sessionManager = new SessionManager(getApplicationContext());
        //如果已经登陆,那么跳转到用户信息页面
        /*
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(RegisterActivity.this, UserDetailActivity.class);
            startActivity(intent);
            finish();
        }
        */

        //注册按钮点击操作
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                boolean cancel = false;
                View focusView = null;
                String errorMsg = null;

                //패스워드 비어있는지 확인
                if (TextUtils.isEmpty(password)) {
                    focusView = passwordInput;
                    cancel = true;
                    errorMsg = "비밀번호를 입력해주세요.";
                    //      passwordInput.setError("请输入密码哦");
                }
                //메일주소 확인
                if (TextUtils.isEmpty(email)) {
                    focusView = emailInput;
                    cancel = true;
                    errorMsg = "아이디를 입력해주세요.";
                    //        nameInput.setError("请输入名字哦");
                }

                //사용자 입력이 적절하면 등록 여부 결정
                if (!cancel) {
                    //차후에 디비에 물어봐서 아이디 등록
                    //registerUser(name, email, password);
                    RegisterUser();
                    //아이디 등록 끝나고 메인화면 이동
                } else {
                    //에러 메세지 출력
                    focusView.requestFocus();
                    cancel = false;
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    // 이메일, 비밀번호 인설트
    public void RegisterUser(){
        String email = emailInput.getText().toString();
        String pass = passwordInput.getText().toString();
        login(emailInput.getText().toString(), passwordInput.getText().toString());
    }
    //로그인 구현

    private void login(final String username, final String password) {

        class LoginAsync extends AsyncTask<String, Void, String>{

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Login.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = username;
                String pass = password;
                //System.out.println("이름은 " + uname + " 이거임");

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("m_id", uname));
                nameValuePairs.add(new BasicNameValuePair("m_pass", pass));
                String result = null;

                try{
                    String logURL = "http://218.150.182.58:2044/login.php";
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(logURL);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                loadingDialog.dismiss();

                if(s.equalsIgnoreCase("fail")){
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }else {
                    System.out.println("topic은 " + s);
                    editor.putString("topic", s);
                    editor.commit();
                    Intent intent1 = new Intent(Login.this, Tab.class);
                    startActivity(intent1);
                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //显示进度条
    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    //隐藏进度条
    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    //判断邮箱是否包含@
    private boolean isEmailValid(String email) {
        if(!email.contains("@")) {
            return false;
        }else {
            return true;
        }
    }

    private boolean isRepasswordValid(String password, String repassword) {
        if(password.equals(repassword)) {
            return true;
        }else {
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            return false;
        }else {
            return true;
        }
    }
}