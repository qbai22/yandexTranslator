package qbai22.com.yandextranslator.fragments.translation;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wefika.flowlayout.FlowLayout;

import java.util.List;

import qbai22.com.yandextranslator.R;
import qbai22.com.yandextranslator.model.dictionaryResponce.DictionaryResponse;
import qbai22.com.yandextranslator.model.dictionaryResponce.DictionaryTranslation;
import qbai22.com.yandextranslator.model.dictionaryResponce.Example;
import qbai22.com.yandextranslator.model.dictionaryResponce.Meaning;
import qbai22.com.yandextranslator.model.dictionaryResponce.Synonym;


public class DictionaryViewGenerator {

    private static final int LARGE_FONT_SIZE = 20;
    private static final int MEDIUM_FONT_SIZE = 16;
    private static final int SMALL_FONT_SIZE = 12;

    /*
    магический класс динамической генерации View элементов
    на основании DictionaryResponse (немного поседел пока его писал *_*)
    ушло некоторое время понять как работает FlowLayout вам я скажу сразу -
    чтобы применить LayoutParams их нужно добавлять к каждому чаилд вью
    в методе addView и никак иначе!
     */

    public static void generateViewsAndAttach(DictionaryResponse dicResponse,
                                              LinearLayout container,
                                              Context context) {
        //динамическое заполнение ScrollView в зависимости от ответа сервера
        if (dicResponse == null) return;
        if (!dicResponse.getDef().isEmpty()) {
            String wordToTranscript = dicResponse.getDef().get(0).getText();
            String transcription = dicResponse.getDef().get(0).getTranscription();
            setTranscriptionViews(wordToTranscript, transcription, container, context);
        }

        int responseSize = dicResponse.getDef().size();
        if (responseSize > 0) {
            for (int i = 0; i < responseSize; i++) {
                String partOfSpeech = dicResponse.getDef().get(i).getPartOfSpeech();
                setPOSViews(partOfSpeech, context, container);
                List<DictionaryTranslation> translations = dicResponse.getDef().get(i).getTranslations();
                if (!translations.isEmpty()) {
                    for (int j = 0; j < translations.size(); j++) {

                        LinearLayout ll = getHorizontalWidthMPLL(context);
                        TextView tvNumber = new TextView(context);
                        tvNumber.setText(String.valueOf(j + 1)); //добавляем единицу чтобы нумерация переводов начиналась с 1
                        tvNumber.setTextColor(ContextCompat.getColor(context, R.color.yandexTextGray));
                        tvNumber.setTextSize(SMALL_FONT_SIZE);
                        LinearLayout.LayoutParams tvNumberLP = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        tvNumberLP.setMargins(0, 12, 10, 0);
                        tvNumber.setLayoutParams(tvNumberLP);
                        ll.addView(tvNumber);

                        FlowLayout trFlowLayout = getTrFlowLayout(context);
                        FlowLayout.LayoutParams params = (FlowLayout.LayoutParams) trFlowLayout.getLayoutParams();
                        ll.addView(trFlowLayout);

                        DictionaryTranslation transl = translations.get(j);

                        List<Synonym> synonymList = transl.getSynonyms();
                        boolean areSynonymsAvailable = !synonymList.isEmpty();
                        String translationText = transl.getText();
                        String gender = transl.getGender();
                        //boolean параметр определяет наличие или отсутствие синонимов
                        //если синонимов нет - то перевод единственный и запятая не нужна
                        LinearLayout trItem = getTrContainer(translationText, gender, !areSynonymsAvailable, context);
                        trFlowLayout.addView(trItem, params);

                        if (areSynonymsAvailable) {
                            for (int k = 0; k < synonymList.size(); k++) {
                                String synonymText = synonymList.get(k).getText();
                                String synonymGender = synonymList.get(k).getGender();
                                boolean isLast = (k == synonymList.size() - 1);
                                //для каждого перевода делаем линеар лэйаут котороый популируем
                                //внутри флоу лэйаута
                                LinearLayout synItem = getTrContainer(synonymText, synonymGender, isLast, context);
                                trFlowLayout.addView(synItem, params);
                            }
                        }
                        container.addView(ll);

                        List<Meaning> meaningList = transl.getMeanings();
                        boolean isMeaningsAvailable = !meaningList.isEmpty();
                        if (isMeaningsAvailable) {
                            FlowLayout meaningFlowLayout = new FlowLayout(context);
                            for (int m = 0; m < meaningList.size(); m++) {
                                boolean isFirst = (m == 0);
                                boolean isLast = (m == meaningList.size() - 1);
                                String meaningText = meaningList.get(m).getText();
                                TextView meaningTextView = generateMeaningTextView(
                                        context,
                                        meaningText,
                                        isFirst,
                                        isLast);
                                meaningFlowLayout.addView(meaningTextView);
                            }
                            //добавляем значения в наш скролящийся контейнер
                            container.addView(meaningFlowLayout);
                        }

                        List<Example> examplesList = transl.getExamples();
                        boolean isExamplesAvailable = !examplesList.isEmpty();
                        if (isExamplesAvailable) {
                            FlowLayout exampleFlowLayout = new FlowLayout(context);
                            FlowLayout.LayoutParams exampleParams = new FlowLayout.LayoutParams(
                                    FlowLayout.LayoutParams.WRAP_CONTENT,
                                    FlowLayout.LayoutParams.WRAP_CONTENT);
                            exampleParams.setMargins(40, 0, 60, 0);
                            exampleFlowLayout.setLayoutParams(exampleParams);
                            for (int e = 0; e < examplesList.size(); e++) {
                                String originalPhrase = examplesList.get(e).getExampleText();
                                String translation = examplesList.get(e).getExampleTranslations().get(0).getText();
                                TextView exampleTextView = generateExampleTextView(
                                        context,
                                        originalPhrase,
                                        translation);
                                exampleFlowLayout.addView(exampleTextView, exampleParams);
                            }
                            container.addView(exampleFlowLayout);
                        }
                    }
                }
            }
        }
    }

    private static void setTranscriptionViews(String text,
                                              String transcription,
                                              LinearLayout container,
                                              Context context) {

        LinearLayout trLinLay = new LinearLayout(context);
        trLinLay.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(0, 0, 0, 0);
        llParams.gravity = Gravity.TOP;
        trLinLay.setLayoutParams(llParams);

        TextView wordToTranscript = new TextView(context);
        wordToTranscript.setTextSize(LARGE_FONT_SIZE);
        wordToTranscript.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        wordToTranscript.setText(text);
        LinearLayout.LayoutParams wordToTrLayParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        wordToTrLayParams.setMargins(0, 0, 10, 0);
        wordToTranscript.setLayoutParams(wordToTrLayParams);
        trLinLay.addView(wordToTranscript);

        container.addView(trLinLay);

        if (transcription != null) {
            TextView trText = new TextView(context);
            trText.setTextSize(LARGE_FONT_SIZE);
            trText.setTextColor(ContextCompat.getColor(context, R.color.yandexTextGray));
            trText.setText("[" + transcription + "]");
            trLinLay.addView(trText);
        }
    }

    private static TextView generateMeaningTextView(Context context,
                                                    String inputText,
                                                    boolean isFirst,
                                                    boolean isLast) {

        FlowLayout.LayoutParams meaningLayoutParams = new FlowLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        StringBuilder resultBuilder = new StringBuilder();

        TextView meaningTextView = new TextView(context);
        meaningTextView.setTextColor(ContextCompat.getColor(context, R.color.dictBrown));
        meaningTextView.setTextSize(MEDIUM_FONT_SIZE);
        if (isFirst) {
            resultBuilder.append("(");

        }
        resultBuilder.append(inputText);

        if (!isLast) {
            //если слово не последнее, то добавляем запятую и делаем отступ
            resultBuilder.append(", ");
        }

        if (isLast) {
            resultBuilder.append(")");
        }

        meaningTextView.setLayoutParams(meaningLayoutParams);
        meaningTextView.setText(resultBuilder.toString());
        return meaningTextView;
    }

    private static TextView generateExampleTextView(Context context,
                                                    String originalPhrase,
                                                    String translation) {

        FlowLayout.LayoutParams exampleLayoutParams = new FlowLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView exampleTextView = new TextView(context);
        exampleTextView.setTextColor(ContextCompat.getColor(context, R.color.dictLightBlue));
        exampleTextView.setTextSize(MEDIUM_FONT_SIZE);
        exampleTextView.setTypeface(exampleTextView.getTypeface(), Typeface.ITALIC);
        exampleTextView.setLayoutParams(exampleLayoutParams);

        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(originalPhrase).append(" \u2014 ").append(translation);
        exampleTextView.setText(resultBuilder.toString());

        return exampleTextView;
    }

    private static void setPOSViews(String partOfSpeech, Context context, LinearLayout container) {
        if (partOfSpeech != null) {
            LinearLayout.LayoutParams posTVParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            posTVParams.setMargins(0, 12, 0, 12);
            TextView posTV = new TextView(context);
            posTV.setTextColor(ContextCompat.getColor(context, R.color.yandexPOScolor));
            posTV.setTypeface(posTV.getTypeface(), Typeface.ITALIC);
            posTV.setLayoutParams(posTVParams);
            posTV.setText(partOfSpeech);
            container.addView(posTV);
        }
    }

    private static LinearLayout getHorizontalWidthMPLL(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(0, 0, 0, 0);
        linearLayout.setLayoutParams(llParams);
        return linearLayout;
    }

    private static LinearLayout getTrContainer(String translation, String gender, boolean isLast, Context context) {

        LinearLayout trTVcontainer = getHorizontalWrapWrapLL(context);
        TextView trTV = new TextView(context);
        LinearLayout.LayoutParams trTVParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        //делаем отступ только если в ответе сервера указан род
        if (gender != null) {
            trTVParams.setMargins(0, 0, 10, 0);
        }
        trTV.setLayoutParams(trTVParams);
        trTV.setTextSize(MEDIUM_FONT_SIZE);
        trTV.setTextColor(ContextCompat.getColor(context, R.color.dictDarkBlue));
        trTV.setText(translation);
        trTVcontainer.addView(trTV);

        if (gender != null) {
            TextView genderTV = new TextView(context);
            genderTV.setText(gender);
            genderTV.setTextSize(MEDIUM_FONT_SIZE);
            trTVcontainer.addView(genderTV);
        }
        //Создаем отдельный textview для запятой
        // потому что ответ сервера не всегда будет содержать род
        //проверяем является ли результат последним чтобы понять нужна ли запятая
        if (!isLast) {
            String comma = ",";
            TextView commaTV = new TextView(context);
            commaTV.setText(comma);
            commaTV.setTextColor(ContextCompat.getColor(context, R.color.dictDarkBlue));
            trTVcontainer.addView(commaTV);
        }

        return trTVcontainer;
    }

    private static FlowLayout getTrFlowLayout(Context context) {
        FlowLayout flowLayout = new FlowLayout(context);
        FlowLayout.LayoutParams flowParams = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT,
                FlowLayout.LayoutParams.WRAP_CONTENT);
        flowParams.setMargins(0, 0, 10, 0);
        flowLayout.setLayoutParams(flowParams);
        return flowLayout;
    }

    //этот лэйаут нужен для связки трёх TextView перевода + род + разделитель
    //чтобы FlowLayout не разделял их на разные строки
    private static LinearLayout getHorizontalWrapWrapLL(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(llParams);
        return linearLayout;
    }
}
