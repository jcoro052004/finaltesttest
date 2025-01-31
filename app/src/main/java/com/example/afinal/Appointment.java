package com.example.afinal;

public class Appointment {
    private int id;
    private String notario, sala, fecha, hora, descripcion;

    public Appointment(int id, String notario, String sala, String fecha, String hora, String descripcion) {
        this.id = id;
        this.notario = notario;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public String getNotario() { return notario; }
    public String getSala() { return sala; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getDescripcion() { return descripcion; }
}
