#ifndef MACROS_H
#define MACROS_H

// Tipos de paredes que puede haber en cada coordenada del mapa
// - L_VACIO ................................ Sin paredes
// - L_VERTIC, L_HORIZO ..................... Pasillo (dos paredes)
// - L_IZQARR, L_IZQABA, L_DERARR, L_DERABA . Esquina (dos paredes)
// - L_MENARR, L_MENABA, L_MENIZQ, L_MENDER . Sólo una pared
// - L_SOLARR, L_SOLABA, L_SOLIZQ, L_SOLDER . Casilla sin salida (tres paredes)
#define L_VACIO 0
#define L_VERTIC 1
#define L_HORIZO 2
#define L_IZQARR 3
#define L_IZQABA 4
#define L_DERARR 5
#define L_DERABA 6
#define L_MENARR 7
#define L_MENABA 8
#define L_MENIZQ 9
#define L_MENDER 10
#define L_SOLARR 11
#define L_SOLABA 12
#define L_SOLIZQ 13
#define L_SOLDER 14

// Direcciones que puede llevar el jugador (los valores de las
// macros deben coincidir con los ángulos de rotación de la cámara)
#define DIR_NORTE 270
#define DIR_ESTE 0
#define DIR_SUR 90
#define DIR_OESTE 180

// Distancia a la que mira el jugador
#define DIST_MIRAR 100.0f

// Distancia de la cámara superior al jugador
#define DIST_CAMSUP 0.8f

// Factor por el que se escalan las paredes al dibujarlas
#define PARED_ESCALA 1.0f

// Factor por el que se escala la pirámide que muestra al jugador
#define PIRAM_ESCALA 0.45f

// Longitud de los pasos que se hacen al moverse por el laberinto
#define LONG_PASO 0.2f

// Incremento de la posición cada frame cuando se anda
#define INCREM_POS 0.025f

// Incremento del ángulo de rotación por cada frame cuando se gira
#define INCREM_ANGROT 5

// Tamaño del laberinto
#define TAM_LABERINTO 10

// Color de fondo (en formato decimal)
#define COLORFONDO 0.10f, 0.04f, 0.04f

// Colores de las paredes
#define COLOR1 255, 255, 110
#define COLOR2 50, 50, 50

// Colores del suelo y el techo
#define COLORS COLOR2
#define COLORT COLOR1

// Colores de la pirámide que muestra la situación del jugador
#define COLORPIRAM1 255, 128, 0
#define COLORPIRAM2 155, 58, 0

// Velocidad de las animaciones en milisegundos por frame
#define MS_FRAME 100

// Constantes de menú
#define MENU_CAMARA 1
#define MENU_SALIR 2

#endif
