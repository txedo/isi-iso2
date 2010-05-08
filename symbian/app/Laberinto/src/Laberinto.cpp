#include <e32std.h>
#include <Glutils.h>
#include "Laberinto.h"
#include "Macros.h"
#include "Vertices.h"

// == Implementación de la clase CLaberinto ==

// Matriz con las casillas del laberinto.
int CLaberinto::laberinto[TAM_LABERINTO][TAM_LABERINTO] = {
	{L_DERABA, L_MENARR, L_IZQABA, L_DERABA, L_IZQABA, L_SOLABA, L_SOLDER, L_HORIZO, L_MENARR, L_IZQABA},
	{L_VERTIC, L_VERTIC, L_DERARR, L_IZQARR, L_VERTIC, L_MENIZQ, L_HORIZO, L_HORIZO, L_IZQARR, L_VERTIC},
	{L_VERTIC, L_DERARR, L_HORIZO, L_IZQABA, L_DERARR, L_MENABA, L_HORIZO, L_IZQABA, L_DERABA, L_IZQARR},
	{L_VERTIC, L_DERABA, L_HORIZO, L_MENABA, L_HORIZO, L_IZQABA, L_DERABA, L_MENABA, L_MENABA, L_IZQABA},
	{L_VERTIC, L_MENIZQ, L_IZQABA, L_DERABA, L_IZQABA, L_VERTIC, L_MENIZQ, L_MENARR, L_IZQABA, L_VERTIC},
	{L_DERARR, L_IZQARR, L_VERTIC, L_VERTIC, L_DERARR, L_MENABA, L_MENDER, L_MENIZQ, L_MENABA, L_IZQARR},
	{L_SOLABA, L_SOLDER, L_MENDER, L_MENIZQ, L_HORIZO, L_HORIZO, L_MENDER, L_DERARR, L_MENARR, L_IZQABA},
	{L_MENIZQ, L_HORIZO, L_MENABA, L_IZQARR, L_DERABA, L_MENARR, L_MENDER, L_DERABA, L_MENABA, L_MENDER},
	{L_MENIZQ, L_IZQABA, L_DERABA, L_HORIZO, L_MENDER, L_VERTIC, L_VERTIC, L_DERARR, L_IZQABA, L_VERTIC},
	{L_SOLARR, L_DERARR, L_IZQARR, L_SOLDER, L_MENABA, L_IZQARR, L_DERARR, L_HORIZO, L_IZQARR, L_SOLARR}};
// Debido a la colocación de los ejes, las casillas del mapa
// están orientadas en la dirección que indica este diagrama:
//        SUR
// OESTE       ESTE
//       NORTE

// Función que crea un nuevo objeto CLaberinto.
CLaberinto* CLaberinto::NewL(TUint aWidth, TUint aHeight) {
	CLaberinto* self = new (ELeave)CLaberinto(aWidth, aHeight);
	CleanupStack::PushL(self);
	self->ConstructL();
	CleanupStack::Pop();
	return self;
}

// Constructor.
CLaberinto::CLaberinto(TUint aWidth, TUint aHeight) : 
iScreenWidth(aWidth),
iScreenHeight(aHeight) {
	// Inicialización de las variables globales
	coordX = 0;
	coordY = 0;
	posX = 0.2f * coordX;
	posZ = 0.2f * coordY;
	angRot = DIR_ESTE;
	dir = DIR_ESTE;
	lookX = 0.0f;
	lookZ = 100.0f;
	andando = false;
	girando = false;
	camSuperior = false;
}

// Destructor.
CLaberinto::~CLaberinto() {
}

// Segundo constructor.
void CLaberinto::ConstructL(void) {
}

// Inicializa OpenGL ES.
void CLaberinto::AppInit(void) {
	// Inicializamos el viewport y la matriz de proyección
	SetScreenSize(iScreenWidth, iScreenHeight);

	// Inicialización de la escena y la iluminación
	glClearColor(COLORFONDO, 0.0f);
    glShadeModel(GL_SMOOTH); 
	glEnable(GL_DEPTH_TEST);
	glEnableClientState(GL_VERTEX_ARRAY);
	glEnableClientState(GL_COLOR_ARRAY);

	// No usar correción de perspectiva 
	//glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);
}

// Libera los recursos reservados por AppInit.
void CLaberinto::AppExit(void) {
}

// Dibuja y anima los objetos de la escena.
void CLaberinto::AppCycle(TInt /*aFrame*/) {
	bool paredesH[TAM_LABERINTO + 1][TAM_LABERINTO + 1];
	bool paredesV[TAM_LABERINTO + 1][TAM_LABERINTO + 1];
	bool paredArr, paredAba, paredIzq, paredDer;
	int i, j;

	// -- Función del temporizador --

	if(girando) {
		// Rotamos la cámara
		angRot += incremAngRot;
		if(angRot < 0) {
			angRot += 360;
		} else if(angRot >= 360) {
			angRot -= 360;
		}
		// Comprobamos si nos hemos colocado en la posición deseada
		if(angRot == nuevaDir) {
			dir = nuevaDir;
			angRot = nuevaDir;
			girando = false;
		}
		// Calculamos el nuevo punto al que debe mirar la cámara
		lookX = sin(-angRot * 2 * PI / 360) * DIST_MIRAR;
		lookZ = cos(-angRot * 2 * PI / 360) * DIST_MIRAR;
	} else if(andando) {
		// Movemos al jugador
		posX += incremPosX;
		posZ += incremPosZ;
		// Comprobamos si nos hemos colocado en la posición deseada
		if((incremPosX > 0 && posX > nuevaPosX) || (incremPosX < 0 && posX < nuevaPosX)) {
			posX = nuevaPosX;
			andando = false;
		} else if((incremPosZ > 0 && posZ > nuevaPosZ) || (incremPosZ < 0 && posZ < nuevaPosZ)) {
			posZ = nuevaPosZ;
			andando = false;
		}
	}
	
	// -- Función de dibujo (colocación de la cámara) --

	// Inicialización de la escena
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();

	// Posicion de la camara virtual (position, look, up)
	if(camSuperior) {
		// Colocamos la cámara encima del jugador
		gluLookAt(posX + 0.9f, DIST_CAMSUP, posZ + 0.9f, posX, 0.0f, posZ, 0.0f, 1.0f, 0.0f);
		// Dibujamos un cubo en la posición del jugador para que se vea
		glPushMatrix();
			glTranslatef(posX, 0.0f, posZ);
			glRotatef(-angRot + 90.0f, 0.0f, 1.0f, 0.0f);
			glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
			glScalef(PIRAM_ESCALA, PIRAM_ESCALA, PIRAM_ESCALA);
			glColorPointer(4, GL_UNSIGNED_BYTE, 0, coloresPiramide);
			glVertexPointer(3, GL_FLOAT, 0, piramide);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 12);
		glPopMatrix();
	} else {
		// Colocamos la cámara enfrente del jugador
		gluLookAt(posX, 0.0f, posZ, lookX, 0.0f, lookZ, 0.0f, 1.0f, 0.0f);
	}
	
	// -- Función de dibujo (colocación de los objetos 3D) --
	
	// Dejamos el laberinto perpendicular al eje Y
	glRotatef(90.0, 1.0, 0.0, 0.0);

	// Inicializamos las matrices que se utilizarán para no
	// dibujar dos veces la misma pared
	for(i = 0; i < TAM_LABERINTO + 1; i++) {
		for(j = 0; j < TAM_LABERINTO + 1; j++) {
			paredesH[i][j] = false;
			paredesV[i][j] = false;
		}
	}
	
	// Dibujamos cada una de las casillas del laberinto
	for(i = 0; i < TAM_LABERINTO; i++) {
		for(j = 0; j < TAM_LABERINTO; j++) {
		
			// Dibujamos el suelo y el techo
			glPushMatrix();
				// Movemos las paredes a su sitio en el laberinto
				glTranslatef(i * (0.1f * PARED_ESCALA * 2), j * (0.1f * PARED_ESCALA * 2), 0.0);
				glScalef(PARED_ESCALA, PARED_ESCALA, PARED_ESCALA);
				// Dibujamos el techo (sólo si no usamos la cámara superior)
				if(!camSuperior) {
					glPushMatrix();
						glTranslatef(0.0f, 0.0f, -0.2f);
						glColorPointer(4, GL_UNSIGNED_BYTE, 0, coloresParedTecho);
						glVertexPointer(3, GL_FLOAT, 0, paredSueloTecho);
						glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
					glPopMatrix();
				}
				// Dibujamos el suelo
				glColorPointer(4, GL_UNSIGNED_BYTE, 0, coloresParedSuelo);
				glVertexPointer(3, GL_FLOAT, 0, paredSueloTecho);
				glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
			glPopMatrix();
			
			// Dibujamos las paredes
			glPushMatrix();
				// Movemos las paredes a su sitio en el laberinto
				glTranslatef(i * (0.1f * PARED_ESCALA * 2), j * (0.1f * PARED_ESCALA * 2), 0.0);
				glScalef(PARED_ESCALA, PARED_ESCALA, PARED_ESCALA);
				// Vemos qué paredes hay que dibujar a partir del tipo de casilla
				ParedesCasilla(laberinto[i][j], &paredArr, &paredAba, &paredIzq, &paredDer);
				// Dibujamos la pared de arriba (si es necesario)
				if(paredArr && !paredesV[i][j]) {
					// Movemos la pared en el eje X
					glTranslatef(-0.2f, 0.0f, 0.0f);
					glColorPointer(4, GL_UNSIGNED_BYTE, 0, coloresParedLaberinto);
					glVertexPointer(3, GL_FLOAT, 0, paredLaberinto);
					glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
					glTranslatef(0.2f, 0.0f, 0.0f);
					// Impedimos dibujar de nuevo esta pared
					paredesV[i][j] = true;
				}
				// Dibujamos la pared de abajo (si es necesario)
				if(paredAba && !paredesV[i + 1][j]) {
					// No hace falta ninguna traslación
					glColorPointer(4, GL_UNSIGNED_BYTE, 0, coloresParedLaberinto);
					glVertexPointer(3, GL_FLOAT, 0, paredLaberinto);
					glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
					// Impedimos dibujar de nuevo esta pared
					paredesV[i + 1][j] = true;
				}
				// Dibujamos la pared de la izquierda (si es necesario)
				if(paredIzq && !paredesH[i][j]) {
					// Rotamos respecto del eje Z
					glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
					glColorPointer(4, GL_UNSIGNED_BYTE, 0, coloresParedLaberinto);
					glVertexPointer(3, GL_FLOAT, 0, paredLaberinto);
					glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
					glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
					// Impedimos dibujar de nuevo esta pared
					paredesH[i][j] = true;
				}
				// Dibujamos la pared de la derecha (si es necesario)
				if(paredDer && !paredesH[i][j + 1]) {
					// Rotamos respecto del eje Z
					glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
					glColorPointer(4, GL_UNSIGNED_BYTE, 0, coloresParedLaberinto);
					glVertexPointer(3, GL_FLOAT, 0, paredLaberinto);
					glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
					glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
					// Impedimos dibujar de nuevo esta pared
					paredesH[i][j + 1] = true;
				}
			glPopMatrix();
		}
	}

	// Volcamos la imagen a la pantalla
	glFlush();
}

// Los siguientes métodos son llamados por CLaberintoAppUi al manejar
// los eventos de menú y de teclado

// Función llamada cuando se pulsa la tecla arriba.
void CLaberinto::MoveUp(void) {
	if(!andando && !girando) {
		// Comprobamos si el jugador puede moverse
		if(PuedeMoverse(coordX, coordY, dir)) {
			// Calculamos la nueva posición del jugador, los
			// incrementos y las coordenadas de la nueva casilla
			incremPosX = 0.0f;
			incremPosZ = 0.0f;
			if(dir == DIR_NORTE) {
				coordX++;
				incremPosX = INCREM_POS;
			} else if(dir == DIR_SUR) {
				coordX--;
				incremPosX = -INCREM_POS;
			} else if(dir == DIR_ESTE) {
				coordY++;
				incremPosZ = INCREM_POS;
			} else if(dir == DIR_OESTE) {
				coordY--;
				incremPosZ = -INCREM_POS;
			}
			nuevaPosX = 0.2 * coordX;
			nuevaPosZ = 0.2 * coordY;
			andando = true;
		}
	}
}

// Función llamada cuando se pulsa la tecla izquierda.
void CLaberinto::MoveLeft(void) {
	if(!andando && !girando) {
		// Rotamos la orientación del jugador hacia la izquierda
		if(dir == DIR_NORTE) {
			nuevaDir = DIR_OESTE;
		} else if(dir == DIR_OESTE) {
			nuevaDir = DIR_SUR;
		} else if(dir == DIR_SUR) {
			nuevaDir = DIR_ESTE;
		} else if(dir == DIR_ESTE) {
			nuevaDir = DIR_NORTE;
		}
		// Establecemos el ángulo de rotación
		incremAngRot = -INCREM_ANGROT;
		girando = true;
	}
}

// Función llamada cuando se pulsa la tecla derecha.
void CLaberinto::MoveRight(void) {
	if(!andando && !girando) {
		// Rotamos la orientación del jugador hacia la derecha
		if(dir == DIR_NORTE) {
			nuevaDir = DIR_ESTE;
		} else if(dir == DIR_ESTE) {
			nuevaDir = DIR_SUR;
		} else if(dir == DIR_SUR) {
			nuevaDir = DIR_OESTE;
		} else if(dir == DIR_OESTE) {
			nuevaDir = DIR_NORTE;
		}
		// Establecemos el ángulo de rotación
		incremAngRot = INCREM_ANGROT;
		girando = true;
	}
}

// Función llamada cuando se quiere cambiar la cámara.
void CLaberinto::CambiarCamara(void) {
	camSuperior = !camSuperior;
}

// Función llamada cuando varía el tamaño de la pantalla. 
void CLaberinto::SetScreenSize(TUint aWidth, TUint aHeight) {
	iScreenWidth = aWidth;
	iScreenHeight = aHeight;

	// Viewport para dibujar en toda la ventana
	glViewport(0, 0, iScreenWidth, iScreenHeight);

	// Actualizamos en la matriz de proyección el ratio ancho/alto
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(60.0f, (GLfloat)iScreenWidth/(GLfloat)iScreenHeight, 0.025f, 50.0f);

	// Volvemos al modo vista de modelo
	glMatrixMode(GL_MODELVIEW);
}

// Función auxiliar que indica si el jugador puede moverse en la
// dirección indicada estando en una determinada casilla del laberinto.
bool CLaberinto::PuedeMoverse(int x, int y, int dir) {
	bool dev;

	// Comprobamos si el movimiento es válido
	switch(laberinto[x][y]) {
		case L_VERTIC:
			dev = (dir == DIR_NORTE) || (dir == DIR_SUR);
			break;
		case L_HORIZO:
			dev = (dir == DIR_ESTE) || (dir == DIR_OESTE);
			break;
		case L_IZQARR:
			dev = (dir == DIR_OESTE) || (dir == DIR_SUR);
			break;
		case L_IZQABA:
			dev = (dir == DIR_OESTE) || (dir == DIR_NORTE);
			break;
		case L_DERARR:
			dev = (dir == DIR_ESTE) || (dir == DIR_SUR);
			break;
		case L_DERABA:
			dev = (dir == DIR_ESTE) || (dir == DIR_NORTE);
			break;
		case L_MENARR:
			dev = (dir != DIR_SUR);
			break;
		case L_MENABA:
			dev = (dir != DIR_NORTE);
			break;
		case L_MENIZQ:
			dev = (dir != DIR_OESTE);
			break;
		case L_MENDER:
			dev = (dir != DIR_ESTE);
			break;
		default:
			dev = FALSE;
			break;
	}

	return dev;
}

// Función auxiliar que indica si el jugador puede moverse en la
// dirección indicada estando en una determinada casilla del laberinto.
void CLaberinto::ParedesCasilla(int casilla, bool *paredArr,
		      bool* paredAba, bool* paredIzq, bool* paredDer) {
	// Desactivamos todas las paredes
	*paredArr = *paredAba = *paredIzq = *paredDer = false;	
	// Activamos las paredes que correspondan según el tipo de casilla
	switch(casilla) {
		case L_VERTIC:
			*paredIzq = *paredDer = true;
			break;
		case L_HORIZO:
			*paredArr = *paredAba = true;
			break;
		case L_IZQARR:
			*paredAba = *paredDer = true;
			break;
		case L_IZQABA:
			*paredArr = *paredDer = true;
			break;
		case L_DERARR:
			*paredAba = *paredIzq = true;
			break;
		case L_DERABA:
			*paredArr = *paredIzq = true;
			break;
		case L_MENARR:
			*paredArr = true;
			break;
		case L_MENABA:
			*paredAba = true;
			break;
		case L_MENIZQ:
			*paredIzq = true;
			break;
		case L_MENDER:
			*paredDer = true;
			break;
		case L_SOLARR:
			*paredAba = *paredIzq = *paredDer = true;
			break;
		case L_SOLABA:
			*paredArr = *paredIzq = *paredDer = true;
			break;
		case L_SOLIZQ:
			*paredArr = *paredAba = *paredDer = true;
			break;
		case L_SOLDER:
			*paredArr = *paredAba = *paredIzq = true;
			break;
		default: // L_VACIO
			break;
	}	
}
