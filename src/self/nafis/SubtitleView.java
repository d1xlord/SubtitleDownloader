package self.nafis;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Created by nafis on 3/3/15.
 */
public class SubtitleView implements Runnable {

    JFrame frame = new JFrame("Subtitle Downloader");
    JPanel panel = new JPanel();
    JLabel label = new JLabel();
    JPanel dPanel = new JPanel();

    SubtitleEnum status;

    public SubtitleView() {
        frame.setSize(new Dimension(400, 200));
        label.setText("Subtitle downloader initializing...");
        panel.add(label);

        dPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        dPanel.setSize(new Dimension(frame.getWidth(), 20));

        JLabel d1xlord = new JLabel("--- by d1xlord ---");
        d1xlord.setHorizontalAlignment(SwingConstants.RIGHT);
        dPanel.add(d1xlord);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(dPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void changeStatus(SubtitleEnum status) {
        this.status = status;
    }

    public void exitView(long sec) throws InterruptedException {
        Thread.sleep(sec);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    private void changeLabel(final String str) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                label.setText(str);
            }
        });
    }

    @Override
    public void run() {
        System.out.println("Thread started");
        SubtitleEnum prevStatus = status;
        while(true) {

            if(prevStatus == status)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            else {
                System.out.println("Status changed: " + status);
                prevStatus = status;
                if(status == SubtitleEnum.LOGIN_SUCCESSFUL)
                    changeLabel("Log in Successful");
                else if(status == SubtitleEnum.LOGIN_FAILED)
                    changeLabel("Log in FAILED :(");

                else if(status == SubtitleEnum.NO_SUBTITLE_FOUND)
                    changeLabel("No subtitle found!! :(");
                else if(status == SubtitleEnum.SUBTITLE_FOUND)
                    changeLabel("Subtitle found!! Downloading...");

                else if(status == SubtitleEnum.DOWNLOAD_SUCCESSFUL)
                    changeLabel("Subtitle Download Successful");
                else if(status == SubtitleEnum.DOWNLOAD_FAILED)
                    changeLabel("Subtitle Download Failed");

            }
        }
    }
}
