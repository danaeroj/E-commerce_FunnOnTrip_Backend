document.addEventListener("DOMContentLoaded", () => {
    console.log("home.js cargado");

    // Tomar todos los botones que activan un collapse
    const botones = document.querySelectorAll('[data-bs-toggle="collapse"]');

    botones.forEach(boton => {
        const target = boton.getAttribute("data-bs-target");
        const collapseEl = document.querySelector(target);

        if (!collapseEl) return;

        // Cuando se abre
        collapseEl.addEventListener("shown.bs.collapse", () => {
            boton.textContent = "Ver menos";
        });

        // Cuando se cierra
        collapseEl.addEventListener("hidden.bs.collapse", () => {
            boton.textContent = "Ver más";
        });
    });
});