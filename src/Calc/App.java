package Calc;

import javax.swing.SwingUtilities;

// import javax.swing.SwingUtilities;

/**
 *
 * @author youcefhmd
 */

public class App {

    public static void main(String[] args) {

        // Use SwingUtilities.invokeLater to ensure thread safety when launching the GUI
        SwingUtilities.invokeLater(() -> {
            // Retrieve the single instance of the CalculatorGUI
            CalculatorGUI calculator = CalculatorGUI.getInstance();
            
            calculator.setVisible(true);
        });
    }
}
