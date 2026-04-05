package com.mycompany.calculadora;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CalculadoraApp extends JFrame {

    private final JTextArea operationArea;
    private final JLabel resultLabel;
    private final JRadioButton degreesButton;
    private final JRadioButton radiansButton;
    private final DecimalFormat decimalFormat;
    private String expression;

    public CalculadoraApp() {
        expression = "";
        decimalFormat = new DecimalFormat("0.###############");

        setTitle("Calculadora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(470, 660);
        setMinimumSize(new Dimension(430, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(13, 27, 42));

        JPanel displayPanel = new JPanel(new BorderLayout(8, 8));
        displayPanel.setBackground(new Color(27, 38, 59));
        displayPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(18, 18, 10, 18),
                BorderFactory.createLineBorder(new Color(65, 90, 119), 2)
        ));

        JLabel titleLabel = new JLabel("Calculadora cientifica");
        titleLabel.setForeground(new Color(224, 225, 221));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        operationArea = new JTextArea(2, 20);
        operationArea.setEditable(false);
        operationArea.setLineWrap(true);
        operationArea.setWrapStyleWord(true);
        operationArea.setFont(new Font("Consolas", Font.PLAIN, 20));
        operationArea.setBackground(new Color(27, 38, 59));
        operationArea.setForeground(new Color(224, 225, 221));
        operationArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JScrollPane operationScroll = new JScrollPane(operationArea);
        operationScroll.setBorder(BorderFactory.createEmptyBorder());
        operationScroll.getViewport().setBackground(new Color(27, 38, 59));

        resultLabel = new JLabel("0");
        resultLabel.setHorizontalAlignment(JLabel.RIGHT);
        resultLabel.setForeground(new Color(119, 211, 149));
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 6));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(titleLabel, BorderLayout.WEST);

        JPanel modePanel = new JPanel();
        modePanel.setOpaque(false);
        degreesButton = createModeButton("Grados", true);
        radiansButton = createModeButton("Radianes", false);
        ButtonGroup group = new ButtonGroup();
        group.add(degreesButton);
        group.add(radiansButton);
        modePanel.add(degreesButton);
        modePanel.add(radiansButton);
        topBar.add(modePanel, BorderLayout.EAST);

        displayPanel.add(topBar, BorderLayout.NORTH);
        displayPanel.add(operationScroll, BorderLayout.CENTER);
        displayPanel.add(resultLabel, BorderLayout.SOUTH);

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setBackground(new Color(13, 27, 42));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        addButton(buttonsPanel, "C", 0, 0, 1, createActionButtonColor(), e -> clearAll());
        addButton(buttonsPanel, "<-", 1, 0, 1, createActionButtonColor(), e -> deleteLast());
        addButton(buttonsPanel, "(", 2, 0, 1, createOperatorButtonColor(), e -> appendText("("));
        addButton(buttonsPanel, ")", 3, 0, 1, createOperatorButtonColor(), e -> appendText(")"));

        addButton(buttonsPanel, "sin", 0, 1, 1, createFunctionButtonColor(), e -> appendText("sin("));
        addButton(buttonsPanel, "cos", 1, 1, 1, createFunctionButtonColor(), e -> appendText("cos("));
        addButton(buttonsPanel, "tan", 2, 1, 1, createFunctionButtonColor(), e -> appendText("tan("));
        addButton(buttonsPanel, "pi", 3, 1, 1, createFunctionButtonColor(), e -> appendText("pi"));

        addButton(buttonsPanel, "7", 0, 2, 1, createNumberButtonColor(), e -> appendText("7"));
        addButton(buttonsPanel, "8", 1, 2, 1, createNumberButtonColor(), e -> appendText("8"));
        addButton(buttonsPanel, "9", 2, 2, 1, createNumberButtonColor(), e -> appendText("9"));
        addButton(buttonsPanel, "/", 3, 2, 1, createOperatorButtonColor(), e -> appendText("/"));

        addButton(buttonsPanel, "4", 0, 3, 1, createNumberButtonColor(), e -> appendText("4"));
        addButton(buttonsPanel, "5", 1, 3, 1, createNumberButtonColor(), e -> appendText("5"));
        addButton(buttonsPanel, "6", 2, 3, 1, createNumberButtonColor(), e -> appendText("6"));
        addButton(buttonsPanel, "*", 3, 3, 1, createOperatorButtonColor(), e -> appendText("*"));

        addButton(buttonsPanel, "1", 0, 4, 1, createNumberButtonColor(), e -> appendText("1"));
        addButton(buttonsPanel, "2", 1, 4, 1, createNumberButtonColor(), e -> appendText("2"));
        addButton(buttonsPanel, "3", 2, 4, 1, createNumberButtonColor(), e -> appendText("3"));
        addButton(buttonsPanel, "-", 3, 4, 1, createOperatorButtonColor(), e -> appendText("-"));

        addButton(buttonsPanel, "0", 0, 5, 1, createNumberButtonColor(), e -> appendText("0"));
        addButton(buttonsPanel, ".", 1, 5, 1, createNumberButtonColor(), e -> appendText("."));
        addButton(buttonsPanel, "+/-", 2, 5, 1, createActionButtonColor(), e -> toggleSign());
        addButton(buttonsPanel, "+", 3, 5, 1, createOperatorButtonColor(), e -> appendText("+"));

        addButton(buttonsPanel, "=", 0, 6, 4, new Color(119, 211, 149), e -> calculateResult());

        add(displayPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
    }

    private JRadioButton createModeButton(String text, boolean selected) {
        JRadioButton button = new JRadioButton(text, selected);
        button.setOpaque(false);
        button.setForeground(new Color(224, 225, 221));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return button;
    }

    private Color createNumberButtonColor() {
        return new Color(224, 225, 221);
    }

    private Color createOperatorButtonColor() {
        return new Color(65, 90, 119);
    }

    private Color createFunctionButtonColor() {
        return new Color(95, 129, 173);
    }

    private Color createActionButtonColor() {
        return new Color(232, 177, 82);
    }

    private void addButton(JPanel panel, String text, int x, int y, int width, Color color,
            java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(color);
        button.setForeground(text.equals("=") ? new Color(13, 27, 42) : chooseTextColor(color));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(13, 27, 42), 2));
        button.setPreferredSize(new Dimension(90, 68));
        button.addActionListener(action);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.weightx = width;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(6, 6, 6, 6);
        panel.add(button, gbc);
    }

    private Color chooseTextColor(Color background) {
        int average = (background.getRed() + background.getGreen() + background.getBlue()) / 3;
        return average < 150 ? new Color(248, 249, 250) : new Color(13, 27, 42);
    }

    private void appendText(String value) {
        expression += value;
        refreshOperation();
    }

    private void clearAll() {
        expression = "";
        operationArea.setText("");
        resultLabel.setText("0");
    }

    private void deleteLast() {
        if (!expression.isEmpty()) {
            expression = expression.substring(0, expression.length() - 1);
            refreshOperation();
        }
    }

    private void toggleSign() {
        if (expression.isEmpty()) {
            expression = "-";
        } else {
            char lastChar = expression.charAt(expression.length() - 1);
            if (isOperator(lastChar) || lastChar == '(') {
                expression += "-";
            } else {
                int start = findCurrentTermStart();
                expression = expression.substring(0, start) + "(-" + expression.substring(start) + ")";
            }
        }
        refreshOperation();
    }

    private int findCurrentTermStart() {
        for (int i = expression.length() - 1; i >= 0; i--) {
            char ch = expression.charAt(i);
            if (isOperator(ch) || ch == '(' || ch == ')') {
                return i + 1;
            }
        }
        return 0;
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private void calculateResult() {
        if (expression.isBlank()) {
            resultLabel.setText("0");
            return;
        }

        try {
            ExpressionParser parser = new ExpressionParser(expression, degreesButton.isSelected());
            double result = parser.parse();
            resultLabel.setText(decimalFormat.format(result));
        } catch (IllegalArgumentException ex) {
            resultLabel.setText("Error");
        }
    }

    private void refreshOperation() {
        operationArea.setText(expression);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            CalculadoraApp app = new CalculadoraApp();
            app.setVisible(true);
        });
    }

    private static final class ExpressionParser {

        private final String input;
        private final boolean useDegrees;
        private int index;

        private ExpressionParser(String input, boolean useDegrees) {
            this.input = input.replace(" ", "");
            this.useDegrees = useDegrees;
        }

        private double parse() {
            double value = parseExpression();
            if (index < input.length()) {
                throw new IllegalArgumentException("Expresion invalida");
            }
            return value;
        }

        private double parseExpression() {
            double value = parseTerm();
            while (index < input.length()) {
                char operator = input.charAt(index);
                if (operator == '+') {
                    index++;
                    value += parseTerm();
                } else if (operator == '-') {
                    index++;
                    value -= parseTerm();
                } else {
                    break;
                }
            }
            return value;
        }

        private double parseTerm() {
            double value = parseFactor();
            while (index < input.length()) {
                char operator = input.charAt(index);
                if (operator == '*') {
                    index++;
                    value *= parseFactor();
                } else if (operator == '/') {
                    index++;
                    double divisor = parseFactor();
                    if (divisor == 0) {
                        throw new IllegalArgumentException("Division por cero");
                    }
                    value /= divisor;
                } else {
                    break;
                }
            }
            return value;
        }

        private double parseFactor() {
            if (index >= input.length()) {
                throw new IllegalArgumentException("Factor invalido");
            }

            char current = input.charAt(index);
            if (current == '+') {
                index++;
                return parseFactor();
            }
            if (current == '-') {
                index++;
                return -parseFactor();
            }

            if (current == '(') {
                index++;
                double value = parseExpression();
                require(')');
                return value;
            }

            if (Character.isLetter(current)) {
                String name = parseName();
                if ("pi".equals(name)) {
                    return Math.PI;
                }

                require('(');
                double argument = parseExpression();
                require(')');
                return applyFunction(name, argument);
            }

            return parseNumber();
        }

        private String parseName() {
            int start = index;
            while (index < input.length() && Character.isLetter(input.charAt(index))) {
                index++;
            }
            return input.substring(start, index).toLowerCase();
        }

        private double parseNumber() {
            int start = index;
            boolean hasDecimalPoint = false;

            while (index < input.length()) {
                char ch = input.charAt(index);
                if (Character.isDigit(ch)) {
                    index++;
                } else if (ch == '.' && !hasDecimalPoint) {
                    hasDecimalPoint = true;
                    index++;
                } else {
                    break;
                }
            }

            if (start == index) {
                throw new IllegalArgumentException("Numero invalido");
            }

            return Double.parseDouble(input.substring(start, index));
        }

        private void require(char expected) {
            if (index >= input.length() || input.charAt(index) != expected) {
                throw new IllegalArgumentException("Se esperaba " + expected);
            }
            index++;
        }

        private double applyFunction(String name, double argument) {
            double normalized = useDegrees ? Math.toRadians(argument) : argument;
            return switch (name) {
                case "sin" -> Math.sin(normalized);
                case "cos" -> Math.cos(normalized);
                case "tan" -> Math.tan(normalized);
                default -> throw new IllegalArgumentException("Funcion desconocida");
            };
        }
    }
}
