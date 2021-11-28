package com.javarush.task36.task3606;

import java.io.*;

public class SimpleClassLoader extends ClassLoader {
    private String pathToClasses;
    public SimpleClassLoader(String pathToClasses) {
        this.pathToClasses = pathToClasses;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        try {
            byte[] b = fetchClassFromFS(pathToClasses + "/" + className + ".class");
            return defineClass(null, b, 0, b.length);
        } catch (FileNotFoundException ex) {
            return super.findClass(className);
        } catch (IOException ex) {
            return super.findClass(className);
        }
    }

    private static byte[] fetchClassFromFS(String path) throws IOException {
        File file = new File(path);
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];

        is.read(bytes);
        is.close();
        return bytes;
    }
}

