// ============================================================
// PARTE 1: GUARDAR USUARIO DE PRUEBA EN LOCALSTORAGE
// ============================================================

/**
 * TAREA 1: Implementación del almacenamiento del usuario de prueba
 * 
 * Objetivo: Guardar un usuario de prueba en localStorage para testing
 * Estructura: { email, password }
 * Clave usada: "usuarioPrueba"
 */
/*
function inicializarUsuarioDePrueba() {
  const KEY = "usuarioPrueba";
  
  // Verificar si ya existe
  const existente = localStorage.getItem(KEY);
  
  if (!existente) {
    // Crear usuario de prueba según el ejemplo proporcionado
    const usuarioPrueba = {
      email: "usuario@ejemplo.com",
      password: "123456"
    };
    
    // Guardar en localStorage (convertir a string JSON)
    localStorage.setItem(KEY, JSON.stringify(usuarioPrueba));
    
    console.log(" Usuario de prueba creado");
    console.log(" Email:", usuarioPrueba.email);
    console.log(" Password:", usuarioPrueba.password);
    console.log(" Clave en LS:", KEY);
  } else {
    console.log("Usuario de prueba ya existe");
    console.log("Datos:", JSON.parse(existente));
  }
}

// Ejecutar automáticamente al cargar
inicializarUsuarioDePrueba();


// ============================================================
// PARTE 2: FUNCIÓN DE RECUPERACIÓN Y COMPARACIÓN
// ============================================================

/**
 * TAREA 2: Ajustes finales a la función de recuperación
 * 
 * Esta función recupera el usuario desde localStorage y 
 * compara con las credenciales ingresadas
 * 
 * @param {string} email - Email ingresado por el usuario
 * @param {string} password - Contraseña ingresada
 * @returns {boolean} - true si las credenciales coinciden, false si no
 */
/*
function validarLogin(email, password) {
  console.log("Iniciando validación de credenciales");
  console.log("Email recibido:", email);
  console.log("Password recibido:", password);
  
  // PASO 1: Recuperar usuario de prueba desde localStorage
  const KEY = "usuarioPrueba";
  const usuarioGuardado = localStorage.getItem(KEY);
  
  // Validar que existe el usuario en localStorage
  if (!usuarioGuardado) {
    console.error(" No hay usuario de prueba en localStorage");
    return false;
  }
  
  // PASO 2: Parsear el JSON a objeto
  let data;
  try {
    data = JSON.parse(usuarioGuardado);
    console.log("Usuario recuperado:", data);
  } catch (error) {
    console.error(" Error al parsear JSON:", error);
    return false;
  }
  
  // PASO 3: Comparar credenciales
  const emailCoincide = data.email === email;
  const passwordCoincide = data.password === password;
  
  console.log("Resultados de comparación:");
  console.log(" Email coincide:", emailCoincide);
  console.log(" Password coincide:", passwordCoincide);
  
  // PASO 4: Retornar true solo si AMBAS credenciales son correctas
  const resultado = emailCoincide && passwordCoincide;
  
  if (resultado) {
    console.log("Credenciales VÁLIDAS");
  } else {
    console.log(" Credenciales INVÁLIDAS");
  }
  
  return resultado;
}


// ============================================================
// PARTE 3: PRUEBAS DE CREDENCIALES
// ============================================================

/**
 * TAREA 3: Pruebas de credenciales correctas vs incorrectas
 * 
 * Esta función ejecuta todas las pruebas necesarias para
 * verificar el comportamiento que se espera del programa
 */
/*
function ejecutarPruebas() {
  console.log("========================================");
  console.log("INICIANDO PRUEBAS");
  console.log(" ========================================");
  console.log("");
  
  let pruebasPasadas = 0;
  let pruebasFalladas = 0;
  
  // ========================================
  // PRUEBA 1: Credenciales CORRECTAS
  // ========================================
  console.log("PRUEBA 1: Credenciales CORRECTAS");
  console.log("   Email: usuario@ejemplo.com");
  console.log("   Password: 123456");
  
  const test1 = validarLogin("usuario@ejemplo.com", "123456");
  
  if (test1 === true) {
    console.log("  RESULTADO: PASS - Retornó true como se esperaba");
    pruebasPasadas++;
  } else {
    console.log("  RESULTADO: FAIL - Debería retornar true");
    pruebasFalladas++;
  }
  console.log("");
  
  // ========================================
  // PRUEBA 2: Email INCORRECTO
  // ========================================
  console.log("PRUEBA 2: Email INCORRECTO");
  console.log("   Email: incorrecto@ejemplo.com");
  console.log("   Password: 123456");
  
  const test2 = validarLogin("incorrecto@ejemplo.com", "123456");
  
  if (test2 === false) {
    console.log("RESULTADO: PASS - Retornó false como se esperaba");
    pruebasPasadas++;
  } else {
    console.log("RESULTADO: FAIL - Debería retornar false");
    pruebasFalladas++;
  }
  console.log("");
  
  // ========================================
  // PRUEBA 3: Password INCORRECTO
  // ========================================
  console.log("PRUEBA 3: Password INCORRECTO");
  console.log("   Email: usuario@ejemplo.com");
  console.log("   Password: wrongpassword");
  
  const test3 = validarLogin("usuario@ejemplo.com", "wrongpassword");
  
  if (test3 === false) {
    console.log(" RESULTADO: PASS - Retornó false como se esperaba");
    pruebasPasadas++;
  } else {
    console.log("RESULTADO: FAIL - Debería retornar false");
    pruebasFalladas++;
  }
  console.log("");
  
  // ========================================
  // PRUEBA 4: AMBOS INCORRECTOS
  // ========================================
  console.log("PRUEBA 4: AMBOS INCORRECTOS");
  console.log("   Email: wrong@email.com");
  console.log("   Password: wrongpass");
  
  const test4 = validarLogin("wrong@email.com", "wrongpass");
  
  if (test4 === false) {
    console.log("RESULTADO: PASS - Retornó false como se esperaba");
    pruebasPasadas++;
  } else {
    console.log(" RESULTADO: FAIL - Debería retornar false");
    pruebasFalladas++;
  }
  console.log("");
  
  // ========================================
  // PRUEBA 5: CAMPOS VACÍOS
  // ========================================
  console.log("PRUEBA 5: CAMPOS VACÍOS");
  console.log("   Email: (vacío)");
  console.log("   Password: (vacío)");
  
  const test5 = validarLogin("", "");
  
  if (test5 === false) {
    console.log("RESULTADO: PASS - Retornó false como se esperaba");
    pruebasPasadas++;
  } else {
    console.log(" RESULTADO: FAIL - Debería retornar false");
    pruebasFalladas++;
  }
  console.log("");
  
  // ========================================
  // PRUEBA 6: Case Sensitivity (Mayúsculas)
  // ========================================
  console.log("PRUEBA 6: EMAIL CON MAYÚSCULAS");
  console.log("   Email: USUARIO@EJEMPLO.COM");
  console.log("   Password: 123456");
  
  const test6 = validarLogin("USUARIO@EJEMPLO.COM", "123456");
  
  if (test6 === false) {
    console.log(" RESULTADO: PASS - Case sensitive funciona correctamente");
    pruebasPasadas++;
  } else {
    console.log(" RESULTADO: PASS - Email NO es case sensitive (depende del diseño)");
    pruebasPasadas++;
  }
  console.log("");
  
  // ========================================
  // RESUMEN FINAL
  // ========================================
  console.log("========================================");
  console.log("RESUMEN DE PRUEBAS");
  console.log("========================================");
  console.log(`Pruebas PASADAS: ${pruebasPasadas}/6`);
  console.log(`Pruebas FALLADAS: ${pruebasFalladas}/6`);
  console.log(`Porcentaje de éxito: ${((pruebasPasadas/6)*100).toFixed(1)}%`);
  console.log("");
  
  if (pruebasFalladas === 0) {
    console.log("¡TODAS LAS PRUEBAS PASARON! Sistema funcionando correctamente.");
  } else {
    console.log("Algunas pruebas fallaron. Revisar la función validarLogin()");
  }
  
  console.log("========================================");
  
  return {
    pasadas: pruebasPasadas,
    falladas: pruebasFalladas,
    total: 6
  };
}


// ============================================================
// PARTE 4: FUNCIONES AUXILIARES PARA DEBUGGING
// ============================================================

/**
 * Muestra el contenido completo de localStorage relacionado con autenticación
 */
/*
function verEstadoLocalStorage() {
  console.log("========================================");
  console.log("ESTADO DE LOCALSTORAGE");
  console.log("========================================");
  console.log("");
  
  // Usuario de prueba
  const usuarioPrueba = localStorage.getItem("usuarioPrueba");
  if (usuarioPrueba) {
    console.log(" Usuario de Prueba:");
    console.log(JSON.parse(usuarioPrueba));
  } else {
    console.log(" No existe 'usuarioPrueba' en localStorage");
  }
  console.log("");
  
  // Usuario actual (del sistema completo)
  const currentUser = localStorage.getItem("currentUser");
  if (currentUser) {
    console.log("Usuario Actual (Sesión Activa):");
    console.log(JSON.parse(currentUser));
  } else {
    console.log("ℹNo hay sesión activa");
  }
  console.log("");
  
  // Usuarios registrados
  const usuarios = localStorage.getItem("funontrip_usuarios");
  if (usuarios) {
    const lista = JSON.parse(usuarios);
    console.log(`Usuarios Registrados: ${lista.length}`);
    lista.forEach((u, i) => {
      console.log(`   ${i + 1}. ${u.nombre} - ${u.email}`);
    });
  } else {
    console.log("ℹNo hay usuarios registrados aún");
  }
  
  console.log("");
  console.log("========================================");
}

/**
 * Limpia SOLO los datos de la tarea de la prueba
 */
/*function limpiarDatosDePrueba() {
  const confirmacion = confirm(" ¿Estás seguro de eliminar el usuario de prueba?\n\nEsto NO afectará a los usuarios registrados ni a la sesión actual.");
  
  if (confirmacion) {
    localStorage.removeItem("usuarioPrueba");
    console.log("Usuario de prueba eliminado");
    console.log("ℹRecarga la página para crear uno nuevo");
  } else {
    console.log("Operación cancelada");
  }
}


// ============================================================
// PARTE 5: INTEGRACIÓN CON EL SISTEMA ACTUAL
// ============================================================

/**
 * Esta función permite usar validarLogin()
 * dentro del flujo de login.js existente
 * 
 * Agregar esto ANTES de las validaciones de admin en login.js:
 * 
 * if (validarLogin(emailValue, passwordValue)) {
 *   console.log("Usuario de prueba validado");
 *   // Continuar con el flujo de login...
 * }
 */

// Hacer la función global para que login.js pueda usarla
/*window.validarLogin = validarLogin;
window.ejecutarPruebas = ejecutarPruebas;
window.verEstadoLocalStorage = verEstadoLocalStorage;
window.limpiarDatosDePrueba = limpiarDatosDePrueba;


// ============================================================
// INSTRUCCIONES DE USO EN CONSOLA
// ============================================================

console.log("========================================");
console.log("FUNCIONES DISPONIBLES");
console.log("========================================");
console.log("");
console.log("Ejecuta estas funciones en la consola del navegador:");
console.log("");
console.log("1️ ejecutarPruebas()");
console.log("   → Ejecuta todas las 6 pruebas de validación");
console.log("");
console.log("2️verEstadoLocalStorage()");
console.log("   → Muestra todos los datos guardados");
console.log("");
console.log("3️validarLogin(email, password)");
console.log("   → Prueba manualmente con tus credenciales");
console.log("   Ejemplo: validarLogin('usuario@ejemplo.com', '123456')");
console.log("");
console.log("4️ limpiarDatosDePrueba()");
console.log("   → Elimina el usuario de prueba (reset)");
console.log("");
console.log("========================================");
*/