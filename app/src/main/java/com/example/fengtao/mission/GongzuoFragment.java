package com.example.fengtao.mission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GongzuoFragment extends Fragment {
    
    private WebView webView;
    private String targetUrl = "http://www.baidu.com";
    
    public void onCreate(Bundle savedInstanceStated){
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceStated);
        
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.fragment_gongzuo,container,false);
        
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceStated){
        super.onActivityCreated(savedInstanceStated);
        webView = (WebView)getActivity().findViewById(R.id.main_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
 /*               try{
                    if(url.startsWith("baidumap://")){
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
        webView.loadUrl(targetUrl);
    }
    
    @Override
    public void onStart(){
        super.onStart();
    }
    
}
