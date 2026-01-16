/*/ ==========================
// CONFIG
// ==========================
const API_URL = "http://TU_IP_PUBLICA/api";

// ==========================
// TOKEN
// ==========================
function getToken() {
  return localStorage.getItem("token");
}

function setToken(token) {
  localStorage.setItem("token", token);
}

function clearSession() {
  localStorage.removeItem("token");
  localStorage.removeItem("user");
}

// ==========================
// USER
// ==========================
function setCurrentUser(user) {
  localStorage.setItem("user", JSON.stringify(user));
}

function getCurrentUser() {
  const u = localStorage.getItem("user");
  return u ? JSON.parse(u) : null;
}

// ==========================
// AUTH HELPERS
// ==========================
function isLoggedIn() {
  return !!getToken();
}

function isAdmin() {
  const user = getCurrentUser();
  return user && user.rol === "ADMIN";
}

// ==========================
// PAGE GUARD
// ==========================
document.addEventListener("DOMContentLoaded", () => {

  const protectedPages = ["carrito.html", "perfil.html", "pago.html"];
  const adminPages = ["form-producto.html"];

  const currentPage = window.location.pathname.split("/").pop();

  if (adminPages.includes(currentPage)) {
    if (!isLoggedIn() || !isAdmin()) {
      window.location.href = "index.html";
    }
    return;
  }

  if (protectedPages.includes(currentPage)) {
    if (!isLoggedIn()) {
      window.location.href = "login.html";
    }
  }
});**\