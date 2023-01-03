package org.dsc.main;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import org.dsc.utilties.RunMetaProcess;
import org.dsc.utilties.folderProcessor;
import org.dsc.utilties.metaDataWriter;
import org.dsc.utilties.postGres;
import org.dsc.utilties.tikaRequest;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class tikaMeta extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtHost;
	private JTextField textPort;
	private JTextField txUserName;
	private JPasswordField textPassword;
	private JFileChooser chooser;
	private String choosertitle;
	private postGres pg;

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final String[] cmd = { "java", "-jar", "G:\\Tika\\tika-server-standard-2.6.0.jar" };
		final Process process = Runtime.getRuntime().exec(cmd);
		Runnable runnable = new Runnable() {
			public void run() {
				process.destroy();
			}
		};
		Runtime.getRuntime().addShutdownHook(new Thread(runnable));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception e) {
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					tikaMeta frame = new tikaMeta();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public tikaMeta() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 535);
		contentPane = new JPanel();
		JPanel folderSelector = new JPanel();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		folderSelector.setBounds(10, 11, 679, 116);
		contentPane.add(folderSelector);
		folderSelector.setLayout(null);

		JLabel lblInFolderText = new JLabel("Select Input Folder");
		lblInFolderText.setBounds(10, 0, 165, 14);
		folderSelector.add(lblInFolderText);

		JButton InFolderBtn = new JButton("Select Folder");

		InFolderBtn.setBounds(542, 28, 137, 23);
		folderSelector.add(InFolderBtn);

		JLabel lblInFolder = new JLabel("");
		lblInFolder.setBounds(0, 32, 334, 14);
		folderSelector.add(lblInFolder);

		JLabel lblOutFolderText = new JLabel("Select Output Folder");
		lblOutFolderText.setBounds(10, 57, 165, 23);
		folderSelector.add(lblOutFolderText);

		JLabel lblOutFolder = new JLabel("");
		lblOutFolder.setBounds(0, 97, 334, 14);
		folderSelector.add(lblOutFolder);

		JButton OutFolderBtn = new JButton("Select Folder");
		OutFolderBtn.setBounds(542, 93, 137, 23);
		folderSelector.add(OutFolderBtn);

		JPanel dbPanel = new JPanel();
		dbPanel.setBounds(10, 136, 679, 255);
		contentPane.add(dbPanel);
		dbPanel.setLayout(null);

		JLabel lbDBText = new JLabel("Database Connection");
		lbDBText.setBounds(10, 11, 128, 14);
		dbPanel.add(lbDBText);

		JLabel lblHost = new JLabel("Host");
		lblHost.setBounds(10, 47, 46, 14);
		dbPanel.add(lblHost);

		txtHost = new JTextField();
		txtHost.setText("localhost");
		txtHost.setBounds(498, 44, 171, 20);
		dbPanel.add(txtHost);
		txtHost.setColumns(10);

		textPort = new JTextField();
		textPort.setText("5342");
		textPort.setColumns(10);
		textPort.setBounds(498, 80, 171, 20);
		dbPanel.add(textPort);

		txUserName = new JTextField();
		txUserName.setText("postgres");
		txUserName.setColumns(10);
		txUserName.setBounds(498, 111, 171, 20);
		dbPanel.add(txUserName);

		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(10, 83, 46, 14);
		dbPanel.add(lblPort);

		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setBounds(10, 114, 94, 14);
		dbPanel.add(lblUserName);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 145, 94, 14);
		dbPanel.add(lblPassword);

		JButton ConnectionTestbtn = new JButton("Test Connection");

		ConnectionTestbtn.setBounds(542, 183, 137, 23);
		dbPanel.add(ConnectionTestbtn);

		textPassword = new JPasswordField();
		textPassword.setBounds(498, 142, 171, 20);
		dbPanel.add(textPassword);

		JLabel DBConnectionTxt = new JLabel("");
		DBConnectionTxt.setForeground(new Color(0, 128, 0));
		DBConnectionTxt.setFont(new Font("Tahoma", Font.BOLD, 13));
		DBConnectionTxt.setBounds(10, 170, 503, 59);
		dbPanel.add(DBConnectionTxt);

		JPanel runPanel = new JPanel();
		runPanel.setBounds(10, 402, 679, 83);
		contentPane.add(runPanel);
		runPanel.setLayout(null);

		JButton runBtn = new JButton("Run Process");
		runBtn.setBounds(542, 29, 137, 23);
		runPanel.add(runBtn);

		JLabel lblRun = new JLabel("");
		lblRun.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblRun.setBounds(10, 11, 511, 61);
		runPanel.add(lblRun);

		InFolderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser();
				chooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
				chooser.setDialogTitle(choosertitle);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(InFolderBtn) == JFileChooser.APPROVE_OPTION) {
					lblInFolder.setText(chooser.getSelectedFile().getAbsolutePath());
				} else {
					lblInFolder.setText("");
				}
			}
		});
		OutFolderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser();
				chooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
				chooser.setDialogTitle(choosertitle);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(InFolderBtn) == JFileChooser.APPROVE_OPTION) {
					lblOutFolder.setText(chooser.getSelectedFile().getAbsolutePath());
				} else {
					lblOutFolder.setText("");
				}
			}
		});

		ConnectionTestbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pg = new postGres();
				String passText = new String(textPassword.getPassword());
				pg.setUrl(txtHost.getText(), textPort.getText());
				pg.setPassword(passText);
				pg.setUser(txUserName.getText());
				try {
					pg.setCon();
					DBConnectionTxt.setText("Connection Suucessful");
					DBConnectionTxt.setForeground(new Color(0, 128, 0));
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					DBConnectionTxt.setText("<html>" + e1.getMessage() + "</html>");
					DBConnectionTxt.setForeground(new Color(0, 0, 0));
				}
			}
		});

		runBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblRun.setText("<html>Running Process</html>");
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (lblInFolder.getText().length() > 0 & lblOutFolder.getText().length() > 0) {

						} else {
							lblRun.setText("<html>" + "Not all parameters are filled in" + "</html>");
							return;
						}
						pg = new postGres();
						String passText = new String(textPassword.getPassword());
						pg.setUrl(txtHost.getText(), textPort.getText());
						pg.setPassword(passText);
						pg.setUser(txUserName.getText());
						try {
							pg.setCon();
							DBConnectionTxt.setText("Connection Suucessful");
							DBConnectionTxt.setForeground(new Color(0, 128, 0));
						} catch (SQLException e1) {
							DBConnectionTxt.setText("<html>" + e1.getMessage() + "</html>");
							DBConnectionTxt.setForeground(new Color(0, 0, 0));
						} finally {
							lblRun.setText("<html>" + "Failed as Database Connection is Incorrect" + "</html>");
						}
						RunMetaProcess rmp = new RunMetaProcess();
						try {
							rmp.setInFolder(lblInFolder.getText());
						} catch (IOException e1) {
							e1.printStackTrace();
							lblRun.setText("<html>" + "Failed as the Input Folder does not exist" + "</html>");
						}
						try {
							rmp.setOutFolder(lblOutFolder.getText());
						} catch (IOException e1) {
							e1.printStackTrace();
							lblRun.setText("<html>" + "Failed as the Output Folder does not exist" + "</html>");
							return;
						}
						try {
							rmp.run(pg);
						} catch (IOException | InterruptedException e1) {
							// TODO Auto-generated catch block
							lblRun.setText("<html>" + e1.getCause() + "</html>");
							return;
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							lblRun.setText("<html>" + e.getCause() + "</html>");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							lblRun.setText("<html>" + e.getCause() + "</html>");
						}
						lblRun.setText("<html>Process Completed</html>");
					}
				});

			}
		});

	}

}
