package qbai22.com.yandextranslator.fragments.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmsearchview.RealmSearchAdapter;
import co.moonmonkeylabs.realmsearchview.RealmSearchViewHolder;
import io.realm.Realm;
import io.realm.Sort;
import qbai22.com.yandextranslator.R;
import qbai22.com.yandextranslator.model.realm.Translation;
import qbai22.com.yandextranslator.utils.RealmTranslationHelper;

//поскольку фрагмент с историей и фрагмент с избранным
//имеют одинаковую структуру и внешний вид, и их отличает
//только набор слов - они используют один адаптер

public class HistoryRecyclerAdapter
        extends RealmSearchAdapter<Translation, HistoryRecyclerAdapter.WordViewHolder> {

    Realm mRealm;

    public HistoryRecyclerAdapter(@NonNull Context context,
                                  Realm realm,
                                  String filterColumnName) {
        super(context, realm, filterColumnName);
        mRealm = realm;
    }

    @Override
    public void setUseContains(boolean useContains) {
        super.setUseContains(true);
    }


    @Override
    public void setSortOrder(Sort sortOrder) {
        super.setSortOrder(null);
    }

    @Override
    public void setBasePredicate(String basePredicate) {
        super.setBasePredicate(null);
    }

    @Override
    public void setSortKey(String sortKey) {
        super.setSortKey(null);
    }

    @Override
    public WordViewHolder onCreateRealmViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        WordViewHolder bvh = new WordViewHolder(v);

        return bvh;
    }

    @Override
    public void onBindRealmViewHolder(WordViewHolder holder, int position) {
        Translation translation = realmResults.get(position);
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