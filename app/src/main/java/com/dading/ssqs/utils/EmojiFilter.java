package com.dading.ssqs.utils;

public class EmojiFilter {
    //检测是否有emoji字符
    public static boolean containsEmoji(String source) {
        if (source.equals(null)) { return false; }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) { return true; }
        }
        return false;
    }
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
    //过滤emoji 或者 其他非文字类型的字符
    public static String filterEmoji(String source) {
        //在传入的source后面加上一个空字符。返回的时候trim掉就OK了
        source += "";
        // 如果不包含，直接返回
        if (!containsEmoji(source)) {
            return source.trim();
        }
        else {
            StringBuilder buf = null;
            int len = source.length();
            for (int i = 0; i < len; i++) {
                char codePoint = source.charAt(i);
                if (isEmojiCharacter(codePoint)) {
                    if (buf == null) {
                        buf = new StringBuilder(source.length());
                    }
                    buf.append(codePoint);
                }
            }
            if (buf == null) {
                //如果没有找到 emoji表情，则返回源字符串
                return source;
            }
            else {
                //这里的意义在于尽可能少的toString，因为会重新生成字符串
                if (buf.length() == len) {
                    buf = null;
                    return source;
                }
                else {
                    return buf.toString();
                }
            }
        }
    }
}