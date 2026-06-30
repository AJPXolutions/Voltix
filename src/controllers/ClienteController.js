import { Cliente } from '../models/Cliente.js';
import { StorageService } from '../services/StorageService.js';

const store = new StorageService('clientes');

export const ClienteController = {
  /** Retorna todos los clientes. */
  getAll() {
    return store.findAll().map((d) => new Cliente(d));
  },

  /** Busca cliente por id. */
  getById(id) {
    const data = store.findById(id);
    return data ? new Cliente(data) : null;
  },

  /**
   * Crea un cliente nuevo.
   * @param {object} formData
   * @returns {{ ok: boolean, errors?: string[], cliente?: Cliente }}
   */
  create(formData) {
    const cliente = new Cliente(formData);
    const errors = cliente.validate();
    if (errors.length) return { ok: false, errors };
    store.insert(cliente.toJSON());
    return { ok: true, cliente };
  },

  /**
   * Actualiza un cliente existente.
   * @param {string} id
   * @param {object} formData
   * @returns {{ ok: boolean, errors?: string[], cliente?: Cliente }}
   */
  update(id, formData) {
    const existing = store.findById(id);
    if (!existing) return { ok: false, errors: ['Cliente no encontrado.'] };
    const cliente = new Cliente({ ...existing, ...formData, id });
    const errors = cliente.validate();
    if (errors.length) return { ok: false, errors };
    store.update(cliente.toJSON());
    return { ok: true, cliente };
  },

  /**
   * Elimina un cliente y sus consumos/facturas asociados.
   * @param {string} id
   * @param {import('../services/StorageService.js').StorageService} consumoStore
   * @param {import('../services/StorageService.js').StorageService} facturaStore
   */
  delete(id, consumoStore, facturaStore) {
    store.delete(id);
    // Eliminar consumos del cliente
    const consumos = consumoStore.findWhere({ clienteId: id });
    consumos.forEach((c) => {
      consumoStore.delete(c.id);
      facturaStore.findWhere({ consumoId: c.id }).forEach((f) => facturaStore.delete(f.id));
    });
  },

  /** Busca clientes por nombre o email (búsqueda parcial, case-insensitive). */
  search(query) {
    const q = query.toLowerCase();
    return this.getAll().filter(
      (c) => c.nombre.toLowerCase().includes(q) || c.email.toLowerCase().includes(q)
    );
  },
};
