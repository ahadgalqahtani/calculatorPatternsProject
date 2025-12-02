package Calc;

import java.awt.Color;

public class LightModeState implements CalculatorModeState {

    @Override
    public Color getAppBackground() { return new Color(230, 230, 230); }
    @Override
    public Color getSecondaryBackground() { return new Color(245, 245, 245); }
    @Override
    public Color getCurrentFieldBackground() { return Color.WHITE; }
    @Override
    public Color getPreviousFieldBackground() { return new Color(245, 245, 245); }
    @Override
    public Color getPrimaryTextColor() { return new Color(30, 30, 30); }
    @Override
    public Color getSecondaryTextColor() { return new Color(100, 100, 100); }
    @Override
    public Color getNumberButtonBackground() { return Color.WHITE; }
    @Override
    public Color getFunctionButtonBackground() { return new Color(220, 220, 220); }
    @Override
    public Color getButtonHoverColor() { return new Color(200, 200, 200); }
    @Override
    public Color getButtonBorderColor() { return new Color(200, 200, 200); }
    @Override
    public String getName() { return "Light"; }

    @Override
    public void applyMode(CalculatorGUI context) {
        context.setTitle("Calculator - Light Mode");
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