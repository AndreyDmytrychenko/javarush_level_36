package com.javarush.task36.task3606;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
Осваиваем ClassLoader и Reflection
*/

public class Solution {
    private List<Class> hiddenClasses = new ArrayList<>();
    private String packageName;


    public Solution(String packageName) {
        this.packageName = packageName;
    }

    public static void main(String[] args) throws ClassNotFoundException {

        Solution solution = new Solution(Paths.get("target/classes/" + Solution.class.getPackage().getName().replaceAll("[.]", "/")
                + "/data/second/").toAbsolutePath().toString());

        solution.scanFileSystem();
        System.out.println(solution.getHiddenClassObjectByKey("secoNdhiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("firsthiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("packa"));
    }

    public void scanFileSystem() throws ClassNotFoundException {

        File dir = new File(packageName);
        String[] paths = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".class");
            }
        });

        for (String p : paths) {
            SimpleClassLoader simpleClassLoader = new SimpleClassLoader(packageName);
            String className = p.substring(0, p.length() - 6);
                    Class<?> clazz = simpleClassLoader.loadClass(className);
                        hiddenClasses.add(clazz);
        }
    }

    public HiddenClass getHiddenClassObjectByKey(String key) {

            try {
                for (Class<?> clazz : hiddenClasses) {
                    Constructor<?> constructor = clazz.getDeclaredConstructor();

                    Class<?>[] parameterTypes = constructor.getParameterTypes();

                    if (parameterTypes.length == 0
                    && HiddenClass.class.isAssignableFrom(clazz)
                    && clazz.getSimpleName().toLowerCase().startsWith(key.toLowerCase())) {

                                constructor.setAccessible(true);
                                return (HiddenClass) constructor.newInstance();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }
}


