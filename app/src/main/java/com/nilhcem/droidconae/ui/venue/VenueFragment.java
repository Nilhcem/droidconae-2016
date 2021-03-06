package com.nilhcem.droidconae.ui.venue;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.nilhcem.droidconae.R;
import com.nilhcem.droidconae.ui.BaseFragment;
import com.nilhcem.droidconae.ui.BaseFragmentPresenter;
import com.nilhcem.droidconae.utils.Intents;
import com.nilhcem.droidconae.utils.Views;

import butterknife.Bind;
import butterknife.OnClick;

public class VenueFragment extends BaseFragment {

    @Bind(R.id.venue_image) ImageView photo;
    @Bind(R.id.venue_directions) TextView directions;

    private static final float PHOTO_RATIO = 0.357f;
    private static final String COORDINATES_URI = "geo:25.1931753,55.2745114?q=" + Uri.encode("Impact HUB Dubai");

    @Override
    protected BaseFragmentPresenter newPresenter() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.venue, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPhotoSize();
        initDirections();
    }

    @OnClick(R.id.venue_locate)
    void openMapsLocation() {
        if (!Intents.startUri(getContext(), COORDINATES_URI)) {
            View view = getView();
            if (view != null) {
                Snackbar.make(view, R.string.venue_see_location_error, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void initPhotoSize() {
        photo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = photo.getWidth();
                if (width != 0) {
                    Views.removeOnGlobalLayoutListener(photo.getViewTreeObserver(), this);
                    photo.getLayoutParams().height = Math.round(width * PHOTO_RATIO);
                    photo.requestLayout();
                }
            }
        });
    }

    private void initDirections() {
        directions.setText(Html.fromHtml(getString(R.string.venue_directions_content)));
        directions.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
