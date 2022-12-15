function aplyStyleToPanel(panelId){
	//Obtenemos el Elemento Panel al cual le aplicaremos el Ancho
	var elementPanel = document.getElementById(panelId);
	//Aplicamos el 99% del Ancho.
	elementPanel.style.width = '99%';
}


function hidePanel(panelId){
    //Obtenemos el Elemento Panel al cual le aplicaremos el Ancho
    var elementPanel = document.getElementById(panelId);
    //Aplicamos el 99% del Ancho.
    elementPanel.style.display = 'none';
}

function showPanel(panelId){
    //Obtenemos el Elemento Panel al cual le aplicaremos el Ancho
    var elementPanel = document.getElementById(panelId);
    //Aplicamos el 99% del Ancho.
    elementPanel.style.display = 'block';
}


function configureWidhtAndHeightToContainer(classNameContainer, classNameElementTable){
	//Traemos la lista de Elementos Contenedores en la Pagina.
	var elementsContainer = document.getElementsByClassName(classNameContainer);
	//capturamos el Numero de Elementos Contenedores
	var numberElements = elementsContainer.length;
	//Recorremos el Numero de Elementos 	
	for(var i=0;i<numberElements;i++){
			//Capturamos el Elemento
			var elementContainer = elementsContainer.item(i);
			//
		}
}


function enviarEnfoque(elementoId){
	//Obtenemos el Elemento al cual le daremos el enfoque
	var elemento = document.getElementById(elementoId);
	//Aplicamos enfoque.
	elemento.focus();
}