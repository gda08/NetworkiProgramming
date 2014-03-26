package executor;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class Runner implements Runnable {
	
	private String website;
	private String pdfFile;
	private String destFolder;
	
	public Runner(String website, String pdfFile, String destFolder) {
		this.website = website;
		this.pdfFile = pdfFile;
		this.destFolder = destFolder;
	}
	
	/**
	 * Downloads a given pdf file
	 * @param pdfUrl
	 * @param destFolder
	 */
	@Override
	public void run() {
		System.out.println("Downloading thread started");
		InputStream is = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		URL pdfUrl = null;
		try {
			pdfUrl = new URL(new URL(website), pdfFile);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
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
