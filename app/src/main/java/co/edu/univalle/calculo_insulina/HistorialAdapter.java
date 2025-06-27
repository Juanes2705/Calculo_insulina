package co.edu.univalle.calculo_insulina;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private List<RegistroInsulina> registros;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(RegistroInsulina registro);
    }

    public HistorialAdapter(List<RegistroInsulina> registros, OnItemClickListener listener) {
        this.registros = registros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_registro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegistroInsulina registro = registros.get(position);
        holder.bind(registro);
    }

    @Override
    public int getItemCount() {
        return registros.size();
    }

    public void updateData(List<RegistroInsulina> newRegistros) {
        this.registros = newRegistros;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewFecha;
        private TextView textViewTipoComida;
        private TextView textViewGlicemia;
        private TextView textViewCarbohidratos;
        private TextView textViewInsulinaTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewTipoComida = itemView.findViewById(R.id.textViewTipoComida);
            textViewGlicemia = itemView.findViewById(R.id.textViewGlicemia);
            textViewCarbohidratos = itemView.findViewById(R.id.textViewCarbohidratos);
            textViewInsulinaTotal = itemView.findViewById(R.id.textViewInsulinaTotal);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(registros.get(position));
                }
            });
        }

        public void bind(RegistroInsulina registro) {
            textViewFecha.setText(registro.getFecha());
            textViewTipoComida.setText(registro.getTipoComida());
            textViewGlicemia.setText(registro.getGlicemia() + " mg/dL");
            textViewCarbohidratos.setText(String.format("%.1f g", registro.getCarbohidratos()));
            textViewInsulinaTotal.setText(String.format("%.2f", registro.getInsulinaTotal()));
        }
    }
} 