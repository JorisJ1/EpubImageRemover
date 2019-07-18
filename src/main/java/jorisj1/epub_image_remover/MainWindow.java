package jorisj1.epub_image_remover;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class MainWindow {

	private JFrame frmEpubImageRemover;
	private JList<String> listFiles;

	private ArrayList<Path> filePaths;
	private ArrayList<String> imageExtensions;

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

		JButton btnBrowse = new JButton("Select .epub files...");
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
		btnBrowse.setBounds(267, 11, 140, 24);
		frmEpubImageRemover.getContentPane().add(btnBrowse);

		JButton btnRemoveImages = new JButton("Remove Images");
		btnRemoveImages.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
			
				for (Path path : filePaths) {
					removeImagesFromZipFile(path.toString());
				}
				
				JOptionPane.showMessageDialog(frmEpubImageRemover, "Done.");
			}
		});
		btnRemoveImages.setBounds(267, 387, 140, 23);
		frmEpubImageRemover.getContentPane().add(btnRemoveImages);
	}

	private void removeImagesFromZipFile(String path) {

		if (imageExtensions == null) {
			imageExtensions = new ArrayList<String>();
			imageExtensions.add("bmp");
			imageExtensions.add("jpg");
			imageExtensions.add("jpeg");
			imageExtensions.add("gif");
			imageExtensions.add("png");
		}

		try {
			ZipFile zipFile = new ZipFile(path);

			@SuppressWarnings("unchecked")
			List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
			
			// Gather a list of fileheaders that will be deleted.
			ArrayList<FileHeader> filesToDelete = new ArrayList<FileHeader>();
			for (int i = 0; i < fileHeaderList.size(); i++) {
				FileHeader fileHeader = fileHeaderList.get(i);
				String fileExtension = FilenameUtils.getExtension(fileHeader.getFileName());

				if (imageExtensions.contains(fileExtension.toLowerCase())) {
					filesToDelete.add(fileHeader);
				}
			}
			
			// Delete every file in the list.
			for (FileHeader fileHeader : filesToDelete) {
				zipFile.removeFile(fileHeader);
			}
			
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	protected void addFilesToList(File[] selectedFiles) {

		// Find all epubs in the selection.
		filePaths = new ArrayList<Path>();
		for (File file : selectedFiles) {
			ArrayList<Path> paths = findEpubsInDirectory(file);
			filePaths.addAll(paths);
		}

		// Display filenames in the list.
		DefaultListModel<String> listModel = new DefaultListModel<>();
		for (Path path : filePaths) {
			listModel.addElement(path.getFileName().toString());
		}
		listFiles.setModel(listModel);
	}

	private ArrayList<Path> findEpubsInDirectory(File file) {
		ArrayList<Path> paths = new ArrayList<Path>();

		try {
			Files.walk(file.toPath()).filter(path -> !Files.isDirectory(path))
					.filter(path -> getExtension(path.getFileName().toString()).equals("epub"))
					.forEach(path -> paths.add(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return paths;
	}

	public static String getExtension(String fileName) {
		char ch;
		int len;
		if (fileName == null || (len = fileName.length()) == 0 || (ch = fileName.charAt(len - 1)) == '/' || ch == '\\'
				|| // in the case of a directory
				ch == '.') // in the case of . or ..
			return "";
		int dotInd = fileName.lastIndexOf('.'),
				sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if (dotInd <= sepInd)
			return "";
		else
			return fileName.substring(dotInd + 1).toLowerCase();
	}

	public static boolean hasElement(Object searched, JList<String> list) {
		for (int a = 0; a < list.getModel().getSize(); a++) {
			Object element = list.getModel().getElementAt(a);
			if (element.equals(searched)) {
				return true;
			}
		}
		return false;
	}
}
