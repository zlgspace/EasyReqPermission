package com.zlgspace.easyreqpermission;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class LifecycleFragment extends Fragment {

    LifecycleListener listener;

    public LifecycleFragment(LifecycleListener listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        listener.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        listener.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        listener.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
