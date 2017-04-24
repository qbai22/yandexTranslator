package qbai22.com.yandextranslator.fragments.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import qbai22.com.yandextranslator.R;
import qbai22.com.yandextranslator.fragments.WordRecyclerAdapter;
import qbai22.com.yandextranslator.model.realm.Translation;


/*
 * Created by Vladimir Kraev
 */

public class HistoryFragment extends Fragment {

    @BindView(R.id.history_recycler)
    RecyclerView mHistoryRecyclerView;
    @BindView(R.id.history_edit_text)
    EditText mHistoryEditText;

    private Realm mRealm;
    private WordRecyclerAdapter mAdapter;
    private RealmResults<Translation> mHistoryResults;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, v);

        mHistoryResults = mRealm.where(Translation.class).findAllSorted("date", Sort.DESCENDING);
        mAdapter = new WordRecyclerAdapter(getActivity(), mHistoryResults, true, mRealm);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dID = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        mHistoryRecyclerView.setLayoutManager(layoutManager);
        mHistoryRecyclerView.addItemDecoration(dID);
        mHistoryRecyclerView.setAdapter(mAdapter);

        mHistoryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    mAdapter.updateData(mHistoryResults);
                }
                OrderedRealmCollection<Translation> query =
                        mRealm.where(Translation.class)
                                .contains("inputText", s.toString().toLowerCase())
                                .findAllSorted("date", Sort.DESCENDING);
                mAdapter.updateData(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
