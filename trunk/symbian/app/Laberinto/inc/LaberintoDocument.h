#ifndef LABERINTODOCUMENT_H
#define LABERINTODOCUMENT_H

#include <akndoc.h>
   
// Declaraciones posteriores
class CEikAppUi;

// == Declaraci�n de la clase CLaberintoDocument ==

/** Clase documento usada como contenedor de la aplicaci�n. */
class CLaberintoDocument : public CAknDocument {

	public:

		/** M�todo usado para crear un objeto CLaberintoDocument. */
		static CLaberintoDocument* NewL(CEikApplication& aApp);

		/** Destructor. */
		virtual ~CLaberintoDocument();

	private:

		/** Constructor. */
		CLaberintoDocument(CEikApplication& aApp);

		/** Segundo constructor. */
		void ConstructL();

	private:

		/** Crea y devuelve un objeto CLaberintoAppUi. */ 
		CEikAppUi* CreateAppUiL();
        
};

#endif
