package qbai22.com.yandextranslator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageView;
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
import qbai22.com.yandextranslator.model.dictionaryResponce.DictionaryResponce;
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
    public final String APP_TAG = "Agile Translator";
    public String photoFileName = "photo.jpg";

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
    @BindView(R.id.camera_output)
    ImageView mCameraOutputImageView;
    @BindView(R.id.detect_button)
    Button detectButton;
    @BindView(R.id.transcription_text_view)
    TextView mTranscriptionTextView;

    String mCurrentPhotoPath;
    Bitmap currentBitmap;
    private Translation mTranslation;
    private String mCurrentText;
    private String mTranslatedText;
    private String mFromLangCode;
    private String mToLangCode;
    private boolean isBookmarked;


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

        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = detectText(currentBitmap);
                mEditText.setText(text);
            }
        });

        return v;
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
                mTranslation = RealmTranslationUtils.createTranslation(isBookmarked,
                        mCurrentText, mTranslatedText, mFromLangCode, mToLangCode);
                RealmTranslationUtils.copyTranslationToRealm(mTranslation);
            }

            @Override
            public void onFailure(Call<YandexResponce> call, Throwable t) {
                Log.e(TAG, "onFailure: callFailed", t);
            }
        });

        Call<DictionaryResponce> dictionaryCall = ApiFactory.getDictionaryService().getDictionary(mCurrentText, "en-ru");
        dictionaryCall.enqueue(new Callback<DictionaryResponce>() {
            @Override
            public void onResponse(Call<DictionaryResponce> call, Response<DictionaryResponce> response) {
                DictionaryResponce responce = response.body();
                mTranscriptionTextView.setText(responce.getDef().get(0).getTranscription());
            }

            @Override
            public void onFailure(Call<DictionaryResponce> call, Throwable t) {

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
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
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

                mCameraOutputImageView.setImageBitmap(bitmap);
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

    //Метод распознает текст со снимка камеры на основе Google Vision API
    public String detectText(Bitmap bitmap) {

        String result = "";
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity()).build();
        if (!textRecognizer.isOperational()) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Text recognizer не может быть запущен на этом устройстве :(")
                    .show();
            return "";
        }

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> text = textRecognizer.detect(frame);

        for (int i = 0; i < text.size(); ++i) {
            TextBlock item = text.valueAt(i);
            if (item != null && item.getValue() != null) {
                result += item.getValue();
            }
        }
        return result;
    }
}
