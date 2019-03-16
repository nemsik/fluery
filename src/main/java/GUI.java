import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;


import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

/**
 * Created by bartek on 07.03.2019.
 */
public class GUI extends JFrame {

    private Graph graph;
    private Map<String, ArrayList<String>> adjList;


    private FileSinkImages pic;


    public GUI() {
        super("Fluery");

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Plik");
        JMenuItem jMenuOpen = new JMenuItem("Otwórz");
        JMenuItem jMenuSave = new JMenuItem("Zapisz graf");
        JMenuItem jMenuSavePicture = new JMenuItem("Zapisz obraz");

        menuBar.add(menu);
        menu.add(jMenuOpen);
        menu.add(jMenuSave);
        menu.add(jMenuSavePicture);
        setJMenuBar(menuBar);


        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new DefaultGraph("Graph");
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);

        View view = viewer.addDefaultView(false);
        setSize(1000, 1000);


        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setSize(1000, 1000);
        panel.add((Component) view, BorderLayout.CENTER);
        add(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        viewer.enableAutoLayout();

        adjList = new HashMap<String, ArrayList<String>>();

        jMenuOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileFilter fileFilter = new FileNameExtensionFilter("Plik myGraph", "myGraph");
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle("Otwórz");
                int res = fileChooser.showOpenDialog(getParent());

                if (res == JFileChooser.APPROVE_OPTION) {
                    try
                    {
                        FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile());
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        adjList = (HashMap) ois.readObject();
                        ois.close();
                        fis.close();
                    }catch(IOException ioe)
                    {
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Nastąpił problem z otwarciem pliku.");
                        ioe.printStackTrace();
                        return;
                    } catch (ClassNotFoundException e1) {
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Nastąpił problem z otwarciem pliku.");
                        e1.printStackTrace();
                    }
                    try {
                        for (String key : adjList.keySet()) {
                            if (graph.getNode(key) == null) {
                                addNode(key);
                                Node n = graph.getNode(key);
                                n.addAttribute("ui.style", "shape:circle;fill-color: yellow;size: 90px; text-alignment: center;");
                                n.addAttribute("ui.label", key);
                            }
                            for (String value : adjList.get(key)) {
                                if (graph.getNode(value) == null) {
                                    addNode(value);
                                    Node n2 = graph.getNode(value);
                                    n2.addAttribute("ui.style", "shape:circle;fill-color: yellow;size: 90px; text-alignment: center;");
                                    n2.addAttribute("ui.label", key);
                                }
                                if (graph.getEdge(key+value) != null) continue;
                                graph.addEdge(key + value, key, value);
                                Edge e2 = graph.getEdge(key + value);
                                e2.addAttribute("ui.style", "text-alignment: center; text-size: 20px; fill-color: green;");
                                e2.addAttribute("ui.label", key + value);
                            }
                        }
                    }catch (Exception e1) {
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Nastąpił problem z wczytaniem grafu \nUpewnij się czy plik jest poprawny.");
                        System.err.println(e1.getMessage());
                    }
                }

            }
        });

        jMenuSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileFilter fileFilter = new FileNameExtensionFilter("Plik myGraph", "myGraph");
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle("Zapisz");
                System.out.println(fileChooser.getChoosableFileFilters());
                int res = fileChooser.showSaveDialog(getParent());

                if (res == JFileChooser.APPROVE_OPTION) {

                    try {
                        FileOutputStream fos =
                                new FileOutputStream(fileChooser.getSelectedFile() + ".myGraph");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(adjList);
                        oos.close();
                        fos.close();
                    } catch (Exception e3) {
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Nastąpił problem z zapisaniem grafu.");
                    }

                }
            }
        });

        jMenuSavePicture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileFilter fileFilter = new FileNameExtensionFilter("Plik JPG", "jpg");
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setDialogTitle("Zapisz obraz grafu");
                System.out.println(fileChooser.getChoosableFileFilters());
                int res = fileChooser.showSaveDialog(getParent());

                if (res == JFileChooser.APPROVE_OPTION) {
                    pic = new FileSinkImages(FileSinkImages.OutputType.PNG, FileSinkImages.Resolutions.VGA);
                    pic.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
                    try {
                        pic.writeAll(graph, fileChooser.getSelectedFile() + ".jpg");
                    } catch (IOException imageException) {
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Nastąpił problem z zapisaniem obrazu.");
                    }
                }
            }
        });


    }

    public void addEdge(String name, String s1, String s2) {
        graph.addEdge(name, s1, s2);
        Edge e = graph.getEdge(name);
        e.addAttribute("ui.style", "text-alignment: center; text-size: 20px; fill-color: green;");
        e.addAttribute("ui.label", name);

        if(adjList.get(s1) != null) {
            adjList.get(s1).add(s2);
        } else {
            adjList.put(s1, new ArrayList<String>());
            adjList.get(s1).add(s2);
        }
        System.out.print("");
    }

    public void addNode(String name) {
        graph.addNode(name);
        Node n = graph.getNode(name);
        n.addAttribute("ui.style", "shape:circle;fill-color: yellow;size: 90px; text-alignment: center;");
        n.addAttribute("ui.label", name);
    }

    public void clear() {
        graph.clear();
        adjList.clear();

    }


    public void fluery() {
        Fluery fluery = new Fluery(adjList);
        fluery.printEulerTour();
        fluery.getTextForGui();
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), fluery.getTextForGui());
    }
}
