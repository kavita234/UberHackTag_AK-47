package com.example.uberhacktag2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;
import com.example.uberhacktag2.cards.SliderAdapter;
import com.example.uberhacktag2.utils.DecodeBitmapTask;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public void sendMessage1(View view) {
        Intent intent = new Intent(getApplicationContext(), Concerts.class);
        startActivity(intent);
    }

    public void sendMessage2(View view) {
        Intent intent = new Intent(getApplicationContext(), Sports.class);
        startActivity(intent);
    }

    public void sendMessage3(View view) {
        Intent intent = new Intent(getApplicationContext(), PerformingArts.class);
        startActivity(intent);
    }

    public void sendMessage4(View view) {
        Intent intent = new Intent(getApplicationContext(), Conferences.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
