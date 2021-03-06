package valkyrie.file;

/**
 * 
 * COPYRIGHT: Paul Neuhold, Laurenz Theuerkauf, Alexander Ritz, Jakob Schweighofer, Milo Tischler
 * � Milo Tischler, Jakob Schweighofer, Alexander Ritz, Paul Neuhold, Laurenz Theuerkauf 
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author madhatter Filemanager to save, load and delete Files from SD Card
 */

public class FileManager {
	final static String TAG = "FileManager";
	final static String IMGNAME = "IMG";
	final static String IMGCOUNT = "0000";
	private boolean SDMOUNTED = false; // check if SD Card is mounted
	private Context context;
	final static String SDPATH = Environment.getExternalStorageDirectory().toString() + "/Valkyrie/Gallery/";
	final static String THUMBPATH = Environment.getExternalStorageDirectory().toString() + "/Valkyrie/Thumbnls/";

//	public FileManager() {
//		initFileManager();
//	}

	public FileManager(Context context) {
		this.context = context;
		try {
			initFileManager();
		} catch (Exception e) {
			Log.e(TAG, "ERROR, couldnt initialize Filemanager");
		}

	}

	/**
	 * init method checks if the File /Valkyrie/Gallery/ and /Valkyrie/Thumbnls/ is already created. if not it will be
	 * created
	 * 
	 * @return
	 */
	private boolean initFileManager() {
		String state = Environment.getExternalStorageState();
		File file = new File(SDPATH);
		File thumb = new File(THUMBPATH);
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			SDMOUNTED = true;
			if (!file.exists())
				file.mkdirs();
			if (!thumb.exists())
				thumb.mkdirs();
			return true;

		} else {
			SDMOUNTED = false;
			Toast.makeText(context, "WARNING: SD Card not mounted!", Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Error SD-Card not mounted");
			return false;
		}
	}

	/**
	 * save an Image as PNG on SD Card at: .../Valkyrie/Gallery/
	 * 
	 * @param bitmap
	 */
	public void saveImageToGallery(Bitmap bitmap) {
		if (SDMOUNTED) {
			OutputStream fOut = null;
			File directory = new File(SDPATH);
			File files[] = directory.listFiles();
			File file;

			if (files.length > 0) {
				int highestnumber = 0;
				for (File f : files) {
					if (f.exists()) {
						int newhighest = Integer.parseInt(getLatestImage().substring(3, getLatestImage().length() - 4));
						if (newhighest >= highestnumber) {
							highestnumber = newhighest + 1;
						}
					}
				}
				String filenumber = IMGCOUNT + highestnumber;
				filenumber = filenumber.substring(filenumber.length() - 4);

				file = new File(SDPATH, IMGNAME + filenumber + ".png");
			} else {
				String filenumber = IMGCOUNT + 1;
				filenumber = filenumber.substring(filenumber.length() - 4);
				file = new File(SDPATH, IMGNAME + filenumber + ".png");
			}

			try {
				fOut = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				fOut.flush();
				fOut.close();
			} catch (FileNotFoundException e) {
				Log.e("FileManager", e.toString());
			} catch (IOException e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
			}
			bitmap.recycle();
		} else
			Toast.makeText(context, "WARNING: SD Card not mounted!", Toast.LENGTH_SHORT).show();
	}

	public void saveImageToInternal(Bitmap bitmap) {
		// TODO laurenz
	}

	/**
	 * Get an Image from SD Card
	 */

	public Bitmap loadImageFromGallery(String imageName) {
		if (SDMOUNTED) {
			InputStream is = FileManager.class.getResourceAsStream(SDPATH + imageName);
			return BitmapFactory.decodeStream(is);
		} else
			Toast.makeText(context, "WARNING: SD Card not mounted!", Toast.LENGTH_SHORT).show();
		return null;
	}

	public Bitmap loadImageFromInternal(String imageName) {
		if (SDMOUNTED) {
			File imageFile = new File(SDPATH + imageName);
			if (imageFile.exists()) {
				InputStream is = FileManager.class.getResourceAsStream(SDPATH + imageName);
				return BitmapFactory.decodeStream(is);
			} else
				return null;
		} else
			return null;
	}

	/**
	 * Delete an Image and its Thumbnail from the SD Card
	 * 
	 * @param imageName
	 */
	public void deleteImageFromGallery(String imageName) {
		if (SDMOUNTED) {
			File fileToDelete = new File(SDPATH + imageName);
			File thumbToDelete = new File(THUMBPATH + imageName);
			if (fileToDelete.exists()) {
				fileToDelete.delete();
				thumbToDelete.delete();
			}
		} else
			Toast.makeText(context, "WARNING: SD Card not mounted!", Toast.LENGTH_SHORT).show();
	}

	/**
	 * get the name of the latest Image on the SD Card
	 * 
	 * @return
	 */
	public String getLatestImage() {
		File directory = new File(SDPATH);
		String imageName = null;
		File files[] = directory.listFiles();
		int highestnumber = 0;
		for (File f : files) {
			if (f.exists()) {
				int newhighest = Integer.parseInt(f.getName().substring(3, f.getName().length() - 4));
				if (newhighest >= highestnumber) {
					highestnumber = newhighest + 1;
					imageName = f.getName();
				}
			}
		}
		if (imageName != null)
			return imageName;
		else
			return "Error";
	}
}
