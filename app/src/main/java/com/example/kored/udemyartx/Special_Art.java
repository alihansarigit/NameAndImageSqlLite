package com.example.kored.udemyartx;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Special_Art extends AppCompatActivity {

  static SQLiteDatabase database;
  EditText txtname;
  ImageView imagevw;
  Bitmap selectionbmp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_special__art);
    txtname = (EditText) findViewById(R.id.special_txtid);
    imagevw = (ImageView) findViewById(R.id.special_imgview);

    Bundle b = getIntent().getBundleExtra("bundle");
    selectionbmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
    if(b!=null){
      byte[] bytarray = b.getByteArray("img");
      if (bytarray.length > 0) {
        txtname.setText(b.getString("name"));
        selectionbmp = BitmapFactory.decodeByteArray(b.getByteArray("img"), 0, bytarray.length);
      }
    }
    imagevw.setImageBitmap(selectionbmp);
  }

  public void Save(View view) {
    String name = txtname.getText().toString();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    selectionbmp.compress(CompressFormat.PNG, 50, outputStream);
    byte[] byteimg = outputStream.toByteArray();

    database = this.openOrCreateDatabase("Zrts", MODE_PRIVATE, null);
    database.execSQL("CREATE TABLE IF NOT EXISTS artsc2 (name VARCHAR, image BLOB)");

    String insertquery = "INSERT INTO artsc2 VALUES (?,?)";
    SQLiteStatement sqLiteStatement = database.compileStatement(insertquery);
    sqLiteStatement.bindString(1, name);
    sqLiteStatement.bindBlob(2, byteimg);
    sqLiteStatement.executeInsert();

    Intent i = new Intent(this, MainActivity.class);
    startActivity(i);
  }


  public void imageselection(View view) {
    Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(intent, 1);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
      Uri image = data.getData();
      try {
        selectionbmp = Media.getBitmap(this.getContentResolver(), image);
        imagevw.setImageBitmap(selectionbmp);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
