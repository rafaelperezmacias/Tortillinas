package com.zamnadev.tortillinas.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zamnadev.tortillinas.BottomSheets.VentasMostradorBottomSheet;
import com.zamnadev.tortillinas.BottomSheets.VentasRepartidorBottomSheet;
import com.zamnadev.tortillinas.MainActivity;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.R;

public class VentasFragment extends Fragment {

    private MainActivity mainActivity;

    public VentasFragment(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);

        FloatingActionButton fabAgregarVenta = view.findViewById(R.id.fab_ventas);
        fabAgregarVenta.setOnClickListener((v) -> {
            if (mainActivity.getEmpleado().getTipo() == Empleado.TIPO_MOSTRADOR) {
                VentasMostradorBottomSheet bottomSheet = new VentasMostradorBottomSheet();
                if (getFragmentManager() != null) {
                    bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
                }
            } else if (mainActivity.getEmpleado().getTipo() == Empleado.TIPO_REPARTIDOR) {
                VentasRepartidorBottomSheet bottomSheet = new VentasRepartidorBottomSheet();
                if (getFragmentManager() != null) {
                    bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
                }
            } else {
                fabAgregarVenta.hide();
            }

        });
        return view;
    }
}