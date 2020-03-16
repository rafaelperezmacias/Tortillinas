package com.zamnadev.tortillinas.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.zamnadev.tortillinas.Firma.FirmaActivity;
import com.zamnadev.tortillinas.R;

public class HomeFragment extends Fragment {

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MaterialButton btnFirmar = view.findViewById(R.id.btn_firmar);
        btnFirmar.setOnClickListener((v) -> {
            Intent intent = new Intent(getActivity(), FirmaActivity.class);
            startActivity(intent);
        });
        return view;
    }
}