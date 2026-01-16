// scripts/carrito.js

document.addEventListener('DOMContentLoaded', function () {
  console.log('JS de carrito cargado');

  const cartPanel = document.getElementById('cart-panel');   // panel flotante (productos.html)
  const cartIcon = document.getElementById('cart-icon');    // botón flotante
  const cartItems = document.getElementById('cart-items');   // tbody (flotante y carrito.html)
  const cartTotalNoImpEl = document.getElementById('cart-total-no-impuestos'); // total sin impuestos (flotante)
  const cartCountEl = document.querySelector('.cart-count');   // burbuja (solo flotante)
  const checkoutBtn = document.getElementById('checkout-btn'); // <a href="./pago.html">
  const goToCartBtn = document.getElementById('go-to-cart');   // <a href="./carrito.html">

  // Estado del carrito compartido
  let cart = JSON.parse(localStorage.getItem('cart') || '[]');

  function saveCart() {
    localStorage.setItem('cart', JSON.stringify(cart));
  }

  // ==========================
  //   RENDER / ACTUALIZAR TABLA
  // ==========================

  function formatear(valor) {
  return valor.toLocaleString("es-MX", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
}
  function updateCart() {
    if (cartItems) cartItems.innerHTML = '';

    let total = 0;
    let totalCount = 0;



    cart.forEach((product, index) => {
      const subtotal = product.price * product.quantity;
      total += subtotal;
      totalCount += product.quantity;

      if (cartItems) {
        const tr = document.createElement('tr');
        tr.innerHTML = `
  <td data-label="Paquete">${product.name}</td>
  <td data-label="Precio">$${formatear(product.price)} MXN</td>

  <td data-label="Cant.">
    <input 
      type="number" 
      value="${product.quantity}" 
      min="1" 
      class="quantity" 
      data-index="${index}">
  </td>
  <td data-label="Subtotal">$${formatear(subtotal)} MXN</td>
  <td data-label="">
    <button class="btn btn-danger remove-from-cart" data-index="${index}">
      Eliminar
    </button>
  </td>
`;

        cartItems.appendChild(tr);
      }
    });

    //  Total antes de impuestos en el flotante
    if (cartTotalNoImpEl) {
      const textoTotal = cart.length > 0 ? formatear(total) : '0.00';
   cartTotalNoImpEl.textContent = textoTotal;
        }
    

    //  Guardar carrito
    localStorage.setItem('cart', JSON.stringify(cart));

    //  Avisar al resto de scripts que el carrito cambió
    document.dispatchEvent(new CustomEvent('cartUpdated'));
  }

  // ==========================
  //   TOGGLE DEL PANEL FLOTANTE
  // ==========================
  if (cartIcon && cartPanel) {
    cartIcon.addEventListener('click', () => {
      const visible = getComputedStyle(cartPanel).display !== 'none';
      cartPanel.style.display = visible ? 'none' : 'block';
    });
  }

  // ==========================
  //   AGREGAR AL CARRITO (productos.html)
  // ==========================
  document.addEventListener('click', function (e) {
    const btn = e.target.closest('.btn-agregar');
    if (!btn) return;

    const id = parseInt(btn.getAttribute('data-id'), 10);
    const lista = window.productos || [];
    const producto = lista.find(p => p.id === id);
    if (!producto) return;

    const existingIndex = cart.findIndex(item => item.id === id);

    if (existingIndex !== -1) {
      cart[existingIndex].quantity += 1;
    } else {
      cart.push({
        id: producto.id,
        name: producto.nombre,
        price: producto.precio,
        quantity: 1
      });
    }

    updateCart();

    // abre el panel al agregar (si existe)
    if (cartPanel) {
      cartPanel.style.display = 'block';
    }
  });

  // ==========================
  //   ELIMINAR / CAMBIAR CANTIDAD
  // ==========================
  document.addEventListener('click', function (e) {
    if (e.target.classList.contains('remove-from-cart')) {
      const index = parseInt(e.target.getAttribute('data-index'), 10);
      cart.splice(index, 1);
      updateCart();
    }
  });

  document.addEventListener('change', function (e) {
    if (e.target.classList.contains('quantity')) {
      const index = parseInt(e.target.getAttribute('data-index'), 10);
      const newQuantity = parseInt(e.target.value, 10);

      if (newQuantity > 0) {
        cart[index].quantity = newQuantity;
      } else {
        cart[index].quantity = 1;
        e.target.value = 1;
      }

      updateCart();
    }
  });
  // ==========================
  //   Cerrar panel flotante (FORZADO)
  // ==========================
  (function forceCloseCart() {
    function cerrarCarrito() {
      const cartPanel = document.getElementById('cart-panel');
      if (cartPanel) {
        cartPanel.style.display = 'none';
      }
    }

    // Detecta clic en la X por delegación
    document.addEventListener('click', (e) => {
      if (e.target.classList.contains('cart-close')) {
        cerrarCarrito();
      }
    });
  })();


  // ==========================
  //   FINALIZAR → ./pago.html
  // ==========================
  if (checkoutBtn) {
    checkoutBtn.addEventListener('click', (e) => {
      if (cart.length === 0) {
        e.preventDefault();
        alert('El carrito está vacío.');
      } else {
        saveCart();
        // el <a> ya navega a ./pago.html
      }
    });
  }

  // ==========================
  //   IR A ./carrito.html
  // ==========================
  if (goToCartBtn) {
    goToCartBtn.addEventListener('click', () => {
      saveCart();
      // el <a href="./carrito.html"> navega solo
    });
  }

  // Inicializar tabla en cualquier página que la tenga
  updateCart();

});


console.log("Carrito flotante JS cargado");
