
/*@Solution.java*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Solution {

	public static class Global {
		public static long fileThreshold;
		public static long fileSize;
		public static String filePath;
	}

	public static void search(List<String> searchList) {
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean found = false;

				@Override
				public void startElement(String uri, String localName, String nodeName, Attributes attributes)
						throws SAXException {

					/*
					 * for(String s : searchList) { if (nodeName.equalsIgnoreCase(s)) { found =
					 * true; System.out.print("Node " + nodeName); break; } }
					 */

					if (searchList.contains(nodeName)) {
						found = true;
						System.out.print("Node " + nodeName);
					}
				}

				@Override
				public void endElement(String uri, String localName, String nodeName) throws SAXException {
					// System.out.println("End Element :" + nodeName);
				}

				@Override
				public void characters(char ch[], int start, int length) throws SAXException {

					if (found) {
						System.out.println(" Found with value " + new String(ch, start, length));
						found = false;
					}
				}
			};

			saxParser.parse(Global.filePath, handler);

		} catch (FileNotFoundException e) {
			System.out.print("File Not Found");

		} catch (Exception e) {
			System.out.print("error occured while parsing, Please check the xml file \n" + e.getLocalizedMessage());
		}

	}

	public static void checkLargeFile() {

		if (Global.fileSize > Global.fileThreshold) {
			System.out.println("WARNING! - This File considered as Large file");
			System.out.println("File Size: " + Global.fileSize);
			System.out.println("Threshold: " + Global.fileThreshold);
		}

	}

	public static void InitFileSize() {
		try {
			File file = new File(Global.filePath);
			Global.fileSize = file.length();
			//System.out.println(Global.fileSize + " bytes");
		} catch (Exception e) {
		}
	}

	public static List<String> readConfig() throws InvalidPropertiesFormatException, IOException {

		String configFilePath = "configuration.xml";
		InputStream inputStream = new FileInputStream(configFilePath);
		Properties props = new Properties();
		props.loadFromXML(inputStream);

		List<String> searchList = Arrays.asList(props.getProperty("searchNodes").split(","));

		if (props.getProperty("thresholdSize") != null) {
			Global.fileThreshold = Long.parseLong(props.getProperty("thresholdSize"));
			//System.out.println(Global.fileThreshold);
		}

		//System.out.println(searchList);

		return searchList;
	}

	public static void getFilePath(){
		System.out.println("[Enter the filename with .xml entention]" );
		
		System.out.print("Please Enter the file name : " );
		//System.out.println("EXAMPLE FILE NAME sample.xml" );
		Scanner in = new Scanner(System.in);
		String filePath = in.nextLine();
		Global.filePath = filePath;
	}
	
	
	public static void main(String argv[]) throws InvalidPropertiesFormatException, IOException{

		
		getFilePath();
		InitFileSize();
		List<String> searchList = readConfig();
		checkLargeFile();
		search(searchList);

	}

}