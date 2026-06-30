/**
 * Modelo Consumo
 * Representa el registro de consumo energético de un cliente en un período.
 */
export class Consumo {
  /**
   * @param {object} data
   * @param {string} [data.id]
   * @param {string} data.clienteId
   * @param {number} data.kwh           - Kilowatts-hora consumidos
   * @param {string} data.periodo       - Período (YYYY-MM, ej. "2024-01")
   * @param {string} [data.observacion]
   * @param {string} [data.creadoEn]
   */
  constructor({ id, clienteId, kwh, periodo, observacion, creadoEn } = {}) {
    this.id = id || crypto.randomUUID();
    this.clienteId = clienteId || '';
    this.kwh = Number(kwh) || 0;
    this.periodo = periodo || '';
    this.observacion = observacion || '';
    this.creadoEn = creadoEn || new Date().toISOString();
  }

  validate() {
    const errors = [];
    if (!this.clienteId) errors.push('Debe seleccionar un cliente.');
    if (!this.kwh || this.kwh <= 0) errors.push('El consumo (kWh) debe ser mayor que 0.');
    if (!this.periodo) errors.push('El período es obligatorio.');
    return errors;
  }

  toJSON() {
    return {
      id: this.id,
      clienteId: this.clienteId,
      kwh: this.kwh,
      periodo: this.periodo,
      observacion: this.observacion,
      creadoEn: this.creadoEn,
    };
  }
}
