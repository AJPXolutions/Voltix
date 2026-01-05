package org.RHV.model;

import java.time.LocalDate;

/**
 * Representa el consumo eléctrico de un cliente en un periodo específico.
 * Incluye kWh consumidos y fechas de inicio/fin.
 */
public class Consumption {

    // ID generado por MySQL
    private int id;

    // Cantidad de energía consumida
    private double kWh;

    // Fecha de inicio del periodo
    private LocalDate startDate;

    // Fecha de fin del periodo
    private LocalDate endDate;

    /**
     * Constructor usado al crear un nuevo consumo desde la UI.
     */
    public Consumption(double kWh, LocalDate startDate, LocalDate endDate) {
        this.kWh = kWh;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Constructor usado al cargar consumos desde MySQL.
     */
    public Consumption(int id, double kWh, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.kWh = kWh;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    // Setter necesario para asignar el ID generado por MySQL
    public void setId(int id) {
        this.id = id;
    }

    public double getKWh() {
        return kWh;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
