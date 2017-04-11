package com.example.gaoxixi.mapmutilnavigation.Adapter;

/**
 * Created by GaoXixi on 2017/4/7.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gaoxixi.mapmutilnavigation.R;

import java.util.List;
import java.util.Map;

/**
 * 借用接口回调的方式处理listView的内部点击事件
 * */
public  class mutilNavigation extends BaseAdapter implements View.OnClickListener{

    private LayoutInflater layoutInflater;
    private List<Map<String,Object>> listItems;  //定义数据
    private Map<String,Object> listItem;

    private ViewHolder viewHolder;
    /**
     * 所有listView共用一个mCallback接口*/
    private Callback mCallback;

    /**
     * 自定义接口，用于回调按钮点击事件到Activity*/
    public interface Callback{
        public void clickInListview(View view);
    }

    public mutilNavigation(Context context,List<Map<String,Object>> listItems,
                           Callback callback)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.listItems = listItems;
        mCallback = callback;
    }

    /**
     * 响应内部点击事件，调用自定义接口，并传入view*/
    @Override
    public void onClick(View view) {
        mCallback.clickInListview(view);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Map<String,Object> getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        listItem = getItem(i);

        if(convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.mutil_navigation_list_item,null);
            viewHolder = new ViewHolder(convertView);

            viewHolder.mutilMiddlePosition = (ImageView) convertView.findViewById(R.id.mutil_middle_icon);
            viewHolder.mutilMiddleText = (EditText) convertView.findViewById(R.id.mutil_middle_text);
            viewHolder.mutilMiddleDelete = (ImageView) convertView.findViewById(R.id.mutil_middle_delete);

            /**
             * 把组件引用通过setTag方法附加在View上面，
             * 当加载第二页的时候，你就不用再次去findViewById了，
             * 直接用getTag方法来取出数据引用即可。
             */
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**设置组件内容*/
//        viewHolder.mutilMiddlePosition.setBackgroundResource(R.drawable.position_icon);
//        viewHolder.mutilMiddleText.setText("西安理工大学");
//        viewHolder.mutilMiddleDelete.setBackgroundResource(R.drawable.delete);

        viewHolder.mutilMiddleDelete.setOnClickListener(this);

        /**
         * 设置当前按钮所在的位置*/
        viewHolder.mutilMiddleDelete.setTag(i);

        return convertView;
    }
     class ViewHolder
    {
        ImageView mutilMiddlePosition;    //定位图标
        EditText mutilMiddleText;    //输入目标点
        ImageView mutilMiddleDelete;  //删除图标

        public ViewHolder(View view)
        {
            mutilMiddlePosition = (ImageView) view.findViewById(R.id.mutil_middle_icon);
            mutilMiddleText = (EditText) view.findViewById(R.id.mutil_middle_text);
            mutilMiddleDelete = (ImageView) view.findViewById(R.id.mutil_middle_delete);
        }
    }
}
