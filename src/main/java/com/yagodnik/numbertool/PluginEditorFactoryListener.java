package com.yagodnik.numbertool;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class PluginEditorFactoryListener implements EditorFactoryListener {
    IntegerTokenListener listener = new IntegerTokenListener();

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();

        if (editor == null) {
            System.out.println("Cant get editor!");
            return;
        }

        Project project = editor.getProject();

        if (project != null) {
            editor.addEditorMouseMotionListener(listener);
            editor.addEditorMouseListener(listener);
        }
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();

        if (editor == null) {
            System.out.println("Cant get editor!");
            return;
        }

        editor.removeEditorMouseListener(listener);
        editor.removeEditorMouseMotionListener(listener);
    }
}
