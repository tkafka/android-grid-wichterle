package eu.inmite.android.gridwichterle.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import eu.inmite.android.gridwichterle.core.Config;
import eu.inmite.android.gridwichterle.core.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Michal Matl (michal.matl@inmite.eu)
 * Date: 7/21/13
 * Time: 4:32 PM
 */
public class DrawView extends View {

	private final Paint paint = new Paint();
	private final Paint highlightPaint = new Paint();
	private final Paint rectPaint = new Paint();
	private final int height;
	private final int width;
	private final int mSquare;
	private float[] points;
	private float[] highlightPoints;
	private Rect[] rects;

	public DrawView(Context context, int height, int width) {
		super(context);

		this.height = height;
		this.width = width;

		Config config = (Config) getContext().getApplicationContext().getSystemService(Config.class.getName());
		int color = config.getColor();
		int rectColor = Color.argb(Color.alpha(color) / 2, Color.red(color), Color.green(color), Color.blue(color));
		paint.setColor(rectColor);
		highlightPaint.setColor(rectColor);
		rectPaint.setColor(rectColor);
		mSquare = Utils.getPxFromDpi(getContext(), config.getGridSideSize());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		List<Float> gridPoints = new LinkedList<Float>();

		int countHorizontalLines = height / mSquare;
		int countVerticalLines = width / mSquare;

		//prepare horizontal lines
		float gap = mSquare;
		for (int i = 0; i <= countHorizontalLines; i++) {
			gridPoints.add(0f);
			gridPoints.add(gap);
			gridPoints.add((float) width);
			gridPoints.add(gap);

			gap = gap + mSquare;

		}

		//prepare vertical lines
		gap = mSquare;
		for (int i = 0; i <= countVerticalLines; i++) {
			gridPoints.add(gap);
			gridPoints.add(0f);
			gridPoints.add(gap);
			gridPoints.add((float) height);

			gap = gap + mSquare;
		}

		points = new float[gridPoints.size()];
		for (int i = 0; i < gridPoints.size(); i++) {
			points[i] = gridPoints.get(i);
		}

		// significant lines

		List<Float> highlightGridPoints = new LinkedList<Float>();

		// add 48, 56 dp horizontal lines, assuming 8dp square it's 6, 7
		List<Integer> horizontalLines = new LinkedList<Integer>();
		horizontalLines.add(6);
		horizontalLines.add(7);

		for (int i = 0; i < horizontalLines.size(); i++) {
			float y = mSquare * horizontalLines.get(i);
			highlightGridPoints.add(0f);
			highlightGridPoints.add(y);
			highlightGridPoints.add((float) width);
			highlightGridPoints.add(y);
		}

		// add 16, 48, 56, 72 dp lines lines, assuming 8dp square it's 2, 6, 7, 9
		List<Integer> verticalLines = new LinkedList<Integer>();
		verticalLines.add(2);
		verticalLines.add(6);
		verticalLines.add(7);
		verticalLines.add(9);
		verticalLines.add(countVerticalLines - 2);


		for (int i = 0; i < verticalLines.size(); i++) {
			float x = mSquare * verticalLines.get(i);
			highlightGridPoints.add(x);
			highlightGridPoints.add(0f);
			highlightGridPoints.add(x);
			highlightGridPoints.add((float) height);
		}

		highlightPoints = new float[highlightGridPoints.size()];
		for (int i = 0; i < highlightGridPoints.size(); i++) {
			highlightPoints[i] = highlightGridPoints.get(i);
		}

		// rects

		List<Integer> horizontalRects = new LinkedList<Integer>();
		horizontalRects.add(5);

		List<Integer> verticalRects = new LinkedList<Integer>();
		verticalRects.add(0);
		verticalRects.add(1);
		verticalRects.add(5);
		verticalRects.add(6);
		verticalRects.add(countVerticalLines - 2);
		verticalRects.add(countVerticalLines - 1);

		rects = new Rect[horizontalRects.size() + verticalRects.size()];
		int rectIndex = 0;

		for (int i = 0; i < horizontalRects.size(); i++) {
			int y = mSquare * horizontalRects.get(i);
			rects[rectIndex++] = new Rect(0, y + 1, width, y + mSquare - 1);
		}

		for (int i = 0; i < verticalRects.size(); i++) {
			int x = mSquare * verticalRects.get(i);
			rects[rectIndex++] = new Rect(x + 1, 0, x + mSquare - 1, height);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//draw whole grid
		for (Rect rect : rects) {
			canvas.drawRect(rect, rectPaint);
		}

		canvas.drawLines(points, paint);
		canvas.drawLines(highlightPoints, highlightPaint);

	}
}
