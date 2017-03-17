package qbai22.com.yandextranslator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.varunest.sparkbutton.SparkButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import qbai22.com.yandextranslator.model.realm.Translation;

/**
 * Created by qbai on 15.03.2017.
 */

public class BookmarkFragment extends Fragment {
    @BindView(R.id.bookmark_recycler_view)
    RecyclerView mBookmarksRecyclerView;

    BookmarkRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bookmark, container, false);
        ButterKnife.bind(this, v);

        RealmResults<Translation> data = RealmTranslationUtils.getAllTranslations();

        mAdapter = new BookmarkRecyclerAdapter(getActivity(), data, true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dID = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        mBookmarksRecyclerView.setLayoutManager(layoutManager);
        mBookmarksRecyclerView.addItemDecoration(dID);
        mBookmarksRecyclerView.setAdapter(mAdapter);
        return v;
    }

    private class BookmarkRecyclerAdapter
            extends RealmRecyclerViewAdapter<Translation, BookmarkRecyclerAdapter.BookmarkViewHolder> {


        public BookmarkRecyclerAdapter(@NonNull Context context,
                                       @Nullable OrderedRealmCollection<Translation> data,
                                       boolean autoUpdate) {
            super(context, data, autoUpdate);
        }

        @Override
        public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_word, parent, false);
            BookmarkViewHolder bvh = new BookmarkViewHolder(v);
            return bvh;
        }

        @Override
        public void onBindViewHolder(BookmarkViewHolder holder, int position) {
            Translation translation = getItem(position);
            assert translation != null;

            holder.bookmarkButton.setChecked(translation.isBookmarked());
            holder.inputText.setText(translation.getInputText());
            holder.translatedText.setText(translation.getTranslatedText());
            String fromLangCode = translation.getFromLangCode().toUpperCase();
            String toLangCode = translation.getToLangCode().toUpperCase();
            holder.languagePair.setText(fromLangCode + " - " + toLangCode);

        }

        public class BookmarkViewHolder extends RecyclerView.ViewHolder {

            SparkButton bookmarkButton;
            TextView inputText;
            TextView translatedText;
            TextView languagePair;

            public BookmarkViewHolder(View itemView) {
                super(itemView);
                bookmarkButton = (SparkButton) itemView.findViewById(R.id.item_recycler_bkmrk_button);
                inputText = (TextView) itemView.findViewById(R.id.item_input_text_view);
                translatedText = (TextView) itemView.findViewById(R.id.item_translated_text_view);
                languagePair = (TextView) itemView.findViewById(R.id.item_langPair_text_view);
            }
        }
    }
}
