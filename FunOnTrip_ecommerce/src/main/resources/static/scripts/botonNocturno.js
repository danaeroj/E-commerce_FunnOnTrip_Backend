document.addEventListener("DOMContentLoaded", function() {

    const btn = document.getElementById("darkModeToggle");

    // 1. Revisar el modo guardado en localStorage
    const savedMode = localStorage.getItem("dark-mode");

    if (savedMode === "enabled") {
        document.body.classList.add("dark-mode");
        btn.textContent = "☀️";  // Icono de modo claro (para apagar)
    } else {
        btn.textContent = "🌙";  // Icono de modo oscuro (para encender)
    }

    // 2. Activar/desactivar modo
    btn.addEventListener("click", function () {
        
        document.body.classList.toggle("dark-mode");

        // Guardar en localStorage según el estado actual
        if (document.body.classList.contains("dark-mode")) {
            localStorage.setItem("dark-mode", "enabled");
            btn.textContent = "☀️";
        } else {
            localStorage.setItem("dark-mode", "disabled");
            btn.textContent = "🌙";
        }
    });

});

