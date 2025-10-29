document.addEventListener('DOMContentLoaded', function() {
    const searchButton = document.getElementById('searchButton');
    const criteriaInput = document.getElementById('criteriaInput');

    searchButton.addEventListener('click', function() {
        const criteria = criteriaInput.value;
        performSearch(criteria);
    });
});

function performSearch(criteria) {
    fetch(`/api/search?criteria=${encodeURIComponent(criteria)}`)
        .then(response => response.json())
        .then(data => renderResults(data))
        .catch(error => console.error('Erreur lors de la recherche:', error));
}

function renderResults(data) {
    const resultsContainer = document.getElementById('results');
    resultsContainer.innerHTML = '';
    data.forEach(item => {
        const itemElement = document.createElement('div');
        itemElement.textContent = JSON.stringify(item);
        resultsContainer.appendChild(itemElement);
    });
}