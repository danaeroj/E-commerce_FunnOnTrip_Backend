/*console.log("header.auth.js cargado");

function setAvatar(dropdownId, user) {
  const dropdown = document.getElementById(dropdownId);
  if (!dropdown) return;

  const avatar = dropdown.querySelector(".header-avatar");
  const menu = dropdown.nextElementSibling;

  if (!avatar || !menu) return;

  if (!user) {
    avatar.innerHTML = `<i class="fa-solid fa-user fs-4"></i>`;
    return;
  }

  const iniciales = user.nombre
    .split(" ")
    .map((p) => p[0])
    .slice(0, 2)
    .join("")
    .toUpperCase();

  avatar.textContent = iniciales;
  avatar.classList.add("avatar-circle");

  menu.innerHTML = `
    <li>
      <a class="dropdown-item" href="./perfil.html">
        <i class="fa-solid fa-id-card me-2"></i> Mi perfil
      </a>
    </li>
    <li><hr class="dropdown-divider"></li>
    <li>
      <a class="dropdown-item text-danger" id="logoutBtn">
        <i class="fa-solid fa-right-from-bracket me-2"></i> Cerrar sesión
      </a>
    </li>
  `;

  document.getElementById("logoutBtn")?.addEventListener("click", () => {
    localStorage.removeItem("currentUser");
    location.reload();
  });
}

function initHeaderAuth() {
  console.log(" initHeaderAuth ejecutado");

  const user = JSON.parse(localStorage.getItem("currentUser"));

  setAvatar("userDropdown", user);
  setAvatar("userDropdownMobile", user);
}*/
