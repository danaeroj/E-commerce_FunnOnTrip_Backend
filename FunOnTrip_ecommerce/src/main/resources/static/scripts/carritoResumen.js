// scripts/carrito_totales.js
document.addEventListener('DOMContentLoaded', () => {
  const subtotalSpan  = document.getElementById('subtotal');
  const impuestosSpan = document.getElementById('impuestos');
  const totalSpan     = document.getElementById('cart-total');

  // Si no existen estos elementos, no estamos en carrito.html
  if (!subtotalSpan || !impuestosSpan || !totalSpan) return;

  const TASA_IMPUESTOS = 0.16; // 16% (cámbialo a 0.083 si quieres 8.3%)

  function formatear(valor) {
    return valor.toLocaleString('es-MX', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
  }

  function recalcularTotales() {
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');

    let subtotal = 0;
    cart.forEach(item => {
      const price = Number(item.price) || 0;
      const qty   = Number(item.quantity) || 0;
      subtotal += price * qty;
    });

    const impuestos = subtotal * TASA_IMPUESTOS;
    const total     = subtotal + impuestos;

    subtotalSpan.textContent  = `$${formatear(subtotal)} MXN`;
    impuestosSpan.textContent = `$${formatear(impuestos)} MXN`;
    totalSpan.textContent     = `$${formatear(total)} MXN`;
  }

  // 1) Al cargar carrito.html
  recalcularTotales();

  // 2) Cada vez que carrito.js avise que algo cambió
  document.addEventListener('cartUpdated', recalcularTotales);
});
