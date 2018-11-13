package com.example.fengtao.mission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class ShezhiFragment extends Fragment {
    private WebView webView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shezhi,container,false);
    }
    
    public void onActivityCreated(Bundle savedInstanceStated){
        super.onActivityCreated(savedInstanceStated);
        webView = (WebView)getActivity().findViewById(R.id.shezhi);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/camera.html");
        webView.addJavascriptInterface(new JsInterface(),"A");
    }
    
    class JsInterface{
        @JavascriptInterface
        public void openCamera(){
            
        }
    }
}
