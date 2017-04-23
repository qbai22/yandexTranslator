package qbai22.com.yandextranslator.fragments.bookmarks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmsearchview.RealmSearchViewHolder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import qbai22.com.yandextranslator.R;
import qbai22.com.yandextranslator.model.realm.Translation;
import qbai22.com.yandextranslator.utils.RealmTranslationHelper;


public class BookmarksRecyclerAdapter
        extends RealmRecyclerViewAdapter<Translation, BookmarksRecyclerAdapter.WordViewHolder> {

    Realm mRealm;

    public BookmarksRecyclerAdapter(@NonNull Context context,
                                    @Nullable OrderedRealmCollection<Translation> data,
                                    boolean autoUpdate,
                                    Realm realm) {
        super(context, data, autoUpdate);
        mRealm = realm;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        WordViewHolder bvh = new WordViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        Translation translation = getItem(position);
        assert translation != null;
        final String inputText = translation.getInputText();
        final String translatedText  = translation.getTranslatedText();
        boolean isBookmarked = translation.isBookmarked();
        String fromLangCode = translation.getFromLangCode().toUpperCase();
        String toLangCode = translation.getToLangCode().toUpperCase();

        holder.inputText.setText(inputText);
        holder.translatedText.setText(translatedText);
        holder.languagePair.setText(fromLangCode + " - " + toLangCode);

        holder.bookmarkButton.setChecked(isBookmarked);
        holder.bookmarkButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    RealmTranslationHelper
                            .changeBookmarkStatus(mRealm, true, inputText, translatedText);
                } else {
                    RealmTranslationHelper
                            .changeBookmarkStatus(mRealm, false, inputText, translatedText);

                }
            }
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {}
            public void onEventAnimationStart(ImageView button, boolean buttonState) {}
        });
    }

    public class WordViewHolder extends RealmSearchViewHolder {

        @BindView(R.id.item_recycler_bkmrk_button)
        SparkButton bookmarkButton;
        @BindView(R.id.item_input_text_view)
        TextView inputText;
        @BindView(R.id.item_translated_text_view)
        TextView translatedText;
        @BindView(R.id.item_langPair_text_view)
        TextView languagePair;

        public WordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}