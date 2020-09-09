package com.zlgspace.easyreqpermission.test;

import androidx.annotation.NonNull;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zlgspace.easyreqpermission.EasyReqPermission;
import com.zlgspace.easyreqpermission.annotation.ForbidPermission;
import com.zlgspace.easyreqpermission.annotation.NeedPermission;
import com.zlgspace.easyreqpermission.annotation.ProclaimPermission;
import com.zlgspace.easyreqpermission.annotation.RefusePermission;

import java.util.Arrays;

public class MainActivity extends Activity {

    MainActivity_ReqPermission reqPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reqPermission = EasyReqPermission.bind(this);

        findViewById(R.id.testBtn).setOnClickListener(view -> reqPermission.click());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        reqPermission.onRequestPermissionsResult(requestCode,permissions,grantResults);
        Log.d("MainActivity","permissions:"+ Arrays.toString(permissions));
        Log.d("MainActivity","grantResults:"+ Arrays.toString(grantResults));
    }

    @NeedPermission(permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE},identifier = "1111")
    public void click(){
        Toast.makeText(this,"click",Toast.LENGTH_LONG).show();
    }

    @ForbidPermission(identifier = "1111")
    public void forbidPermission(){
        Toast.makeText(this,"禁用了权限",Toast.LENGTH_LONG).show();
    }

    @ProclaimPermission(identifier = "1111")
    public void proclaimPermission(){
       new AlertDialog.Builder(this)
       .setTitle("测试权限")
       .setMessage("申请权限，请求给与！")
       .setPositiveButton("确认", (dialogInterface, i) -> reqPermission.goOn())
       .setNegativeButton("取消", (dialogInterface, i) -> reqPermission.end())
       .show();
    }

    @RefusePermission(identifier = "1111")
    public void refusePermission(){
        Toast.makeText(this,"拒绝了权限",Toast.LENGTH_LONG).show();
    }
}
