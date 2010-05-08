#ifndef VERTICES_H
#define VERTICES_H

#include <Glutils.h>
#include "Macros.h"

// V�rtices de las paredes
GLfloat paredLaberinto[] = {
	0.1, 0.1, -0.1,
	0.1, -0.1, -0.1,
	0.1, 0.1, 0.1,
	0.1, -0.1, 0.1
};
// V�rtices del suelo y el techo
GLfloat paredSueloTecho[] = {
	0.1, 0.1, 0.1,
	-0.1, 0.1, 0.1,
	0.1, -0.1, 0.1,
	-0.1, -0.1, 0.1
};

// Colores asociados a los v�rtices de "paredLaberinto"
GLubyte coloresParedLaberinto[] = {
	COLOR1, 255,
	COLOR1, 255,
	COLOR2, 255,
	COLOR2, 255
};
// Colores asociados a los v�rtices de "paredSueloTecho"
GLubyte coloresParedTecho[] = {
	COLORT, 255,
	COLORT, 255,
	COLORT, 255,
	COLORT, 255
};
// Colores asociados a los v�rtices de "paredSueloTecho"
GLubyte coloresParedSuelo[] = {
	COLORS, 255,
	COLORS, 255,
	COLORS, 255,
	COLORS, 255
};

// V�rtices de la pir�mide que muestra la ubicaci�n del jugador
GLfloat piramide[] = {
	// Primera cara
	 0.0f,  0.1f,  0.0f,
	-0.1f, -0.1f,  0.1f,
	 0.1f, -0.1f,  0.1f,
	// Segunda cara
	 0.0f,  0.1f,  0.0f,
	 0.1f, -0.1f,  0.1f,
	 0.1f, -0.1f, -0.1f,
	// Tercera cara
	 0.0f,  0.1f,  0.0f,
	 0.1f, -0.1f, -0.1f,
	-0.1f, -0.1f, -0.1f,
	// Cuarta cara
	 0.0f,  0.1f,  0.0f,
	-0.1f, -0.1f, -0.1f,
	-0.1f, -0.1f,  0.1f
};

// Colores asociados a los v�rtices de "piramide"
GLubyte coloresPiramide[] = {
	// Primera cara
	COLORPIRAM1, 255,
	COLORPIRAM2, 255,
	COLORPIRAM2, 255,
	// Segunda cara
	COLORPIRAM1, 255,
	COLORPIRAM2, 255,
	COLORPIRAM2, 255,
	// Tercera cara
	COLORPIRAM1, 255,
	COLORPIRAM2, 255,
	COLORPIRAM2, 255,
	// Cuarta cara
	COLORPIRAM1, 255,
	COLORPIRAM2, 255,
	COLORPIRAM2, 255
};

#endif
