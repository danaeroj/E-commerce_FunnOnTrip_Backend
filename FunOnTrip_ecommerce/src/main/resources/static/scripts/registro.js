// ========================================================================
// REGISTRO — FunOnTrip
// Responsabilidad:
// - Validar formulario
// - Registrar usuario vía backend
// ========================================================================

const API_URL = "http://localhost:8080/api"; // cambia en deploy

document.addEventListener("DOMContentLoaded", () => {
  const formRegistro = document.getElementById("form-registro");
  if (!formRegistro) return;

  // ============================
  // VALIDACIONES
  // ============================
  const esEmailValido = (email) =>
    /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

  const esTelefonoValido = (tel) => {
    const telefono = tel.replace(/\D/g, "");

    if (!/^\d{10}$/.test(telefono)) return false;
    if (/^(\d)\1{9}$/.test(telefono)) return false;

    const secuenciasInvalidas = [
      "1234567890","0123456789","9876543210","0987654321"
    ];
    if (secuenciasInvalidas.includes(telefono)) return false;

    return true;
  };

  const esPasswordValido = (password) =>
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{6,}$/.test(password);

  // ============================
  // SUBMIT
  // ============================
  formRegistro.addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = new FormData(formRegistro);

    const nombre = formData.get("nombre")?.trim();
    const email = formData.get("email")?.trim().toLowerCase();
    const telefono = formData.get("telefono")?.trim();
    const password = formData.get("password")?.trim();
    const password2 = formData.get("password2")?.trim();

    const errores = [];

    if (!nombre) errores.push("El nombre es obligatorio.");
    if (!email || !esEmailValido(email))
      errores.push("Ingresa un correo electrónico válido.");
    if (!telefono || !esTelefonoValido(telefono))
      errores.push("Ingresa un teléfono válido de 10 dígitos.");
    if (!password || !esPasswordValido(password))
      errores.push(
        "La contraseña debe tener mínimo 6 caracteres, incluir mayúscula, minúscula, número y carácter especial."
      );
    if (password !== password2)
      errores.push("Las contraseñas no coinciden.");

    if (errores.length > 0) {
      Swal.fire({
        icon: "error",
        title: "Revisa tu información",
        html: `<ul class="text-start mb-0">
          ${errores.map((e) => `<li>${e}</li>`).join("")}
        </ul>`,
      });
      return;
    }

    // Mostrar loading
    Swal.fire({
      title: "Registrando...",
      text: "Por favor espera",
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      },
    });

    try {
      // ============================
      // FETCH BACKEND
      // ============================
      const res = await fetch(`${API_URL}/usuarios/registro`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nombre,
          email,
          telefono,
          password,
        }),
      });

      if (!res.ok) {
        // Intentar obtener mensaje de error del backend
        let errorMsg = "Error al registrar usuario";
        
        try {
          const contentType = res.headers.get("content-type");
          
          if (contentType && contentType.includes("application/json")) {
            const errorData = await res.json();
            errorMsg = errorData.error || errorData.message || errorMsg;
          } else {
            errorMsg = await res.text();
          }
        } catch (parseError) {
          console.error("Error al parsear respuesta:", parseError);
        }
        
        throw new Error(errorMsg);
      }

      const usuario = await res.json();
      console.log("Usuario registrado:", usuario);

      Swal.fire({
        icon: "success",
        title: "¡Registro exitoso!",
        text: "Tu cuenta ha sido creada correctamente.",
        timer: 2000,
        showConfirmButton: false,
      }).then(() => {
        formRegistro.reset();
        window.location.href = "./login.html";
      });

    } catch (error) {
      console.error("Error en registro:", error);
      
      Swal.fire({
        icon: "error",
        title: "Error al registrar",
        text: error.message,
        confirmButtonText: "Intentar de nuevo",
      });
    }
  });
});

// ========================================================================
// TOGGLE PASSWORD
// ========================================================================
document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".toggle-password").forEach((icon) => {
    icon.addEventListener("click", () => {
      const input = document.getElementById(
        icon.getAttribute("data-target")
      );
      if (!input) return;

      const isPassword = input.type === "password";
      input.type = isPassword ? "text" : "password";
      icon.classList.toggle("fa-eye");
      icon.classList.toggle("fa-eye-slash");
    });
  });
});
