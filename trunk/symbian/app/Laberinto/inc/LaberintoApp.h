#ifndef LABERINTOAPP_H
#define LABERINTOAPP_H

#include <aknapp.h>
#include "Laberinto.hrh"

// == Declaración de la clase CLaberintoApp ==

/** UID de la aplicación. */
const TUid KUidLaberinto = { _UID3 };

/** Clase de la aplicación. */
class CLaberintoApp : public CAknApplication {

	private:

		/** Crea y devuelve un objeto CLaberintoDocument.
		 * @return Puntero al objeto creado.
		 */
		CApaDocument* CreateDocumentL();

		/** Devuelve el UID de la aplicación.
		 * @return Valor de KUidLaberinto.
		 */
		TUid AppDllUid() const;
		
};

// == Otras funciones exportadas ==

/** Método usado por E32Main para crear una nueva aplicación. */
LOCAL_C CApaApplication* NewApplication();

/** Punto de entrada a la aplicación EXE. */
GLDEF_C TInt E32Main();

#endif
