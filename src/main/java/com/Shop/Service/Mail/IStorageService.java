package com.Shop.Service.Mail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {

	String getFileUrl(String name);

	String save(MultipartFile file) throws IOException;

	String save(BufferedImage bufferedImage, String originalFileName) throws IOException;

	void delete(String name) throws IOException;

	String update(MultipartFile file, String pathName) throws IOException;

	default String getExtension(String originalFileName) {
		return StringUtils.getFilenameExtension(originalFileName);
	}

	default String generateFileName(String originalFileName) {
		return UUID.randomUUID().toString(); // + getExtension(originalFileName);
	}

	default byte[] getByteArrays(BufferedImage bufferedImage, String format) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {

			ImageIO.write(bufferedImage, format, baos);

			baos.flush();

			return baos.toByteArray();

		} catch (IOException e) {
			throw e;
		} finally {
			baos.close();
		}
	}

}