package com.yagodnik.numbertool;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class PluginEditorFactoryListener implements EditorFactoryListener {
    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        Project project = editor.getProject();

        if (project != null) {
            IntegerTokenListener listener = new IntegerTokenListener();
            editor.addEditorMouseMotionListener(listener);
            editor.addEditorMouseListener(listener);
        }
    }
}
