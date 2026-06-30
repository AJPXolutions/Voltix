/**
 * scripts/build.js
 *
 * Script de build minimalista para Voltix Web.
 * Copia los archivos estáticos a la carpeta dist/ lista para despliegue.
 *
 * Uso: node scripts/build.js
 *
 * Nota: Para una build con bundling avanzado (tree-shaking, minificación)
 * considera añadir Vite o esbuild en el futuro.
 */

const fs   = require('fs');
const path = require('path');

const SRC  = path.resolve(__dirname, '..');
const DIST = path.join(SRC, 'dist');

const INCLUDE = ['index.html', 'css', 'src', 'README.md'];

// ── Utilidades ─────────────────────────────────────────────────────
function copyDir(src, dest) {
  fs.mkdirSync(dest, { recursive: true });
  for (const entry of fs.readdirSync(src, { withFileTypes: true })) {
    const from = path.join(src, entry.name);
    const to   = path.join(dest, entry.name);
    if (entry.isDirectory()) {
      copyDir(from, to);
    } else {
      fs.copyFileSync(from, to);
    }
  }
}

// ── Build ──────────────────────────────────────────────────────────
console.log('⚡ Voltix — Build de producción web\n');

// Limpiar dist/
if (fs.existsSync(DIST)) {
  fs.rmSync(DIST, { recursive: true, force: true });
}
fs.mkdirSync(DIST);

for (const item of INCLUDE) {
  const from = path.join(SRC, item);
  if (!fs.existsSync(from)) continue;

  const to = path.join(DIST, item);
  const stat = fs.statSync(from);

  if (stat.isDirectory()) {
    copyDir(from, to);
    console.log(`  ✅ Directorio copiado: ${item}/`);
  } else {
    fs.copyFileSync(from, to);
    console.log(`  ✅ Archivo copiado:   ${item}`);
  }
}

console.log(`\n🎉 Build completado en: dist/`);
console.log('   Para servir: npx serve dist -p 3000\n');
