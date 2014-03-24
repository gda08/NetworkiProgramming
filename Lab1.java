

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

/**
 * nslookup www.lth.se
 * Server: 130.235.63.228
 * Address:130.235.63.228#53
 * 130.235.209.220
 * 
 * nslookup 130.235.35.100
 * login.student.lth.se
 * 
 * 
 * traceroute www.colorado.edu
 * 
 *1     2 ms     9 ms     9 ms  d001b-gw-190.student.lth.se [130.235.34.2]  <--------- 1 LUND
  2     6 ms     9 ms     9 ms  c001--d001b.net.lu.se [130.235.217.45]
  3     9 ms     9 ms     9 ms  x001--c001.net.lu.se [130.235.217.10]
  
  4    <1 ms    <1 ms    <1 ms  lu-br1.sunet.se [193.11.20.9]   <--------- 2 SUNET
  5     9 ms     9 ms     9 ms  m1fre-xe-5-2-0.sunet.se [130.242.85.1]
  6     9 ms     9 ms     9 ms  t1fre-ae5-v1.sunet.se [130.242.83.46]
  
  7     9 ms     9 ms     9 ms  se-fre.nordu.net [109.105.102.9] <--------- 3 NORDU
  8   115 ms   115 ms   115 ms  us-man.nordu.net [109.105.97.69]
  
  9   148 ms   152 ms   155 ms  xe-2-3-0.118.rtr.newy32aoa.net.internet2.edu [109.105.98.10] <--------- 4 INTERNET2
 10   152 ms   148 ms   152 ms  et-5-0-0.102.rtr.clev.net.internet2.edu [198.7145.2]
 11   152 ms   155 ms   153 ms  et-10-0-0.107.rtr.chic.net.internet2.edu [198.71.45.8]
 
 12   180 ms   188 ms   187 ms  ae0.3454.core-l3.frgp.net [192.43.217.223]   <--------- 5 FRGP
 
 13   189 ms   180 ms   189 ms  ucb-re-frgpl3.colorado.edu [198.59.55.1]     <--------- 6 COLORADO
 14   193 ms   199 ms   204 ms  ucbcomp-ucbtcom.colorado.edu [128.138.81.253]
 15   198 ms   191 ms   189 ms  fw-juniper.colorado.edu [128.138.81.194]
 16   198 ms   203 ms   196 ms  hut-fw.colorado.edu [128.138.81.249]
 17   190 ms   195 ms   194 ms  comp-hut.colorado.edu [128.138.81.11]
 18   196 ms   186 ms   189 ms  www.colorado.edu [128.138.129.98]
 *
 *
 * Visual traceroute:
 * http://www.yougetsignal.com/tools/visual-tracert/
 * 
 * 
 * netstat -i
 * Kernel Interface table
 * Iface   MTU Met   RX-OK RX-ERR RX-DRP RX-OVR    TX-OK TX-ERR TX-DRP TX-OVR Flg
 * eth0       1500 0  41472688      0      0 0      26400823      0      0      0 BMRU
 * lo        16436 0     54709      0      0 0         54709      0      0      0 LRU
 * 
 * 
 * Where do all your packets go when they are not destined for the local network?
 * Gateway
 * 
 * What is the localhost address? 
 * 127.0.0.1
 * 
 * 
 * What are the transport protocols on your machine? List some services, their IP address, and their TCP/UDP ports.
 * netstat -a
 * tcp        0      0 panter-7.student.:55096 arn02s06-in-f18.1:https ESTABLISHED
 * tcp        0      0 panter-7.student.lt:692 tiger.student.lth.s:nfs ESTABLISHED
 * tcp        0      0 panter-7.student.:60683 david.ddg.lth.se:ldap   ESTABLISHED
 * tcp        0      0 panter-7.student.:39372 arn02s06-in-f20.1:https ESTABLISHED
 * tcp        0      0 panter-7.student.lt:820 puma.student.lth.se:nfs ESTABLISHED
 * tcp        0      0 panter-7.student.:38774 david.ddg.lth.se:ldap   ESTABLISHED
 * tcp        0      0 panter-7.student.:40651 arn02s06-in-f23.1:https ESTABLISHED
 * tcp        0      0 panter-7.student.:55643 lb-in-f189.1e100.:https ESTABLISHED
 * 
 * 
 * 
 * 
 * 
 * 
 *
 */

public class Lab1 {

	/** Used to save web page links */
	private static List<String> links;

	public static void main(String[] args) throws IOException {

		links = new ArrayList<>();
		
		scanAndExtractLinks("http://cs.lth.se/kurs/eda095-naetverksprogrammering/tentamen/");
		
		String destFolder = "\\\\tiger\\dh08hf1\\Windows\\Desktop\\pdfs\\";	
		
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
