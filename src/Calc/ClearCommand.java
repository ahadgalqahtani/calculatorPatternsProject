package Calc;

// ClearCommand.java
public class ClearCommand implements CalculatorCommand {
    private final Calculator receiver;

    public ClearCommand(Calculator receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.clear();
    }
}