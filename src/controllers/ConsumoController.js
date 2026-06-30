import { Consumo } from '../models/Consumo.js';
import { StorageService } from '../services/StorageService.js';

export const consumoStore = new StorageService('consumos');

export const ConsumoController = {
  getAll() {
    return consumoStore.findAll().map((d) => new Consumo(d));
  },

  getById(id) {
    const data = consumoStore.findById(id);
    return data ? new Consumo(data) : null;
  },

  getByCliente(clienteId) {
    return consumoStore.findWhere({ clienteId }).map((d) => new Consumo(d));
  },

  create(formData) {
    const consumo = new Consumo(formData);
    const errors = consumo.validate();
    if (errors.length) return { ok: false, errors };
    consumoStore.insert(consumo.toJSON());
    return { ok: true, consumo };
  },

  update(id, formData) {
    const existing = consumoStore.findById(id);
    if (!existing) return { ok: false, errors: ['Consumo no encontrado.'] };
    const consumo = new Consumo({ ...existing, ...formData, id });
    const errors = consumo.validate();
    if (errors.length) return { ok: false, errors };
    consumoStore.update(consumo.toJSON());
    return { ok: true, consumo };
  },

  delete(id) {
    return consumoStore.delete(id);
  },

  /** Retorna el consumo total en kWh de un cliente. */
  totalKwhByCliente(clienteId) {
    return this.getByCliente(clienteId).reduce((sum, c) => sum + c.kwh, 0);
  },
};
