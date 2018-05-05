package proto;

import java.io.Serializable;

public class DistributedMapOperation implements Serializable{
    private Operation operation;
    private String key;
    private String value;

    public DistributedMapOperation(Operation operation, String key, String value) {
        this.operation = operation;
        this.key = key;
        this.value = value;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DistributedMapOperation{" +
                "operation=" + operation +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
