package com.example.linesketch;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

public class SketchMainActivity extends AppCompatActivity {

    SeekBar rotateSlider;
    SeekBar scaleSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout root = findViewById(R.id.root);
        root.setOrientation(LinearLayout.VERTICAL);

        scaleSlider = new SeekBar(this);
        rotateSlider = new SeekBar(this);

        LinearLayout sliders = new LinearLayout(this);
        sliders.setOrientation(LinearLayout.VERTICAL);

        sliders.addView(scaleSlider, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        sliders.addView(rotateSlider, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        root.addView(sliders);

        SketchView sketchView = new SketchView(this);
        root.addView(sketchView);

        SketchModel model = new SketchModel();
        InteractionModel interactionModel = new InteractionModel();
        SketchController controller = new SketchController();

        model.addSubscriber(sketchView);
        interactionModel.addSubscriber(sketchView);

        controller.setIModel(interactionModel);
        controller.setModel(model);

        sketchView.setModel(model);
        sketchView.setInteractionModel(interactionModel);
        sketchView.setController(controller);

        scaleSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                controller.changeScale(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rotateSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                controller.changeRotation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.cut:
                break;
            case R.id.copy:
                break;
            case R.id.paste:
                break;
            case R.id.group:
                break;
            case R.id.ungroup:
                break;

        }
        return true;
    }

}
