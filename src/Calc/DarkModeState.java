package Calc;

import java.awt.Color;

public class DarkModeState implements CalculatorModeState {
    
    @Override
    public Color getAppBackground() { return new Color(13, 12, 20); }
    @Override
    public Color getSecondaryBackground() { return new Color(21, 20, 22); }
    @Override
    public Color getCurrentFieldBackground() { return new Color(41, 39, 44); }
    @Override
    public Color getPreviousFieldBackground() { return new Color(21, 20, 22); }
    @Override
    public Color getPrimaryTextColor() { return Color.WHITE; }
    @Override
    public Color getSecondaryTextColor() { return new Color(203, 198, 213); }
    @Override
    public Color getNumberButtonBackground() { return new Color(21, 20, 22); }
    @Override
    public Color getFunctionButtonBackground() { return new Color(41, 39, 44); }
    @Override
    public Color getButtonHoverColor() { return new Color(73, 69, 78); }
    @Override
    public Color getButtonBorderColor() { return new Color(41, 39, 44); }
    @Override
    public String getName() { return "Dark"; }

    @Override
    public void applyMode(CalculatorGUI context) {
        context.setTitle("Calculator - Dark Mode");
        context.getApp().setBackground(getAppBackground());
        
        context.getTitleBar().setBackground(getSecondaryBackground());
        context.getTitleLabel().setForeground(getPrimaryTextColor());
        context.getBtnMini().setBackground(getSecondaryBackground());
        context.getBtnClose().setBackground(getSecondaryBackground());
        
        context.getResultsPanel().setBackground(getSecondaryBackground());
        
        context.getPreviousField().setBackground(getPreviousFieldBackground());
        context.getPreviousField().setForeground(getSecondaryTextColor());
        
        context.getCurrentField().setBackground(getCurrentFieldBackground());
        context.getCurrentField().setForeground(getPrimaryTextColor());
        
        context.getButtonsPanel().setBackground(getSecondaryBackground());
        
        // Apply colors to all buttons via a helper method in CalculatorGUI
        context.applyButtonColors(
            getNumberButtonBackground(), 
            getFunctionButtonBackground(), 
            getPrimaryTextColor(), 
            getButtonBorderColor()
        );
    }
}