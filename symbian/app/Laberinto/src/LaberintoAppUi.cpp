#include <avkon.hrh>
#include <aknnotewrappers.h>
#include <stringloader.h>
#include <Laberinto.rsg>
#include "LaberintoAppUi.h"
#include "LaberintoView.h" 
#include "Laberinto.hrh"

// == Implementación de la clase CLaberintoAppUi ==

// Constructor.
void CLaberintoAppUi::ConstructL() {
	BaseConstructL();
	iAppView = new (ELeave) CLaberintoView;
	iAppView->SetMopParent(this);
	iAppView->ConstructL(ClientRect());
	AddToStackL(iAppView);
}

// Destructor que libera los recursos utilizados.
CLaberintoAppUi::~CLaberintoAppUi() {
	if(iAppView) {
		RemoveFromStack(iAppView);
		delete iAppView;
	}
}

// Función llamada por el EIKON framework antes de mostrar un menú desplegable.
// Se puede usar para establecer el estado de los elementos de menú de forma
// dinámica.
void CLaberintoAppUi::DynInitMenuPaneL(TInt /*aResourceId*/, CEikMenuPane* /*aMenuPane*/) {
}

// Manejador de eventos de teclado.
TKeyResponse CLaberintoAppUi::HandleKeyEventL(const TKeyEvent& aKeyEvent, TEventCode aType) {
	if(aType == EEventKeyUp) {
		TUint scan = aKeyEvent.iScanCode;
		switch(scan) {
			case(EStdKeyUpArrow):
				iAppView->iLaberinto->MoveUp();
				return EKeyWasConsumed;
			case(EStdKeyLeftArrow):
				iAppView->iLaberinto->MoveLeft();
				return EKeyWasConsumed;
			case(EStdKeyRightArrow):
				iAppView->iLaberinto->MoveRight();
				return EKeyWasConsumed;
			default:
				return EKeyWasNotConsumed;
		}
    } else {
		return EKeyWasNotConsumed;
	}
}

// Manejador de comandos.
void CLaberintoAppUi::HandleCommandL(TInt aCommand) {
	HBufC* textResource;
	CAknInformationNote* informationNote;
	
	switch(aCommand) {
		case EAknSoftkeyBack:
		case EEikCmdExit:
			Exit();
			break;
		case ELaberintoCambiarCamara:
			iAppView->iLaberinto->CambiarCamara();
			break;
		case ELaberintoAcercaDe:
			// Mostramos el cuadro de diálogo "Acerca de..."
			textResource = StringLoader::LoadLC(R_ABOUT_DIALOG_TEXT);
			informationNote = new (ELeave)CAknInformationNote;
			informationNote->ExecuteLD(*textResource);
			CleanupStack::PopAndDestroy(textResource);
			break;
		default:
			break;      
	}
}
