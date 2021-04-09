package gameml.gomoku;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class GomokuView extends View {

	protected static int GRID_SIZE = 20;
	protected static int GRID_WIDTH = 30;
	protected static int CHESS_DIAMETER = 26;
	protected static int mStartX;
	protected static int mStartY;
	private static int[][] mGridArray;
	private int wbflag = 1;
	private int mWinFlag = 0;
	private final int BLACK = 1;
	private final int WHITE = 2;
	int mGameState = GAMESTATE_RUN;
	static final int GAMESTATE_PRE = 0;
	static final int GAMESTATE_RUN = 1;
	static final int GAMESTATE_PAUSE = 2;
	static final int GAMESTATE_END = 3;

	private float mTouchStartX;
	private float mTouchStartY;
	private float mTouchCurrX;
	private float mTouchCurrY;
	private float mDifX;
	private float mDifY;
	public TextView mStatusTextView;
	private Paint mPaintCircle;
	private int mBGColor;
	private Paint mPaintGrid;
	private Paint mPaintGridBorder;
	private boolean mIsPut;
	private boolean mIsShowTitleBg;
	private CharSequence mText;
	private CharSequence STRING_WIN;
	private CharSequence STRING_LOSE;
	private CharSequence STRING_EQUAL;
	private GomokuAI Ai;
	private Paint mSplashScreenLogoPaint;
	private boolean mIsStarted;
	private int Width, Height;

	public GomokuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GomokuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);

		init();
	}

	private final Random mRnd = new Random();

	private void init() {

		mIsShowTitleBg = false;
		mGameState = 1;
		wbflag = BLACK;
		mWinFlag = 0;
		mGridArray = new int[GRID_SIZE - 1][GRID_SIZE - 1];
		mIsPut = true;

		mPaintGrid = new Paint();
		mPaintGrid.setColor(Color.rgb(77, 75, 75));
		mPaintGrid.setStrokeWidth(1);
		mPaintGrid.setStyle(Style.STROKE);
		mPaintGridBorder = new Paint();
		mPaintGridBorder.setColor(Color.rgb(77, 75, 75));
		mPaintGridBorder.setStrokeWidth(2);
		mPaintGridBorder.setStyle(Style.STROKE);
		mPaintGridBorder.setStrokeWidth(4);
		mPaintCircle = new Paint();
		mPaintCircle.setAntiAlias(true);

		mRnd.nextInt(6);
		int Colors[] = { Color.rgb(226, 182, 28), Color.rgb(186, 226, 28),
				Color.rgb(226, 121, 28), Color.rgb(28, 196, 226),
				Color.rgb(51, 143, 49), Color.rgb(51, 204, 49),
				Color.rgb(143, 102, 153) };

		mBGColor = Colors[mRnd.nextInt(6)];

		STRING_WIN = "White win! \n Press Fire Key to start new game.";
		STRING_LOSE = "Black win! \n Press Fire Key to start new game.";
		STRING_EQUAL = "Cool! You are equal! \n Press Fire Key to start new Game.";
		Ai = new GomokuAI(GRID_SIZE);

		mSplashScreenLogoPaint = new Paint();
		mSplashScreenLogoPaint.setAntiAlias(true);
		mSplashScreenLogoPaint.setColor(Color.WHITE);

		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Neuropol_Medium.TTF");
		mSplashScreenLogoPaint.setTypeface(tf);
		mIsStarted = false;

		if (Width != 0) {
			mStartX = Width / 2 - GRID_SIZE * GRID_WIDTH / 2;
			mStartY = Height / 2 - GRID_SIZE * GRID_WIDTH / 2;
		}

	}

	public void setTextView(TextView tv) {

		mStatusTextView = tv;
		mStatusTextView.setVisibility(View.INVISIBLE);
		mIsShowTitleBg = false;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		mStartX = w / 2 - GRID_SIZE * GRID_WIDTH / 2;
		mStartY = h / 2 - GRID_SIZE * GRID_WIDTH / 2;
		Width = w;
		Height = h;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!mIsStarted) {
			mIsStarted = true;
			invalidate();
			return false;
		}

		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			mIsPut = true;
			mStartX = mStartX + (int) mDifX;
			mStartY = mStartY + (int) mDifY;
			mTouchStartX = mTouchCurrX = x;
			mTouchStartY = mTouchCurrY = y;

			break;
		case MotionEvent.ACTION_MOVE:

			float TmpDifX = mDifX;
			float TmpDifY = mDifY;

			mTouchCurrX = x;
			mTouchCurrY = y;
			mDifX = mTouchCurrX - mTouchStartX;
			mDifY = mTouchCurrY - mTouchStartY;

			if ((mDifX != 0) || (mDifY != 0))
				mIsPut = false;
			else
				mIsPut = true;

			if ((mStartX + mDifX + GRID_WIDTH * GRID_SIZE < Width - 10)
					|| (mStartX + mDifX > 10)) {
				mDifX = TmpDifX;
			}

			if ((mStartY + mDifY + GRID_WIDTH * GRID_SIZE < Height - 10)
					|| (mStartY + mDifY > 10)) {
				mDifY = TmpDifY;
			}

			invalidate();
			break;
		case MotionEvent.ACTION_UP:

			if (mIsPut) {

				float x0 = GRID_WIDTH - (event.getX() - mStartX) % GRID_WIDTH;
				Log.e("x0=", String.valueOf(x0));
				float y0 = GRID_WIDTH - (event.getY() - mStartY) % GRID_WIDTH;
				Log.e("y0=", String.valueOf(y0));

				if (x0 < GRID_WIDTH / 2) {

					x = (int) ((event.getX() - mStartX) / GRID_WIDTH);
					Log.e("x=", String.valueOf(x));

				} else {

					x = (int) ((event.getX() - mStartX) / GRID_WIDTH) - 1;
					Log.e("x=", String.valueOf(x));

				}
				if (y0 < GRID_WIDTH / 2) {

					y = (int) ((event.getY() - mStartY) / GRID_WIDTH);
					Log.e("y=", String.valueOf(y));

				} else {

					y = (int) ((event.getY() - mStartY) / GRID_WIDTH) - 1;

				}
				if ((x >= 0 && x < GRID_SIZE - 1)
						&& (y >= 0 && y < GRID_SIZE - 1)) {

					if (mGridArray[x][y] == 0) {

						if (wbflag == BLACK) {

							putChess(x, y, BLACK);

							if (checkWin(BLACK)) {

								mText = STRING_LOSE;
								mGameState = GAMESTATE_END;
								showTextView(mText);
							} else if (checkFull()) {

								mText = STRING_EQUAL;
								mGameState = GAMESTATE_END;
								showTextView(mText);
							}

							wbflag = WHITE;
							MachineMove(x, y);//

						} else if (wbflag == WHITE) {

							putChess(x, y, WHITE);

							if (checkWin(WHITE)) {

								mText = STRING_WIN;
								mGameState = GAMESTATE_END;
								showTextView(mText);
							} else if (checkFull()) {

								mText = STRING_EQUAL;
								mGameState = GAMESTATE_END;
								showTextView(mText);
							}
							wbflag = BLACK;
						}
					}
				}
			}

			invalidate();
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		Log.e("KeyEvent.KEYCODE_DPAD_CENTER", " " + keyCode);

		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			switch (mGameState) {
			case GAMESTATE_PRE:
				break;
			case GAMESTATE_RUN:
				break;
			case GAMESTATE_PAUSE:
				break;
			case GAMESTATE_END: {

				Log.e("Fire Key Pressed:::", "FIRE");
				mGameState = GAMESTATE_RUN;
				this.setVisibility(View.VISIBLE);
				this.mStatusTextView.setVisibility(View.INVISIBLE);
				this.init();
				this.invalidate();

			}
				break;
			}
		}

		return super.onKeyDown(keyCode, msg);
	}

	@Override
	public void onDraw(Canvas canvas) {

		if (!mIsStarted) {

			mSplashScreenLogoPaint.setTextSize(29);
			canvas.drawText("G O M O K U", Width / 2 - 120, 63,
					mSplashScreenLogoPaint);
			mSplashScreenLogoPaint.setTextSize(9);
			canvas.drawText("S T A R T", Width / 2 - 30, 193,
					mSplashScreenLogoPaint);
			return;
		} else {

			canvas.drawColor(mBGColor);

			for (int i = 0; i < GRID_SIZE; i++) {

				int Left = mStartX + (int) mDifX;
				int Top = i * GRID_WIDTH + mStartY + (int) mDifY;
				int Rright = Left + GRID_WIDTH * GRID_SIZE;
				int Bottom = mStartY + (int) mDifY + i * GRID_WIDTH;

				canvas.drawLine(Left, Top, Rright, Bottom, mPaintGrid);

				Left = mStartX + (int) mDifX + i * GRID_WIDTH;
				Top = mStartY + (int) mDifY;
				Rright = mStartX + (int) mDifX + GRID_WIDTH * i;
				Bottom = mStartY + (int) mDifY + GRID_WIDTH * GRID_SIZE;

				canvas.drawLine(Left, Top, Rright, Bottom, mPaintGrid);
			}

			canvas.drawRect(mStartX + (int) mDifX, mStartY + (int) mDifY,
					mStartX + (int) mDifX + GRID_WIDTH * GRID_SIZE, mStartY
							+ (int) mDifY + GRID_WIDTH * GRID_SIZE,
					mPaintGridBorder);

			for (int i = 0; i < GRID_SIZE - 1; i++)
				for (int j = 0; j < GRID_SIZE - 1; j++) {
					if (mGridArray[i][j] == BLACK) {

						mPaintCircle.setColor(Color.BLACK);
						canvas.drawCircle(mStartX + mDifX + (i + 1)
								* GRID_WIDTH, mStartY + mDifY + (j + 1)
								* GRID_WIDTH, CHESS_DIAMETER / 2, mPaintCircle);
					} else if (mGridArray[i][j] == WHITE) {

						mPaintCircle.setColor(Color.WHITE);
						canvas.drawCircle(mStartX + mDifX + (i + 1)
								* GRID_WIDTH, mStartY + mDifY + (j + 1)
								* GRID_WIDTH, CHESS_DIAMETER / 2, mPaintCircle);
					}
				}
			if (mIsShowTitleBg) {

				Paint mPaintTitleBg = new Paint();
				mPaintTitleBg = new Paint();
				mPaintTitleBg.setColor(Color.rgb(0, 0, 0));
				// mPaintGrid.setStrokeWidth(1);
				mPaintTitleBg.setStyle(Style.FILL);

				canvas.drawRect(0, 0, Width, 40, mPaintTitleBg);
			}
		}

	}

	private void putChess(int x, int y, int blackwhite) {
		mGridArray[x][y] = blackwhite;
	}

	private boolean checkWin(int wbflag) {

		for (int i = 0; i < GRID_SIZE - 1; i++)
			for (int j = 0; j < GRID_SIZE - 1; j++) {

				if (((i + 4) < (GRID_SIZE - 1)) && (mGridArray[i][j] == wbflag)
						&& (mGridArray[i + 1][j] == wbflag)
						&& (mGridArray[i + 2][j] == wbflag)
						&& (mGridArray[i + 3][j] == wbflag)
						&& (mGridArray[i + 4][j] == wbflag)) {
					Log.e("check win or loss:", wbflag + "win");

					mWinFlag = wbflag;
				}

				if (((j + 4) < (GRID_SIZE - 1)) && (mGridArray[i][j] == wbflag)
						&& (mGridArray[i][j + 1] == wbflag)
						&& (mGridArray[i][j + 2] == wbflag)
						&& (mGridArray[i][j + 3] == wbflag)
						&& (mGridArray[i][j + 4] == wbflag)) {
					Log.e("check win or loss:", wbflag + "win");

					mWinFlag = wbflag;
				}

				if (((j + 4) < (GRID_SIZE - 1)) && ((i + 4) < (GRID_SIZE - 1))
						&& (mGridArray[i][j] == wbflag)
						&& (mGridArray[i + 1][j + 1] == wbflag)
						&& (mGridArray[i + 2][j + 2] == wbflag)
						&& (mGridArray[i + 3][j + 3] == wbflag)
						&& (mGridArray[i + 4][j + 4] == wbflag)) {
					Log.e("check win or loss:", wbflag + "win");

					mWinFlag = wbflag;
				}

				if (((i - 4) >= 0) && ((j + 4) < (GRID_SIZE - 1))
						&& (mGridArray[i][j] == wbflag)
						&& (mGridArray[i - 1][j + 1] == wbflag)
						&& (mGridArray[i - 2][j + 2] == wbflag)
						&& (mGridArray[i - 3][j + 3] == wbflag)
						&& (mGridArray[i - 4][j + 4] == wbflag)) {
					Log.e("check win or loss:", wbflag + "win");

					mWinFlag = wbflag;
				}
			}

		if (mWinFlag == wbflag) {
			return true;
		} else
			return false;

	}

	private boolean checkFull() {
		int mNotEmpty = 0;
		for (int i = 0; i < GRID_SIZE - 1; i++)
			for (int j = 0; j < GRID_SIZE - 1; j++) {
				if (mGridArray[i][j] != 0)
					mNotEmpty += 1;
			}

		if (mNotEmpty == (GRID_SIZE - 1) * (GRID_SIZE - 1))
			return true;
		else
			return false;
	}

	private void showTextView(CharSequence mT) {
		this.mStatusTextView.setText(mT);
		mStatusTextView.setBackgroundColor(BLACK);
		mStatusTextView.setFadingEdgeLength(6);
		mStatusTextView.setVisibility(View.VISIBLE);
		mIsShowTitleBg = true;

	}

	// AI working
	private void MachineMove(int iUser, int jUser) {

		Ai.machineMove(iUser, jUser, mGridArray);
		putChess(Ai.ResultCol, Ai.ResultRow, WHITE);

		if (checkWin(WHITE)) {
			mText = STRING_WIN;
			mGameState = GAMESTATE_END;

			showTextView(mText);

		} else if (checkFull()) {
			mText = STRING_EQUAL;
			mGameState = GAMESTATE_END;
			showTextView(mText);

		}
		wbflag = BLACK;
	}
}
