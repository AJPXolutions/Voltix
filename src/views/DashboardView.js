import { ReporteController } from '../controllers/ReporteController.js';

export function renderDashboard(container) {
  const stats = ReporteController.resumenGeneral();
  const periodos = ReporteController.consumoPorPeriodo(6);
  const top = ReporteController.topClientesPorConsumo(5);

  const maxKwh = periodos.reduce((m, p) => Math.max(m, p.kwh), 1);

  container.innerHTML = `
    <div class="stat-grid">
      <div class="stat-card">
        <div class="stat-value">${stats.totalClientes}</div>
        <div class="stat-label">👥 Clientes</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">${stats.totalKwh.toLocaleString()}</div>
        <div class="stat-label">⚡ kWh totales</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">$${stats.totalFacturado.toLocaleString()}</div>
        <div class="stat-label">🧾 Total facturado</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">$${stats.pendiente.toLocaleString()}</div>
        <div class="stat-label">⏳ Pendiente</div>
      </div>
    </div>

    <div style="display:grid;grid-template-columns:1fr 1fr;gap:20px;flex-wrap:wrap">
      <div class="card">
        <div class="section-header"><h3>📊 Consumo por período (kWh)</h3></div>
        ${
          periodos.length === 0
            ? '<p class="empty-state"><span class="empty-icon">📭</span><br>Sin datos de consumo aún.</p>'
            : `
          <div class="report-bars">
            ${periodos
              .map(
                (p) =>
                  `<div class="report-bar"
                     style="height:${Math.round((p.kwh / maxKwh) * 100)}%"
                     title="${p.periodo}: ${p.kwh} kWh"></div>`
              )
              .join('')}
          </div>
          <div class="report-labels">
            ${periodos.map((p) => `<span>${p.periodo.slice(5)}</span>`).join('')}
          </div>`
        }
      </div>

      <div class="card">
        <div class="section-header"><h3>🏆 Top clientes por consumo</h3></div>
        ${
          top.length === 0
            ? '<p class="empty-state"><span class="empty-icon">📭</span><br>Sin datos aún.</p>'
            : `<table>
            <thead><tr><th>#</th><th>Cliente</th><th>kWh</th></tr></thead>
            <tbody>
              ${top
                .map(
                  (t, i) => `
                <tr>
                  <td>${i + 1}</td>
                  <td>${escapeHtml(t.cliente.nombre)}</td>
                  <td>${t.kwh.toLocaleString()}</td>
                </tr>`
                )
                .join('')}
            </tbody>
          </table>`
        }
      </div>
    </div>
  `;
}

function escapeHtml(str = '') {
  return str.replace(/[&<>"']/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}
