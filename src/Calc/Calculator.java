package Calc;

// Product Interface
interface Operation {
    float compute(float a, float b);
}

// Concrete Products

class AddOperation implements Operation {
    @Override
    public float compute(float a, float b) {
        return a + b;
    }
}

class SubtractOperation implements Operation {
    @Override
    public float compute(float a, float b) {
        return a - b;
    }
}

class MultiplyOperation implements Operation {
    @Override
    public float compute(float a, float b) {
        return a * b;
    }
}

class DivideOperation implements Operation {
    @Override
    public float compute(float a, float b) {
        // Exception handling is essential here
        if (b == 0)
            throw new ArithmeticException("Division by zero");
        return a / b;
    }
}

// The Factory
class OperationFactory {
    /**
     * Factory Method to get a specific Operation object based on the symbol.
     * * @param op The string symbol for the operation ("+", "-", "×", "÷").
     * 
     * @return A concrete implementation of the Operation interface.
     */
    public static Operation getOperation(String op) {
        return switch (op) {
            case "+" -> new AddOperation();
            case "-" -> new SubtractOperation();
            case "×" -> new MultiplyOperation();
            case "÷" -> new DivideOperation();
            default -> null; // Return null or throw an exception for unknown operations
        };
    }
}

public class Calculator {

    private String currentOperand;
    private String previousOperand;
    private String operation;

    public Calculator() {
        clear();
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
            this.compute();
        }

        this.operation = newOperation;
        this.previousOperand = this.currentOperand;
        this.currentOperand = "";
    }

    public void compute() {
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
            // Handle case where conversion fails
            clear();
            this.currentOperand = "Error";
            return;
        }
        // Factory Method Pattern: Get the appropriate Operation object
        Operation op = OperationFactory.getOperation(this.operation);

        if (op == null) {
            // Handle case where the operation symbol is not recognized
            return;
        }

        try {
            // Call the generic compute method on the interface object
            float result = op.compute(prev, curr);

            // Update State: Set the result and clear the history
            this.currentOperand = formatResult(result);
            this.previousOperand = "";
            this.operation = "";

        } catch (ArithmeticException e) {
            // Handle Division by Zero error thrown by DivideOperation
            clear();
            this.currentOperand = "Error";
        }
    }

    private String formatResult(float value) {
        return (value - (int) value) != 0 ? Float.toString(value) : Integer.toString((int) value);
    }
}