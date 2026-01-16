const API_URL = "http://localhost:8080/api";

document.addEventListener("DOMContentLoaded", () => {
  const loginBtn = document.getElementById("loginBtn");
  const emailInput = document.getElementById("email");
  const passwordInput = document.getElementById("password");

  if (!loginBtn || !emailInput || !passwordInput) return;

  const validateEmail = (email) =>
    /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

  loginBtn.addEventListener("click", async (e) => {
    e.preventDefault();

    const email = emailInput.value.trim().toLowerCase();
    const password = passwordInput.value.trim();

    if (!email || !validateEmail(email)) {
      Swal.fire("Error", "Correo inválido", "error");
      return;
    }

    if (!password || password.length < 6) {
      Swal.fire("Error", "Contraseña debe tener al menos 6 caracteres", "error");
      return;
    }

    try {
      const res = await fetch(`${API_URL}/usuarios/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.error || errorData.message || "Credenciales incorrectas");
      }

      const usuario = await res.json();

      // Sesión
      window.appAuth.setCurrentUser(usuario);

      // Redirección
      const redirect = localStorage.getItem("redirectAfterLogin") || "./index.html";
      localStorage.removeItem("redirectAfterLogin");

      Swal.fire({
        icon: "success",
        title: "Bienvenido",
        text: `Hola ${usuario.nombre || ""}`,
        timer: 1500,
        showConfirmButton: false,
      }).then(() => {
        window.location.href = redirect;
      });

    } catch (error) {
      Swal.fire("Error", error.message, "error");
    }
  });
});

