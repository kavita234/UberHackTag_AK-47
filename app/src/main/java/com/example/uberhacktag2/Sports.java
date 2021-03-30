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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
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

public class Sports extends AppCompatActivity {
    private String[] longitude, latitude;
    private final int[][] dotCoords = new int[10][2];
    private final int[] pics = {R.drawable.sports1, R.drawable.sports2, R.drawable.sports3, R.drawable.sports4, R.drawable.sports5, R.drawable.sports6};
    private final int[] maps = {R.drawable.map_paris, R.drawable.map_seoul, R.drawable.map_london, R.drawable.map_beijing, R.drawable.map_greece};
    private String[] descriptions;
    private final String eventType = "Sports";
    private String[] places;
    private String currLong, currLat, destination;
    private final String bookBtn  = "Visit This Event";
    private final String[] times = {"Aug 1 - Dec 15    7:00-18:00", "Sep 5 - Nov 10    8:00-16:00", "Mar 8 - May 21    7:00-18:00"};

    private SliderAdapter sliderAdapter;

    private CardSliderLayoutManager layoutManger;
    private TextView TypeOfEvent;
    private TextView VisitEvent;
    private RecyclerView recyclerView;
    private ImageSwitcher mapSwitcher;
    private TextSwitcher placeSwitcher;
    private TextSwitcher clockSwitcher;
    private TextSwitcher descriptionsSwitcher;
    private View greenDot;

    private int currentPosition;

    private DecodeBitmapTask decodeMapBitmapTask;
    private DecodeBitmapTask.Listener mapLoadListener;

    public void sendMessage(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void sendBookingMessage(View view) {
        AndroidNetworking.get("https://maps.googleapis.com/maps/api/geocode/json")
                .addQueryParameter("latlng", currLat+","+currLong)
                .addQueryParameter("key", "YOUR_API_KEY")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject addresses) {
                        JSONArray arr = new JSONArray();
                        try {
                            arr = addresses.getJSONArray("results");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            destination = arr.getJSONObject(0).getString("formatted_address");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(), Address.class);
                        intent.putExtra("value","Booking a ride for - " + currLong + ", " + currLat + ", " + destination );
                        startActivity(intent);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    private void getData() {

        AndroidNetworking.get("https://api.predicthq.com/v1/events?category=sports&limit=10")
                .addHeaders("Authorization", "Bearer DLcs06ikjcABfWjcEgX9n0EN8NgKuJeCj7TW1J7M")
                .addHeaders("Accept", "application/json")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject events) {
                        JSONArray arr = new JSONArray();
                        try {
                            arr = events.getJSONArray("results");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        places = new String[arr.length()];
                        descriptions = new String[arr.length()];
                        longitude = new String[arr.length()];
                        latitude = new String[arr.length()];
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                places[i] = arr.getJSONObject(i).getString("title");
                                String descript = arr.getJSONObject(i).getString("description");
                                descriptions[i] = (descript.equals(""))?"No Description Available":descript;
                                longitude[i] = arr.getJSONObject(i).getJSONArray("location").getString(0);
                                latitude[i] = arr.getJSONObject(i).getJSONArray("location").getString(1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        sliderAdapter = new SliderAdapter(pics, arr.length(), new Sports.OnCardClickListener());
                        setContentView(R.layout.sports);
                        TypeOfEvent = findViewById(R.id.tv_event_type);
                        TypeOfEvent.setText(eventType);
                        VisitEvent = findViewById(R.id.ts_bookBtn);
                        VisitEvent.setText(bookBtn);
                        initRecyclerView();
                        initSwitchers();
                        initGreenDot();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(sliderAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });

        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();

        new CardSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && decodeMapBitmapTask != null) {
            decodeMapBitmapTask.cancel(true);
        }
    }

    private void initSwitchers() {
        currLong = longitude[0];
        currLat = latitude[0];

        placeSwitcher = (TextSwitcher) findViewById(R.id.ts_place);
        placeSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
        placeSwitcher.setCurrentText(places[0]);

        clockSwitcher = (TextSwitcher) findViewById(R.id.ts_clock);
        clockSwitcher.setFactory(new TextViewFactory(R.style.ClockTextView, false));
        clockSwitcher.setCurrentText(times[0]);

        descriptionsSwitcher = (TextSwitcher) findViewById(R.id.ts_description);
        descriptionsSwitcher.setInAnimation(this, android.R.anim.fade_in);
        descriptionsSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        descriptionsSwitcher.setFactory(new TextViewFactory(R.style.DescriptionTextView, false));
        descriptionsSwitcher.setCurrentText(descriptions[0]);

        mapSwitcher = (ImageSwitcher) findViewById(R.id.ts_map);
        mapSwitcher.setInAnimation(this, R.anim.fade_in);
        mapSwitcher.setOutAnimation(this, R.anim.fade_out);
        mapSwitcher.setFactory(new ImageViewFactory());
        mapSwitcher.setImageResource(maps[0]);

        mapLoadListener = new DecodeBitmapTask.Listener() {
            @Override
            public void onPostExecuted(Bitmap bitmap) {
                ((ImageView)mapSwitcher.getNextView()).setImageBitmap(bitmap);
                mapSwitcher.showNext();
            }
        };
    }

    private void initGreenDot() {
        mapSwitcher.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mapSwitcher.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                final int viewLeft = mapSwitcher.getLeft();
                final int viewTop = mapSwitcher.getTop() + mapSwitcher.getHeight() / 3;

                final int border = 100;
                final int xRange = Math.max(1, mapSwitcher.getWidth() - border * 2);
                final int yRange = Math.max(1, (mapSwitcher.getHeight() / 3) * 2 - border * 2);

                final Random rnd = new Random();

                for (int i = 0, cnt = dotCoords.length; i < cnt; i++) {
                    dotCoords[i][0] = viewLeft + border + rnd.nextInt(xRange);
                    dotCoords[i][1] = viewTop + border + rnd.nextInt(yRange);
                }

                greenDot = findViewById(R.id.green_dot);
                greenDot.setX(dotCoords[0][0]);
                greenDot.setY(dotCoords[0][1]);
            }
        });
    }

    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[] {R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[] {R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }
        currLong = longitude[pos];
        currLat = latitude[pos];
        placeSwitcher.setInAnimation(Sports.this, animV[0]);
        placeSwitcher.setOutAnimation(Sports.this, animV[1]);
        placeSwitcher.setText(places[pos % places.length]);

        clockSwitcher.setInAnimation(Sports.this, animV[0]);
        clockSwitcher.setOutAnimation(Sports.this, animV[1]);
        clockSwitcher.setText(times[pos % times.length]);

        descriptionsSwitcher.setText(descriptions[pos % descriptions.length]);

        showMap(maps[pos % maps.length]);

        ViewCompat.animate(greenDot)
                .translationX(dotCoords[pos % dotCoords.length][0])
                .translationY(dotCoords[pos % dotCoords.length][1])
                .start();

        currentPosition = pos;
    }

    private void showMap(@DrawableRes int resId) {
        if (decodeMapBitmapTask != null) {
            decodeMapBitmapTask.cancel(true);
        }

        final int w = mapSwitcher.getWidth();
        final int h = mapSwitcher.getHeight();

        decodeMapBitmapTask = new DecodeBitmapTask(getResources(), resId, w, h, mapLoadListener);
        decodeMapBitmapTask.execute();
    }

    private class TextViewFactory implements  ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(Sports.this);

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(Sports.this, styleId);
            } else {
                textView.setTextAppearance(styleId);
            }

            return textView;
        }

    }

    private class ImageViewFactory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {
            final ImageView imageView = new ImageView(Sports.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            final LayoutParams lp = new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);

            return imageView;
        }
    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm =  (CardSliderLayoutManager) recyclerView.getLayoutManager();

            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
                final Intent intent = new Intent(Sports.this, DetailsActivity.class);
                intent.putExtra(DetailsActivity.BUNDLE_IMAGE_ID, pics[activeCardPosition % pics.length]);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent);
                } else {
                    final CardView cardView = (CardView) view;
                    final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
                    final ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(Sports.this, sharedView, "shared");
                    startActivity(intent, options.toBundle());
                }
            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }
    }

}
