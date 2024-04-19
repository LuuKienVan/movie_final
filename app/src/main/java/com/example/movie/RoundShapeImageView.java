package com.example.movie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RoundShapeImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Path path = null;
    private Float radius = 18.0f;
    private RectF rect = null;

    public RoundShapeImageView(Context context) {
        super(context);
        init();
    }

    public RoundShapeImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public RoundShapeImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.path = new Path();
    }

    @Override
    public void onDraw(Canvas canvas) {
        rect = new RectF(0.0f, 0.0f, (float) this.getWidth(), (float) this.getHeight());
        Path path2 = path;
        RectF rectF = rect;
        Float f = radius;
        path2.addRoundRect(rectF, f, f, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
