package com.zamnadev.tortillinas.Sucursales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zamnadev.tortillinas.BottomSheets.EditarDireccionBottomSheet;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

public class MenuSucursalesBottomSheet extends BottomSheetDialogFragment {

    private Sucursal sucursal;
    private FragmentManager fragmentManager;

    public MenuSucursalesBottomSheet(Sucursal sucursal, FragmentManager fragmentManager) {
        this.sucursal = sucursal;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_sucursales_bottom_sheet,container);

        ((Button) view.findViewById(R.id.btnNombre))
                .setOnClickListener(view1 -> {
                    EditarNombreSucursalBottomSheet bottomSheet = new EditarNombreSucursalBottomSheet(sucursal);
                    bottomSheet.show(fragmentManager,bottomSheet.getTag());
                    dismiss();
                });

        ((Button) view.findViewById(R.id.btnDireccion))
                .setOnClickListener(view12 -> {
                    EditarDireccionBottomSheet bottomSheet = new EditarDireccionBottomSheet(1,sucursal.getIdSucursal(),sucursal.getDireccion());
                    bottomSheet.show(fragmentManager,bottomSheet.getTag());
                    dismiss();
                });

        ((Button) view.findViewById(R.id.btnCubetas))
                .setOnClickListener(view13 -> {
                    EditarCubetasSucursalBottomSheet bottomSheet = new EditarCubetasSucursalBottomSheet(sucursal);
                    bottomSheet.show(fragmentManager,bottomSheet.getTag());
                    dismiss();
                });

        return view;
    }
}
