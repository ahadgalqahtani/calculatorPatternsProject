package Calc;
// CalculatorFacade.java (Revised)

public class CalculatorFacade {
    // Reference to the complex subsystem (The Receiver)
    private final Calculator logic;

    public CalculatorFacade(Calculator logic) {
        this.logic = logic;
    }

    /**
     * Helper method to execute the command.
     */
    private void executeAndRecord(CalculatorCommand command) {
        command.execute();
    }

    // --- Facade Methods using the Command Pattern ---

    // Handles digits (0-9) and the decimal dot
    public void handleNumberOrDot(String input) {
        CalculatorCommand command = new AppendNumberCommand(logic, input);
        executeAndRecord(command);
    }

    // Handles binary operators (+, -, ×, ÷)
    public void handleOperation(String op) {
        CalculatorCommand command = new ChooseOperationCommand(logic, op);
        executeAndRecord(command);
    }

    // Handles the equals button (=)
    public void handleEquals() {
        CalculatorCommand command = new ComputeBinaryCommand(logic);
        executeAndRecord(command);
    }

    // Handles the clear button (C)
    public void handleClear() {
        CalculatorCommand command = new ClearCommand(logic);
        executeAndRecord(command);
    }

    // Handles the delete button (←)
    public void handleDelete() {
        CalculatorCommand command = new DeleteCommand(logic);
        executeAndRecord(command);
    }

    // Handles the sign toggle (+/-)
    public void handleToggleSign() {
        CalculatorCommand command = new ToggleSignCommand(logic);
        executeAndRecord(command);
    }

    // Handles Unary functions (√, sin, cos)
    private void handleUnary(String unaryOp) {
        CalculatorCommand command = new ComputeUnaryCommand(logic, unaryOp);
        executeAndRecord(command);
    }

    // ... Simplified handlers for your existing methods
    public void handleSqrt() {
        handleUnary("√");
    }

    public void handleSin() {
        handleUnary("sin");
    }

    public void handleCos() {
        handleUnary("cos");
    }

    // Display Getters (These do not change state, so they don't need Commands)
    public String getCurrentDisplay() {
        return logic.getCurrentOperand();
    }

    public String getPreviousDisplay() {
        return logic.getPreviousOperandDisplay();
    }
}