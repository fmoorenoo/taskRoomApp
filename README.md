<div align="center">

# Task Room App

### Gestor de tareas y categorías con almacenamiento local en Android

**Proyecto Android · CRUD · Persistencia con Room**

![Kotlin](https://img.shields.io/badge/Kotlin-1A1B26?style=for-the-badge&logo=kotlin&logoColor=E46B79)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-1A1B26?style=for-the-badge&logo=jetpackcompose&logoColor=E46B79)
![Room](https://img.shields.io/badge/Room-1A1B26?style=for-the-badge&logo=android&logoColor=E46B79)
![Material 3](https://img.shields.io/badge/Material_3-1A1B26?style=for-the-badge&logo=materialdesign&logoColor=E46B79)

[![Estado](https://img.shields.io/badge/ESTADO-Proyecto_académico-712B39?style=flat-square)](#estado-del-proyecto)
[![Plataforma](https://img.shields.io/badge/PLATAFORMA-Android-22252C?style=flat-square)](#tecnologías)
[![Código](https://img.shields.io/badge/REPOSITORIO-Código_fuente-22252C?style=flat-square)](https://github.com/fmoorenoo/taskRoomApp)

</div>

---

## Sobre el proyecto

**Task Room App** es una aplicación Android para gestionar tareas personales y sus categorías. Está desarrollada en **Kotlin** con interfaz **Jetpack Compose** y persistencia local mediante **Room**.

El proyecto permite aplicar operaciones **CRUD** (crear, consultar, actualizar y eliminar) sobre dos entidades relacionadas: tareas y tipos de tarea. Su diseño parte de un boceto en FigJam y se materializa en una interfaz de tarjetas y formularios.

## Funcionalidades

- **Crear tareas:** nombre, descripción opcional y categoría.
- **Consultar tareas:** listado de registros almacenados localmente.
- **Editar y eliminar tareas:** acciones desde las tarjetas de cada tarea.
- **Gestionar categorías:** creación, consulta, modificación y eliminación de tipos de tarea.
- **Persistencia local:** almacenamiento en la base de datos Room del dispositivo.
- **Descripción desplegable:** presentación condicionada a la existencia de una descripción, según la documentación original.

> Las funcionalidades se han contrastado con la estructura de datos y los componentes del proyecto. No se han ejecutado pruebas funcionales en un dispositivo durante esta revisión.

## Tecnologías

| Área | Tecnologías |
| --- | --- |
| **Lenguaje** | Kotlin |
| **Plataforma** | Android · minSdk 27 · targetSdk 34 |
| **Interfaz** | Jetpack Compose · Material 3 |
| **Persistencia** | Room Database · DAOs · entidades |
| **Procesamiento de anotaciones** | KSP |
| **Asincronía** | Kotlin Coroutines |
| **Compilación** | Gradle Kotlin DSL |

## Arquitectura

```text
taskRoomApp/
├── app/src/main/
│   ├── java/org/iesharia/taskroomapp/
│   │   ├── MainActivity.kt          # Punto de entrada Android
│   │   ├── model/
│   │   │   ├── AppDatabase.kt       # Configuración de Room
│   │   │   ├── Task.kt              # Entidad de tarea
│   │   │   ├── TaskDao.kt           # Operaciones CRUD
│   │   │   ├── TaskType.kt          # Entidad de categoría
│   │   │   └── TaskTypeDAO.kt       # Operaciones sobre categorías
│   │   ├── view/
│   │   │   ├── TaskApp.kt           # Pantalla principal y formulario
│   │   │   ├── ShowTasks.kt         # Listado de tareas
│   │   │   └── ...                  # Tarjetas y vistas auxiliares
│   │   └── ui/theme/               # Diseño visual
│   └── res/                        # Recursos de Android
├── app/build.gradle.kts
└── settings.gradle.kts
```

La capa `model/` concentra entidades y acceso a datos con **Room**. La carpeta `view/` contiene los componentes de interfaz construidos con **Compose**, que consultan y actualizan los DAOs utilizando corrutinas.

## Diseño y capturas

Se conservan las dos imágenes de la documentación original: el boceto de interfaz y una captura del resultado.

| Boceto en FigJam | Aplicación |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/b9be576e-597e-4c22-b509-42d6f1cb1f62" alt="Boceto inicial en FigJam" width="100%"> | <img src="https://github.com/user-attachments/assets/bfd4dcd6-6c1f-44c1-93f5-ae8e779540a7" alt="Interfaz de Task Room App" width="100%"> |

## Instalación y ejecución

1. Clona el repositorio:

   ```bash
   git clone https://github.com/fmoorenoo/taskRoomApp.git
   cd taskRoomApp
   ```

2. Abre el proyecto con **Android Studio**.
3. Configura el SDK de Android y sincroniza Gradle.
4. Ejecuta el módulo `app` en un emulador o dispositivo con **Android 8.1 (API 27)** o superior.

La aplicación utiliza una base de datos local y no requiere configurar un servidor externo para las operaciones documentadas. No se ha verificado la compilación durante esta revisión.

## Estado del proyecto

**Proyecto académico / aplicación de práctica de persistencia local.**

La base de datos está configurada con `fallbackToDestructiveMigration()`. **Esto puede eliminar los datos existentes si se produce una migración de esquema sin una migración definida.** Es una solución provisional y debe sustituirse por migraciones controladas si se pretende mantener datos de usuarios entre versiones.

## Autor

**Fernando Montelongo Moreno** · Full Stack Developer · DAM + DAW

[![GitHub](https://img.shields.io/badge/GitHub-fmoorenoo-22252C?style=flat-square&logo=github)](https://github.com/fmoorenoo)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Perfil_profesional-712B39?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/fernando-montelongo/)

---

<div align="center">
  <sub>Task Room App · Gestión de tareas con Kotlin, Jetpack Compose y Room</sub>
</div>
