package qbai22.com.yandextranslator.model;

import java.util.ArrayList;
import java.util.List;

public class Language {

    String name;
    String code;

    public Language(String n, String c) {
        name = n;
        code = c;
    }

    //метод понадобился потому что языки копировал из документации словаря
    //а они там с маленькой буквы -_-
    public Language capitalizeLabel(){
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return this;
    }

    public static List<Language> getAllLanguages() {
        List<Language> result = new ArrayList<>();
        result.add(new Language("азербайджанский", "az"));
        result.add(new Language("албанский", "sq"));
        result.add(new Language("амхарский", "am"));
        result.add(new Language("английский", "en"));
        result.add(new Language("арабский", "ar"));
        result.add(new Language("армянский", "hy"));
        result.add(new Language("африкаанс", "af"));
        result.add(new Language("баскский", "eu"));
        result.add(new Language("башкирский", "ba"));
        result.add(new Language("белорусский", "be"));
        result.add(new Language("бенгальский", "bn"));
        result.add(new Language("болгарский", "bg"));
        result.add(new Language("боснийский", "bs"));
        result.add(new Language("валлийский", "cy"));
        result.add(new Language("венгерский", "hu"));
        result.add(new Language("вьетнамский", "vi"));
        result.add(new Language("гаитянский", "ht"));
        result.add(new Language("галисийский", "gl"));
        result.add(new Language("голландский", "nl"));
        result.add(new Language("горномарийский", "mrj"));
        result.add(new Language("греческий", "el"));
        result.add(new Language("грузинский", "ka"));
        result.add(new Language("гуджарати", "gu"));
        result.add(new Language("датский", "da"));
        result.add(new Language("иврит", "he"));
        result.add(new Language("идиш", "yi"));
        result.add(new Language("индонезийский", "id"));
        result.add(new Language("ирландский", "ga"));
        result.add(new Language("итальянский", "it"));
        result.add(new Language("исландский", "is"));
        result.add(new Language("испанский", "es"));
        result.add(new Language("казахский", "kk"));
        result.add(new Language("каннада", "kn"));
        result.add(new Language("каталанский", "ca"));
        result.add(new Language("киргизский", "ky"));
        result.add(new Language("китайский", "zh"));
        result.add(new Language("корейский", "ko"));
        result.add(new Language("коса", "xh"));
        result.add(new Language("латынь", "la"));
        result.add(new Language("латышский", "lv"));
        result.add(new Language("литовский", "lt"));
        result.add(new Language("люксембургский", "lb"));
        result.add(new Language("малагасийский", "mg"));
        result.add(new Language("малайский", "ms"));
        result.add(new Language("малаялам", "ml"));
        result.add(new Language("мальтийский", "mt"));
        result.add(new Language("македонский", "mk"));
        result.add(new Language("маори", "mi"));
        result.add(new Language("маратхи", "mr"));
        result.add(new Language("марийский", "mhr"));
        result.add(new Language("монгольский", "mn"));
        result.add(new Language("немецкий", "de"));
        result.add(new Language("непальский", "ne"));
        result.add(new Language("норвежский", "no"));
        result.add(new Language("панджаби", "pa"));
        result.add(new Language("папьяменто", "pap"));
        result.add(new Language("персидский", "fa"));
        result.add(new Language("польский", "pl"));
        result.add(new Language("португальский", "pt"));
        result.add(new Language("румынский", "ro"));
        result.add(new Language("русский", "ru"));
        result.add(new Language("себуанский", "ceb"));
        result.add(new Language("сербский", "sr"));
        result.add(new Language("сингальский", "si"));
        result.add(new Language("словацкий", "sk"));
        result.add(new Language("словенский", "sl"));
        result.add(new Language("суахили", "sw"));
        result.add(new Language("сунданский", "su"));
        result.add(new Language("таджикский", "tg"));
        result.add(new Language("тайский", "th"));
        result.add(new Language("тагальский", "tl"));
        result.add(new Language("тамильский", "ta"));
        result.add(new Language("татарский", "tt"));
        result.add(new Language("телугу", "te"));
        result.add(new Language("турецкий", "tr"));
        result.add(new Language("удмуртский", "udm"));
        result.add(new Language("узбекский", "uz"));
        result.add(new Language("украинский", "uk"));
        result.add(new Language("урду", "ur"));
        result.add(new Language("финский", "fi"));
        result.add(new Language("французский", "fr"));
        result.add(new Language("хинди", "hi"));
        result.add(new Language("хорватский", "hr"));
        result.add(new Language("чешский", "cs"));
        result.add(new Language("шведский", "sv"));
        result.add(new Language("шотландский", "gd"));
        result.add(new Language("эстонский", "et"));
        result.add(new Language("эсперанто", "eo"));
        result.add(new Language("яванский", "jv"));
        result.add(new Language("японский", "ja"));

        return result;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}
