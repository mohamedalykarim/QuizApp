package mohalim.app.quizapp.core.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.List;
import java.util.Random;

import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.UserItem;

public class Utils {

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static boolean isQuizListPeopleCanAccessContainsId(List<UserItem> list, String id) {
        for (UserItem object : list) {
            if (object.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static int randInt(int low, int high) {
        Random random = new Random();

        return random.nextInt(high-low) + low;
    }


}
