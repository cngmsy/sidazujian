package com.example.a110.mytongxunlu;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ContentResolver cr;
    private ListView ls_contacts;
    private List<Map<String,String>> mp=new ArrayList<>();
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //拿到内容访问者
        cr = getContentResolver();
        //得到listView
        ls_contacts = (ListView) findViewById(R.id.ls_contacts);
        //实例一个简易适配器
        simpleAdapter = new SimpleAdapter(this,mp, R.layout.item_listview,new String[]{"name","phone"},new int[]{R.id.tv_item_list_name,R.id.tv_item_list_number} );
        //给listView设置适配器
        ls_contacts.setAdapter(simpleAdapter);
    }


    public  void getsms(View view){
        Intent intent=new Intent(this,SMSActivity.class);
        startActivity(intent);
    }


    public void getContacts(View view){
       // content://contacts/people/  这个URI将返回设备上的所有联系人信息
      //  content://contacts/people/45 这个URI返回单个结果（联系人信息中ID为45的联系人记录）
        Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cs=cr.query(uri,null,null,null,null,null);
        while(cs.moveToNext()){
            //拿到联系人id 跟name
            int id=cs.getInt(cs.getColumnIndex("_id"));
            String name=cs.getString(cs.getColumnIndex("display_name"));
            //得到这个id的所有数据（data表）
            Uri uri1=Uri.parse("content://com.android.contacts/raw_contacts/"+id+"/data");
            Cursor cs2=cr.query(uri1,null,null,null,null,null);
            Map<String,String>  maps=new HashMap<>();//实例化一个map
            while ( cs2.moveToNext()){
                //得到data这一列 ，包括很多字段
                String data1=cs2.getString(cs2.getColumnIndex("data1"));
                //得到data中的类型
                String type=cs2.getString(cs2.getColumnIndex("mimetype"));
                String str=type.substring(type.indexOf("/")+1,type.length());//截取得到最后的类型
                if("name".equals(str)){//匹配是否为联系人名字
                    maps.put("name",data1);
                }if("phone_v2".equals(str)){//匹配是否为电话
                    maps.put("phone",data1);
                }
                Log.i("test",data1+"       "+type);
            }
            mp.add(maps);//将map加入list集合中
        }
        simpleAdapter.notifyDataSetChanged();//通知适配器发生数据改变

        Button button=(Button)view;//得到button
        button.setEnabled(false);//设置不可点击
    }
}
