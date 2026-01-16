/* ========================================================================
   auth.js — CORE AUTH GLOBAL (Sesion + Guards + Header + Demo usuarioPrueba)
   FunOnTrip
   ======================================================================== */

console.log("auth.js cargado");

/* ------------------------------------------------------------------------
   CONFIG
------------------------------------------------------------------------- */
const ADMIN_EMAILS = [
  "danaero25@gmail.com",
  "david_carranco1111@outlook.es",
  "barrancojared577@gmail.com",
  "jorfernandofo@gmail.com",
  "anguietorres.92@gmail.com",
];

const ADMIN_MASTER_PASSWORD = "Fun2024*";

const PROTECTED_PAGES = ["carrito.html", "perfil.html", "pago.html"];
const ADMIN_PAGES = ["form-producto.html"];

/* ------------------------------------------------------------------------
   UTILS
------------------------------------------------------------------------- */
function isAdminEmail(email) {
  return email && ADMIN_EMAILS.includes(String(email).toLowerCase());
}

function obtenerIniciales(nombreCompleto) {
  const partes = String(nombreCompleto || "")
    .trim()
    .split(/\s+/)
    .filter(Boolean);

  if (partes.length === 0) return "US";
  if (partes.length === 1) return partes[0][0].toUpperCase();
  return (partes[0][0] + partes[partes.length - 1][0]).toUpperCase();
}

function safeParse(jsonStr) {
  try {
    return JSON.parse(jsonStr);
  } catch {
    return null;
  }
}

function currentPageName() {
  return window.location.pathname.split("/").pop();
}

function showError(msg, options = {}) {
  if (window.Swal?.fire) {
    Swal.fire({ icon: "error", title: options.title || "Error", text: msg });
  } else {
    alert(msg);
  }
}

function showInfo(msg, options = {}) {
  if (window.Swal?.fire) {
    Swal.fire({ icon: "info", title: options.title || "Info", text: msg });
  } else {
    alert(msg);
  }
}

/* ------------------------------------------------------------------------
   SESSION (source of truth: localStorage currentUser)
   - Normaliza SIEMPRE a "rol"
   - Compatibilidad: si viene "role", lo mapea a "rol"
------------------------------------------------------------------------- */
function setCurrentUser(usuario) {
  const user = {
    id: usuario?.id ?? null,
    nombre: usuario?.nombre ?? "",
    email: (usuario?.email ?? "").toLowerCase(),
    telefono: usuario?.telefono ?? "",
    rol: usuario?.rol
      ? usuario.rol
      : (usuario?.role ? usuario.role : (isAdminEmail(usuario?.email) ? "admin" : "user")),
  };

  localStorage.setItem("currentUser", JSON.stringify(user));
  return user;
}

function getCurrentUser() {
  const raw = localStorage.getItem("currentUser");
  if (!raw) return null;

  const u = safeParse(raw);
  if (!u) return null;

  if (!u.rol && u.role) u.rol = u.role;
  return u;
}

function logoutUser(redirectTo = "./index.html") {
  localStorage.removeItem("currentUser");
  window.location.href = redirectTo;
}

/* ------------------------------------------------------------------------
   GUARDS
------------------------------------------------------------------------- */
function requireLogin() {
  const user = getCurrentUser();
  if (!user) {
    localStorage.setItem("redirectAfterLogin", window.location.href);

    Swal.fire({
      icon: "info",
      title: "Sesión requerida",
      text: "Debes iniciar sesión para acceder al carrito",
      confirmButtonText: "Iniciar sesión",
      allowOutsideClick: false,
    }).then(() => {
      window.location.href = "./login.html";
    });
  }
}


function requireAdmin() {
  const user = getCurrentUser();
  if (!user || user.rol !== "admin") {
    showError("Esta sección es solo para administradores.", { title: "Acceso denegado" });
    setTimeout(() => {
      window.location.href = "./index.html";
    }, 1200);
  }
}

function runGuards() {
  const page = currentPageName();

  if (ADMIN_PAGES.includes(page)) {
    requireAdmin();
    return;
  }

  if (PROTECTED_PAGES.includes(page)) {
    requireLogin();
  }
}

/* ------------------------------------------------------------------------
   HEADER AUTH
   - Respeta ids: userDropdown / userDropdownMobile
   - Respeta clase: .header-avatar y .dropdown-menu
   - Switchea menu: Iniciar sesión / Mi perfil + Cerrar sesión
------------------------------------------------------------------------- */
function renderHeaderAuth() {
  const user = getCurrentUser();
  const ids = ["userDropdown", "userDropdownMobile"];

  ids.forEach((id) => {
    const dropdownBtn = document.getElementById(id);
    if (!dropdownBtn) return;

    const avatar = dropdownBtn.querySelector(".header-avatar");
    if (!avatar) return;

    const container = dropdownBtn.closest(".dropdown") || dropdownBtn.parentElement;
    const menu = container?.querySelector(".dropdown-menu");
    if (!menu) return;

    // NO LOGUEADO
    if (!user) {
      avatar.innerHTML = `<i class="fa-solid fa-user fs-4"></i>`;
      avatar.classList.remove("avatar-circle");
      menu.innerHTML = `
        <li>
          <a class="dropdown-item" href="./login.html">
            <i class="fa-solid fa-right-to-bracket me-2"></i> Iniciar sesión
          </a>
        </li>
      `;
      return;
    }

    // LOGUEADO
    avatar.textContent = obtenerIniciales(user.nombre);
    avatar.classList.add("avatar-circle");

    const logoutId = `logoutBtn-${id}`;
    menu.innerHTML = `
      <li>
        <a class="dropdown-item" href="./perfil.html">
          <i class="fa-solid fa-id-card me-2"></i> Mi perfil
        </a>
      </li>
      <li><hr class="dropdown-divider"></li>
      <li>
        <a class="dropdown-item text-danger" id="${logoutId}" href="#">
          <i class="fa-solid fa-right-from-bracket me-2"></i> Cerrar sesión
        </a>
      </li>
    `;

    const btn = document.getElementById(logoutId);
    if (btn) {
      btn.addEventListener("click", (e) => {
        e.preventDefault();
        logoutUser("./index.html");
      });
    }
  });
}

/* ------------------------------------------------------------------------
   Header injection safe hook
   - Espera a que aparezca el header (si main.js lo inserta)
   - Renderiza una vez y se desconecta (evita loop infinito)
------------------------------------------------------------------------- */
function installHeaderObserverOnce() {
  let renderedOnce = false;

  const observer = new MutationObserver(() => {
    if (renderedOnce) return;

    const exists =
      document.getElementById("userDropdown") ||
      document.getElementById("userDropdownMobile");

    if (!exists) return;

    renderedOnce = true;
    observer.disconnect();
    requestAnimationFrame(() => renderHeaderAuth());
  });

  observer.observe(document.body, { childList: true, subtree: true });
}

/* ------------------------------------------------------------------------
   SUPERADMIN VALIDATION (master password)
------------------------------------------------------------------------- */
function validarAdminAcceso(email, password) {
  return isAdminEmail(email) && password === ADMIN_MASTER_PASSWORD;
}

/* ------------------------------------------------------------------------
   DEMO: usuarioPrueba / validarLogin / pruebas / debugging
------------------------------------------------------------------------- */
function inicializarUsuarioDePrueba() {
  const KEY = "usuarioPrueba";
  const existente = localStorage.getItem(KEY);
  if (!existente) {
    localStorage.setItem(KEY, JSON.stringify({ email: "usuario@ejemplo.com", password: "123456" }));
  }
}

function validarLogin(email, password) {
  const KEY = "usuarioPrueba";
  const raw = localStorage.getItem(KEY);
  if (!raw) return false;

  const data = safeParse(raw);
  if (!data) return false;

  return data.email === email && data.password === password;
}

function ejecutarPruebas() {
  inicializarUsuarioDePrueba();

  const casos = [
    { email: "usuario@ejemplo.com", pass: "123456", expected: true, name: "Correctas" },
    { email: "incorrecto@ejemplo.com", pass: "123456", expected: false, name: "Email incorrecto" },
    { email: "usuario@ejemplo.com", pass: "wrongpassword", expected: false, name: "Password incorrecto" },
    { email: "wrong@email.com", pass: "wrongpass", expected: false, name: "Ambos incorrectos" },
    { email: "", pass: "", expected: false, name: "Vacíos" },
  ];

  let pasadas = 0;
  let falladas = 0;

  casos.forEach((c) => {
    const r = validarLogin(c.email, c.pass);
    if (r === c.expected) pasadas++;
    else falladas++;
  });

  return { pasadas, falladas, total: casos.length };
}

function verEstadoLocalStorage() {
  return {
    usuarioPrueba: localStorage.getItem("usuarioPrueba"),
    currentUser: localStorage.getItem("currentUser"),
    funontrip_usuarios: localStorage.getItem("funontrip_usuarios"),
    redirectAfterLogin: localStorage.getItem("redirectAfterLogin"),
  };
}

function limpiarDatosDePrueba() {
  const ok = confirm(
    "¿Estás seguro de eliminar el usuario de prueba?\n\nEsto NO afectará a los usuarios registrados ni a la sesión actual."
  );
  if (ok) localStorage.removeItem("usuarioPrueba");
}

/* ------------------------------------------------------------------------
   INIT
------------------------------------------------------------------------- */
function initAuthCore() {
  runGuards();
  renderHeaderAuth();
  installHeaderObserverOnce();
  inicializarUsuarioDePrueba();
}

document.addEventListener("DOMContentLoaded", initAuthCore);

/* ------------------------------------------------------------------------
   EXPORT GLOBAL API
------------------------------------------------------------------------- */
window.appAuth = {
  // session
  setCurrentUser,
  getCurrentUser,
  logoutUser,

  // guards
  requireLogin,
  requireAdmin,
  runGuards,

  // header
  renderHeaderAuth,

  // admin
  validarAdminAcceso,

  // demo
  inicializarUsuarioDePrueba,
  validarLogin,
  ejecutarPruebas,
  verEstadoLocalStorage,
  limpiarDatosDePrueba,
};
