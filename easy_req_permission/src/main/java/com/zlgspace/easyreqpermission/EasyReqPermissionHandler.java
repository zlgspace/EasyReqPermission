package com.zlgspace.easyreqpermission;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.zlgspace.easyreqpermission.utils.PermissionUtils;

import java.util.HashMap;

import static com.zlgspace.easyreqpermission.Constant.*;

public abstract class EasyReqPermissionHandler implements Unbinder{

    protected Activity bindObj;

    protected HashMap<String,ExecutionUnit> executionUnitList = new HashMap<>();

    protected ExecutionUnit curExecutionUnit;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == PERMISSIONS_REQUEST)
        {

            refreshPermission(curExecutionUnit);

            if (grantResults.length > 0 && grantResults.length>0) {//有权限了
                boolean isGot = true;
                for(int result:grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        isGot = false;
                        break;
                    }
                }
                if(isGot)
                    gotPermissions(permissions);
                else
                    doNotGetPermission(permissions,grantResults);
            }else{
                doNotGetPermission(permissions,grantResults);
            }
        }
//        else{
//            doNotGetPermission(permissions,grantResults);
//        }
    }

    @Override
    public void unbind() {
        Activity activity = bindObj;
        activity = null;
    }

    public void goOn(){
        if(curExecutionUnit==null)
            return;
        curExecutionUnit.goOn();
    }

    public void end(){
        if(curExecutionUnit==null)
            return;
        curExecutionUnit.end();
        curExecutionUnit = null;
    }

    void refreshPermission(ExecutionUnit unit){
        if(unit==null)
            return;
        for(PermissionTag tag:unit.getPermissionTags()){
            if(!PermissionUtils.hasPermssion(bindObj,tag.getPermission())){
                tag.setGot(false);
            }else{
                tag.setGot(true);
            }
        }
    }

//    boolean checkPermission(PermissionTag[] permissionTags){
//        if(permissionTags==null)
//            return true;
//        for(PermissionTag tag:permissionTags){
//            if(!PermissionUtils.hasPermssion(bindObj,tag.getPermission())){
//                return false;
//            }
//        }
//        return true;
//    }

    protected void bindActivity(Activity act){
        bindObj = act;
    }

    protected void addExecutionUnit(ExecutionUnit unit){
        executionUnitList.put(unit.getTargetMethodId(),unit);
    }

    protected ExecutionUnit getExecutionUnitByName(String name){
        for(String id:executionUnitList.keySet()){
            if(executionUnitList.get(id).getName().equals(name)){
                return executionUnitList.get(id);
            }
        }
        return null;
    }

    void realReqPermission(){
        if(curExecutionUnit==null)
            return;
        String permissions[] = curExecutionUnit.getPermissions();
        PermissionUtils.reqPermission(bindObj,permissions);
    }

    private void doNotGetPermission( String[] permissions, int[] grantResults){
        for(String p:permissions){
            if(!PermissionUtils.canRequestPermission(bindObj,p)){//禁止
                forbidPermissions(permissions,grantResults);
                return;
            }
        }
        //拒绝
        refusePermissions(permissions,grantResults);
    }


    protected boolean proclaimPermissions(){return false;}//宣告权限

    protected void gotPermissions(String[] permissions){}//获取到权限

    protected void refusePermissions(String[] permissions,int[] grantResults){}//拒绝权限

    protected void forbidPermissions(String[] permissions,int[] grantResults){} //禁止权限

}
