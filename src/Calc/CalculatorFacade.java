package Calc;

public class CalculatorFacade {
    // Reference to the complex subsystem
    private final Calculator logic;

    public CalculatorFacade(Calculator logic) {
        this.logic = logic;
    }

    // --- Facade Methods corresponding to button actions ---
    public void handleNumberOrDot(String input) {
        logic.appendNumber(input);
    }
    
    public void handleOperation(String op) {
        logic.chooseOperation(op);
    }

    public void handleEquals() {
        logic.computeBinary();
    }
    
    public void handleClear() {
        logic.clear();
    }
    
    public void handleDelete() {
        logic.deleteLastDigit();
    }
    
    public void handleToggleSign() {
        logic.toggleSign();
    }

    // Unary functions handlers
    public void handleSqrt() {
        logic.computeUnary("√");
    }

    public void handleSin() {
        logic.computeUnary("sin");
    }

    public void handleCos() {
        logic.computeUnary("cos");
    }

    // Display Getters 

    public String getCurrentDisplay() {
        return logic.getCurrentOperand();
    }

    public String getPreviousDisplay() {
        return logic.getPreviousOperandDisplay();
    }
}
