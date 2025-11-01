package Calc;

import java.math.BigDecimal;

// --- Target/Product Interface ---
// Used as Factory Product Interface and Adapter Target Interface

public interface Operation {
        // Binary operation (two operands)
    default float compute(float a, float b) {
        throw new UnsupportedOperationException("Binary operation not supported");
    }

    // Unary operation (one operand)
    default BigDecimal compute(BigDecimal a) {
        throw new UnsupportedOperationException("Unary operation not supported");
    }
}

// --- Concrete Products (Binary Operations) ---
// Factory Method Concrete Products

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
        if (b == 0)
            throw new ArithmeticException("Division by zero");
        return a / b;
    }
}
// --- Single Adapter (Unary Operations) ---

class UniversalUnaryAdapter implements Operation {
    
    // 1. Adaptee reference
    private final CalculatorApp calculatorAppAdaptee;
    // 2. Operation Identifier for each unary operation
    private final String opType;

    /**
     * Single constructor takes both the Adaptee and the operation identifier.
     */
    public UniversalUnaryAdapter(CalculatorApp calculatorApp, String operationType) {
        this.calculatorAppAdaptee = calculatorApp;
        this.opType = operationType;
    }
    
    // 3. Target Method Implementation with Conditional Logic
    @Override
    public BigDecimal compute(BigDecimal a) {
        return switch (opType) {
            case "√" -> calculatorAppAdaptee.sqrt(a);
            case "sin" -> calculatorAppAdaptee.sin(calculatorAppAdaptee.toRadians(a));
            case "cos" -> calculatorAppAdaptee.cos(calculatorAppAdaptee.toRadians(a));
            default -> throw new UnsupportedOperationException("Unknown unary operation: " + opType);
        };
    }
} 