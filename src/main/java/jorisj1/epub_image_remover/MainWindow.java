package jorisj1.epub_image_remover;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractListModel;

public class MainWindow {

	private JFrame frmEpubImageRemover;
	private JTextField txtFolder;
	private JList<String> listFiles;

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

		listFiles = new JList<String>();
		listFiles.setBounds(10, 46, 397, 330);
		frmEpubImageRemover.getContentPane().add(listFiles);

		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setMultiSelectionEnabled(true);

				int returnVal = chooser.showOpenDialog(frmEpubImageRemover);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					addFilesToList(chooser.getSelectedFiles());
				}
			}
		});
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

	protected void addFilesToList(File[] selectedFiles) {
		
	    DefaultListModel<String> listModel = new DefaultListModel<>();

		for (File file : selectedFiles) {
			try {
				Files.walk(file.toPath())
				  .filter(path -> !Files.isDirectory(path))
				  .filter(path -> getExtension(path.getFileName().toString()).equals("epub"))
//				  .forEach(path -> System.out.println(getExtension(path.getFileName().toString())));
				  .forEach(path -> listModel.addElement(path.getFileName().toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		listFiles.setModel(listModel);
	}
	
	public static String getExtension(String fileName) {
	    char ch;
	    int len;
	    if(fileName==null || 
	            (len = fileName.length())==0 || 
	            (ch = fileName.charAt(len-1))=='/' || ch=='\\' || //in the case of a directory
	             ch=='.' ) //in the case of . or ..
	        return "";
	    int dotInd = fileName.lastIndexOf('.'),
	        sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
	    if( dotInd<=sepInd )
	        return "";
	    else
	        return fileName.substring(dotInd+1).toLowerCase();
	}

	public static boolean hasElement(Object searched, JList list) {
		for (int a = 0; a < list.getModel().getSize(); a++) {
			Object element = list.getModel().getElementAt(a);
			if (element.equals(searched)) {
				return true;
			}
		}
		return false;
	}
}
