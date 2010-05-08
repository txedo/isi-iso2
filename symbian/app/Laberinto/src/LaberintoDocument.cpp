#include "LaberintoDocument.h"
#include "LaberintoAppUi.h"

// == Implementación de la clase CLaberintoDocument ==

// Constructor.
CLaberintoDocument::CLaberintoDocument(CEikApplication& aApp)
: CAknDocument(aApp) {
}

// Destructor.
CLaberintoDocument::~CLaberintoDocument() {
}

// Segundo constructor vacío.
void CLaberintoDocument::ConstructL() {
}

// Crea y devuelve un nuevo objeto CLaberintoDocument.
CLaberintoDocument* CLaberintoDocument::NewL(CEikApplication& aApp) {
	CLaberintoDocument* self = new (ELeave)CLaberintoDocument(aApp);
	CleanupStack::PushL( self );
	self->ConstructL();
	CleanupStack::Pop();
	return self;
}
    
// Crea y devuelve un nuevo objeto CLaberintoAppUi.
CEikAppUi* CLaberintoDocument::CreateAppUiL() {
	return new (ELeave)CLaberintoAppUi;
}
