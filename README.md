# ⚡ Voltix — Sistema de Facturación Energética (Web)

Voltix es un sistema de facturación energética diseñado como proyecto profesional para **AJPXolutions**.  
Permite gestionar clientes, consumos, facturas y reportes con una interfaz web moderna y responsiva.

> **Nota de migración:** El proyecto fue migrado de una aplicación de escritorio JavaFX/Maven a una **aplicación web** con HTML, CSS y JavaScript puro (ES Modules). La arquitectura MVC se mantiene; la persistencia usa `localStorage` en lugar de MySQL para el entorno navegador (ver [Notas de migración](#-notas-de-migración)).

---

## 🚀 Características

- Gestión de clientes (CRUD completo)
- Registro de consumos energéticos por cliente y período
- Generación y gestión de facturas (pendiente / pagada / vencida)
- Dashboard con estadísticas en tiempo real
- Reportes de consumo mensual y ranking de clientes
- Interfaz web responsiva con tema oscuro
- Arquitectura MVC limpia y modular (ES Modules)
- Persistencia local en navegador (localStorage)

---

## 🛠 Tecnologías

- HTML5 / CSS3 / JavaScript (ES Modules, sin framework)
- Node.js (solo para servidor de desarrollo y scripts de build)
- `localStorage` como capa de persistencia (reemplaza MySQL en el navegador)
- Git / GitHub

---

## 📦 Cómo ejecutar en desarrollo web

### Requisitos previos

- [Node.js](https://nodejs.org/) 16+ (para el servidor de desarrollo)
- Navegador moderno con soporte ES Modules (Chrome, Firefox, Edge, Safari recientes)

### Pasos

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/AJPXolutions/Voltix.git
   cd Voltix
   ```

2. Instalar dependencias de desarrollo:
   ```bash
   npm install
   ```

3. Iniciar el servidor de desarrollo:
   ```bash
   npm run dev
   ```
   Abrir en el navegador: **http://localhost:3000**

---

## 🏗 Build de producción web

```bash
npm run build
```

Los archivos listos para despliegue quedan en la carpeta `dist/`.  
Para probarlos localmente:

```bash
npx serve dist -p 3000
```

### Despliegue

La carpeta `dist/` es un sitio estático que puede publicarse en:

| Plataforma | Comando / Configuración |
|------------|------------------------|
| **Vercel** | Conectar repo → directorio raíz, sin build command |
| **Netlify** | Drag & drop de `dist/`, o conectar repo con `npm run build` → publish dir `dist` |
| **GitHub Pages** | Publicar rama `gh-pages` con contenido de `dist/` |
| **Cualquier CDN/hosting estático** | Subir contenido de `dist/` |

---

## 📁 Estructura del proyecto

```
Voltix/
├── index.html              # Punto de entrada (app shell)
├── package.json            # Scripts npm (dev / build)
├── .gitignore
├── css/
│   └── style.css           # Estilos globales
├── src/
│   ├── app.js              # Bootstrap, router SPA, helpers (modal, toast)
│   ├── models/
│   │   ├── Cliente.js      # Modelo + validación
│   │   ├── Consumo.js
│   │   └── Factura.js
│   ├── controllers/
│   │   ├── ClienteController.js
│   │   ├── ConsumoController.js
│   │   ├── FacturaController.js
│   │   └── ReporteController.js
│   ├── views/
│   │   ├── DashboardView.js
│   │   ├── ClienteView.js
│   │   ├── ConsumoView.js
│   │   ├── FacturaView.js
│   │   └── ReporteView.js
│   └── services/
│       └── StorageService.js  # Capa CRUD sobre localStorage
└── scripts/
    └── build.js            # Script de build estático
```

---

## 🔄 Notas de migración

### Desktop → Web (cambios principales)

| Aspecto | Desktop (anterior) | Web (actual) |
|---------|-------------------|--------------|
| Lenguaje | Java 17 + JavaFX 21 | HTML/CSS/JS (ES Modules) |
| UI | JavaFX (FXML + CSS) | HTML + CSS personalizado |
| Persistencia | MySQL (JDBC) | `localStorage` del navegador |
| Ejecución | JVM + Maven | Navegador (sin instalación) |
| Build | `mvn package` | `npm run build` |
| Dev server | IDE / `mvn javafx:run` | `npm run dev` |

### Limitaciones conocidas

- **Persistencia**: Los datos se guardan en `localStorage` del navegador (máx. ~5 MB típico). No hay sincronización entre dispositivos ni usuarios. Para producción con múltiples usuarios, conectar un backend REST con base de datos.
- **Sin autenticación**: La versión web no incluye login/roles. Añadir una capa de autenticación antes de despliegue en entorno compartido.
- **Exportación**: No hay exportación a PDF/Excel en esta versión (roadmap futuro).

### Roadmap sugerido

- [ ] Backend REST (Node.js/Express o Spring Boot) con MySQL/PostgreSQL
- [ ] Autenticación de usuarios
- [ ] Exportación de facturas a PDF
- [ ] Bundler (Vite) para minificación y tree-shaking
- [ ] Tests unitarios (Vitest)

---

## 👤 Autor

**AJPXolutions**  
[GitHub](https://github.com/AJPXolutions)
