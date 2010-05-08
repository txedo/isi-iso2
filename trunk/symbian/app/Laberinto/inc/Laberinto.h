#ifndef LABERINTO_H
#define LABERINTO_H

#include <e32base.h>
#include <GLES/gl.h>
#include "Macros.h"

// == Declaración de la clase CLaberinto ==

/** Clase que lleva a cabo el renderizado de OpenGL ES. */
class CLaberinto : public CBase {

	public:

		/** Función que crea un nuevo objeto CLaberinto. */
		static CLaberinto* NewL(TUint aWidth, TUint aHeight);

		/** Destructor. */
		virtual ~CLaberinto();

	public:
        
		/** Inicializa OpenGL ES. */
		void AppInit( void );
        
		/** Función llamada al finalizar la aplicación. */ 
		void AppExit( void );
        
		/** Renderiza un frame de la escena.
		 * @param aFrame Número de frame a renderizar.
		 */
		void AppCycle(TInt aFrame);

		/** Notifica que el tamaño de la ventana ha cambiado durante la
		 * ejecución del programa.
		 * @param aWidth Nuevo ancho de la pantalla.
		 * @param aHeight Nuevo alto de la pantalla.
		 */
		void SetScreenSize(TUint aWidth, TUint aHeight);
        
		/* Función llamada cuando se pulsa la tecla arriba. */
		void CLaberinto::MoveUp(void);

		/* Función llamada cuando se pulsa la tecla izquierda. */
		void CLaberinto::MoveLeft(void);

		/* Función llamada cuando se pulsa la tecla derecha. */
		void CLaberinto::MoveRight(void);

		/* Función llamada cuando se quiere cambiar la cámara. */
		void CLaberinto::CambiarCamara(void);

	protected:
        
		/** Constructor que almacena el ancho y alto indicados.
		 * @param aWidth Ancho de la pantalla.
		 * @param aHeight Alto de la pantalla.
		 */
		CLaberinto( TUint aWidth, TUint aHeight);
        
		/** Segundo constructor. */
		void ConstructL(void);
        
	private:
        
		/** Ancho de la pantalla. */
		TUint iScreenWidth;
		/** Alto de la pantalla. */
		TUint iScreenHeight;

		/** Posición actual del jugador en la escena. */
		GLfloat posX, posZ;
		/** Nueva posición que tendrá el jugador tras andar. */
		GLfloat nuevaPosX, nuevaPosZ;

		/** Incremento de la posición del jugador en cada frame. */
		GLfloat incremPosX, incremPosZ;

		/** Ángulo de rotación de la cámara durante un giro. */
		int angRot;
		/** Incremento del ángulo de rotación en cada frame. */
		int incremAngRot;

		/** Dirección (o ángulo) que lleva el jugador. */
		int dir;
		/** Nueva dirección (o ángulo) que llevará el jugador tras girar. */
		int nuevaDir;

		/** Punto al que mira la cámara. */
		GLfloat lookX, lookZ;
		/** Indica si el jugador está en medio de andar o girar. */
		bool andando, girando;

		/** Coordenadas del mapa de la casilla donde está el jugador. */
		int coordX, coordY;

		/** Indica si está activa la cámara superior. */
		bool camSuperior;

		/** Matriz con las casillas del laberinto. */
		static int laberinto[TAM_LABERINTO][TAM_LABERINTO];

		/** Función auxiliar que indica si el jugador puede moverse en la
		 *  dirección indicada estando en una determinada casilla del laberinto.
		 */
		bool CLaberinto::PuedeMoverse(int x, int y, int dir);

		void CLaberinto::ParedesCasilla(int casilla, bool *paredArr,
				      bool* paredAba, bool* paredIzq, bool* paredDer);
		
};

#endif
