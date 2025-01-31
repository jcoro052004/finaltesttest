package com.example.afinal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AddAppointmentActivity extends AppCompatActivity {

    private Spinner spinnerSalas, spinnerHoras;
    private EditText editFecha, editNotario, editDescripcion;
    private Button btnAddCita;
    private String salaSeleccionada, horaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        // Inicialitzar els elements de la interfície
        editNotario = findViewById(R.id.editNotario);
        editFecha = findViewById(R.id.editFecha);
        editDescripcion = findViewById(R.id.editDescripcion);
        spinnerSalas = findViewById(R.id.spinnerSalas);
        spinnerHoras = findViewById(R.id.spinnerHoras);
        btnAddCita = findViewById(R.id.btnAddCita);

        // Configurar el selector de data
        editFecha.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddAppointmentActivity.this,
                    (DatePicker view1, int selectedYear, int selectedMonth, int selectedDay) -> {
                        // Crear una data seleccionada en format "dd/MM/yyyy"
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        // Comprovar si la data és posterior o igual a l'actual
                        if (isDateValid(selectedYear, selectedMonth, selectedDay)) {
                            editFecha.setText(selectedDate);
                        } else {
                            Toast.makeText(AddAppointmentActivity.this, "No pots seleccionar una data anterior a l'actual", Toast.LENGTH_SHORT).show();
                        }
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });

        // Configurar el Spinner de sales
        ArrayAdapter<CharSequence> adapterSalas = ArrayAdapter.createFromResource(
                this,
                R.array.opciones_salas,
                android.R.layout.simple_spinner_item
        );
        adapterSalas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSalas.setAdapter(adapterSalas);
        spinnerSalas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                salaSeleccionada = parent.getItemAtPosition(position).toString();
                Toast.makeText(AddAppointmentActivity.this, "Sala seleccionada: " + salaSeleccionada, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Configurar el Spinner d’hores
        ArrayAdapter<CharSequence> adapterHoras = ArrayAdapter.createFromResource(
                this,
                R.array.opciones_horas,
                android.R.layout.simple_spinner_item
        );
        adapterHoras.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHoras.setAdapter(adapterHoras);
        spinnerHoras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horaSeleccionada = parent.getItemAtPosition(position).toString();
                Toast.makeText(AddAppointmentActivity.this, "Hora seleccionada: " + horaSeleccionada, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Acció del botó per afegir la cita
        btnAddCita.setOnClickListener(view -> {
            String notario = editNotario.getText().toString().trim();
            String fecha = editFecha.getText().toString().trim();
            String descripcion = editDescripcion.getText().toString().trim();

            if (notario.isEmpty() || fecha.isEmpty() || descripcion.isEmpty() || salaSeleccionada == null || horaSeleccionada == null) {
                Toast.makeText(this, "Tots els camps són obligatoris", Toast.LENGTH_SHORT).show();
            } else {
                // Comprovar si la sala està disponible per una cita
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                boolean isSalaDisponible = dbHelper.isSalaDisponible(salaSeleccionada, fecha, horaSeleccionada);

                if (isSalaDisponible) {
                    // Afegir la cita a la base de dades
                    boolean success = dbHelper.addCita(notario, salaSeleccionada, fecha, horaSeleccionada, descripcion);
                    if (success) {
                        Toast.makeText(this, "Cita afegida correctament!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error en afegir la cita", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "La sala ja està reservada per aquesta data i hora.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Funció per validar la data seleccionada (ha d'estar dins de la classe AddAppointmentActivity)
    private boolean isDateValid(int selectedYear, int selectedMonth, int selectedDay) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0); // Ignorem l'hora per comparació
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);

        return selectedDate.compareTo(currentDate) >= 0; // Comprovar si la data seleccionada és igual o posterior a la data actual
    }
}
