package org.example;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Main extends JFrame {
    private static final int BUTTONS_IN_COLUMN = 10;
    private static final int WIDTH = 120;
    private static final int HEIGHT = 30;
    private static final int SPACE = 10;
    private static final int MAX_NUMBER = 1000;
    private static final int MAX_VALUE_TO_REBUILD = 30;
    private static int WAIT_TIME = 500;
    private static final String SELECT_VALUE_SMALLER_30 =
            String.format("Please select a value smaller or equal to %d.", MAX_VALUE_TO_REBUILD);
    private static final String HOW_MANY_NUMBERS = "How many numbers to display?";
    private static final String CHECK_ENTERED_VALUE_MESSAGE =
            String.format("The entered number must be between 1 and  %d.", MAX_NUMBER);
    private static final String BUTTON_ENTER_TEXT = "Enter";
    private static final String BUTTON_SORT_TEXT = "Sort";
    private static final String BUTTON_SORT_RESET = "Reset";
    private static final Font FONT = new Font("Arial", Font.BOLD, 15);
    private static Thread sortThread;
    private final JPanel panelIntro = new JPanel();
    private final JPanel panelSort = new JPanel();
    private final JPanel panelButtons = new JPanel();
    private final JTextField textField = new JTextField();
    private final List<JButton> buttonList = new ArrayList<>();
    private final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    private int[] numbers;
    private boolean isAsc = false;

    private final ActionListener numberButtonListenerOnClick = e -> {
        if ((sortThread == null || !sortThread.isAlive()) && e.getSource() instanceof JButton jButton) {
            int number = Integer.parseInt(jButton.getText());

            if (number > MAX_VALUE_TO_REBUILD) {
                JOptionPane.showMessageDialog(this, SELECT_VALUE_SMALLER_30);
            } else {
                initSort();
                createNumbers(number);
                createNumberButtons();

                revalidate();
                repaint();
            }
        }
    };

    public static void main(String[] args) {
        try {
            if (args.length > 1) {
                System.out.println("Too many parameters");
                return;
            } else if (args.length == 1 && (WAIT_TIME = Integer.parseInt(args[0])) < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid parameter");
            return;
        }
        new Main().init();
    }

    private void init() {
        setupIntroPanel();
        setupSortPanel();

        setTitle("Quick sort visualization");
        setLayout(new CardLayout());
        add(panelIntro, "Intro");
        add(panelSort, "Sort");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void setupIntroPanel() {
        panelIntro.setLayout(null);

        JLabel label = new JLabel(HOW_MANY_NUMBERS);
        createElement(label,null, null, new Rectangle(dimension.width / 2 - WIDTH,
                dimension.height / 2 - 2 * (HEIGHT + SPACE), WIDTH * 2, HEIGHT), panelIntro);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panelIntro.add(label);

        textField.setBounds((dimension.width - WIDTH) / 2,dimension.height / 2 - HEIGHT - SPACE, WIDTH, HEIGHT);
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        panelIntro.add(textField);

        JButton enterButton = new JButton(BUTTON_ENTER_TEXT);
        createElement(enterButton, Color.BLUE, Color.WHITE, new Rectangle((dimension.width - WIDTH) / 2,
                dimension.height / 2, WIDTH, HEIGHT), panelIntro);
        enterButton.addActionListener(e -> handleButtonClick());
    }

    private void setupSortPanel() {
        int width = (int) ((1.0f - (WIDTH + SPACE * 8.0f) / dimension.width) * dimension.width);

        panelButtons.setLayout(null);
        JScrollPane scrollPane = new JScrollPane(panelButtons);
        scrollPane.setBounds(SPACE, SPACE, width,
                Math.min(dimension.height, (HEIGHT * 2 + SPACE) * (BUTTONS_IN_COLUMN)) + SPACE);

        panelSort.setLayout(null);
        panelSort.add(scrollPane);

        JButton sortButton = new JButton(BUTTON_SORT_TEXT);
        createElement(sortButton, Color.GREEN, Color.WHITE,
                new Rectangle(width + SPACE * 4, SPACE, WIDTH, HEIGHT), panelSort);
        sortButton.addActionListener(e -> {
            reset();
            sortThread = new Thread(() -> {
                quickSort(0, numbers.length - 1);
                isAsc = !isAsc;
            });
            sortThread.start();
        });

        JButton resetButton = new JButton(BUTTON_SORT_RESET);
        createElement(resetButton, Color.GREEN, Color.WHITE, new Rectangle(width + SPACE * 4, HEIGHT
                + 2 * SPACE, WIDTH, HEIGHT), panelSort);
        resetButton.addActionListener(e -> {
            if (sortThread != null) {
                sortThread.interrupt();
            }
            ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Intro");
        });
    }

    private void showError() {
        JOptionPane.showMessageDialog(this, CHECK_ENTERED_VALUE_MESSAGE);
        textField.requestFocus();
        textField.selectAll();
    }

    private void handleButtonClick() {
        try {
            int number = Integer.parseInt(textField.getText());
            if (number < 1 || number > MAX_NUMBER) {
                throw new NumberFormatException();
            }
            initSort();
            createNumbers(number);
            createNumberButtons();
            ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Sort");

            revalidate();
            repaint();
        } catch (NumberFormatException ex) {
            showError();
        }
    }

    private void createElement(JComponent component, Color background, Color foreground,
                                       Rectangle rectangle, JComponent parent) {
        component.setBackground(background);
        component.setForeground(foreground);
        component.setFont(FONT);
        component.setBounds(rectangle);
        parent.add(component);
    }

    private void createNumbers(int n) {
        numbers = new Random(n).ints(n, 1, MAX_NUMBER + 1).toArray();
        numbers[new Random().nextInt(n)] = new Random().nextInt(MAX_VALUE_TO_REBUILD) + 1;
    }

    private void createNumberButtons() {
        for (int i = 0; i < numbers.length; i++) {
            JButton numberButton = new JButton(String.valueOf(numbers[i]));

            int x = (WIDTH + SPACE) * (i / BUTTONS_IN_COLUMN) + SPACE;
            int y = (HEIGHT * 2 + SPACE) * (i % BUTTONS_IN_COLUMN) + SPACE;
            createElement(numberButton, Color.BLUE, Color.WHITE, new Rectangle(x, y, WIDTH, HEIGHT * 2), panelButtons);
            numberButton.addActionListener(numberButtonListenerOnClick);
            buttonList.add(numberButton);
        }
        panelButtons.setPreferredSize(new Dimension((WIDTH + SPACE) * (1 + numbers.length / BUTTONS_IN_COLUMN), 0));
    }

    private void reset() {
        buttonList.forEach(b -> b.setBackground(Color.BLUE));
    }

    private void initSort() {
        buttonList.clear();
        panelButtons.removeAll();
        isAsc = false;
    }

    private void changeButtonVisual(int index, Color background, Border border) {
        JButton button = buttonList.get(index);
        if (background != null) {
            button.setBackground(background);
        }
        button.setBorder(border);
    }

    private void visualChangeButton(int[] indexes, IterationType iterationType, Boolean color, boolean wait) {
        try {
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }

            for (int index : indexes) {
                switch (iterationType) {
                    case PIVOT_ON -> changeButtonVisual(index, null, new LineBorder(Color.RED, 4));
                    case PIVOT_OFF -> changeButtonVisual(index, null, null);
                    case LEFT_MOVE -> changeButtonVisual(index, Color.MAGENTA, null);
                    case RIGHT_MOVE -> changeButtonVisual(index, Color.PINK, null);
                    case SORTED -> changeButtonVisual(index, Color.GREEN, null);
                    case SWAP -> changeButtonVisual(index, Color.YELLOW, null);
                }
            }
            repaint();

            if (wait) {
                Thread.sleep(WAIT_TIME);
            }

            if (color) {
                for (int index : indexes) {
                    changeButtonVisual(index, Color.BLUE, null);
                }
            }
            repaint();
        } catch (InterruptedException e) {
            throw new RuntimeException("Sorting was interrupted...");
        }
    }

    private void changeButtons(int index1, int index2) {
        buttonList.get(index1).setText(String.valueOf(numbers[index1]));
        buttonList.get(index2).setText(String.valueOf(numbers[index2]));
    }

    private void quickSort(int low, int high) {
        if (low >= high) {
            visualChangeButton(new int[]{high}, IterationType.SORTED, false, false);
            return;
        }

        int i = low;
        int j = high;
        int indexPivot = (low + high) / 2;
        int pivot = numbers[indexPivot];

        visualChangeButton(new int[]{indexPivot}, IterationType.PIVOT_ON, false, true);

        while (i <= j) {
            while ((isAsc && numbers[i] < pivot) || (!isAsc && numbers[i] > pivot)) {
                visualChangeButton(new int[]{i++}, IterationType.LEFT_MOVE, true, true);
            }

            while ((isAsc && numbers[j] > pivot) || (!isAsc && numbers[j] < pivot)) {
                visualChangeButton(new int[]{j--},IterationType.RIGHT_MOVE, true, true);
            }

            if (i <= j) {
                int temp = numbers[i];
                numbers[i] = numbers[j];
                numbers[j] = temp;
                changeButtons(i, j);
                visualChangeButton(new int[]{i, j}, IterationType.SWAP, true, true);

                if (i == indexPivot || j == indexPivot) {
                    visualChangeButton(new int[]{indexPivot}, IterationType.PIVOT_OFF, false, false);
                    indexPivot = (i == indexPivot) ? j : i;
                    visualChangeButton(new int[]{indexPivot}, IterationType.PIVOT_ON, false, false);
                }
                i++;
                j--;
            }
        }

        visualChangeButton(new int[]{indexPivot}, IterationType.PIVOT_OFF, false, false);

        for (int k = j + 1; k < i; k++) {
            visualChangeButton(new int[]{k}, IterationType.SORTED, false, false);
        }

        if ((low < j)) {
            quickSort(low, j);
        } else {
            visualChangeButton(new int[]{low}, IterationType.SORTED, false, true);
        }

        if (i < high) {
            quickSort(i, high);
        } else {
            visualChangeButton(new int[]{high}, IterationType.SORTED, false, true);
        }
    }

    private enum IterationType {
        PIVOT_ON, PIVOT_OFF, LEFT_MOVE, RIGHT_MOVE, SWAP, SORTED
    }
}
