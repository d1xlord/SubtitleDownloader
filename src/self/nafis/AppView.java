package self.nafis;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by nafis on 3/1/15.
 */
public class AppView {
    private JScrollPane filenamePanel;
    private JPanel jPanel;
    private JList fileList;

    public AppView() {
        System.out.println("Code is here");
        JFrame frame = new JFrame("AppView");
        jPanel = new JPanel();


        DefaultListModel listModel = new DefaultListModel();
        File folder = new File("/home/nafis");
        File[] listOfFiles = folder.listFiles();
        for(int i=0;i<listOfFiles.length;i++) {
            if(listOfFiles[i].isFile()) {
                listModel.addElement(listOfFiles[i].getName());
                System.out.println("Files : " + listOfFiles[i].getName());
            }
        }
        fileList = new JList(listModel);

        filenamePanel = new JScrollPane(fileList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        filenamePanel.setPreferredSize(new Dimension(500, 400));
        jPanel.add(filenamePanel);
        //jPanel.setSize(jPanel.getPreferredSize());
        frame.setContentPane(jPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
