package edu.ucf.etadetector.app;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class MACAddrTextWatcher implements TextWatcher {

	private EditText editText;
	private int keyDel;
	private String text;
	
	public MACAddrTextWatcher(EditText editText) {
		this.editText = editText;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean flag = true;
        
        String eachBlock[] = editText.getText().toString().split(":");
        for (int i = 0; i < eachBlock.length; i++) {
            if (eachBlock[i].length() > 2) {
                flag = false;
            }
        }
        
        if (flag) {
            editText.setOnKeyListener(new OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_DEL)
                        keyDel = 1;
                    return false;
                }
            });

            if (keyDel == 0) {
                if (((editText.getText().length() + 1) % 3) == 0) {
                	// does not add a separator to the last block
                    if (editText.getText().toString().split(":").length <= 5) {
                        editText.setText(editText.getText() + ":");
                        editText.setSelection(editText.getText().length());
                    }
                }
                text = editText.getText().toString();
            } else {
                text = editText.getText().toString();
                keyDel = 0;
            }  
        } else {
            editText.setText(text);
        }
	}

}
