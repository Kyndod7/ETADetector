package edu.ucf.etadetector.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static String INVALID_INPUT_MESSAGE = "Invalid input";
	private final MainActivity baseActivity = this;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedPrefEditor;
	//private EditText serverIpEditText;
	private EditText ssidEditText;
	private EditText macAddr1EditText;
	private EditText macAddr2EditText;
	private TextView outputTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize();

		Button testButton = (Button) findViewById(R.id.testButton);
		testButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				outputTextView.setText("");

				if (ssidEditText.getText().toString().length() == 0
						|| macAddr1EditText.getText().toString().length() < 17
						|| macAddr2EditText.getText().toString().length() < 17) {
					Toast.makeText(arg0.getContext(), INVALID_INPUT_MESSAGE, Toast.LENGTH_SHORT)
							.show();
					return;
				}

				ETADetector etaDetector = new ETADetector(baseActivity);
				etaDetector.start();
			}

		});
	}

	private void initialize() {
		sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		sharedPrefEditor = sharedPref.edit();
		//serverIpEditText = (EditText) findViewById(R.id.serverIpEditText);
		ssidEditText = (EditText) findViewById(R.id.SsidEditText);
		macAddr1EditText = (EditText) findViewById(R.id.mac1EditText);
		macAddr2EditText = (EditText) findViewById(R.id.mac2EditText);
		outputTextView = (TextView) findViewById(R.id.outputTextView);
		macAddr1EditText.addTextChangedListener(new MACAddrTextWatcher(macAddr1EditText));
		macAddr2EditText.addTextChangedListener(new MACAddrTextWatcher(macAddr2EditText));
	}

	protected void onPause() {
		super.onPause();
		//sharedPrefEditor.putString(getString(R.string.server_ip_key), getServerIp());
		sharedPrefEditor.putString(getString(R.string.ssid_key), getSsid());
		sharedPrefEditor.putString(getString(R.string.mac_addr_1_key), getMacAddr1());
		sharedPrefEditor.putString(getString(R.string.mac_addr_2_key), getMacAddr2());
		sharedPrefEditor.commit();
	}

	protected void onResume() {
		super.onResume();
		String serverIp = sharedPref.getString(getString(R.string.server_ip_key),
				getString(R.string.default_server_ip));
		String ssid = sharedPref.getString(getString(R.string.ssid_key),
				getString(R.string.default_ssid));
		String mac1 = sharedPref.getString(getString(R.string.mac_addr_1_key),
				getString(R.string.default_mac));
		String mac2 = sharedPref.getString(getString(R.string.mac_addr_2_key),
				getString(R.string.default_mac));
		//serverIpEditText.setText(serverIp);
		ssidEditText.setText(ssid);
		macAddr1EditText.setText(mac1);
		macAddr2EditText.setText(mac2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void outputResults(String outputLine) {
		outputTextView.append(outputLine + "\n");
	}

//	public String getServerIp() {
//		return serverIpEditText.getText().toString();
//	}

	public String getSsid() {
		return ssidEditText.getText().toString();
	}

	public String getMacAddr1() {
		return macAddr1EditText.getText().toString();
	}

	public String getMacAddr2() {
		return macAddr2EditText.getText().toString();
	}

}
