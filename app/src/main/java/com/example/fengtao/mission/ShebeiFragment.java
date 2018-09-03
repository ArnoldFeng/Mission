package com.example.fengtao.mission;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowContentFrameStats;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.lang.reflect.Method;

public class ShebeiFragment extends Fragment{
    WebView webView;
    
    public void onCreate(Bundle savedInstanceStated){
        super.onCreate(savedInstanceStated);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_shebei,container,false);
    }
    
    public void onActivityCreated(Bundle savedInstanceStated){
        super.onActivityCreated(savedInstanceStated);
        webView = (WebView)getActivity().findViewById(R.id.testweb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url){
/*                try{
                    if( url.startsWith("file://")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                }catch (Exception e){
                    return false;
                }*/
                view.loadUrl(url);
                return true;
            }
        });
        
        webView.loadUrl("file:///android_asset/test.html");
        webView.addJavascriptInterface(new JSInterface1(),"baobao");
    }
    
    class JSInterface1{
        @JavascriptInterface
        public void showDialog(int a,float b,String c,boolean d){
            if(d){
                String strMessage = "a+b+c=" + a + b + c;
                new AlertDialog.Builder(getActivity()).setTitle("setTitle设置对话框标题").setMessage(strMessage).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater){
        //menu.clear();
        menuInflater.inflate(R.menu.shebei,menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
    }
}
