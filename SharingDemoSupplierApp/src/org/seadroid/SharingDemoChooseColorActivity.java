package org.seadroid;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SharingDemoChooseColorActivity extends Activity {
	// A string for our result Intent.
	public static final String CHOOSE_COLOR_RESULT = "org.seadroid.CHOOSE_COLOR_RESULT";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    /** Send the user's selected color to the requesting app. */
    public void doneClick(View view) {
    	// Which color was selected?
    	RadioGroup g = (RadioGroup)findViewById(R.id.colorGroup);
    	int selected = g.getCheckedRadioButtonId();
    	RadioButton b = (RadioButton)findViewById(selected);
    	String colorName = (String) b.getTag();
    	// @ToDo: Look up color resource by name.
    	int color = 0;
    	
    	// The color info is tiny, so just send it back in the Intent.
    	// Note we could send the color value back in place of the result code, without
    	// any Intent.  The result code just needs to be a positive integer.
    	Intent result = new Intent(CHOOSE_COLOR_RESULT);
    	result.putExtra(CHOOSE_COLOR_RESULT, color);
    	
    	// Hand over the Intent.
    	setResult(Activity.RESULT_OK, result);
    	finish();
    }
}
