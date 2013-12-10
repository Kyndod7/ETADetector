package edu.ucf.etadetector.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ETADetector {

	private static final int SERVERPORT = 80;
	public final static String PASSWORD = "";
	public final static byte[] SERVER_IP = {(byte)132, (byte)170, (byte)217, (byte)3};
	private final MainActivity baseActivity;
	private int hackCounter;
	private BroadcastReceiver receiver;
	private State nextState;
	private String ap1Ip;
	private String ap2Ip;

	public enum State {
		AP1_CONNECTION, AP1_IP, AP2_CONNECTION, AP2_IP, COMPARE_IP;
	}

	public ETADetector(MainActivity baseActivity) {
		this.baseActivity = baseActivity;
		this.receiver = new ConnectivityChangedReceiver();
	}

	public void start() {
		nextState = State.AP1_CONNECTION;
		nextAction();
	}

	private void nextAction() {
		switch (nextState) {
		case AP1_CONNECTION:
			nextState = State.AP1_IP;
			sleep(1000);
			connectToAp1();
			break;
		case AP1_IP:
			nextState = State.AP2_CONNECTION;
			sleep(1000);
			getAp1Ip();
			break;
		case AP2_CONNECTION:
			nextState = State.AP2_IP;
			sleep(1000);
			connectToAp2();
			break;
		case AP2_IP:
			nextState = State.COMPARE_IP;
			sleep(1000);
			getAp2Ip();
			break;
		case COMPARE_IP:
			sleep(1000);
			compareIPs();
			break;
		}
	}

	private void connectToAp1() {
		hackCounter = 0;
		baseActivity.outputResults("Connecting to AP1...");
		IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		baseActivity.registerReceiver(receiver, intentFilter);
		WifiHandler wifiHandler = new WifiHandler(baseActivity);
		wifiHandler.connectToOpenAp(baseActivity.getSsid(), baseActivity.getMacAddr1());
		//wifiHandler.connectToWpaAp(baseActivity.getSsid(), baseActivity.getMacAddr1(), PASSWORD);
	}

	private void connectToAp2() {
		hackCounter = 0;
		baseActivity.outputResults("Connecting to AP2...");
		IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		baseActivity.registerReceiver(receiver, intentFilter);
		WifiHandler wifiHandler = new WifiHandler(baseActivity);
		//wifiHandler.connectToOpenAp(baseActivity.getSsid(), baseActivity.getMacAddr2());
		wifiHandler.connectToWpaAp(baseActivity.getSsid(), baseActivity.getMacAddr2(), PASSWORD);
	}

	private void getAp1Ip() {
		baseActivity.outputResults("Getting AP1 Gateway IP...");
		try {
			InetAddress host = InetAddress.getByAddress(SERVER_IP);
			Socket socket = new Socket(host.getHostName(), SERVERPORT);

			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject("What is my IP?");

			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String serverResponse = (String) ois.readObject();
			ap1Ip = serverResponse.substring(0, serverResponse.indexOf(":"));
			baseActivity.outputResults("\t" + serverResponse);

			oos.close();
			ois.close();
			nextAction(); 
		} catch (UnknownHostException e) {
			baseActivity.outputResults("\tFailed");
		} catch (IOException e) {
			baseActivity.outputResults("\tFailed");
		} catch (ClassNotFoundException e) {
			baseActivity.outputResults("\tFailed");
		}
	}

	private void getAp2Ip() {
		baseActivity.outputResults("Getting AP2 Gateway IP...");
		try {
			InetAddress host = InetAddress.getByAddress(SERVER_IP);
			Socket socket = new Socket(host.getHostName(), SERVERPORT);

			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject("What is my IP?");

			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String serverResponse = (String) ois.readObject();
			ap2Ip = serverResponse.substring(0, serverResponse.indexOf(":"));
			baseActivity.outputResults("\t" + serverResponse);

			oos.close();
			ois.close();
			nextAction();
		} catch (UnknownHostException e) {
			baseActivity.outputResults("\tFailed");
		} catch (IOException e) {
			baseActivity.outputResults("\tFailed");
		} catch (ClassNotFoundException e) {
			baseActivity.outputResults("\tFailed");
		}
	}

	private void compareIPs() {
		baseActivity.outputResults("Comparing IPs...");
		if (ap1Ip.equals(ap2Ip)) {
			baseActivity.outputResults("\tNo ETA attack detected");
		} else {
			baseActivity.outputResults("\tETA attack detected");
		}
	}

	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class ConnectivityChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) baseActivity
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				hackCounter++;
				if (hackCounter >= 2) {
					baseActivity.unregisterReceiver(receiver);
					baseActivity.outputResults("\tSuccessful");
					nextAction();
				}
			}
		}

	}

}
