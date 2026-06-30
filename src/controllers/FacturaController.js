import { Factura } from '../models/Factura.js';
import { StorageService } from '../services/StorageService.js';

export const facturaStore = new StorageService('facturas');

export const FacturaController = {
  getAll() {
    return facturaStore.findAll().map((d) => new Factura(d));
  },

  getById(id) {
    const data = facturaStore.findById(id);
    return data ? new Factura(data) : null;
  },

  getByCliente(clienteId) {
    return facturaStore.findWhere({ clienteId }).map((d) => new Factura(d));
  },

  /**
   * Genera una factura desde un objeto consumo.
   * @param {import('../models/Consumo.js').Consumo} consumo
   * @returns {{ ok: boolean, errors?: string[], factura?: Factura }}
   */
  generarDesdeConsumo(consumo) {
    const factura = new Factura({
      clienteId: consumo.clienteId,
      consumoId: consumo.id,
      kwh: consumo.kwh,
      periodo: consumo.periodo,
    });
    const errors = factura.validate();
    if (errors.length) return { ok: false, errors };
    facturaStore.insert(factura.toJSON());
    return { ok: true, factura };
  },

  /**
   * Cambia el estado de una factura.
   * @param {string} id
   * @param {'pendiente'|'pagada'|'vencida'} nuevoEstado
   */
  cambiarEstado(id, nuevoEstado) {
    const data = facturaStore.findById(id);
    if (!data) return { ok: false, errors: ['Factura no encontrada.'] };
    data.estado = nuevoEstado;
    facturaStore.update(data);
    return { ok: true, factura: new Factura(data) };
  },

  delete(id) {
    return facturaStore.delete(id);
  },

  /** Suma total facturado ($). */
  totalFacturado() {
    return this.getAll().reduce((sum, f) => sum + f.total, 0);
  },

  /** Suma total facturado pendiente ($). */
  totalPendiente() {
    return this.getAll()
      .filter((f) => f.estado === 'pendiente')
      .reduce((sum, f) => sum + f.total, 0);
  },
};
