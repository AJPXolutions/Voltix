package org.RHV.util;
/*Esta clase sirve para calcular la tarifa
y el total a pagar según el consumo de energía en kWh.*/
public class TariffCalculator {
    /*
    * Este método devuelve la tarifa por kWh dependiendo del consumo:
    * Si el cliente consume 0–100 kWh, paga 0.12 por kWh
    * Si consume 101–300 kWh, paga 0.15
    * Si consume más de 300 kWh, paga 0.20
    */
    public static double getRate(double kwh){
        if(kwh<=100){
            return 0.12; //Basic rate
        }else if (kwh<=300) {
            return 0.15; //Medium rate
        }else {
            return 0.20; // High rate
        }
    }

    /*
    * Este método calcula el total a pagar:
    * Primero obtiene la tarifa llamando a getRate(kWh)
    * Luego multiplica tarifa × consumo.
    * */
    public static  double calculateTotal(double kwh){
        double rate = getRate(kwh);
        return kwh * rate;
    }
}
