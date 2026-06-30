import { ReporteController } from '../controllers/ReporteController.js';

function escapeHtml(str = '') {
  return str.replace(/[&<>"']/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}

export function renderReportes(container) {
  const stats = ReporteController.resumenGeneral();
  const periodos = ReporteController.consumoPorPeriodo(12);
  const top = ReporteController.topClientesPorConsumo(10);

  const maxKwh = periodos.reduce((m, p) => Math.max(m, p.kwh), 1);

  container.innerHTML = `
    <div class="stat-grid" style="margin-bottom:24px">
      <div class="stat-card">
        <div class="stat-value">${stats.totalClientes}</div>
        <div class="stat-label">👥 Clientes totales</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">${stats.totalConsumos}</div>
        <div class="stat-label">⚡ Registros de consumo</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">${stats.totalKwh.toLocaleString()}</div>
        <div class="stat-label">🔋 kWh totales</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">$${stats.totalFacturado.toLocaleString()}</div>
        <div class="stat-label">💰 Total facturado</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">$${stats.pendiente.toLocaleString()}</div>
        <div class="stat-label">⏳ Pendiente de cobro</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">${stats.facturasPagadas}</div>
        <div class="stat-label">✅ Facturas pagadas</div>
      </div>
    </div>

    <div style="display:grid;grid-template-columns:1fr 1fr;gap:20px">
      <div class="card">
        <div class="section-header"><h3>📊 Consumo mensual (kWh)</h3></div>
        ${
          periodos.length === 0
            ? '<p class="empty-state"><span class="empty-icon">📭</span><br>Sin datos de consumo aún.</p>'
            : `
          <div class="report-bars" style="height:140px">
            ${periodos.map((p) => `
              <div class="report-bar"
                   style="height:${Math.round((p.kwh / maxKwh) * 100)}%"
                   title="${p.periodo}: ${p.kwh} kWh"></div>
            `).join('')}
          </div>
          <div class="report-labels">
            ${periodos.map((p) => `<span>${p.periodo.slice(5)}</span>`).join('')}
          </div>
          <div style="margin-top:12px">
            <table>
              <thead><tr><th>Período</th><th>kWh</th></tr></thead>
              <tbody>
                ${periodos.map((p) => `
                  <tr><td>${p.periodo}</td><td>${p.kwh.toLocaleString()}</td></tr>
                `).join('')}
              </tbody>
            </table>
          </div>`
        }
      </div>

      <div class="card">
        <div class="section-header"><h3>🏆 Ranking de consumo por cliente</h3></div>
        ${
          top.length === 0
            ? '<p class="empty-state"><span class="empty-icon">📭</span><br>Sin datos aún.</p>'
            : `<table>
            <thead><tr><th>#</th><th>Cliente</th><th>kWh totales</th></tr></thead>
            <tbody>
              ${top.map((t, i) => `
                <tr>
                  <td>${i + 1}</td>
                  <td>${escapeHtml(t.cliente.nombre)}</td>
                  <td>${t.kwh.toLocaleString()}</td>
                </tr>
              `).join('')}
            </tbody>
          </table>`
        }
      </div>
    </div>
  `;
}
