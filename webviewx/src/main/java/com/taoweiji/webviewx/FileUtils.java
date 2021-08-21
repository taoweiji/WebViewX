package com.taoweiji.webviewx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class FileUtils {
    public static void unZip(File file, File targetDir) {
        if (!file.exists()) {
            return;
        }
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                File tmp = new File(targetDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    tmp.mkdirs();
                } else {
                    InputStream is = zipFile.getInputStream(zipEntry);
                    write(tmp, is);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(File file, InputStream is) {
        try {
            FileOutputStream os = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(File target) {
        if (!target.exists()) {
            return;
        }
        if (target.isDirectory()) {
            File[] files = target.listFiles();
            if (files != null) {
                for (File it : files) {
                    delete(it);
                }
            }
        }
        target.delete();
    }


    public static String read(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr).append("\n");
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public static String read(InputStream is) {
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr).append("\n");
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
}
