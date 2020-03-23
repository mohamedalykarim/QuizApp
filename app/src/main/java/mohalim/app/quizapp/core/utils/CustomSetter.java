package mohalim.app.quizapp.core.utils;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.util.List;

import mohalim.app.quizapp.core.models.AnswerItem;

public class CustomSetter {
    public final static String TAG = "CustomSetter";

    public static Typeface type;
    public final static String DEFAULT_ARABIC_FONT = "fonts/droid.ttf";

    @BindingAdapter("android:fontType")
    public static void setFont(View view, String font){

        if (type==null){
            type = Typeface.createFromAsset(view.getContext().getAssets(),font);
        }

        if (view instanceof TextView){
            ((TextView) view).setTypeface(type);
        }else if (view instanceof EditText){
            ((EditText) view).setTypeface(type);
        }else if (view instanceof Button){
            ((Button) view).setTypeface(type);
        }else if (view instanceof Switch){
            ((Switch) view).setTypeface(type);
        }
    }


    @BindingAdapter("android:textArray")
    public static void setTextArray(TextView textView, List<AnswerItem> answers){
        textView.setText("");
        for(AnswerItem answer : answers){
            textView.append("*  " +answer.getAnswerText() + "\n");
        }
    }

}
