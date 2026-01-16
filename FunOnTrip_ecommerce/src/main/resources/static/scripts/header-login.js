/*console.log("header-login.js cargado");

function setAvatar(dropdownId, user) {
  const dropdownBtn = document.getElementById(dropdownId);
  if (!dropdownBtn) return;

  const avatar = dropdownBtn.querySelector(".header-avatar");
  if (!avatar) return;

  // Usuario NO logueado
  if (!user) {
    avatar.innerHTML = `<i class="fa-solid fa-user fs-4"></i>`;
    avatar.classList.remove("avatar-circle");
    menu.innerHTML = "";
    return;
  }

  // Usuario logueado
  const iniciales = user.nombre
    .split(" ")
    .map(p => p[0])
    .slice(0, 2)
    .join("")
    .toUpperCase();

  avatar.textContent = iniciales;
  avatar.classList.add("avatar-circle");

  const logoutId = `logoutBtn-${dropdownId}`;

  menu.innerHTML = `
    <li>
      <a class="dropdown-item" href="./perfil.html">
        <i class="fa-solid fa-id-card me-2"></i> Mi perfil
      </a>
    </li>
    <li><hr class="dropdown-divider"></li>
    <li>
      <a class="dropdown-item text-danger" id="${logoutId}">
        <i class="fa-solid fa-right-from-bracket me-2"></i> Cerrar sesión
      </a>
    </li>
  `;

  document.getElementById(logoutId)?.addEventListener("click", () => {
    if (window.logout) {
      window.logout();
    } else {
      localStorage.removeItem("currentUser");
      location.reload();
    }
  });
}

/* ============================
   INIT HEADER AUTH
============================ */
/*function initHeaderAuth() {
  console.log("initHeaderAuth ejecutado");

  const user = window.getCurrentUser
    ? window.getCurrentUser()
    : JSON.parse(localStorage.getItem("currentUser"));

  setAvatar("userDropdown", user);
  setAvatar("userDropdownMobile", user);
}

document.addEventListener("DOMContentLoaded", initHeaderAuth);
*/


