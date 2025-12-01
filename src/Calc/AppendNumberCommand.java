package Calc;

public class AppendNumberCommand implements CalculatorCommand {
    private final Calculator receiver;
    private final String input; // The digit or dot to append

    public AppendNumberCommand(Calculator receiver, String input) {
        this.receiver = receiver;
        this.input = input;
    }

    @Override
    public void execute() {
        // Delegates the original appendNumber logic to the Calculator receiver
        receiver.appendNumber(input);
    }
}