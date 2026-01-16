// ========================================================================
// AUTH PARA USUARIOS Y SUPERUSUARIOS (ADMIN)
// ========================================================================

console.log("auth.js cargado");

// ------------------------------------------------------------------------
// CONFIG ADMIN
// ------------------------------------------------------------------------
/*
const ADMIN_EMAILS = [
  "danaero25@gmail.com",
  "david_carranco1111@outlook.es",
  "barrancojared577@gmail.com",
  "jorfernandofo@gmail.com",
  "anguietorres.92@gmail.com",
];

const ADMIN_MASTER_PASSWORD = "Fun2024*";

// ------------------------------------------------------------------------
// UTILIDADES
// ------------------------------------------------------------------------

function isAdminEmail(email) {
  return email && ADMIN_EMAILS.includes(email.toLowerCase());
}

function obtenerIniciales(nombreCompleto) {
  const partes = nombreCompleto.trim().split(" ");
  return partes.length === 1
    ? partes[0][0]
    : partes[0][0] + partes[partes.length - 1][0];
}

// ------------------------------------------------------------------------
// SESIÓN
// ------------------------------------------------------------------------

function setCurrentUser(usuario) {
  const user = {
    id: usuario.id || null,
    nombre: usuario.nombre,
    email: usuario.email.toLowerCase(),
    telefono: usuario.telefono || "",
    rol: isAdminEmail(usuario.email) ? "admin" : "user",
  };

  localStorage.setItem("currentUser", JSON.stringify(user));
}

function getCurrentUser() {
  const data = localStorage.getItem("currentUser");
  if (!data) return null;

  try {
    return JSON.parse(data);
  } catch {
    return null;
  }
}

function logoutUser() {
  localStorage.removeItem("currentUser");
  window.location.href = "login.html";
}

// ------------------------------------------------------------------------
// PROTECCIÓN DE RUTAS
// ------------------------------------------------------------------------

function requireLogin() {
  const user = getCurrentUser();

  if (!user) {
    localStorage.setItem("redirectAfterLogin", window.location.href);
    window.location.href = "login.html";
  }
}

function requireAdmin() {
  const user = getCurrentUser();

  if (!user || user.rol !== "admin") {
    Swal.fire({
      icon: "error",
      title: "Acceso denegado",
      text: "Esta sección es solo para administradores.",
    });

    setTimeout(() => {
      window.location.href = "perfil.html";
    }, 1500);
  }
}

// ------------------------------------------------------------------------
// AVATAR HEADER (FUNCIONA EN TODAS LAS PÁGINAS)
// ------------------------------------------------------------------------

function renderUserAvatar() {
  const user = getCurrentUser();
  if (!user || !user.nombre) return;

  const iniciales = obtenerIniciales(user.nombre);

  document.querySelectorAll(".header-avatar").forEach((avatar) => {
    avatar.textContent = iniciales;
  });
}

// OBSERVA CUANDO EL HEADER ENTRA AL DOM
const headerObserver = new MutationObserver(() => {
  const avatars = document.querySelectorAll(".header-avatar");
  if (avatars.length > 0) {
    renderUserAvatar();
    headerObserver.disconnect(); // importante
  }
});

headerObserver.observe(document.body, {
  childList: true,
  subtree: true,
});

// Backup por si el header ya existía
document.addEventListener("DOMContentLoaded", renderUserAvatar);

// ------------------------------------------------------------------------
// VALIDACIÓN SUPERADMIN
// ------------------------------------------------------------------------

function validarAdminAcceso(email, password) {
  return isAdminEmail(email) && password === ADMIN_MASTER_PASSWORD;
}
*/