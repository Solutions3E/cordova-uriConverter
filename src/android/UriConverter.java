
/**
 * Cordova Android Plugin for Uri Conversion.
 */

package com.eeesolutions.plugin;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Photo;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.widget.Toast;

public class UriConverter extends CordovaPlugin {
	private String folder = "MyFileStorage";
	File myInternalFile;
	String filePath;

	@Override
	public boolean execute(String action, JSONArray data,
			CallbackContext callbackContext) throws JSONException {
		Context context = this.cordova.getActivity().getApplicationContext();
		if (action.equals("getcontenturi")) {

			String filepath = data.getString(0);

			Uri contentUri = getContentUri(context, new File(filepath));
			callbackContext.success(contentUri + "");

			return true;
		}

		if (action.equals("getfilepath")) {
			String contentUri = data.getString(0);

			if (contentUri.contains("contacts")) {

				filePath = getFilePathFromContactUri(context, contentUri);

			} else
				filePath = getRealPathFromURI(context, Uri.parse(contentUri));

			callbackContext.success(filePath);

			return true;
		}

		return false;
	}

	public static Bitmap getContactBitmapFromURI(Context context, Uri uri) {
		InputStream input = null;
		try {
			input = context.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (input == null) {
			return null;
		}
		return BitmapFactory.decodeStream(input);
	}

	public String getFilePathFromContactUri(Context context, String contentUri) {
		ContextWrapper contextWrapper = new ContextWrapper(
				context.getApplicationContext());
		File directory = contextWrapper.getDir(folder, Context.MODE_PRIVATE);
		myInternalFile = new File(directory, "app_" + contentUri.split("/")[4]
				+ ".png");

		try {
			FileOutputStream fos = new FileOutputStream(myInternalFile);
			Bitmap bitmap = getContactBitmapFromURI(context,
					Uri.parse(contentUri));
			bitmap.compress(Bitmap.CompressFormat.PNG, 85, fos);
			fos.flush();
			fos.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

		return myInternalFile.getAbsolutePath();
	}

	public static String getRealPathFromURI(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };

		// This method was deprecated in API level 11
		// Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		try {
			CursorLoader cursorLoader = new CursorLoader(context, contentUri,
					proj, null, null, null);
			Cursor cursor = cursorLoader.loadInBackground();

			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return null;
		}
	}

	public static Uri getContentUri(Context context, File file) {

		try {
			String filePath = file.getAbsolutePath();
			Cursor cursor = context.getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new String[] { MediaStore.Images.Media._ID },
					MediaStore.Images.Media.DATA + "=? ",
					new String[] { filePath }, null);

			if (cursor != null && cursor.moveToFirst()) {
				int id = cursor.getInt(cursor
						.getColumnIndex(MediaStore.MediaColumns._ID));
				Uri baseUri = Uri
						.parse("content://media/external/images/media");
				return Uri.withAppendedPath(baseUri, "" + id);
			} else {
				if (file.exists()) {
					ContentValues values = new ContentValues();
					values.put(MediaStore.Images.Media.DATA, filePath);
					return context.getContentResolver().insert(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							values);
				} else {
					return null;
				}
			}

		} catch (Exception e) {
			return null;
		}

	}

	
}
