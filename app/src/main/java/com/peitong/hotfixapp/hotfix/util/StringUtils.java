package com.peitong.hotfixapp.hotfix.util;

import android.text.TextUtils;

//import com.tencent.bugly.proguard.T;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * Created by peitong.
 */
public final class StringUtils {

    public static final int INDEX_NOT_FOUND = -1;

    public static final String EMPTY = "";

    private StringUtils() {
    }

    /**
     * 判断字符串是否为null或者""
     *
     * @param str 需要判断是否为null或者为""的字符串
     * @return 是否为null或者为空
     */
    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 将字符串以下划线连接
     *
     * @param args 需要拼接在一起的字符串
     * @return 用下划线拼接在一起后的字符串
     */
    public static String appendByUnderline(String... args) {
        StringBuffer buffer = new StringBuffer();
        for (String item : args) {
            buffer.append(item).append("_");
        }
        // 去掉最后一个下划线
        return buffer.substring(0, buffer.length() - 1);
    }

    /**
     * 字符串拼接
     *
     * @param divider
     * @param args
     * @return
     */
    public static String append(String divider, Object... args) {
        if (TextUtils.isEmpty(divider)) {
            throw new NullPointerException("[append]divider cannot be null");
        }
        if (args.length == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (Object item : args) {
            if (item == null) {
                continue;
            }
            if (TextUtils.isEmpty(item.toString())) {
                continue;
            }
            buffer.append(item).append(divider);
        }
        if (buffer.length() == 0) {
            return "";
        }
        return buffer.substring(0, buffer.length() - 1);
    }

    /**
     * 字符串拼接
     *
     * @param args
     * @return
     */
    public static String appendWithOutDivider(Object... args) {
        if (args.length == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (Object item : args) {
            if (item == null) {
                continue;
            }
            if (TextUtils.isEmpty(item.toString())) {
                continue;
            }
            buffer.append(item);
        }
        if (buffer.length() == 0) {
            return "";
        }
        return buffer.toString();
    }

    /**
     * 拼接字符串
     *
     * @param source
     * @param value
     * @param mark，间隔符
     * @return
     */
    public static String appondStr(String source, String value, String mark) {
        if (StringUtils.isNullOrEmpty(value))
            return source;

        if (!isNullOrEmpty(mark)) {
            if (!StringUtils.isNullOrEmpty(source)) {
                source = source + mark + value;
            } else {
                source = value;
            }
        } else {
            if (!StringUtils.isNullOrEmpty(source)) {
                source = source + value;
            } else {
                source = value;
            }
        }

        return source;
    }

    /**
     * 合并字符串，以给定的flag间隔
     *
     * @param arr  字符串数组
     * @param flag 间隔符
     * @return 合并后的字符串
     * @author 段屈直
     */
    public static String join(ArrayList<String> arr, String flag) {
        StringBuffer strBuff = new StringBuffer();

        for (int i = 0, len = arr.size(); i < len; i++) {
            strBuff.append(String.valueOf(arr.get(i)));
            if (i < len - 1) {
                strBuff.append(flag);
            }
        }
        return strBuff.toString();
    }

    /**
     * 用指定字符替换字符串中所有指定位置的字符，返回该字符串
     *
     * @param s         要替换的字符串
     * @param positions 要替换的位置
     * @param replace   替换字符
     * @return 替换后的字符串
     * @author 李瑞东
     */
    public static String replaceCharAtPosition(String s, int[] positions, char replace) {
        char[] chars = s.toCharArray();
        int charLength = chars.length;
        int strLength = positions.length;
        for (int i = 0; i < strLength; i++) {
            if (charLength - 1 >= positions[i]) {
                chars[positions[i]] = replace;
            }
        }
        return new String(chars);
    }

    /**
     * 删除字符串前后的空格和换行
     *
     * @author 段屈直
     * @param str
     *            需要改变的字符串
     * @return 改变后的字符串
     */
    //    public static String removeSpaceAndWrap(String str) {
    //        if (isNullOrEmpty(str)) {
    //            return "";
    //        }
    //
    //        StringBuffer stringBuffer = new StringBuffer();
    //        char[] chars = str.toCharArray();
    //        for (int i = 0; i < chars.length; i++) {
    //            if(char[i] != " ") {
    //
    //            }
    //        }
    //    }

    /**
     * @author tanzhenxing
     * <p/>
     * TODO手机或固话号码正则表达式
     */
    public static boolean isMobileNO(String mobiles) {
        //手机
        boolean IS_MOBEILPHONE = false;
//        String phoneExp= "(^(0\\d{2,3})?-?([2-9]\\d{6,7})(-\\d{1,5})?$)|(^((\\(\\d{3}\\))|(\\d{0}))?(13|14|15|17|18)\\d{9}$)|(^(400|800)\\d{7}(-\\d{1,6})?$)|(^(95013)\\d{6,8}$)";
        String phoneExp = "(^((0\\d{2,3})-)?([2-9]\\d{6,7})(-\\d{1,5})?$)|(^(086)?(13|14|15|17|18)\\d{9}$)|(^(400|800)\\d{7}(-\\d{1,6})?$)|(^(95013)\\d{6,8}$)";
        try {
            Pattern p = Pattern.compile(phoneExp);
            Matcher m = p.matcher(mobiles);
            IS_MOBEILPHONE = m.matches();
            if (IS_MOBEILPHONE) {
                return true;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return false;
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String replace(final String text, final String searchString, final String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static String join(final Iterator<?> iterator, final char separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            return toString(first);
        }

        // two or more elements
        final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            buf.append(separator);
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }

        return buf.toString();
    }

    /**
     * <p>Joins the elements of the provided {@code Iterator} into
     * a single String containing the provided elements.</p>
     * <p/>
     * <p>No delimiter is added before or after the list.
     * A {@code null} separator is the same as an empty String ("").</p>
     * <p/>
     * <p>See the examples here: {@link #join(Object[], String)}. </p>
     *
     * @param iterator  the {@code Iterator} of values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, {@code null} if null iterator input
     */
    public static String join(final Iterator<?> iterator, final String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            return toString(first);
        }

        // two or more elements
        final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    /**
     * <p>Joins the elements of the provided {@code Iterable} into
     * a single String containing the provided elements.</p>
     * <p/>
     * <p>No delimiter is added before or after the list. Null objects or empty
     * strings within the iteration are represented by empty strings.</p>
     * <p/>
     * <p>See the examples here: {@link #join(Object[], char)}. </p>
     *
     * @param iterable  the {@code Iterable} providing the values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, {@code null} if null iterator input
     * @since 2.3
     */
    public static String join(final Iterable<?> iterable, final char separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    /**
     * <p>Joins the elements of the provided {@code Iterable} into
     * a single String containing the provided elements.</p>
     * <p/>
     * <p>No delimiter is added before or after the list.
     * A {@code null} separator is the same as an empty String ("").</p>
     * <p/>
     * <p>See the examples here: {@link #join(Object[], String)}. </p>
     *
     * @param iterable  the {@code Iterable} providing the values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, {@code null} if null iterator input
     * @since 2.3
     */
    public static String join(final Iterable<?> iterable, final String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.indexOf(searchStr) >= 0;
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }


    public static String toString(final Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !StringUtils.isBlank(cs);
    }

    public static String replaceOnce(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    public static String replaceEach(final String text, final String[] searchList, final String[] replacementList) {
        return replaceEach(text, searchList, replacementList, false, 0);
    }

    private static String replaceEach(
            final String text, final String[] searchList, final String[] replacementList, final boolean repeat, final int timeToLive) {

        // mchyzer Performance note: This creates very few new objects (one major goal)
        // let me know if there are performance requests, we can create a harness to measure

        if (text == null || text.equals("") || searchList == null ||
                searchList.length == 0 || replacementList == null || replacementList.length == 0) {
            return text;
        }

        // if recursing, this shouldn't be less than 0
        if (timeToLive < 0) {
            throw new IllegalStateException("Aborting to protect against StackOverflowError - " +
                    "output of one loop is the input of another");
        }

        final int searchLength = searchList.length;
        final int replacementLength = replacementList.length;

        // make sure lengths are ok, these need to be equal
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: "
                    + searchLength
                    + " vs "
                    + replacementLength);
        }

        // keep track of which still have matches
        final boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                    searchList[i].isEmpty() || replacementList[i] == null) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i]);

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if (textIndex == -1) {
            return text;
        }

        int start = 0;

        // get a good guess on the size of the result buffer so it doesn't have to double if it goes over a bit
        int increase = 0;

        // count the replacement text elements that are larger than their corresponding text being replaced
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i] == null || replacementList[i] == null) {
                continue;
            }
            final int greater = replacementList[i].length() - searchList[i].length();
            if (greater > 0) {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min(increase, text.length() / 5);

        final StringBuilder buf = new StringBuilder(text.length() + increase);

        while (textIndex != -1) {

            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacementList[replaceIndex]);

            start = textIndex + searchList[replaceIndex].length();

            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                        searchList[i].isEmpty() || replacementList[i] == null) {
                    continue;
                }
                tempIndex = text.indexOf(searchList[i], start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else {
                    if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            // NOTE: logic duplicated above END

        }
        final int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        final String result = buf.toString();
        if (!repeat) {
            return result;
        }

        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }

    /**
     * 判断string是否只包含空格或回车换行
     * <p/>
     * by liyaxing
     *
     * @param str
     * @return 如果str为null，则return true
     */
    public static boolean isOnlyContainSpace(String str) {

        if (isEmpty(str)) return true;

        return isEmpty(str.trim().replace("\n", ""));

    }

}
