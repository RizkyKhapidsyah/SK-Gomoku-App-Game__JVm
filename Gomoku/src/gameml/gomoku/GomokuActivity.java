package gameml.gomoku;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GomokuActivity extends Activity {

	GomokuView GView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Calendar c = Calendar.getInstance();
		int yr = c.get(Calendar.YEAR);

		GView = (GomokuView) this.findViewById(R.id.gomokuview);
		GView.setTextView((TextView) this.findViewById(R.id.text));

	}
}