# Empresa de videojuegos

## Notas

La actividad **GameActivity** es una pantalla de listado donde dado un parámetro (id), busca el juego con dicho id.

## Aclaraciones

1. Si no se pasa ningún parámetro, se devuelven todos los juegos.
2. La aplicación consulta con la base de datos sqlite para cargar los juegos.
3. Si no hay juegos existentes en la base de datos, se cargan desde la API.