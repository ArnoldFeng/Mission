package com.example.fengtao.mission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.fengtao.mission.database.MyDAOImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Handler handler = new Handler();
    private String resultData;
    private String appName;
    private String imageUrl;
    private Bitmap bitmap;
    private List<Fragment> mFragments;
    private DaibanFragment daibanFragment;
    private GongzuoFragment gongzuoFragment;
    private ShebeiFragment shebeiFragment;
    private ShezhiFragment shezhiFragment;
    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    
    private Fragment fr1,fr2,fr3,fr4;
    private FrameLayout frameLayout;
    private View bottomTab;
    private RelativeLayout tabDaiban,tabGongzuo,tabShebei,tabShezhi;
    private ImageView iconDaiban,iconGongzuo,iconShebei,iconShezhi;
    private TextView textDaiban,textGongzuo,textShebei,textShezhi;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    
    private int red = 0xFFFF6633;
    private int black = 0xFF000000;
   // private MyDAOImpl my = new MyDAOImpl(this);

    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //hideActionBar();
        statusBarTransparent();
        
        setContentView(R.layout.activity_main);
        initView();
        
        
        fragmentManager = getSupportFragmentManager();
        updateActionbarTitle("掌上运维");
        setItem(0);
        
    }
    
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
/*        switch (requestCode){
            case Constant.REQ_QR_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String content = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
                    Log.d("Tag",content);
                    Toast.makeText(this,content,Toast.LENGTH_LONG).show();
                    my.insert(content);
                    
                }
        }
        //queryResult();*/
    }
    
/*    public void queryResult(){
        List<Map<String,String>> list = my.query();
        if(list.size()>0&&list!=null){
            Map<String,String> map = list.get(0);
            Toast.makeText(this,map.get("mapcontent"),Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"查询失败",Toast.LENGTH_LONG).show();
        }
        //new AlertDialog.Builder(this).setTitle("扫描结果").setMessage(map.get("mapcontent")).show();
    }*/
    
    public void initView(){
        frameLayout = (FrameLayout)this.findViewById(R.id.fragment_container);
        bottomTab = (View)findViewById(R.id.bottom_tab); 
        tabDaiban = (RelativeLayout)this.findViewById(R.id.tab_daiban);
        tabGongzuo = (RelativeLayout)this.findViewById(R.id.tab_gongzuo);
        tabShebei = (RelativeLayout)this.findViewById(R.id.tab_shebei);
        tabShezhi = (RelativeLayout)this.findViewById(R.id.tab_shezhi);
        iconDaiban = (ImageView)this.findViewById(R.id.icon_daiban);
        iconGongzuo = (ImageView)this.findViewById(R.id.icon_gongzuo);
        iconShebei = (ImageView)this.findViewById(R.id.icon_shebei);
        iconShezhi = (ImageView)this.findViewById(R.id.icon_shezhi);
        textDaiban = (TextView)this.findViewById(R.id.text_daiban);
        textGongzuo = (TextView)this.findViewById(R.id.text_gongzuo);
        textShebei = (TextView)this.findViewById(R.id.text_shebei);
        textShezhi = (TextView)this.findViewById(R.id.text_shezhi);
        toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        tabDaiban.setOnClickListener(this);
        tabGongzuo.setOnClickListener(this);
        tabShebei.setOnClickListener(this);
        tabShezhi.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.tab_daiban:
                updateActionbarTitle("掌上运维");
                setItem(0);
                break;
            case R.id.tab_gongzuo:
                updateActionbarTitle("我的工作");
                setItem(1);
                break;
            case R.id.tab_shebei:
                updateActionbarTitle("设备台账");
                setItem(2);
                break;
            case R.id.tab_shezhi:
                updateActionbarTitle("系统设置");
                setItem(3);
                break;
        }
        
    }
    
    private void setItem(int index){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        clearStatus();
        hideFragment(transaction);
        switch (index){
            case 0:
                
                iconDaiban.setImageResource(R.drawable.tab);
                textDaiban.setTextColor(red);
                if(fr1 == null){
                    fr1 = new DaibanFragment();
                    transaction.add(R.id.fragment_container,fr1);
                    transaction.commit();
                }else{
                    transaction.show(fr1).commit();
                }
                break;
            case 1:   
                iconGongzuo.setImageResource(R.drawable.tab);
                textGongzuo.setTextColor(red);
                if(fr2 == null){
                    fr2 = new GongzuoFragment();
                    transaction.add(R.id.fragment_container,fr2);
                    transaction.commit();
                }else{
                    transaction.show(fr2).commit();
                }
                break;
            case 2:
                iconShebei.setImageResource(R.drawable.tab);
                textShebei.setTextColor(red);
                if(fr3 == null){
                    fr3 = new ShebeiFragment();
                    transaction.add(R.id.fragment_container,fr3);
                    transaction.commit();
                }else{
                    transaction.show(fr3).commit();
                }
                break;
            case 3:
                iconShezhi.setImageResource(R.drawable.tab);
                textShezhi.setTextColor(red);
                if(fr4 == null){
                    fr4 = new ShezhiFragment();
                    transaction.add(R.id.fragment_container,fr4);
                    transaction.commit();
                }else{
                    transaction.show(fr4).commit();
                }
                break;
        }
    }

    private void clearStatus() {
        iconDaiban.setImageResource(R.drawable.tab1);
        textDaiban.setTextColor(black);
        iconGongzuo.setImageResource(R.drawable.tab1);
        textGongzuo.setTextColor(black);
        iconShebei.setImageResource(R.drawable.tab1);
        textShebei.setTextColor(black);
        iconShezhi.setImageResource(R.drawable.tab1);
        textShezhi.setTextColor(black);
    }
    
    private void hideFragment(FragmentTransaction transaction){
        if(fr1 != null){
            transaction.hide(fr1);
        }
        if(fr2 != null){
            transaction.hide(fr2);
        }
        if(fr3 != null){
            transaction.hide(fr3);
        }        
        if(fr4 != null){
            transaction.hide(fr4);
        }
    }
    
    private void updateActionbarTitle(String title){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        
       // actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
        
    }
    
/*    public void initView(){
        viewPager = (ViewPager)findViewById(R.id.viewpager_container);
        mFragments = new ArrayList<Fragment>();
        daibanFragment = new DaibanFragment();
        gongzuoFragment = new GongzuoFragment();
        shebeiFragment = new ShebeiFragment();
        shezhiFragment = new ShezhiFragment();
        mFragments.add(daibanFragment);
        mFragments.add(gongzuoFragment);
        mFragments.add(shebeiFragment);
        mFragments.add(shezhiFragment);
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                
            }

            @Override
            public void onPageSelected(int i) {
                int currentItem = viewPager.getCurrentItem();
                //setTab(currentItem);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        
    }*/
    
     

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
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//这个flag表示window负责绘制状态栏的背景
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    
    private void responsedata() {
        try {
            SharedPreferences sp = getSharedPreferences("logintoken",MODE_PRIVATE);
            String token = sp.getString("token","");
            String dataUrl = "http://172.26.52.172:8081/rest/Token/getApps";
            URL url = new URL(dataUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("connection","keep-alive");
            httpURLConnection.setRequestProperty("Content-Type","application/json");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            httpURLConnection.setRequestProperty("Authorization",token);
            if(httpURLConnection.getResponseCode() == 200){
                InputStream is = httpURLConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len=0;
                byte buffer[] = new byte[1024];
                if((len = is.read(buffer)) !=-1){
                    baos.write(buffer,0,len);
                }
                is.close();
                baos.close();
                String data = new String(baos.toByteArray());
                JSONObject resultObject = new JSONObject(data);
                
                if(resultObject.has("data")){
                    JSONArray dataArray = resultObject.getJSONArray("data");
                    JSONObject dataObject = dataArray.getJSONObject(1);
                    appName = dataObject.getString("AppName");
                    imageUrl = dataObject.getString("icon");
                    try {
                        URL fileUrl = new URL(imageUrl);
                       // URL fileUrl = new URL("http://ww1.sinaimg.cn/bmiddle/6834c769jw1djjf4p3p9rj.jpg");
                        HttpURLConnection httpURLConnection1 = (HttpURLConnection)fileUrl.openConnection();
                        InputStream imageInput = httpURLConnection1.getInputStream();
                        
                        int length = httpURLConnection1.getContentLength();
                        if(length != -1){
                            byte imagData[] = new byte[length];
                            byte buffer1[] = new byte[512];
                            int readlen = 0;
                            int destpos = 0;
                            while((readlen = imageInput.read(buffer1)) != -1){
                                System.arraycopy(buffer1,0,imagData,destpos,readlen);
                                destpos += readlen;
                            }
                            bitmap = BitmapFactory.decodeByteArray(imagData,0,imagData.length);
                        }
                        
                        
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    
                    
                    Looper.prepare();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
/*                            tv_data.setText(appName);
                            tv_iconurl.setText(imageUrl);
                            iv_icon.setImageBitmap(bitmap);*/
                            //iv_icon.setImageURI();
                        }
                    });

                    Looper.loop();
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
