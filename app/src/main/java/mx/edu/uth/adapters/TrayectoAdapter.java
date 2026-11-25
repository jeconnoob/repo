package mx.edu.uth.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mx.edu.uth.R;
import mx.edu.uth.dto.Trayecto;

public class TrayectoAdapter extends RecyclerView.Adapter<TrayectoAdapter.TrayectoViewHolder> {
    public interface OnTrayectoClickListener{
        void onTrayectoClick(Trayecto trayecto);
    }
    private final List<Trayecto> trayectos;
    private final OnTrayectoClickListener listener;
    public TrayectoAdapter(List<Trayecto> trayectos, OnTrayectoClickListener listener){
        this.trayectos=trayectos;
        this.listener=listener;
    }
    static class TrayectoViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre;
        TextView tvDetalle;
        TrayectoViewHolder(@NonNull View itemView){
            super(itemView);
            tvNombre=itemView.findViewById(R.id.tvNombre);
            tvDetalle=itemView.findViewById(R.id.tvDetalle);
        }
    }
    @NonNull
    @Override
    public TrayectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View vista= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trayecto,parent,false);
        return new TrayectoViewHolder(vista);
    }
    @Override
    public void onBindViewHolder(@NonNull TrayectoViewHolder holder, int position){
        Trayecto t = trayectos.get(position);
        holder.tvNombre.setText(t.getNombre());
        holder.tvDetalle.setText(t.getPuntos()+"puntos"+t.getFecha_creacion());
        holder.itemView.setOnClickListener(view->listener.onTrayectoClick(t));
    }
    @Override
    public int getItemCount(){
        return trayectos.size();
    }
}
