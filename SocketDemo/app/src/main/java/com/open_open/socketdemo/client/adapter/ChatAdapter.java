package com.open_open.socketdemo.client.adapter;

import android.graphics.BitmapFactory;

import com.github.library.BaseMultiItemQuickAdapter;
import com.github.library.BaseViewHolder;
import com.open_open.socketdemo.R;
import com.open_open.socketdemo.client.bean.Transmission;
import com.open_open.socketdemo.client.bean.Constants;

import java.util.List;

/******************************************
 * 类名称：ChatAdapter
 * 类描述：
 *
 * @version: 1.0
 * @author: chj
 * @time: 2018/1/18
 * @email: chj294671171@126.com
 * @github: https://github.com/cngmsy
 ******************************************/

public class ChatAdapter extends BaseMultiItemQuickAdapter<Transmission, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatAdapter(List<Transmission> data) {
        super(data);

        addItemType(Constants.CHAT_FROM, R.layout.chat_from_msg);
        addItemType(Constants.CHAT_SEND, R.layout.chat_send_msg);
    }

    @Override
    protected void convert(BaseViewHolder helper, Transmission item) {

        switch (item.itemType) {
            case Constants.CHAT_FROM:
                helper.setText(R.id.chat_from_content, item.content);
                break;
            case Constants.CHAT_SEND:
                if (item.showType == 1) {
                    helper.setVisible(R.id.chat_send_image, true);
                    helper.setVisible(R.id.chat_send_content, false);
                    helper.setImageBitmap(R.id.chat_send_image, BitmapFactory.decodeFile(item.content));
                } else {
                    helper.setVisible(R.id.chat_send_image, false);
                    helper.setVisible(R.id.chat_send_content, true);
                    helper.setText(R.id.chat_send_content, item.content);
                }
                break;
            default:
        }

    }
}

