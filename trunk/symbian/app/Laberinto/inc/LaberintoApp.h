#ifndef LABERINTOAPP_H
#define LABERINTOAPP_H

#include <aknapp.h>
#include "Laberinto.hrh"

// == Declaraci�n de la clase CLaberintoApp ==

/** UID de la aplicaci�n. */
const TUid KUidLaberinto = { _UID3 };

/** Clase de la aplicaci�n. */
class CLaberintoApp : public CAknApplication {

	private:

		/** Crea y devuelve un objeto CLaberintoDocument.
		 * @return Puntero al objeto creado.
		 */
		CApaDocument* CreateDocumentL();

		/** Devuelve el UID de la aplicaci�n.
		 * @return Valor de KUidLaberinto.
		 */
		TUid AppDllUid() const;
		
};

// == Otras funciones exportadas ==

/** M�todo usado por E32Main para crear una nueva aplicaci�n. */
LOCAL_C CApaApplication* NewApplication();

/** Punto de entrada a la aplicaci�n EXE. */
GLDEF_C TInt E32Main();

#endif
