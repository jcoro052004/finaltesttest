package com.example.afinal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.afinal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón para agregar cita
        binding.btnAddCita.setOnClickListener {
            val intent = Intent(this, AddAppointmentActivity::class.java)
            startActivity(intent)
        }

        // Botón para ver citas
        binding.btnViewCitas.setOnClickListener {
            val intent = Intent(this, ViewAppointmentsActivity::class.java)
            startActivity(intent)
        }
    }
}