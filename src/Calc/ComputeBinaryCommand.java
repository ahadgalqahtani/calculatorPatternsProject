package Calc;

// ComputeBinaryCommand.java
public class ComputeBinaryCommand implements CalculatorCommand {
    private final Calculator receiver;

    public ComputeBinaryCommand(Calculator receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        // Delegates the original computeBinary logic (for the '=' button)
        receiver.computeBinary();
    }
}