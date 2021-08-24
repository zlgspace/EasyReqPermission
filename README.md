# EasyReqPermission
APT实现的权限请求框架，简化请求流程，目前华为P20上验证通过,之前jcenter依赖已经废弃，改为mavencentral
# 基本用法
## Maven
```
   mavenCentral()
```
## 引用
```
    annotationProcessor 'io.github.zlgspace:easy-req-compile:1.0.1'
    implementation 'io.github.zlgspace:easy-req-permission:1.0.1'
```
## 代码
```
   /**
    * 初始化，权限请求
    */
    MainActivity_ReqPermission reqPermission;
    reqPermission = EasyReqPermission.bind(this);
    
    /**
     * 请求权限
     */
   findViewById(R.id.testBtn).setOnClickListener(view -> reqPermission.click());
   
    
     /**
     * 需要权限执行的函数
     */
    @NeedPermission(permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE},identifier = "1111")
    public void click(){
        Toast.makeText(this,"click",Toast.LENGTH_LONG).show();
    }

    /**
     * 用户禁止请求权限
     */
    @ForbidPermission(identifier = "1111")
    public void forbidPermission(){
        Toast.makeText(this,"禁用了权限请求",Toast.LENGTH_LONG).show();
    }

    /**
     * 权限告知，可用来提示用户权限用途
     */
    @ProclaimPermission(identifier = "1111")
    public void proclaimPermission(){
       new AlertDialog.Builder(this)
       .setTitle("测试权限")
       .setMessage("申请权限，请求给与！")
       .setPositiveButton("确认", (dialogInterface, i) -> reqPermission.goOn())
       .setNegativeButton("取消", (dialogInterface, i) -> reqPermission.end())
       .show();
    }

    /**
     * 用户拒绝权限请求
     */
    @RefusePermission(identifier = "1111")
    public void refusePermission(){
        Toast.makeText(this,"拒绝了权限",Toast.LENGTH_LONG).show();
    }
   
```
        
