package com.ddtsdk.utils;

/**
 * Created by a5706 on 2018/7/9.
 */

import android.support.annotation.IntRange;
import android.util.Log;

import com.ddtsdk.mylibrary.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Formatter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 只有当LEVEL常量的值大于或等于对应日志级别值的时候，才会打印日志。
 * 开发阶段，将LEVEL赋值为VERBOSE，上线阶段将LEVEL赋值为NOTHING
 *
 * 某些必须显示的信息，需使用LogUtil.ee();不会被屏蔽；
 */
public class LogUtil {
    private static final int FILE = 16;
    private static final int JSON = 32;
    private static final int XML = 48;
    private static final char[] T = new char[]{'V', 'D', 'I', 'W', 'E', 'N'};
    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final LogUtil.Config CONFIG = new LogUtil.Config();
    private static final Format FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ");

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int NOTHING = 7;

    public static final String TAG = "ddtsdk";

    /**控制想要打印的日志级别
     * 等于 VERBOSE，则就会打印所有级别的日志，记得注释openLog的方法体，或者外部不调用openLog
     * 等于 DEBUG，则只会打印调试级别以上的日志
     * 等于 INFO，则只会打印提示级别以上的日志
     * 等于 WARN，则只会打印警告级别以上的日志
     * 等于 ERROR，则只会打印错误级别以上的日志
     * 等于 NOTHING，则会屏蔽掉所有日志*/
    private static int LEVEL = VERBOSE;

    //必须被显示的日志,默认标签为TAG
    public static void vv(String msg){
        Log.v(TAG, msg);
    }
    public static void dd(String msg){
        Log.d(TAG, msg);
    }
    public static void ii(String msg){
        Log.i(TAG, msg);
    }
    public static void ww(String msg){
        Log.w(TAG, msg);
    }
    public static void ee(String msg){
        Log.e(TAG, msg);
    }

    //必须被显示的日志,标签可改
    public static void vv(String tag, String msg){
        Log.v(tag, msg);
    }
    public static void dd(String tag, String msg){
        Log.d(tag, msg);
    }
    public static void ii(String tag, String msg){
        Log.i(tag, msg);
    }
    public static void ww(String tag, String msg){
        Log.w(tag, msg);
    }
    public static void ee(String tag, String msg){
        Log.e(tag, msg);
    }

    public static Boolean isOpen() {
        /*
         * 可以按如下方式配置是否打开打印LOG
         *
         * 打印，则LEVEL=WARN
         * 不打印，则LEVEL=ERROR
         */
        return (LEVEL < ERROR);
    }

    // 默认LEVEL=NOTHING，【isopen=true，则为WARN；isopen=false，则为ERROR】
    public static void openLog(boolean isopen) {
        if (isopen) {
            LEVEL = INFO;
        } else {
            LEVEL = ERROR;
        }
    }

    // 默认TAG,输出日志可被屏蔽
    public static void v(String msg){
        if(LEVEL <= VERBOSE){
            CONFIG.setGlobalTag(TAG);
            log(VERBOSE, CONFIG.mGlobalTag, msg);
        }
    }
    public static void d(String msg){
        if(LEVEL <= VERBOSE) {
            CONFIG.setGlobalTag(TAG);
            log(DEBUG, CONFIG.mGlobalTag, msg);
        } else if(LEVEL <= DEBUG){
            dd(msg);
        }
    }
    public static void i(String msg){
        if(LEVEL <= VERBOSE) {
            CONFIG.setGlobalTag(TAG);
            log(INFO, CONFIG.mGlobalTag, msg);
        } else if(LEVEL <= INFO){
            ii(msg);
        }
    }
    public static void w(String msg){
        if(LEVEL <= VERBOSE) {
            CONFIG.setGlobalTag(TAG);
            log(WARN, CONFIG.mGlobalTag, msg);
        } else if(LEVEL <= WARN){
            ww(msg);
        }
    }
    public static void e(String msg){
        if(LEVEL <= VERBOSE) {
            CONFIG.setGlobalTag(TAG);
            log(ERROR, CONFIG.mGlobalTag, msg);
        } else if(LEVEL <= ERROR){
            ee(msg);
        }
    }


    //常规输出，可改标签
    public static void v(String tag, String msg){
        if(LEVEL <= VERBOSE){
            CONFIG.setGlobalTag(tag);
            log(VERBOSE, CONFIG.mGlobalTag, msg);
        }
    }

    public static void d(String tag, String msg){
        if(LEVEL <= VERBOSE) {
            CONFIG.setGlobalTag(tag);
            log(DEBUG, CONFIG.mGlobalTag, msg);
        } else if(LEVEL <= DEBUG){
            dd(msg);
        }
    }

    public static void i(String tag, String msg){
        if(LEVEL <= VERBOSE) {
            CONFIG.setGlobalTag(tag);
            log(INFO, CONFIG.mGlobalTag, msg);
        } else if(LEVEL <= INFO){
            ii(msg);
        }
    }

    public static void w(String tag, String msg){
        if(LEVEL <= VERBOSE) {
            CONFIG.setGlobalTag(tag);
            log(WARN, CONFIG.mGlobalTag, msg);
        } else if(LEVEL <= WARN){
            ww(msg);
        }
    }

    public static void e(String tag, String msg){
        if(LEVEL <= VERBOSE) {
            CONFIG.setGlobalTag(tag);
            log(ERROR, CONFIG.mGlobalTag, msg);
        } else if(LEVEL <= ERROR){
            ee(msg);
        }
    }

    private static String formatJson(String json) {
        try {
            if (json.startsWith("{")) {
                json = (new JSONObject(json)).toString(4);
            } else if (json.startsWith("[")) {
                json = (new JSONArray(json)).toString(4);
            }
        } catch (JSONException var2) {
            var2.printStackTrace();
        }

        return json;
    }

    private static String formatXml(String xml) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(xmlInput, xmlOutput);
            xml = xmlOutput.getWriter().toString().replaceFirst(">", ">" + LINE_SEP);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return xml;
    }

    public static void log(int type, String tag, Object... contents) {
        if (CONFIG.mLogSwitch && (CONFIG.mLog2ConsoleSwitch || CONFIG.mLog2FileSwitch)) {
            if(LEVEL > type) return;

            LogUtil.TagHead tagHead = processTagAndHead(tag);
            String body = processBody(type, contents);
            if (CONFIG.mLog2ConsoleSwitch && type >= CONFIG.mConsoleFilter && type != FILE) {
                print2Console(type, tagHead.tag, tagHead.consoleHead, body);
            }

            if (CONFIG.mLog2FileSwitch && type >= CONFIG.mFileFilter || type == FILE) {
                print2File(type, tagHead.tag, tagHead.fileHead + body);
            }
        }
    }
    private static LogUtil.TagHead processTagAndHead(String tag) {
        if (!CONFIG.mTagIsSpace && !CONFIG.mLogHeadSwitch) {
            tag = CONFIG.mGlobalTag;
        } else {
            StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
            int stackIndex = 3 + CONFIG.mStackOffset;
            StackTraceElement targetElement;
            String fileName;
            int index;
            if (stackIndex >= stackTrace.length) {
                targetElement = stackTrace[3];
                fileName = getFileName(targetElement);
                if (CONFIG.mTagIsSpace && isSpace(tag)) {
                    index = fileName.indexOf(46);
                    tag = index == -1 ? fileName : fileName.substring(0, index);
                }

                return new LogUtil.TagHead(tag, (String[])null, ": ");
            }

            targetElement = stackTrace[stackIndex];
            fileName = getFileName(targetElement);
            if (CONFIG.mTagIsSpace && isSpace(tag)) {
                index = fileName.indexOf(46);
                tag = index == -1 ? fileName : fileName.substring(0, index);
            }

            if (CONFIG.mLogHeadSwitch) {
                String tName = Thread.currentThread().getName();
                String head = (new Formatter()).format("%s, %s.%s(%s:%d)", tName, targetElement.getClassName(), targetElement.getMethodName(), fileName, targetElement.getLineNumber()).toString();
                String fileHead = " [" + head + "]: ";
                if (CONFIG.mStackDeep <= 1) {
                    return new LogUtil.TagHead(tag, new String[]{head}, fileHead);
                }

                String[] consoleHead = new String[Math.min(CONFIG.mStackDeep, stackTrace.length - stackIndex)];
                consoleHead[0] = head;
                int spaceLen = tName.length() + 2;
                String space = (new Formatter()).format("%" + spaceLen + "s", "").toString();
                int i = 1;

                for(int len = consoleHead.length; i < len; ++i) {
                    targetElement = stackTrace[i + stackIndex];
                    consoleHead[i] = (new Formatter()).format("%s%s.%s(%s:%d)", space, targetElement.getClassName(), targetElement.getMethodName(), getFileName(targetElement), targetElement.getLineNumber()).toString();
                }

                return new LogUtil.TagHead(tag, consoleHead, fileHead);
            }
        }

        return new LogUtil.TagHead(tag, (String[])null, ": ");
    }

    private static String getFileName(StackTraceElement targetElement) {
        String fileName = targetElement.getFileName();
        if (fileName != null) {
            return fileName;
        } else {
            String className = targetElement.getClassName();
            String[] classNameInfo = className.split("\\.");
            if (classNameInfo.length > 0) {
                className = classNameInfo[classNameInfo.length - 1];
            }

            int index = className.indexOf(36);
            if (index != -1) {
                className = className.substring(0, index);
            }

            return className + ".java";
        }
    }

    private static String processBody(int type, Object... contents) {
        String body = "null";
        if (contents != null) {
            if (contents.length == 1) {
                Object object = contents[0];
                if (object != null) {
                    body = object.toString();
                }

                if (type == JSON) {
                    body = formatJson(body);
                } else if (type == XML) {
                    body = formatXml(body);
                }
            } else {
                StringBuilder sb = new StringBuilder();
                int i = 0;

                for(int len = contents.length; i < len; ++i) {
                    Object content = contents[i];
                    sb.append("args").append("[").append(i).append("]").append(" = ").append(content == null ? "null" : content.toString()).append(LINE_SEP);
                }

                body = sb.toString();
            }
        }

        return body.length() == 0 ? "log nothing" : body;
    }

    private static void print2Console(int type, String tag, String[] head, String msg) {
        if (CONFIG.mSingleTagSwitch) {
            StringBuilder sb = new StringBuilder();
            sb.append(" ").append(LINE_SEP);
            String[] var5;
            int var6;
            int var7;
            String aHead;
            if (CONFIG.mLogBorderSwitch) {
                sb.append("┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────").append(LINE_SEP);
                if (head != null) {
                    var5 = head;
                    var6 = head.length;

                    for(var7 = 0; var7 < var6; ++var7) {
                        aHead = var5[var7];
                        sb.append("│ ").append(aHead).append(LINE_SEP);
                    }

                    sb.append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄").append(LINE_SEP);
                }

                var5 = msg.split(LINE_SEP);
                var6 = var5.length;

                for(var7 = 0; var7 < var6; ++var7) {
                    aHead = var5[var7];
                    sb.append("│ ").append(aHead).append(LINE_SEP);
                }

                sb.append("└────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
            } else {
                if (head != null) {
                    var5 = head;
                    var6 = head.length;

                    for(var7 = 0; var7 < var6; ++var7) {
                        aHead = var5[var7];
                        sb.append(aHead).append(LINE_SEP);
                    }
                }

                sb.append(msg);
            }

            printMsgSingleTag(type, tag, sb.toString());
        } else {
            printBorder(type, tag, true);
            printHead(type, tag, head);
            printMsg(type, tag, msg);
            printBorder(type, tag, false);
        }
    }

    private static void print2File(int type, String tag, String msg) {
//        Date now = new Date(System.currentTimeMillis());
//        String format = FORMAT.format(now);
//        String date = format.substring(0, 5);
//        String time = format.substring(6);
//        String fullPath = (CONFIG.mDir == null ? CONFIG.mDefaultDir : CONFIG.mDir) + CONFIG.mFilePrefix + "-" + date + ".txt";
//        if (!createOrExistsFile(fullPath)) {
//            Log.e("LogUtil", "create " + fullPath + " failed!");
//        } else {
//            StringBuilder sb = new StringBuilder();
//            sb.append(time).append(T[type - 2]).append("/").append(tag).append(msg).append(LINE_SEP);
//            String content = sb.toString();
//            input2File(content, fullPath);
//        }
    }

    private static void printMsg(int type, String tag, String msg) {
        int len = msg.length();
        int countOfSub = len / 3000;
        if (countOfSub > 0) {
            int index = 0;

            for(int i = 0; i < countOfSub; ++i) {
                printSubMsg(type, tag, msg.substring(index, index + 3000));
                index += 3000;
            }

            if (index != len) {
                printSubMsg(type, tag, msg.substring(index, len));
            }
        } else {
            printSubMsg(type, tag, msg);
        }
    }

    private static void printSubMsg(int type, String tag, String msg) {
        if (!CONFIG.mLogBorderSwitch) {
            Log.println(type, tag, msg);
        } else {
            new StringBuilder();
            String[] lines = msg.split(LINE_SEP);
            String[] var5 = lines;
            int var6 = lines.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String line = var5[var7];
                Log.println(type, tag, "│ " + line);
            }

        }
    }

    private static void printBorder(int type, String tag, boolean isTop) {
        if (CONFIG.mLogBorderSwitch) {
            Log.println(type, tag, isTop ? "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────" : "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        }
    }

    private static void printHead(int type, String tag, String[] head) {
        if (head != null) {
            String[] var3 = head;
            int var4 = head.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String aHead = var3[var5];
                Log.println(type, tag, CONFIG.mLogBorderSwitch ? "│ " + aHead : aHead);
            }

            if (CONFIG.mLogBorderSwitch) {
                Log.println(type, tag, "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
            }
        }
    }

    private static void printMsgSingleTag(int type, String tag, String msg) {
        int len = msg.length();
        int countOfSub = len / 3000;
        if (countOfSub > 0) {
            int index;
            int i;
            if (CONFIG.mLogBorderSwitch) {
                Log.println(type, tag, msg.substring(0, 3000) + LINE_SEP + "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
                index = 3000;

                for(i = 1; i < countOfSub; ++i) {
                    Log.println(type, tag, " " + LINE_SEP + "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────" + LINE_SEP + "│ " + msg.substring(index, index + 3000) + LINE_SEP + "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
                    index += 3000;
                }

                if (index != len) {
                    Log.println(type, tag, " " + LINE_SEP + "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────" + LINE_SEP + "│ " + msg.substring(index, len));
                }
            } else {
                index = 0;

                for(i = 0; i < countOfSub; ++i) {
                    Log.println(type, tag, msg.substring(index, index + 3000));
                    index += 3000;
                }

                if (index != len) {
                    Log.println(type, tag, msg.substring(index, len));
                }
            }
        } else {
            Log.println(type, tag, msg);
        }

    }

    private static boolean isSpace(String s) {
        if (s == null) {
            return true;
        } else {
            int i = 0;

            for(int len = s.length(); i < len; ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    //-----------------------------------------TagHead----------------------------------------------

    private static class TagHead {
        String tag;
        String[] consoleHead;
        String fileHead;

        TagHead(String tag, String[] consoleHead, String fileHead) {
            this.tag = tag;
            this.consoleHead = consoleHead;
            this.fileHead = fileHead;
        }
    }

    //-----------------------------------------Config----------------------------------------------
    public static class Config {
        private boolean mLogSwitch;
        private boolean mLogHeadSwitch;
        private boolean mLog2ConsoleSwitch;
        private boolean mLog2FileSwitch;
        private boolean mLogBorderSwitch;
        private boolean mSingleTagSwitch;

        private String mGlobalTag;
        private boolean mTagIsSpace;
        private int mStackDeep;
        private int mStackOffset;

        private int mConsoleFilter;
        private int mFileFilter;
        private String mDir;

        private Config() {
            this.mLogSwitch = true;
            this.mLogHeadSwitch = true;
            this.mLog2ConsoleSwitch = true;
            this.mLog2FileSwitch = false;
            this.mLogBorderSwitch = true;
            this.mSingleTagSwitch = true;

            this.mGlobalTag = null;
            this.mTagIsSpace = true;
            this.mStackDeep = 1;
            this.mStackOffset = 0;

            this.mConsoleFilter = LogUtil.VERBOSE;
            this.mFileFilter = LogUtil.VERBOSE;
        }

        public LogUtil.Config setGlobalTag(String tag) {
            if (LogUtil.isSpace(tag)) {
                this.mGlobalTag = "";
                this.mTagIsSpace = true;
            } else {
                this.mGlobalTag = tag;
                this.mTagIsSpace = false;
            }

            return this;
        }

        public LogUtil.Config setLogSwitch(boolean logSwitch) {
            this.mLogSwitch = logSwitch;
            return this;
        }

        public LogUtil.Config setLogHeadSwitch(boolean logHeadSwitch) {
            this.mLogHeadSwitch = logHeadSwitch;
            return this;
        }

        public LogUtil.Config setConsoleSwitch(boolean consoleSwitch) {
            this.mLog2ConsoleSwitch = consoleSwitch;
            return this;
        }

        public LogUtil.Config setLog2FileSwitch(boolean log2FileSwitch) {
            this.mLog2FileSwitch = log2FileSwitch;
            return this;
        }

        public LogUtil.Config setBorderSwitch(boolean borderSwitch) {
            this.mLogBorderSwitch = borderSwitch;
            return this;
        }

        public LogUtil.Config setSingleTagSwitch(boolean singleTagSwitch) {
            this.mSingleTagSwitch = singleTagSwitch;
            return this;
        }

        public LogUtil.Config setStackDeep(@IntRange(from = 1L) int stackDeep) {
            this.mStackDeep = stackDeep;
            return this;
        }

        public LogUtil.Config setStackOffset(@IntRange(from = 0L) int stackOffset) {
            this.mStackOffset = stackOffset;
            return this;
        }

        public LogUtil.Config setConsoleFilter(int consoleFilter) {
            this.mConsoleFilter = consoleFilter;
            return this;
        }

        public LogUtil.Config setFileFilter(int fileFilter) {
            this.mFileFilter = fileFilter;
            return this;
        }

        public LogUtil.Config setDir(String dir) {
            if (LogUtil.isSpace(dir)) {
                this.mDir = null;
            } else {
                this.mDir = dir.endsWith(LogUtil.FILE_SEP) ? dir : dir + LogUtil.FILE_SEP;
            }

            return this;
        }

        public LogUtil.Config setDir(File dir) {
            this.mDir = dir == null ? null : dir.getAbsolutePath() + LogUtil.FILE_SEP;
            return this;
        }

        public String toString() {
            StringBuffer str = new StringBuffer();
            str.append("{ " + LogUtil.LINE_SEP);
            str.append("    switch=" + this.mLogSwitch + LogUtil.LINE_SEP);
            str.append("    console=" + this.mLog2ConsoleSwitch + " " + LogUtil.LINE_SEP);
            str.append("    tag=" + (this.mTagIsSpace ? "null" : this.mGlobalTag) + LogUtil.LINE_SEP);
            str.append("    head=" + this.mLogHeadSwitch + LogUtil.LINE_SEP);
            str.append("    file=" + this.mLog2FileSwitch + LogUtil.LINE_SEP);
//            str.append("  dir=" + (this.mDir == null ? this.mDefaultDir : this.mDir) + LogUtil.LINE_SEP);
//            str.append("  filePrefix=" + this.mFilePrefix + LogUtil.LINE_SEP);
            str.append("    border=" + this.mLogBorderSwitch + LogUtil.LINE_SEP);
            str.append("    singleTag=" + this.mSingleTagSwitch + LogUtil.LINE_SEP);
            str.append("    consoleFilter=" + LogUtil.T[this.mConsoleFilter - LogUtil.VERBOSE] + LogUtil.LINE_SEP);
            str.append("    fileFilter=" + LogUtil.T[this.mFileFilter - LogUtil.VERBOSE] + LogUtil.LINE_SEP);
            str.append("    stackDeep=" + this.mStackDeep + LogUtil.LINE_SEP);
            str.append("    mStackOffset=" + this.mStackOffset + LogUtil.LINE_SEP);
            str.append(" }");
            return str.toString();
        }
    }

}