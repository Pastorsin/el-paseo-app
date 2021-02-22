package laboratorio.app.helpers;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class IntentManager {

    private final Fragment fragment;

    public IntentManager(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setInstagramIntent(TextView view) {
        final String INSTAGRAM_URL = "http://instagram.com/_u/%s";

        view.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String instagramUser = view.getText().toString();
            String url = String.format(INSTAGRAM_URL, instagramUser);
            intent.setData(Uri.parse(url));

            fragment.startActivity(intent);
        });
    }

    public void setPhoneIntent(TextView view) {
        view.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + view.getText().toString()));

            fragment.startActivity(intent);
        });
    }

    public void setEmailIntent(TextView view) {
        view.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + view.getText().toString()));

            fragment.startActivity(intent);
        });
    }
}
