package Calc;

import java.awt.Color;

public interface CalculatorModeState {
    /** Gets the background color for the main application and button panel. */
    Color getAppBackground();
    
    /** Gets the background color for the title bar and secondary panels. */
    Color getSecondaryBackground();
    
    /** Gets the color for the text fields displaying current results. */
    Color getCurrentFieldBackground();
    
    /** Gets the color for the text fields displaying previous operations. */
    Color getPreviousFieldBackground();
    
    /** Gets the primary text color (for current result, titles). */
    Color getPrimaryTextColor();
    
    /** Gets the secondary text color (for previous operation). */
    Color getSecondaryTextColor();
    
    /** Gets the background color for number and dot buttons. */
    Color getNumberButtonBackground();
    
    /** Gets the background color for function/operator buttons. */
    Color getFunctionButtonBackground();
    
    /** Gets the hover color for all buttons. */
    Color getButtonHoverColor();
    
    /** Gets the border color for buttons. */
    Color getButtonBorderColor();
    
    /** Returns the name of the mode. */
    String getName();
    
    /** Applies all colors to the CalculatorGUI components. */
    void applyMode(CalculatorGUI context);
}