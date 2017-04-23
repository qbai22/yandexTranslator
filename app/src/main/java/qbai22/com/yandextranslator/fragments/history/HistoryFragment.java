package qbai22.com.yandextranslator.fragments.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmsearchview.RealmSearchView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import qbai22.com.yandextranslator.R;
import qbai22.com.yandextranslator.model.realm.Translation;


public class HistoryFragment extends Fragment {
    @BindView(R.id.history_search_recycler)
    RealmSearchView mRealmSearchRecyclerView;

    private Realm mRealm;
    private HistoryRecyclerAdapter mAdapter;
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

        mAdapter = new HistoryRecyclerAdapter(getActivity(), mRealm, "inputText");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dID = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());

        mRealmSearchRecyclerView.setAdapter(mAdapter);

        mRealm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                mAdapter.notifyDataSetChanged();
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
