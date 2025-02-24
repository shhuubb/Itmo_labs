package Utility;

public class ExecutionResponse {
    private String response;
    private boolean success;
    public ExecutionResponse(String response, boolean success) {
        this.response = response;
        this.success = success;
    }

    public String getResponse() {
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return String.format("ExecutionResponse [response=%s, success=%s]", response, success);
    }
}
