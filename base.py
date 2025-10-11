class BaseDB:
    def __init__(self, connection):
        self.connection = connection
        self.table_name = None

    def count(self, filters=None):
        """
        Count records in the database table with optional filtering.
        
        Args:
            filters (dict, optional): Dictionary of field-value pairs for filtering
            
        Returns:
            int: Number of records matching the criteria
        """
        query = f"SELECT COUNT(*) FROM {self.table_name}"
        
        if filters:
            conditions = []
            values = []
            for field, value in filters.items():
                conditions.append(f"{field} = ?")
                values.append(value)
            
            if conditions:
                query += " WHERE " + " AND ".join(conditions)
            
            cursor = self.connection.execute(query, values)
        else:
            cursor = self.connection.execute(query)
            
        return cursor.fetchone()[0]