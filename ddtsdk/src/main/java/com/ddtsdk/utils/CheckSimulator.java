package com.ddtsdk.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.view.ForceExitDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class CheckSimulator {

    String Tag = "CheckSimulator";
    private static Activity activity = null;
    private int suspectCount = 0;
    private static CheckSimulator checkSimulator = null;

    public static StringBuilder whereAdd = new StringBuilder();
    Boolean hasExist = false;

    public static CheckSimulator getInstance(){
        if (checkSimulator == null){
            checkSimulator = new CheckSimulator();
        }
        return checkSimulator;
    }


    public  boolean readSysProperty(Context context) {
        suspectCount = 0;
        activity = (Activity) context;

        String baseBandVersion = getSysProperty("gsm.version.baseband");
        if (null == baseBandVersion || baseBandVersion.contains("1.0.0.0") || "".equals(baseBandVersion)){
            suspectCount += 2;//基带信息
            hasExist = true;
        }
        whereAdd.append("基带信息:" + hasExist + "\n");
        hasExist = false;

        String buildFlavor = getSysProperty("ro.build.flavor");
        if (null == buildFlavor || buildFlavor.contains("vbox")
                || buildFlavor.contains("sdk_gphone") || buildFlavor.contains("x86")
                || "".equals(buildFlavor)){
            suspectCount++;//渠道
            hasExist = true;
        }
        whereAdd.append("渠道信息:" + hasExist + "\n");
        hasExist = false;

        String productBoard = Build.BOARD;
        if (null == productBoard || productBoard.contains("android")
                || productBoard.contains("goldfish") || "".equals(productBoard)) {
            suspectCount++;//芯片
            hasExist = true;
        }
        whereAdd.append("芯片信息:" + hasExist + "\n");
        hasExist = false;

        LogUtil.d("suspectCount3=" + suspectCount);

        String boardPlatform = getSysProperty("ro.board.platform");
        if (null == boardPlatform || boardPlatform.contains("android") || "".equals(boardPlatform)) {
            suspectCount++;//芯片平台
            hasExist = true;
        }
        whereAdd.append("芯片平台:" + hasExist + "\n");
        hasExist = false;

        LogUtil.d("suspectCount4=" + suspectCount);

        String hardWare = Build.HARDWARE;
        if (null == hardWare || hardWare.contains("x86") || "".equals(hardWare)) {
            suspectCount++;
            hasExist = true;
        }

        else if (hardWare.toLowerCase().contains("ttvm")){
            suspectCount += 10;//天天
            hasExist = true;
        }
        else if (hardWare.toLowerCase().contains("nox")){
            suspectCount += 10;//夜神
            hasExist = true;
        }
        whereAdd.append("硬件信息:" + hasExist + "\n");
        hasExist = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (String cpu : Build.SUPPORTED_ABIS){
                if (cpu.contains("x86")){
                    suspectCount++;
                    hasExist = true;
                    break;
                }
            }
        }
        whereAdd.append("cpu_abi信息:" + hasExist + "\n");
        hasExist = false;

        String filter = execute("cat /proc/self/cgroup");
        if (null == filter || "".equals(filter)){
            suspectCount++;
            hasExist = true;
        }//进程租
        whereAdd.append("进程组信息:" + hasExist + "\n");
        hasExist = false;

        String cpuinfo = getCpuInfo();
        if (cpuinfo.contains("intel") || cpuinfo.contains("amd")){
            suspectCount += 2;
            hasExist = true;
        }
        whereAdd.append("cpuinfo信息:" + hasExist + "\n");
        hasExist = false;

        if (!hasCallUI()){
            suspectCount += 2;
            hasExist = true;
        }
        whereAdd.append("拨号界面信息:" + hasExist + "\n");
        hasExist = false;

        if (!hasCall()){
            suspectCount += 1;
            hasExist = true;
        }
        whereAdd.append("打电话:" + hasExist + "\n");
        hasExist = false;

        if (!hasMMS()){
            suspectCount += 1;
            hasExist = true;
        }
        whereAdd.append("发彩信:" + hasExist + "\n");
        hasExist = false;

        if (!hasSendTo()){
            suspectCount += 1;
            hasExist = true;
        }
        whereAdd.append("发短信:" + hasExist + "\n");
        hasExist = false;

        if (!hasRecord()){
            suspectCount += 1;
            hasExist = true;
        }
        whereAdd.append("录音机:" + hasExist + "\n");
        hasExist = false;

        checkIoPorts();
        checkIomem();
        checkSensor(context);
//        checkProcInterrupts();
        checkDevProperties();
        checkSysModule();
        checkSysBus();
        checkSystemLib();
        checkLinuxVersion();

        whereAdd.append("指数:" + suspectCount + "\n");

        LogUtil.d(whereAdd.toString());
        return suspectCount > 8;
    }

    public String getSysProperty(String name){
        String data = null;
        try {
            Class sysclass = Class.forName("android.os.SystemProperties");
            data = (String) sysclass.getMethod("get", String.class).invoke(null, name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            data = "Class error";
            LogUtil.e("android.os.SystemProperties-----" + "ClassNotFoundException");
        } catch (IllegalAccessException | InvocationTargetException e) {
            LogUtil.e(name + "-----" + "NoSuchMethodException");
            data = "Method error";
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            data = "Method error";
            LogUtil.e(name + "-----" + "NoSuchMethodException");
        }
        return data;
    }

    public String execute(String command){
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("sh");
            bufferedOutputStream = new BufferedOutputStream(process.getOutputStream());

            bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedOutputStream.write(command.getBytes());
            bufferedOutputStream.write('\n');
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            process.waitFor();

            String outputStr = getStrFromBufferInputSteam(bufferedInputStream);
            return outputStr;
        } catch (Exception e) {
            return null;
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }

    }

    private static String getStrFromBufferInputSteam(BufferedInputStream bufferedInputStream) {
        if (null == bufferedInputStream) {
            return "";
        }
        int BUFFER_SIZE = 512;
        byte[] buffer = new byte[BUFFER_SIZE];
        StringBuilder result = new StringBuilder();
        try {
            while (true) {
                int read = bufferedInputStream.read(buffer);
                if (read > 0) {
                    result.append(new String(buffer, 0, read));
                }
                if (read < BUFFER_SIZE) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private String getCpuInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            String data = null;
            while ((data = bufferedReader.readLine()) != null){
                stringBuilder.append(data);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString().toLowerCase();
    }

    private void checkSensor(Context context){
        if (context != null) {
            boolean isSupportCameraFlash = context.getPackageManager().hasSystemFeature("android.hardware.camera.flash");//是否支持闪光灯
            if (!isSupportCameraFlash){
                suspectCount++;
            }

            SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            if (sm.getDefaultSensor(Sensor.TYPE_LIGHT) == null){
                suspectCount++;
                hasExist = true;
            }
            whereAdd.append("光传感器信息:" + hasExist + "\n");
            hasExist = false;

            int sensorSize = sm.getSensorList(Sensor.TYPE_ALL).size();
            if (sensorSize <= 7) {
                suspectCount++;//传感器个数
                hasExist = true;
            }
            whereAdd.append("传感器个数小于7:" + hasExist + "\n");
            hasExist = false;
        }
    }

    String name = "";
    private boolean hasCallUI(){
        boolean callphone = true;
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + "123456");
            intent.setData(data);
            ComponentName componentName = intent.resolveActivity(activity.getPackageManager());

            name = componentName.getClassName();
            String[] namelist = name.split("\\.");
            if (namelist.length < 2 || TextUtils.isEmpty(name)){
                callphone = false;
            }
        }
        catch (Exception e){
            //Attempt to invoke virtual method 'java.lang.String android.content.ComponentName.getClassName()
            // ' on a null object reference
            callphone = false;
        }
        return callphone;
    }

    public boolean hasCall(){
        boolean callphone = true;
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + "123456");
            intent.setData(data);
            ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
            if (componentName == null){
                callphone = false;
            }
        }
        catch (Exception e){
            //Attempt to invoke virtual method 'java.lang.String android.content.ComponentName.getClassName()
            // ' on a null object reference
            callphone = false;
        }
        return callphone;
    }

    public boolean hasSendTo(){
        boolean callphone = true;
        try {
            Uri data = Uri.parse("smsto:123456");
            Intent intent = new Intent(Intent.ACTION_SENDTO, data);
            ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
            if (componentName == null){
                callphone = false;
            }
        }
        catch (Exception e){
            //Attempt to invoke virtual method 'java.lang.String android.content.ComponentName.getClassName()
            // ' on a null object reference
            callphone = false;
        }
        return callphone;
    }

    //彩信
    public boolean hasMMS(){
        boolean callphone = true;
        try {
            Uri uri = Uri.parse("content://media/external/images/media/23");
            Intent it = new Intent(Intent.ACTION_SEND);
            it.putExtra("sms_body", "some text");
            it.putExtra(Intent.EXTRA_STREAM, uri);
            it.setType("image/png");
            ComponentName componentName = it.resolveActivity(activity.getPackageManager());
            if (componentName == null){
                callphone = false;
            }
        }
        catch (Exception e){
            //Attempt to invoke virtual method 'java.lang.String android.content.ComponentName.getClassName()
            // ' on a null object reference
            callphone = false;
        }
        return callphone;
    }

    //录音机
    public boolean hasRecord(){
        boolean callphone = true;
        try {
            Intent it = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            ComponentName componentName = it.resolveActivity(activity.getPackageManager());
            if (componentName == null){
                callphone = false;
            }
        }
        catch (Exception e){
            //Attempt to invoke virtual method 'java.lang.String android.content.ComponentName.getClassName()
            // ' on a null object reference
            callphone = false;
        }
        return callphone;
    }

    private String getBuildProp(){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader("/system/build.prop"));
            String data = null;
            while ((data = bufferedReader.readLine()) != null){
                stringBuilder.append(data + "\n");
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString().toLowerCase();
    }

    private void checkSystemLib(){
        String  path = "/system/lib";
        File file = new File(path);
        File[] fileList = file.listFiles();
        if (fileList == null){
            LogUtil.e(path + ", check system lib failed");
            return;
        }
        for (File libfile : fileList){
            String filename = libfile.getName();
            if (filename.contains("vbox") && filename.contains(".ko")){
                suspectCount++;
                hasExist = true;
            }
            whereAdd.append("system/lib信息:" + hasExist + "\n");
            hasExist = false;
            return;
        }
    }

    private void checkSysBus(){
        String path = "/sys/bus";
        File file = new File(path);
        File busfile = new File(path + "/virtio");
        if (!file.exists() || busfile.exists()){
            suspectCount++;
            hasExist = true;
        }
        whereAdd.append("sys/bus信息:" + hasExist + "\n");
        hasExist = false;
    }

    private void checkSysModule(){
        String path = "/sys/module";
        File file = new File(path);
        File[] fileList = file.listFiles();
        if (fileList != null){
            for (File folder: fileList){
                String name = folder.getName();
                if (name.contains("vbox") || name.contains("virtio")
                || name.contains("qemu")){
                    suspectCount++;
                    hasExist = true;
                    whereAdd.append("sys/module信息:" + hasExist + "\n");
                    hasExist = false;
                    return;
                }
            }
        }
        whereAdd.append("sys/module信息:" + hasExist + "\n");
        LogUtil.e(path +  "  failed");

    }

    private void checkDevProperties(){
        String path = "/dev/_properties_";
        String content = readFile(path);
        if (!"".equals(content) && (content.contains("qemu") || content.contains("vbox"))){
            suspectCount++;
            hasExist = true;
        }
        whereAdd.append("dev/properties信息:" + hasExist + "\n");
        hasExist = false;
    }


    /**
     * 获取中断信息，判断中断是否出现可疑字符串
     * 该方法弃用， 检测发现真机也有相似字段出现
     */
    private void checkProcInterrupts(){
        String path = "/proc/interrupts";
        String content = readFile(path);
        if (!"".equals(content) && (content.contains("hypervisor") || content.contains("goldfish"))){
            suspectCount++;
            hasExist = true;
        }
        whereAdd.append("proc/interrupts信息:" + hasExist + "\n");
        hasExist = false;
    }

    private void checkIomem(){
        String path = "/proc/iomem";
        String content = readFile(path);
        if (!"".equals(content) && (content.contains("goldfish") || content.contains("vbox"))){
            suspectCount++;
            hasExist = true;
        }
        whereAdd.append("proc/iomem信息:" + hasExist + "\n");
        hasExist = false;
    }

    private void checkIoPorts(){
        String path = "/proc/ioports";
        String content = readFile(path);
        if (!"".equals(content) && (content.contains("goldfish") || content.contains("virtio"))){
            suspectCount++;
            hasExist = true;
        }
        whereAdd.append("proc/ioports信息:" + hasExist + "\n");
        hasExist = false;
    }

    private void checkLinuxVersion(){
        String path = "/proc/version";
        String content = readFile(path);
        if (!"".equals(content) && content.contains("x86")){
            suspectCount++;
            hasExist = true;
        }
        whereAdd.append("/proc/version信息:" + hasExist + "\n");
        hasExist = false;
        LogUtil.d("checkLinuxVersion=" + content);
    }


    public String readFile(String path){
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        if (file.exists()){
            BufferedReader reader = null;
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
                reader = new BufferedReader(fileReader);
                String content;
                while((content = reader.readLine()) != null){
                    stringBuilder.append(content);
                }
                reader.close();
                fileReader.close();

                return stringBuilder.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static void showExitDialog(Context context){
        activity = (Activity)context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ForceExitDialog.showDialog(activity, null, 0, null);
            }
        });
    }
}
