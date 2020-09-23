package com.zlgspace.easyreqpermission;

import androidx.annotation.NonNull;

interface LifecycleListener {
    void onStart();
    void onStop();
    void onDestroy();
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
