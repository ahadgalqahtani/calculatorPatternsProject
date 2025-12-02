package Calc;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Font;

public final class CalculatorGUI extends javax.swing.JFrame {

    // --- SINGLETON, LOGIC & FACADE INSTANCES ---
    private static CalculatorGUI instance = null;
    private final Calculator logic; // Core logic instance
    private final CalculatorFacade calculatorFacade; // FACADE INSTANCE

    // --- ADAPTEE INSTANCE ---
    private final CalculatorApp calculatorAppAdaptee;

    // --- STATE PATTERN FIELDS ---
    private CalculatorModeState currentState;
    private final CalculatorModeState darkModeState = new DarkModeState();
    private final CalculatorModeState lightModeState = new LightModeState();

    // --- GUI DRAGGING FIELDS ---
    private int x, y;

    // --- GUI COMPONENTS (Declared as instance fields) ---
    private javax.swing.JPanel app;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JTextField previous;
    private javax.swing.JTextField current;
    private javax.swing.JPanel buttonsPanel;

    // Buttons
    private javax.swing.JButton btnDel, btnClear, btnDiv, btnMult, btn7, btn8, btn9, btnSub,
            btn4, btn5, btn6, btnPlus, btn1, btn2, btn3, btnPlusSub, btn0, btnDot, btnEqual;

    // NEW MODE TOGGLE BUTTON
    private javax.swing.JButton btnToggleMode;

    // NEW ADAPTER BUTTONS
    private javax.swing.JButton btnSqrt;
    private javax.swing.JButton btnSin;
    private javax.swing.JButton btnCos;

    // Title Bar Components
    private javax.swing.JPanel titleBar;
    private javax.swing.JLabel title;
    private javax.swing.JButton btnMini;
    private javax.swing.JButton btnClose;

    // --- CONSTRUCTOR (Singleton) ---
    private CalculatorGUI() {
        // 1. Initialize Adaptee and Core Logic
        this.calculatorAppAdaptee = new CalculatorApp();
        this.logic = new Calculator(calculatorAppAdaptee);

        // 2. Initialize the Facade, passing the core logic dependency
        this.calculatorFacade = new CalculatorFacade(this.logic);

        // Hide the original CalculatorApp frame since we only need its logic
        this.calculatorAppAdaptee.setVisible(false);

        // 3. Initialize currentState (MUST be done BEFORE setupGUI uses it)
        this.currentState = darkModeState; 

        // 4. Setup GUI components (MUST be done BEFORE applying state)
        setupGUI(); 

        // 5. Apply Initial State (applies colors to all existing components)
        this.currentState.applyMode(this); 

        getContentPane().setSize(320, 580);
        this.calculatorFacade.handleClear();
        this.updateDisplay();
        
        // 6. Set up Events (Action Listeners once, Hover Listeners initially)
        this.addEvents(); // One-time action listeners
        this.setupHoverEvents(); // Initial hover listeners
    }

    // --- SINGLETON ACCESSOR ---
    public static CalculatorGUI getInstance() {
        if (instance == null) {
            instance = new CalculatorGUI();
        }
        return instance;
    }

    // --- VIEW/CONTROLLER LOGIC (communicate with calculator through Facade)---

    /**
     * Registers all action listeners (the logic that happens on click).
     * This method is called ONLY ONCE in the constructor.
     */
    public void addEvents() {

        JButton[] numbers = {
                btn0, btn1, btn2, btn3, btn4,
                btn5, btn6, btn7, btn8, btn9
        };

        // Number button event handler (Delegates to Facade)
        for (JButton number : numbers) {
            number.addActionListener((ActionEvent e) -> {
                calculatorFacade.handleNumberOrDot(((JButton) e.getSource()).getText());
                updateDisplay();
            });
        }

        // --- MODE TOGGLE HANDLER (State Change) ---
        btnToggleMode.addActionListener((ActionEvent evt) -> {
            if (currentState instanceof DarkModeState) {
                changeMode(lightModeState);
                btnToggleMode.setText("🌙");
            } else {
                changeMode(darkModeState);
                btnToggleMode.setText("🔆");
            }
        });

        // --- Functional Button Event Handlers (Delegates to Facade) ---
        btnDot.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleNumberOrDot(".");
            updateDisplay();
        });

        btnClear.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleClear();
            updateDisplay();
        });

        btnDel.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleDelete();
            updateDisplay();
        });

        btnPlus.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleOperation("+");
            updateDisplay();
        });

        btnMult.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleOperation("×");
            updateDisplay();
        });

        btnSub.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleOperation("-");
            updateDisplay();
        });

        btnDiv.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleOperation("÷");
            updateDisplay();
        });

        btnEqual.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleEquals();
            updateDisplay();
        });

        btnPlusSub.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleToggleSign();
            updateDisplay();
        });

        // --- Unary Operation Handlers (Delegates to Facade) ---
        btnSqrt.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleSqrt();
            updateDisplay();
        });

        btnSin.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleSin();
            updateDisplay();
        });

        btnCos.addActionListener((ActionEvent evt) -> {
            calculatorFacade.handleCos();
            updateDisplay();
        });
    }
    
    /**
     * Sets up the hover event listeners for all buttons. 
     * This is called on initial setup AND every time the mode changes.
     */
    private void setupHoverEvents() {
        JButton[] btns = {
            btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
            btnDiv, btnDot, btnEqual, btnDel, btnMult, btnPlus, btnPlusSub, 
            btnSub, btnClear, btnSqrt, btnSin, btnCos, btnToggleMode,
            btnMini, btnClose
        };

        // prevents stacking on mode change
        for (JButton btn : btns) {
            for (MouseListener listener : btn.getMouseListeners()) {
                if (listener instanceof MouseAdapter) {
                    btn.removeMouseListener(listener);
                }
            }
        }

        // New Listeners using the current state's colors
        for (JButton btn : btns) {
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    ((JButton) e.getSource()).setBackground(currentState.getButtonHoverColor());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    Object b = e.getSource();

                    if (b == btnDiv || b == btnEqual || b == btnDel || b == btnMult || b == btnSub || b == btnPlus
                            || b == btnClear || b == btnSqrt || b == btnSin || b == btnCos) {
                        ((JButton) b).setBackground(currentState.getFunctionButtonBackground());
                    } else if (b == btnMini || b == btnClose || b == btnToggleMode) { // Title bar buttons
                        ((JButton) b).setBackground(currentState.getSecondaryBackground());
                    } else { // Number/Dot buttons
                        ((JButton) b).setBackground(currentState.getNumberButtonBackground());
                    }
                    
                    // Special case for close button text color reset
                    if (b == btnClose) {
                        ((JButton) b).setForeground(currentState.getPrimaryTextColor());
                    }
                }
            });
        }
    }


    /**
     * Updates the text fields using data retrieved from the CalculatorFacade.
     * This is the only link between the logic (Model) and the display (View).
     */
    public void updateDisplay() {
        current.setText(calculatorFacade.getCurrentDisplay());
        previous.setText(calculatorFacade.getPreviousDisplay());
    }

    // --- GUI SETUP METHODS (View) ---

    private void setupGUI() {

        // --- 1. Frame and Top-Level Panel Setup ---
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calculator");
        setLocation(new java.awt.Point(500, 100));
        setUndecorated(true);
        setResizable(false);

        app = new JPanel();
        app.setLayout(new BorderLayout()); // Colors applied later by currentState.applyMode()

        // --- 2. Title Bar Setup (Panel initialized first!) ---
        titleBar = new JPanel();
        titleBar.setLayout(null);
        titleBar.setPreferredSize(new java.awt.Dimension(320, 30));

        // State Design Buttons
        btnToggleMode = createTitleBarButton("🔆");
        btnToggleMode.setBounds(230, 0, 30, 30);
        titleBar.add(btnToggleMode);

        btnMini = createTitleBarButton("-");
        btnMini.setFont(new Font("Century Gothic", Font.BOLD, 24));
        btnMini.setBounds(260, 0, 30, 30);
        titleBar.add(btnMini);

        btnClose = createTitleBarButton("×");
        btnClose.setFont(new Font("Century Gothic", Font.BOLD, 24));
        btnClose.setBounds(290, 0, 30, 30);
        titleBar.add(btnClose);
        
        // Title Label
        title = new JLabel("Calculator");
        title.setFont(new Font("Century Gothic", Font.BOLD, 17));
        title.setBounds(6, 2, 100, 25);
        titleBar.add(title);


        // Mouse Listeners for Dragging
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                titleBarMouseDragged(evt);
            }
        });
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                titleBarMousePressed(evt);
            }
        });

        // Listeners for Mini/Close buttons (Only UI changes here, no action logic)
        btnMini.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMiniActionPerformed(evt);
            }
        });

        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        // --- 3. Results Panel Setup ---

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridLayout(2, 1));
        resultsPanel.setPreferredSize(new java.awt.Dimension(320, 110));

        previous = new JTextField();
        previous.setEditable(false);
        previous.setFont(new Font("Century Gothic", Font.BOLD, 18));
        previous.setHorizontalAlignment(JTextField.RIGHT);
        previous.setBorder(null);

        current = new JTextField();
        current.setEditable(false);
        current.setFont(new Font("Century Gothic", Font.BOLD, 24));
        current.setHorizontalAlignment(JTextField.RIGHT);
        current.setBorder(null);

        resultsPanel.add(previous);
        resultsPanel.add(current);

        // --- 4. Buttons Panel Setup ---

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(6, 4, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize all buttons (Initial colors will be overwritten by applyMode)
        btnDel = createButton("←", Color.BLACK);
        btnClear = createButton("C", Color.BLACK);
        btnDiv = createButton("÷", Color.BLACK);
        btnMult = createButton("×", Color.BLACK);

        btn7 = createButton("7", Color.BLACK);
        btn8 = createButton("8", Color.BLACK);
        btn9 = createButton("9", Color.BLACK);
        btnSub = createButton("-", Color.BLACK);

        btn4 = createButton("4", Color.BLACK);
        btn5 = createButton("5", Color.BLACK);
        btn6 = createButton("6", Color.BLACK);
        btnPlus = createButton("+", Color.BLACK);

        btn1 = createButton("1", Color.BLACK);
        btn2 = createButton("2", Color.BLACK);
        btn3 = createButton("3", Color.BLACK);
        btnPlusSub = createButton("+/-", Color.BLACK);

        btn0 = createButton("0", Color.BLACK);
        btnDot = createButton(".", Color.BLACK);
        btnEqual = createButton("=", Color.BLACK);

        // Initialize Unary Function buttons
        btnSqrt = createButton("√", Color.BLACK);
        btnSin = createButton("sin", Color.BLACK);
        btnCos = createButton("cos", Color.BLACK);

        // Add buttons in the 6x4 grid order

        // Row 1: New functions
        buttonsPanel.add(btnSin);
        buttonsPanel.add(btnCos);
        buttonsPanel.add(new JPanel() { { setOpaque(false); } });
        buttonsPanel.add(new JPanel() { { setOpaque(false); } });

        // Row 2: Standard functions
        buttonsPanel.add(btnDel);
        buttonsPanel.add(btnClear);
        buttonsPanel.add(btnDiv);
        buttonsPanel.add(btnMult);

        // Row 3: Numbers 7-9
        buttonsPanel.add(btn7);
        buttonsPanel.add(btn8);
        buttonsPanel.add(btn9);
        buttonsPanel.add(btnSub);

        // Row 4: Numbers 4-6
        buttonsPanel.add(btn4);
        buttonsPanel.add(btn5);
        buttonsPanel.add(btn6);
        buttonsPanel.add(btnPlus);

        // Row 5: Numbers 1-3
        buttonsPanel.add(btn1);
        buttonsPanel.add(btn2);
        buttonsPanel.add(btn3);
        buttonsPanel.add(btnPlusSub);

        // Row 6: Bottom row
        buttonsPanel.add(btn0);
        buttonsPanel.add(btnDot);
        buttonsPanel.add(btnEqual);
        buttonsPanel.add(btnSqrt);

        // --- 5. Assemble the Frame ---
        app.add(titleBar, BorderLayout.NORTH);
        app.add(resultsPanel, BorderLayout.CENTER);
        app.add(buttonsPanel, BorderLayout.SOUTH);

        getContentPane().add(app);

        pack();
    }

    // HELPER METHOD for creating standard buttons
    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setFont(new Font("Century Gothic", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        // The Border color will be set explicitly by applyButtonColors()
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        return btn;
    }

    // HELPER METHOD FOR STATE DESIGN PATTERN
    private JButton createTitleBarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Century Gothic", Font.BOLD, 17));
        btn.setForeground(currentState.getPrimaryTextColor()); // Use state color
        btn.setBackground(currentState.getSecondaryBackground()); // Use state color
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Changes the current state (mode) of the calculator and applies the new look.
     */
    public void changeMode(CalculatorModeState newState) {
        if (this.currentState != newState) {
            this.currentState = newState;
            
            // 1. Apply new colors
            this.currentState.applyMode(this);
            
            // 2. Reset the hover listeners using the new color scheme
            setupHoverEvents(); 
            
            revalidate();
            repaint();
        }
    }

    /**
     * Helper to apply the correct background/foreground/border to all buttons.
     */
    public void applyButtonColors(Color numBg, Color funcBg, Color fg, Color border) {
        JButton[] numbers = { btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnDot, btnPlusSub };
        JButton[] functions = { btnDel, btnClear, btnDiv, btnMult, btnSub, btnPlus, btnEqual, btnSqrt, btnSin, btnCos };

        for (JButton btn : numbers) {
            btn.setBackground(numBg);
            btn.setForeground(fg);
            btn.setBorder(BorderFactory.createLineBorder(border));
        }

        for (JButton btn : functions) {
            btn.setBackground(funcBg);
            btn.setForeground(fg);
            btn.setBorder(BorderFactory.createLineBorder(border));
        }

        // Title Bar buttons need their base color updated
        Color titleBarBg = currentState.getSecondaryBackground();
        Color titleBarFg = currentState.getPrimaryTextColor();
        
        btnMini.setBackground(titleBarBg);
        btnClose.setBackground(titleBarBg);
        btnToggleMode.setBackground(titleBarBg);
        
        btnMini.setForeground(titleBarFg);
        btnClose.setForeground(titleBarFg);
        btnToggleMode.setForeground(titleBarFg);
        
        // Ensure Title label color is updated
        title.setForeground(titleBarFg);
    }

    // --- PUBLIC GETTERS FOR STATE ACCESS ---
    public JPanel getApp() { return app; }
    public JPanel getTitleBar() { return titleBar; }
    public JLabel getTitleLabel() { return title; }
    public JButton getBtnMini() { return btnMini; }
    public JButton getBtnClose() { return btnClose; }
    public JTextField getPreviousField() { return previous; }
    public JTextField getCurrentField() { return current; }
    public JPanel getResultsPanel() { return resultsPanel; }
    public JPanel getButtonsPanel() { return buttonsPanel; }
    public JButton getBtnToggleMode() { return btnToggleMode; }

    // --- MOUSE LISTENERS  ---

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void btnMiniActionPerformed(java.awt.event.ActionEvent evt) {
        setState(CalculatorGUI.ICONIFIED);
    }

    private void titleBarMousePressed(java.awt.event.MouseEvent evt) {
        x = evt.getX();
        y = evt.getY();
    }

    private void titleBarMouseDragged(java.awt.event.MouseEvent evt) {
        int xx = evt.getXOnScreen();
        int yy = evt.getYOnScreen();
        this.setLocation(xx - x, yy - y);
    }
}