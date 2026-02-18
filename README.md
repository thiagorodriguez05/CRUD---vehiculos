# 🚗 CRUD - Gestión de Vehículos

---

## 👨‍💻 Sobre mí

Mi nombre es Thiago Rodríguez.  
Soy estudiante de Programación y este proyecto fue desarrollado como trabajo final aplicando los conceptos de Programación Orientada a Objetos en Java.

Este sistema demuestra el uso de herencia, polimorfismo, colecciones genéricas, interfaces funcionales, Iterator, persistencia de datos y una interfaz gráfica funcional.

---

## 📌 Resumen

CRUD - Gestión de Vehículos es una aplicación desarrollada en Java que permite administrar un conjunto de vehículos.

La aplicación permite:

- ✅ Agregar vehículos
- ✅ Modificar vehículos existentes
- ✅ Eliminar vehículos
- ✅ Listar vehículos
- ✅ Buscar vehículos
- ✅ Guardar y cargar datos desde archivo (persistencia)

El sistema utiliza una interfaz gráfica desarrollada con Swing para facilitar la interacción con el usuario.

### ▶ Cómo se usa

1. Ejecutar la clase `Main`.
2. Desde la interfaz gráfica:
   - Completar los campos del vehículo.
   - Seleccionar el tipo (Auto o Moto).
   - Presionar el botón correspondiente (Agregar, Modificar, Eliminar, etc.).
3. Los datos pueden guardarse en archivo para mantener persistencia.

---


## 📐 Diagrama de Clases UML

El sistema fue diseñado aplicando herencia y polimorfismo.

- Clase abstracta `Vehiculo`
- Clases derivadas `Auto` y `Moto`
- Clase `GestionVehiculos` que administra la colección
- Uso de Iterator para recorridos
<img width="1094" height="834" alt="image" src="https://github.com/user-attachments/assets/0393764f-3657-459d-8734-2b59518d3e04" />

---

## 💾 Persistencia y Archivos Generados

El sistema genera y utiliza distintos tipos de archivos:

### 📂 Archivo .dat (Serialización)
Permite guardar y cargar la lista de vehículos.

Ejemplo:
