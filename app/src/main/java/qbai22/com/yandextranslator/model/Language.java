package qbai22.com.yandextranslator.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import qbai22.com.yandextranslator.R;

/*
 * Created by Vladimir Kraev
 */

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

    public static List<Language> getAllLanguages(Context context) {

        List<Language> result = new ArrayList<>();
        result.add(new Language(context.getString(R.string.az_l), "az"));
        result.add(new Language(context.getString(R.string.sq_l), "sq"));
        result.add(new Language(context.getString(R.string.am_l), "am"));
        result.add(new Language(context.getString(R.string.en_l), "en"));
        result.add(new Language(context.getString(R.string.ar_l), "ar"));
        result.add(new Language(context.getString(R.string.hy_l), "hy"));
        result.add(new Language(context.getString(R.string.af_l), "af"));
        result.add(new Language(context.getString(R.string.eu_l), "eu"));
        result.add(new Language(context.getString(R.string.ba_l), "ba"));
        result.add(new Language(context.getString(R.string.be_l), "be"));
        result.add(new Language(context.getString(R.string.bn_l), "bn"));
        result.add(new Language(context.getString(R.string.bg_l), "bg"));
        result.add(new Language(context.getString(R.string.bs_l), "bs"));
        result.add(new Language(context.getString(R.string.cy_l), "cy"));
        result.add(new Language(context.getString(R.string.hu_l), "hu"));
        result.add(new Language(context.getString(R.string.vi_l), "vi"));
        result.add(new Language(context.getString(R.string.ht_l), "ht"));
        result.add(new Language(context.getString(R.string.gl_l), "gl"));
        result.add(new Language(context.getString(R.string.nl_l), "nl"));
        result.add(new Language(context.getString(R.string.mrj_l), "mrj"));
        result.add(new Language(context.getString(R.string.el_l), "el"));
        result.add(new Language(context.getString(R.string.ka_l), "ka"));
        result.add(new Language(context.getString(R.string.gu_l), "gu"));
        result.add(new Language(context.getString(R.string.da_l), "da"));
        result.add(new Language(context.getString(R.string.he_l), "he"));
        result.add(new Language(context.getString(R.string.yi_l), "yi"));
        result.add(new Language(context.getString(R.string.id_l), "id"));
        result.add(new Language(context.getString(R.string.ga_l), "ga"));
        result.add(new Language(context.getString(R.string.it_l), "it"));
        result.add(new Language(context.getString(R.string.is_l), "is"));
        result.add(new Language(context.getString(R.string.es_l), "es"));
        result.add(new Language(context.getString(R.string.kk_l), "kk"));
        result.add(new Language(context.getString(R.string.kn_l), "kn"));
        result.add(new Language(context.getString(R.string.ca_l), "ca"));
        result.add(new Language(context.getString(R.string.ky_l), "ky"));
        result.add(new Language(context.getString(R.string.zh_l), "zh"));
        result.add(new Language(context.getString(R.string.ko_l), "ko"));
        result.add(new Language(context.getString(R.string.xh_l), "xh"));
        result.add(new Language(context.getString(R.string.la_l), "la"));
        result.add(new Language(context.getString(R.string.lv_l), "lv"));
        result.add(new Language(context.getString(R.string.lt_l), "lt"));
        result.add(new Language(context.getString(R.string.lb_l), "lb"));
        result.add(new Language(context.getString(R.string.mg_l), "mg"));
        result.add(new Language(context.getString(R.string.ms_l), "ms"));
        result.add(new Language(context.getString(R.string.ml_l), "ml"));
        result.add(new Language(context.getString(R.string.mt_l), "mt"));
        result.add(new Language(context.getString(R.string.mk_l), "mk"));
        result.add(new Language(context.getString(R.string.mi_l), "mi"));
        result.add(new Language(context.getString(R.string.mr_l), "mr"));
        result.add(new Language(context.getString(R.string.mhr_l), "mhr"));
        result.add(new Language(context.getString(R.string.mn_l), "mn"));
        result.add(new Language(context.getString(R.string.de_l), "de"));
        result.add(new Language(context.getString(R.string.ne_l), "ne"));
        result.add(new Language(context.getString(R.string.no_l), "no"));
        result.add(new Language(context.getString(R.string.pa_l), "pa"));
        result.add(new Language(context.getString(R.string.pap_l), "pap"));
        result.add(new Language(context.getString(R.string.fa_l), "fa"));
        result.add(new Language(context.getString(R.string.pl_l), "pl"));
        result.add(new Language(context.getString(R.string.pt_l), "pt"));
        result.add(new Language(context.getString(R.string.ro_l), "ro"));
        result.add(new Language(context.getString(R.string.ru_l), "ru"));
        result.add(new Language(context.getString(R.string.ceb_l), "ceb"));
        result.add(new Language(context.getString(R.string.sr_l), "sr"));
        result.add(new Language(context.getString(R.string.si_l), "si"));
        result.add(new Language(context.getString(R.string.sk_l), "sk"));
        result.add(new Language(context.getString(R.string.sl_l), "sl"));
        result.add(new Language(context.getString(R.string.sw_l), "sw"));
        result.add(new Language(context.getString(R.string.su_l), "su"));
        result.add(new Language(context.getString(R.string.tg_l), "tg"));
        result.add(new Language(context.getString(R.string.th_l), "th"));
        result.add(new Language(context.getString(R.string.tl_l), "tl"));
        result.add(new Language(context.getString(R.string.ta_l), "ta"));
        result.add(new Language(context.getString(R.string.tt_l), "tt"));
        result.add(new Language(context.getString(R.string.te_l), "te"));
        result.add(new Language(context.getString(R.string.tr_l), "tr"));
        result.add(new Language(context.getString(R.string.udm_l), "udm"));
        result.add(new Language(context.getString(R.string.uz_l), "uz"));
        result.add(new Language(context.getString(R.string.uk_l), "uk"));
        result.add(new Language(context.getString(R.string.ur_l), "ur"));
        result.add(new Language(context.getString(R.string.fi_l), "fi"));
        result.add(new Language(context.getString(R.string.fr_l), "fr"));
        result.add(new Language(context.getString(R.string.hi_l), "hi"));
        result.add(new Language(context.getString(R.string.hr_l), "hr"));
        result.add(new Language(context.getString(R.string.cs_l), "cs"));
        result.add(new Language(context.getString(R.string.sv_l), "sv"));
        result.add(new Language(context.getString(R.string.gd_l), "gd"));
        result.add(new Language(context.getString(R.string.et_l), "et"));
        result.add(new Language(context.getString(R.string.eo_l), "eo"));
        result.add(new Language(context.getString(R.string.jv_l), "jv"));
        result.add(new Language(context.getString(R.string.ja_l), "ja"));

        return result;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}
