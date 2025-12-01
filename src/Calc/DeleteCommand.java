package Calc;

// DeleteCommand.java
public class DeleteCommand implements CalculatorCommand {
    private final Calculator receiver;

    public DeleteCommand(Calculator receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        // Delegates to the existing core logic method
        receiver.deleteLastDigit();
    }
}
