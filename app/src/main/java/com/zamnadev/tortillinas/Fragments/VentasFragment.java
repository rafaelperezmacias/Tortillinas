package com.zamnadev.tortillinas.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zamnadev.tortillinas.BottomSheets.VentasBottomSheet;
import com.zamnadev.tortillinas.R;

public class VentasFragment extends Fragment {

    public VentasFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);
        FloatingActionButton fabAgregarVenta = view.findViewById(R.id.fab_ventas);
        fabAgregarVenta.setOnClickListener((v) -> {
            VentasBottomSheet bottomSheet = new VentasBottomSheet();
            if (getFragmentManager() != null) {
                bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
            }
        });
        return view;
    }
}