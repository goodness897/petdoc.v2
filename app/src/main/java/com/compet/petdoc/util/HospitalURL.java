package com.compet.petdoc.util;

/**
 * Created by Mu on 2016-12-01.
 */

public class HospitalURL {

    private final static String AUTH_KEY = "536b784f66676f6f313032674544424a";

    public static final String[] URL = new String[] {"http://openAPI.gangnam.go.kr:8088/" + AUTH_KEY
                                                     + "/json/GnAnimalHospital/%s/%s",

                                                     "http://openAPI.gd.go.kr:8088/" + AUTH_KEY
                                                                                       + "/json/GdAnimalHospital/%s/%s",
                                                     "http://openAPI.gangbuk.go.kr:8088/" + AUTH_KEY
                                                                                                                         + "/json/GbAnimalHospital/%s/%s",
                                                     "http://openAPI.gangseo.seoul.kr:8088/" + AUTH_KEY
                                                                                                                                                           + "/json/GangseoAnimalHospital/%s/%s",
                                                     "http://openAPI.gwanak.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                  + "/json/GaAnimalHospital/%s/%s",
                                                     "http://openAPI.gwangjin.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                    + "/json/GwangjinAnimalHospital/%s/%s",
                                                     "http://openAPI.guro.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                            + "/json/GuroAnimalHospital/%s/%s",
                                                     "http://openAPI.geumcheon.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                + "/json/GeumcheonAnimalHospital/%s/%s",
                                                     "http://openAPI.nowon.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                         + "/json/NwAnimalHospital/%s/%s",
                                                     "http://openAPI.dobong.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                           + "/json/DobongAnimalHospital/%s/%s",
                                                     "http://openAPI.ddm.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                 + "/json/DongdaemoonAnimalHospital/%s/%s",
                                                     "http://openAPI.dongjak.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                            + "/json/DjAnimalHospital/%s/%s",
                                                     "http://openAPI.mapo.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              + "/json/MpAnimalHospital/%s/%s",
                                                     "http://openAPI.sdm.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                + "/json/SeodaemunAnimalHospital/%s/%s",
                                                     "http://openAPI.seocho.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         + "/json/ScAnimalHospital/%s/%s",
                                                     "http://openAPI.sd.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           + "/json/SdAnimalHospital/%s/%s",
                                                     "http://openAPI.sb.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             + "/json/SbAnimalHospital/%s/%s",
                                                     "http://openAPI.songpa.seoul.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               + "/json/SpAnimalHospital/%s/%s",
                                                     "http://openAPI.yangcheon.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 + "/json/YcAnimalHospital/%s/%s",
                                                     "http://openAPI.ydp.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   + "/json/YdpAnimalHospital/%s/%s",
                                                     "http://openAPI.yongsan.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      + "/json/YsAnimalHospital/%s/%s",
                                                     "http://openAPI.ep.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        + "/json/EpAnimalHospital/%s/%s",
                                                     "http://openAPI.jongno.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          + "/json/JongnoAnimalHospital/%s/%s",
                                                     "http://openAPI.junggu.seoul.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                + "/json/JungguAnimalHospital/%s/%s",
                                                     "http://openAPI.jungnang.go.kr:8088/" + AUTH_KEY
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      + "/json/JungnangAnimalHospital/%s/%s"

    };

    private String GANG_NAM = "http://openAPI.gangnam.go.kr:8088/" + AUTH_KEY + "/json/GnAnimalHospital/%s/%s";

    private final String GANG_DONG = "http://openAPI.gd.go.kr:8088/" + AUTH_KEY + "/json/GdAnimalHospital/%s/%s";

    private final String GANG_BUK = "http://openAPI.gangbuk.go.kr:8088/" + AUTH_KEY + "/json/GbAnimalHospital/%s/%s";

    private final String GANG_SEO =
                                  "http://openAPI.gangseo.go.kr:8088/" + AUTH_KEY + "/json/GangseoAnimalHospital/%s/%s";

    private final String GWAN_AK = "http://openAPI.gwanak.go.kr:8088/" + AUTH_KEY + "/json/GaAnimalHospital/%s/%s";

    private final String GWANG_JIN = "http://openAPI.gwangjin.go.kr:8088/" + AUTH_KEY
                                     + "/json/GwangjinAnimalHospital/%s/%s";

    private final String GU_RO = "http://openAPI.guro.go.kr:8088/" + AUTH_KEY + "/json/GuroAnimalHospital/%s/%s";

    private final String GEUM_CHEON = "http://openAPI.geumcheon.go.kr:8088/" + AUTH_KEY
                                      + "/json/GeumcheonAnimalHospital/%s/%s";

    private final String NO_WON = "http://openAPI.nowon.go.kr:8088/" + AUTH_KEY + "/json/NwAnimalHospital/%s/%s";

    private final String DO_BONG = "http://openAPI.dobong.go.kr:8088/" + AUTH_KEY + "/json/DobongAnimalHospital/%s/%s";

    private final String DONG_DAE_MOON = "http://openAPI.ddm.go.kr:8088/" + AUTH_KEY
                                         + "/json/DongdaemoonAnimalHospital/%s/%s";

    private final String DONG_JAK = "http://openAPI.dongjak.go.kr:8088/" + AUTH_KEY + "/json/DjAnimalHospital/%s/%s";

    private final String MA_PO = "http://openAPI.mapo.go.kr:8088/" + AUTH_KEY + "/json/MpAnimalHospital/%s/%s";

    private final String SEO_DAE_MUN = "http://openAPI.sdm.go.kr:8088/" + AUTH_KEY
                                       + "/json/SeodaemunAnimalHospital/%s/%s";

    private final String SEO_CHO = "http://openAPI.seocho.go.kr:8088/" + AUTH_KEY + "/json/ScAnimalHospital/%s/%s";

    private final String SEONG_DONG = "http://openAPI.sd.go.kr:8088/" + AUTH_KEY + "/json/SdAnimalHospital/%s/%s";

    private final String SEONG_BUK = "http://openAPI.sb.go.kr:8088/" + AUTH_KEY + "/json/SbAnimalHospital/%s/%s";

    private final String SONG_PA = "http://openAPI.songpa.go.kr:8088/" + AUTH_KEY + "/json/SpAnimalHospital/%s/%s";

    private final String YANG_CHEON =
                                    "http://openAPI.yangcheon.go.kr:8088/" + AUTH_KEY + "/json/YcAnimalHospital/%s/%s";

    private final String YDP = "http://openAPI.ydp.go.kr:8088/" + AUTH_KEY + "/json/YdpAnimalHospital/%s/%s";

    private final String YONGSAN = "http://openAPI.yongsan.go.kr:8088/" + AUTH_KEY + "/json/YsAnimalHospital/%s/%s";

    private final String EP = "http://openAPI.ep.go.kr:8088/" + AUTH_KEY + "/json/EpAnimalHospital/%s/%s";

    private final String JONGNO = "http://openAPI.jongno.go.kr:8088/" + AUTH_KEY + "/json/JongnoAnimalHospital/%s/%s";

    private final String JUNG_GU = "http://openAPI.junggu.go.kr:8088/" + AUTH_KEY + "/json/JungguAnimalHospital/%s/%s";

    private final String JUNG_NANG = "http://openAPI.jungnang.go.kr:8088/" + AUTH_KEY
                                     + "/json/JungnangAnimalHospital/%s/%s";
}
