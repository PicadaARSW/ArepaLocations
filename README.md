# ArepaLocations Microservice - WhereWeApp

Este microservicio se encarga de gestionar la localización de los usuarios y los lugares favoritos dentro de la aplicación **WhereWeApp**. Utiliza WebSocket con STOMP para la actualización en tiempo real de la ubicación y el estado de los lugares favoritos de los usuarios.

---

## 🧩 Tipo de Microservicio

- **Dominio**: Gestión de ubicaciones y lugares favoritos
- **Base de datos**: MongoDB
- **Framework**: Spring Boot
- **Tecnología de comunicación**: WebSocket + STOMP
- **Arquitectura**: RESTful + WebSocket
- **Transporte**: HTTP y WebSocket para la comunicación en tiempo real

---

## 📦 Endpoints

> Los endpoints se agrupan bajo el prefijo `/api/v1/favoritePlaces` para lugares favoritos y bajo `/api/v1/users/push-token` para la gestión de tokens de notificación.

### `POST /api/v1/location`
Envía la ubicación de un usuario a través de WebSocket (STOMP). Este endpoint no es accesible directamente en Swagger, ya que utiliza WebSocket.

> Los mensajes se envían a `/app/location` y se distribuyen en `/topic/location/{groupId}`.

### `POST /api/v1/addFavoritePlace`
Agrega un lugar favorito para un grupo mediante WebSocket. Similar al anterior, este endpoint no es accesible directamente en Swagger, pero el mensaje es enviado a `/app/addFavoritePlace` y transmitido a `/topic/favoritePlace/{groupId}`.

### `POST /api/v1/editFavoritePlace`
Permite editar un lugar favorito para un grupo a través de WebSocket. Los mensajes se envían a `/app/editFavoritePlace` y se distribuyen a `/topic/favoritePlaceEdited/{groupId}`.

### `POST /api/v1/deleteFavoritePlace`
Elimina un lugar favorito mediante WebSocket. Los mensajes se envían a `/app/deleteFavoritePlace` y se distribuyen a `/topic/favoritePlaceDeleted/{groupId}`.

### `GET /api/v1/favoritePlaces/{groupId}`
Obtiene todos los lugares favoritos asociados a un grupo.

### `PUT /api/v1/favoritePlaces`
Edita un lugar favorito de forma tradicional utilizando HTTP.

### `DELETE /api/v1/favoritePlaces/{id}`
Elimina un lugar favorito mediante HTTP utilizando su ID.

### `POST /api/v1/users/push-token`
Guarda un token de notificación push para un usuario.

---

## 📄 Modelo de datos

### `LocationMessage`
```json
{
  "groupId": "group-id",
  "userId": "user-id",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "timestamp": "2025-04-04T12:00:00Z"
}
```

### `FavoritePlaceDTO`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "placeName": "Central Park",
  "latitude": 40.7851,
  "longitude": -73.9683,
  "groupId": "group-id",
  "radius": 100,
  "timestamp": "2025-04-04T12:00:00Z"
}
```

---

### WebSocket
Configuración de WebSocket y STOMP disponible en `WebSocketConfig.java`, que habilita los mensajes a través del endpoint `/ws` para clientes.

### Dotenv
Las variables de entorno se cargan desde un archivo `.env` ubicado en la raíz del proyecto mediante `DotEnvConfig`.

### Firebase
Configuración de Firebase para manejar notificaciones push disponible en `FirebaseConfig.java`. 

---

## ✅ Cobertura de Pruebas - JaCoCo

### Generar reporte:
```bash
mvn clean verify
```

Reporte HTML generado en:  
`/target/site/jacoco/index.html`

![image](https://github.com/user-attachments/assets/8f10fb7a-6605-4c8f-9a04-f7de10823243)

---

## 📊 Análisis de Calidad - SonarCloud

![image](https://github.com/user-attachments/assets/eca124d0-fd1d-46f6-a29b-1ec189f81700)


Incluye:
- Cobertura de JaCoCo
- Reglas de calidad
- Análisis estático (bugs, code smells, duplicación)

---

## 📌 Consideraciones

- Todos los servicios de ubicación y lugares favoritos están basados en WebSocket con STOMP, lo que permite una actualización en tiempo real de la información entre los miembros de un grupo.
- Se utilizan notificaciones push para alertar a los usuarios cuando ingresan o salen de lugares favoritos.
- El servicio está documentado con Swagger/OpenAPI y cubierto con pruebas automatizadas usando JaCoCo.

---

## 👨‍💻 Equipo de Desarrollo

- **Team Picada ARSW 2025**
- 📧 picadaarsw2025@outlook.com

---
