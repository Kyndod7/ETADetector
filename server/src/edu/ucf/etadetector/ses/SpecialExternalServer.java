package edu.ucf.etadetector.ses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SpecialExternalServer {

	private static final int port = 7777;
	private ServerSocket server;

	public SpecialExternalServer() {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleConnection() {
		System.out.println("Server is running...");
		while (true) {
			try {
				Socket socket = server.accept();
				new ConnectionHandler(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ConnectionHandler implements Runnable {

		private final Socket socket;

		public ConnectionHandler(Socket socket) {
			this.socket = socket;
			Thread t = new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			try {
				// reads client message
				ObjectInputStream ois = new ObjectInputStream(
						socket.getInputStream());
				String clientMessage = (String) ois.readObject();
				String clientSocket = socket.getRemoteSocketAddress()
						.toString().replace("/", "");
				System.out.println("Message Received: " + clientMessage);

				// sends server responses=
				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				oos.writeObject(clientSocket);
				System.out.println("Message Response: " + clientSocket);

				// closes connection
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
		SpecialExternalServer server = new SpecialExternalServer();
		server.handleConnection();
	}

}
