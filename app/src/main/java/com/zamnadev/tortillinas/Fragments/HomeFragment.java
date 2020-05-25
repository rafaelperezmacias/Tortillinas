package com.zamnadev.tortillinas.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.zamnadev.tortillinas.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    private String networkType;

    private Activity mActivity;

    private Disposable internetDisposable;
    private Disposable networkDisposable;

    private ImageView imgConexion;

    private TextView tvEstadoConexion;

    public HomeFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Thread t = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                networkDisposable = ReactiveNetwork.observeNetworkConnectivity(mActivity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(connectivity -> {
                            networkType = connectivity.typeName();
                            if(networkType.equalsIgnoreCase("WIFI")) {
                                imgConexion.setImageResource(R.drawable.ic_wifi_24dp);
                            } else if(networkType.equalsIgnoreCase("MOBILE")) {
                                imgConexion.setImageResource(R.drawable.signal);
                            }
                        });
                internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isConnected -> {
                            if(isConnected) {
                                tvEstadoConexion.setText("Estás conectado");
                                tvEstadoConexion.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
                                imgConexion.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary));
                            } else {
                                imgConexion.setImageResource(R.drawable.wifi_off);
                                imgConexion.setColorFilter(ContextCompat.getColor(mActivity, R.color.error));
                                tvEstadoConexion.setText("Sin conexión a internet");
                                tvEstadoConexion.setTextColor(ContextCompat.getColor(mActivity, R.color.error));
                            }
                        });
            }
        });
        t.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imgConexion = view.findViewById(R.id.img_connection);
        tvEstadoConexion = view.findViewById(R.id.tv_connection_status);
        imgConexion.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(internetDisposable != null && !internetDisposable.isDisposed()) internetDisposable.dispose();
        if(networkDisposable != null && !networkDisposable.isDisposed()) networkDisposable.dispose();
    }
}