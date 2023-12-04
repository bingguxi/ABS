package kopo.poly.util;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;

public class KomoranUtil {

    // Komoran 형태소 분석 사용 Util
    public class KomoranUtils {
        private static final Logger log = LoggerFactory.getLogger(KomoranUtil.class);
        private void KomoranUtil(){}

        public static Komoran getInstance() {
            return KomoranInstance.instance;
        }

        private static class KomoranInstance {
            public static final Komoran instance = new Komoran(DEFAULT_MODEL.FULL);
        }
    }

}
