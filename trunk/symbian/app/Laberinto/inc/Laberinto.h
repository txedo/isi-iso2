#ifndef LABERINTO_H
#define LABERINTO_H

#include <e32base.h>
#include <GLES/gl.h>
#include "Macros.h"

// == Declaraci�n de la clase CLaberinto ==

/** Clase que lleva a cabo el renderizado de OpenGL ES. */
class CLaberinto : public CBase {

	public:

		/** Funci�n que crea un nuevo objeto CLaberinto. */
		static CLaberinto* NewL(TUint aWidth, TUint aHeight);

		/** Destructor. */
		virtual ~CLaberinto();

	public:
        
		/** Inicializa OpenGL ES. */
		void AppInit( void );
        
		/** Funci�n llamada al finalizar la aplicaci�n. */ 
		void AppExit( void );
        
		/** Renderiza un frame de la escena.
		 * @param aFrame N�mero de frame a renderizar.
		 */
		void AppCycle(TInt aFrame);

		/** Notifica que el tama�o de la ventana ha cambiado durante la
		 * ejecuci�n del programa.
		 * @param aWidth Nuevo ancho de la pantalla.
		 * @param aHeight Nuevo alto de la pantalla.
		 */
		void SetScreenSize(TUint aWidth, TUint aHeight);
        
		/* Funci�n llamada cuando se pulsa la tecla arriba. */
		void CLaberinto::MoveUp(void);

		/* Funci�n llamada cuando se pulsa la tecla izquierda. */
		void CLaberinto::MoveLeft(void);

		/* Funci�n llamada cuando se pulsa la tecla derecha. */
		void CLaberinto::MoveRight(void);

		/* Funci�n llamada cuando se quiere cambiar la c�mara. */
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

		/** Posici�n actual del jugador en la escena. */
		GLfloat posX, posZ;
		/** Nueva posici�n que tendr� el jugador tras andar. */
		GLfloat nuevaPosX, nuevaPosZ;

		/** Incremento de la posici�n del jugador en cada frame. */
		GLfloat incremPosX, incremPosZ;

		/** �ngulo de rotaci�n de la c�mara durante un giro. */
		int angRot;
		/** Incremento del �ngulo de rotaci�n en cada frame. */
		int incremAngRot;

		/** Direcci�n (o �ngulo) que lleva el jugador. */
		int dir;
		/** Nueva direcci�n (o �ngulo) que llevar� el jugador tras girar. */
		int nuevaDir;

		/** Punto al que mira la c�mara. */
		GLfloat lookX, lookZ;
		/** Indica si el jugador est� en medio de andar o girar. */
		bool andando, girando;

		/** Coordenadas del mapa de la casilla donde est� el jugador. */
		int coordX, coordY;

		/** Indica si est� activa la c�mara superior. */
		bool camSuperior;

		/** Matriz con las casillas del laberinto. */
		static int laberinto[TAM_LABERINTO][TAM_LABERINTO];

		/** Funci�n auxiliar que indica si el jugador puede moverse en la
		 *  direcci�n indicada estando en una determinada casilla del laberinto.
		 */
		bool CLaberinto::PuedeMoverse(int x, int y, int dir);

		void CLaberinto::ParedesCasilla(int casilla, bool *paredArr,
				      bool* paredAba, bool* paredIzq, bool* paredDer);
		
};

#endif
