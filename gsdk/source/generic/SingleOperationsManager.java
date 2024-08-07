package gsdk.source.generic;

import java.util.ArrayList;

import java.util.function.Supplier;

/**
 * Small utility for implementing operations that require to be performed only once.
 */
public class SingleOperationsManager {
    private static final ArrayList<Integer> operations = new ArrayList<>();

    private static final ArrayList<Object> loads = new ArrayList<>();

    /**
     * Perform single operation. If operation ID was already registered as performed, operation is not going to execute.
     *
     * @param operation Operation lambda.
     * @param operationID Operation ID.
     */
    public static void performSingleOperation(Runnable operation, int operationID) {
        if(!operationPerformed(operationID)) {
            operation.run();

            operations.add(operationID);
        }
    }

    /**
     * Perform single load. If load ID was already registered as performed, operation is not going to execute.
     *
     * @param load Load lambda.
     * @param loadID Load ID.
     */
    public static void performSingleLoad(Supplier<Object> load, int loadID) {
        if(!operationPerformed(loadID)) {
            loads.add(load.get());

            operations.add(loadID);
        }
    }

    /**
     * Get loaded object by position.
     *
     * @param pos Position.
     */
    public static Object getLoad(int pos) {
        return loads.get(pos);
    }

    /**
     * Get last loaded object.
     */
    public static Object getLastLoad() {
        return getLoad(loads.size() - 1);
    }

    /**
     * Get first loaded object.
     */
    public static Object getFirstLoad() {
        return getLoad(0);
    }

    /**
     * Is operation performed?
     *
     * @param id Operation ID.
     */
    public static boolean operationPerformed(int id) {
        return operations.contains(id);
    }
}
