package qbai22.com.yandextranslator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qbai22.com.yandextranslator.model.Language;

/**
 * Created by qbai on 15.03.2017.
 */

public class LanguagePickerActivity extends AppCompatActivity {
    @BindView(R.id.recent_lang_recycler_view)
    RecyclerView mRecentLangRecyclerView;
    @BindView(R.id.all_lang_recycler_view)
    RecyclerView mAllLangRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_picker);
        ButterKnife.bind(this);

        //явно не лучший способ
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000'> Выберите язык </font>"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dID = new DividerItemDecoration(this, layoutManager.getOrientation());
        List<Language> languageList = Language.getAllLanguages();
        LanguageRecyclerAdapter adapter = new LanguageRecyclerAdapter(languageList);

        mRecentLangRecyclerView.setLayoutManager(layoutManager);
        mRecentLangRecyclerView.addItemDecoration(dID);
        mRecentLangRecyclerView.setAdapter(adapter);

    }

    private class LanguageRecyclerAdapter
            extends RecyclerView.Adapter<LanguageRecyclerAdapter.LanguageViewHolder> {

        List<Language> data;

        public LanguageRecyclerAdapter(List<Language> languages) {
            data = languages;
        }

        @Override
        public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_language, parent, false);

            return new LanguageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(LanguageViewHolder holder, int position) {
            final Language language = data.get(position);
            final String label = language.getName();
            final String capitalizedLabel = label.substring(0, 1).toUpperCase() + label.substring(1);
            final String code = language.getCode();
            holder.languageName.setText(capitalizedLabel);
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("code", code);
                    resultIntent.putExtra("label", capitalizedLabel);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
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
    }

}
