package com.qlkh.server.util;

/**
 * Created with IntelliJ IDEA.
 * User: dungvn3000
 * Date: 4/9/15
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public final class StringUtilsFixed {

    /**
     * Convert vietnamese unicode string to ascii string.
     *
     * @param st vietnamese unicode string.
     * @return ascii string.
     */
    public static String convertNonAscii(String st) {
        st = st.replaceAll("[eéèẽẻẹ]", "e");
        st = st.replaceAll("[êếềễểệ]", "e");

        st = st.replaceAll("[uúùũủụ]", "u");
        st = st.replaceAll("[ưứừữửự]", "u");

        st = st.replaceAll("[iíìĩỉị]", "i");

        st = st.replaceAll("[yýỷỹỳỵ]", "y");

        st = st.replaceAll("[aáàãảạ]", "a");
        st = st.replaceAll("[ăắằẵẳặ]", "a");
        st = st.replaceAll("[âấầẫẩậ]", "a");

        st = st.replaceAll("[oóòõỏọ]", "o");
        st = st.replaceAll("[ơớờỡởợ]", "o");
        st = st.replaceAll("[ôốồỗổộ]", "o");

        st = st.replaceAll("[EÉÈẼẺẸ]", "E");
        st = st.replaceAll("[ÊẾỀỄỂỆ]", "E");

        st = st.replaceAll("[UÚÙŨỦỤ]", "U");
        st = st.replaceAll("[ƯỨỪỮỬỰ]", "U");

        st = st.replaceAll("[IÍÌĨỈỊ]", "I");

        st = st.replaceAll("[YÝỲỶỸỴ]", "Y");

        st = st.replaceAll("[AÁÀÃẢẠ]", "A");
        st = st.replaceAll("[ĂẮẰẴẲẶ]", "A");
        st = st.replaceAll("[ÂẤẦẪẨẬ]", "A");

        st = st.replaceAll("[OÓÒÕỎỌ]", "O");
        st = st.replaceAll("[ÔỐỒỖỔỘ]", "O");
        st = st.replaceAll("[ƠỚỜỠỞỢ]", "O");

        st = st.replaceAll("[đ]", "d");
        st = st.replaceAll("[Đ]", "D");
        return st;
    }


}
