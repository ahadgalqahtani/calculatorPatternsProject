package Calc;

// ToggleSignCommand.java
public class ToggleSignCommand implements CalculatorCommand {
    private final Calculator receiver;

    public ToggleSignCommand(Calculator receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        // Delegates to the existing core logic method
        receiver.toggleSign();
    }
}