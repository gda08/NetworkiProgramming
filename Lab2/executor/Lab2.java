package executor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lab2 {

	/** Used to save web page links */
	private static List<String> links;

	public static void main(String[] args) throws IOException {

		links = new ArrayList<>();
		
		String website = "http://cs.lth.se/kurs/eda095-naetverksprogrammering/tentamen/";
		
		scanAndExtractLinks(website);
		
		String destFolder = "/h/d7/u/dh08hf1/Desktop/pdfs/";
		
		long time = System.currentTimeMillis();
				
		ExecutorService executor = Executors.newFixedThreadPool(links.size());
		
		for (String s : links) {
			if (s.contains("pdf")) {
				System.out.println("Downloading: " + s);
				Runnable runner = new Runner(website, s, destFolder);
				executor.execute(runner);
			}
		}
		
		System.out.println(">>>>>>>>>>>>>> " + (System.currentTimeMillis() - time));
		
		executor.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
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

}
