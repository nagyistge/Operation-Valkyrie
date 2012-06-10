package valkyrie.filter.ascii;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import valkyrie.ui.LayoutManager;
import valkyrie.ui.MainActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.PaintDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;

import valkyrie.colorpicker.ColorPicker;
import valkyrie.colorpicker.ColorPickerDialog.OnColorChangedListener;
import valkyrie.filter.FilterAssets;
import valkyrie.filter.FilterInternalStorage;
import valkyrie.filter.IFilter;
import valkyrie.main.R;

/**
 * 
 * COPYRIGHT: Paul Neuhold, Laurenz Theuerkauf, Alexander Ritz, Jakob
 * Schweighofer, Milo Tischler � Milo Tischler, Jakob Schweighofer, Alexander
 * Ritz, Paul Neuhold, Laurenz Theuerkauf
 * 
 */
public class Ascii implements IFilter {
	public static final String TAG = "Ascii";

	/**
	 * OPtions: - Font Size : seekBar - Foregr, background : colorpicker - color
	 * : onOff
	 */
	private Font activeFont;
	private Converter converter;
	private TableLayout layout;
	private SharedPreferences options;

	private Vector<Font> fonts;
	private String fontsList[] = { "test1" };

	public static final String name = "ascii";

	/**
	 * TODO: Sry aber bei mir wirft des a nullpointer exception wenn ich alle
	 * filter f�r den filtermanager instancier, lg milo
	 */
	public Ascii() {
		this.fonts = new Vector<Font>();
		for (String name : this.fontsList) {
			this.fonts.add(new Font(name, true));
		}

		this.activeFont = this.fonts.get(0);

		this.converter = new Converter();
	}

	public Bitmap manipulatePreviewImage(Mat bitmapMat) {
		this.options = LayoutManager.getInstance().getSharedPreferencesOfFilter(Ascii.class.getSimpleName());
		boolean colorMode = options.getBoolean("color_mode", false);
		if (colorMode)
			return this.converter.colorToASCII(bitmapMat, this.activeFont.getLUT());
		else
			return this.converter.grayscaleToASCII(bitmapMat, this.activeFont.getLUT());
	}

	public Bitmap manipulateImage(Mat bitmapMat) {
		this.options = LayoutManager.getInstance().getSharedPreferencesOfFilter(Ascii.class.getSimpleName());
		boolean colorMode = options.getBoolean("color_mode", false);
		if (colorMode)
			return this.converter.colorToASCII(bitmapMat, this.activeFont.getLUT());
		else
			return this.converter.grayscaleToASCII(bitmapMat, this.activeFont.getLUT());
	}

	public int getFilterCaptureFormat() {
		return Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGB;
	}

	/**
	 * Returns the defined UI-Elements for the Options Panel as whole
	 * RelativeLayout. TODO: vl hamma hier falsch herum gedacht, es w�re wsh eine methode setUIElements besser,
	 * bei dieser bekommt der filter sein inflatetes layout und kann nachher die listener drauf setzen und braucht somit
	 * kein context objekt mehr!
	 * 
	 * @param mainActivity
	 *            Activity, the main activity of the Program. Gives us access to
	 *            the LayoutInflater.
	 */
	public TableLayout getUIElements(Activity mainActivity) {
		final LayoutInflater inflater = (LayoutInflater) mainActivity
				.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);

		this.layout = (TableLayout) inflater.inflate(R.layout.ascii, null);

		ColorPicker backgroundcolorPicker = (ColorPicker) this.layout.findViewById(R.id.backgroundcolor);

		backgroundcolorPicker.setColorChangeListener(new ColorPicker.ColorChangeListener() {

			public void colorChanged(int color) {
				converter.setBackgroundcolor(color);
			}
		});

		ColorPicker foregroundcolorPicker = (ColorPicker) this.layout.findViewById(R.id.foregroundcolor);

		foregroundcolorPicker.setColorChangeListener(new ColorPicker.ColorChangeListener() {

			public void colorChanged(int color) {
				converter.setForegroundColor(color);
			}
		});

		SeekBar fontsizeSeekBar = (SeekBar) this.layout.findViewById(R.id.ascii_option_fontsize);
		fontsizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				converter.setFontSize(progress);
			}
		});

		return this.layout;
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public Bitmap getIcon() {
		return null;
	}

	public void setup(FilterInternalStorage filterInternalStorage, FilterAssets filterAssets, Boolean firstRun) {

	}

	/**
	 * Initializes Options for the current Filter.
	 */
	public void initOptions() {

	}
}
