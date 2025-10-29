import { test, expect } from '@jest/globals';

// Mocking the DOM elements
document.body.innerHTML = `
    <button id="searchButton"></button>
    <input id="criteriaInput" />
    <div id="results"></div>
`;

// Mocking the fetch API
global.fetch = jest.fn(() =>
    Promise.resolve({
        json: () => Promise.resolve([{ id: 1, name: 'Test Item' }]),
    })
);

// Importing the functions to test
import { performSearch, renderResults } from '../src/ui/search';

// Test suite for performSearch function
test('performSearch should fetch data and call renderResults', async () => {
    const criteria = 'test';
    const mockRenderResults = jest.fn();
    global.renderResults = mockRenderResults;

    await performSearch(criteria);

    expect(fetch).toHaveBeenCalledWith(`/api/search?criteria=test`);
    expect(mockRenderResults).toHaveBeenCalledWith([{ id: 1, name: 'Test Item' }]);
});

test('performSearch should handle fetch errors gracefully', async () => {
    const consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation(() => {});
    fetch.mockImplementationOnce(() => Promise.reject('API is down'));

    await performSearch('test');

    expect(consoleErrorSpy).toHaveBeenCalledWith('Erreur lors de la recherche:', 'API is down');

    consoleErrorSpy.mockRestore();
});

// Test suite for renderResults function
test('renderResults should render items in the results container', () => {
    const data = [{ id: 1, name: 'Test Item' }, { id: 2, name: 'Another Item' }];
    renderResults(data);

    const resultsContainer = document.getElementById('results');
    expect(resultsContainer.children.length).toBe(2);
    expect(resultsContainer.children[0].textContent).toBe(JSON.stringify(data[0]));
    expect(resultsContainer.children[1].textContent).toBe(JSON.stringify(data[1]));
});

test('renderResults should handle empty data array', () => {
    const data = [];
    renderResults(data);

    const resultsContainer = document.getElementById('results');
    expect(resultsContainer.children.length).toBe(0);
});