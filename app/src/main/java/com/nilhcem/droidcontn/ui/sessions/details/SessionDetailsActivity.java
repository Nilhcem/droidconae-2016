package com.nilhcem.droidcontn.ui.sessions.details;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.nilhcem.droidcontn.DroidconApp;
import com.nilhcem.droidcontn.R;
import com.nilhcem.droidcontn.data.app.model.Session;
import com.nilhcem.droidcontn.data.app.model.Speaker;
import com.nilhcem.droidcontn.data.database.dao.SessionsDao;
import com.nilhcem.droidcontn.receiver.reminder.SessionsReminder;
import com.nilhcem.droidcontn.ui.BaseActivity;
import com.nilhcem.droidcontn.ui.speakers.details.SpeakerDetailsDialogFragment;
import com.nilhcem.droidcontn.utils.Animations;
import com.nilhcem.droidcontn.utils.App;
import com.nilhcem.droidcontn.utils.Strings;
import com.nilhcem.droidcontn.utils.Views;
import com.squareup.picasso.Picasso;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class SessionDetailsActivity extends BaseActivity<SessionDetailsPresenter> implements SessionDetailsView {

    @Extra Session session;

    @Inject Picasso picasso;
    @Inject SessionsDao sessionsDao;
    @Inject SessionsReminder sessionsReminder;

    @Bind(R.id.session_details_layout) View layout;
    @Bind(R.id.session_details_toolbar) Toolbar toolbar;
    @Bind(R.id.session_details_toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.session_details_photo) ImageView photo;
    @Bind(R.id.session_details_header) ViewGroup header;
    @Bind(R.id.session_details_title) TextView title;
    @Bind(R.id.session_details_info) TextView talkInfo;
    @Bind(R.id.session_details_description_header) View descriptionHeader;
    @Bind(R.id.session_details_description) TextView description;
    @Bind(R.id.session_details_speakers_title) TextView speakersTitle;
    @Bind(R.id.session_details_speakers_container) ViewGroup speakersContainer;
    @Bind(R.id.session_details_fab) FloatingActionButton fab;

    @Override
    protected SessionDetailsPresenter newPresenter() {
        return new SessionDetailsPresenter(this, session, sessionsDao, sessionsReminder);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DroidconApp.get(this).component().inject(this);
        SessionDetailsActivityIntentBuilder.inject(getIntent(), this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void bindSessionDetails(Session session) {
        title.setText(session.getTitle());
        bindTalkInfo(session);
        if (TextUtils.isEmpty(session.getDescription())) {
            descriptionHeader.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
        } else {
            descriptionHeader.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            description.setText(session.getDescription());
        }

        header.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = header.getHeight();
                if (height != 0) {
                    Views.removeOnGlobalLayoutListener(header.getViewTreeObserver(), this);
                    int toolbarHeight = height + Views.getActionBarSize(SessionDetailsActivity.this);
                    toolbar.getLayoutParams().height = toolbarHeight;
                    toolbar.requestLayout();
                    toolbarLayout.getLayoutParams().height = Math.round(2.25f * (toolbarHeight));
                    toolbarLayout.requestLayout();
                    bindHeaderPhoto(session, header.getWidth());
                }
            }
        });

        List<Speaker> speakers = session.getSpeakers();
        if (speakers != null && !speakers.isEmpty()) {
            speakersTitle.setText(getResources().getQuantityString(R.plurals.session_details_speakers, speakers.size()));
            for (Speaker speaker : speakers) {
                SessionDetailsSpeaker view = new SessionDetailsSpeaker(this, speaker, picasso);
                view.setOnClickListener(v -> openSpeakerDetails(speaker));
                speakersContainer.addView(view);
            }
        }

        fab.setOnClickListener(v -> presenter.onFloatingActionButtonClicked());
    }

    @Override
    public void updateFabButton(boolean isSelected, boolean animate) {
        if (animate) {
            Animations.scale(fab, 0.8f, 1f, 600);
        }

        if (isSelected) {
            fab.setImageDrawable(Views.getDrawable(this, R.drawable.session_details_like_selected));
        } else {
            fab.setImageDrawable(Views.getDrawable(this, R.drawable.session_details_like_default));
        }
    }

    @Override
    public void showSnackbarMessage(@StringRes int resId) {
        Snackbar.make(layout, resId, Snackbar.LENGTH_SHORT).show();
    }

    private void bindTalkInfo(Session session) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        String day = session.getFromTime().format(DateTimeFormatter.ofPattern(getString(R.string.session_details_talk_info_date_pattern)));
        String fromTime = session.getFromTime().format(timeFormatter);
        String toTime = session.getToTime().format(timeFormatter);
        String room = session.getRoom();
        talkInfo.setText(Strings.capitalizeFirstLetter(getString(R.string.session_details_talk_info, day, fromTime, toTime, room)));
    }

    private void bindHeaderPhoto(Session session, int headerWidth) {
        String url = App.getPhotoUrl(session);
        if (!TextUtils.isEmpty(url)) {
            picasso.load(url).resize(headerWidth, 0).into(photo);
        }
    }

    private void openSpeakerDetails(Speaker speaker) {
        SpeakerDetailsDialogFragment.show(speaker, getSupportFragmentManager());
    }
}
