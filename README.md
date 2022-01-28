# ChallengFinalMD

A continuación, redacto brevemente el proceso de creación de este proyecto:

-Para empezar, leí los requerimientos para identificar las herramientas a usar, de esta forma también fragmente el proyecto en dos partes, el loguin y la lista de equipos con sus datos.

-Comencé con dos proyectos separados, una para el loguin, ya que quería implementar el registro de usuarios que no lo había hecho previamente. Por otro lado, la lista de equipos con sus datos, para implementar sin problema el seteo de favoritos como la búsqueda por nombre.

-Luego, un tercer proyecto para implementar el mapa y el uso de Geocoder, que también desconocía su implementación.

-Para finalizar una vez cada parte ya funcionar cree el proyecto final anexando cada parte sin grandes problemas.

---

Algunas decisiones de diseño:

-La lista de favoritos es recorrer la lista de todos los equipos y mostrar solo los que sean favoritos, esto evita crear una nueva lista en database de solo favoritos con algunos mismos elementos que la lista completa de equipo.

-Mostrar la lista de favoritos es sobre el mismo fragment que mostrar la lista completa de equipos, volviendo a dibujar la lista solo con los equipos favoritos, esto permite reutilizar el buscador de equipos solamente con la lista de favoritos.

-El objeto Equipo almacena la id de las ligas a las que puede pertenecer un equipo, esto tiene dos propósitos, primero, porque existe un identificador de una liga inexistente (_No League Soccer), permitiendo no mostrarla tan solo con ese identificador. Por último, permite en un futuro traer la tabla de clasificaciones de esa misma liga.
