package edu.ucf.etadetector.app;

import java.util.List;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WifiHandler {

	private final MainActivity baseActivity;

	public WifiHandler(MainActivity baseActivity) {
		this.baseActivity = baseActivity;
	}

	public void connectToOpenAp(String ssid, String bssid) {
		connectToAp(configOpenAp(ssid, bssid));
	}

	public void connectToWpaAp(String ssid, String bssid, String password) {
		connectToAp(configWpaAp(ssid, bssid, password));

	}

	private void connectToAp(WifiConfiguration config) {
		removeConfigs(config.SSID);
		WifiManager wifiManager = (WifiManager) baseActivity.getSystemService(Context.WIFI_SERVICE);
		int netId = wifiManager.addNetwork(config);
		wifiManager.disconnect();
		wifiManager.enableNetwork(netId, true);
		wifiManager.reconnect();
	}

	private WifiConfiguration configOpenAp(String ssid, String bssid) {
		WifiConfiguration config = new WifiConfiguration();
		config.SSID = "\"" + ssid + "\"";
		config.BSSID = bssid;
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		return config;
	}

	private WifiConfiguration configWpaAp(String ssid, String bssid, String password) {
		WifiConfiguration config = new WifiConfiguration();
		config.SSID = "\"" + ssid + "\"";
		config.BSSID = bssid;
		config.preSharedKey = "\"" + password + "\"";
		return config;
	}

	private void removeConfigs(String SSID) {
		WifiManager wifiManager = (WifiManager) baseActivity.getSystemService(Context.WIFI_SERVICE);
		List<WifiConfiguration> configNetworks = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration configNetwork : configNetworks) {
			if (configNetwork.SSID.equals(SSID)) {
				int netId = configNetwork.networkId;
				wifiManager.removeNetwork(netId);
			}
		}
	}

}
