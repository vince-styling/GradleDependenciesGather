package gradledependenciesgather.gradledependenciesgather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.sublibrary.LayoutLibUsage;
import com.example.toplibrary.MainActivity;

public class MainPageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        findViewById(R.id.btnClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // will crash because MainActivity extended FragmentActivity !
                startActivity(new Intent(MainPageActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LayoutLibUsage.execute();
    }
}
