package laboratorio.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import laboratorio.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button productListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Event handlers:
        productListButton = (Button) findViewById(R.id.product_list_button);
        productListButton.setOnClickListener(view -> startActivity(
                new Intent(this, ProductListActivity.class))
        );
    }
}