// scripts/about/teamCards.js

function initTeamCards() {
  // Solo las cards del bloque de equipo
  const cards = document.querySelectorAll(".teamCards .card");

  if (!cards.length) return;

  // -------------------------------
  // FLIP DE TARJETAS (adelante y atrás)
  // -------------------------------
  cards.forEach((card) => {
    const flipBtns = card.querySelectorAll(".card__button-flip");

    flipBtns.forEach((btnFlip) => {
      btnFlip.addEventListener("click", () => {
        card.classList.toggle("flipped"); // clase que maneja el giro en tu CSS
      });
    });
  });

 
}

// La dejamos global para usarla desde main.js
window.initTeamCards = initTeamCards;

