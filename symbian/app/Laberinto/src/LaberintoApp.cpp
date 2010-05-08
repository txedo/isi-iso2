#include <eikstart.h>
#include "LaberintoApp.h"
#include "LaberintoDocument.h"

// == Implementación de la clase CLaberintoApp ==

// Devuelve el UID de la aplicación.
TUid CLaberintoApp::AppDllUid() const {
	return KUidLaberinto;
}

// Crea un objeto CLaberintoDocument.
CApaDocument* CLaberintoApp::CreateDocumentL() {
	return CLaberintoDocument::NewL(*this);
}

// == Otras funciones exportadas ==

CApaApplication* NewApplication() {
	return new CLaberintoApp;
}

TInt E32Main() {
	return EikStart::RunApplication(NewApplication);
}
