package com.zlgspace.easyreqpermission;


import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;

public class ExecutionUnit {
    private String name;
    private PermissionTag[] permissionTags ;//PermissionTag显得有点无力
    private String targetMethodId; //目标函数
    private String proclaimMethodId;//宣告

    private boolean isWaitingToContinue = false;

    private EasyReqPermissionHandler permissionHandler;

    public ExecutionUnit(){
    }

    public ExecutionUnit(String targetMethodId,PermissionTag ...tag){
        this.targetMethodId = targetMethodId;
        permissionTags = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PermissionTag[] getPermissionTags() {
        return permissionTags;
    }

    public String getTargetMethodId() {
        return targetMethodId;
    }

    public String getProclaimMethodId() {
        return proclaimMethodId;
    }

    public void setProclaimMethodId(String proclaimMethodId) {
        this.proclaimMethodId = proclaimMethodId;
    }

    public PermissionTag getPermissionTagByName(String permissionName){
        if(permissionTags==null||permissionTags.length==0)
            return null;
        for(PermissionTag tag:permissionTags){
            if(tag.equalsPermission(permissionName))
                return tag;
        }
        return null;
    }

    public boolean isGotPermission(){
        if(permissionTags==null||permissionTags.length==0)
            return true;
        for(PermissionTag tag:permissionTags){
            if(!tag.isGot())
                return false;
        }
        return true;
    }

    public String[] getPermissions(){
        if(permissionTags==null||permissionTags.length==0)
            return null;
        String permissions[] = new String[permissionTags.length];
        int index = 0;
        for(PermissionTag tag:permissionTags){
            permissions[index++] = tag.getPermission();
        }
        return permissions;
    }


    public void bindEasyReqPermissionHandler(EasyReqPermissionHandler handler){
        permissionHandler = handler;
    }

    public void goOn(){
        if(!isWaitingToContinue)
            return;
        isWaitingToContinue = false;
        permissionHandler.realReqPermission();
    }

    public void end(){
        if(!isWaitingToContinue)
            return;
        isWaitingToContinue = false;
        bindEasyReqPermissionHandler(null);
    }

    public void execute(){
        if(permissionHandler==null)
            return;
        //刷新权限，- -,效率貌似有点底下
        permissionHandler.refreshPermission(this);

        //有权限就直接执行对应函数
        if(isGotPermission()) {
            permissionHandler.gotPermissions(getPermissions());
            return;
        }
        //请求权限宣告，提示用户需要这些权限
        if(proclaimMethodId!=null&& !TextUtils.isEmpty(proclaimMethodId)) {
            permissionHandler.proclaimPermissions();
            isWaitingToContinue = true;
            return;
        }
        //请求权限
        permissionHandler.realReqPermission();
    }
}
