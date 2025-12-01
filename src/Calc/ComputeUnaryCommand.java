package Calc;

// ComputeUnaryCommand.java
public class ComputeUnaryCommand implements CalculatorCommand {
    private final Calculator receiver;
    private final String operation; // e.g., "√", "sin", "cos"

    public ComputeUnaryCommand(Calculator receiver, String operation) {
        this.receiver = receiver;
        this.operation = operation;
    }

    @Override
    public void execute() {
        // Delegates to the existing core logic method
        receiver.computeUnary(operation);
    }
}