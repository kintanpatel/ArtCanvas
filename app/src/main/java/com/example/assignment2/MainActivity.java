package com.example.assignment2;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyDrawingArea drawingArea;
    private final ImageView[] thumbnails = new ImageView[3];
    private ArrayList<Bitmap> alImage;
    private Integer currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingArea = findViewById(R.id.cusview);
        thumbnails[0] = findViewById(R.id.thumbnail1);
        thumbnails[1] = findViewById(R.id.thumbnail2);
        thumbnails[2] = findViewById(R.id.thumbnail3);

        alImage = new ArrayList<>();


        thumbnails[0].setOnClickListener(this);
        thumbnails[1].setOnClickListener(this);
        thumbnails[2].setOnClickListener(this);
        // Initialize color buttons and set click listeners
        Button blackColorButton = findViewById(R.id.blackColorButton);
        Button redColorButton = findViewById(R.id.redColorButton);
        Button blueColorButton = findViewById(R.id.blueColorButton);


        //Button Click for Change color for MyDrawingArea
        blackColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingArea.setCurrentColor(Color.BLACK);

            }
        });

        redColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingArea.setCurrentColor(Color.RED);
            }
        });

        blueColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingArea.setCurrentColor(Color.BLUE);
            }
        });
        //end

        //Retrieve Old Save Image
        alImage.add(getSaveImages("0"));
        alImage.add(getSaveImages("1"));
        alImage.add(getSaveImages("2"));
        updateThumbnailImages();
    }


    public void clear(View view) {
        drawingArea.clear();
    }


    public void save(View view) {
        //currentIndex is use for file name which is useful when get image from storage
        if (currentIndex == 3) {
            currentIndex = 0;
        }

        Bitmap b = drawingArea.getBitmap(); //we wrote this function inside custom view
        try {
            File f = new File(getFilesDir().getAbsolutePath() + "/" + currentIndex + ".png");

            FileOutputStream fos = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Remove first image and save new at last index
        alImage.remove(0);
        alImage.add(b);

        updateThumbnailImages();
        drawingArea.clear();
        currentIndex++;
    }

    //Function use to set Image when user save the image
    // also use for initial time when we collect image from local storage
    private void updateThumbnailImages() {
        // Update ImageView elements with new thumbnails
        for (int i = 0; i < alImage.size(); i++) {
            Bitmap img = alImage.get(i);
            if (img != null) {
                thumbnails[i].setImageBitmap(img);
            }
        }
    }


    @Override
    public void onClick(View view) {
        int index = 0;
        if (view.getId() == R.id.thumbnail1) {
            index = 0;
        } else if (view.getId() == R.id.thumbnail2) {
            index = 1;
        } else if (view.getId() == R.id.thumbnail3) {
            index = 2;
        }
        try {
            Bitmap imageBitmap = ((BitmapDrawable) thumbnails[index].getDrawable()).getBitmap();

            // Set the retrieved bitmap to your custom view
            drawingArea.clear();
            drawingArea.setBitmap(imageBitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Bitmap getSaveImages(String fileName) {
        File file = new File(getFilesDir(), fileName + ".png");

        if (file.exists()) {
            // The file exists, so let's decode it into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            if (bitmap != null) {
                // The Bitmap was successfully loaded from the file.
                // You can now use 'bitmap' as needed, such as displaying it in an ImageView.
                return bitmap;
            } else {
                // An error occurred while decoding the file into a Bitmap.
                // Handle this error condition.
            }
        } else {
            // The file does not exist.
            // Handle this case accordingly.
        }
        return null;
    }
}

