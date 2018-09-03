package com.example.fengtao.mission;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
   private EditText et_user;
   private EditText et_password;
   private Button bt_login;
   private TextView tv_resultmsg;
   private String name,password;
   private Handler handler = new Handler();
   private String resultMsg;
   private String token;
   private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        hideActionBar();
        statusBarTransparent();
        setContentView(R.layout.activity_login);
        initView();
        bt_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                name = et_user.getText().toString().trim();
                password = et_password.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login(name,password);
                    }
                }).start();
                //tv_resultmsg.setText(resultmsg);

            }
        });

        
    }
    
    //继承AppCompatActivity下去除标题栏
    public void hideActionBar(){
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    //状态栏全透明的方法封装
    public void statusBarTransparent(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    
    //控件初始化
    public void initView(){
        et_user = (EditText)findViewById(R.id.ET_User);
        et_password =(EditText)findViewById(R.id.ET_Password);
        bt_login = (Button)findViewById(R.id.BT_login);
        tv_resultmsg = (TextView)findViewById(R.id.TV_resultmsg);
    }
    
    private void login(String username,String password)  {
      try{

        JSONObject Object = new JSONObject();//实例化一个JSON对象
        Object.put("username",username);//放入一个键值对
        Object.put("password",password);
        String jsonStr = Object.toString();//将json对象转换成json字符串
          
        String loginUrl = "http://172.26.52.172:8081/rest/Token/Login";
        //String loginUrl = "http://120.27.23.105/user/login";   
        URL url = new URL(loginUrl);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setReadTimeout(5000);
        urlConnection.setConnectTimeout(5000);
        urlConnection.setRequestProperty("connection","keep-alive");
        urlConnection.setRequestProperty("Content-Type","application/json");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
        urlConnection.setDoOutput(true);//Set the DoOutput flag to true if you intend to use the URL connection for output
        urlConnection.setDoInput(true);
        OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
        osw.write(jsonStr);
        osw.flush();//刷新流
        osw.close();
        
        if(urlConnection.getResponseCode() == 200){
            token = urlConnection.getHeaderField("Authorization");
    
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte buffer[] = new byte[1024];
            while((len = is.read(buffer)) != -1){
                //len:the total number of bytes read into the buffer,
                // or -1 if there is no more data because the end of the stream has been reached.
                baos.write(buffer,0,len);
                //Writes len bytes from the specified byte array starting at offset off to this output stream
            }
            is.close();
            baos.close();
            final String result = new String(baos.toByteArray());
            JSONObject jsonObject = new JSONObject(result);
            String resultcode = jsonObject.getString("status");
            resultMsg = jsonObject.getString("message");
            SharedPreferences.Editor editor = getSharedPreferences("logintoken",MODE_PRIVATE).edit();
            editor.putString("token",token);
            editor.commit();
            Looper.prepare();
            if(resultcode.equals("1")){
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        tv_resultmsg.setText(resultMsg);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       tv_resultmsg.setText(resultMsg);
                       //tv_resultmsg.setText(token);
                    }
                });
            }
            Looper.loop();
        }
    }catch (Exception e){
            e.printStackTrace();
        }
    }
}
