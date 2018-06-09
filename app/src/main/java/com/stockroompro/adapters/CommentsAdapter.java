package com.stockroompro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.artjoker.core.views.ArtJokerTextView;
import com.artjoker.tool.core.SystemHelper;
import com.stockroompro.R;
import com.stockroompro.models.Comment;
import com.stockroompro.models.PersonalData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by user on 10.04.15.
 */
public class CommentsAdapter extends BaseAdapter {

    private static final int TYPE_USER_MESSAGE = 0;
    private static final int TYPE_ANOTHER_MESSAGE = TYPE_USER_MESSAGE + 1;
    private static final int COUNT_TYPES = TYPE_ANOTHER_MESSAGE + 1;
    private Context context;
    private ArrayList<Comment> comments = new ArrayList<>();
    private final SimpleDateFormat simpleDateFormat;
    private PersonalData userData;
    private CommentActionListener commentActionListener;

    {
        simpleDateFormat = new SimpleDateFormat("H:mm, dd MMM yyyy", Locale.getDefault());
    }

    public CommentsAdapter(Context context, ArrayList<Comment> c) {
        this.context = context;
        comments = c;
        userData = PersonalData.getInstance(context);
    }

    public void setCommentActionListener(CommentActionListener commentActionListener) {
        this.commentActionListener = commentActionListener;
    }

    @Override
    public int getViewTypeCount() {
        return COUNT_TYPES;
    }

    @Override
    public int getCount() {
        return comments != null ? comments.size() : 0;
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            int type = getItemViewType(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (type == TYPE_ANOTHER_MESSAGE) {
                convertView = inflater.inflate(R.layout.adapter_comments_input, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.adapter_comments_output, parent, false);
            }
            viewHolder = new ViewHolder((ArtJokerTextView) convertView.findViewById(R.id.tv_comments_sender_name),
                    (ArtJokerTextView) convertView.findViewById(R.id.tv_comments_date),
                    (ArtJokerTextView) convertView.findViewById(R.id.tv_comments_text),
                    (ImageView) convertView.findViewById(R.id.iv_like),
                    (ImageView) convertView.findViewById(R.id.iv_dislike),
                    (ArtJokerTextView) convertView.findViewById(R.id.tv_like_text),
                    (ArtJokerTextView) convertView.findViewById(R.id.tv_dislike_text),
                    (LinearLayout) convertView.findViewById(R.id.like_main_container),
                    (LinearLayout) convertView.findViewById(R.id.dislike_main_container));
            if (type == TYPE_ANOTHER_MESSAGE) {
                viewHolder.setArrow((ImageView) convertView.findViewById(R.id.iv_comments_input_arrow));
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Comment comment = getItem(position);
        viewHolder.date.setText(simpleDateFormat.format(SystemHelper.getInstance().getTimeInMillisFromSec(comment.getDate())));
        viewHolder.text.setText(comment.getText());
        viewHolder.likesText.setText(String.valueOf(comment.getLikes()));
        viewHolder.dislikesText.setText(String.valueOf(comment.getDislikes()));
        int vote = comment.getUserChoice();
        boolean enabled = userData.getToken() != null && vote == Comment.USER_DID_NOT_VOTE;
        viewHolder.dislike.setEnabled(enabled);
        viewHolder.like.setEnabled(enabled);
        if (!enabled) {
            if (vote == Comment.USER_CHOICE_IS_DISLIKE) {
                viewHolder.dislike.setImageLevel(1);
                viewHolder.like.setImageLevel(0);
            } else if (vote == Comment.USER_CHOICE_IS_LIKE) {
                viewHolder.dislike.setImageLevel(0);
                viewHolder.like.setImageLevel(1);
            } else {
                viewHolder.dislike.setImageLevel(0);
                viewHolder.like.setImageLevel(0);
            }
        } else {
            viewHolder.dislike.setImageLevel(0);
            viewHolder.like.setImageLevel(0);
            viewHolder.dislikeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commentActionListener != null && comment.getSenderId() != userData.getUserId()) {
                        viewHolder.dislike.setImageLevel(1);
                        viewHolder.likeContainer.setOnClickListener(null);
                        commentActionListener.dislike(comment.getId());

                    }
                }
            });
            viewHolder.likeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commentActionListener != null && comment.getSenderId() != userData.getUserId()) {
                        viewHolder.like.setImageLevel(1);
                        viewHolder.dislikeContainer.setOnClickListener(null);
                        commentActionListener.like(comment.getId());
                    }
                }
            });
        }

        if (getItemViewType(position) == TYPE_ANOTHER_MESSAGE) {
            viewHolder.senderName.setText(comment.getSenderName());
            viewHolder.senderName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commentActionListener != null) {
                        commentActionListener.goToSenderProfile(comment.getSenderId());
                    }
                }
            });

            viewHolder.getArrow().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commentActionListener != null) {
                        commentActionListener.replyComment(comment.getId());
                    }
                }
            });
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getSenderId() == userData.getUserId() ?
                TYPE_USER_MESSAGE : TYPE_ANOTHER_MESSAGE;
    }

    public void changeData(ArrayList<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private ArtJokerTextView senderName;
        private ArtJokerTextView date;
        private ArtJokerTextView text;
        private ImageView arrow;
        private ImageView like;
        private ImageView dislike;
        private LinearLayout likeContainer;
        private LinearLayout dislikeContainer;
        private ArtJokerTextView likesText;
        private ArtJokerTextView dislikesText;

        public ViewHolder(ArtJokerTextView senderName, ArtJokerTextView date,
                          ArtJokerTextView text, ImageView like, ImageView dislike,
                          ArtJokerTextView likesText, ArtJokerTextView dislikesText,
                          LinearLayout likeContainer, LinearLayout dislikeContainer) {
            this.senderName = senderName;
            this.date = date;
            this.text = text;
            this.like = like;
            this.dislike = dislike;
            this.likesText = likesText;
            this.dislikesText = dislikesText;
            this.likeContainer = likeContainer;
            this.dislikeContainer = dislikeContainer;
        }

        public ImageView getArrow() {
            return arrow;
        }

        public void setArrow(ImageView arrow) {
            this.arrow = arrow;
        }

    }

    public interface CommentActionListener {
        void replyComment(long parentId);

        void like(long commentId);

        void dislike(long commentId);

        void goToSenderProfile(long senderId);
    }
}
