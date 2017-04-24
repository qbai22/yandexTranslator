package qbai22.com.yandextranslator.fragments.bookmarks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import qbai22.com.yandextranslator.R;
import qbai22.com.yandextranslator.fragments.WordRecyclerAdapter;
import qbai22.com.yandextranslator.model.realm.Translation;


/*
 * Created by Vladimir Kraev
 */

public class BookmarkFragment extends Fragment {

    @BindView(R.id.bookmark_recycler_view)
    RecyclerView mBookmarksRecyclerView;

    private Realm mRealm;
    private WordRecyclerAdapter mAdapter;
    private RealmResults<Translation> mBookmarks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bookmark, container, false);
        ButterKnife.bind(this, v);

        mBookmarks = mRealm.where(Translation.class).equalTo("isBookmarked", true).findAllSorted("date", Sort.DESCENDING);
        mAdapter = new WordRecyclerAdapter(getActivity(), mBookmarks, true, mRealm);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dID = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        mBookmarksRecyclerView.setLayoutManager(layoutManager);
        mBookmarksRecyclerView.addItemDecoration(dID);
        mBookmarksRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
    }

}
