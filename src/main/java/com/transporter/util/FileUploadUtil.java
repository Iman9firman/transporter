package com.transporter.util;

import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

	public static String readExt(String fileName) {
		String extension = "";
		// Extract the extension from the file name
		int index = fileName.lastIndexOf('.');
		if (index > 0) {
			extension = fileName.substring(index+1);
		}
		return extension;
	}

		public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
 		if(!Files.exists(uploadPath)) {
 			Files.createDirectories(uploadPath);
 		}

//		 Files file = new Files();
 		try(InputStream inputStream = multipartFile.getInputStream()){
 			Path filePath = uploadPath.resolve(fileName);
 			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
 		}catch (IOException e) {
			throw new IOException("could not save File " +fileName, e);
		}
	}
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		try {
			Files.list(dirPath).forEach(file -> {
				if(!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						LOGGER.error("Could not delete file "+file);
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error("Directories "+dirPath + " is Empty");
		}
	}
	public static void removeDir(String dir) {
		Path dirPath = Paths.get(dir);

		cleanDir(dir);
		try {
			Files.delete(dirPath);
			LOGGER.info("Directories "+dirPath + " is Deleted");
		} catch (IOException e) {
			LOGGER.error("Could not remove directory: " + dir);
		}

	}

}
