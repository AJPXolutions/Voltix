/**
 * Modelo Factura
 * Representa una factura generada a partir del consumo de un cliente.
 */

/** Precio por kWh en la moneda del sistema (configurable). */
export const PRECIO_KWH = 0.12; // USD por defecto

export class Factura {
  /**
   * @param {object} data
   * @param {string}  [data.id]
   * @param {string}  data.clienteId
   * @param {string}  data.consumoId
   * @param {number}  data.kwh
   * @param {string}  data.periodo
   * @param {number}  [data.precioKwh]
   * @param {string}  [data.estado]    - 'pendiente' | 'pagada' | 'vencida'
   * @param {string}  [data.emitidaEn]
   */
  constructor({ id, clienteId, consumoId, kwh, periodo, precioKwh, estado, emitidaEn } = {}) {
    this.id = id || crypto.randomUUID();
    this.clienteId = clienteId || '';
    this.consumoId = consumoId || '';
    this.kwh = Number(kwh) || 0;
    this.periodo = periodo || '';
    this.precioKwh = Number(precioKwh) || PRECIO_KWH;
    this.total = +(this.kwh * this.precioKwh).toFixed(2);
    this.estado = estado || 'pendiente';
    this.emitidaEn = emitidaEn || new Date().toISOString();
  }

  /** Recalcula el total (útil si se modifican campos). */
  recalcular() {
    this.total = +(this.kwh * this.precioKwh).toFixed(2);
  }

  validate() {
    const errors = [];
    if (!this.clienteId) errors.push('Debe estar asociada a un cliente.');
    if (!this.consumoId) errors.push('Debe estar asociada a un consumo.');
    if (this.kwh <= 0) errors.push('El consumo debe ser mayor que 0.');
    return errors;
  }

  toJSON() {
    return {
      id: this.id,
      clienteId: this.clienteId,
      consumoId: this.consumoId,
      kwh: this.kwh,
      periodo: this.periodo,
      precioKwh: this.precioKwh,
      total: this.total,
      estado: this.estado,
      emitidaEn: this.emitidaEn,
    };
  }
}
