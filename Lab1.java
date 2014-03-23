import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lab1 {

	/** Used to save web page links */
	private static List<String> links;

	public static void main(String[] args) throws IOException {

		links = new ArrayList<>();
		
		scanAndExtractLinks("http://cs.lth.se/kurs/eda095-naetverksprogrammering/tentamen/");
		
		String destFolder = "C:\\Users\\Hani\\Desktop\\pdfs\\";	
		
		for (String s : links) {
			if (s.contains("pdf")) {
				System.out.println("Downloading: " + s);
				downloadPDF(new URL(s), destFolder);
			}
		}
		
	}
	
	/**
	 * Adds all links to the links list.
	 * Matches after the patter href="*"
	 * @param href
	 */
	private static void getHrefValue(String href) {
		Pattern p = Pattern.compile("href=\"(.*?)\"");
		Matcher m = p.matcher(href);
		String url = null;
		while (m.find()) {
		    url = m.group(1);
		    links.add(url);
		}
	}

	/**
	 * Given a page name extracts all the links found in the page.
	 * This function uses getHrefValue(String) to extract the value
	 * of a href link.
	 * @param pageName
	 */
	private static void scanAndExtractLinks(String pageName) {
		InputStream is = null;
		BufferedReader br = null;
		String href = "<a href=\"";
		try {
			URL page = new URL(pageName);
			try {
				is = page.openStream();
				br = new BufferedReader(new InputStreamReader(is));
				String line;
				while ((line = br.readLine()) != null) {
					if (line.contains(href)) {
						getHrefValue(line);
					}
				}
				br.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Downloads a given pdf file
	 * @param pdfUrl
	 * @param destFolder
	 */
	private static void downloadPDF(URL pdfUrl, String destFolder) {
		InputStream is = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		String fileName = pdfUrl.getFile();
		fileName = fileName.substring(fileName.lastIndexOf("/")+1, fileName.length());
		try {
			is = pdfUrl.openStream();
			bis = new BufferedInputStream(is);
			fos = new FileOutputStream(new File(destFolder + fileName));
			byte[] data = new byte[1024];
			int readBytes = 0;
			while ( (readBytes = bis.read(data)) > 0 ) {
				fos.write(data, 0, readBytes);
			}
			fos.close();
			bis.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
