package Calc;
// ChooseOperationCommand.java
public class ChooseOperationCommand implements CalculatorCommand {
    private final Calculator receiver;
    private final String operation; // e.g., "+", "-", "×", "÷"

    public ChooseOperationCommand(Calculator receiver, String operation) {
        this.receiver = receiver;
        this.operation = operation;
    }

    @Override
    public void execute() {
        // Delegates the original chooseOperation logic
        receiver.chooseOperation(operation);
    }
}
