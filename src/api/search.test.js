import { test, expect, jest } from '@jest/globals';
import request from 'supertest';
import express from 'express';
import router from '../src/api/search';
import Database from '../src/database/core/Database';

// Mock the Database class
jest.mock('../src/database/core/Database');

const app = express();
app.use(express.json());
app.use('/', router);

test('GET /search - should return search results for valid criteria', async () => {
    const mockResults = [{ id: 1, name: 'Test Item' }];
    Database.prototype.executeQuery = jest.fn().mockResolvedValue(mockResults);

    const response = await request(app).get('/search').query({ criteria: "name = 'Test Item'" });

    expect(response.status).toBe(200);
    expect(response.body).toEqual(mockResults);
    expect(Database.prototype.executeQuery).toHaveBeenCalledWith("SELECT * FROM items WHERE name = 'Test Item'");
});

test('GET /search - should handle database errors gracefully', async () => {
    Database.prototype.executeQuery = jest.fn().mockRejectedValue(new Error('Database error'));

    const response = await request(app).get('/search').query({ criteria: "name = 'Test Item'" });

    expect(response.status).toBe(500);
    expect(response.body).toEqual({ error: 'Erreur lors de la recherche.' });
    expect(Database.prototype.executeQuery).toHaveBeenCalledWith("SELECT * FROM items WHERE name = 'Test Item'");
});

test('performDynamicSearch - should return results for valid criteria', async () => {
    const mockResults = [{ id: 1, name: 'Test Item' }];
    Database.prototype.executeQuery = jest.fn().mockResolvedValue(mockResults);

    const results = await performDynamicSearch("name = 'Test Item'");

    expect(results).toBe(mockResults);
    expect(Database.prototype.executeQuery).toHaveBeenCalledWith("SELECT * FROM items WHERE name = 'Test Item'");
});

test('performDynamicSearch - should throw error for invalid criteria', async () => {
    Database.prototype.executeQuery = jest.fn().mockRejectedValue(new Error('Invalid query'));

    await expect(performDynamicSearch("invalid SQL")).rejects.toThrow('Invalid query');
    expect(Database.prototype.executeQuery).toHaveBeenCalledWith("SELECT * FROM items WHERE invalid SQL");
});