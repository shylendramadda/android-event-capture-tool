import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class AndroidEventApp {
    private static final String sourceLogFilePath = "/sdcard/Android/data/com.opentext.androidagent/files/";
    private static final String logFileName = "events_log.json";
    private static final String videoFileName = "screen_video.mp4";
    private static final String screenshotFileName = "screenshot1.png";
    private static final String destinationPath = "~/Downloads/";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AndroidEventApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Android Event Capture Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        JButton startCaptureButton = new JButton("Start Capture Events");
        JButton exportLogsButton = new JButton("Export Logs");
        JButton decryptLogsButton = new JButton("Decrypt Logs");
        JButton captureScreenshotButton = new JButton("Capture Screenshot");
        JButton exportScreenshotButton = new JButton("Export Screenshot");
        JButton startVideoButton = new JButton("Start Capture Video");
        JButton stopVideoButton = new JButton("Stop Capture Video");
        JButton exportVideoButton = new JButton("Export Video");

        startCaptureButton.addActionListener(
                e -> executeADBCommand("adb shell getevent -l > " + sourceLogFilePath + logFileName));
        exportLogsButton.addActionListener(
                e -> executeADBCommand(
                        "adb pull " + sourceLogFilePath + logFileName + " " + destinationPath + logFileName));
        decryptLogsButton.addActionListener(e -> DecryptionUtils.decryptFile(logFileName));
        captureScreenshotButton
                .addActionListener(e -> executeADBCommand("adb shell screencap -p /sdcard/" + screenshotFileName));
        exportScreenshotButton.addActionListener(
                e -> executeADBCommand("adb pull /sdcard/" + screenshotFileName + " " + destinationPath
                        + screenshotFileName));
        startVideoButton.addActionListener(
                e -> executeADBCommand("adb shell screenrecord --time-limit 10 /sdcard/" + videoFileName));
        stopVideoButton.addActionListener(e -> executeADBCommand("adb shell pkill -l2 screenrecord"));
        exportVideoButton.addActionListener(
                e -> executeADBCommand("adb pull /sdcard/" + videoFileName + " " + destinationPath + videoFileName));

        panel.add(startCaptureButton);
        panel.add(exportLogsButton);
        panel.add(decryptLogsButton);
        panel.add(captureScreenshotButton);
        panel.add(exportScreenshotButton);
        panel.add(startVideoButton);
        panel.add(stopVideoButton);
        panel.add(exportVideoButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void executeADBCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });
            process.waitFor();
            JOptionPane.showMessageDialog(null, "Command executed: " + command);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error executing command: " + e.getMessage());
        }
    }
}
