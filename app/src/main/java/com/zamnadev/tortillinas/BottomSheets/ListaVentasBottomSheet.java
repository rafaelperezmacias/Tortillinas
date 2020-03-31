package com.zamnadev.tortillinas.BottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.zamnadev.tortillinas.R;

public class ListaVentasBottomSheet extends Fragment {

    public ListaVentasBottomSheet() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_ventas_bottom_sheet, container, false);
    }
}