package comMain.GvozdevDanii.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {


    public static BufferedImage resize(BufferedImage image, int width, int height) {

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        newImage.getGraphics().drawImage(image, 0, 0, width, height, null);


        return newImage;
    }

    public static boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public static Integer[][] levelParser(String filePath) {

        Integer[][] result = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line = null;
            List<Integer[]> lvlLines = new ArrayList<Integer[]>();
            while ((line = reader.readLine()) != null) {
                lvlLines.add(strToIntArray(line.split(" ")));
            }
            result = new Integer[lvlLines.size()][lvlLines.get(0).length];

            for (int i = 0; i < lvlLines.size(); i++) {
                result[i] = lvlLines.get(i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Integer[] strToIntArray(String[] stringArr) {
        Integer[] intArr = new Integer[stringArr.length];

        for (int i = 0; i < stringArr.length; i++) {
            intArr[i] = Integer.parseInt(stringArr[i]);
        }
        return intArr;
    }


}
