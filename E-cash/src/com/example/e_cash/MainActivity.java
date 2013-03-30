package com.example.e_cash;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	public static Context maincontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		maincontext = getApplicationContext();
		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.sendCash)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.recvCash)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.sync)
				.setTabListener(this));
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, show the tab contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
				tab.getPosition() + 1);
		Log.e("ecash_change", String.valueOf(tab.getPosition() + 1));
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View curView = new View(getActivity());
			String cursec = String.valueOf(getArguments().getInt(
					ARG_SECTION_NUMBER));
			if (cursec.equals(Constants.SEND_ID)) {
				Log.e("Clicked", "Send Tab");
				curView = inflater.inflate(R.layout.activity_send, container,
						false);
				final EditText whoInput = (EditText) curView
						.findViewById(R.id.whoInput);
				final EditText whatInput = (EditText) curView
						.findViewById(R.id.whatInput);
				final EditText whyInput = (EditText) curView
						.findViewById(R.id.whyInput);
				final DatePicker datepicker = (DatePicker) curView
						.findViewById(R.id.datePicker1);

				final Button b = (Button) curView.findViewById(R.id.savebutton);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(
								maincontext);
						SQLiteDatabase db = mDbHelper.getWritableDatabase();
						ContentValues values = new ContentValues();
						values.put("T_WHO", whoInput.getText().toString());
						values.put("T_WHY", whyInput.getText().toString());
						values.put("T_WHAT", whatInput.getText().toString());
						values.put(
								"T_WHEN",
								String.valueOf(datepicker.getDayOfMonth())
										+ "/"
										+ String.valueOf(datepicker.getMonth())
										+ "/"
										+ String.valueOf(datepicker.getYear()));
						values.put("T_WHERE", "");
						long newRowId;
						newRowId = db.insert("Transactions", null, values);
						//Log.d("New Row", String.valueOf(newRowId));
						//Cursor c = db.rawQuery("Select * from Transactions",
						//		null);
						//c.moveToFirst();
						//Log.d("DB", c.getString(1));
						Log.e("Check output", whoInput.getText().toString());
						Log.e("what Check output", whatInput.getText()
								.toString());
						Log.e("why Check output", whyInput.getText().toString());
						Log.e("when Check output",
								String.valueOf(datepicker.getDayOfMonth()));
					}
				});
			} else if (cursec.equals(Constants.RECV_ID)) {
				Log.e("Clicked", "Receive Tab");
				curView = inflater.inflate(R.layout.activity_recv, container,
						false);
			} else if (cursec.equals(Constants.SYNC_ID)) {
				Log.e("Clicked", "Sync Tab");
				curView = inflater.inflate(R.layout.activity_sync, container,
						false);
			}

			return curView;
		}
	}

}
