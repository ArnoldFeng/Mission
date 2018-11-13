package com.example.fengtao.mission;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowContentFrameStats;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fengtao.mission.database.MyDAO;
import com.example.fengtao.mission.database.MyDAOImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ShebeiFragment extends Fragment{
    private WebView webView;
    private ImageView imageView;
    private MyDAOImpl my;
    public String fileName;
    public String path;
    public Uri imageUri;
    public String result;
    public void onCreate(Bundle savedInstanceStated){
        super.onCreate(savedInstanceStated);
        
        //解决系统调用相机出现问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        my = new MyDAOImpl(getActivity());
        return inflater.inflate(R.layout.fragment_shebei,container,false);
    }
    
    public void onActivityCreated(Bundle savedInstanceStated){
        super.onActivityCreated(savedInstanceStated);
        imageView = (ImageView)getActivity().findViewById(R.id.photoResult); 
        webView = (WebView)getActivity().findViewById(R.id.testweb);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        
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
        webView.loadUrl("file:///android_asset/camera.html");
        /*webView.loadUrl("file:///android_asset/widget.basic.html");*/
        webView.addJavascriptInterface(new JSInterface1(getActivity()),"baobao");
    }
    
    class JSInterface1 {
        public  Context context;
        
        public JSInterface1(Context context){
            this.context = context;
        }
        @JavascriptInterface
        public void showDialog(int a,float b,String c,boolean d){
            if(d){
                String strMessage = "a+b+c=" + a + b + c;
                new AlertDialog.Builder(getActivity()).setTitle("setTitle设置对话框标题").setMessage(strMessage).show();
            }
        }
        @JavascriptInterface
        public void startQrCode(){
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA},11003);
            }
            Intent intent = new Intent(getActivity(),CaptureActivity.class);
            getActivity().startActivityForResult(intent,Constant.REQ_QR_CODE);
        }
        
        @JavascriptInterface
        public void openPhoto(){
            Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
            innerIntent.setType("image/*");
            startActivityForResult(innerIntent, 100);
        }
        
        @JavascriptInterface
        public void takePhoto(){
            File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"shebeiPhoto");
            if(!appDir.exists()){
                appDir.mkdirs();
            }
            Log.d("getDataDirectory",Environment.getDownloadCacheDirectory().getAbsolutePath());
            Log.d("getExternalStorageState",Environment.getExternalStorageState());
            Log.d("getDataDirectory()",Environment.getDataDirectory().getAbsolutePath());
            Log.d("getDownloadCacheDirecto",Environment.getDownloadCacheDirectory().getAbsolutePath());
            Log.d("getRootDirectory()",Environment.getRootDirectory().getAbsolutePath());
            Log.d("ExternalStorage",Environment.getExternalStorageDirectory().getAbsolutePath());
            //Log.d("ExternalMediaDirs",Context.getDataDir().toString());

            fileName = new Date().getTime()+".jpg";
            File photoResult = new File(appDir,fileName);
            //imageUri = FileProvider.getUriForFile(getActivity(),"com.example.fengtao.mission.fileprovider",photoResult);
            imageUri = Uri.fromFile(photoResult);
            Log.d("file_photoResult",photoResult.getPath());
            Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //intentnew.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intentCapture.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(intentCapture,10);
        }
        
        @JavascriptInterface
        public void saveData(String ajaxDataStr){
            //Log.d("Tag",ajaxDataStr);
            try {
                JSONObject jo = new JSONObject(ajaxDataStr);
                JSONArray jsonArray = jo.getJSONArray("data");
                MyDAOImpl myDAO = new MyDAOImpl(getActivity());
                for(int i=0;i<jsonArray.length();i++){
                    //String femalename = jsonArray.getString(i);
                    //Log.d("json数组",femalename);
                    String femalename = jsonArray.getJSONObject(i).getString("femalename");
                    myDAO.insert(femalename);
                    Log.d("saveData",femalename);
                }
                
            }catch (Exception e){
                e.printStackTrace();
            }
            
        }
        
        @JavascriptInterface
        public void queryData(){
            MyDAOImpl myDAO = new MyDAOImpl(getActivity());
            try{
                JSONObject jsonObject = new JSONObject(myDAO.query());
                JSONArray jsonArray = jsonObject.getJSONArray("femalename");
                Log.d("jsonArrayToString",jsonArray.toString());
                Log.d("queryData",jsonArray.getString(2));
            }catch (Exception e){
                e.printStackTrace();
            }
            
            
            
        }
        
        @JavascriptInterface
        public void deleteData(){
            MyDAOImpl myDAO = new MyDAOImpl(getActivity());
            myDAO.delete();
            Log.d("deletedata","数据库表已清空");
        }
        
        @JavascriptInterface
        public void showDataInHtml(){
            MyDAOImpl myDAO = new MyDAOImpl(getActivity());
            try{
                result = myDAO.query();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:showHtml("+result+")");
                    }
                });
                //webView.loadUrl("javascript:showHtml(result)");
            }catch (Exception e){
                e.printStackTrace();
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.saosao:
           // startQrCode();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    

    
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case Constant.REQ_QR_CODE:
                if(resultCode == getActivity().RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
                    //Toast.makeText(getActivity(),scanResult,Toast.LENGTH_LONG).show();
                    Log.d("Tag",scanResult);
                   //my.delete();
                    my.insert(scanResult);
                }
                
            case 10:
                if(resultCode == getActivity().RESULT_OK){
                    //imageView.setImageURI(Uri.fromFile(new File(path,fileName)));
    /*                Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap)bundle.get("data");
                    saveImgToSDCard(getActivity(),bitmap);*/
                    Intent intentBroadcast = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    //intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intentBroadcast.setData(imageUri);
                    getActivity().sendBroadcast(intentBroadcast);
                    Toast.makeText(getActivity(),"拍照成功",Toast.LENGTH_LONG).show();
                    Log.d("imageUri.getPath",imageUri.getPath());
                    
                }
        }
        //queryResult();
    }
    
    public void saveImgToSDCard(Context context,Bitmap bitmap){
        File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"imagess");
        if(!appDir.exists()){
            appDir.mkdirs();
        }
        String fileName = new Date().getTime() + ".jpg";
        File photoResult = new File(appDir,fileName);
        try{
            FileOutputStream fos = new FileOutputStream(photoResult);
            //fos.write(bitmap.getByteCount());
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Uri uri = Uri.fromFile(photoResult);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
    }
    
/*    public void saveImgToSDCard(Bitmap bitmap, String fileName){
        FileOutputStream fos;
        try{
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsoluteFile()+fileName);
                Log.d("tag",Environment.getExternalStorageDirectory().getAbsoluteFile()+fileName);
            }
            else{
                fos = new FileOutputStream(getActivity().getFilesDir()+fileName);
                Log.d("tag",getActivity().getFilesDir()+fileName);
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            //imageView.setImageBitmap(bitmap);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            
        }
    }*/

/*    public void queryResult(){
        List<Map<String,String>> list = my.query();
        if(list.size()>0&&list!=null){
            Map<String,String> map = list.get(0);
            Toast.makeText(getActivity(),map.get("mapcontent"),Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(),"查询失败",Toast.LENGTH_LONG).show();
        }
        //new AlertDialog.Builder(this).setTitle("扫描结果").setMessage(map.get("mapcontent")).show();
    }*/


    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode){
            case Constant.REQ_PERM_CAMERA:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //startQrCode();
                }
                else{
                    Toast.makeText(getActivity(),"请到权限中心打开相机权限",Toast.LENGTH_LONG).show();
                }
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //takePhoto();
                }
        }
    }
}
