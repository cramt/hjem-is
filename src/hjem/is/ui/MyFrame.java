package hjem.is.ui;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.List;

public class MyFrame extends JFrame {
    protected void order(List<JComponent> components) {
        int i = 0;
        for (JComponent component : components) {
            add(component);
            component.setBounds(0, i, 150, 30);
            i += 30;
        }
    }

    protected void close(){
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
