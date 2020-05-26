package hjem.is.ui;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.List;

public class MyFrame extends JFrame {
    protected void close(){
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    protected JPanel makeMainPanel(){
        setSize(1000, 700);
        setContentPane(new JPanel());
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 1000, 700);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        getContentPane().add(panel);
        return panel;
    }
}
