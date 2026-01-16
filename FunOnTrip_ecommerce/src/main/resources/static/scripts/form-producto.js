document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formProducto");
  const alertError = document.getElementById("alertError");
  const alertSuccess = document.getElementById("alertSuccess");
  const btnLimpiar = document.getElementById("btnLimpiar");

  const uploadBox = document.getElementById("uploadBox");
  const inputImagen = document.getElementById("imagen"); // type="file"
  const preview = document.getElementById("previewImagen");

  // Helpers
  const resetAlerts = () => {
    alertError.classList.add("d-none");
    alertSuccess.classList.add("d-none");
    alertError.innerHTML = "";
  };

  const clearValidationStates = () => {
    Array.from(form.elements).forEach((el) => {
      if (!(el instanceof HTMLInputElement || el instanceof HTMLSelectElement || el instanceof HTMLTextAreaElement)) return;
      el.classList.remove("is-invalid", "is-valid");
      const fb = el.parentElement?.querySelector(".invalid-feedback");
      if (fb) fb.textContent = "";
    });
  };

  const setInvalid = (el, msg) => {
    el.classList.add("is-invalid");
    let fb = el.parentElement?.querySelector(".invalid-feedback");
    if (!fb) {
      fb = document.createElement("div");
      fb.className = "invalid-feedback";
      el.parentElement?.appendChild(fb);
    }
    fb.textContent = msg;
  };

  const setValid = (el) => el.classList.add("is-valid");

  // Limpiar formulario
  btnLimpiar?.addEventListener("click", () => {
    form.reset();
    resetAlerts();
    clearValidationStates();
    preview?.classList.add("d-none");
  });

  // Botón visual de imagen
  uploadBox?.addEventListener("click", () => inputImagen?.click());

  // Preview imagen
  inputImagen?.addEventListener("change", () => {
    const file = inputImagen.files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      preview.src = reader.result;
      preview.classList.remove("d-none");
    };
    reader.readAsDataURL(file);
  });

  // SUBMIT (UNO SOLO)
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    resetAlerts();
    clearValidationStates();

    const nombreEl = form.querySelector("#nombre");
    const precioEl = form.querySelector("#precio");
    const ubicacionEl = form.querySelector("#ubicacion");
    const tipoEl = form.querySelector("#tipo");
    const descripcionEl = form.querySelector("#descripcion");

    const errors = [];
    let firstInvalid = null;

    const nombre = (nombreEl.value || "").trim();
    const precioStr = (precioEl.value || "").trim();
    const precio = Number(precioStr);
    const ubicacion = (ubicacionEl.value || "").trim();
    const tipo = (tipoEl.value || "").trim();
    const descripcion = (descripcionEl.value || "").trim();
    const imagenFile = inputImagen.files?.[0];

    // Validaciones
    if (!nombre) {
      errors.push("El nombre del paquete es obligatorio.");
      setInvalid(nombreEl, "El nombre del paquete es obligatorio.");
      firstInvalid ||= nombreEl;
    } else if (nombre === "00000" || /^0+$/.test(nombre)) {
      errors.push("El nombre no puede ser solo ceros.");
      setInvalid(nombreEl, "El nombre no puede ser solo ceros.");
      firstInvalid ||= nombreEl;
    } else setValid(nombreEl);

    if (!precioStr) {
      errors.push("El precio es obligatorio.");
      setInvalid(precioEl, "El precio es obligatorio.");
      firstInvalid ||= precioEl;
    } else if (!/^\d+(\.\d{1,2})?$/.test(precioStr)) {
      errors.push("El precio solo puede contener números y hasta 2 decimales.");
      setInvalid(precioEl, "Solo números y hasta 2 decimales.");
      firstInvalid ||= precioEl;
    } else if (precio <= 0) {
      errors.push("El precio debe ser mayor a 0.");
      setInvalid(precioEl, "Debe ser mayor a 0.");
      firstInvalid ||= precioEl;
    } else setValid(precioEl);

    if (!imagenFile) {
      errors.push("Debes seleccionar una imagen.");
      setInvalid(inputImagen, "Debes seleccionar una imagen.");
      firstInvalid ||= inputImagen;
    } else if (!imagenFile.type.startsWith("image/")) {
      errors.push("El archivo debe ser una imagen válida.");
      setInvalid(inputImagen, "Debe ser una imagen válida.");
      firstInvalid ||= inputImagen;
    } else if (imagenFile.size > 2 * 1024 * 1024) {
      errors.push("La imagen no debe pesar más de 2 MB.");
      setInvalid(inputImagen, "Máximo 2 MB.");
      firstInvalid ||= inputImagen;
    } else setValid(inputImagen);

    if (!descripcion) {
      errors.push("La descripción es obligatoria.");
      setInvalid(descripcionEl, "La descripción es obligatoria.");
      firstInvalid ||= descripcionEl;
    } else if (/^0+$/.test(descripcion)) {
      errors.push("La descripción no puede ser solo ceros.");
      setInvalid(descripcionEl, "No puede ser solo ceros.");
      firstInvalid ||= descripcionEl;
    } else setValid(descripcionEl);

    if (!ubicacion) {
      errors.push("Selecciona una ubicación.");
      setInvalid(ubicacionEl, "Selecciona una ubicación.");
      firstInvalid ||= ubicacionEl;
    } else setValid(ubicacionEl);

    if (!tipo) {
      errors.push("Selecciona un tipo de experiencia.");
      setInvalid(tipoEl, "Selecciona un tipo de experiencia.");
      firstInvalid ||= tipoEl;
    } else setValid(tipoEl);

    if (errors.length > 0) {
      const unique = [...new Set(errors)];
      alertError.innerHTML =
        "<strong>Revisa los siguientes errores:</strong><ul>" +
        unique.map((m) => `<li>${m}</li>`).join("") +
        "</ul>";
      alertError.classList.remove("d-none");

      if (firstInvalid) {
        firstInvalid.scrollIntoView({ behavior: "smooth", block: "center" });
        firstInvalid.focus({ preventScroll: true });
      }
      return;
    }

    // Guardar producto con imagen en base64
    const reader = new FileReader();
    reader.onload = () => {
      const nuevoProducto = {
        id: Date.now(),
        nombre,
        precio,
        descripcion,
        imagen: reader.result,
        ubicacion,
        tipo,
      };

      // Asegura array global
      window.productos = window.productos || JSON.parse(localStorage.getItem("productos")) || [];
      window.productos.push(nuevoProducto);
      localStorage.setItem("productos", JSON.stringify(window.productos));

      if (typeof renderizarProductos === "function") renderizarProductos();

      alertSuccess.classList.remove("d-none");
      form.reset();
      preview.classList.add("d-none");
      clearValidationStates();
    };

    reader.readAsDataURL(imagenFile);
  });
});
