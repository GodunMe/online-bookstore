package fa.training.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileHandle {
	
	private static final String coverUploadPath = "resources/books/";
	private static final String overviewUploadPath = "resources/overview/";
	

	public static String saveImage(MultipartFile image, String isbn) {
		try {
			String originalFilename = image.getOriginalFilename();
			String extension = "";

			if (originalFilename != null && originalFilename.contains(".")) {
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}

			// Create new filename
			String newFilename = isbn + extension;

			// Save image file
			Path savePath = Paths.get(coverUploadPath, newFilename);
			Files.createDirectories(savePath.getParent());
			Files.copy(image.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Saving to: " + savePath.toAbsolutePath());

			// Do something with 'name'
			System.out.println("Uploaded image with name: " + newFilename);
			return newFilename;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String saveOverview(String overview, String isbn) {

		String fileName = isbn + ".txt";
		String filePath = overviewUploadPath + fileName;
		try (FileWriter writer = new FileWriter(filePath, true)) {
			writer.write(overview + "\n");
			System.out.println("Content written successfully!");
			return fileName;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readOverview(String overviewPath) {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(overviewUploadPath + overviewPath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "<p><em>Overview not available.</em></p>";
		}
		return content.toString();
	}

	public static void deleteFile(String fileName, String type) {
		String uploadPath;
		switch (type) {
			case "cover":
				uploadPath = coverUploadPath;
				break;
			case "overview":
				uploadPath = overviewUploadPath;
				break;
			default:
				uploadPath = null;
				System.out.println("Unknown file type.");
				break;
		}
		
		try {
			boolean deleted = Files.deleteIfExists(Paths.get(uploadPath + fileName));
			if (deleted) {
				System.out.println("File deleted.");
			} else {
				System.out.println("File not found.");
			}
		} catch (IOException e) {
			System.err.println("Error deleting file: " + e.getMessage());
		}

	}

}
