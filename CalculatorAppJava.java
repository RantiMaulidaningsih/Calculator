package Kalkulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorAppJava extends JFrame {
    private Calculator calculator;
    private JTextField display;

    public CalculatorAppJava() {
        super("Kalkulator");
        calculator = new Calculator();
        display = new JTextField();
        display.setFont(new Font("Helvetica", Font.PLAIN, 16));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4));

        // Tombol angka 0-9
        for (int i = 1; i <= 9; i++) {
            addButton(buttonPanel, String.valueOf(i));
        }

        addButton(buttonPanel, "0");

        // Tombol operasi
        addButton(buttonPanel, "+");
        addButton(buttonPanel, "-");
        addButton(buttonPanel, "*");
        addButton(buttonPanel, "/");

        // Tombol fungsi lain
        addButton(buttonPanel, "CE");
        addButton(buttonPanel, "C");
        addButton(buttonPanel, "=");

        // Mengatur layout frame
        setLayout(new BorderLayout());
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Pengaturan frame
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void addButton(JPanel panel, String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Helvetica", Font.PLAIN, 16));
        button.addActionListener(new ButtonClickListener());
        panel.add(button);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String buttonText = source.getText();

            if (Character.isDigit(buttonText.charAt(0))) {
                calculator.inputNumber(buttonText);
            } else if ("+-*/".contains(buttonText)) {
                calculator.inputOperator(buttonText);
            } else if ("CE".equals(buttonText)) {
                calculator.clearEntry();
            } else if ("C".equals(buttonText)) {
                calculator.clear();
            } else if ("=".equals(buttonText)) {
                calculator.calculate();
            }

            display.setText(calculator.getInput());
        }
    }

    private static class Calculator {
        private StringBuilder input;
        private double result;

        public Calculator() {
            clear();
        }

        public void inputNumber(String number) {
            input.append(number);
        }

        public void inputOperator(String operator) {
            if (input.length() > 0 && Character.isDigit(input.charAt(input.length() - 1))) {
                input.append(operator);
            }
        }

        public void clearEntry() {
            if (input.length() > 0) {
                input.deleteCharAt(input.length() - 1);
            }
        }

        public void clear() {
            input = new StringBuilder();
            result = 0;
        }

        public void calculate() {
            try {
                result = evaluateExpression(input.toString());
                input.setLength(0);
                input.append(result);
            } catch (ArithmeticException e) {
                input.setLength(0);
                input.append("Error");
            }
        }

        private double evaluateExpression(String expression) {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
                }

                boolean isDigit() {
                    return Character.isDigit(ch);
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    for (; ; ) {
                        if (eat('+')) x += parseTerm();
                        else if (eat('-')) x -= parseTerm();
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (; ; ) {
                        if (eat('*')) x *= parseFactor();
                        else if (eat('/')) x /= parseFactor();
                        else return x;
                    }
                }

                double parseFactor() {
                    if (eat('+')) return parseFactor();
                    if (eat('-')) return -parseFactor();

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) {
                        x = parseExpression();
                        eat(')');
                    } else if (isDigit() || ch == '.') {
                        while (isDigit() || ch == '.') nextChar();
                        x = Double.parseDouble(expression.substring(startPos, this.pos));
                    } else {
                        throw new RuntimeException("Unexpected: " + (char) ch);
                    }

                    return x;
                }

                boolean eat(int charToEat) {
                    while (Character.isWhitespace(ch)) nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }
            }.parse();
        }

        public String getInput() {
            return input.toString();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalculatorAppJava::new);
    }
}