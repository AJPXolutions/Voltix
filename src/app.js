/**
 * app.js — Bootstrap principal de Voltix Web
 *
 * Inicializa el router SPA, registra los módulos de vista
 * y expone helpers globales (modal, toast) para los views.
 */

import { renderDashboard } from './views/DashboardView.js';
import { renderClientes }  from './views/ClienteView.js';
import { renderConsumos }  from './views/ConsumoView.js';
import { renderFacturas }  from './views/FacturaView.js';
import { renderReportes }  from './views/ReporteView.js';

// ── Elementos del DOM ─────────────────────────────────────────────
const viewContainer = document.getElementById('view-container');
const pageTitle     = document.getElementById('page-title');
const navLinks      = document.querySelectorAll('.nav-item[data-view]');
const sidebar       = document.getElementById('sidebar');
const menuToggle    = document.getElementById('menu-toggle');
const modalOverlay  = document.getElementById('modal-overlay');
const modalBody     = document.getElementById('modal-body');
const modalClose    = document.getElementById('modal-close');
const toastContainer= document.getElementById('toast-container');

// ── Registro de vistas ─────────────────────────────────────────────
const views = {
  dashboard: { title: 'Dashboard',  render: renderDashboard },
  clientes:  { title: 'Clientes',   render: renderClientes  },
  consumos:  { title: 'Consumos',   render: renderConsumos  },
  facturas:  { title: 'Facturas',   render: renderFacturas  },
  reportes:  { title: 'Reportes',   render: renderReportes  },
};

// ── Router ─────────────────────────────────────────────────────────
function navigate(viewName) {
  const view = views[viewName] || views.dashboard;

  // Actualizar nav activo
  navLinks.forEach((link) => {
    link.classList.toggle('active', link.dataset.view === viewName);
  });

  pageTitle.textContent = view.title;
  viewContainer.innerHTML = '';
  view.render(viewContainer);

  // Cerrar sidebar en móvil
  sidebar.classList.remove('open');
}

// ── Eventos de navegación ──────────────────────────────────────────
navLinks.forEach((link) => {
  link.addEventListener('click', (e) => {
    e.preventDefault();
    navigate(link.dataset.view);
  });
});

// ── Toggle sidebar (móvil) ─────────────────────────────────────────
menuToggle.addEventListener('click', () => {
  sidebar.classList.toggle('open');
});

// Cerrar sidebar al hacer click fuera en móvil
document.addEventListener('click', (e) => {
  if (
    sidebar.classList.contains('open') &&
    !sidebar.contains(e.target) &&
    e.target !== menuToggle
  ) {
    sidebar.classList.remove('open');
  }
});

// ── Modal ──────────────────────────────────────────────────────────
export function showModal(html) {
  modalBody.innerHTML = html;
  modalOverlay.hidden = false;
  // Foco en el primer input si existe
  const firstInput = modalBody.querySelector('input, select, textarea');
  if (firstInput) firstInput.focus();
}

export function closeModal() {
  modalOverlay.hidden = true;
  modalBody.innerHTML = '';
}

modalClose.addEventListener('click', closeModal);
modalOverlay.addEventListener('click', (e) => {
  if (e.target === modalOverlay) closeModal();
});
document.addEventListener('keydown', (e) => {
  if (e.key === 'Escape' && !modalOverlay.hidden) closeModal();
});

// ── Toast ──────────────────────────────────────────────────────────
/**
 * Muestra una notificación temporal.
 * @param {string} message
 * @param {'success'|'error'|'info'} [type='success']
 * @param {number} [duration=3000]
 */
export function showToast(message, type = 'success', duration = 3000) {
  const toast = document.createElement('div');
  toast.className = `toast${type !== 'success' ? ` ${type}` : ''}`;
  toast.textContent = message;
  toastContainer.appendChild(toast);
  setTimeout(() => toast.remove(), duration);
}

// ── Inicio ─────────────────────────────────────────────────────────
navigate('dashboard');
