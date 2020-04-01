package mohalim.app.quizapp.core.di.base;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import dagger.android.support.DaggerAppCompatActivity;
import mohalim.app.quizapp.R;
import mohalim.app.quizapp.core.models.QuizItem;
import mohalim.app.quizapp.core.models.UserItem;
import mohalim.app.quizapp.core.utils.Constants;
import mohalim.app.quizapp.ui.admin_main.AdminMainActivity;
import mohalim.app.quizapp.ui.feedback.FeedBackActivity;
import mohalim.app.quizapp.ui.login.LoginActivity;
import mohalim.app.quizapp.ui.user_main.UserMainActivity;

public class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "BaseActivity Tag";
    public UserItem intentUserItem;
    public QuizItem intentQuizItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    public void initNavigation(DrawerLayout drawerLayout, final Class<?> cls) {
        NavigationView navigationView = (NavigationView) drawerLayout.getChildAt(drawerLayout.getChildCount()-1);
        TextView textView = navigationView.findViewById(R.id.userNameTv);
        LinearLayout menuContainer = navigationView.findViewById(R.id.menuContainer);

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            return;
        }else{

            menuContainer.removeAllViews();

            textView.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

            for (int i =0; i <3; i++){

                if (i == 0){
                    View view = null;

                    if (cls.equals(UserMainActivity.class)){
                        view = addNavItem(menuContainer, "Instructor", R.drawable.constructor_icon);
                    }else if (cls.equals(AdminMainActivity.class)){
                        view = addNavItem(menuContainer, "Student", R.drawable. people_access_icon);
                    }

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;

                            if (cls.equals(UserMainActivity.class)){
                                intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                                intent.putExtra(Constants.USER_ITEM, intentUserItem);
                                intent.putExtra(Constants.QUESTION_ITEM, intentQuizItem);

                            }else if (cls.equals(AdminMainActivity.class)){
                                intent = new Intent(getApplicationContext(), UserMainActivity.class);
                                intent.putExtra(Constants.USER_ITEM, intentUserItem);
                                intent.putExtra(Constants.QUESTION_ITEM, intentQuizItem);                            }

                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            |
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            |
                                            Intent.FLAG_ACTIVITY_NEW_TASK
                            );

                            startActivity(intent);
                            finish();
                        }
                    });

                    drawerLayout.closeDrawer(drawerLayout.getChildAt(drawerLayout.getChildCount()-1));

                    menuContainer.addView(view);

                }else if (i == 1){
                    View view = addNavItem(menuContainer, "Feedback", R.drawable.feedback_icon);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), FeedBackActivity.class);
                            startActivity(intent);
                        }
                    });

                    drawerLayout.closeDrawer(drawerLayout.getChildAt(drawerLayout.getChildCount()-1));

                    menuContainer.addView(view);

                }else if (i == 2){

                    View view = addNavItem(menuContainer, "Logout", R.drawable.logout_icon);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            |
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            |
                                            Intent.FLAG_ACTIVITY_NEW_TASK
                            );

                            startActivity(intent);
                        }
                    });

                    menuContainer.addView(view);
                }



            }






        }


    }


    public View addNavItem(ViewGroup container, String titleString, int imgIcon){
        View view = getLayoutInflater().inflate(R.layout.navigation_main_item, container, false);
        TextView title = view.findViewById(R.id.itemTitleTv);
        ImageView img = view.findViewById(R.id.itemImg);

        title.setText(titleString);
        img.setImageDrawable(getDrawable(imgIcon));

        return view;
    }
}
