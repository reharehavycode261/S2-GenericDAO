import { test, expect } from '@jest/globals';
import { updateFields } from './implementation';

// Mock DBConnection object
const mockDBConnection = {
  update: jest.fn()
};

test('updateFields updates specific fields correctly', () => {
  const fields = ['name', 'age'];
  const values = ['John Doe', 30];
  const condition = 'id = 1';

  updateFields(mockDBConnection, fields, values, condition);

  // Verify that the update method was called with the correct SQL query
  expect(mockDBConnection.update).toHaveBeenCalledWith(
    'UPDATE tableName SET name = ?, age = ? WHERE id = 1',
    ['John Doe', 30]
  );
});

test('updateFields throws error when fields and values length mismatch', () => {
  const fields = ['name', 'age'];
  const values = ['John Doe'];
  const condition = 'id = 1';

  expect(() => {
    updateFields(mockDBConnection, fields, values, condition);
  }).toThrow('Fields and values arrays must have the same length');
});

test('updateFields throws error on empty fields array', () => {
  const fields = [];
  const values = [];
  const condition = 'id = 1';

  expect(() => {
    updateFields(mockDBConnection, fields, values, condition);
  }).toThrow('Fields array cannot be empty');
});

test('updateFields throws error on null DBConnection', () => {
  const fields = ['name'];
  const values = ['John Doe'];
  const condition = 'id = 1';

  expect(() => {
    updateFields(null, fields, values, condition);
  }).toThrow('DBConnection cannot be null');
});

test('updateFields handles SQL injection attempts safely', () => {
  const fields = ['name'];
  const values = ["John Doe'; DROP TABLE users; --"];
  const condition = 'id = 1';

  updateFields(mockDBConnection, fields, values, condition);

  // Verify that the update method was called with the correct SQL query
  expect(mockDBConnection.update).toHaveBeenCalledWith(
    'UPDATE tableName SET name = ? WHERE id = 1',
    ["John Doe'; DROP TABLE users; --"]
  );
});