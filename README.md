# LoteriaMX
Juego multijugador de la Lotería mexicana desarrollado en Java, como proyecto de la materia de Arquitectura de Software (ITSON). Permite configurar partidas, registrar usuarios, iniciar juego, marcar casillas, gritar cartas y reclamar puntaje.

# Arquitectura
Este proyecto utiliza **Arquitectura Hexagonal (Puertos y Adaptadores)** para separar la lógica de negocio de las tecnologías externas:
- **Dominio:** Entidades del juego (Cartas, Tableros, Jugadores).
- **Aplicación:** Casos de uso (Iniciar partida, Cantar carta).
- **Infraestructura:** /ComponenteComunicacion (Sockets/Red), Base de datos, UI.