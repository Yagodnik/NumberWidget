package com.yagodnik.numbertool;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.project.Project;

import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;

import javax.swing.*;
import java.awt.*;

public class IntegerTokenListener implements EditorMouseMotionListener, EditorMouseListener {
    private Timer hoverTimer;
    private NumberWidget popup;
    private final int HOVER_THRESHOLD = 500;

    @Override
    public void mouseMoved(EditorMouseEvent e) {
        Editor editor = e.getEditor();
        Project project = editor.getProject();
        Point position = e.getMouseEvent().getPoint();
        SelectionModel selectionModel = editor.getSelectionModel();

        if (selectionModel.hasSelection()) {
            return;
        }

        if (project == null) {
            return;
        }

        if (popup != null) {
            if (!popup.isInside(position)) {
                popup.cancel();
                popup = null;
            } else {
                return;
            }
        }

        PsiDocumentManager instance = PsiDocumentManager.getInstance(project);
        if (instance == null) {
            return;
        }

        PsiFile psiFile = instance.getPsiFile(editor.getDocument());
        LogicalPosition logicalPosition = e.getLogicalPosition();
        int offset = editor.logicalPositionToOffset(logicalPosition);

        if (psiFile != null) {
            PsiElement element = psiFile.findElementAt(offset);

            if (element != null) {
                IElementType tokenType = element.getNode().getElementType();

                if (tokenType == null) {
                    return;
                }

                boolean hoveringOverInteger = tokenType.toString().contains("INTEGER_LITERAL");

                if (hoveringOverInteger) {
                    if (hoverTimer != null && hoverTimer.isRunning()) {
                        hoverTimer.stop();
                    }

                    hoverTimer = new Timer(HOVER_THRESHOLD, evt -> {
                        try {
                            System.out.println(NumberConverter.fromAnyBase(element.getText()));
                        } catch (NumberFormatException exception) {
                            System.out.println(exception.getMessage());
                            return;
                        }

                        showPopup(e, editor, element.getText());
                    });
                    hoverTimer.setRepeats(false);
                    hoverTimer.start();
                } else if (hoverTimer != null) {
                    hoverTimer.stop();
                }
            }
        }
    }

    private void showPopup(EditorMouseEvent e, Editor editor, String text) {
        Point point = e.getMouseEvent().getPoint();

        popup = new NumberWidget(point, 7);
        popup.setValues(
            NumberConverter.toDecimal(text),
            NumberConverter.toHex(text),
            NumberConverter.toBinary(text)
        );

        popup.show(editor);
    }
}
