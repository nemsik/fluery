

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by bartek on 07.03.2019.
 */
public class GraphClick extends JFrame {

    GUI gui;

    public GraphClick() {
        super("Kontroler");
        gui = new GUI();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container allContainer = getContentPane();
        allContainer.setLayout(new BorderLayout());


        final JTextField source = new JTextField();
        final JTextField desc = new JTextField();

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());


        textPanel.add(source, BorderLayout.NORTH);
        textPanel.add(desc, BorderLayout.SOUTH);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton edgeButton = new JButton("Dodaj krawędź");
        final JButton flueryButton = new JButton("Fluery");
        JButton clearButton = new JButton("Wyczyść");

        buttonPanel.add(edgeButton);
        buttonPanel.add(flueryButton);
        buttonPanel.add(clearButton);

        allContainer.add(textPanel, BorderLayout.NORTH);
        allContainer.add(buttonPanel, BorderLayout.SOUTH);


        edgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s1 = source.getText();
                String s2 = desc.getText();
                if (!StringUtils.isBlank(s1) && !StringUtils.isBlank(s2) ) {
                    String name = s1 + s2;
                    try {
                        gui.addNode(s1);
                        System.out.println("s1: " + s1);
                    } catch (Exception e1) {
                        System.err.println("punkt " + s1 + " już istnieje");
                    }
                    try {
                        gui.addNode(s2);
                        System.out.println("s2: " + s2);

                    } catch (Exception e2) {
                        System.err.println("punkt " + s2 + " już istnieje");
                    }
                    try {

                        gui.addEdge(name, s1, s2);
                        System.out.println("name: " + name);

                    } catch (Exception e3) {
                        System.err.println("punkt " + name + " już istnieje");
                    }
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.clear();
            }
        });

        flueryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.fluery();
            }
        });

        pack();
        gui.setVisible(true);
    }

}
