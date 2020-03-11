package com.zamnadev.tortillinas.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zamnadev.tortillinas.Adaptadores.ViewPagerAdapter;
import com.zamnadev.tortillinas.R;

public class AdminFragment extends Fragment {
    private TabLayout tabLayout;

    private ViewPager viewPager;

    private ViewPagerAdapter adapter;

    public AdminFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);
        adapter.addFragment(new EmpleadosFragment(), "Empleados");
        adapter.addFragment(new SucursalesFragment(), "Sucursales");
        adapter.addFragment(new ProductosFragment(), "Productos");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}