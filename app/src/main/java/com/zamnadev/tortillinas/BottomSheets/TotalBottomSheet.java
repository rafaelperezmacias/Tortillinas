package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorTotal;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorTotalRepartidores;
import com.zamnadev.tortillinas.MainActivity;
import com.zamnadev.tortillinas.Moldes.AuxVenta;
import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.Moldes.Modificado;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.ProductoModificado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaDelDia;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.R;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TotalBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private String idVenta;
    private String idEmpleado;
    private double ventasDelDia;
    private double materia;
    private String idSucursal;

    private double mermasTotal;
    private double total;

    private int x;
    private int cont;
    private boolean precio;
    private boolean activo;
    private double precioProducto;

    private AdaptadorTotalRepartidores adaptador;
    private TextView txtTotal;

    public TotalBottomSheet(String idVenta, String idEmpleado, String idSucursal)
    {
        this.idVenta = idVenta;
        this.idEmpleado = idEmpleado;
        this.idSucursal = idSucursal;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_total_botttom_sheet, null);
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if(BottomSheetBehavior.STATE_HIDDEN == i) dismiss();
                if(BottomSheetBehavior.STATE_DRAGGING == i) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override public void onSlide(@NonNull View view, float v) { }
        });

        view.findViewById(R.id.btnGuardar).setVisibility(View.GONE);

        TextInputEditText txtTortillas = view.findViewById(R.id.txtTortillas);
        TextInputEditText txtMasa = view.findViewById(R.id.txtMasa);

        MaterialCardView cardVentasExtras = view.findViewById(R.id.card_ventas_extra);
        MaterialCardView cardVentasMostrador = view.findViewById(R.id.card_ventas_mostrador);
        MaterialCardView cardGastosMostrador = view.findViewById(R.id.card_gastos_mostrador);
        MaterialCardView cardMermas = view.findViewById(R.id.card_mermas);
        MaterialCardView cardMasa = view.findViewById(R.id.card_masa_vendida);

        TextView txtSucursal = view.findViewById(R.id.txtSucursal);

        TextView txtTotalMostrador = view.findViewById(R.id.txtTotalVentaMostrador);
        RecyclerView rVentaMostrador = view.findViewById(R.id.recyclerviewVentaMostrador);
        ImageButton btnVentaMostrador = view.findViewById(R.id.btnVentaMostrador);

        TextView txtTotalGastosMostrador = view.findViewById(R.id.txtGastosMostrador);
        RecyclerView rGastosMostrador = view.findViewById(R.id.recyclerviewGastosMostrador);
        ImageButton btnGastosMostrador = view.findViewById(R.id.btnGastosMostrador);

        TextView txtVentasExtra = view.findViewById(R.id.txtVentasExtra);
        RecyclerView rVentasExtra = view.findViewById(R.id.recyclerviewVentasExtra);
        ImageButton btnVentasExtra = view.findViewById(R.id.btnVentasExtra);

        ImageButton btnMermas = view.findViewById(R.id.btnMermas);
        ImageButton btnMasa = view.findViewById(R.id.btnMasaVendida);
        LinearLayout lytContenidoMermas = view.findViewById(R.id.lytContenidoMermas);

        TextView txtMasaVendida = view.findViewById(R.id.txtMasaVendida);
        TextView txtKgsMasaVendia = view.findViewById(R.id.txtMasaKgVendida);

        RecyclerView rRepartidores = view.findViewById(R.id.recyclerview_repartidores);

        TextView txtMolino = view.findViewById(R.id.txtMolino);
        TextView txtMolinoP = view.findViewById(R.id.txtMolinoP);
        TextView txtMaquinaMasa = view.findViewById(R.id.txtMaquinaMasa);
        TextView txtMaquinaMasaP = view.findViewById(R.id.txtMaquinaMasaP);
        TextView txtTortilla = view.findViewById(R.id.txtTortilla);
        TextView txtTortillaP = view.findViewById(R.id.txtTortillaP);
        TextView txtMermaTotal = view.findViewById(R.id.txtMermaTotal);
        txtTotal = view.findViewById(R.id.txtTotal);

        rVentaMostrador.setLayoutManager(new LinearLayoutManager(getContext()));
        rVentaMostrador.setHasFixedSize(true);
        rGastosMostrador.setLayoutManager(new LinearLayoutManager(getContext()));
        rGastosMostrador.setHasFixedSize(true);
        rVentasExtra.setLayoutManager(new LinearLayoutManager(getContext()));
        rVentasExtra.setHasFixedSize(true);
        rRepartidores.setLayoutManager(new LinearLayoutManager(getContext()));
        rRepartidores.setHasFixedSize(true);

        FirebaseDatabase.getInstance().getReference("VentasMostrador")
                .child(idEmpleado)
                .child(idVenta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        VentaMostrador ventaMostrador = dataSnapshot.getValue(VentaMostrador.class);
                        materia = 0.0;
                        //TODO MANERJO DE RECURSOS POR PARTE DE LA VENTA
                        if (ventaMostrador.getCostales() > 0 || ventaMostrador.getBotes() > 0)
                        {
                            int botes = 0;
                            if (ventaMostrador.getBotes() >= 0) {
                                botes += ventaMostrador.getBotes();
                            }
                            if (ventaMostrador.getCostales() >= 0) {
                                botes += ventaMostrador.getCostales() * 5;
                            }
                            if (ventaMostrador.getMaizNixtamalizado() >= 0.0) {
                                botes -= ventaMostrador.getMaizNixtamalizado();
                            }

                            materia =   (((double) botes / 5) * 50) * 1.8;
                        }

                        precio = false;
                        if (ventaMostrador.getMasaVendida() >= 0.0) {
                            txtKgsMasaVendia.setText(ventaMostrador.getMasaVendida() + " kgs");
                            precio = true;
                        } else {
                            txtKgsMasaVendia.setText("0 kgs");
                            txtMasaVendida.setText("+ $0");
                        }

                        FirebaseDatabase.getInstance().getReference("Productos")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        mermasTotal = 0.0;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Producto p = snapshot.getValue(Producto.class);
                                            if (p.getNombre().toUpperCase().contains("TORTILLA")) {
                                                if (p.isModificado()) {
                                                    activo = false;
                                                    precioProducto = 0.0;
                                                    FirebaseDatabase.getInstance().getReference("CambioPrecios")
                                                            .child(p.getIdProducto())
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    int x = 0;
                                                                    Modificado ant = null;
                                                                    for (DataSnapshot sp : dataSnapshot.getChildren()) {
                                                                        Modificado pd = sp.getValue(Modificado.class);
                                                                        if (x == 0) {
                                                                            if (p.getAlta() < ventaMostrador.getTiempo() && ventaMostrador.getTiempo() < pd.getFecha()) {
                                                                                precioProducto = pd.getPrecio();
                                                                                activo = true;
                                                                                break;
                                                                            }
                                                                            ant = new Modificado(pd);
                                                                        } else {
                                                                            if (ant.getFecha() < ventaMostrador.getTiempo() && ventaMostrador.getTiempo() < pd.getFecha()) {
                                                                                precioProducto = pd.getPrecio();
                                                                                activo = true;
                                                                                break;
                                                                            }
                                                                            ant = new Modificado(pd);
                                                                        }
                                                                        x++;
                                                                    }
                                                                    if (!activo) {
                                                                        precioProducto = p.getPrecio();
                                                                    }
                                                                    if (ventaMostrador.getMermaTortilla() >= 0.0) {
                                                                        txtTortilla.setText("Tortillas: " + ventaMostrador.getMermaTortilla() + " kgs");
                                                                        txtTortillaP.setText("$" + precioProducto * ventaMostrador.getMermaTortilla());
                                                                        mermasTotal += precioProducto * ventaMostrador.getMermaTortilla();
                                                                    } else {
                                                                        txtTortilla.setText("Tortillas: 0 kgs");
                                                                        txtTortillaP.setText("$0");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                } else {
                                                    if (ventaMostrador.getMermaTortilla() >= 0.0) {
                                                        txtTortilla.setText("Tortillas: " + ventaMostrador.getMermaTortilla() + " kgs");
                                                        txtTortillaP.setText("$" + p.getPrecio() * ventaMostrador.getMermaTortilla());
                                                        mermasTotal += p.getPrecio() * ventaMostrador.getMermaTortilla();
                                                    } else {
                                                        txtTortilla.setText("Tortillas: 0 kgs");
                                                        txtTortillaP.setText("$0");
                                                    }
                                                }
                                            }
                                            if (p.getNombre().toUpperCase().contains("MASA")) {
                                                if (p.isModificado()) {
                                                    activo = false;
                                                    precioProducto = 0.0;
                                                    FirebaseDatabase.getInstance().getReference("CambioPrecios")
                                                            .child(p.getIdProducto())
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    int x = 0;
                                                                    Modificado ant = null;
                                                                    for (DataSnapshot sp : dataSnapshot.getChildren()) {
                                                                        Modificado pd = sp.getValue(Modificado.class);
                                                                        if (x == 0) {
                                                                            if (p.getAlta() < ventaMostrador.getTiempo() && ventaMostrador.getTiempo() < pd.getFecha()) {
                                                                                precioProducto = pd.getPrecio();
                                                                                activo = true;
                                                                                break;
                                                                            }
                                                                            ant = new Modificado(pd);
                                                                        } else {
                                                                            if (ant.getFecha() < ventaMostrador.getTiempo() && ventaMostrador.getTiempo() < pd.getFecha()) {
                                                                                precioProducto = pd.getPrecio();
                                                                                activo = true;
                                                                                break;
                                                                            }
                                                                            ant = new Modificado(pd);
                                                                        }
                                                                        x++;
                                                                    }
                                                                    if (!activo) {
                                                                        precioProducto = p.getPrecio();
                                                                    }

                                                                    if (precio) {
                                                                        txtMasaVendida.setText("+ $" + ventaMostrador.getMasaVendida() * precioProducto);
                                                                        total += ventaMostrador.getMasaVendida() * precioProducto;
                                                                        txtTotal.setText("TOTAL: $" + total);
                                                                    }

                                                                    if (ventaMostrador.getMaquinaMasa() >= 0.0) {
                                                                        txtMaquinaMasa.setText("Maquina masa: " + ventaMostrador.getMaquinaMasa() + " kgs");
                                                                        txtMaquinaMasaP.setText("$" + precioProducto * ventaMostrador.getMaquinaMasa());
                                                                        mermasTotal += precioProducto * ventaMostrador.getMaquinaMasa();
                                                                    } else {
                                                                        txtMaquinaMasa.setText("Maquina masa: 0 kgs");
                                                                        txtMaquinaMasaP.setText("$0");
                                                                    }
                                                                    if (ventaMostrador.getMolino() >= 0.0) {
                                                                        txtMolino.setText("Molino: " + ventaMostrador.getMolino() + " kgs");
                                                                        txtMolinoP.setText("$" + precioProducto * ventaMostrador.getMolino());
                                                                        mermasTotal += precioProducto * ventaMostrador.getMolino();
                                                                    } else {
                                                                        txtMolino.setText("Molino: 0 kgs");
                                                                        txtMolinoP.setText("$0");
                                                                    }
                                                                    txtMermaTotal.setText("- $" + mermasTotal);
                                                                    total -= mermasTotal;
                                                                    txtTotal.setText("TOTAL: $" + total);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                } else {
                                                    if (ventaMostrador.getMolino() >= 0.0) {
                                                        txtMolino.setText("Molino: " + ventaMostrador.getMolino() + " kgs");
                                                        txtMolinoP.setText("$" + p.getPrecio() * ventaMostrador.getMolino());
                                                        mermasTotal += p.getPrecio() * ventaMostrador.getMolino();
                                                    } else {
                                                        txtMolino.setText("Molino: 0 kgs");
                                                        txtMolinoP.setText("$0");
                                                    }
                                                    if (precio) {
                                                        txtMasaVendida.setText("+ $" + ventaMostrador.getMasaVendida() * p.getPrecio());
                                                        total += ventaMostrador.getMasaVendida() * p.getPrecio();
                                                        txtTotal.setText("TOTAL: $" + total);
                                                    }
                                                    txtMaquinaMasa.setText("Maquina masa: " + ventaMostrador.getMaquinaMasa() + " kgs");
                                                    txtMaquinaMasaP.setText("$" + p.getPrecio() * ventaMostrador.getMaquinaMasa());
                                                    mermasTotal += p.getPrecio() * ventaMostrador.getMaquinaMasa();
                                                    txtMermaTotal.setText("- $" + mermasTotal);
                                                    total -= mermasTotal;
                                                    txtTotal.setText("TOTAL: $" + total);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                        if (!ventaMostrador.getRepartidores().get("repartidor0").equals("null")) {
                            ArrayList<String> repartidores = new ArrayList<>();
                            repartidores.clear();
                            for (int x = 0; x < ventaMostrador.getRepartidores().size(); x++) {
                                repartidores.add(ventaMostrador.getRepartidores().get("repartidor"+x));
                            }
                            adaptador = new AdaptadorTotalRepartidores(getContext(),repartidores,idVenta, TotalBottomSheet.this);
                            rRepartidores.setAdapter(adaptador);
                        }

                        FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                                .child(idVenta)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        double masa = 0.0, tortillas = 0.0, totopos = 0.0;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                        {
                                            AuxVenta auxVenta = snapshot.getValue(AuxVenta.class);
                                            if (auxVenta.getVuelta1().isConfirmado()) {
                                                if (auxVenta.getVuelta1().getMasa() > 0) {
                                                    masa += auxVenta.getVuelta1().getMasa();
                                                }
                                                if (auxVenta.getVuelta1().getTortillas() > 0) {
                                                    tortillas += auxVenta.getVuelta1().getTortillas();
                                                }
                                                if (auxVenta.getVuelta1().getTotopos() > 0 ) {
                                                    totopos += auxVenta.getVuelta1().getTotopos();
                                                }
                                            }
                                            if (auxVenta.getVuelta2().isConfirmado()) {
                                                if (auxVenta.getVuelta2().getMasa() > 0) {
                                                    masa += auxVenta.getVuelta2().getMasa();
                                                }
                                                if (auxVenta.getVuelta2().getTortillas() > 0) {
                                                    tortillas += auxVenta.getVuelta2().getTortillas();
                                                }
                                                if (auxVenta.getVuelta2().getTotopos() > 0) {
                                                    totopos += auxVenta.getVuelta2().getTotopos();
                                                }
                                            }
                                        }
                                        if (ventaMostrador.getMasaVendida() > 0) {
                                            masa += ventaMostrador.getMasaVendida();
                                        }
                                        materia -= masa;
                                        double tortillasFinal = materia * .8;
                                        if (ventaMostrador.getTortillaSobra() > 0) {
                                            tortillasFinal += ventaMostrador.getTortillaSobra();
                                        }

                                        if (materia > 0) {
                                            txtMasa.setText("" + redondearDecimales(masa,2));
                                            txtTortillas.setText("" + redondearDecimales(tortillasFinal,2));
                                        } else {
                                            txtMasa.setText("0 kgs");
                                            txtTortillas.setText("0 kgs");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("Mostrador")
                .child(idEmpleado)
                .child(idVenta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Concepto> conceptos = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            double total = 0.0;
                            conceptos.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Concepto concepto = snapshot.getValue(Concepto.class);
                                total += concepto.getPrecio();
                                conceptos.add(concepto);
                            }
                            txtTotalMostrador.setText("+ $" + total);
                            TotalBottomSheet.this.total += total;
                            txtTotal.setText("TOTAL: $" + TotalBottomSheet.this.total);
                            AdaptadorTotal adaptador = new AdaptadorTotal(getContext(),conceptos);
                            rVentaMostrador.setAdapter(adaptador);
                        } else {
                            txtTotalMostrador.setText("+ $0.0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("Gastos")
                .child(idEmpleado)
                .child(idVenta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Concepto> conceptos = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            double total = 0.0;
                            conceptos.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Concepto concepto = snapshot.getValue(Concepto.class);
                                total += concepto.getPrecio();
                                conceptos.add(concepto);
                            }
                            txtTotalGastosMostrador.setText("- $" + total);
                            TotalBottomSheet.this.total -= total;
                            txtTotal.setText("TOTAL: $" + TotalBottomSheet.this.total);
                            AdaptadorTotal adaptador = new AdaptadorTotal(getContext(),conceptos);
                            rGastosMostrador.setAdapter(adaptador);
                        } else {
                            txtTotalGastosMostrador.setText("+ $0.0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("VentasDelDia")
                .child(idEmpleado)
                .child(idVenta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ArrayList<Concepto> conceptos = new ArrayList<>();
                            ventasDelDia = 0.0;
                            conceptos.clear();
                            x = 0; cont = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                cont++;
                            }
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                VentaDelDia venta = snapshot.getValue(VentaDelDia.class);
                                FirebaseDatabase.getInstance().getReference("Productos")
                                        .child(venta.getIdProducto())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snp) {
                                                x++;
                                                Producto producto = snp.getValue(Producto.class);
                                                Concepto concepto = new Concepto();
                                                concepto.setNombre(producto.getNombre() + "\nCantidad: " + venta.getCantidad());
                                                concepto.setPrecio(venta.getTotal());
                                                ventasDelDia += concepto.getPrecio();
                                                conceptos.add(concepto);
                                                txtVentasExtra.setText("+ $" + ventasDelDia);
                                                AdaptadorTotal adaptador = new AdaptadorTotal(getContext(),conceptos);
                                                rVentasExtra.setAdapter(adaptador);
                                                if (x == cont) {
                                                    total += ventasDelDia;
                                                    txtTotal.setText("TOTAL: $" + total);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                            AdaptadorTotal adaptador = new AdaptadorTotal(getContext(),conceptos);
                            rVentasExtra.setAdapter(adaptador);
                        } else {
                            txtVentasExtra.setText("$0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        DatabaseReference refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales")
                .child(idSucursal);
        refSucursal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                txtSucursal.setText(sucursal.getNombre());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnMasa.setOnClickListener(view1 -> {
            if (btnMasa.getTag().equals("down")) {
                btnMasa.setTag("up");
                btnMasa.setImageResource(R.drawable.ic_arrow_up_24dp);
                txtKgsMasaVendia.setVisibility(View.VISIBLE);
            } else {
                btnMasa.setTag("down");
                btnMasa.setImageResource(R.drawable.ic_arrow_down_24dp);
                txtKgsMasaVendia.setVisibility(View.GONE);
            }
        });

        cardMasa.setOnClickListener(view1 -> {
            if (btnMasa.getTag().equals("down")) {
                btnMasa.setTag("up");
                btnMasa.setImageResource(R.drawable.ic_arrow_up_24dp);
                txtKgsMasaVendia.setVisibility(View.VISIBLE);
            } else {
                btnMasa.setTag("down");
                btnMasa.setImageResource(R.drawable.ic_arrow_down_24dp);
                txtKgsMasaVendia.setVisibility(View.GONE);
            }
        });

        btnMermas.setOnClickListener(view1 -> {
            if (btnMermas.getTag().equals("down")) {
                btnMermas.setTag("up");
                btnMermas.setImageResource(R.drawable.ic_arrow_up_24dp);
                lytContenidoMermas.setVisibility(View.VISIBLE);
            } else {
                btnMermas.setTag("down");
                btnMermas.setImageResource(R.drawable.ic_arrow_down_24dp);
                lytContenidoMermas.setVisibility(View.GONE);
            }
        });

        cardMermas.setOnClickListener(view1 -> {
            if (btnMermas.getTag().equals("down")) {
                btnMermas.setTag("up");
                btnMermas.setImageResource(R.drawable.ic_arrow_up_24dp);
                lytContenidoMermas.setVisibility(View.VISIBLE);
            } else {
                btnMermas.setTag("down");
                btnMermas.setImageResource(R.drawable.ic_arrow_down_24dp);
                lytContenidoMermas.setVisibility(View.GONE);
            }
        });

        btnVentaMostrador.setOnClickListener(view1 -> {
            if (btnVentaMostrador.getTag().equals("down")) {
                btnVentaMostrador.setTag("up");
                btnVentaMostrador.setImageResource(R.drawable.ic_arrow_up_24dp);
                rVentaMostrador.setVisibility(View.VISIBLE);
            } else {
                btnVentaMostrador.setTag("down");
                btnVentaMostrador.setImageResource(R.drawable.ic_arrow_down_24dp);
                rVentaMostrador.setVisibility(View.GONE);
            }
        });

        cardVentasMostrador.setOnClickListener(view1 -> {
            if (btnVentaMostrador.getTag().equals("down")) {
                btnVentaMostrador.setTag("up");
                btnVentaMostrador.setImageResource(R.drawable.ic_arrow_up_24dp);
                rVentaMostrador.setVisibility(View.VISIBLE);
            } else {
                btnVentaMostrador.setTag("down");
                btnVentaMostrador.setImageResource(R.drawable.ic_arrow_down_24dp);
                rVentaMostrador.setVisibility(View.GONE);
            }
        });

        btnGastosMostrador.setOnClickListener(view1 -> {
            if (btnGastosMostrador.getTag().equals("down")) {
                btnGastosMostrador.setTag("up");
                btnGastosMostrador.setImageResource(R.drawable.ic_arrow_up_24dp);
                rGastosMostrador.setVisibility(View.VISIBLE);
            } else {
                btnGastosMostrador.setTag("down");
                btnGastosMostrador.setImageResource(R.drawable.ic_arrow_down_24dp);
                rGastosMostrador.setVisibility(View.GONE);
            }
        });

        cardGastosMostrador.setOnClickListener(view1 -> {
            if (btnGastosMostrador.getTag().equals("down")) {
                btnGastosMostrador.setTag("up");
                btnGastosMostrador.setImageResource(R.drawable.ic_arrow_up_24dp);
                rGastosMostrador.setVisibility(View.VISIBLE);
            } else {
                btnGastosMostrador.setTag("down");
                btnGastosMostrador.setImageResource(R.drawable.ic_arrow_down_24dp);
                rGastosMostrador.setVisibility(View.GONE);
            }
        });

        btnVentasExtra.setOnClickListener(view1 -> {
            if (btnVentasExtra.getTag().equals("down")) {
                btnVentasExtra.setTag("up");
                btnVentasExtra.setImageResource(R.drawable.ic_arrow_up_24dp);
                rVentasExtra.setVisibility(View.VISIBLE);
            } else {
                btnVentasExtra.setTag("down");
                btnVentasExtra.setImageResource(R.drawable.ic_arrow_down_24dp);
                rVentasExtra.setVisibility(View.GONE);
            }
        });

        cardVentasExtras.setOnClickListener(view1 -> {
            if (btnVentasExtra.getTag().equals("down")) {
                btnVentasExtra.setTag("up");
                btnVentasExtra.setImageResource(R.drawable.ic_arrow_up_24dp);
                rVentasExtra.setVisibility(View.VISIBLE);
            } else {
                btnVentasExtra.setTag("down");
                btnVentasExtra.setImageResource(R.drawable.ic_arrow_down_24dp);
                rVentasExtra.setVisibility(View.GONE);
            }
        });

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener(view1 -> dismiss());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            NestedScrollView nestedScrollView = view.findViewById(R.id.nested_scroll_ventas);
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener)
                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                        if(scrollY == 0) {
                            toolbar.setElevation(0);
                        } else {
                            toolbar.setElevation(8);
                        }
                    });
        }
        setCancelable(false);
        return bottomSheet;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void aumentarRepartidores() {
        double tmpPrecio = 0.0;
        for (double p : adaptador.getTotales()) {
            tmpPrecio += p;
        }
        Log.e("sad",""+ tmpPrecio);
        total += tmpPrecio;
        txtTotal.setText("TOTAL: $" + total);
    }

    public double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }
}

