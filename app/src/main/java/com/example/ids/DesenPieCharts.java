package com.example.ids;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DesenPieCharts extends View {
    public DesenPieCharts(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint2=new Paint();
        int startP=200;
        int latimeColoana=50;

        for(int i=0;i<8;i++){
            paint2.setColor(Color.rgb(((i+2)*23)%256,(i*79)%256,(i*157)%256));
            canvas.drawRect(startP,
                    800-((i+1)*100),
                    startP+latimeColoana,
                    800,
                    paint2
            );
            startP+=latimeColoana*2;
        }
        Paint paint = new Paint();

        for (int i = 0; i < 4; i++) {
            paint.setColor(Color.rgb((i*23)%256,(i*79)%256,(i*157)%256));
            canvas.drawArc(
                    100,
                    100,
                    500,
                    500,
                    90*i,
                    90,
                    false,
                    paint
            );
        }

        //canvas.drawText("My Text", paint2);




    }
}