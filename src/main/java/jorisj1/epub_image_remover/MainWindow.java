package jorisj1.epub_image_remover;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class MainWindow {

	private JFrame frmEpubImageRemover;
	private JTextField txtFolder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmEpubImageRemover.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEpubImageRemover = new JFrame();
		frmEpubImageRemover.setTitle("Epub Image Remover");
		frmEpubImageRemover.setResizable(false);
		frmEpubImageRemover.setBounds(100, 100, 435, 458);
		frmEpubImageRemover.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEpubImageRemover.getContentPane().setLayout(null);
		
		JList<String> listFiles = new JList<String>();
		listFiles.setBounds(10, 46, 397, 330);
		frmEpubImageRemover.getContentPane().add(listFiles);
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.setBounds(320, 11, 87, 24);
		frmEpubImageRemover.getContentPane().add(btnBrowse);
		
		txtFolder = new JTextField();
		txtFolder.setBounds(10, 11, 300, 24);
		frmEpubImageRemover.getContentPane().add(txtFolder);
		txtFolder.setColumns(10);
		
		JButton btnRemoveImages = new JButton("Remove Images");
		btnRemoveImages.setBounds(267, 387, 140, 23);
		frmEpubImageRemover.getContentPane().add(btnRemoveImages);
		
		JCheckBox chckbxDeleteCoverImage = new JCheckBox("Delete cover images");
		chckbxDeleteCoverImage.setBounds(10, 387, 146, 23);
		frmEpubImageRemover.getContentPane().add(chckbxDeleteCoverImage);
	}
}
