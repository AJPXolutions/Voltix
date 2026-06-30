/**
 * StorageService
 *
 * Capa de persistencia basada en localStorage.
 * Reemplaza la conexión a MySQL en la versión desktop.
 * Expone una interfaz CRUD genérica por colección (tabla).
 *
 * Nota de migración: Para conectar una base de datos real en el futuro
 * (MySQL, PostgreSQL, etc.) basta con reemplazar esta implementación
 * manteniendo la misma interfaz pública.
 */
export class StorageService {
  /**
   * @param {string} collection - Nombre de la colección (equivale a una tabla)
   */
  constructor(collection) {
    this._key = `voltix_${collection}`;
  }

  /** Retorna todos los registros de la colección. */
  findAll() {
    try {
      return JSON.parse(localStorage.getItem(this._key) || '[]');
    } catch {
      return [];
    }
  }

  /** Busca un registro por id. Retorna undefined si no existe. */
  findById(id) {
    return this.findAll().find((r) => r.id === id);
  }

  /**
   * Inserta un nuevo registro.
   * @param {object} record - Debe tener propiedad `id`.
   * @returns {object} El registro insertado.
   */
  insert(record) {
    const all = this.findAll();
    all.push(record);
    this._save(all);
    return record;
  }

  /**
   * Actualiza un registro existente (match por id).
   * @param {object} record - Debe tener propiedad `id`.
   * @returns {object|null} El registro actualizado, o null si no existe.
   */
  update(record) {
    const all = this.findAll();
    const idx = all.findIndex((r) => r.id === record.id);
    if (idx === -1) return null;
    all[idx] = record;
    this._save(all);
    return record;
  }

  /**
   * Elimina un registro por id.
   * @param {string} id
   * @returns {boolean} true si fue eliminado, false si no existía.
   */
  delete(id) {
    const all = this.findAll();
    const filtered = all.filter((r) => r.id !== id);
    if (filtered.length === all.length) return false;
    this._save(filtered);
    return true;
  }

  /** Busca registros que coincidan con los pares clave-valor de `criteria`. */
  findWhere(criteria) {
    return this.findAll().filter((r) =>
      Object.entries(criteria).every(([k, v]) => r[k] === v)
    );
  }

  /** Elimina TODOS los registros de la colección. Útil para tests. */
  clear() {
    localStorage.removeItem(this._key);
  }

  _save(data) {
    localStorage.setItem(this._key, JSON.stringify(data));
  }
}
