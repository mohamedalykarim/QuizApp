package mohalim.app.quizapp.core.di.base;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import dagger.android.support.DaggerAppCompatActivity;
import mohalim.app.quizapp.R;
import mohalim.app.quizapp.ui.login.LoginActivity;

public class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "BaseActivity Tag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    public void initNavigation(DrawerLayout drawerLayout) {
        NavigationView navigationView = (NavigationView) drawerLayout.getChildAt(drawerLayout.getChildCount()-1);
        TextView textView = navigationView.findViewById(R.id.userNameTv);
        LinearLayout menuContainer = navigationView.findViewById(R.id.menuContainer);

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            return;
        }else{
            textView.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            View logoutView = getLayoutInflater().inflate(R.layout.navigation_main_item, menuContainer, false);
            TextView logoutTitle = logoutView.findViewById(R.id.itemTitleTv);
            ImageView logoutImg = logoutView.findViewById(R.id.itemImg);
            logoutTitle.setText("Logout");
            logoutImg.setImageDrawable(getDrawable(R.drawable.logout_icon));

            logoutView.setOnClickListener(new View.OnClickListener() {
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

            menuContainer.removeAllViews();
            menuContainer.addView(logoutView);
        }


    }
}
