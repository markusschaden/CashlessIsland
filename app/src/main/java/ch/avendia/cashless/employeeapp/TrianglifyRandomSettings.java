package ch.avendia.cashless.employeeapp;

import java.util.Random;

import ch.avendia.trianglify_lib.RandomColor;
import ch.avendia.trianglify_lib.TriangleColor;
import ch.avendia.trianglify_lib.TrianglifySettings;

/**
 * Created by Markus on 02.09.2015.
 */
public class TrianglifyRandomSettings extends TrianglifySettings {

    public TrianglifyRandomSettings() {
        RandomColor randomColor = new RandomColor();
        Random random = new Random();

        setCellSize(random.nextInt(60)+40);
        setVariance(Math.max(Math.min(0.85f, random.nextFloat()), 0.25f));
        setSeed(null);
        TriangleColor color = randomColor.getRandomColor();
        setxColors(color);
        TriangleColor color2 = randomColor.getRandomColor();
        setyColors(color2);
        setStrokeWidth(1.51f);
    }
}
