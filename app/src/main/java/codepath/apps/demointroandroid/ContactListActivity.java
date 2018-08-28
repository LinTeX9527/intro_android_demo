package codepath.apps.demointroandroid;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ContactListActivity extends Activity {
	
	ArrayList<String> names = new ArrayList<String>();

	private static final String TAG = ContactListActivity.class.getName();


	// 动态申请读取联系人的权限
	private static final int REQUEST_PERMISSION_CODE = 0x1234;
	// 需要申请的权限
	private String[] REQUEST_PERMISSION_LISTS = new String[]{
			Manifest.permission.READ_CONTACTS,
	};

	List<String> missingPermissions = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 从这里开始申请权限
		checkAndRequestPermissions();

		setContentView(R.layout.activity_contact_list);
		loadContacts();
		populateListView();
	}

	private void checkAndRequestPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			for (String eachPermission: REQUEST_PERMISSION_LISTS) {
				if (checkSelfPermission(eachPermission) == PackageManager.PERMISSION_GRANTED) {
					Log.d(TAG, "权限 < " + eachPermission + " > 已满足");
				} else {
					missingPermissions.add(eachPermission);
				}
			}

			if (!missingPermissions.isEmpty()) {
				requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]), REQUEST_PERMISSION_CODE);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_CODE) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				for (int index = grantResults.length-1; index >= 0; index--) {
					if (checkSelfPermission(permissions[index]) == PackageManager.PERMISSION_GRANTED) {
						missingPermissions.remove(permissions[index]);
					}
				}

				if (missingPermissions.isEmpty()) {
					Log.d(TAG, "所有的权限都满足");
				} else {
					Log.d(TAG, "如下的权限未满足：\n" + missingPermissions);
				}
			}
		}
	}

	private void populateListView() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
		  android.R.layout.simple_list_item_1, names);

		ListView listView = (ListView) findViewById(R.id.lvContacts);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(ContactListActivity.this, names.get(position), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@SuppressLint("NewApi")
	private void loadContacts() {
		// 联系人的URI
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[]{
				ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME
		};
		Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				names.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_contact_list, menu);
		return true;
	}

}
