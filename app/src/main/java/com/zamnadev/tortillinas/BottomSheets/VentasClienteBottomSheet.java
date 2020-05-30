package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorRepartidorClientes;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.Modificado;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.VentaCliente;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.R;

import java.util.HashMap;
import java.util.StringTokenizer;

public class VentasClienteBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private boolean primero;
    private Cliente cliente;
    private VentaRepartidor ventaRepartidor;
    private VentaCliente ventaCliente;
    private boolean devolucion;
    private AdaptadorRepartidorClientes adaptador;

    private boolean isEditable;

    private double masaAnterior;
    private double tortillaAnterior;
    private double totopoAnterior;

    private boolean activo;
    private double precioProducto;
    private double tortillaVenta;
    private double totoposVenta;
    private double masaVenta;

    public VentasClienteBottomSheet(AdaptadorRepartidorClientes adaptador, boolean primero, Cliente cliente, VentaRepartidor ventaRepartidor, VentaCliente ventaCliente, boolean devolucion, boolean isEditable) {
        this.primero = primero;
        this.cliente = cliente;
        this.ventaRepartidor = ventaRepartidor;
        this.ventaCliente = ventaCliente;
        this.devolucion = devolucion;
        this.adaptador = adaptador;
        this.isEditable = isEditable;
        masaAnterior = -1;
        tortillaAnterior = -1;
        totopoAnterior = -1;
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_ventas_cliente_bottom_sheet, null);
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (BottomSheetBehavior.STATE_HIDDEN == i) dismiss();
                if (BottomSheetBehavior.STATE_DRAGGING == i) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });

        TextInputEditText txtTortilla = view.findViewById(R.id.txtTortilla);
        TextInputLayout lytTortilla = view.findViewById(R.id.lytTortilla);
        TextInputEditText txtMasa = view.findViewById(R.id.txtMasa);
        TextInputLayout lytMasa = view.findViewById(R.id.lytMasa);
        TextInputEditText txtTotopos = view.findViewById(R.id.txtTotopos);
        TextInputLayout lytTotopos = view.findViewById(R.id.lytTotopos);

        ImageView icon1 = view.findViewById(R.id.icon1);
        ImageView icon2 = view.findViewById(R.id.icon2);
        ImageView icon3 = view.findViewById(R.id.icon3);

        RelativeLayout layout1 = view.findViewById(R.id.layout1);
        RelativeLayout layout2 = view.findViewById(R.id.layout2);
        RelativeLayout layout3 = view.findViewById(R.id.layout3);

        if (!isEditable) {
            hideTxt(txtMasa);
            hideTxt(txtTortilla);
            hideTxt(txtTotopos);
            ((MaterialButton) view.findViewById(R.id.btnGuardar)).setVisibility(View.GONE);
        }

        if (ventaCliente != null) {
            if (devolucion) {
                if (ventaCliente.getDevolucion() != null) {
                    if (ventaCliente.getDevolucion().getTortillas() >= 0) {
                        txtTortilla.setText("" + ventaCliente.getDevolucion().getTortillas());
                    }
                    if (ventaCliente.getDevolucion().getTotopos() >= 0) {
                        txtTotopos.setText("" + ventaCliente.getDevolucion().getTotopos());
                    }
                    if (ventaCliente.getDevolucion().getMasa() >= 0) {
                        txtMasa.setText("" + ventaCliente.getDevolucion().getMasa());
                    }
                }
            } else if (primero) {
                if (ventaCliente.getVuelta1() != null) {
                    if (ventaCliente.getVuelta1().getTortillas() >= 0) {
                        txtTortilla.setText("" + ventaCliente.getVuelta1().getTortillas());
                        tortillaAnterior = ventaCliente.getVuelta1().getTortillas();
                    }
                    if (ventaCliente.getVuelta1().getTotopos() >= 0) {
                        txtTotopos.setText("" + ventaCliente.getVuelta1().getTotopos());
                        totopoAnterior = ventaCliente.getVuelta1().getTotopos();
                    }
                    if (ventaCliente.getVuelta1().getMasa() >= 0) {
                        txtMasa.setText("" + ventaCliente.getVuelta1().getMasa());
                        masaAnterior = ventaCliente.getVuelta1().getMasa();
                    }
                }
            } else {
                if (ventaCliente.getVuelta2() != null) {
                    if (ventaCliente.getVuelta2().getTortillas() >= 0) {
                        txtTortilla.setText("" + ventaCliente.getVuelta2().getTortillas());
                        tortillaAnterior = ventaCliente.getVuelta2().getTortillas();
                    }
                    if (ventaCliente.getVuelta2().getTotopos() >= 0) {
                        txtTotopos.setText("" + ventaCliente.getVuelta2().getTotopos());
                        totopoAnterior = ventaCliente.getVuelta2().getTotopos();
                    }
                    if (ventaCliente.getVuelta2().getMasa() >= 0) {
                        txtMasa.setText("" + ventaCliente.getVuelta2().getMasa());
                        masaAnterior = ventaCliente.getVuelta2().getMasa();
                    }
                }
            }
        }

        if (devolucion) {
            ((TextView) view.findViewById(R.id.txtTitulo))
                    .setText("Devolución");
            if (ventaCliente.getVuelta2() != null && ventaCliente.getVuelta1() != null) {
                if (ventaCliente.getVuelta1().getTortillas() < 0 && ventaCliente.getVuelta2().getTortillas() < 0) {
                    layout1.setVisibility(View.GONE);
                }
                if (ventaCliente.getVuelta1().getMasa() < 0 && ventaCliente.getVuelta2().getMasa() < 0) {
                    layout2.setVisibility(View.GONE);
                } else {
                    if (layout1.getVisibility() == View.GONE) {
                        icon2.setVisibility(View.VISIBLE);
                    }
                }
                if (ventaCliente.getVuelta1().getTotopos() < 0 && ventaCliente.getVuelta2().getTotopos() < 0) {
                    layout3.setVisibility(View.GONE);
                } else {
                    if (layout2.getVisibility() == View.GONE && layout1.getVisibility() == View.GONE) {
                        icon3.setVisibility(View.VISIBLE);
                    }
                }
            } else if (ventaCliente.getVuelta1() != null) {
                if (ventaCliente.getVuelta1().getTortillas() < 0) {
                    layout1.setVisibility(View.GONE);
                }
                if (ventaCliente.getVuelta1().getMasa() < 0) {
                    layout2.setVisibility(View.GONE);
                } else {
                    if (layout1.getVisibility() == View.GONE) {
                        icon2.setVisibility(View.VISIBLE);
                    }
                }
                if (ventaCliente.getVuelta1().getTotopos() < 0) {
                    layout3.setVisibility(View.GONE);
                } else {
                    if (layout2.getVisibility() == View.GONE && layout1.getVisibility() == View.GONE) {
                        icon3.setVisibility(View.VISIBLE);
                    }
                }
            } else if (ventaCliente.getVuelta2() != null) {
                if (ventaCliente.getVuelta2().getTortillas() < 0) {
                    layout1.setVisibility(View.GONE);
                }
                if (ventaCliente.getVuelta2().getMasa() < 0) {
                    layout2.setVisibility(View.GONE);
                } else {
                    if (layout1.getVisibility() == View.GONE) {
                        icon2.setVisibility(View.VISIBLE);
                    }
                }
                if (ventaCliente.getVuelta2().getTotopos() < 0) {
                    layout3.setVisibility(View.GONE);
                } else {
                    if (layout2.getVisibility() == View.GONE && layout1.getVisibility() == View.GONE) {
                        icon3.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else if (primero) {
            ((TextView) view.findViewById(R.id.txtTitulo))
                    .setText("Primer vuelta");
            if (ventaRepartidor.getVuelta1().getTortillas() <= 0) {
                layout1.setVisibility(View.GONE);
            }
            if (ventaRepartidor.getVuelta1().getMasa() <= 0) {
                layout2.setVisibility(View.GONE);
            } else {
                if (layout1.getVisibility() == View.GONE) {
                    icon2.setVisibility(View.VISIBLE);
                }
            }
            if (ventaRepartidor.getVuelta1().getTotopos() <= 0) {
                layout3.setVisibility(View.GONE);
            } else {
                if (layout2.getVisibility() == View.GONE && layout1.getVisibility() == View.GONE) {
                    icon3.setVisibility(View.VISIBLE);
                }
            }
        } else {
            ((TextView) view.findViewById(R.id.txtTitulo))
                    .setText("Segunda vuelta");
            if (ventaRepartidor.getVuelta2().getTortillas() <= 0) {
                layout1.setVisibility(View.GONE);
            }
            if (ventaRepartidor.getVuelta2().getMasa() <= 0) {
                layout2.setVisibility(View.GONE);
            } else {
                if (layout1.getVisibility() == View.GONE && layout1.getVisibility() == View.GONE) {
                    icon2.setVisibility(View.VISIBLE);
                }
            }
            if (ventaRepartidor.getVuelta2().getTotopos() <= 0) {
                layout3.setVisibility(View.GONE);
            } else {
                if (layout2.getVisibility() == View.GONE && layout1.getVisibility() == View.GONE) {
                    icon3.setVisibility(View.VISIBLE);
                }
            }
        }

        ((TextView) view.findViewById(R.id.txtSubTitulo))
                .setText(cliente.getNombre().getNombres() + " " + cliente.getNombre().getApellidos());

        ((MaterialButton) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    if (!isEditable) {
                        dismiss();
                        return;
                    }
                    double masa = -1, tortilla = -1, totopos = -1;
                    if (!txtMasa.getText().toString().isEmpty()) {
                        masa = Double.parseDouble(txtMasa.getText().toString());
                    }
                    if (!txtTortilla.getText().toString().isEmpty()) {
                        tortilla = Double.parseDouble(txtTortilla.getText().toString());
                    }
                    if (!txtTotopos.getText().toString().isEmpty()) {
                        totopos = Double.parseDouble(txtTotopos.getText().toString());
                    }
                    if (devolucion) {
                        double tortillaCompra = 0.0, masaCompra = 0.0, totoposCompra = 0.0;
                        if (ventaCliente.getVuelta1() != null) {
                            if (ventaCliente.getVuelta1().getMasa() >= 0.0) {
                                masaCompra += ventaCliente.getVuelta1().getMasa();
                            }
                            if (ventaCliente.getVuelta1().getTotopos() >= 0.0) {
                                totoposCompra += ventaCliente.getVuelta1().getTotopos();
                            }
                            if (ventaCliente.getVuelta1().getTortillas() >= 0.0) {
                                tortillaCompra += ventaCliente.getVuelta1().getTortillas();
                            }
                        }
                        if (ventaCliente.getVuelta2() != null) {
                            if (ventaCliente.getVuelta2().getMasa() >= 0.0) {
                                masaCompra += ventaCliente.getVuelta2().getMasa();
                            }
                            if (ventaCliente.getVuelta2().getTotopos() >= 0.0) {
                                totoposCompra += ventaCliente.getVuelta2().getTotopos();
                            }
                            if (ventaCliente.getVuelta2().getTortillas() >= 0.0) {
                                tortillaCompra += ventaCliente.getVuelta2().getTortillas();
                            }
                        }
                        boolean isReturn = false;
                        if (!txtMasa.getText().toString().isEmpty()) {
                            if (masa > masaCompra) {
                                lytMasa.setError("No puede regresar más de lo que compro");
                                isReturn = true;
                            } else {
                                lytMasa.setError(null);
                            }
                        }
                        if (!txtTortilla.getText().toString().isEmpty()) {
                            if (tortilla > tortillaCompra) {
                                lytTortilla.setError("No puede regresar más de lo que compro");
                                isReturn = true;
                            } else {
                                lytTortilla.setError(null);
                            }
                        }
                        if (!txtTotopos.getText().toString().isEmpty()) {
                            if (totopos > totoposCompra) {
                                lytTotopos.setError("No puede regresar más de lo que compro");
                                isReturn = true;
                            } else {
                                lytTotopos.setError(null);
                            }
                        }
                        if (isReturn) {
                            return;
                        }
                    } else {
                        if (primero) {
                            boolean isReturn = false;
                            if (adaptador.getMasaVentaPrimerVuelta() >= 0 && masa != 0 && masa > adaptador.getMasaVentaPrimerVuelta() && masa > masaAnterior && (masa - masaAnterior) > adaptador.getMasaVentaPrimerVuelta()) {
                                lytMasa.setError("Masa disponible ("+adaptador.getMasaVentaPrimerVuelta()+" kgs)");
                                isReturn = true;
                            } else {
                                lytMasa.setError(null);
                            }
                            if (adaptador.getTortillasVentaPrimerVuelta() >= 0 && tortilla != 0 && tortilla > adaptador.getTortillasVentaPrimerVuelta() && tortilla > tortillaAnterior && (tortilla - tortillaAnterior) > adaptador.getTortillasVentaPrimerVuelta()) {
                                lytTortilla.setError("Tortilla disponible ("+adaptador.getTortillasVentaPrimerVuelta()+" kgs)");
                                isReturn = true;
                            } else {
                                lytTortilla.setError(null);
                            }
                            if (adaptador.getTotoposVentaPrimerVuelta() >= 0 && totopos != 0 && totopos > adaptador.getTotoposVentaPrimerVuelta() && totopos > totopoAnterior && (totopos - totopoAnterior) > adaptador.getTotoposVentaPrimerVuelta()) {
                                lytTotopos.setError("Totopos disponible ("+adaptador.getTotoposVentaPrimerVuelta()+" kgs)");
                                isReturn = true;
                            } else {
                                lytTotopos.setError(null);
                            }
                            if (isReturn) {
                                return;
                            }
                        } else {
                            boolean isReturn = false;
                            if (adaptador.getMasaVentaSegundaVuelta() >= 0 && masa != 0 && masa > adaptador.getMasaVentaSegundaVuelta() && masa > masaAnterior && (masa - masaAnterior) > adaptador.getMasaVentaSegundaVuelta()) {
                                lytMasa.setError("Masa disponible (" + adaptador.getMasaVentaSegundaVuelta() + " kgs)");
                                isReturn = true;
                            } else {
                                lytMasa.setError(null);
                            }
                            if (adaptador.getTortillasVentaSegundaVuelta() >= 0 && tortilla != 0 && tortilla > adaptador.getTortillasVentaSegundaVuelta() && tortilla > tortillaAnterior && (tortilla - tortillaAnterior) > adaptador.getTortillasVentaSegundaVuelta()) {
                                lytTortilla.setError("Tortilla disponible (" + adaptador.getTortillasVentaSegundaVuelta() + " kgs)");
                                isReturn = true;
                            } else {
                                lytTortilla.setError(null);
                            }
                            if (adaptador.getTotoposVentaSegundaVuelta() >= 0 && totopos != 0 && totopos > adaptador.getTotoposVentaSegundaVuelta() && totopos > totopoAnterior && (totopos - totopoAnterior) > adaptador.getTotoposVentaSegundaVuelta()) {
                                lytTotopos.setError("Totopos disponible ("+adaptador.getTotoposVentaSegundaVuelta()+" kgs)");
                                isReturn = true;
                            } else {
                                lytTotopos.setError(null);
                            }
                            if (isReturn) {
                                return;
                            }
                        }
                    }
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("idCliente",cliente.getIdCliente());
                    HashMap<String, Object> vueltaMap = new HashMap<>();
                    vueltaMap.put("totopos",totopos);
                    vueltaMap.put("masa",masa);
                    vueltaMap.put("tortillas",tortilla);
                    DatabaseReference ref =  FirebaseDatabase.getInstance().getReference("AuxVentaRepartidor")
                            .child(ventaRepartidor.getIdEmpleado())
                            .child(ventaRepartidor.getIdVenta())
                            .child(cliente.getIdCliente());
                    if (devolucion) {
                        hashMap.put("devolucion",vueltaMap);
                    } else if (primero) {
                        hashMap.put("vuelta1",vueltaMap);
                    } else {
                        hashMap.put("vuelta2",vueltaMap);
                    }
                    double finalTortilla = tortilla;
                    double finalMasa = masa;
                    double finalTotopos = totopos;
                    ref.updateChildren(hashMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference("Productos")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    tortillaVenta = 0.0; totoposVenta = 0.0; masaVenta = 0.0;
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                                    {
                                                        Producto p = snapshot.getValue(Producto.class);
                                                        if (p.getNombre().toUpperCase().contains("TORTILLA")) {
                                                            if (cliente.isPreferencial()) {
                                                                for (int x = 0; x < cliente.getPrecios().size(); x++) {
                                                                    StringTokenizer a = new StringTokenizer(cliente.getPrecios().get("p"+x),"?");
                                                                    double precio = Double.parseDouble(a.nextToken());
                                                                    String id = a.nextToken();
                                                                    if (id.equals(p.getIdProducto())) {
                                                                        tortillaVenta = precio * finalTortilla;
                                                                    }
                                                                }
                                                            } else {
                                                                if (p.isModificado()) {
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
                                                                                            if (p.getAlta() < ventaRepartidor.getTiempo() && ventaRepartidor.getTiempo() < pd.getFecha()) {
                                                                                                precioProducto = pd.getPrecio();
                                                                                                activo = true;
                                                                                                break;
                                                                                            }
                                                                                            ant = new Modificado(pd);
                                                                                        } else {
                                                                                            if (ant.getFecha() < ventaRepartidor.getTiempo() && ventaRepartidor.getTiempo() < pd.getFecha()) {
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

                                                                                    tortillaVenta = precioProducto * finalTortilla;

                                                                                    vueltaMap.put("totoposVenta",totoposVenta);
                                                                                    vueltaMap.put("masaVenta",masaVenta);
                                                                                    vueltaMap.put("tortillaVenta",tortillaVenta);
                                                                                    if (devolucion) {
                                                                                        hashMap.put("devolucion",vueltaMap);
                                                                                    } else if (primero) {
                                                                                        hashMap.put("vuelta1",vueltaMap);
                                                                                    } else {
                                                                                        hashMap.put("vuelta2",vueltaMap);
                                                                                    }
                                                                                    ref.updateChildren(hashMap);
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                } else {
                                                                    tortillaVenta = p.getPrecio() * finalTortilla;
                                                                }
                                                            }
                                                        }
                                                        if (p.getNombre().toUpperCase().contains("MASA")) {
                                                            if (cliente.isPreferencial()) {
                                                                for (int x = 0; x < cliente.getPrecios().size(); x++) {
                                                                    StringTokenizer a = new StringTokenizer(cliente.getPrecios().get("p"+x),"?");
                                                                    double precio = Double.parseDouble(a.nextToken());
                                                                    String id = a.nextToken();
                                                                    if (id.equals(p.getIdProducto())) {
                                                                        masaVenta = precio * finalMasa;
                                                                    }
                                                                }
                                                            } else {
                                                                if (p.isModificado()) {
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
                                                                                            if (p.getAlta() < ventaRepartidor.getTiempo() && ventaRepartidor.getTiempo() < pd.getFecha()) {
                                                                                                precioProducto = pd.getPrecio();
                                                                                                activo = true;
                                                                                                break;
                                                                                            }
                                                                                            ant = new Modificado(pd);
                                                                                        } else {
                                                                                            if (ant.getFecha() < ventaRepartidor.getTiempo() && ventaRepartidor.getTiempo() < pd.getFecha()) {
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

                                                                                    masaVenta = precioProducto * finalMasa;

                                                                                    vueltaMap.put("totoposVenta",totoposVenta);
                                                                                    vueltaMap.put("masaVenta",masaVenta);
                                                                                    vueltaMap.put("tortillaVenta",tortillaVenta);
                                                                                    if (devolucion) {
                                                                                        hashMap.put("devolucion",vueltaMap);
                                                                                    } else if (primero) {
                                                                                        hashMap.put("vuelta1",vueltaMap);
                                                                                    } else {
                                                                                        hashMap.put("vuelta2",vueltaMap);
                                                                                    }
                                                                                    ref.updateChildren(hashMap);
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                } else {
                                                                    masaVenta = p.getPrecio() * finalMasa;
                                                                }
                                                            }
                                                        }
                                                        if (p.getNombre().toUpperCase().contains("TOTOPO")) {
                                                            if (cliente.isPreferencial()) {
                                                                for (int x = 0; x < cliente.getPrecios().size(); x++) {
                                                                    StringTokenizer a = new StringTokenizer(cliente.getPrecios().get("p"+x),"?");
                                                                    double precio = Double.parseDouble(a.nextToken());
                                                                    String id = a.nextToken();
                                                                    if (id.equals(p.getIdProducto())) {
                                                                        totoposVenta = precio * finalTotopos;
                                                                    }
                                                                }
                                                            } else {
                                                                if (p.isModificado()) {
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
                                                                                            if (p.getAlta() < ventaRepartidor.getTiempo() && ventaRepartidor.getTiempo() < pd.getFecha()) {
                                                                                                precioProducto = pd.getPrecio();
                                                                                                activo = true;
                                                                                                break;
                                                                                            }
                                                                                            ant = new Modificado(pd);
                                                                                        } else {
                                                                                            if (ant.getFecha() < ventaRepartidor.getTiempo() && ventaRepartidor.getTiempo() < pd.getFecha()) {
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

                                                                                    totoposVenta = precioProducto * finalTotopos;

                                                                                    vueltaMap.put("totoposVenta",totoposVenta);
                                                                                    vueltaMap.put("masaVenta",masaVenta);
                                                                                    vueltaMap.put("tortillaVenta",tortillaVenta);
                                                                                    if (devolucion) {
                                                                                        hashMap.put("devolucion",vueltaMap);
                                                                                    } else if (primero) {
                                                                                        hashMap.put("vuelta1",vueltaMap);
                                                                                    } else {
                                                                                        hashMap.put("vuelta2",vueltaMap);
                                                                                    }
                                                                                    ref.updateChildren(hashMap);
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                } else {
                                                                    totoposVenta = p.getPrecio() * finalTotopos;
                                                                }
                                                            }
                                                        }
                                                        vueltaMap.put("totoposVenta",totoposVenta);
                                                        vueltaMap.put("masaVenta",masaVenta);
                                                        vueltaMap.put("tortillaVenta",tortillaVenta);
                                                        if (devolucion) {
                                                            hashMap.put("devolucion",vueltaMap);
                                                        } else if (primero) {
                                                            hashMap.put("vuelta1",vueltaMap);
                                                        } else {
                                                            hashMap.put("vuelta2",vueltaMap);
                                                        }
                                                        ref.updateChildren(hashMap);
                                                    }
                                                    dismiss();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                }
                            });
                });

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener(view1 ->  dismiss() );

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

    private void hideTxt(TextInputEditText txt) {
        txt.setClickable(false);
        txt.setLongClickable(false);
        txt.setFocusable(false);
        txt.setCursorVisible(false);
    }
}