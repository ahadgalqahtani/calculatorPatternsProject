package Calc;

import java.math.BigDecimal;

// --- Client Context (Calculator Core Logic) ---

public class Calculator {
    private String currentOperand;
    private String previousOperand;
    private String operation;

    // Dependency on the Adaptee (CalculatorApp instance)
    private final CalculatorApp adaptee;

    /**
     * Constructor now takes the CalculatorApp instance to manage dependencies for adapters.
     */
    public Calculator(CalculatorApp app) {
        this.adaptee = app;
        clear();
    }

    /**
     * Factory Method to get a specific Operation object.
     * Uses the stored adaptee instance for adapter creation.
     */
    public Operation getOperation(String op) {
        return switch (op) {
            case "+" -> new AddOperation();
            case "-" -> new SubtractOperation();
            case "×" -> new MultiplyOperation();
            case "÷" -> new DivideOperation();
            // Adapter cases: Use the single UniversalUnaryAdapter
            case "√", "sin", "cos" -> new UniversalUnaryAdapter(this.adaptee, op); 
            default -> null; // Return null or throw an exception for unknown operations
        };
    }

    // --- State Accessors ---

    public String getCurrentOperand() {
        return currentOperand;
    }

    public String getPreviousOperandDisplay() {
        return previousOperand + " " + operation;
    }

    // --- Core Logic Methods ---

    public void clear() {
        this.currentOperand = "";
        this.previousOperand = "";
        this.operation = "";
    }

    public void deleteLastDigit() {
        if (!this.currentOperand.equals("")) {
            this.currentOperand = this.currentOperand.substring(0, this.currentOperand.length() - 1);
        }
    }

    public void toggleSign() {
        if (!this.currentOperand.isBlank()) {
            try {
                float tmp = -Float.parseFloat(this.currentOperand);
                this.currentOperand = formatResult(tmp);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
    }

    public void appendNumber(String number) {
        if (this.currentOperand.equals("0") && number.equals("0")) {
            return;
        }

        if (number.equals(".") && this.currentOperand.isBlank()) {
            this.currentOperand = "0.";
            return;
        }

        if (number.equals(".") && this.currentOperand.contains(".")) {
            return;
        }

        if (this.currentOperand.equals("0") && !number.equals("0") && !number.equals(".")) {
            this.currentOperand = "";
        }

        this.currentOperand += number;
    }

    public void chooseOperation(String newOperation) {
        if (this.currentOperand.equals("") && !this.previousOperand.equals("")) {
            this.operation = newOperation;
            return;
        }
        if (this.currentOperand.equals("")) {
            return;
        }

        if (!this.previousOperand.equals("")) {
            this.computeBinary();
        }

        this.operation = newOperation;
        this.previousOperand = this.currentOperand;
        this.currentOperand = "";
    }

    /**
     * Handles binary operations (+, -, *, /) using the Operation interface's float compute(float, float) method.
     */
    public void computeBinary() {
        // Validation: Ensure both operands are present
        if (this.currentOperand.equals("") || this.previousOperand.equals("")) {
            return;
        }

        float curr;
        float prev;

        // Parsing: Convert string operands to float
        try {
            curr = Float.parseFloat(this.currentOperand);
            prev = Float.parseFloat(this.previousOperand);
        } catch (NumberFormatException e) {
            clear();
            this.currentOperand = "Error";
            return;
        }
        // Factory Method Pattern: Get the appropriate Operation object
        Operation op = this.getOperation(this.operation);

        if (op == null) {
            return;
        }

        try {
            float result = op.compute(prev, curr);

            // Update State: Set the result and clear the history
            this.currentOperand = formatResult(result);
            this.previousOperand = "";
            this.operation = "";

        } catch (ArithmeticException e) {
            clear();
            this.currentOperand = "Error";
        }
    }

public void computeUnary(String unaryOperation) {
        if (this.currentOperand.isBlank()) {
            return;
        }

        BigDecimal curr;
        
        try {
            curr = new BigDecimal(this.currentOperand);
        } catch (NumberFormatException e) {
            clear();
            this.currentOperand = "Error";
            return;
        }

        Operation op = this.getOperation(unaryOperation);

        if (op == null) {
            return;
        }

        try {
            // The UniversalUnaryAdapter handles the operation based on its stored opType
            BigDecimal result = op.compute(curr); 
            
            this.currentOperand = result.toPlainString(); 
            this.previousOperand = "";
            this.operation = "";
        } catch (UnsupportedOperationException e) {
            // This case handles if a binary operation was mistakenly called as unary.
        }
    }

    private String formatResult(float value) {
        // Simplification for float display
        return (value - (int) value) != 0 ? Float.toString(value) : Integer.toString((int) value);
    }
}