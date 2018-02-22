package me.marcooliveira.forcetouchtest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Dialog builder;
    ImageView sfo;
    ImageView sq;
    CardView dialog;

    Rect wishRect = new Rect();
    Rect cartRect = new Rect();
    Rect shareRect = new Rect();

    ImageView wishlist;
    ImageView cart;
    ImageView share;

    private static final int ACTION_NONE = -1;
    private static final int ACTION_WISH = 0;
    private static final int ACTION_CART = 1;
    private static final int ACTION_SHARE = 2;

    int currentButton = ACTION_NONE;

    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            int x = (int) motionEvent.getRawX();
            int y = (int) motionEvent.getRawY();

            switch (actionMasked) {
                case MotionEvent.ACTION_DOWN:
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    hideQuickView();
                    break;

                case MotionEvent.ACTION_MOVE:
                    wishlist.getGlobalVisibleRect(wishRect);
                    cart.getGlobalVisibleRect(cartRect);
                    share.getGlobalVisibleRect(shareRect);
                    int newButton = ACTION_NONE;

                    Log.d("WishRect", wishRect.toShortString() + "; curr: (" + x + ", " + y + ")");
                    if (wishRect.contains(x, y)) {
                        newButton = ACTION_WISH;
                    }
                    if (cartRect.contains(x, y)) {
                        newButton = ACTION_CART;
                    }
                    if (shareRect.contains(x, y)) {
                        newButton = ACTION_SHARE;
                    }

                    if (currentButton != newButton) {
                        Log.e("Button", "Current button: " + newButton);
                        currentButton = newButton;
                        highlightSelectedButton(currentButton);
                    }
                    break;

                default:
                    break;
            }

            return false;
        }
    };

    private void highlightSelectedButton(int button) {
        wishlist.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        cart.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        share.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);

        if (button == ACTION_WISH) {
            wishlist.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            wishlist.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        }

        if (button == ACTION_CART) {
            cart.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            cart.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        }

        if (button == ACTION_SHARE) {
            share.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            share.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = findViewById(R.id.dialog);
        final ImageView image = findViewById(R.id.image);
        wishlist = findViewById(R.id.wish);
        cart = findViewById(R.id.cart);
        share = findViewById(R.id.share);

        wishlist.getHitRect(wishRect);

        sq = findViewById(R.id.sq);
        sfo = findViewById(R.id.sfo);

        sq.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Bitmap bitmap = ((BitmapDrawable)sq.getDrawable()).getBitmap();
                image.setImageBitmap(bitmap);
                dialog.setVisibility(View.VISIBLE);
//                publicationQuickView(bitmap);
                // não é centralizado
                return true;
            }
        });

        sfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Bitmap bitmap = ((BitmapDrawable)sfo.getDrawable()).getBitmap();
                image.setImageBitmap(bitmap);
//                dialog.setVisibility(View.VISIBLE);
                // centralizado na tela
                // ja tem ~blur de fundo
                // precisa de ajustes pra hover de botão da forma certa
                publicationQuickView(bitmap);
                return true;
            }
        });

        sfo.setOnTouchListener(mOnTouchListener);
        sq.setOnTouchListener(mOnTouchListener);
    }

    public void publicationQuickView(Bitmap bitmap) {
        View view = getLayoutInflater().inflate( R.layout.dialog_preview, null);

        ImageView image = view.findViewById(R.id.image);
//        ImageView wishlist = view.findViewById(R.id.wish);
//        ImageView cart = view.findViewById(R.id.cart);
//        ImageView share = view.findViewById(R.id.share);

        image.setImageBitmap(bitmap);

//        Picasso.with(this).load(post.picture).priority(Picasso.Priority.HIGH).noPlaceholder().into(postImage);
//        Picasso.with(this).load(post.user.picture).noPlaceholder().into(profileImage);

        builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.setContentView(view);
        builder.show();
    }

    public void hideQuickView(){
        if(builder != null) builder.dismiss();
        dialog.setVisibility(View.GONE);
    }
}
