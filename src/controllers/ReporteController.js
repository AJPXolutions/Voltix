import { ClienteController } from './ClienteController.js';
import { ConsumoController } from './ConsumoController.js';
import { FacturaController } from './FacturaController.js';

export const ReporteController = {
  /** Resumen general para el dashboard. */
  resumenGeneral() {
    const clientes = ClienteController.getAll();
    const consumos = ConsumoController.getAll();
    const facturas = FacturaController.getAll();

    const totalKwh = consumos.reduce((s, c) => s + c.kwh, 0);
    const totalFacturado = facturas.reduce((s, f) => s + f.total, 0);
    const pendiente = facturas
      .filter((f) => f.estado === 'pendiente')
      .reduce((s, f) => s + f.total, 0);
    const pagadas = facturas.filter((f) => f.estado === 'pagada').length;

    return {
      totalClientes: clientes.length,
      totalConsumos: consumos.length,
      totalFacturas: facturas.length,
      totalKwh: +totalKwh.toFixed(2),
      totalFacturado: +totalFacturado.toFixed(2),
      pendiente: +pendiente.toFixed(2),
      facturasPagadas: pagadas,
    };
  },

  /**
   * Consumo agrupado por período (últimos N períodos).
   * @param {number} [n=6]
   * @returns {Array<{periodo:string, kwh:number}>}
   */
  consumoPorPeriodo(n = 6) {
    const consumos = ConsumoController.getAll();
    const map = {};
    consumos.forEach((c) => {
      map[c.periodo] = (map[c.periodo] || 0) + c.kwh;
    });
    return Object.entries(map)
      .sort(([a], [b]) => b.localeCompare(a))
      .slice(0, n)
      .reverse()
      .map(([periodo, kwh]) => ({ periodo, kwh: +kwh.toFixed(2) }));
  },

  /**
   * Top N clientes por consumo total.
   * @param {number} [n=5]
   */
  topClientesPorConsumo(n = 5) {
    const clientes = ClienteController.getAll();
    return clientes
      .map((c) => ({
        cliente: c,
        kwh: ConsumoController.totalKwhByCliente(c.id),
      }))
      .sort((a, b) => b.kwh - a.kwh)
      .slice(0, n);
  },
};
