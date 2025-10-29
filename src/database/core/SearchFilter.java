package database.core;

// Classe pour définir un filtre de recherche
public class SearchFilter {
    private String fieldName;
    private String operator;
    private Object value;

    public SearchFilter(String fieldName, String operator, Object value) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }
}