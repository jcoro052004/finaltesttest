package com.example.afinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Appointment> appointmentList;

    public AppointmentAdapter(Context context, ArrayList<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.txtNotario.setText("Notari: " + appointment.getNotario());
        holder.txtSala.setText("Sala: " + appointment.getSala());
        holder.txtFecha.setText("Data: " + appointment.getFecha());
        holder.txtHora.setText("Hora: " + appointment.getHora());
        holder.txtDescripcion.setText("Descripció: " + appointment.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNotario, txtSala, txtFecha, txtHora, txtDescripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNotario = itemView.findViewById(R.id.txtNotario);
            txtSala = itemView.findViewById(R.id.txtSala);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtHora = itemView.findViewById(R.id.txtHora);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
        }
    }
}