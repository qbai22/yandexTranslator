package qbai22.com.yandextranslator.fragments.translation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.varunest.sparkbutton.SparkButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import qbai22.com.yandextranslator.LanguagePickerActivity;
import qbai22.com.yandextranslator.R;
import qbai22.com.yandextranslator.model.dictionaryResponce.DictionaryResponse;
import qbai22.com.yandextranslator.model.realm.Translation;
import qbai22.com.yandextranslator.model.transatorResponce.TranslatorResponse;
import qbai22.com.yandextranslator.utils.NetworkUtils;
import qbai22.com.yandextranslator.utils.RealmTranslationHelper;
import qbai22.com.yandextranslator.utils.TranslationPreferencesUtils;

import static android.app.Activity.RESULT_OK;

/*
 * Created by Vladimir Kraev
 */


public class TranslationFragment extends Fragment {

    public final static String TAG = "Translation fragment";

    public final static int LANGUAGE_FROM_PICK_REQUEST_CODE = 23;
    public final static int LANGUAGE_TO_PICK_REQUEST_CODE = 22;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int REQUEST_CAMERA_PERMISSION = 888;

    @BindView(R.id.translation_window)
    EditText mEditText;
    @BindView(R.id.translation_text_view)
    TextView mTranslationTextView;
    @BindView(R.id.translation_fragment_bkmrk_button)
    SparkButton mBookmarkButton;
    @BindView(R.id.from_lang_button)
    Button mFromLangButton;
    @BindView(R.id.to_lang_button)
    Button mToLangButton;
    @BindView(R.id.language_swap_image_button)
    ImageButton mSwapLangImageButton;
    @BindView(R.id.camera_image_view)
    ImageView mCameraImageView;
    @BindView(R.id.clear_image_view)
    ImageView mClearImageView;
    @BindView(R.id.scroll_linear)
    LinearLayout mScrollLinearLayout;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;


    String mCurrentPhotoPath;
    Bitmap currentBitmap;

    private boolean mIsBookmarked;
    private String mCurrentText;
    private String mTranslatedText;
    private String mFromLangCode;
    private String mToLangCode;
    private Realm mRealm;
    private Translation mTranslation;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshBookmarkButtonState();
        if (currentBitmap != null) {
            String text = detectText(currentBitmap);
            mEditText.setText(text);
            currentBitmap = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translation, container, false);
        ButterKnife.bind(this, v);
        //обновляем состояние кнопок
        initViewsAndVariables();

        //устанавливаем сохраненный текст в поле ввода
        mEditText.setText(TranslationPreferencesUtils.getCurrentText(getActivity()));
        mEditText.addTextChangedListener(new TextWatcher() {
            Handler handler = new Handler();
            Runnable delayedAction;

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    mScrollLinearLayout.removeAllViews();
                    mTranslationTextView.setText("");
                    mBookmarkButton.setVisibility(View.INVISIBLE);
                    mBookmarkButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

                mCurrentText = s.toString().trim().toLowerCase();
                //обновляем значение текущего текста для лоадера
                TranslationPreferencesUtils.setCurrentText(getActivity(), mCurrentText);

                //отменяет предыдущий поиск если он есть
                if (delayedAction != null) {
                    handler.removeCallbacks(delayedAction);
                }

                if (s.toString().length() > 1) {
                    delayedAction = new Runnable() {
                        @Override
                        public void run() {
                            makeTranslation(true);
                        }
                    };
                    //делаем запрос каждые 1000мс
                    handler.postDelayed(delayedAction, 1000);
                }
            }
        });

        mFromLangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LanguagePickerActivity.class);
                startActivityForResult(i, LANGUAGE_FROM_PICK_REQUEST_CODE);
            }
        });

        mToLangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LanguagePickerActivity.class);
                startActivityForResult(i, LANGUAGE_TO_PICK_REQUEST_CODE);
            }
        });

        mSwapLangImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapTongues();
            }
        });

        mCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera();
            }
        });

        mBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBookmarked) {
                    RealmTranslationHelper
                            .changeBookmarkStatus(mRealm, false, mCurrentText, mTranslatedText);
                } else {
                    RealmTranslationHelper
                            .changeBookmarkStatus(mRealm, true, mCurrentText, mTranslatedText);
                }
            }
        });

        mClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSpace();
            }
        });

        mBookmarkButton.setEnabled(false);
        makeTranslation(false);

        //листенер для обновления состояния кнопки в зависимости от избранного
        mTranslation = RealmTranslationHelper.getCurrentTranslation(mRealm, getActivity());
        if (mTranslation != null) {
            mTranslation.addChangeListener(new RealmChangeListener<RealmModel>() {
                @Override
                public void onChange(RealmModel element) {
                    refreshBookmarkButtonState();
                }
            });
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
        if (mTranslation != null) {
            mTranslation.removeChangeListeners();
        }
    }


    public void onLaunchCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);
            } else {
                dispatchTakePictureIntent();
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //если распознавание делается впервые - запускаем камеру после выдачи разрешения
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                dispatchTakePictureIntent();
            }
        }
    }

    private File createImageFile() throws IOException {
        // создаем файл распознаваемой фотографии
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "dispatchTakePictureIntent: " + "ошибка при создании файла");
            }

            if (photoFile != null) {
                //Пришлось помучаться прежде чем нашел из за чего все крашилось на киткате :)
                Uri photoURI = (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
                        ? FileProvider.getUriForFile(getActivity(), "qbai22.com.yandextranslator.fileprovider", photoFile)
                        : Uri.fromFile(photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                currentBitmap = bitmap;
            } else {
                Toast.makeText(getActivity(), R.string.photo_error_toast_text, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == LANGUAGE_FROM_PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String label = data.getStringExtra(LanguagePickerActivity.LANGUAGE_LABLE);
                String code = data.getStringExtra(LanguagePickerActivity.LANGUAGE_CODE);
                if (!isTonguesConflicted(label)) {
                    mFromLangButton.setText(label);
                    mFromLangCode = code;
                    TranslationPreferencesUtils.setFromLanguage(getActivity(), label, code);
                    TranslationPreferencesUtils.updateLanguagePairCodes(getActivity());
                }
                //после смены языка получаем новые результаты
                makeTranslation(true);
            }
        } else if (requestCode == LANGUAGE_TO_PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String label = data.getStringExtra(LanguagePickerActivity.LANGUAGE_LABLE);
                String code = data.getStringExtra(LanguagePickerActivity.LANGUAGE_CODE);
                if (!isTonguesConflicted(label)) {
                    mToLangButton.setText(label);
                    mToLangCode = code;
                    TranslationPreferencesUtils.setToLanguage(getActivity(), label, code);
                    TranslationPreferencesUtils.updateLanguagePairCodes(getActivity());
                }
                makeTranslation(true);
            }
        }
    }

    private boolean isTonguesConflicted(String incTongue) {

        String fromBtnText = mFromLangButton.getText().toString();
        String toBtnText = mToLangButton.getText().toString();

        if (fromBtnText.equals(incTongue) || toBtnText.equals(incTongue)) {
            swapTongues();
            return true;
        }
        return false;
    }

    private void swapTongues() {
        //меняем [обозначение кнопок/коды языков/сохраненные в sharedPreferences записи] местами
        String fromBtnText = mFromLangButton.getText().toString();
        String toBtnText = mToLangButton.getText().toString();
        mFromLangButton.setText(toBtnText);
        mToLangButton.setText(fromBtnText);

        String fromCode = mFromLangCode;
        String toCode = mToLangCode;
        mFromLangCode = toCode;
        mToLangCode = fromCode;

        TranslationPreferencesUtils.setFromLanguage(getActivity(), toBtnText, toCode);
        TranslationPreferencesUtils.setToLanguage(getActivity(), fromBtnText, fromCode);
        TranslationPreferencesUtils.updateLanguagePairCodes(getActivity());

        //если перевод уже был сделан то повторяем его в обратном направлении
        if ((mCurrentText != null && mCurrentText.length() > 0)
                && mTranslatedText != null && mTranslatedText.length() > 0) {
            mEditText.setText(mTranslatedText);
        }

        makeTranslation(true);
    }


    //распознание текста со снимка камеры на основе Google Vision API
    //не распознает кириллицу :(
    public String detectText(Bitmap bitmap) {
        //включаем автораспознавание языка
        String result = "";
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity()).build();
        if (!textRecognizer.isOperational()) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Распознаватель текста не может быть запущен на этом устройстве")
                    .show();
            return "";
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> text = textRecognizer.detect(frame);

        for (int i = 0; i < text.size(); i++) {
            TextBlock item = text.valueAt(i);
            if (item != null && item.getValue() != null) {
                result += item.getValue();
            }
        }
        if (result.equals("")) {
            Toast.makeText(getActivity(), R.string.undetected_text_toast, Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private void clearSpace() {
        mScrollLinearLayout.removeAllViews();
        mTranslationTextView.setText("");
        mBookmarkButton.setVisibility(View.INVISIBLE);
        mBookmarkButton.setEnabled(false);
        mEditText.setText("");
        TranslationPreferencesUtils.setCurrentText(getActivity(), "");
    }

    private void enableBookmark() {
        refreshBookmarkButtonState();
        mBookmarkButton.setVisibility(View.VISIBLE);
        mBookmarkButton.setEnabled(true);
    }

    private void refreshBookmarkButtonState() {
        boolean isTranslationBookmarked = RealmTranslationHelper
                .isTranslationBookmarked(mRealm, mCurrentText, mTranslatedText);
        mBookmarkButton.setChecked(isTranslationBookmarked);
        mIsBookmarked = isTranslationBookmarked;
    }

    private void makeTranslation(boolean restart) {
        if (mCurrentText != null && mCurrentText.length() > 9999) {
            Toast.makeText(getActivity(), R.string.too_long_text_toast_message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mCurrentText != null && mCurrentText.length() == 0) {
            return;
        }

        LoaderManager.LoaderCallbacks<TranslatorResponse> translatorLoaderCallbacks =
                new TranslatorLoaderCallbacks();
        LoaderManager.LoaderCallbacks<DictionaryResponse> dictionaryResponseLoaderCallbacks =
                new DictionaryLoaderCallbacks();

        if (restart) {
            //если результат не закеширован и нет сети - показываем тоаст
            if (getActivity() != null && !NetworkUtils.isNetworkAvailableAndConnected(getActivity())) {
                Toast.makeText(getActivity(), R.string.network_unavailable_toast, Toast.LENGTH_SHORT).show();
                return;
            }
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            getLoaderManager().restartLoader(R.id.translator_loader_id, Bundle.EMPTY, translatorLoaderCallbacks);
            getLoaderManager().restartLoader(R.id.dictionary_loader_id, Bundle.EMPTY, dictionaryResponseLoaderCallbacks);
        } else {
            getLoaderManager().initLoader(R.id.translator_loader_id, Bundle.EMPTY, translatorLoaderCallbacks);
            getLoaderManager().initLoader(R.id.dictionary_loader_id, Bundle.EMPTY, dictionaryResponseLoaderCallbacks);
        }
    }

    private void initViewsAndVariables() {
        mFromLangButton.setText(TranslationPreferencesUtils.getFromLanguage(getActivity()).getName());
        mToLangButton.setText(TranslationPreferencesUtils.getToLanguage(getActivity()).getName());
        mFromLangCode = TranslationPreferencesUtils.getFromLanguage(getActivity()).getCode();
        mToLangCode = TranslationPreferencesUtils.getToLanguage(getActivity()).getCode();
        mCurrentText = TranslationPreferencesUtils.getCurrentText(getActivity());
    }

    public class TranslatorLoaderCallbacks
            implements LoaderManager.LoaderCallbacks<TranslatorResponse> {

        @Override
        public Loader<TranslatorResponse> onCreateLoader(int id, Bundle args) {
            return new RetrofitTranslatorLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<TranslatorResponse> loader, TranslatorResponse data) {
            mProgressBar.setVisibility(View.GONE);
            TranslationPreferencesUtils.setCurrentText(getActivity(), mCurrentText);
            if (data == null) return;

            mTranslatedText = data.getText().get(0).toLowerCase();
            mTranslationTextView.setText(mTranslatedText);
            enableBookmark();
            //проверяем наличие аналогичной записи в базе
            if (RealmTranslationHelper.checkIfExists(mRealm, mCurrentText, mTranslatedText)) {
                return;
            }
            mTranslation = RealmTranslationHelper.createTranslation(
                    mRealm,
                    mCurrentText,
                    mTranslatedText,
                    mFromLangCode,
                    mToLangCode);
            //сохраняем id последнего перевода для того чтобы обращаться к нему
            //и крепить на него листенеры при смене ориентации
            TranslationPreferencesUtils.setCurrentTranslationId(getActivity(), mTranslation.getId());
            //листенер проверяет нахождение перевода в избранном
            //перевод можно добавить в избарнное из других фрагментов
            mTranslation.addChangeListener(new RealmChangeListener<RealmModel>() {
                @Override
                public void onChange(RealmModel element) {
                    refreshBookmarkButtonState();
                }
            });
        }

        @Override
        public void onLoaderReset(Loader<TranslatorResponse> loader) {
        }
    }

    public class DictionaryLoaderCallbacks implements LoaderManager.LoaderCallbacks<DictionaryResponse> {

        @Override
        public Loader<DictionaryResponse> onCreateLoader(int id, Bundle args) {
            return new RetrofitDictionaryLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<DictionaryResponse> loader, DictionaryResponse data) {
            mScrollLinearLayout.removeAllViews();
            DictionaryViewGenerator.generateViewsAndAttach(data, mScrollLinearLayout, getActivity());
        }

        @Override
        public void onLoaderReset(Loader<DictionaryResponse> loader) {

        }
    }

}
