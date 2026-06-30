import { FacturaController } from '../controllers/FacturaController.js';
import { ClienteController } from '../controllers/ClienteController.js';
import { showToast } from '../app.js';

function escapeHtml(str = '') {
  return str.replace(/[&<>"']/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}

function badgeEstado(estado) {
  const map = {
    pendiente: 'badge-warning',
    pagada:    'badge-success',
    vencida:   'badge-danger',
  };
  return `<span class="badge ${map[estado] || 'badge-info'}">${estado}</span>`;
}

function nombreCliente(clienteId) {
  const c = ClienteController.getById(clienteId);
  return c ? c.nombre : '—';
}

export function renderFacturas(container) {
  const facturas = FacturaController.getAll().sort((a, b) =>
    b.emitidaEn.localeCompare(a.emitidaEn)
  );

  const totalFacturado = FacturaController.totalFacturado();
  const totalPendiente = FacturaController.totalPendiente();

  container.innerHTML = `
    <div class="stat-grid" style="margin-bottom:20px">
      <div class="stat-card">
        <div class="stat-value">$${totalFacturado.toLocaleString()}</div>
        <div class="stat-label">Total facturado</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">$${totalPendiente.toLocaleString()}</div>
        <div class="stat-label">Pendiente de cobro</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">${facturas.length}</div>
        <div class="stat-label">Facturas emitidas</div>
      </div>
    </div>

    <div class="card">
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>Cliente</th>
              <th>Período</th>
              <th>kWh</th>
              <th>Total</th>
              <th>Estado</th>
              <th>Emitida</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            ${renderRows(facturas, container)}
          </tbody>
        </table>
      </div>
    </div>
  `;

  bindRowActions(container);
}

function renderRows(facturas, container) {
  if (facturas.length === 0) {
    return `<tr><td colspan="7" class="empty-state">
      <span class="empty-icon">🧾</span><br>Sin facturas. Genera facturas desde la sección <strong>Consumos</strong>.
    </td></tr>`;
  }
  return facturas
    .map(
      (f) => `
    <tr data-id="${f.id}">
      <td>${escapeHtml(nombreCliente(f.clienteId))}</td>
      <td>${escapeHtml(f.periodo)}</td>
      <td>${f.kwh.toLocaleString()} kWh</td>
      <td><strong>$${f.total.toLocaleString()}</strong></td>
      <td>${badgeEstado(f.estado)}</td>
      <td>${new Date(f.emitidaEn).toLocaleDateString()}</td>
      <td>
        <select class="form-control btn-sm estado-select" data-id="${f.id}" style="padding:4px 8px;width:auto">
          <option value="pendiente" ${f.estado === 'pendiente' ? 'selected' : ''}>Pendiente</option>
          <option value="pagada"    ${f.estado === 'pagada'    ? 'selected' : ''}>Pagada</option>
          <option value="vencida"   ${f.estado === 'vencida'   ? 'selected' : ''}>Vencida</option>
        </select>
        <button class="btn btn-danger btn-sm btn-delete" data-id="${f.id}">🗑</button>
      </td>
    </tr>`
    )
    .join('');
}

function bindRowActions(container) {
  container.querySelectorAll('.estado-select').forEach((sel) => {
    sel.addEventListener('change', () => {
      const result = FacturaController.cambiarEstado(sel.dataset.id, sel.value);
      if (!result.ok) {
        showToast(result.errors.join(' '), 'error');
        return;
      }
      showToast(`Estado actualizado a "${sel.value}".`);
      renderFacturas(container);
    });
  });

  container.querySelectorAll('.btn-delete').forEach((btn) => {
    btn.addEventListener('click', () => {
      if (!confirm('¿Eliminar esta factura?')) return;
      FacturaController.delete(btn.dataset.id);
      showToast('Factura eliminada.', 'info');
      renderFacturas(container);
    });
  });
}
