package com.example.gaoxixi.mapmutilnavigation;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.gaoxixi.mapmutilnavigation.Adapter.mutilNavigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MutilNavigation extends Activity implements View.OnClickListener,mutilNavigation.Callback {

    private ListView listView;
    private ImageView imageViewAddItem;
    private Button mutilNavigationButton;
    private mutilNavigation mutilNavigationAdapter;

    //创建一个list集合，集合元素是Map
    final List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();

    //定义item中用到的信息
    private int positionIcon  = R.drawable.position_icon;
    private String[] positionName = {"省图书馆","省体育场"};
    final String positionName1 = " ";
    private int positionDelete = R.drawable.delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutil_navigation);

        /**初始化控件*/
        initView();

        /**为listView布局填充信息*/
        addInfo();

        /**通过自定义Adapter显示Adapter*/
        mutilNavigationAdapter = new mutilNavigation(MutilNavigation.this,listItems,this);
        listView.setAdapter(mutilNavigationAdapter);

        /**定义增加一条Item的事件监听器*/
        imageViewAddItem.setOnClickListener(new MyClickListenerAddItem());

        /**为导航按钮设置监听事件*/
        mutilNavigationButton.setOnClickListener(new MyClickListenerNavition());
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.mutil_middle_list);
        imageViewAddItem = (ImageView) findViewById(R.id.mutil_end_add);
        mutilNavigationButton = (Button) findViewById(R.id.mutil_navigation_button);
    }

    private void addInfo() {
        for(int i = 0 ; i < positionName.length ; i++)
        {
           /**为listItem填充信息*/
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("positionIcon",positionIcon);
            listItem.put("positionName",positionName[i]);
            listItem.put("positionDelete",positionDelete);
            listItems.add(listItem);
        }
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 接口方法，响应ListView内部的按钮点击事件
     */
    @Override
    public void clickInListview(View view) {
        if(view.getId() == R.id.mutil_middle_delete)
        {
            /**
             *删除该item*/
            deleteItem(view);
        }
    }

    /**
     * 删除一条Item记录*/
    private void deleteItem(View view) {
        int position;
        position =  Integer.parseInt(view.getTag().toString());
        listItems.remove(position);
        mutilNavigationAdapter.notifyDataSetChanged();
    }

    /**增加一条Item的事件监听器*/
    class MyClickListenerAddItem implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /**增加一条Item*/
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("positionIcon",positionIcon);
            listItem.put("positionName",positionName1);
            listItem.put("positionDelete",positionDelete);
            listItems.add(listItem);
            mutilNavigationAdapter.notifyDataSetChanged();
        }
    }

    /**导航按钮监听器*/
    class MyClickListenerNavition implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            
        }
    }
}
