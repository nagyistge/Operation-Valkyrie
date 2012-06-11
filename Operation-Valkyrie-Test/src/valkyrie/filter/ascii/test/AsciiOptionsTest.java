package valkyrie.filter.ascii.test;

import java.lang.reflect.Field;

import valkyrie.filter.FilterManager;
import valkyrie.filter.ascii.Ascii;
import valkyrie.filter.ascii.Converter;
import valkyrie.main.R;
import valkyrie.ui.LayoutManager;
import valkyrie.ui.MainActivity;
import valkyrie.ui.preview.CameraPreviewViewCV;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.jayway.android.robotium.solo.Solo;

public class AsciiOptionsTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private static final String TAG = "AsciiOptionsTest";

	private MainActivity mainActivity;
	
	private Ascii ascii;

	private Solo solo;

	public AsciiOptionsTest() {
		super("valkyrie.ui", MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.mainActivity = this.getActivity();

		LayoutManager.getInstance().setMainActivity(this.getActivity());

		solo = new Solo(this.getInstrumentation(), this.mainActivity);

		try {
			this.runTestOnUiThread(new Runnable() {
				public void run() {
					try {
						Field filterManagerField = mainActivity.getClass().getDeclaredField("filterManager");
						
						filterManagerField.setAccessible(true);
						
						FilterManager filterManager = (FilterManager) filterManagerField.get(mainActivity);
						
						filterManager.setActiveFilter(new Ascii());
						
						ascii = (Ascii) filterManager.getActiveFilter();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Throwable e) {
			Log.e(TAG, e.toString());
			assertTrue(false);
		}
	}

	public void testPreconditions() {
		assertNotNull(this.mainActivity);
		assertNotNull(this.getInstrumentation());
		assertNotNull(this.solo);
	}

	public void testChangeFontSize() {
		
		final int TEST_SIZE = 5;
		
		this.solo.sendKey(Solo.MENU);

		assertNotNull(this.solo.getView(R.id.ascii_option_fontsize));
		
		SeekBar fontSize = (SeekBar) this.getActivity().findViewById(R.id.ascii_option_fontsize);
		
		this.solo.setProgressBar(fontSize, TEST_SIZE);
		
		try {			
			Field converterField = this.ascii.getClass().getDeclaredField("converter");
			
			converterField.setAccessible(true);
			
			Converter converter = (Converter) converterField.get(this.ascii);
			
			Field fontsizeFiled = converter.getClass().getDeclaredField("fontsize");
			Field fontscale = converter.getClass().getDeclaredField("FONT_SCALE");
			
			fontsizeFiled.setAccessible(true);
			fontscale.setAccessible(true);
			
			int size = fontsizeFiled.getInt(converter);
			int scale = fontscale.getInt(converter);
			
			assertTrue(size == TEST_SIZE + scale);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void testChangeBackgroundColor() {
		fail("Not yet implemented");
	}

	public void testChangeForegroundColor() {
		fail("Not yet implemented");
	}

	public void testChangeColorMode() {
		
		try {			
			Field converterField = this.ascii.getClass().getDeclaredField("converter");
			
			converterField.setAccessible(true);
			
			Converter converter = (Converter) converterField.get(this.ascii);
			
			Field colorModeFiled = converter.getClass().getDeclaredField("colorMode");
			
			colorModeFiled.setAccessible(true);

			Boolean beforeColor = colorModeFiled.getBoolean(converter);
			
			
			this.solo.sendKey(Solo.MENU);
			
			assertNotNull(this.solo.getView(R.id.color_mode));
			
			ToggleButton colorMode = (ToggleButton) this.solo.getView(R.id.color_mode);
			
			this.solo.clickOnView(colorMode);
			
			
			Boolean afterColor = colorModeFiled.getBoolean(converter);
			
			
			assertTrue(beforeColor != afterColor);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
