package com.zlgspace.easyreqpermission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LifecycleSupportFragment extends Fragment {

    private LifecycleListener listener;

    public LifecycleSupportFragment(LifecycleListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        listener.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
