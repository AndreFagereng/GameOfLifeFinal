package no.hioa.gameoflifeandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText searchBar;
    private Button toQrBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = (EditText) findViewById(R.id.search_field);
        toQrBtn = (Button) findViewById(R.id.to_qr_btn);
    }

    public void btnToQr(View v) {
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.putExtra("qr", searchBar.toString());
        startActivity(intent);
    }
}
