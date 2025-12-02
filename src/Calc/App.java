package Calc;

import javax.swing.SwingUtilities;

// import javax.swing.SwingUtilities;

/**
 *
 * @author youcefhmd
 */

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                CalculatorGUI calculator = CalculatorGUI.getInstance();
                calculator.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
