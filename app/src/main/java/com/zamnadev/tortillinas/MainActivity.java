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
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Fragments.AdminFragment;
import com.zamnadev.tortillinas.Fragments.ClientesFragment;
import com.zamnadev.tortillinas.Fragments.HomeFragment;
import com.zamnadev.tortillinas.Fragments.VentasFragment;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fm = getSupportFragmentManager();
        fragmentHome = new HomeFragment();
        fragmentVentas = new VentasFragment();
        currentFragment = fragmentHome;
        fm.beginTransaction().add(R.id.container, fragmentHome).commit();
        fm.beginTransaction().add(R.id.container, fragmentVentas).hide(fragmentVentas).commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        if (ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()) != null) {
            refEmpleado = FirebaseDatabase.getInstance().getReference("Empleados")
                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()));
            listenerEmpleado = refEmpleado.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("Data", dataSnapshot.toString());
                    Empleado empleado = dataSnapshot.getValue(Empleado.class);
                    switch (empleado.getTipo()) {
                        case Empleado.TIPO_ADMIN: {
                            fragmentClientes = new ClientesFragment();
                            fragmentAdministrador = new AdminFragment();
                            fm.beginTransaction().add(R.id.container, fragmentClientes).hide(fragmentClientes).commit();
                            fm.beginTransaction().add(R.id.container, fragmentAdministrador).hide(fragmentAdministrador).commit();
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

    private void showFragment(Fragment fragment) {
        fm.beginTransaction().hide(currentFragment).show(fragment).commit();
        currentFragment = fragment;
    }
}