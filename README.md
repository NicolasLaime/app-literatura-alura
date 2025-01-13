# Proyecto: Literatura JPA

Este proyecto es una aplicación de consola creada con **Spring JPA** que permite gestionar información relacionada con libros y autores. La aplicación está diseñada para facilitar operaciones como buscar libros por título, listar libros, autores, y realizar consultas específicas.

---

## 🚀 Características

El menú interactivo incluye las siguientes opciones:

1. **Buscar libro por título**  
   Permite buscar un libro ingresando su título.

2. **Listar libros registrados**  
   Muestra todos los libros disponibles en la base de datos.

3. **Listar autores registrados**  
   Muestra todos los autores registrados.

4. **Listar autores vivos en un año**  
   Filtra los autores que estaban vivos en un año específico.

5. **Listar libros por idioma**  
   Permite listar los libros según su idioma.

0. **Salir**  
   Finaliza la ejecución del programa.

---

## 🛠️ Requisitos previos

Para ejecutar este proyecto necesitas tener instalado lo siguiente:

- **Java 17 o superior**
- **Maven 3.8.1 o superior**
- **MySQL u otro motor de base de datos compatible**
- **IDE** como IntelliJ IDEA o Eclipse (opcional)

---

## 📂 Estructura del proyecto

- **src/main/java**: Contiene el código fuente principal.
- **src/main/resources/application.properties**: Configuración de la base de datos y la aplicación.
- **pom.xml**: Archivo de configuración de Maven para gestionar dependencias.

---

## 📋 Configuración de la base de datos

1. Asegúrate de que tu base de datos está activa.
2. Configura las credenciales en el archivo `application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/literatura
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   spring.jpa.hibernate.ddl-auto=update
