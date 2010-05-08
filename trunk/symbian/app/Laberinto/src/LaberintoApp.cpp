#include <eikstart.h>
#include "LaberintoApp.h"
#include "LaberintoDocument.h"

// == Implementaci�n de la clase CLaberintoApp ==

// Devuelve el UID de la aplicaci�n.
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
