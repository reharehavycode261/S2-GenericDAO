import pytest
import sqlite3
from base import BaseDB

class TestBaseDB:
    @pytest.fixture
    def db(self):
        conn = sqlite3.connect(':memory:')
        conn.execute('''CREATE TABLE test_table 
                       (id INTEGER PRIMARY KEY, name TEXT)''')
        db = BaseDB(conn)
        db.table_name = 'test_table'
        return db

    def test_count_empty_table(self, db):
        assert db.count() == 0

    def test_count_with_records(self, db):
        db.connection.execute("INSERT INTO test_table (name) VALUES (?)", ("test1",))
        db.connection.execute("INSERT INTO test_table (name) VALUES (?)", ("test2",))
        assert db.count() == 2

    def test_count_with_filters(self, db):
        db.connection.execute("INSERT INTO test_table (name) VALUES (?)", ("test1",))
        db.connection.execute("INSERT INTO test_table (name) VALUES (?)", ("test2",))
        assert db.count(filters={"name": "test1"}) == 1