package com.huntmobi.w2a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.huntmobi.smartlink.HM_Smartlink;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private static final String HM_SharedPreferences_Info = "HM_SharedPreferences_Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HM_Smartlink.init(getApplication(), "zjnpqs");
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean installed = preferences.getBoolean("Installed", false); // 判断App是否新装并首次启动

        if (!installed) { // 设备未安装App，安装App后首次启动
            HM_Smartlink.setdeviceTrackID("");// 新装用户deviceTrackID传值为空；
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("Installed", true);
            editor.apply();
        } else { // 设备已安装或更新App或非首次启动
            // 更新用户deviceTrackID传入自定义唯一ID，例：IDFV，IDFA，GUID或自定义规则的唯一ID等等，确保可以关联到当前登录的用户即可
            HM_Smartlink.setdeviceTrackID("CustomID");
        }
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        Uri data = intent.getData();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String deepLinkPath = data.getPath();
            assert deepLinkPath != null;
            Log.d("deepLinkPath", deepLinkPath);
            HM_Smartlink.setFrom(data.toString());
        }
        HM_Smartlink.start(new HM_Smartlink.attributeCallback() {
            @Override
            public void onSuccess(JSONObject data) {
                Log.i("attributeCallback", data.toString());
            }
        });
    }
}