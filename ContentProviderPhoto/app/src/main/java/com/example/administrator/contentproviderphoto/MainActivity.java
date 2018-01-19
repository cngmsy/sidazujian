package com.example.administrator.contentproviderphoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    private ListView listview;
    private SimpleAdapter simpleAdapter;
    ArrayList names = new ArrayList();
    ArrayList descs = new ArrayList();
    ArrayList<String> fileNames = new ArrayList();
    ArrayList<HashMap<String, Object>> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Button show = findViewById(R.id.bt_show);
        listview = findViewById(R.id.listview);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMediaPhoto();
            }
        });
    }

    //查询并添加到集合
    private void showMediaPhoto() {
        names.clear();
        descs.clear();
//        //通过ContentResolver查询所有图片信息(存储在外部存储器上的图片的Uri)
//        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        //通过ContentResolver查询所有图片信息(存储在内部存储器上的图片的Uri)
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            //获取图片的显示名
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //这里的desc为空，没有提供具体的描述信息
            String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));
            Log.d("GsonUtils", "desc=" + desc);
            //故描述信息用文件的大小来展示
            String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE) / 1024);
            Log.d("GsonUtils", "size=" + size);
            //获取图片的保存位置的数据
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            names.add(name);
            //descs.add(desc);
            //故描述信息用文件的大小来展示
            descs.add(size);
            //将图片的保存位置的数据转换成字符串形式的路径
            String filePath = new String(data, 0, data.length - 1);
            Log.d("GsonUtils", "filePath=" + filePath);
            fileNames.add(filePath);
        }

        for (int i = 0; i < names.size(); i++) {
            HashMap<String, Object> listItem = new HashMap<>();
            listItem.put("name", names.get(i));
            listItem.put("desc", descs.get(i));
            listItems.add(listItem);
        }

        //给ListView设置适配器
        simpleAdapter = new SimpleAdapter(
                MainActivity.this,
                listItems,
                R.layout.list_item,
                new String[]{"name", "desc"},
                new int[]{R.id.name, R.id.desc}
        );
        listview.setAdapter(simpleAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("GsonUtils", "onItemClick" + position);
                View viewDailog = getLayoutInflater().inflate(R.layout.dialog_item, null);
                ImageView show = (ImageView) viewDailog.findViewById(R.id.show);
                //获取对应条目上的指定图片
                Bitmap bitmap = BitmapFactory.decodeFile(fileNames.get(position));
                show.setImageBitmap(bitmap);

                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewDailog)
                        .setPositiveButton("关闭", null)
                        .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //点击删除，删除手机中的这张图片，并刷新适配器
                                getContentResolver().delete(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                                        MediaStore.Images.Media.DATA + "=?", new String[]{fileNames.get(position)});

                                listItems.remove(position);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });

    }


}
