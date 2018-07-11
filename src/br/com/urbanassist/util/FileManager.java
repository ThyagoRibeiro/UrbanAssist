package br.com.urbanassist.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

	public static String readString(String filePath) {

		try {
			BufferedReader buffRead = new BufferedReader(new FileReader(filePath));

			String line = "";
			String content = "";
			while (true) {
				if (line != null) {
					content += line;

				} else
					break;
				line = buffRead.readLine();
			}
			buffRead.close();
			return content;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void writeString(String filePath, String content, boolean append) {
		try {
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(filePath, append));
			buffWrite.append(content);
			buffWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deleteString(String lineToRemove, String filePath) {

		try {
			File inputFile = new File(filePath);
			StringBuilder content = new StringBuilder();

			BufferedReader reader;
			reader = new BufferedReader(new FileReader(inputFile));

			// TODO Auto-generated catch block

			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				// trim newline when comparing with lineToRemove
				String trimmedLine = currentLine.trim();
				if (!trimmedLine.equals(lineToRemove)) {
					content.append(currentLine);
					content.append(System.getProperty("line.separator"));
				}
			}
			reader.close();
			writeString(filePath, content.toString(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}