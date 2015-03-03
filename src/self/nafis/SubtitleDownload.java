package self.nafis;

import com.googlecode.opensubtitlesjapi.LANGUAGE;
import com.googlecode.opensubtitlesjapi.OpenSubtitlesAPI;
import com.googlecode.opensubtitlesjapi.OpenSubtitlesException;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;

/**
 * Created by nafis on 3/1/15.
 */
public class SubtitleDownload {

    OpenSubtitlesAPI subs;

    public SubtitleDownload() {
        System.out.println("Code is here");

        subs = new OpenSubtitlesAPI();

        System.out.println("This is printed");
        JFileChooser chooser = new JFileChooser();
        SubtitleView view = new SubtitleView();
        Thread thread = new Thread(view);
        thread.start();

        String tokenSearch = "";
        try {
            tokenSearch = subs.login("", "");
            System.out.println("Logged in successfully!!");
            view.changeStatus(SubtitleEnum.LOGIN_SUCCESSFUL);

            // Choosing file dialog
            int ret = chooser.showDialog(null, "Choose file");
            if(ret == JFileChooser.APPROVE_OPTION) {
                System.out.println("you have chosen: " + chooser.getSelectedFile().getName());
            }
            else {
                view.exitView(0);
                return;
            }
        } catch (OpenSubtitlesException e) {
            view.changeStatus(SubtitleEnum.LOGIN_FAILED);
            System.out.println("Error while login");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DefaultListModel listModel = new DefaultListModel();

        try {
            List<Map<String, Object>> searchRes = subs.search(tokenSearch, chooser.getSelectedFile(), LANGUAGE.ENG);
            System.out.println("Done searching!!! Found: " + searchRes.size());
            if(searchRes.size() == 0)
                view.changeStatus(SubtitleEnum.NO_SUBTITLE_FOUND);
            else
                view.changeStatus(SubtitleEnum.SUBTITLE_FOUND);

            List<Integer> temp = new ArrayList<Integer>();
            for(int i=0;i<searchRes.size();i++) {
                System.out.println("Object : " + searchRes.get(i).get("IDSubtitleFile"));
                temp.add(Integer.parseInt((String) searchRes.get(i).get("IDSubtitleFile")));

                for(String key: searchRes.get(i).keySet()) {
                    System.out.println(key + " // " + searchRes.get(i).get(key));
                    listModel.addElement(key);
                }
                System.out.println("//////");
                break; //Only selecting the first subtitle (TODO: Suggestion)
            }

            Map<Integer, byte[]> downloadSub = subs.download(tokenSearch, temp);
            byte[] gotSub = {};
            for(Integer key : downloadSub.keySet()) {
                System.out.println(key + "////" + downloadSub.get(key));
                gotSub = downloadSub.get(key);
                break; // Only selecting the first subtitle (FOR NOW, TODO: Suggestion)
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
            view.changeStatus(SubtitleEnum.DOWNLOAD_SUCCESSFUL);
            view.exitView(2000);
        } catch (Exception e) {
            view.changeStatus(SubtitleEnum.DOWNLOAD_FAILED);
            e.printStackTrace();
        }

        // TODO: File suggestion to be implemented here
        /*
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
        }); */
    }

}
