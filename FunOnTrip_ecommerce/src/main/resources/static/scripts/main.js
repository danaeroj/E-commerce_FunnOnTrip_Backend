// Inicializar los componentes html
// Incluir fragmentos HTML
async function includeHTML(selector, url) {
  const host = document.querySelector(selector);
  if (!host) return; // si no existe el contenedor, salimos

  const res = await fetch(url, { cache: "no-cache" });
  if (!res.ok) {
    console.error(`No se pudo cargar ${url}`);
    return;
  }

  host.innerHTML = await res.text();
  document.dispatchEvent(new Event("headerLoaded"));
}

document.addEventListener("DOMContentLoaded", async () => {
  // 1) Header y footer
  await includeHTML("#site-header", "./header.html");
  await includeHTML("#site-footer", "./footer.html");

  // =====================================================
  // LÓGICA PARA ACTUALIZAR EL CARRITO EN EL HEADER
  // =====================================================
  function actualizarCarritoHeader() {
    // buscamos todos los posibles badges que uses
    const badgeDesktop = document.getElementById("cart-badge-desktop");
    const badgeMobile = document.getElementById("cart-badge-mobile");
    const badgeClass = document.querySelector(".cart-count");

    const cart = JSON.parse(localStorage.getItem("cart") || "[]");

    // tolerante: soporta quantity y qty
    const totalItems = cart.reduce(
      (sum, item) => sum + (item.quantity || item.qty || 0),
      0
    );

    if (badgeDesktop) {
      badgeDesktop.textContent = totalItems;
      totalItems === 0
        ? badgeDesktop.classList.add("empty")
        : badgeDesktop.classList.remove("empty");
    }
    if (badgeMobile) {
      badgeMobile.textContent = totalItems;
      totalItems === 0
        ? badgeMobile.classList.add("empty")
        : badgeMobile.classList.remove("empty");
    }
    if (badgeClass) {
      badgeClass.textContent = totalItems;
    }

    console.log(`🛒 Badge header actualizado: ${totalItems}`);
  }

  // 1) Actualizar al iniciar (después de inyectar header)
  actualizarCarritoHeader();

  // 2) Escuchar actualizaciones desde home-cart.js (o cualquier otro que dispare cartUpdated)
  document.addEventListener("cartUpdated", actualizarCarritoHeader);

  // 2) Secciones de about
  await includeHTML("#site-agencia", "./sections/about/agencia.html");
  await includeHTML("#site-valores", "./sections/about/valores.html");
  await includeHTML("#site-teamCards", "./sections/about/teamCards.html");

  // 3) Secciones de productos (si aplican)
  await includeHTML("#site-home", "./sections/home.html");

  // 👇 aquí ya existen las cards en el DOM
  if (typeof initTeamCards === "function") {
    initTeamCards();
  }
});

// Si la URL tiene un hash (ej: #equipo)
window.addEventListener("load", () => {
  const hash = window.location.hash;
  if (hash) {
    const seccion = document.querySelector(hash);
    if (seccion) {
      const NAV_OFFSET = 100; // altura aprox de tu navbar en px

      setTimeout(() => {
        const top =
          seccion.getBoundingClientRect().top + window.pageYOffset - NAV_OFFSET;
        window.scrollTo({
          top: top,
          behavior: "smooth",
        });
      }, 150);
    }
  }
});

// Header dinámico
function initHeader() {
  const header = document.querySelector(".header-dinamico");
  if (header) {
    let lastScroll = 0;

    window.addEventListener("scroll", () => {
      const currentScroll =
        window.pageYOffset || document.documentElement.scrollTop;

      if (currentScroll > lastScroll && currentScroll > 80) {
        header.classList.add("header-hidden");
      } else {
        header.classList.remove("header-hidden");
      }

      lastScroll = currentScroll <= 0 ? 0 : currentScroll;
    });
  }

  const toggle = document.getElementById("navToggle");
  const menu = document.getElementById("navMenu");

  if (toggle && menu) {
    toggle.addEventListener("click", () => {
      menu.classList.toggle("active");
    });
  }
}
