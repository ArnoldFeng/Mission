package com.example.fengtao.mission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import com.example.fengtao.mission.database.MyDAOImpl;

public class DaibanFragment extends Fragment {
    private WebView webView;
    public void onCreate(Bundle savedInsatanceStated){
        super.onCreate(savedInsatanceStated);


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daiban,container,false);
        return v;
    }
    public void onActivityCreated(Bundle savedInstanceStated){
        super.onActivityCreated(savedInstanceStated);
        webView = (WebView)getActivity().findViewById(R.id.daibanWeb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/daiban.html");
    }
    //这是改变的内容
}
