package com.stockroompro.adapters;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.tool.core.Notification;
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.R;
import com.stockroompro.models.Message;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.converters.ChatMessageCursorConverter;
import com.stockroompro.utils.ChatConfig;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 14.04.15.
 */
public class ChatMessageAdapter extends DateFormatConverterCursorAdapter<ChatMessageCursorConverter> implements View.OnClickListener, OnLongClickListener {
    public static final int VIEW_TYPE_COUNT = 2;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM, h:mm", Locale.getDefault());

    private Context context;
    private long myId;
    private OnMessageClickListener onMessageClickListener;
    private SparseBooleanArray messagesStates;
    private SparseArray<View> messageTextViews;
    private int selectedPosition;

    public ChatMessageAdapter(Context context, Cursor c, Class converterClass, Fragment parent) {
        super(context, c, converterClass);
        this.context = context;
        initFields(context, c);

    }

    private void initFields(Context context, Cursor c) {
        myId = PersonalData.getInstance(context).getUserId();
        int count = 0;
        if (c != null)
            if (!c.isClosed()) {
                count = c.getCount();
            }
        messagesStates = new SparseBooleanArray(count);
        messageTextViews = new SparseArray<View>(count);
    }


    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessageCursorConverter cursorConverter = new ChatMessageCursorConverter();

        cursorConverter.setCursor(getCursor());
        if (cursorConverter.getObject(position) != null)
            if (cursorConverter.getObject(position).getSenderId() == myId)
                return ChatConfig.OUTGOING_MESSAGE;
            else
                return ChatConfig.INCOMING_MESSAGE;

        return ChatConfig.OUTGOING_MESSAGE;
    }


    @Deprecated
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getCursor() != null && !getCursor().isClosed() && !getCursor().isBeforeFirst() && !getCursor().isAfterLast()) {
            int type = getItemViewType(position);
            switch (type) {
                case ChatConfig.INCOMING_MESSAGE:
                    convertView = LayoutInflater.from(context).inflate(R.layout.adapter_chat_message_input, parent, false);
                    break;
                case ChatConfig.OUTGOING_MESSAGE:
                    convertView = LayoutInflater.from(context).inflate(R.layout.adapter_chat_message_output, parent, false);
                    LinearLayout textView = (LinearLayout) convertView.findViewById(R.id.adapter_chat_message_output_container);
                    textView.setClickable(true);
                    textView.setOnClickListener(this);
                    textView.setTag(position);

                    ChatMessageCursorConverter cursorConverter = new ChatMessageCursorConverter();
                    cursorConverter.setCursor(getCursor());
                    if (cursorConverter.getObject().getUnsend() != ChatConfig.SEND_STATUS_FAIL) {
                        ((ImageView) convertView.findViewById(R.id.iv_output_status)).setVisibility(View.GONE);
                        textView.setClickable(false);
                        textView.setOnClickListener(null);
                    }

                    if (cursorConverter.getObject(position).getSendingStatus() != ChatConfig.STATUS_READ) {
                        ((ArtJokerTextView) convertView.findViewById(R.id.tv_message_status)).setText(R.string.not_read);
                    } else {
                        ((ArtJokerTextView) convertView.findViewById(R.id.tv_message_status)).setVisibility(View.GONE);
                    }
                    break;
                default:
                    convertView = LayoutInflater.from(context).inflate(R.layout.adapter_chat_message_input, parent, false);

            }
        }
        if (convertView != null) {
            ViewHolder holder = new ViewHolder(
                    (LinearLayout) convertView.findViewById(R.id.adapter_chat_message_output_container),
                    (TextView) convertView.findViewById(R.id.tv_message_in_chat),
                    (TextView) convertView.findViewById(R.id.tv_message_date),
                    position);

            convertView.setTag(holder);
            bindView(convertView, context, getCursor());
        }
        return convertView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ChatMessageCursorConverter cursorConverter, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor, ChatMessageCursorConverter cursorConverter) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (cursorConverter != null && cursorConverter.isValid()) {

            holder.messageText.setText(cursorConverter.getObject().getText());
            holder.date.setText(dateFormat.format(new Date(SystemHelper.getInstance().getTimeInMillisFromSec(cursorConverter.getObject().getDate()))));
            if (messagesStates.get(holder.position)) {
                holder.container.setSelected(true);
            } else {
                holder.container.setSelected(false);
            }

            holder.container.setOnLongClickListener(this);
            messageTextViews.put(holder.position, view);
        }
    }

    @Override
    public void onClick(View currentView) {
        switch (currentView.getId()) {
            case R.id.adapter_chat_message_output_container:
                ChatMessageCursorConverter cursorConverter = new ChatMessageCursorConverter();
                cursorConverter.setCursor(getCursor());
                if (onMessageClickListener != null) {
                    ViewHolder holder = (ViewHolder) ((View) currentView.getParent().getParent().getParent()).getTag();
                    selectedPosition = holder.position;
                    if (messagesStates.get(holder.position)) {
                        setSelectedView(currentView, holder.position, false);
                        if (messagesStates.indexOfValue(true) != -1) {
                            onMessageClickListener.onMessageClick(getMessages(cursorConverter), holder.position, true);
                        } else {
                            onMessageClickListener.onMessageClick(getMessages(cursorConverter), holder.position, false);
                        }
                    } else {
                        setSelectedView(currentView, holder.position, true);
                        disableAnotherViews((Integer) currentView.getTag());
                        onMessageClickListener.onMessageClick(getMessages(cursorConverter), holder.position, true);
                    }
                }
                break;

        }
    }

    private void setSelectedView(View currentView, int position, boolean state) {
        currentView.setSelected(state);
        messagesStates.put(position, state);
    }

    public void setSelectedView(int position, boolean state) {
        messageTextViews.get(position).setSelected(state);
        messagesStates.put(position, state);
    }

    private void disableAnotherViews(int currentPos) {
        for (int i = 0; i < messageTextViews.size(); i++) {
            int position = messageTextViews.keyAt(i);
            if (position != currentPos) {
                messageTextViews.get(position).setSelected(false);
                messagesStates.put(position, false);
                notifyDataSetInvalidated();
            }
        }
    }

    private ArrayList<Message> getMessages(ChatMessageCursorConverter cursorConverter) {
        ArrayList<Message> listOfMessages = new ArrayList<Message>();
        for (int i = 0; i < messagesStates.size(); i++) {
            int position = messagesStates.keyAt(i);
            if (messagesStates.get(position)) {
                listOfMessages.add(cursorConverter.getObject(position));
            }
        }
        return listOfMessages;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.adapter_chat_message_output_container:
                ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                TextView showTextParam = (TextView) v.findViewById(R.id.tv_message_in_chat);
                manager.setPrimaryClip(ClipData.newPlainText(context.getResources().getString(R.string.message_is_copied),
                        showTextParam.getText().toString()));
                Notification.getInstance().show(context, context.getResources().getString(R.string.message_is_copied));
                return true;

            default:
                return false;
        }
    }


    private static class ViewHolder {
        LinearLayout container;
        TextView messageText;
        TextView date;
        int position;

        private ViewHolder(LinearLayout container, TextView messageText, TextView date, int position) {
            this.container = container;
            this.position = position;
            this.messageText = messageText;
            this.date = date;
        }
    }

    public interface OnMessageClickListener {
        void onMessageClick(List<Message> messages, int currentClickPosition, boolean isHavePressedItems);
    }

    public void setOnMessageClickListener(OnMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }
}
