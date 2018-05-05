package proto;

/**
 * Created by AD on 03.04.2018.
 */
public enum Operation {

    PUT(0), REMOVE(1);

    int operationValue;

    Operation(int var) {
        this.operationValue = var;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "operationValue=" + operationValue +
                '}';
    }
}
