package org.RHV.model;
/*
* Esta clase representa el consumo eléctrico del cliente.
* Incluye cuántos kWh usó y entre qué fechas.
* */
import java.time.LocalDate;

public class Consumption {

    private double kWh;
    private LocalDate startDate;
    private LocalDate endDate;

    public Consumption(double kWh, LocalDate startDate, LocalDate endDate) {
        this.kWh = kWh;
        this.startDate = startDate;
        this.endDate = endDate;
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

    @Override
    public String toString() {
        return "Consumption{" +
                "kWh=" + kWh +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

