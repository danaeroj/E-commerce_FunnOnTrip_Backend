// scripts/home-cart.js
console.log("Home Cart JS cargado");

document.addEventListener("DOMContentLoaded", function () {

  const CART_KEY = "cart";

  // ==========================================
  // CLICK EN BOTÓN DE AÑADIR AL CARRITO (HOME)
  // ==========================================
  document.addEventListener("click", function (e) {
    const btn = e.target.closest(".btn-add-cart-icon");
    if (!btn) return;

    const id = Number(btn.getAttribute("data-id"));
    const nombre = btn.getAttribute("data-nombre");
    const precio = Number(btn.getAttribute("data-precio"));
    const descripcion = btn.getAttribute("data-descripcion");
    const imagen = btn.getAttribute("data-imagen");

    // Validar datos esenciales (id debe ser número válido)
    if (!Number.isFinite(id) || !nombre || !Number.isFinite(precio)) {
      console.error("Datos incompletos del producto:", { id, nombre, precio });
      return;
    }

    // Obtener carrito
    const cart = JSON.parse(localStorage.getItem(CART_KEY) || "[]");

    // Revisar si ya existe (comparando id)
    const existingIndex = cart.findIndex(item => item.id === id);

    if (existingIndex !== -1) {
      cart[existingIndex].quantity = (Number(cart[existingIndex].quantity) || 0) + 1;
    } else {
      cart.push({
        id,
        name: nombre,
        price: precio,
        quantity: 1,
        description: descripcion,
        image: imagen
      });
    }

    // Guardar carrito
    localStorage.setItem(CART_KEY, JSON.stringify(cart));

    // Actualizar contador inmediatamente
    actualizarContadorCarrito();

    // Notificar a otros scripts (si alguien más escucha el evento)
    document.dispatchEvent(new CustomEvent("cartUpdated"));

    // Mostrar notificación
    mostrarNotificacion(nombre, imagen);

    // Animación del botón
    animarBoton(btn);
  });

  // ==========================================
  // CONTADOR DEL CARRITO (BADGE)
  // ==========================================
  function actualizarContadorCarrito() {
    const cart = JSON.parse(localStorage.getItem(CART_KEY) || "[]");

    // Suma de quantities
    const total = cart.reduce((acc, item) => acc + (Number(item.quantity) || 0), 0);

    // Ajusta el selector a tu badge real
    const badge = document.querySelector("#cartCount, .cart-badge, [data-cart-count]");
    if (!badge) return;

    badge.textContent = total;
    badge.classList.toggle("d-none", total === 0);
  }

  // Si el header se inserta dinámicamente, esto asegura el valor al inicio
  actualizarContadorCarrito();

  // ==========================================
  // NOTIFICACIÓN TOAST
  // ==========================================
  function mostrarNotificacion(nombreProducto, imagenProducto) {
    let toastContainer = document.getElementById("toast-container");

    if (!toastContainer) {
      toastContainer = document.createElement("div");
      toastContainer.id = "toast-container";
      toastContainer.style.cssText = `
        position: fixed;
        top: 100px;
        right: 20px;
        z-index: 9999;
        max-width: 350px;
      `;
      document.body.appendChild(toastContainer);
    }

    const toast = document.createElement("div");
    toast.className = "cart-toast";
    toast.style.cssText = `
      background: white;
      border: 2px solid #28a745;
      border-radius: 10px;
      padding: 15px;
      margin-bottom: 10px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      display: flex;
      align-items: center;
      gap: 15px;
      animation: slideInRight 0.3s ease-out;
    `;

    toast.innerHTML = `
      <img src="${imagenProducto || ""}"
           style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px;"
           alt="${nombreProducto || ""}">
      <div style="flex: 1;">
        <div style="font-weight: 600; color: #28a745; margin-bottom: 5px;">
          Agregado al carrito
        </div>
        <div style="font-size: 14px; color: #666;">
          ${nombreProducto || ""}
        </div>
      </div>
      <button type="button"
              onclick="this.parentElement.remove()"
              style="background: none; border: none; font-size: 20px; cursor: pointer; color: #999;">
        ×
      </button>
    `;

    toastContainer.appendChild(toast);

    setTimeout(() => {
      toast.style.animation = "slideOutRight 0.3s ease-in";
      setTimeout(() => toast.remove(), 300);
    }, 3000);
  }

  // ==========================================
  // ANIMACIÓN DEL BOTÓN
  // ==========================================
  function animarBoton(btn) {
    btn.style.transform = "scale(1.2)";
    btn.style.transition = "transform 0.2s";
    setTimeout(() => {
      btn.style.transform = "scale(1)";
    }, 200);
  }

  // ==========================================
  // ANIMACIONES CSS (toast)
  // ==========================================
  if (!document.getElementById("cart-animations")) {
    const style = document.createElement("style");
    style.id = "cart-animations";
    style.textContent = `
      @keyframes slideInRight {
        from { transform: translateX(100%); opacity: 0; }
        to   { transform: translateX(0); opacity: 1; }
      }
      @keyframes slideOutRight {
        from { transform: translateX(0); opacity: 1; }
        to   { transform: translateX(100%); opacity: 0; }
      }
      .btn-add-cart-icon:hover {
        transform: scale(1.05);
        transition: transform 0.2s;
      }
    `;
    document.head.appendChild(style);
  }

});
