package com.zlgspace.easyreqpermission.test;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.zlgspace.easyreqpermission.EasyReqPermission;
import com.zlgspace.easyreqpermission.annotation.ForbidPermission;
import com.zlgspace.easyreqpermission.annotation.NeedPermission;
import com.zlgspace.easyreqpermission.annotation.ProclaimPermission;
import com.zlgspace.easyreqpermission.annotation.RefusePermission;


public class MainActivity extends Activity {

    MainActivity_ReqPermission reqPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reqPermission = EasyReqPermission.bind(this);

        findViewById(R.id.testBtn).setOnClickListener(view -> reqPermission.click());
    }


    @NeedPermission(permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE},identifier = "1111")
    public void click(){
        Toast.makeText(this,"click",Toast.LENGTH_LONG).show();
    }

    @NeedPermission(permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE},identifier = "222")
    public void click2(){
        Toast.makeText(this,"click",Toast.LENGTH_LONG).show();
    }

    @ForbidPermission(identifier = "1111")
    public void forbidPermission(){
        Toast.makeText(this,"禁用了权限",Toast.LENGTH_LONG).show();
    }

    @ForbidPermission(identifier = "222")
    public void forbidPermission2(){
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

    @ProclaimPermission(identifier = "222")
    public void proclaimPermission2(){
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

    @RefusePermission(identifier = "222")
    public void refusePermission2(){
        Toast.makeText(this,"拒绝了权限",Toast.LENGTH_LONG).show();
    }
}
