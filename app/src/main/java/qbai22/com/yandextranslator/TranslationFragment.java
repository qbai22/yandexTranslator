package qbai22.com.yandextranslator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;
import com.wefika.flowlayout.FlowLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import qbai22.com.yandextranslator.model.dictionaryResponce.DictionaryResponse;
import qbai22.com.yandextranslator.model.dictionaryResponce.DictionaryTranslation;
import qbai22.com.yandextranslator.model.dictionaryResponce.Synonym;
import qbai22.com.yandextranslator.model.realm.Translation;
import qbai22.com.yandextranslator.model.transatorResponce.YandexResponce;
import qbai22.com.yandextranslator.network.ApiFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by qbai on 15.03.2017.
 */


public class TranslationFragment extends Fragment {

    public final static String TAG = "Translation fragment";
    public final static String CURRENT_TEXT_ARG = "current text arg";
    public final static String TRANSLATED_TEXT_ARG = "translated text arg";
    public final static String FROM_LANG_ARG = "from lang arg";
    public final static String TO_LANG_ARG = "to lang arg";
    public final static int LANGUAGE_FROM_PICK_REQUEST_CODE = 23;
    public final static int LANGUAGE_TO_PICK_REQUEST_CODE = 22;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int REQUEST_CAMERA_PERMISSION = 888;


    @BindView(R.id.translation_window)
    EditText mEditText;
    @BindView(R.id.translation_text_view)
    TextView mTranslationTextView;
    @BindView(R.id.translation_button)
    Button mTranslationButton;
    @BindView(R.id.translation_fragment_bkmrk_button)
    SparkButton mBookmarkButton;
    @BindView(R.id.from_lang_text_view)
    TextView mFromLangTextView;
    @BindView(R.id.to_lang_text_view)
    TextView mToLangTextView;
    @BindView(R.id.language_swap_image_view)
    ImageView mSwapLangImageView;
    @BindView(R.id.camera_image_view)
    ImageView mCameraImageView;
    @BindView(R.id.scroll_linear)
    LinearLayout mScrollLinearLayout;

    String mCurrentPhotoPath;
    Bitmap currentBitmap;
    private Translation mTranslation;
    private String mCurrentText;
    private String mTranslatedText;
    private String mFromLangCode;
    private String mToLangCode;
    private boolean isBookmarked;
    private Realm mRealm;
    private String currentId; // TODO: 19.03.2017 перенести работу с базой в бэкграунд 

    public static TranslationFragment newInstance(String currentText, String translatedText,
                                                  String fromLang, String toLang) {
        Bundle args = new Bundle();
        args.putString(CURRENT_TEXT_ARG, currentText);
        args.putString(TRANSLATED_TEXT_ARG, translatedText);
        args.putString(FROM_LANG_ARG, fromLang);
        args.putString(TO_LANG_ARG, toLang);

        TranslationFragment fragment = new TranslationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 16.03.2017 поправить коды
        mFromLangCode = "en";
        mToLangCode = "ru";
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: " + "called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: called");
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

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCurrentText = s.toString();
                if (mEditText.length() == 0) {
                    clearSpace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mTranslationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeTranslation();

            }
        });

        mFromLangTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LanguagePickerActivity.class);
                startActivityForResult(i, LANGUAGE_FROM_PICK_REQUEST_CODE);
            }
        });

        mToLangTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LanguagePickerActivity.class);
                startActivityForResult(i, LANGUAGE_TO_PICK_REQUEST_CODE);
            }
        });

        mCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera();
            }
        });

        mBookmarkButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    mRealm.beginTransaction();
                    mRealm.where(Translation.class)
                            .equalTo("id", mTranslation.getId())
                            .findFirst()
                            .setBookmarked(true);
                    mRealm.commitTransaction();

                } else {
                    mRealm.beginTransaction();
                    mRealm.where(Translation.class)
                            .equalTo("id", mTranslation.getId())
                            .findFirst()
                            .setBookmarked(false);
                    mRealm.commitTransaction();
                }
            }
        });

        mBookmarkButton.setEnabled(false);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRealm.close();
    }

    private void makeTranslation() {

        if (mCurrentText.length() > 10000) {
            Toast.makeText(getActivity(), R.string.too_long_text_toast_message, Toast.LENGTH_SHORT).show();
            return;
        }

        Call<YandexResponce> call = ApiFactory.getTranslationService().getPairTranslation(mCurrentText, "ru");
        call.enqueue(new Callback<YandexResponce>() {
            @Override
            public void onResponse(Call<YandexResponce> call, Response<YandexResponce> response) {
                YandexResponce r = response.body();
                mTranslatedText = r.getText().get(0);
                Log.e(TAG, "onResponse: " + r.getCode() + " " + r.getLang() + " " + r.getText().get(0));
                updateUI();

              /*  mTranslation = RealmTranslationUtils.getUtils(mRealm).createTranslation(isBookmarked,
                        mCurrentText, mTranslatedText, mFromLangCode, mToLangCode);
                RealmTranslationUtils.getUtils(mRealm).copyToRealm(mTranslation);*/

                mRealm.beginTransaction();
                String id = UUID.randomUUID().toString();
                mTranslation = mRealm.createObject(Translation.class, id);
                mTranslation.setBookmarked(isBookmarked);
                mTranslation.setInputText(mCurrentText);
                mTranslation.setTranslatedText(mTranslatedText);
                mTranslation.setFromLangCode(mFromLangCode);
                mTranslation.setToLangCode(mToLangCode);
                mRealm.commitTransaction();

                enableBookmark();
            }

            @Override
            public void onFailure(Call<YandexResponce> call, Throwable t) {
                Log.e(TAG, "onFailure: callFailed", t);
            }
        });

        Call<DictionaryResponse> dictionaryCall = ApiFactory.getDictionaryService().getDictionary(mCurrentText, "en-ru");
        dictionaryCall.enqueue(new Callback<DictionaryResponse>() {
            @Override
            public void onResponse(Call<DictionaryResponse> call, Response<DictionaryResponse> response) {
                DictionaryResponse dicResponse = response.body();

                //динамическое заполнение ScrollView в зависимости от ответа сервера
                if (!dicResponse.getDef().isEmpty()) {
                    String wordToTranscript = dicResponse.getDef().get(0).getText();
                    String transcription = dicResponse.getDef().get(0).getTranscription();
                    setTranscriptionViews(wordToTranscript, transcription);
                }
                int responseSize = dicResponse.getDef().size();
                if (responseSize > 0) {
                    for (int i = 0; i < responseSize; i++) {
                        String partOfSpeech = dicResponse.getDef().get(i).getPartOfSpeech();
                        setPOSViews(partOfSpeech);
                        List<DictionaryTranslation> translations = dicResponse.getDef().get(i).getTranslations();
                        if (!translations.isEmpty()) {
                            for (int j = 0; j < translations.size(); j++) {

                                LinearLayout ll = getHorizontalWidthMPLL();
                                TextView tvNumber = new TextView(getActivity());
                                tvNumber.setText(String.valueOf(j + 1)); //добавляем единицу чтобы нумерация переводов начиналась с 1
                                tvNumber.setTextColor(ContextCompat.getColor(getActivity(), R.color.yandexTextGray));
                                tvNumber.setTextSize(12);
                                LinearLayout.LayoutParams tvNumberLP = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                tvNumberLP.setMargins(0, 12, 10, 0);
                                tvNumber.setLayoutParams(tvNumberLP);
                                ll.addView(tvNumber);

                                // TODO: 18.03.2017 иисусе христе
                                FlowLayout trFlowLayout = getTrFlowLayout();
                                FlowLayout.LayoutParams params = (FlowLayout.LayoutParams) trFlowLayout.getLayoutParams();
                                ll.addView(trFlowLayout);

                                DictionaryTranslation transl = translations.get(j);
                                List<Synonym> synonymList = transl.getSynonyms();
                                boolean areSynonymsAvailable = !synonymList.isEmpty();
                                String translationText = transl.getText();
                                String gender = transl.getGender();
                                //boolean параметр определяет наличие или отсутствие синонимов
                                //если синонимов нет - то перевод единственный и запятая не нужна
                                LinearLayout trItem = getTrContainer(translationText, gender, !areSynonymsAvailable);
                                trFlowLayout.addView(trItem, params);

                                if (areSynonymsAvailable) {
                                    for (int k = 0; k < synonymList.size(); k++) {
                                        String synonymText = synonymList.get(k).getText();
                                        String synonymGender = synonymList.get(k).getGender();
                                        boolean isLast = k == synonymList.size() - 1;
                                        LinearLayout synItem = getTrContainer(synonymText, synonymGender, isLast);
                                        trFlowLayout.addView(synItem, params);
                                    }
                                }
                                mScrollLinearLayout.addView(ll);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DictionaryResponse> call, Throwable t) {
            }
        });
    }

    private void updateUI() {
        mTranslationTextView.setText(mTranslatedText);
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
                Toast.makeText(getActivity(), "Фотография не была сделана", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == LANGUAGE_FROM_PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String label = data.getStringExtra("label");
                String code = data.getStringExtra("code");
                mFromLangTextView.setText(label);
                mFromLangCode = code;
            }
        } else if (requestCode == LANGUAGE_TO_PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String label = data.getStringExtra("label");
                String code = data.getStringExtra("code");
                mToLangTextView.setText(label);
                mToLangCode = code;
            }
        }
    }

    //распознание текста со снимка камеры на основе Google Vision API
    public String detectText(Bitmap bitmap) {
        //включаем автораспознавание языка
        mFromLangCode = "auto";
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
        return result;
    }

    private void setTranscriptionViews(String text, String transcription) {

        LinearLayout trLinLay = new LinearLayout(getActivity());
        trLinLay.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(0, 0, 0, 0);
        llParams.gravity = Gravity.TOP;
        trLinLay.setLayoutParams(llParams);

        TextView wordToTranscript = new TextView(getActivity());
        wordToTranscript.setTextSize(20);
        wordToTranscript.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        wordToTranscript.setText(text);
        LinearLayout.LayoutParams wordToTrLayParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        wordToTrLayParams.setMargins(0, 0, 10, 0);
        wordToTranscript.setLayoutParams(wordToTrLayParams);
        trLinLay.addView(wordToTranscript);

        mScrollLinearLayout.addView(trLinLay);

        if (transcription != null) {
            TextView trText = new TextView(getActivity());
            trText.setTextSize(20);
            trText.setTextColor(ContextCompat.getColor(getActivity(), R.color.yandexTextGray));
            trText.setText("[" + transcription + "]");
            trLinLay.addView(trText);
        }
    }

    private void setPOSViews(String partOfSpeach) {
        if (partOfSpeach != null) {
            LinearLayout.LayoutParams posTVParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            posTVParams.setMargins(0, 12, 0, 12);
            TextView posTV = new TextView(getActivity());
            posTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.yandexPOScolor));
            posTV.setTypeface(posTV.getTypeface(), Typeface.ITALIC);
            posTV.setLayoutParams(posTVParams);
            posTV.setText(partOfSpeach);
            mScrollLinearLayout.addView(posTV);
        }
    }

    private LinearLayout getHorizontalWidthMPLL() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(0, 0, 0, 12);
        linearLayout.setLayoutParams(llParams);
        return linearLayout;
    }

    //этот лэйаут нужен для связки трёх TextView перевода + род + разделитель
    //чтобы FlowLayout не разделял их на разные строки
    private LinearLayout getHorizontalWrapWrapLL() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(llParams);
        return linearLayout;
    }

    private LinearLayout getTrContainer(String translation, String gender, boolean isLast) {

        LinearLayout trTVcontainer = getHorizontalWrapWrapLL();
        TextView trTV = new TextView(getActivity());
        LinearLayout.LayoutParams trTVParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        //делаем отступ только если в ответе сервера указан род
        if (gender != null) {
            trTVParams.setMargins(0, 0, 10, 0);
        }
        trTV.setLayoutParams(trTVParams);
        trTV.setTextSize(18);
        trTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.dictDarkBlue));
        trTV.setText(translation);
        trTVcontainer.addView(trTV);

        if (gender != null) {
            TextView genderTV = new TextView(getActivity());
            genderTV.setText(gender);
            genderTV.setTextSize(18);
            trTVcontainer.addView(genderTV);
        }
        //Создаем отдельный textview для запятой
        // потому что ответ сервера не всегда будет содержать род
        //проверяем является ли результат последним чтобы понять нужна ли запятая
        if (!isLast) {
            String comma = ",";
            TextView commaTV = new TextView(getActivity());
            commaTV.setText(comma);
            commaTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.dictDarkBlue));
            trTVcontainer.addView(commaTV);
        }

        return trTVcontainer;
    }

    private FlowLayout getTrFlowLayout() {
        FlowLayout flowLayout = new FlowLayout(getActivity());
        FlowLayout.LayoutParams flowParams = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT,
                FlowLayout.LayoutParams.WRAP_CONTENT);
        flowParams.setMargins(0, 0, 10, 0);
        flowLayout.setLayoutParams(flowParams);
        return flowLayout;
    }

    private void clearSpace() {
        mScrollLinearLayout.removeAllViews();

        mTranslationTextView.setText("");
        mBookmarkButton.setVisibility(View.INVISIBLE);
        mBookmarkButton.setEnabled(false);
    }

    private void enableBookmark() {
        mBookmarkButton.setVisibility(View.VISIBLE);
        mBookmarkButton.setEnabled(true);
    }


}
