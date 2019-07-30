package com.zxk175.well.common.util.json;

/**
 * https://blog.csdn.net/weixin_40393909/article/details/80369300
 *
 * @author zxk175
 */
public class JsonFormatUtil {

    /**
     * 对json字符串格式化输出
     *
     * @param jsonStr ignore
     * @return ignore
     */
    static String formatJsonStr(String jsonStr) {
        int level = 0;
        // 存放格式化的json字符串
        StringBuilder jsonForMatStr = new StringBuilder();
        int length = jsonStr.length();
        // 将字符串中的字符逐个按行输出
        for (int index = 0; index < length; index++) {
            // 获取s中的每个字符
            char c = jsonStr.charAt(index);

            // level大于0并且jsonFormatStr中的最后一个字符为\n,jsonFormatStr加入\t
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            // 遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c).append("\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c).append("\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level)).append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();
    }

    private static StringBuilder getLevelStr(int level) {
        StringBuilder levelStr = new StringBuilder();
        for (int leveli = 0; leveli < level; leveli++) {
            levelStr.append("\t");
        }
        return levelStr;
    }

    public static void main(String[] args) {
        String s = "{\"entry\":1,\"notifyData\":{\"transaction_id\":\"4200000372201907236899188649\"}}";

        String out = formatJsonStr(s);
        System.out.println(out);
    }
}
