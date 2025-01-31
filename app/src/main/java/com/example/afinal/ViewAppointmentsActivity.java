package com.example.afinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

public class ViewAppointmentsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewCitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        dbHelper = new DatabaseHelper(this);
        listViewCitas = findViewById(R.id.listViewCitas);

        loadAppointments();
    }

    private void loadAppointments() {
        List<Map<String, String>> citasList = dbHelper.getAllCitas();
        SimpleAdapter citasAdapter = new SimpleAdapter(
                this,
                citasList,
                R.layout.list_item_cita,
                new String[]{"notario", "sala", "fecha", "hora", "descripcion"}, // Elimina "id" de aquí
                new int[]{R.id.txtNotario, R.id.txtSala, R.id.txtFecha, R.id.txtHora, R.id.txtDescripcion} // Ajusta los índices
        );

        listViewCitas.setAdapter(citasAdapter);

        // Permitir eliminar una cita al hacer clic
        listViewCitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Map<String, String> selectedCita = citasList.get(position);
                int citaId = Integer.parseInt(selectedCita.get("id")); // Aún utilizamos "id" para eliminar

                showDeleteConfirmationDialog(citaId);
            }
        });
    }

    private void showDeleteConfirmationDialog(int citaId) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar cita")
                .setMessage("¿Estás seguro de que quieres eliminar esta cita?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteCita(citaId);
                        Toast.makeText(ViewAppointmentsActivity.this, "Cita eliminada", Toast.LENGTH_SHORT).show();
                        loadAppointments(); // Recargar lista después de eliminar
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}

