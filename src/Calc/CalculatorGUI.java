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
        this.calculatorFacade = new CalculatorFacade(this.logic); // <--- FACADE INITIALIZED
        
        // Hide the original CalculatorApp frame since we only need its logic
        this.calculatorAppAdaptee.setVisible(false);

        setupGUI();
        getContentPane().setSize(320, 580); 
        this.calculatorFacade.handleClear(); // <--- USING FACADE
        this.updateDisplay();
        this.addEvents();
    }

    // --- SINGLETON ACCESSOR ---
    public static CalculatorGUI getInstance() {
        if (instance == null) {
            instance = new CalculatorGUI();
        }
        return instance;
    }

    // --- VIEW/CONTROLLER LOGIC (communicate with calculator through Facade)---

    public void addEvents() {
        JButton[] btns = {
            btn0, btn1, btn2, btn3, btn4,
            btn5, btn6, btn7, btn8, btn9,
            btnDiv, btnDot, btnEqual, btnDel,
            btnMult, btnPlus, btnPlusSub, btnSub, btnClear, btnSqrt, btnSin, btnCos
        };

        JButton[] numbers = {
            btn0, btn1, btn2, btn3, btn4,
            btn5, btn6, btn7, btn8, btn9
        };

        // Number button event handler (Delegates to Facade)
        for (JButton number : numbers) {
            number.addActionListener((ActionEvent e) -> {
                calculatorFacade.handleNumberOrDot(((JButton) e.getSource()).getText()); // <--- FACADE
                updateDisplay();
            });
        }

        // Mouse hover event handlers (UI only - no change)
        for (JButton btn : btns) {
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    ((JButton) e.getSource()).setBackground(new Color(73, 69, 78));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    Object b = e.getSource();
                    // Keep background logic for function buttons
                    if (b == btnDiv || b == btnEqual || b == btnDel || b == btnMult || b == btnSub || b == btnPlus || b == btnClear || b == btnSqrt || b == btnSin || b == btnCos) {
                        ((JButton) b).setBackground(new Color(41, 39, 44));
                    } else {
                        ((JButton) b).setBackground(new Color(21, 20, 22));
                    }
                }
            });
        }
        
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
        app.setBackground(new Color(13, 12, 20));
        app.setLayout(new BorderLayout());
        
        // --- 2. Title Bar Setup ---
        
        titleBar = new JPanel();
        titleBar.setBackground(new Color(21, 20, 22));
        titleBar.setLayout(null);
        titleBar.setPreferredSize(new java.awt.Dimension(320, 30));
        
        title = new JLabel("Calculator");
        title.setFont(new Font("Century Gothic", Font.BOLD, 17));
        title.setForeground(Color.WHITE);
        title.setBounds(6, 2, 100, 25);
        titleBar.add(title);

        btnMini = new JButton("-");
        btnMini.setFont(new Font("Century Gothic", Font.BOLD, 24));
        btnMini.setForeground(Color.WHITE);
        btnMini.setBackground(new Color(21, 20, 22));
        btnMini.setBorder(null);
        btnMini.setFocusPainted(false);
        btnMini.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMini.setBounds(260, 0, 30, 30);
        titleBar.add(btnMini);

        btnClose = new JButton("×");
        btnClose.setFont(new Font("Century Gothic", Font.BOLD, 24));
        btnClose.setForeground(Color.WHITE);
        btnClose.setBackground(new Color(21, 20, 22));
        btnClose.setBorder(null);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClose.setBounds(290, 0, 30, 30);
        titleBar.add(btnClose);
        
        // Mouse Listeners for Dragging
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) { titleBarMouseDragged(evt); }
        });
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) { titleBarMousePressed(evt); }
        });
        
        // Listeners for Mini/Close buttons 
        btnMini.addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btnMiniMouseEntered(evt); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btnMiniMouseExited(evt); }
        });
        btnMini.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { btnMiniActionPerformed(evt); }
        });
        
        btnClose.addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btnCloseMouseEntered(evt); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btnCloseMouseExited(evt); }
        });
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { btnCloseActionPerformed(evt); }
        });


        // --- 3. Results Panel Setup ---
        
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridLayout(2, 1));
        resultsPanel.setPreferredSize(new java.awt.Dimension(320, 110));
        
        previous = new JTextField();
        previous.setEditable(false);
        previous.setBackground(new Color(21, 20, 22));
        previous.setFont(new Font("Century Gothic", Font.BOLD, 18));
        previous.setForeground(new Color(203, 198, 213));
        previous.setHorizontalAlignment(JTextField.RIGHT);
        previous.setBorder(null);

        current = new JTextField();
        current.setEditable(false);
        current.setBackground(new Color(41, 39, 44));
        current.setFont(new Font("Century Gothic", Font.BOLD, 24));
        current.setForeground(Color.WHITE);
        current.setHorizontalAlignment(JTextField.RIGHT);
        current.setBorder(null);
        
        resultsPanel.add(previous);
        resultsPanel.add(current);
        

        // --- 4. Buttons Panel Setup ---
        
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(6, 4, 10, 10)); 
        buttonsPanel.setBackground(new Color(21, 20, 22));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize all buttons
        btnDel = createButton("←", new Color(41, 39, 44));
        btnClear = createButton("C", new Color(41, 39, 44));
        btnDiv = createButton("÷", new Color(41, 39, 44));
        btnMult = createButton("×", new Color(41, 39, 44));
        
        btn7 = createButton("7", new Color(21, 20, 22));
        btn8 = createButton("8", new Color(21, 20, 22));
        btn9 = createButton("9", new Color(21, 20, 22));
        btnSub = createButton("-", new Color(41, 39, 44));
        
        btn4 = createButton("4", new Color(21, 20, 22));
        btn5 = createButton("5", new Color(21, 20, 22));
        btn6 = createButton("6", new Color(21, 20, 22));
        btnPlus = createButton("+", new Color(41, 39, 44));
        
        btn1 = createButton("1", new Color(21, 20, 22));
        btn2 = createButton("2", new Color(21, 20, 22));
        btn3 = createButton("3", new Color(21, 20, 22));
        btnPlusSub = createButton("+/-", new Color(21, 20, 22)); 
        
        btn0 = createButton("0", new Color(21, 20, 22));
        btnDot = createButton(".", new Color(21, 20, 22));
        btnEqual = createButton("=", new Color(41, 39, 44));
        
        // Initialize Unary Function buttons
        btnSqrt = createButton("√", new Color(41, 39, 44));
        btnSin = createButton("sin", new Color(41, 39, 44));
        btnCos = createButton("cos", new Color(41, 39, 44));

        // Add buttons in the 6x4 grid order
        
        // Row 1: New functions
        buttonsPanel.add(btnSin);
        buttonsPanel.add(btnCos);
        buttonsPanel.add(new JPanel() {{ setOpaque(false); }}); 
        buttonsPanel.add(new JPanel() {{ setOpaque(false); }}); 
        
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
    
    // HELPER METHOD for creating buttons with common properties (unchanged)
    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setFont(new Font("Century Gothic", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(new Color(41, 39, 44)));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        return btn;
    }

    // --- MOUSE LISTENERS (UI Actions - unchanged) ---

    private void btnCloseMouseEntered(java.awt.event.MouseEvent evt) {
        btnClose.setBackground(new Color(255, 75, 75));
        btnClose.setForeground(new Color(31, 30, 33));
    }

    private void btnCloseMouseExited(java.awt.event.MouseEvent evt) {
        btnClose.setBackground(new Color(21, 20, 22));
        btnClose.setForeground(Color.WHITE);
    }

    private void btnMiniMouseEntered(java.awt.event.MouseEvent evt) {
        btnMini.setBackground(new Color(73, 69, 78));
    }

    private void btnMiniMouseExited(java.awt.event.MouseEvent evt) {
        btnMini.setBackground(new Color(21, 20, 22));
    }

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