package com.example.kored.udemyartx;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

  ListView lstview;
  ArrayList<String> name;
  ArrayList<Bitmap> img;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    name = new ArrayList<>();
    img = new ArrayList<>();
    lstview = (ListView) findViewById(R.id.main_listvieww);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1,
        name);
    lstview.setAdapter(adapter);
    lstview.setOnItemClickListener(this);

    Special_Art.database = openOrCreateDatabase("Zrts", MODE_PRIVATE, null);

    Special_Art.database.execSQL("CREATE TABLE IF NOT EXISTS artsc2 (name VARCHAR, image BLOB)");
    Cursor cursor = Special_Art.database.rawQuery("Select * from artsc2", null);
    int namexd = cursor.getColumnIndex("name");
    int imagexd = cursor.getColumnIndex("image");
    while (cursor != null && cursor.moveToNext()) {
      name.add(cursor.getString(namexd));
      byte[] byteArray = cursor.getBlob(imagexd);
      Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
      img.add(bmp);
      adapter.notifyDataSetChanged();
    }
    Special_Art.database.close();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.main_menu_addImage:
        Intent i = new Intent(this, Special_Art.class);
        startActivity(i);
        break;
    }
    return super.onOptionsItemSelected(item);
  }


  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    Intent ItemClickIntent = new Intent(this, Special_Art.class);
    Bitmap selectBitmap = img.get(i);
    String selectName = name.get(i);

    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
    selectBitmap.compress(CompressFormat.PNG, 50, byteOutput);
    byte[] bitmapByte = byteOutput.toByteArray();

    Bundle bundle = new Bundle();
    bundle.putByteArray("img", bitmapByte);
    bundle.putString("name", selectName);
    ItemClickIntent.putExtra("bundle", bundle);
    startActivity(ItemClickIntent);
  }
}
