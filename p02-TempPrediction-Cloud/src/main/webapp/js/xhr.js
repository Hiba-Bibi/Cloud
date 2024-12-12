function getAuthors() {
	let keyword = document.getElementById('keyword').value;
	let url = "biblio/api/get-authors-by-keyword?keyword=" + keyword;
	
	// Implémentation d'un moteur AJAX : Asynchronous JavaScript And XML
	let xhr = new XMLHttpRequest();
	xhr.open('GET', url, true);
	xhr.onreadystatechange = () => { // Callback Function
		if (xhr.readyState == XMLHttpRequest.DONE) {
			if (xhr.status == 200) {
				let data = xhr.responseText;
				let authors = JSON.parse(data);
				printAuthors(authors, 'result');
			}
			else { // cas d'erreur
				
			}
		}
	}
	xhr.send();
}