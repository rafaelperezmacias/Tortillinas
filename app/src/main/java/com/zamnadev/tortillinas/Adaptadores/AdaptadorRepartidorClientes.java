package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zamnadev.tortillinas.BottomSheets.ClientesBottomSheet;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorRepartidorClientes extends RecyclerView.Adapter<AdaptadorRepartidorClientes.ViewHolder> {

    private Context context;
    private ArrayList<Cliente> clientes;
    private FragmentManager fragmentManager;

    public AdaptadorRepartidorClientes(Context context, ArrayList<Cliente> clientes, FragmentManager fragmentManager)
    {
        this.context = context;
        this.clientes = clientes;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_repartidor_clientes,parent,false);
        return new AdaptadorRepartidorClientes.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.txtNombre.setText(cliente.getNombre().getNombres() + " " + cliente.getNombre().getApellidos());

        holder.btnCliente.setOnClickListener(view -> {
            ClientesBottomSheet bottomSheet = new ClientesBottomSheet(cliente,true);
            bottomSheet.show(fragmentManager,bottomSheet.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private ImageButton btnCliente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            btnCliente = (ImageButton) itemView.findViewById(R.id.btnMostrarCliente);
        }

    }
}
