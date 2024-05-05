package com.demo.helpers;

import java.util.UUID;

public class FileHelper {

	public static String generateFileName(String fileName) {
		// abc.gif.png.jpg
		int lastIndex = fileName.lastIndexOf(".");
		String ext = fileName.substring(lastIndex);
		return UUID.randomUUID().toString().replace("-", "") + ext;
	}

}
