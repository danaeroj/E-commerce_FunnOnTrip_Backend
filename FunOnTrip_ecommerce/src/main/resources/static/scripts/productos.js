
// ========================================================================
// PRODUCTOS — FunOnTrip
// - Carga productos desde backend
// - Renderiza catálogo
// - Aplica filtros
// ========================================================================

console.log("JS de productos cargado correctamente");

const API_URL = "http://localhost:8080/api"; // cambia en deploy

let productos = [];

// ===============================
// 1. FETCH PRODUCTOS
// ===============================
async function cargarProductos() {
  const contenedorN = document.getElementById("productos-nacionales");
  const contenedorI = document.getElementById("productos-internacionales");
  const contenedorP = document.getElementById("productos-petfriendly");

  // Mostrar loading
  const loading = `
    <div class="col-12 text-center py-5">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Cargando...</span>
      </div>
      <p class="mt-2 text-muted">Cargando productos...</p>
    </div>
  `;
  
  if (contenedorN) contenedorN.innerHTML = loading;
  if (contenedorI) contenedorI.innerHTML = "";
  if (contenedorP) contenedorP.innerHTML = "";

  try {
    const res = await fetch(`${API_URL}/productos`);

    if (!res.ok) {
      throw new Error(`Error ${res.status}: No se pudieron cargar los productos`);
    }

    productos = await res.json();
    
    console.log("Productos cargados:", productos);
    renderizarProductos();
    
  } catch (error) {
    console.error("Error al cargar productos:", error);
    
    // Mostrar error en UI
    const errorHTML = `
      <div class="col-12 text-center py-5">
        <i class="fas fa-exclamation-triangle fa-3x text-danger mb-3"></i>
        <p class="text-danger">No fue posible cargar los productos</p>
        <button class="btn btn-primary btn-sm mt-3" onclick="cargarProductos()">
          <i class="fas fa-redo me-2"></i>Reintentar
        </button>
      </div>
    `;
    
    if (contenedorN) contenedorN.innerHTML = errorHTML;
    if (contenedorI) contenedorI.innerHTML = "";
    if (contenedorP) contenedorP.innerHTML = "";
    
    Swal.fire({
      icon: "error",
      title: "Error de conexión",
      text: "No fue posible cargar los productos. Verifica tu conexión e intenta de nuevo.",
      confirmButtonText: "Reintentar",
    }).then((result) => {
      if (result.isConfirmed) {
        cargarProductos();
      }
    });
  }
}

// ===============================
// 2. CREAR CARD
// ===============================
function crearCardProducto(producto) {
  const nombre = producto.nombre || "Sin nombre";
  const descripcion = producto.descripcion || "Sin descripción";
  const precio = producto.precio || 0;
  const tipo = producto.tipo || "otro";
  const ubicacion = producto.ubicacion || "nacional";
  
  // Manejo inteligente de imágenes
  let imagen = producto.imagen || "./assets/images/default-producto.jpg";
  if (imagen && !imagen.startsWith('http')) {
    imagen = `./assets/images/productos/${imagen}`;
  }

  return `
    <article class="col-12 col-sm-6 col-md-4 col-lg-3 producto-card"
             data-tipo="${tipo}"
             data-ubicacion="${ubicacion}"
             data-precio="${precio}">
      
      <div class="producto-img-wrapper">
        <img src="${imagen}" 
             class="producto-img" 
             alt="${nombre}"
             onerror="this.src='./assets/images/default-producto.jpg'"
             loading="lazy">
      </div>

      <div class="producto-content">
        <h3 class="producto-titulo">${nombre}</h3>
        <p class="producto-descripcion">${descripcion}</p>
        <p class="producto-precio">
          $${precio.toLocaleString("es-MX")} MXN
        </p>

        <button class="btn-agregar" data-id="${producto.id}">
          <i class="fas fa-shopping-cart"></i> Agregar al carrito
        </button>
      </div>
    </article>
  `;
}

// ===============================
// 3. RENDERIZAR PRODUCTOS
// ===============================
function renderizarProductos() {
  const contenedorN = document.getElementById("productos-nacionales");
  const contenedorI = document.getElementById("productos-internacionales");
  const contenedorP = document.getElementById("productos-petfriendly");

  if (!contenedorN || !contenedorI || !contenedorP) {
    console.warn("Contenedores de productos no encontrados");
    return;
  }

  contenedorN.innerHTML = "";
  contenedorI.innerHTML = "";
  contenedorP.innerHTML = "";

  // Si no hay productos
  if (productos.length === 0) {
    const mensajeVacio = `
      <div class="col-12 text-center py-5">
        <i class="fas fa-box-open fa-3x text-muted mb-3"></i>
        <p class="text-muted fs-5">No hay productos disponibles</p>
        <p class="text-muted small">Vuelve más tarde para ver nuestras ofertas</p>
      </div>
    `;
    contenedorN.innerHTML = mensajeVacio;
    return;
  }

  // Renderizar productos por ubicación
  const nacionales = productos.filter(p => p.ubicacion === "nacional");
  const internacionales = productos.filter(p => p.ubicacion === "internacional");
  const petfriendly = productos.filter(p => p.ubicacion === "petfriendly");

  // Nacional
  if (nacionales.length > 0) {
    contenedorN.innerHTML = nacionales.map(p => crearCardProducto(p)).join("");
  } else {
    contenedorN.innerHTML = `
      <div class="col-12 text-center py-4">
        <p class="text-muted">No hay productos nacionales disponibles</p>
      </div>
    `;
  }

  // Internacional
  if (internacionales.length > 0) {
    contenedorI.innerHTML = internacionales.map(p => crearCardProducto(p)).join("");
  } else {
    contenedorI.innerHTML = `
      <div class="col-12 text-center py-4">
        <p class="text-muted">No hay productos internacionales disponibles</p>
      </div>
    `;
  }

  // Pet Friendly
  if (petfriendly.length > 0) {
    contenedorP.innerHTML = petfriendly.map(p => crearCardProducto(p)).join("");
  } else {
    contenedorP.innerHTML = `
      <div class="col-12 text-center py-4">
        <p class="text-muted">No hay productos pet-friendly disponibles</p>
      </div>
    `;
  }

  console.log(`Productos renderizados: ${productos.length} total`);
}

// ===============================
// 4. FILTROS
// ===============================
document.addEventListener("DOMContentLoaded", () => {
  cargarProductos();

  let filtroUbicacion = "todos";
  let filtroTipo = "todos";
  let maxPrecio = null;

  function aplicarFiltros() {
    const cards = document.querySelectorAll(".producto-card");
    let visibles = 0;

    cards.forEach((card) => {
      const ubicacion = card.dataset.ubicacion;
      const tipo = card.dataset.tipo;
      const precio = parseInt(card.dataset.precio, 10);

      const coincideUbicacion =
        filtroUbicacion === "todos" || filtroUbicacion === ubicacion;

      const coincideTipo =
        filtroTipo === "todos" || filtroTipo === tipo;

      const coincidePrecio =
        maxPrecio === null ||
        (maxPrecio === 20001 && precio > 20000) ||
        (maxPrecio !== 20001 && precio <= maxPrecio);

      const mostrar = coincideUbicacion && coincideTipo && coincidePrecio;
      
      card.style.display = mostrar ? "block" : "none";
      if (mostrar) visibles++;
    });

    console.log(`Filtros aplicados: ${visibles} productos visibles`);
  }

  function activarBoton(boton, selectorGrupo) {
    document
      .querySelectorAll(selectorGrupo)
      .forEach((b) => b.classList.remove("active"));
    boton.classList.add("active");
  }

  // Filtros de ubicación
  document
    .querySelectorAll("#filtros-ubicacion .filtro-destino")
    .forEach((btn) => {
      btn.addEventListener("click", () => {
        filtroUbicacion = btn.dataset.dest;
        activarBoton(btn, "#filtros-ubicacion .filtro-destino");
        aplicarFiltros();
      });
    });

  // Filtros de experiencia
  document
    .querySelectorAll("#filtros-experiencia .filtro-experiencia")
    .forEach((btn) => {
      btn.addEventListener("click", () => {
        filtroTipo = btn.dataset.exp;
        activarBoton(btn, "#filtros-experiencia .filtro-experiencia");
        aplicarFiltros();
      });
    });

  // Filtros de precio
  document
    .querySelectorAll("#filtros-precio .filtro-precio")
    .forEach((btn) => {
      btn.addEventListener("click", () => {
        maxPrecio = btn.dataset.maxprice
          ? parseInt(btn.dataset.maxprice, 10)
          : null;
        activarBoton(btn, "#filtros-precio .filtro-precio");
        aplicarFiltros();
      });
    });

  // Toggle panel de filtros (móvil)
  const toggleBtn = document.getElementById("toggle-filtros");
  const panelFiltros = document.querySelector(".container-filtro-float");

  if (toggleBtn && panelFiltros) {
    toggleBtn.addEventListener("click", () => {
      panelFiltros.classList.toggle("show");
    });
  }
});

