package com.zamnadev.tortillinas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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
import com.zamnadev.tortillinas.Fragments.AdminFragment;
import com.zamnadev.tortillinas.Fragments.ClientesFragment;
import com.zamnadev.tortillinas.Fragments.HomeFragment;
import com.zamnadev.tortillinas.Fragments.VentasFragment;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Notificaciones.Client;
import com.zamnadev.tortillinas.Notificaciones.Data;
import com.zamnadev.tortillinas.Notificaciones.FCMServiceAPI;
import com.zamnadev.tortillinas.Notificaciones.MyResponse;
import com.zamnadev.tortillinas.Notificaciones.Sender;
import com.zamnadev.tortillinas.Notificaciones.Token;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    private Fragment fragmentHome;
    private Fragment fragmentClientes;
    private Fragment fragmentVentas;
    private Fragment fragmentAdministrador;
    private Fragment currentFragment;

    private FragmentManager fm;

    private DatabaseReference refEmpleado;

    private ValueEventListener listenerEmpleado;

    private FCMServiceAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentHome = new HomeFragment();
        fragmentClientes = new ClientesFragment();
        fragmentVentas = new VentasFragment();
        fragmentAdministrador = new AdminFragment();
        currentFragment = fragmentHome;

        api = Client.getClient("https://fcm.googleapis.com/").create(FCMServiceAPI.class);

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

        //Envia un mensaje al mostrador
        enviarMensaje("-M34EaQ4s_qTqP9ZdNHH");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
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

    //Para enviar un mensaje ocupasmos el id del receptor
    public void enviarMensaje(final String receptor) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens")
                .child(receptor);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Token token = dataSnapshot.getValue(Token.class);
                Data data = new Data(receptor,ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()));
                Sender sender  = new Sender(data,token.getToken());

                api.enviarNotificacion(sender)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.code() == 200) {
                                    assert response.body() != null;
                                    if (response.body().success != 1) {
                                        Log.e("NOTIFICACION","Error con la notificacion");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
}