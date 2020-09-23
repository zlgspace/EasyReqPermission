package com.zlgspace.easyreqpermission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.zlgspace.easyreqpermission.utils.PermissionUtils;

import java.security.InvalidParameterException;
import java.util.HashMap;

import static com.zlgspace.easyreqpermission.Constant.*;

public abstract class EasyReqPermissionHandler implements Unbinder, LifecycleListener {

    private final String TAG = getClass().getSimpleName();

    protected Activity bindActivity;

    protected Object bindObj;//fragment/Activity

    protected HashMap<String,ExecutionUnit> executionUnitList = new HashMap<>();

    protected ExecutionUnit curExecutionUnit;

    @Override
    public void onStart() {
        Log.d(TAG,"onStart");
    }

    @Override
    public void onStop() {
        Log.d(TAG,"onStop");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        unbind();
    }

    @Override
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
        Activity activity = bindActivity;
        activity = null;
        Object obj = bindObj;
        obj = null;
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
            if(!PermissionUtils.hasPermssion(bindActivity,tag.getPermission())){
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

    protected void bindObject(Object obj){
        if(obj instanceof Activity){
            bindActivity((Activity)obj);
        }else if(obj instanceof Fragment){
            bindActivity((Fragment)obj);
        }else if(obj instanceof android.app.Fragment){
            bindActivity((android.app.Fragment)obj);
        }else{//其他情况直接抛出异常
            throw new InvalidParameterException("Params must be Activity\\Fragment\\android.app.Fragment");
        }
    }

    protected void bindActivity(Activity act){
        bindActivity = act;
        bindObj = act;
        bindLifecycle(act);
    }

    protected void bindActivity(Fragment frg){
        bindActivity = frg.getActivity();
        bindObj = frg;
        bindLifecycle(bindActivity);
    }

    protected void bindActivity(android.app.Fragment frg){
        bindActivity = frg.getActivity();
        bindObj = frg;
        bindLifecycle(bindActivity);
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

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    void realReqPermission(){
        if(curExecutionUnit==null)
            return;
        String permissions[] = curExecutionUnit.getPermissions();
        if(bindObj instanceof FragmentActivity){
            FragmentManager supportFragmentManager =  ((FragmentActivity)bindActivity).getSupportFragmentManager();
            LifecycleSupportFragment fragment = (LifecycleSupportFragment)supportFragmentManager.findFragmentByTag(getClass().getName());
            PermissionUtils.reqPermission(fragment,permissions);
        }else{
            android.app.FragmentManager supportFragmentManager = bindActivity.getFragmentManager();
            LifecycleFragment fragment = (LifecycleFragment)supportFragmentManager.findFragmentByTag(getClass().getName());
            PermissionUtils.reqPermission(fragment,permissions);
        }
    }

    private void doNotGetPermission( String[] permissions, int[] grantResults){
        for(String p:permissions){
            if(!PermissionUtils.canRequestPermission(bindActivity,p)){//拒绝
                refusePermissions(permissions,grantResults);
                return;
            }
        }
        //禁止
        forbidPermissions(permissions,grantResults);
    }

    private void bindLifecycle(@NonNull Activity activity){
        if(activity instanceof FragmentActivity){
            LifecycleSupportFragment fragment = new LifecycleSupportFragment(this);
            FragmentManager supportFragmentManager =  ((FragmentActivity)activity).getSupportFragmentManager();
            supportFragmentManager.beginTransaction().add(fragment,getClass().getName()).commit();
        }else{
            LifecycleFragment fragment = new LifecycleFragment(this);
            android.app.FragmentManager supportFragmentManager = activity.getFragmentManager();
            supportFragmentManager.beginTransaction().add(fragment,getClass().getName()).commit();
        }
    }


    protected boolean proclaimPermissions(){return false;}//宣告权限

    protected void gotPermissions(String[] permissions){}//获取到权限

    protected void refusePermissions(String[] permissions,int[] grantResults){}//拒绝权限

    protected void forbidPermissions(String[] permissions,int[] grantResults){} //禁止权限

}
