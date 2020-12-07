package laboratorio.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import laboratorio.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button categoriesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Event handlers:
        categoriesButton = (Button) findViewById(R.id.category_list_button);
        categoriesButton.setOnClickListener(view -> startActivity(
                new Intent(this, CategoryListActivity.class))
        );
    }
}