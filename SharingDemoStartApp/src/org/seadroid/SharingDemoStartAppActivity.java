package org.seadroid;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Let the user select one of two actions.  Each is serviced by an Activity in a
 * separate app.
 * 
 * @author Pat Tressel
 */
public class SharingDemoStartAppActivity extends Activity {
	// We'll use a custom action for one activity, and a generic action for the other.
	public static final String CHOOSE_COLOR_ACTION = "org.seadroid.CHOOSE_COLOR";
	
	// Request codes distinguish activities that return results.
	// We hand the qctivity a code, and it returns the same with its results.
	public static final int COLOR_REQUEST = 1;
	public static final int EDIT_REQUEST = 2;
	
	/** 
	 * Check whether any app can service your request.
	 * This function is similar to one from the camera tutorial:
	 * http://developer.android.com/training/camera/photobasics.html
	 */
	public boolean isIntentAvailable(Intent intent) {
	    final PackageManager packageManager = getPackageManager();
	    List<ResolveInfo> list =
	        packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    /** Start the supplier app with data we supply */
    public void startSupplierClick(View view) {
    	// Which action is selected?
    	RadioButton colorButton = (RadioButton)findViewById(R.id.colorButton);
    	if (colorButton.isChecked()) {
    		// The user wants to pick a color.  It will return data in an Intent, not
    		// in an external resource, since the data is tiny -- just an RGB value.
		    final Intent intent = new Intent(CHOOSE_COLOR_ACTION);
		    if (isIntentAvailable(intent)) {
		    	// Yay!  Call it...
		    	startActivityForResult(intent, COLOR_REQUEST);
		    	// That doesn't return here -- this Activity is reentered via onCreate
		    	// when the other on ends.
		    } else {
		    	// Sorry, no app for that request...
    			TextView userText = (TextView)findViewById(R.id.userText);
    			userText.setText(getString(R.string.no_app));
		    }
    	} else {
    		// The user wants to enter some text.
        	// Provide a file for the other app to write into.  For want of a better
        	// place, put this in the the shared storage downloads directory.
    		// For a more unique name, append a timestamp.
    		// It would be better to check availability of shared storage up front and
    		// disable the option for entering text.
    		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    			String filename = getString(R.string.app_name);
    			File file = new File(
    				Environment.getExternalStoragePublicDirectory(
    					Environment.DIRECTORY_DOWNLOADS), filename);
    			File absFile = file.getAbsoluteFile();
    			Uri fileUri = Uri.fromFile(absFile);
    		    final Intent intent = new Intent(Intent.ACTION_GET_CONTENT, fileUri);
    		    if (isIntentAvailable(intent)) {
    		    	startActivityForResult(intent, EDIT_REQUEST);
    		    } else {
    		    	// Sorry, no app for that request...
        			TextView userText = (TextView)findViewById(R.id.userText);
        			userText.setText(getString(R.string.no_app));
    		    }
    		} else {
    			// Sorry, no SD card...
    			TextView userText = (TextView)findViewById(R.id.userText);
    			userText.setText(getString(R.string.no_sd_card));
    		}
    	}
    }
    
    /** Callback for Activitys that return results. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (resultCode != Activity.RESULT_CANCELED) {
    		// Get the action name since we're using that to name the result data as well.
    		String action = intent.getAction();
    		
    		// Check which request we're responding to
    		if (requestCode == COLOR_REQUEST) {
    			int c = intent.getIntExtra(action, 0);
    			// Do something with the color.
        		TextView userText = (TextView)findViewById(R.id.userText);
        		userText.setBackgroundColor(c);
    		} else if (requestCode == EDIT_REQUEST) {


    		} else {
    			// Shouldn't get here -- means we got called with a bad code.
    			TextView userText = (TextView)findViewById(R.id.userText);
    			userText.setText(getString(R.string.no_code));
    		}
    	} else {
    		// Canceled isn't really an error -- the user may just have closed the other
    		// activity.
    		TextView userText = (TextView)findViewById(R.id.userText);
			userText.setText(getString(R.string.canceled));
    	}
    }
}