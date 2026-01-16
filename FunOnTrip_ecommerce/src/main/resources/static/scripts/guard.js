/*document.addEventListener("DOMContentLoaded", () => {
  const protectedPages = ["carrito.html", "perfil.html", "pago.html"];
  const adminPages = ["form-producto.html"];

  const currentPage = window.location.pathname.split("/").pop();
  const user = JSON.parse(localStorage.getItem("currentUser"));

  // SOLO ADMIN
  if (adminPages.includes(currentPage)) {
    if (!user || user.role !== "admin") {
      alert("Acceso solo para administradores");
      window.location.href = "./index.html";
    }
    return;
  }

  // LOGIN NORMAL
  if (protectedPages.includes(currentPage)) {
    if (!user) {
      alert("Debes iniciar sesión");
      window.location.href = "./login.html";
    }
  }
});*/
