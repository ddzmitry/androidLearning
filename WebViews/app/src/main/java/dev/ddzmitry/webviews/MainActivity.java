package dev.ddzmitry.webviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webView);
        // enable JS
        webView.getSettings().setJavaScriptEnabled(true);
        // Set web view client its own
        webView.setWebViewClient(new WebViewClient());

        //webView.loadUrl("http://www.google.com");

        webView.loadData("<html> <body> <h1> Hello </h1> <p> This is my cool website </p> </body></html>","text-html","UTF-8");
    }
}
