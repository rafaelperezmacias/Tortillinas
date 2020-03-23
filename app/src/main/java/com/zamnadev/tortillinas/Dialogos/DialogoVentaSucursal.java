package com.zamnadev.tortillinas.Dialogos;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorSucursalesDialogo;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorVentaSucursales;
import com.zamnadev.tortillinas.BottomSheets.EmpleadosBottomSheet;
import com.zamnadev.tortillinas.Fragments.VentasFragment;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.Venta;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class DialogoVentaSucursal extends DialogFragment {

    private AdaptadorVentaSucursales adaptador;

    private VentasFragment ventasFragment;
    private Empleado empleado;
    private ArrayList<Venta> ventas;
    private boolean isMostrador;

    public DialogoVentaSucursal(VentasFragment ventasFragment, Empleado empleado, ArrayList<Venta> ventas, boolean isMostrador) {
        this.ventasFragment = ventasFragment;
        this.empleado = empleado;
        this.ventas = ventas;
        this.isMostrador = isMostrador;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogo_empleado_sucursal, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final ArrayList<Sucursal> sucursals = new ArrayList<>();

        for (int x = 0; x < empleado.getSucursales().size(); x++)
        {
            if (!ventas.isEmpty()) {
                for (int y = 0; y < ventas.size(); y++) {
                    if (empleado.getSucursales().get("s"+x).equals(ventas.get(y).getIdSucursal())) {
                        continue;
                    }
                    Sucursal s = new Sucursal();
                    s.setIdSucursal(empleado.getSucursales().get("s"+x));
                    sucursals.add(s);
                }
            } else {
                Sucursal s = new Sucursal();
                s.setIdSucursal(empleado.getSucursales().get("s"+x));
                sucursals.add(s);
            }
        }


        adaptador = new AdaptadorVentaSucursales(getContext(),sucursals);
        recyclerView.setAdapter(adaptador);

        ((Button) view.findViewById(R.id.btnSiguiente)).setText("SIGUIENTE");
        (view.findViewById(R.id.btnSiguiente)).setOnClickListener(view1 -> {
            if (isMostrador) {
                ventasFragment.altaVentaMostrador(adaptador.getIdSucursalActiva());
            } else {
                ventasFragment.altaVentaRepartidor(adaptador.getIdSucursalActiva());
            }
            dismiss();
        });
        (view.findViewById(R.id.btnCancelar)).setOnClickListener(view1 -> dismiss());
        return view;
    }
}