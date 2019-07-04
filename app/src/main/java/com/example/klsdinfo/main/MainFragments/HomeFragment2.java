package com.example.klsdinfo.main.MainFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.klsdinfo.R;

public class HomeFragment2 extends Fragment {


    public static HomeFragment2 newInstance(){
        return new HomeFragment2();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        print("onCreateView");

        View view = inflater.inflate(R.layout.main_home_layout2, container, false);

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        print("onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        print("onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        print("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        print("onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        print("onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        print("onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        print("onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        print("onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        print("onDestroyView");
        super.onDestroyView();
    }


    private void print(String string){
        Log.d("lifecycle", string);
    }

}
