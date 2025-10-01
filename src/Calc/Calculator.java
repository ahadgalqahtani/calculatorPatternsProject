package Calc;


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
        if (this.currentOperand.equals("") || this.previousOperand.equals("")) {
            return;
        }

        try {
            float curr = Float.parseFloat(this.currentOperand);
            float prev = Float.parseFloat(this.previousOperand);
            float computation;

            switch (this.operation) {
                case "+" -> computation = prev + curr;
                case "-" -> computation = prev - curr;
                case "×" -> computation = prev * curr;
                case "÷" -> {
                    if (curr == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    computation = prev / curr;
                }
                default -> { return; }
            }

            this.currentOperand = formatResult(computation);
            this.previousOperand = "";
            this.operation = "";
        } catch (ArithmeticException | NumberFormatException e) {
            clear();
            this.currentOperand = "Error";
        }
    }
    
    private String formatResult(float value) {
        return (value - (int) value) != 0 ? Float.toString(value) : Integer.toString((int) value);
    }
}