/**
 * Modelo Cliente
 * Representa un cliente del sistema de facturación energética.
 */
export class Cliente {
  /**
   * @param {object} data
   * @param {string} [data.id]
   * @param {string} data.nombre
   * @param {string} data.email
   * @param {string} data.telefono
   * @param {string} data.direccion
   * @param {string} [data.creadoEn]
   */
  constructor({ id, nombre, email, telefono, direccion, creadoEn } = {}) {
    this.id = id || crypto.randomUUID();
    this.nombre = nombre || '';
    this.email = email || '';
    this.telefono = telefono || '';
    this.direccion = direccion || '';
    this.creadoEn = creadoEn || new Date().toISOString();
  }

  /** Valida los campos requeridos. Retorna lista de errores o [] si es válido. */
  validate() {
    const errors = [];
    if (!this.nombre.trim()) errors.push('El nombre es obligatorio.');
    if (!this.email.trim()) errors.push('El email es obligatorio.');
    if (this.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email)) {
      errors.push('El email no tiene un formato válido.');
    }
    return errors;
  }

  toJSON() {
    return {
      id: this.id,
      nombre: this.nombre,
      email: this.email,
      telefono: this.telefono,
      direccion: this.direccion,
      creadoEn: this.creadoEn,
    };
  }
}
