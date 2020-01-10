package com.example.sbase;

public interface DataAccessCallbacks {
    /**
     * A method to be called when data access is completed.
     * @param response_code the identifier for the calling method/purpose
     * @param results the results of the operation
     */
    void onDataRequestCompleted(int response_code, Object[] results);
}
