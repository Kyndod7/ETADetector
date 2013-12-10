package edu.ucf.etadetector.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLConnectionManager {

	private static final String URL = "www.google.com";
	private final MainActivity baseActivity;
	private final SSLSocketFactory sslSocketFactory;
	private SSLSocket sslSocket;

	public SSLConnectionManager(MainActivity baseActivity) {
		this.baseActivity = baseActivity;
		sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
	}

	public void executeHandshake() throws UnknownHostException, IOException {
		sslSocket = (SSLSocket) sslSocketFactory.createSocket(URL, 443);
		sslSocket.startHandshake();
	}

	public void downloadFiles() throws IOException {
		PrintWriter pw = new PrintWriter(sslSocket.getOutputStream());
		pw.println("GET / HTTP/1.1");
		pw.println("Host:www.google.com");
		pw.println("User Agent: Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		pw.println("Connection: Close");
		pw.println("");
		pw.flush();
	}

	public void printFiles() {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(sslSocket.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				baseActivity.outputResults(line);
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Unable to print files");
		}
	}

}
