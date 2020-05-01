package com.zamnadev.tortillinas;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.zamnadev.tortillinas.Dialogs.MessageDialog;
import com.zamnadev.tortillinas.Dialogs.MessageDialogBuilder;
import com.zamnadev.tortillinas.Firma.FirmaActivity;
import com.zamnadev.tortillinas.Fragments.AdminFragment;
import com.zamnadev.tortillinas.Fragments.ClientesFragment;
import com.zamnadev.tortillinas.Fragments.HomeFragment;
import com.zamnadev.tortillinas.Fragments.VentasFragment;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.Moldes.Vuelta;
import com.zamnadev.tortillinas.Notificaciones.Data;
import com.zamnadev.tortillinas.Notificaciones.FCMService;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, FCMService.onConfirmacion {
    private Toolbar toolbar;

    private Fragment fragmentHome;
    private Fragment fragmentClientes;
    private Fragment fragmentVentas;
    private Fragment fragmentAdministrador;
    private Fragment currentFragment;

    private FragmentManager fm;

    private DatabaseReference refEmpleado;

    private ValueEventListener listenerEmpleado;

    private String idVenta;
    private int tipo;
    private String idSucursal;
    private Vuelta vuelta;

    private static final int CODE_INTENT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean notificacion = getIntent().getBooleanExtra("notificacion",false);
        if (notificacion) {
            String idVenta = getIntent().getStringExtra("idVenta");
            int tipo = getIntent().getIntExtra("tipo",-1);
            String emisor = getIntent().getStringExtra("emisor");
            if (tipo == -1 || idVenta == null || emisor == null) {
                return;
            }
            mostrarAlertaVenta(idVenta,tipo,emisor);
        }

        fragmentHome = new HomeFragment();
        fragmentClientes = new ClientesFragment();
        fragmentVentas = new VentasFragment();
        fragmentAdministrador = new AdminFragment();
        currentFragment = fragmentHome;
        FCMService.setLISTENER(MainActivity.this);

        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.container, fragmentHome).commit();
        fm.beginTransaction().add(R.id.container, fragmentClientes).hide(fragmentClientes).commit();
        fm.beginTransaction().add(R.id.container, fragmentVentas).hide(fragmentVentas).commit();
        fm.beginTransaction().add(R.id.container, fragmentAdministrador).hide(fragmentAdministrador).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.getMenu().getItem(1).setEnabled(false);
        bottomNavigationView.getMenu().getItem(3).setEnabled(false);

        if (ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()) != null) {
            refEmpleado = FirebaseDatabase.getInstance().getReference("Empleados")
                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()));
            listenerEmpleado = refEmpleado.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Empleado empleado = dataSnapshot.getValue(Empleado.class);
                    if (!empleado.isConexion()) {
                        return;
                    }
                    switch (empleado.getTipo()) {
                        case Empleado.TIPO_ADMIN: {
                            fragmentClientes = new ClientesFragment();
                            fragmentAdministrador = new AdminFragment();
                            if (fm != null) {
                                fm.beginTransaction().add(R.id.container, fragmentClientes).hide(fragmentClientes).commit();
                                fm.beginTransaction().add(R.id.container, fragmentAdministrador).hide(fragmentAdministrador).commit();
                                bottomNavigationView.getMenu().getItem(1).setEnabled(true);
                                bottomNavigationView.getMenu().getItem(3).setEnabled(true);
                            }
                            break;
                        }
                        case Empleado.TIPO_REPARTIDOR:
                        case Empleado.TIPO_MOSTRADOR: {
                            bottomNavigationView.getMenu().removeItem(R.id.navigation_clientes);
                            bottomNavigationView.getMenu().removeItem(R.id.navigation_administrador);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

        }

        //Valida la primera clave para enviar el mensaje, genera el token por primera vez
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    String token = task.getResult().getToken();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens")
                            .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))
                            .child("token");

                    reference.setValue(token);
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    public static String getFecha() {
        TimeZone tz = TimeZone.getTimeZone("GMT-05:00");
        Calendar c = Calendar.getInstance(tz);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fecha = sdf.format(c.getTime());
        return fecha;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuCerrarSesion) {
            ControlSesiones.EliminaUsuario(getApplicationContext());
            refEmpleado.removeEventListener(listenerEmpleado);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home : {
                showFragment(fragmentHome);
                break;
            }
            case R.id.navigation_clientes : {
                showFragment(fragmentClientes);
                break;
            }
            case R.id.navigation_ventas : {
                showFragment(fragmentVentas);
                break;
            }
            case R.id.navigation_administrador : {
                showFragment(fragmentAdministrador);
                break;
            }
        }
        return true;
    }

    private void showFragment(Fragment fragment) {
        fm.beginTransaction().hide(currentFragment).show(fragment).commit();
        currentFragment = fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", ServerValue.TIMESTAMP);
        hashMap.put("conexion", true);
        refEmpleado.updateChildren(hashMap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", ServerValue.TIMESTAMP);
        hashMap.put("conexion", false);
        refEmpleado.updateChildren(hashMap);
    }

    @Override
    public void enviarMensaje(String idVenta, int tipo, String emisor) {
        mostrarAlertaVenta(idVenta, tipo, emisor);
    }

    public void mostrarAlertaVenta(String idVenta, int tipo, String emisor) {
        this.idVenta = idVenta;
        this.tipo = tipo;
        if (tipo == Data.TIPO_CONFIRMACION_REPARTIDOR_PRIMER_VUELTA) {
            FirebaseDatabase.getInstance().getReference("VentasMostrador")
                    .child(emisor)
                    .child(idVenta)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            VentaMostrador ventaMostrador = dataSnapshot.getValue(VentaMostrador.class);
                            idSucursal = ventaMostrador.getIdSucursal();
                            FirebaseDatabase.getInstance().getReference("Sucursales")
                                    .child(ventaMostrador.getIdSucursal())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                                            FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                                                    .child(idVenta)
                                                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))
                                                    .child("vuelta1")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            Vuelta vuelta = dataSnapshot.getValue(Vuelta.class);
                                                            getMe().vuelta = vuelta;
                                                            String text = "";
                                                            text += sucursal.getNombre() + "\n";
                                                            text += "Primer vuelta";
                                                            if (vuelta.getTortillas() > 0) {
                                                                text += "\n\t\tTortillas: " +  vuelta.getTortillas() + " kgs.";
                                                            }
                                                            if (vuelta.getMasa() > 0) {
                                                                text += "\n\t\tMasa: " +  vuelta.getMasa() + " kgs.";
                                                            }
                                                            if (vuelta.getTotopos() > 0) {
                                                                text += "\n\t\tTotopos: " +  vuelta.getTotopos() + " kgs.";
                                                            }
                                                            MessageDialog dialog = new MessageDialog(MainActivity.this, new MessageDialogBuilder()
                                                                    .setTitle("Alerta")
                                                                    .setMessage(text + "\nIngrese su firma para confirmar")
                                                                    .setPositiveButtonText("Firmar")
                                                                    .setNegativeButtonText("Cancelar")
                                                            );
                                                            dialog.show();
                                                            dialog.setPositiveButtonListener(v -> {
                                                                startActivityForResult(new Intent(MainActivity.this, FirmaActivity.class),CODE_INTENT);
                                                                dialog.dismiss();
                                                            });
                                                            dialog.setNegativeButtonListener(v -> dialog.dismiss());
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else if (tipo == Data.TIPO_CONFIRMACION_REPARTIDOR_SEGUNDA_VUELTA) {
            FirebaseDatabase.getInstance().getReference("VentasMostrador")
                    .child(emisor)
                    .child(idVenta)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            VentaMostrador ventaMostrador = dataSnapshot.getValue(VentaMostrador.class);
                            idSucursal = ventaMostrador.getIdSucursal();
                            FirebaseDatabase.getInstance().getReference("Sucursales")
                                    .child(ventaMostrador.getIdSucursal())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                                            FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                                                    .child(idVenta)
                                                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))
                                                    .child("vuelta2")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            Vuelta vuelta = dataSnapshot.getValue(Vuelta.class);
                                                            getMe().vuelta = vuelta;
                                                            String text = "";
                                                            text += sucursal.getNombre() + "\n";
                                                            text += "Segunda vuelta";
                                                            if (vuelta.getTortillas() > 0) {
                                                                text += "\n\t\tTortillas: " +  vuelta.getTortillas() + " kgs.";
                                                            }
                                                            if (vuelta.getMasa() > 0) {
                                                                text += "\n\t\tMasa: " +  vuelta.getMasa() + " kgs.";
                                                            }
                                                            if (vuelta.getTotopos() > 0) {
                                                                text += "\n\t\tTotopos: " +  vuelta.getTotopos() + " kgs.";
                                                            }
                                                            MessageDialog dialog = new MessageDialog(MainActivity.this, new MessageDialogBuilder()
                                                                    .setTitle("Alerta")
                                                                    .setMessage(text + "\nIngrese su firma para confirmar")
                                                                    .setPositiveButtonText("Firmar")
                                                                    .setNegativeButtonText("Cancelar")
                                                            );
                                                            dialog.show();
                                                            dialog.setPositiveButtonListener(v -> {
                                                                startActivityForResult(new Intent(MainActivity.this, FirmaActivity.class),CODE_INTENT);
                                                                dialog.dismiss();
                                                            });
                                                            dialog.setNegativeButtonListener(v -> dialog.dismiss());
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private MainActivity getMe() {
        return this;
    }

    private void alta() {
        if (idVenta == null) {
            return;
        }
        if (tipo == Data.TIPO_CONFIRMACION_REPARTIDOR_PRIMER_VUELTA) {
            FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                    .child(idVenta)
                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))
                    .child("vuelta1")
                    .child("confirmado")
                    .setValue(true);
            FirebaseDatabase.getInstance().getReference("Confirmaciones")
                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))
                    .child(idVenta)
                    .child("vuelta1")
                    .child("confirmado")
                    .setValue(true);
            altaVentaRepartidor(idSucursal,idVenta,vuelta,true);
        } else if (tipo == Data.TIPO_CONFIRMACION_REPARTIDOR_SEGUNDA_VUELTA) {
            FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                    .child(idVenta)
                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))
                    .child("vuelta2")
                    .child("confirmado")
                    .setValue(true);
            FirebaseDatabase.getInstance().getReference("Confirmaciones")
                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))
                    .child(idVenta)
                    .child("vuelta2")
                    .child("confirmado")
                    .setValue(true);
            altaVentaRepartidor(idSucursal,idVenta,vuelta,false);
        }
    }

    public void altaVentaRepartidor(String idSucursal, String idVenta, Vuelta vuelta, boolean primero) {
        DatabaseReference refVenta = FirebaseDatabase.getInstance().getReference("VentasRepartidor")
                .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idVenta",idVenta);
        HashMap<String, Object> vueltaMap = new HashMap<>();
        vueltaMap.put("masa",vuelta.getMasa());
        vueltaMap.put("tortillas",vuelta.getTortillas());
        vueltaMap.put("totopos",vuelta.getTotopos());
        vueltaMap.put("time",ServerValue.TIMESTAMP);
        vueltaMap.put("registrada",true);
        vueltaMap.put("confirmado",true);
        if (primero) {
            hashMap.put("vuelta1",vueltaMap);
        } else {
            hashMap.put("vuelta2",vueltaMap);
        }
        String fecha;
        hashMap.put("tiempo", ServerValue.TIMESTAMP);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fecha = sdf.format(calendar.getTime());
        hashMap.put("fecha",fecha);
        hashMap.put("idSucursal",idSucursal);
        hashMap.put("idEmpleado",ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()));
        refVenta.child(idVenta).updateChildren(hashMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(this, "Venta agrega con éxito", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_INTENT) {
            if (resultCode == FirmaActivity.FIRMA_ACEPTADA) {
                alta();
            } else {
                Toast.makeText(this, "Error al firmar la entrega", Toast.LENGTH_SHORT).show();
            }
        }
    }
}