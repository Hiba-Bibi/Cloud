
function printAuthors(authors, id) {
	let s = `
		<table class="data-grid">
			<tr><th>Id</th><th>Nom</th><th>Année</th></tr>
	`;
	s = authors.map(a => `
		<tr>
			<td>${a.id}</td>
			<td>${a.name}</td>
			<td>${a.yearBorn}</td>
		</tr>
	`)
	.reduce((pv, cv) => pv + cv, s);
	s += '</table>';
	document.getElementById(id).innerHTML = s;
}