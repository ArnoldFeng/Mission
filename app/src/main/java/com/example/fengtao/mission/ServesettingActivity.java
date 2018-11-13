package com.example.fengtao.mission;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;

public class ServesettingActivity extends AppCompatActivity{
    
    private TextView tv_ip;
    private TextView tv_port;
    private EditText et_ip;
    private EditText et_port;
    private Button bt_ipsave;
    private String ipstr;
    private int port;
    private String portstr;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceStated){
        super.onCreate(savedInstanceStated);
        statusBarTransparent();
        setContentView(R.layout.servesetting);
        initView();
        setToolbar();
        SharedPreferences sp = getSharedPreferences("servesetting",MODE_PRIVATE);
        String ip = sp.getString("ip","");
        int port = sp.getInt("port",0);
        
        String portstr1 = String.valueOf(port);
        et_ip.setText(ip);
        et_port.setText(portstr1);
        
        bt_ipsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveIpPort(); 
            }
        });
        
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
    
    public void initView(){
        tv_ip = (TextView)findViewById(R.id.tv_ip);
        tv_port = (TextView)findViewById(R.id.tv_port);
        et_ip = (EditText)findViewById(R.id.serveip);
        et_port = (EditText)findViewById(R.id.serveport);
        bt_ipsave = (Button)findViewById(R.id.IPsave);
        toolbar = (Toolbar)findViewById(R.id.serveToolbar);
    }
    
    public void saveIpPort(){
        ipstr = et_ip.getText().toString();
        portstr = et_port.getText().toString();
        port = Integer.parseInt(portstr);
        sharedPreferences = getSharedPreferences("servesetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ip",ipstr);
        editor.putInt("port",port);
        editor.commit();
        Toast.makeText(this,"保存成功",Toast.LENGTH_LONG).show();
        Log.d("ipstr",ipstr);
        Log.d("poststr",portstr);
        
    }
    
    public void setToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//去除显示的默认title
        //actionBar.setTitle("服务器IP端口设置");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
