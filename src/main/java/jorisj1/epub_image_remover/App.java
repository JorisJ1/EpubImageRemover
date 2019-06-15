package jorisj1.epub_image_remover;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class App 
{
	public static ArrayList<String> imageExtensions;
	
    public App()
    {

        imageExtensions = new ArrayList<String>();
        imageExtensions.add("bmp");
        imageExtensions.add("jpg");
        imageExtensions.add("jpeg");
        imageExtensions.add("gif");
        imageExtensions.add("png");
        
        // removeImagesFromZipFile("C:\\temp\\test - Copy.epub");
    }

	private static void removeImagesFromZipFile(String path) {
		try {
			ZipFile zipFile = new ZipFile(path);
			
			@SuppressWarnings("unchecked")
			List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
			
			for (int i = 0; i < fileHeaderList.size(); i++) {
				FileHeader fileHeader = fileHeaderList.get(i);
				String fileExtension = FilenameUtils.getExtension(fileHeader.getFileName());

				if (imageExtensions.contains(fileExtension.toLowerCase())) {
					zipFile.removeFile(fileHeader);
				}
			}
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
}
