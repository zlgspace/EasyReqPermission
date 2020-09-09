package com.zlgspace.easyreqpermission;

public class PermissionTag {

    private String permission;
    private boolean isGot;

    public PermissionTag(){
    }

    public PermissionTag(String permission){
        setPermission(permission);
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isGot() {
        return isGot;
    }

    public void setGot(boolean got) {
        isGot = got;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PermissionTag))
            return false;
        if(permission.equals(((PermissionTag)obj).getPermission())){
            return true;
        }
        return false;
    }

    public boolean equalsPermission(String name) {
        if(permission.equals(name)){
            return true;
        }
        return false;
    }
}
