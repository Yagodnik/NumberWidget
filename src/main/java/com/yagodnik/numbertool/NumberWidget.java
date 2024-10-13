package com.yagodnik.numbertool;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumberWidget {
    final public JPanel root = new JPanel();

    final public int thresholdMargin = 20;

    final public JBPopup popup;
    final public Point point;

    final public JLabel decimalValueLabel = new JLabel();
    final public JLabel hexValueLabel = new JLabel();
    final public JLabel binaryValueLabel = new JLabel();

    NumberWidget(Point point, int margin) {
        this.point = point;

        Font defaultFont = UIManager.getFont("Label.font");
        Font myFont = defaultFont.deriveFont(Font.BOLD);

        JPanel decimalPanel = createPanel("Decimal: ", decimalValueLabel, myFont);
        JPanel hexPanel = createPanel("Hex: ", hexValueLabel, myFont);
        JPanel binaryPanel = createPanel("Binary: ", binaryValueLabel, myFont);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(decimalPanel);
        content.add(hexPanel);
        content.add(binaryPanel);

        EmptyBorder rootBorder = JBUI.Borders.empty(margin, margin, margin, margin);

        root.add(content);
        root.setBorder(rootBorder);

        popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(root, null)
                .setRequestFocus(true)
                .setMovable(true)
                .createPopup();
    }

    private JPanel createPanel(String title, JLabel valueLabel, Font font) {
        JPanel panel = new JPanel();

        JLabel titleLabel = new JLabel(title);
        JButton copyButton = new JButton("Copy");

        valueLabel.setFont(font);
        valueLabel.setBorder(JBUI.Borders.empty(0, 5));
        copyButton.setFont(font.deriveFont(Font.PLAIN, 11));
        copyButton.setPreferredSize(new Dimension(50, 30));

        panel.setLayout(new BorderLayout());
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.add(copyButton, BorderLayout.EAST);

        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection(valueLabel.getText());

                clipboard.setContents(stringSelection, null);
            }
        });

        return panel;
    }

    public void setValues(String decimal, String hex, String binary) {
        decimalValueLabel.setText(decimal);
        hexValueLabel.setText(hex);
        binaryValueLabel.setText(binary);
    }

    public boolean isInside(Point p) {
        if (popup == null) {
            return false;
        }

        Dimension size = popup.getSize();

        if (size == null) {
            return false;
        }

        Rectangle bounds = new Rectangle(
            point.x - thresholdMargin,
            point.y - thresholdMargin,
            size.width + 2 * thresholdMargin,
            size.height + 2 * thresholdMargin
        );

        return bounds.contains(p);
    }

    public void show(Editor editor) {
        if (popup == null) {
            return;
        }

        popup.show(new RelativePoint(editor.getContentComponent(), point));
    }

    public void cancel() {
        if (popup == null) {
            return;
        }

        popup.cancel();
    }
}
