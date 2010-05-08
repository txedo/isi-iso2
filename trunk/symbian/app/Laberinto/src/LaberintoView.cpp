#include "LaberintoView.h"
#include "Macros.h"

// == Implementación de la clase CLaberintoView ==

// Constructor EPOC.
void CLaberintoView::ConstructL(const TRect& /*aRect*/) {
	iOpenGlInitialized = EFalse;
	CreateWindowL();

	// Activamos el modo de pantalla completa
	SetExtentToWholeScreen();
	ActivateL();

	// Inicializamos el contador de frames
	iFrame = 0;

	// Describe el formato, tipo y tamaño de los búferes usados por EGLSurface
	EGLConfig Config;
	
	// Obtenemos la pantalla principal donde dibujaremos la escena
	iEglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
	if(iEglDisplay == NULL) {
		_LIT(KGetDisplayFailed, "eglGetDisplay failed");
		User::Panic(KGetDisplayFailed, 0);
	}

	// Inicializamos la pantalla 
	if(eglInitialize(iEglDisplay, NULL, NULL) == EGL_FALSE) {
		_LIT(KInitializeFailed, "eglInitialize failed");
		User::Panic(KInitializeFailed, 0);
	}

	EGLConfig *configList = NULL;
	EGLint numOfConfigs = 0; 
	EGLint configSize = 0;

	// Obtenemos el número de posibles EGLConfigs 
	if(eglGetConfigs(iEglDisplay, configList, configSize, &numOfConfigs) == EGL_FALSE) {
		_LIT(KGetConfigsFailed, "eglGetConfigs failed");
		User::Panic(KGetConfigsFailed, 0);
	}
	configSize = numOfConfigs;

	// Reservamos memoria para la lista de configuración 
	configList = (EGLConfig*)User::Alloc(sizeof(EGLConfig)*configSize);
	if(configList == NULL) {
		_LIT(KConfigAllocFailed, "config alloc failed");
		User::Panic(KConfigAllocFailed, 0);
	}

	// Definimos las propiedades para el EGLSurface
	// (para obtener mayor rendimiento, se elige un EGLConfig con un tamaño
	// de búfer similar al del modo de visualización de la ventana)
	TDisplayMode DMode = Window().DisplayMode();
	TInt BufferSize = 0;

	switch(DMode) {
		case(EColor4K):
			BufferSize = 12;
			break;
		case(EColor64K):
			BufferSize = 16;
			break;
		case(EColor16M):
			BufferSize = 24;
			break;
		case(EColor16MU):
			case(EColor16MA):
			BufferSize = 32;
			break;
		default:
			_LIT(KDModeError, "unsupported displaymode");
			User::Panic(KDModeError, 0);
			break;
	}

	// Definimos las propiedades para pedir una superficie de dibujo
	// a pantalla completa y con anti-aliasing
	const EGLint attrib_list_fsaa[] = {
		EGL_SURFACE_TYPE,   EGL_WINDOW_BIT,
		EGL_BUFFER_SIZE,    BufferSize,
		EGL_DEPTH_SIZE,     16,
		EGL_SAMPLE_BUFFERS, 1,
		EGL_SAMPLES,        4,
		EGL_NONE
	};

	// Definimos las propiedades para pedir una superficie de dibujo
	// a pantalla completa y sin anti-aliasing
    const EGLint attrib_list[] = {
    	EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
    	EGL_BUFFER_SIZE,  BufferSize,
    	EGL_DEPTH_SIZE,   16,
    	EGL_NONE
	};

    // Intentamos usar la configuración con anti-aliasing 
    if(eglChooseConfig(iEglDisplay, attrib_list_fsaa, configList, configSize, &numOfConfigs) == EGL_FALSE) {
		_LIT( KChooseConfigFailed, "eglChooseConfig failed");
		User::Panic(KChooseConfigFailed, 0);
	}

	// Comprobamos si se encontraron configuraciones
	if(numOfConfigs == 0) {
		// Intentamos usar la configuración sin anti-aliasing
		if(eglChooseConfig(iEglDisplay, attrib_list, configList, configSize, &numOfConfigs) == EGL_FALSE) {
			_LIT( KChooseConfigFailed, "eglChooseConfig failed");
			User::Panic(KChooseConfigFailed, 0);
		}
		if(numOfConfigs == 0) {
			// Tampoco se encontraron configuraciones sin anti-aliasing
			_LIT( KNoConfig, "Can't find the requested config.");
			User::Panic(KNoConfig, 0);
		}
	}

	// Elegimos la mejor configuración, que será la primera del vector devuelto
	Config = configList[0];
	// Liberamos la lista de configuraciones
	User::Free(configList); 

	// Creamos una superficie de dibujo 
	iEglSurface = eglCreateWindowSurface(iEglDisplay, Config, &Window(), NULL);
	if(iEglSurface == NULL) {
		_LIT(KCreateWindowSurfaceFailed, "eglCreateWindowSurface failed");
		User::Panic(KCreateWindowSurfaceFailed, 0);
	}

	// Creamos un contexto de renderizado 
	iEglContext = eglCreateContext(iEglDisplay, Config, EGL_NO_CONTEXT, NULL);
	if(iEglContext == NULL) {
		_LIT(KCreateContextFailed, "eglCreateContext failed");
		User::Panic(KCreateContextFailed, 0);
	}

	// Activamos el contexto generado y lo asociamos a la superficie
	if(eglMakeCurrent(iEglDisplay, iEglSurface, iEglSurface, iEglContext) == EGL_FALSE) {
		_LIT(KMakeCurrentFailed, "eglMakeCurrent failed");
		User::Panic( KMakeCurrentFailed, 0 );
	}

	TSize size;
	size = this->Size();

	// Creamos una instancia de CLaberinto e inicializamos OpenGL ES
	iLaberinto = CLaberinto::NewL(size.iWidth, size.iHeight);
	iLaberinto->AppInit();
	iOpenGlInitialized = ETrue;
    
	// Creamos un objeto para animar la escena
	iPeriodic = CPeriodic::NewL(CActive::EPriorityIdle);
	iPeriodic->Start(MS_FRAME, MS_FRAME, TCallBack(CLaberintoView::DrawCallBack, this));
}

// Destructor.
CLaberintoView::~CLaberintoView() {
	delete iPeriodic;

	// Llamamos a AppExit para liberar los recursos reservados en AppInit
	if(iLaberinto) {
    	iLaberinto->AppExit();
    	delete iLaberinto;
    }

	// Liberamos los recursos asociados a EGL y OpenGL ES
	eglMakeCurrent(iEglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
	eglDestroySurface(iEglDisplay, iEglSurface);
	eglDestroyContext(iEglDisplay, iEglContext);
	eglTerminate(iEglDisplay);
}

// Función llamada por el framework cuando cambia el tamaño de la vista.
void CLaberintoView::SizeChanged() {
	if(iOpenGlInitialized && iLaberinto) {
		TSize size;
		size = this->Size();
		iLaberinto->SetScreenSize(size.iWidth, size.iHeight);
	}
}

// Función llamada cuando cambia el tamaño de la pantalla (entre otros).
void CLaberintoView::HandleResourceChange(TInt aType) {
	switch(aType) {
		case KEikDynamicLayoutVariantSwitch:
			SetExtentToWholeScreen();
			break;
	}
}

// Función no utilizada.
TInt CLaberintoView::CountComponentControls() const {
	return 0;
}

// Función no utilizada.
CCoeControl* CLaberintoView::ComponentControl(TInt /*aIndex*/) const {
	return NULL;
}

// Función no utilizada.
void CLaberintoView::Draw(const TRect& /*aRect*/) const {
}

// Función llamada por CPeriodic para dibujar la escena en el dispositivo.
TInt CLaberintoView::DrawCallBack(TAny* aInstance) {
	CLaberintoView* instance = (CLaberintoView*)aInstance;
	instance->iFrame++;

	// Llamamos al ciclo de renderizado principal de OpenGL ES 
	instance->iLaberinto->AppCycle(instance->iFrame);

	// Intercambiamos los búferes para volcar la imagen a la pantalla 
	eglSwapBuffers(instance->iEglDisplay, instance->iEglSurface);
    
	// Mantenemos la luz de fondo activa 
	if(!(instance->iFrame % 100)) {
		User::ResetInactivityTime();
	}
    
	// Suspendemos el hilo durante un tiempo
	if(!(instance->iFrame % 50)) {
		User::After(0);
	}

    return 0; 
}

// Función no utilizada.
void CLaberintoView::HandleControlEventL(CCoeControl* /*aControl*/, TCoeEvent /*aEventType*/) {
}
