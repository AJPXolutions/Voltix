import { ConsumoController } from '../controllers/ConsumoController.js';
import { ClienteController } from '../controllers/ClienteController.js';
import { FacturaController } from '../controllers/FacturaController.js';
import { showModal, closeModal, showToast } from '../app.js';

function escapeHtml(str = '') {
  return str.replace(/[&<>"']/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}

function nombreCliente(clienteId) {
  const c = ClienteController.getById(clienteId);
  return c ? c.nombre : '—';
}

export function renderConsumos(container) {
  const consumos = ConsumoController.getAll().sort((a, b) =>
    b.periodo.localeCompare(a.periodo)
  );

  container.innerHTML = `
    <div class="actions-bar">
      <span style="color:var(--text-muted);font-size:.9rem">${consumos.length} registro(s)</span>
      <button class="btn btn-primary" id="btn-nuevo-consumo">＋ Registrar consumo</button>
    </div>
    <div class="card">
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>Cliente</th>
              <th>Período</th>
              <th>kWh</th>
              <th>Observación</th>
              <th>Fecha registro</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody id="tbody-consumos">
            ${renderRows(consumos)}
          </tbody>
        </table>
      </div>
    </div>
  `;

  document.getElementById('btn-nuevo-consumo').addEventListener('click', () => {
    openForm(null, container);
  });

  bindRowActions(container);
}

function renderRows(consumos) {
  if (consumos.length === 0) {
    return `<tr><td colspan="6" class="empty-state">
      <span class="empty-icon">⚡</span><br>Sin consumos registrados.
    </td></tr>`;
  }
  return consumos
    .map(
      (c) => `
    <tr data-id="${c.id}">
      <td>${escapeHtml(nombreCliente(c.clienteId))}</td>
      <td>${escapeHtml(c.periodo)}</td>
      <td>${c.kwh.toLocaleString()} kWh</td>
      <td>${escapeHtml(c.observacion) || '—'}</td>
      <td>${new Date(c.creadoEn).toLocaleDateString()}</td>
      <td>
        <button class="btn btn-secondary btn-sm btn-edit" data-id="${c.id}">✏️</button>
        <button class="btn btn-primary btn-sm btn-facturar" data-id="${c.id}" title="Generar factura">🧾</button>
        <button class="btn btn-danger btn-sm btn-delete" data-id="${c.id}">🗑</button>
      </td>
    </tr>`
    )
    .join('');
}

function bindRowActions(container) {
  container.querySelectorAll('.btn-edit').forEach((btn) => {
    btn.addEventListener('click', () => openForm(btn.dataset.id, container));
  });

  container.querySelectorAll('.btn-facturar').forEach((btn) => {
    btn.addEventListener('click', () => {
      const consumo = ConsumoController.getById(btn.dataset.id);
      if (!consumo) return;
      const result = FacturaController.generarDesdeConsumo(consumo);
      if (!result.ok) {
        showToast(result.errors.join(' '), 'error');
        return;
      }
      showToast(`Factura generada: $${result.factura.total}`, 'success');
    });
  });

  container.querySelectorAll('.btn-delete').forEach((btn) => {
    btn.addEventListener('click', () => {
      if (!confirm('¿Eliminar este consumo?')) return;
      ConsumoController.delete(btn.dataset.id);
      showToast('Consumo eliminado.', 'info');
      renderConsumos(container);
    });
  });
}

function openForm(id, container) {
  const consumo = id ? ConsumoController.getById(id) : null;
  const clientes = ClienteController.getAll();

  if (clientes.length === 0) {
    showToast('Primero crea al menos un cliente.', 'error');
    return;
  }

  showModal(`
    <h3 class="form-title">${consumo ? '✏️ Editar consumo' : '➕ Registrar consumo'}</h3>
    <form id="form-consumo" novalidate>
      <div class="form-group">
        <label for="f-cliente">Cliente *</label>
        <select id="f-cliente" class="form-control">
          <option value="">— Seleccionar —</option>
          ${clientes
            .map(
              (c) =>
                `<option value="${c.id}" ${consumo?.clienteId === c.id ? 'selected' : ''}>${escapeHtml(c.nombre)}</option>`
            )
            .join('')}
        </select>
      </div>
      <div class="form-group">
        <label for="f-periodo">Período (YYYY-MM) *</label>
        <input id="f-periodo" class="form-control" placeholder="2024-01" value="${escapeHtml(consumo?.periodo || '')}" />
      </div>
      <div class="form-group">
        <label for="f-kwh">Consumo (kWh) *</label>
        <input id="f-kwh" type="number" min="0.01" step="0.01" class="form-control" value="${consumo?.kwh || ''}" />
      </div>
      <div class="form-group">
        <label for="f-obs">Observación</label>
        <input id="f-obs" class="form-control" value="${escapeHtml(consumo?.observacion || '')}" />
      </div>
      <div class="form-actions">
        <button type="button" class="btn btn-secondary" id="btn-cancel-form">Cancelar</button>
        <button type="submit" class="btn btn-primary">${consumo ? 'Guardar' : 'Registrar'}</button>
      </div>
    </form>
  `);

  document.getElementById('btn-cancel-form').addEventListener('click', closeModal);

  document.getElementById('form-consumo').addEventListener('submit', (e) => {
    e.preventDefault();
    const data = {
      clienteId: document.getElementById('f-cliente').value,
      periodo: document.getElementById('f-periodo').value.trim(),
      kwh: parseFloat(document.getElementById('f-kwh').value),
      observacion: document.getElementById('f-obs').value.trim(),
    };

    const result = consumo
      ? ConsumoController.update(consumo.id, data)
      : ConsumoController.create(data);

    if (!result.ok) {
      showToast(result.errors.join(' '), 'error');
      return;
    }

    showToast(consumo ? 'Consumo actualizado.' : 'Consumo registrado.', 'success');
    closeModal();
    renderConsumos(container);
  });
}
