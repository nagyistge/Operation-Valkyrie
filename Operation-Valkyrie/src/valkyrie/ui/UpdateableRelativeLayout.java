package valkyrie.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * 
 * COPYRIGHT: Paul Neuhold, Laurenz Theuerkauf, Alexander Ritz, Jakob Schweighofer, Milo Tischler
 * � Milo Tischler, Jakob Schweighofer, Alexander Ritz, Paul Neuhold, Laurenz Theuerkauf
 * 
 */
public class UpdateableRelativeLayout extends RelativeLayout implements IUpdateableUI {
	private static final String TAG = "UpdateableRelativeLayout";

	public UpdateableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutManager.getInstance().registerUpdateableComponent(this);
	}

	/**
	 * Redraws this UI Component.
	 * 
	 */
	public void redrawUI(RelativeLayout uiElements) {
		
		// remove all child elements from this layout
		this.removeAllViews();
		
		// add possibly new elements to panel
		if(uiElements == null)
			Log.d("FasuDebug", "omfg");
		this.addView(uiElements);
		
	}

}
