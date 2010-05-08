#ifndef LABERINTOAPPUI_H
#define LABERINTOAPPUI_H

#include <eikapp.h>
#include <eikdoc.h>
#include <e32std.h>
#include <coeccntx.h>
#include <aknappui.h>

// == Declaraci�n de la clase CLaberintoAppUi ==

// Declaraciones posteriores
class CLaberintoView;

/** Clase de interfaz de la aplicaci�n que contiene el CLaberintoView. */
class CLaberintoAppUi : public CAknAppUi {

	public:

		/** Segundo constructor. */
		void ConstructL();

		/** Destructor. */      
		virtual ~CLaberintoAppUi();
        
	private:

		/** M�todo llamado por el EIKON framework antes de mostrar un men�. */ 
		void DynInitMenuPaneL(TInt aResourceId, CEikMenuPane* aMenuPane);

		/** Manejador de comandos.
		 * @param aCommand Comando a ser controlado.
		 */
		void HandleCommandL(TInt aCommand);

		/** Manejador de eventos de teclado.
		 * @param aKeyEvent Evento a controlar.
		 * @param aType Tipo de evento. 
		 * @return C�digo de respuesta. 
		 */
		virtual TKeyResponse HandleKeyEventL(const TKeyEvent& aKeyEvent, TEventCode aType);

	private:
    
		/** Contenedor de la interfaz. */
		CLaberintoView* iAppView;

};

#endif
