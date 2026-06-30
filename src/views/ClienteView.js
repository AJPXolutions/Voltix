import { ClienteController } from '../controllers/ClienteController.js';
import { showModal, closeModal, showToast } from '../app.js';

function escapeHtml(str = '') {
  return str.replace(/[&<>"']/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}

export function renderClientes(container) {
  const clientes = ClienteController.getAll();

  container.innerHTML = `
    <div class="actions-bar">
      <input class="search-input" id="search-cliente" placeholder="🔍 Buscar cliente..." autocomplete="off" />
      <button class="btn btn-primary" id="btn-nuevo-cliente">＋ Nuevo cliente</button>
    </div>
    <div class="card">
      <div class="table-wrapper">
        <table id="tabla-clientes">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Email</th>
              <th>Teléfono</th>
              <th>Dirección</th>
              <th>Registrado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody id="tbody-clientes">
            ${renderRows(clientes)}
          </tbody>
        </table>
      </div>
    </div>
  `;

  // Search
  document.getElementById('search-cliente').addEventListener('input', (e) => {
    const result = e.target.value.trim()
      ? ClienteController.search(e.target.value)
      : ClienteController.getAll();
    document.getElementById('tbody-clientes').innerHTML = renderRows(result);
    bindRowActions(container);
  });

  document.getElementById('btn-nuevo-cliente').addEventListener('click', () => {
    openForm(null, container);
  });

  bindRowActions(container);
}

function renderRows(clientes) {
  if (clientes.length === 0) {
    return `<tr><td colspan="6" class="empty-state">
      <span class="empty-icon">👥</span><br>No hay clientes registrados.
    </td></tr>`;
  }
  return clientes
    .map(
      (c) => `
    <tr data-id="${c.id}">
      <td>${escapeHtml(c.nombre)}</td>
      <td>${escapeHtml(c.email)}</td>
      <td>${escapeHtml(c.telefono)}</td>
      <td>${escapeHtml(c.direccion)}</td>
      <td>${new Date(c.creadoEn).toLocaleDateString()}</td>
      <td>
        <button class="btn btn-secondary btn-sm btn-edit" data-id="${c.id}">✏️ Editar</button>
        <button class="btn btn-danger btn-sm btn-delete" data-id="${c.id}">🗑 Eliminar</button>
      </td>
    </tr>`
    )
    .join('');
}

function bindRowActions(container) {
  container.querySelectorAll('.btn-edit').forEach((btn) => {
    btn.addEventListener('click', () => openForm(btn.dataset.id, container));
  });
  container.querySelectorAll('.btn-delete').forEach((btn) => {
    btn.addEventListener('click', () => {
      if (!confirm('¿Eliminar este cliente y todos sus datos asociados?')) return;
      ClienteController.delete(btn.dataset.id);
      showToast('Cliente eliminado.', 'info');
      renderClientes(container);
    });
  });
}

function openForm(id, container) {
  const cliente = id ? ClienteController.getById(id) : null;

  showModal(`
    <h3 class="form-title">${cliente ? '✏️ Editar cliente' : '➕ Nuevo cliente'}</h3>
    <form id="form-cliente" novalidate>
      <div class="form-group">
        <label for="f-nombre">Nombre *</label>
        <input id="f-nombre" class="form-control" value="${escapeHtml(cliente?.nombre || '')}" required />
      </div>
      <div class="form-group">
        <label for="f-email">Email *</label>
        <input id="f-email" type="email" class="form-control" value="${escapeHtml(cliente?.email || '')}" required />
      </div>
      <div class="form-group">
        <label for="f-telefono">Teléfono</label>
        <input id="f-telefono" class="form-control" value="${escapeHtml(cliente?.telefono || '')}" />
      </div>
      <div class="form-group">
        <label for="f-direccion">Dirección</label>
        <input id="f-direccion" class="form-control" value="${escapeHtml(cliente?.direccion || '')}" />
      </div>
      <div class="form-actions">
        <button type="button" class="btn btn-secondary" id="btn-cancel-form">Cancelar</button>
        <button type="submit" class="btn btn-primary">${cliente ? 'Guardar cambios' : 'Crear cliente'}</button>
      </div>
    </form>
  `);

  document.getElementById('btn-cancel-form').addEventListener('click', closeModal);

  document.getElementById('form-cliente').addEventListener('submit', (e) => {
    e.preventDefault();
    const data = {
      nombre: document.getElementById('f-nombre').value.trim(),
      email: document.getElementById('f-email').value.trim(),
      telefono: document.getElementById('f-telefono').value.trim(),
      direccion: document.getElementById('f-direccion').value.trim(),
    };

    const result = cliente
      ? ClienteController.update(cliente.id, data)
      : ClienteController.create(data);

    if (!result.ok) {
      showToast(result.errors.join(' '), 'error');
      return;
    }

    showToast(cliente ? 'Cliente actualizado.' : 'Cliente creado.', 'success');
    closeModal();
    renderClientes(container);
  });
}
