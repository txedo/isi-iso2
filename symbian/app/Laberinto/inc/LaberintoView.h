#ifndef LABERINTOCONTAINER_H
#define LABERINTOCONTAINER_H

#include <coecntrl.h>
#include <GLES/egl.h>
#include "Laberinto.h"
#include <akndef.h>

// == Declaración de la clase CLaberintoView ==

/** Clase contenedora que maneja la inicialización y liberación de OpenGL ES
 * y que usa la clase CLaberinto para hacer el renderizado.
 */
class CLaberintoView : public CCoeControl, MCoeControlObserver {

	public:
        
		/** Constructor que inicializa OpenGL ES.
		 * @param aRect Rectángulo del contenedor.
		 */
		void ConstructL(const TRect& aRect);

		/** Destructor. */
		virtual ~CLaberintoView();

	public:

		/** Función llamada por CPeriodic que renderiza los frames. 
		 * @param aInstance Puntero a esta instancia de CLaberintoView.
		 */
		static TInt DrawCallBack(TAny* aInstance);

	private:

		/** Método llamado cuando cambia el tamaño de la pantalla. */
		void SizeChanged();

		/** Controlador de los cambios de recursos.
		 * @param aType Valor del mensaje.
		 */
		void HandleResourceChange(TInt aType); 

		/** Función no utilizada. */
		TInt CountComponentControls() const;

		/** Función no utilizada. */
		CCoeControl* ComponentControl(TInt aIndex) const;

		/** Función no utilizada. */
        void Draw(const TRect& aRect) const;

		/** Función no utilizada. */
        void HandleControlEventL(CCoeControl* aControl,TCoeEvent aEventType);
        
	private:

		/** Pantalla donde reside la superficie de dibujo de OpenGL ES. */
		EGLDisplay iEglDisplay;
        
		/** Superficie de dibujo donde se hace el renderizado de OpenGL ES. */
		EGLSurface iEglSurface;  

		/** Contexto de renderizado de OpenGL ES. */
		EGLContext iEglContext;
        
		/** Objeto que controla la animación. */
		CPeriodic* iPeriodic;
        
		/** Bandera que indica si OpenGL ES ha sido inicializado. */
		TBool iOpenGlInitialized;

		/** Contador de frames. */
		TInt iFrame;             

	public:
        
		/** Puntero usado para llevar a cabo el renderizado de OpenGL ES */
		CLaberinto* iLaberinto;

};

#endif
