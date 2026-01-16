document.addEventListener("DOMContentLoaded", () => {
  console.log("perfil.js cargado");

  const user = getCurrentUser(); // auth.js

  // ====== SIN SESIÓN ======
  if (!user) {
    Swal.fire({
      icon: "warning",
      title: "Acceso restringido",
      text: "Debes iniciar sesión para ver tu perfil",
    }).then(() => {
      window.location.href = "login.html";
    });
    return;
  }

  // compat: por si algún día llega con role en vez de rol
  const userRole = user.rol || user.role;

  // ====== ELEMENTOS ======
  const emailResumen = document.getElementById("perfil-email-resumen");
  const rolSpan = document.getElementById("perfil-rol");
  const nombreTitulo = document.getElementById("perfil-nombre");
  const emailContacto = document.querySelector("#perfil-email span");
  const telefonoContacto = document.querySelector("#perfil-telefono span");
  const btnAgregar = document.getElementById("btn-agregar-destino");
  const btnLogout = document.getElementById("btn-logout");
  const tituloPrincipal = document.getElementById("titulo-principal");
  const avatar = document.querySelector(".avatar-circle");

  if (avatar && user.nombre) {
    const iniciales = user.nombre
      .trim()
      .split(" ")
      .filter((p) => p.length > 0)
      .map((p) => p[0].toUpperCase())
      .slice(0, 2)
      .join("");

    avatar.textContent = iniciales;
  }

  // ====== PINTAR DATOS ======
  if (emailResumen) emailResumen.textContent = user.email;
  if (rolSpan) rolSpan.textContent = userRole === "admin" ? "Administrador" : "Usuario";
  if (nombreTitulo) nombreTitulo.textContent = user.nombre;
  if (emailContacto) emailContacto.textContent = user.email;
  if (telefonoContacto) telefonoContacto.textContent = user.telefono;

  // ====== TÍTULO SEGÚN ROL ======
  if (userRole === "admin") {
    tituloPrincipal.innerHTML = `Perfil <span>super usuario</span>`;
  } else {
    tituloPrincipal.textContent = "Mi perfil";
  }

  // ====== BOTÓN ADMIN ======
  if (userRole === "admin" && btnAgregar) {
    btnAgregar.style.display = "inline-block";
    btnAgregar.addEventListener("click", () => {
      window.location.href = "form-producto.html";
    });
  }

  // ====== LOGOUT ======
  if (btnLogout) {
    btnLogout.addEventListener("click", logoutUser);
  }
});
