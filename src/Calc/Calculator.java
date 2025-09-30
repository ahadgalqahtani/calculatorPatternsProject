package Calc;

import java.awt.*;
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

    private int x, y; // for dragging

    // GUI COMPONENTS
    private JPanel app;
    private JPanel resultsPanel;
    private JTextField previous;
    private JTextField current;
    private JPanel buttonsPanel;

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
        JButton[] btns = { btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
                btnDiv, btnDot, btnEqual, btnDel, btnMult, btnPlus, btnPlusSub, btnSub, btnClear };

        JButton[] numbers = { btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9 };

        for (JButton number : numbers) {
            number.addActionListener(e -> appendNumber(((JButton) e.getSource()).getText()));
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
                    if (b == btnDiv || b == btnEqual || b == btnDel || b == btnMult || b == btnSub || b == btnPlus
                            || b == btnClear) {
                        ((JButton) b).setBackground(new Color(41, 39, 44));
                    } else {
                        ((JButton) b).setBackground(new Color(21, 20, 22));
                    }
                }
            });
        }

        btnDot.addActionListener(e -> appendNumber((currentOperand.isBlank() ? "0." : ".")));
        btnClear.addActionListener(e -> clear());
        btnDel.addActionListener(e -> {
            if (!currentOperand.equals("")) {
                currentOperand = currentOperand.substring(0, currentOperand.length() - 1);
                updateDisplay();
            }
        });

        btnPlus.addActionListener(e -> chooseOperation("+"));
        btnMult.addActionListener(e -> chooseOperation("×"));
        btnSub.addActionListener(e -> chooseOperation("-"));
        btnDiv.addActionListener(e -> chooseOperation("÷"));

        btnEqual.addActionListener(e -> {
            compute();
            updateDisplay();
            if (currentOperand.equals("Error"))
                currentOperand = "";
        });

        btnPlusSub.addActionListener(e -> {
            if (!currentOperand.isBlank()) {
                float tmp = -Float.parseFloat(currentOperand);
                currentOperand = (tmp - (int) tmp) != 0 ? Float.toString(tmp) : Integer.toString((int) tmp);
                updateDisplay();
            }
        });
    }

    public void clear() {
        currentOperand = "";
        previousOperand = "";
        operation = "";
        updateDisplay();
    }

    public void appendNumber(String number) {
        if (currentOperand.equals("0") && number.equals("0"))
            return;
        if (number.equals(".") && currentOperand.contains("."))
            return;
        if (currentOperand.equals("0") && !number.equals("0") && !number.equals("."))
            currentOperand = "";
        currentOperand += number;
        updateDisplay();
    }

    public void chooseOperation(String op) {
        if (currentOperand.equals("") && !previousOperand.equals("")) {
            operation = op;
            updateDisplay();
            return;
        }
        if (currentOperand.equals(""))
            return;
        if (!previousOperand.equals(""))
            compute();
        operation = op;
        previousOperand = currentOperand;
        currentOperand = "";
        updateDisplay();
    }

    public void compute() {
        if (currentOperand.equals("") || previousOperand.equals(""))
            return;

            
        float curr = Float.parseFloat(currentOperand);
        float prev = Float.parseFloat(previousOperand);

        Operation op = OperationFactory.getOperation(operation);
        if (op == null)
            return;

        try {
            float result = op.compute(prev, curr);
            currentOperand = (result - (int) result) != 0 ? Float.toString(result) : Integer.toString((int) result);
            previousOperand = "";
            operation = "";
        } catch (ArithmeticException e) {
            clear();
            currentOperand = "Error";
        }
    }

    public void updateDisplay() {
        current.setText(currentOperand);
        previous.setText(previousOperand + " " + operation);
    }

    // --- GUI SETUP USING BUTTON FACTORY ---
    private void setupGUI() {

        // --- FRAME ---
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calculator");
        setLocation(new Point(500, 100));
        setUndecorated(true);
        setResizable(false);

        app = new JPanel();
        app.setBackground(new Color(13, 12, 20));
        app.setLayout(new BorderLayout());

        // --- TITLE BAR ---
        titleBar = new JPanel();
        titleBar.setBackground(new Color(21, 20, 22));
        titleBar.setLayout(null);
        titleBar.setPreferredSize(new Dimension(320, 30));

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
        btnMini.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMini.setBounds(260, 0, 30, 30);
        titleBar.add(btnMini);

        btnClose = new JButton("×");
        btnClose.setFont(new Font("Century Gothic", Font.BOLD, 24));
        btnClose.setForeground(Color.WHITE);
        btnClose.setBackground(new Color(21, 20, 22));
        btnClose.setBorder(null);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setBounds(290, 0, 30, 30);
        titleBar.add(btnClose);

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

        btnMini.addActionListener(e -> setState(ICONIFIED));
        btnClose.addActionListener(e -> System.exit(0));

        // --- RESULTS PANEL ---
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridLayout(2, 1));
        resultsPanel.setPreferredSize(new Dimension(320, 110));

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

        // --- BUTTONS PANEL ---
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(5, 4, 10, 10));
        buttonsPanel.setBackground(new Color(21, 20, 22));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Button factories
        Button operatorFactory = new ButtonFactory(new Color(41, 39, 44));
        Button numberFactory = new ButtonFactory(new Color(21, 20, 22));

        // Create buttons
        btnDel = operatorFactory.createButton("←");
        btnClear = operatorFactory.createButton("C");
        btnDiv = operatorFactory.createButton("÷");
        btnMult = operatorFactory.createButton("×");

        btn7 = numberFactory.createButton("7");
        btn8 = numberFactory.createButton("8");
        btn9 = numberFactory.createButton("9");
        btnSub = operatorFactory.createButton("-");

        btn4 = numberFactory.createButton("4");
        btn5 = numberFactory.createButton("5");
        btn6 = numberFactory.createButton("6");
        btnPlus = operatorFactory.createButton("+");

        btn1 = numberFactory.createButton("1");
        btn2 = numberFactory.createButton("2");
        btn3 = numberFactory.createButton("3");
        btnPlusSub = numberFactory.createButton("+/-");

        btn0 = numberFactory.createButton("0");
        btnDot = numberFactory.createButton(".");
        btnEqual = operatorFactory.createButton("=");

        // Add buttons to panel
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
        buttonsPanel.add(btnPlus);
        buttonsPanel.add(btn1);
        buttonsPanel.add(btn2);
        buttonsPanel.add(btn3);
        buttonsPanel.add(btnPlusSub);
        buttonsPanel.add(btn0);
        buttonsPanel.add(btnDot);
        buttonsPanel.add(btnEqual);
        buttonsPanel.add(new JPanel() {
            {
                setOpaque(false);
            }
        }); // filler

        // Assemble frame
        app.add(titleBar, BorderLayout.NORTH);
        app.add(resultsPanel, BorderLayout.CENTER);
        app.add(buttonsPanel, BorderLayout.SOUTH);
        getContentPane().add(app);

        pack();
    }

    private void titleBarMousePressed(MouseEvent evt) {
        x = evt.getX();
        y = evt.getY();
    }

    private void titleBarMouseDragged(MouseEvent evt) {
        setLocation(evt.getXOnScreen() - x, evt.getYOnScreen() - y);
    }
}