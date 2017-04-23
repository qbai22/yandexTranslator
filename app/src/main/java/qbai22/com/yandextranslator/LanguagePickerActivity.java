package qbai22.com.yandextranslator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qbai22.com.yandextranslator.model.Language;

/**
 * Created by qbai on 15.03.2017.
 */

public class LanguagePickerActivity extends AppCompatActivity {
    // TODO: 20.04.2017  добавить тулбар со стрелкой назад

    public static final String LANGUAGE_CODE = "code";
    public static final String LANGUAGE_LABLE = "label";
    @BindView(R.id.all_lang_recycler_view)
    RecyclerView mAllLangRecyclerView;
    @BindView(R.id.search_lang_edit_text)
    EditText mSearchLangEditText;
    @BindView(R.id.picker_activity_toolbar)
    Toolbar mToolbar;

    List<Language> mLanguageList;
    LanguageRecyclerAdapter mAdapter;
    LanguageRecyclerAdapter.LanguageFilter mLanguageFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_picker);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dID = new DividerItemDecoration(this, layoutManager.getOrientation());
        mLanguageList = Language.getAllLanguages();
        mAdapter = new LanguageRecyclerAdapter(mLanguageList);

        mAllLangRecyclerView.setLayoutManager(layoutManager);
        mAllLangRecyclerView.addItemDecoration(dID);
        mAllLangRecyclerView.setAdapter(mAdapter);

        mSearchLangEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.getFilter().filter(s.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LanguageRecyclerAdapter
            extends RecyclerView.Adapter<LanguageRecyclerAdapter.LanguageViewHolder>
            implements Filterable {

        List<Language> mDisplayedList;
        //нам нужна ссылка на полный дата сет при обнулении фильтрации
        List<Language> mFullData;
        LanguageFilter mLanguageFilter;


        public LanguageRecyclerAdapter(List<Language> languages) {
            mDisplayedList = languages;
            mFullData = languages;
        }

        @Override
        public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_language, parent, false);

            return new LanguageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(LanguageViewHolder holder, int position) {
            final Language language = mDisplayedList.get(position).capitalizeLabel();
            final String label = language.getName();
            final String code = language.getCode();
            holder.languageName.setText(label);
            //возвраащем пользователя назад с кодом выбранного языка
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(LANGUAGE_CODE, code);
                    resultIntent.putExtra(LANGUAGE_LABLE, label);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDisplayedList.size();
        }

        @Override
        public Filter getFilter() {
            if(mLanguageFilter == null){
                mLanguageFilter = new LanguageFilter();
            }
            return mLanguageFilter;
        }

        public class LanguageViewHolder extends RecyclerView.ViewHolder {

            TextView languageName;
            ImageView checkImage;
            LinearLayout container;

            public LanguageViewHolder(View itemView) {
                super(itemView);
                languageName = (TextView) itemView.findViewById(R.id.item_lang_text_view);
                checkImage = (ImageView) itemView.findViewById(R.id.item_check_image);
                container = (LinearLayout) itemView.findViewById(R.id.language_item_container);
            }
        }

        class LanguageFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.toString().length() == 0) {
                    results.values = mFullData;
                    results.count = mFullData.size();
                } else {
                    List<Language> filteredLanguages = new ArrayList<>();
                    for(Language language : mDisplayedList){
                        if(language
                                .getName().toLowerCase()
                                .contains(constraint.toString().toLowerCase())){
                            filteredLanguages.add(language);
                        }
                    }
                    results.values = filteredLanguages;
                    results.count = filteredLanguages.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDisplayedList = (List<Language>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
