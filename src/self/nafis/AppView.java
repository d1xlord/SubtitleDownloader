package self.nafis;

import com.googlecode.opensubtitlesjapi.LANGUAGE;
import com.googlecode.opensubtitlesjapi.OpenSubtitlesAPI;
import com.googlecode.opensubtitlesjapi.OpenSubtitlesException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by nafis on 3/1/15.
 */
public class AppView {
    private JScrollPane filenamePanel;
    private JPanel jPanel;
    private JButton selectButton;
    private JList fileList;

    OpenSubtitlesAPI subs;

    public AppView() {
        System.out.println("Code is here");
        JFrame frame = new JFrame("AppView");
        jPanel = new JPanel();
        subs = new OpenSubtitlesAPI();

        System.out.println("This is printed");
        JFileChooser chooser = new JFileChooser();
        int ret = chooser.showDialog(null, "Choose file");
        if(ret == JFileChooser.APPROVE_OPTION) {
            System.out.println("you have chosen: " + chooser.getSelectedFile().getName());
        }

        String tokenSearch = "";
        try {
            tokenSearch = subs.login("", "");
        } catch (OpenSubtitlesException e) {
            System.out.println("Error while login");
        }
        System.out.println("Logged in successfully!!");

        DefaultListModel listModel = new DefaultListModel();

        try {
            List<Map<String, Object>> searchRes = subs.search(tokenSearch, chooser.getSelectedFile(), LANGUAGE.ENG);
            System.out.println("Done searching!!! Found: " + searchRes.size());
            List<Integer> temp = new ArrayList<Integer>();
            for(int i=0;i<searchRes.size();i++) {
                System.out.println("Object : " + searchRes.get(i).get("IDSubtitle"));
                //temp.add(Integer.parseInt((String) searchRes.get(i).get("IDSubtitle")));
                temp.add(Integer.parseInt((String) searchRes.get(i).get("IDSubtitleFile")));

                for(String key: searchRes.get(i).keySet()) {
                    System.out.println(key + " // " + searchRes.get(i).get(key));
                    listModel.addElement(key);
                }
                System.out.println("//////");
            }

            Map<Integer, byte[]> downloadSub = subs.download(tokenSearch, temp);
            byte[] gotSub = {};
            for(Integer key : downloadSub.keySet()) {
                System.out.println(key + "////" + downloadSub.get(key));
                gotSub = downloadSub.get(key);
            }

            String pathToWrite = chooser.getSelectedFile().getAbsolutePath();
            for(int i=pathToWrite.length()-1;i>=0;i--)
            {
                if(pathToWrite.charAt(i) == '.'){
                    pathToWrite = pathToWrite.substring(0, i) + ".srt";
                    break;
                }
            }
            System.out.println("File Path : " + pathToWrite);
            FileOutputStream fos = new FileOutputStream(pathToWrite);
            fos.write(gotSub);
            fos.close();
        } catch (Exception e) {
                e.printStackTrace();
        }

        fileList = new JList(listModel);

        filenamePanel = new JScrollPane(fileList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        filenamePanel.setPreferredSize(new Dimension(500, 400));
        jPanel.add(filenamePanel);
        //jPanel.setSize(jPanel.getPreferredSize());
        frame.setContentPane(jPanel);
        frame.add(selectButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

}
