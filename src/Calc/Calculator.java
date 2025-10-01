package Calc;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

// --- OPERATION  ---

interface Operation {
    float compute(float a, float b);
}

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

class OperationFactory {
    public static Operation getOperation(String op) {
        return switch (op) {
            case "+" -> new AddOperation();
            case "-" -> new SubtractOperation();
            case "×" -> new MultiplyOperation();
            case "÷" -> new DivideOperation();
            default -> null;
        };
    }
}

// --- BUTTON FACTORY ---

interface Button{
    JButton createButton(String text);
}

class ButtonFactory implements Button{

    private Color bgColor;

    public ButtonFactory(Color bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    public JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setFont(new Font("Century Gothic", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(new Color(41, 39, 44)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        return btn;
    }
}

// --- CALCULATOR CLASS ---

public final class Calculator extends JFrame {

    private static Calculator instance = null; // Singleton

    private String currentOperand;
    private String previousOperand;
    private String operation;
    
    // GUI DRAGGING FIELDS
    private int x, y;

    // Buttons
    private JButton btnDel, btnClear, btnDiv, btnMult, btn7, btn8, btn9, btnSub,
            btn4, btn5, btn6, btnPlus, btn1, btn2, btn3, btnPlusSub,
            btn0, btnDot, btnEqual;

    // Title bar
    private JPanel titleBar;
    private JLabel title;
    private JButton btnMini, btnClose;

    // --- CONSTRUCTOR (Singleton) ---
    private Calculator() {
        setupGUI();
        getContentPane().setSize(320, 530);
        this.clear();
        this.addEvents();
    }

    public static Calculator getInstance() {
        if (instance == null) {
            instance = new Calculator();
        }
        return instance;
    }

    // --- CORE LOGIC ---

    public void addEvents() {
        // Now using instance variables (btn0, btn1, etc.)
        JButton[] btns = {
            btn0, btn1, btn2, btn3, btn4,
            btn5, btn6, btn7, btn8, btn9,
            btnDiv, btnDot, btnEqual, btnDel,
            btnMult, btnPlus, btnPlusSub, btnSub, btnClear
        };

        JButton[] numbers = {
            btn0, btn1, btn2, btn3, btn4,
            btn5, btn6, btn7, btn8, btn9
        };

        for (JButton number : numbers) {
            number.addActionListener((ActionEvent e) -> {
                appendNumber(((JButton) e.getSource()).getText());
            });
        }

        for (JButton btn : btns) {
            btn.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    ((JButton) e.getSource()).setBackground(new Color(73, 69, 78));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    Object b = e.getSource();
                    if (b == btnDiv || b == btnEqual || b == btnDel || b == btnMult || b == btnSub || b == btnPlus || b == btnClear) {
                        ((JButton) b).setBackground(new Color(41, 39, 44));
                    } else {
                        ((JButton) b).setBackground(new Color(21, 20, 22));
                    }
                }
            });
        }
        
        // Add Action Listeners for all functional buttons
        btnDot.addActionListener((ActionEvent evt) -> {
            appendNumber((currentOperand.isBlank() ? "0." : "."));
        });

        btnClear.addActionListener((ActionEvent evt) -> {
            clear();
        });

        btnDel.addActionListener((ActionEvent evt) -> {
            if (!currentOperand.equals("")) {
                currentOperand = currentOperand.substring(0, currentOperand.length() - 1);
                updateDisplay();
            }
        });

        btnPlus.addActionListener((ActionEvent evt) -> {
            chooseOperation("+");
        });

        btnMult.addActionListener((ActionEvent evt) -> {
            chooseOperation("×");
        });

        btnSub.addActionListener((ActionEvent evt) -> {
            chooseOperation("-");
        });

        btnDiv.addActionListener((ActionEvent evt) -> {
            chooseOperation("÷");
        });

        btnEqual.addActionListener((ActionEvent evt) -> {
            compute();
            updateDisplay();
            if (currentOperand.equals("Error"))
                currentOperand = "";
        });

        btnPlusSub.addActionListener((ActionEvent evt) -> {
            if (!currentOperand.isBlank()) {
                float tmp = -Float.parseFloat(currentOperand);
                currentOperand = (tmp - (int) tmp) != 0 ? Float.toString(tmp) : Integer.toString((int) tmp);
                updateDisplay();
            }
        });
    }

    public void clear() {
        this.currentOperand = "";
        this.previousOperand = "";
        this.operation = "";
        this.updateDisplay();
    }

    public void appendNumber(String number) {
        if (this.currentOperand.equals("0") && number.equals("0")) {
            return;
        }

        if (number.equals(".") && this.currentOperand.contains(".")) {
            return;
        }

        if (this.currentOperand.equals("0")
                && !number.equals("0")
                && !number.equals(".")) {
            this.currentOperand = "";
        }

        this.currentOperand += number;
        this.updateDisplay();
    }

    public void chooseOperation(String operation) {
        if (this.currentOperand.equals("") && !this.previousOperand.equals("")) {
            this.operation = operation;
            this.updateDisplay();
        }
        if (this.currentOperand.equals("")) {
            return;
        }

        if (!this.previousOperand.equals("")) {
            this.compute();
        }

        this.operation = operation;
        this.previousOperand = this.currentOperand;
        this.currentOperand = "";
        this.updateDisplay();
    }

    public void compute() {
        float computation;
        if (this.currentOperand.equals("") || this.previousOperand.equals("")) {
            return;
        }

        float curr = Float.parseFloat(this.currentOperand);
        float prev = Float.parseFloat(this.previousOperand);
        if (Float.isNaN(curr) || Float.isNaN(prev)) {
            return;
        }

        switch (this.operation) {
            case "+" ->
                computation = prev + curr;
            case "-" ->
                computation = prev - curr;
            case "×" ->
                computation = prev * curr;
            case "÷" -> {
                if (curr == 0) {
                    this.clear();
                    this.currentOperand = "Error";
                    return;
                }
                computation = prev / curr;
            }
            default -> {
                return;
            }
        }

        this.currentOperand = (computation - (int) computation) != 0 ? Float.toString(computation) : Integer.toString((int) computation);
        this.previousOperand = "";
        this.operation = "";
    }

    public void updateDisplay() {
        current.setText(this.currentOperand);
        previous.setText(previousOperand + " " + this.operation);
    }

    // METHOD TO SETUP THE GUI MANUALLY
    private void setupGUI() {
        
        // --- 1. Frame and Top-Level Panel Setup ---
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calculator");
        setLocation(new java.awt.Point(500, 100));
        setUndecorated(true);
        setResizable(false);
        
        app = new JPanel();
        app.setBackground(new Color(13, 12, 20));
        app.setLayout(new BorderLayout()); // Use BorderLayout for main structure
        
        // --- 2. Title Bar Setup ---
        
        titleBar = new JPanel();
        titleBar.setBackground(new Color(21, 20, 22));
        titleBar.setLayout(null); // Use null layout to precisely position close/mini buttons
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
        
        // Add Mouse Listeners for Dragging (Inline implementation)
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
        
        // Add listeners for Mini/Close buttons (Inline implementation)
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
        resultsPanel.setLayout(new GridLayout(2, 1)); // Use 2 rows, 1 column
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
        // GridLayout: 5 rows, 4 columns, 10px horizontal and vertical gap
        buttonsPanel.setLayout(new GridLayout(5, 4, 10, 10)); 
        buttonsPanel.setBackground(new Color(21, 20, 22));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Helper function to create standard number buttons
        
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
        // Placeholder for the row skip caused by the original double-height plus
        btnPlusSub = createButton("+/-", new Color(21, 20, 22)); 
        
        btn0 = createButton("0", new Color(21, 20, 22));
        btnDot = createButton(".", new Color(21, 20, 22));
        btnEqual = createButton("=", new Color(41, 39, 44));
        
        // Add buttons in the correct grid order (top-to-bottom, left-to-right)
        buttonsPanel.add(btnDel);
        buttonsPanel.add(btnClear);
        buttonsPanel.add(btnDiv);
        buttonsPanel.add(btnMult);

        buttonsPanel.add(btn7);
        buttonsPanel.add(btn8);
        buttonsPanel.add(btn9);
        buttonsPanel.add(btnSub);

        buttonsPanel.add(btn4);
        buttonsPanel.add(btn5);
        buttonsPanel.add(btn6);
        buttonsPanel.add(btnPlus); // Now single height

        buttonsPanel.add(btn1);
        buttonsPanel.add(btn2);
        buttonsPanel.add(btn3);
        buttonsPanel.add(btnPlusSub);
        
        // Use a dummy panel or a custom layout to combine the last row elements,
        // but for pure GridLayout simplicity, we'll put the remaining buttons:
        buttonsPanel.add(btn0);
        buttonsPanel.add(btnDot);
        buttonsPanel.add(btnEqual);
        // Add a filler component to keep the 4-column structure (optional)
        buttonsPanel.add(new JPanel() {{ setOpaque(false); }}); 


        // --- 5. Assemble the Frame ---
        app.add(titleBar, BorderLayout.NORTH);
        app.add(resultsPanel, BorderLayout.CENTER);
        app.add(buttonsPanel, BorderLayout.SOUTH);
        
        getContentPane().add(app);
        
        // Pack and Display
        pack();
    }
    
    // HELPER METHOD for creating buttons with common properties
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

    // --- MOUSE LISTENERS (UNCHANGED) ---

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
        setState(Calculator.ICONIFIED);
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

        // GUI COMPONENTS (Now declared as instance fields instead of static)
    private javax.swing.JPanel app;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JTextField previous;
    private javax.swing.JTextField current;
    private javax.swing.JPanel buttonsPanel;
    
    // Buttons
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDiv;
    private javax.swing.JButton btnMult;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnSub;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btnPlus;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btnPlusSub;
    private javax.swing.JButton btn0;
    private javax.swing.JButton btnDot;
    private javax.swing.JButton btnEqual;
    
    // Title Bar Components
    private javax.swing.JPanel titleBar;
    private javax.swing.JLabel title;
    private javax.swing.JButton btnMini;
    private javax.swing.JButton btnClose;
}