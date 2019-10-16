package xinrui.cloud.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.SystemPropertyUtils;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author liuliuliu
 * @version 1.0
 *  2019/8/7 10:32
 */
@SuppressWarnings({"unused", "unchecked"})
@Slf4j
public class PackageUtils {

    /**
     * 扫描 scanPackages 下的文件的匹配符
     */
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    /**
     * 结合spring的类扫描方式 根据需要扫描的包路径及相应的注解，获取最终测method集合
     * 仅返回public方法，如果方法是非public类型的，不会被返回 可以扫描工程下的class文件及jar中的class文件
     *
     * @param scanPackages 需要扫描的包路径
     * @param annotation   注解类型
     * @return 当前包下所有的class文件下的所有含有此注解的method
     */
    public static Set<Method> findClassAnnotationMethods(String scanPackages, Class<? extends Annotation> annotation) {
        Set<String> clazzSet = findPackageClass(scanPackages);
        Set<Method> methods = new HashSet<>();
        for (String clazz : clazzSet) {
            try {
                Set<Method> ms = findAnnotationMethods(clazz, annotation);
                if (ms != null) {
                    methods.addAll(ms);
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
        return methods;
    }

    /**
     * 根据扫描包的,查询下面的所有类
     *
     * @param scanPackages 扫描的package路径
     * @return 当前包下所有的类的全类名
     */
    public static Set<String> findPackageClass(String scanPackages) {
        if (scanPackages == null || scanPackages.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        // 验证及排重包路径,避免父子路径多次扫描
        Set<String> packages = checkPackage(scanPackages);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        Set<String> clazzSet = new HashSet<>();
        for (String basePackage : packages) {
            if (basePackage == null || basePackage.isEmpty()) {
                continue;
            }
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + org.springframework.util.ClassUtils.convertClassNameToResourcePath(
                    SystemPropertyUtils.resolvePlaceholders(basePackage))
                    + "/" + DEFAULT_RESOURCE_PATTERN;
            try {
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    String clazz = loadClassName(metadataReaderFactory, resource);
                    clazzSet.add(clazz);
                }
            } catch (Exception e) {
                log.error("获取包下面的类信息失败,package:" + basePackage, e);
            }
        }
        return clazzSet;
    }

    /**
     * 排重、检测package父子关系，避免多次扫描
     *
     * @param scanPackages 扫描的包路径
     * @return 返回检查后有效的路径集合
     */
    @SuppressWarnings("unchecked")
    private static Set<String> checkPackage(String scanPackages) {
        if (scanPackages == null || scanPackages.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Set<String> packages = new HashSet<>();
        // 排重路径
        Collections.addAll(packages, scanPackages.split(","));
        for (String pInArr : packages.toArray(new String[0])) {
            if ((pInArr == null || pInArr.isEmpty()) || ".".equals(pInArr) || pInArr.startsWith(".")) {
                continue;
            }
            if (pInArr.endsWith(".")) {
                pInArr = pInArr.substring(0, pInArr.length() - 1);
            }
            Iterator<String> packageIte = packages.iterator();
            boolean needAdd = true;
            while (packageIte.hasNext()) {
                String pack = packageIte.next();
                if (pInArr.startsWith(pack + ".")) {
                    // 如果待加入的路径是已经加入的pack的子集，不加入
                    needAdd = false;
                } else if (pack.startsWith(pInArr + ".")) {
                    // 如果待加入的路径是已经加入的pack的父集，删除已加入的pack
                    packageIte.remove();
                }
            }
            if (needAdd) {
                packages.add(pInArr);
            }
        }
        return packages;
    }

    /**
     * 加载资源，根据resource获取className
     *
     * @param metadataReaderFactory spring中用来读取resource为class的工具
     * @param resource              这里的资源就是一个Class
     */
    private static String loadClassName(MetadataReaderFactory metadataReaderFactory, Resource resource) {
        try {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (metadataReader != null) {
                    return metadataReader.getClassMetadata().getClassName();
                }
            }
        } catch (Exception e) {
            log.error("根据resource获取类名称失败", e);
        }
        return null;
    }

    /**
     * 把action下面的所有method遍历一次，标记他们是否需要进行敏感词验证 如果需要，放入cache中
     *
     * @param fullClassName
     */
    public static Set<Method> findAnnotationMethods(String fullClassName, Class<? extends Annotation> annotation)
            throws ClassNotFoundException {
        Set<Method> methodSet = new HashSet<Method>();
        Class<?> clz = Class.forName(fullClassName);
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getModifiers() != Modifier.PUBLIC) {
                continue;
            }
            if (method.getAnnotation(annotation) != null) {
                methodSet.add(method);
            }
        }
        return methodSet;
    }

    /**
     * <li>获得包下面的所有的class
     *
     * @param pack       package完整名称
     * @param annotation 需要哪些注解类型的类
     * @return List包含所有class的实例
     */
    @SuppressWarnings("rawtypes")
    public static List<Class> getClasssFromPackage(String pack, Class<? extends Annotation> annotation) {

        List<Class> clazzs = new ArrayList<Class>();
        // 包名字
        String packageName = pack;
        // 包名对应的路径名称
        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassInPackageByFile(packageName, filePath, true, clazzs, annotation);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 获取jar
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    // 扫描jar包文件 并添加到集合中
                    findClassesByJar(packageDirName, jar, clazzs, annotation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clazzs;
    }

    /**
     * 根据jarFile对象获取指定包名前缀的class对象集合
     *
     * @param pkgName    的包路径
     * @param jar        {@link JarFile}对象
     * @param clazzList  装结果类的集合
     * @param annotation 注解类型   code can be null
     */
    private static void findClassesByJar(String pkgName, JarFile jar, List<Class> clazzList, Class<? extends Annotation> annotation) {
        Enumeration<JarEntry> entry = jar.entries();
        JarEntry jarEntry;
        String name, className;
        Class<?> loadClass;
        while (entry.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文
            jarEntry = entry.nextElement();
            name = jarEntry.getName();
            // 如果是以/开头的
            if (name.charAt(0) == '/') {
                // 获取后面的字符串
                name = name.substring(1);
            }
            if (jarEntry.isDirectory() || !name.startsWith(pkgName) || !name.endsWith(".class")) {
                continue;
            }
            // 如果是一个.class文件 而且不是目录
            // 去掉后面的".class" 获取真正的类名
            className = name.substring(0, name.length() - 6);
            // 加载类
            loadClass = loadClass(className.replace("/", "."));
            // 添加到集合中去
            if (loadClass != null) {
                if (annotation != null) {
                    if (loadClass.isAnnotationPresent(annotation)) {
                        clazzList.add(loadClass);
                    }
                    continue;
                }
                clazzList.add(loadClass);
            }
        }
    }

    /**
     * 加载类
     *
     * @param fullClzName 类全名
     * @return
     */
    private static Class<?> loadClass(String fullClzName) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(fullClzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <li>在package对应的路径下找到所有的class
     *
     * @param packageName package名称
     * @param filePath    package对应的路径
     * @param recursive   是否查找子package
     * @param clazzList   找到class以后存放的集合
     * @param annotation  预期的注解类型
     */
    public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive,
                                                List<Class> clazzList, Class<? extends Annotation> annotation) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 在给定的目录下找到所有的文件，并且进行条件过滤
        File[] dirFiles = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
                boolean acceptClass = file.getName().endsWith("class");// 接受class文件
                return acceptDir || acceptClass;
            }
        });
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzList, annotation);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> loadClass = Thread.currentThread().getContextClassLoader()
                            .loadClass(packageName + "." + className);
                    if (annotation != null) {
                        if (loadClass.isAnnotationPresent(annotation)) {
                            clazzList.add(loadClass);
                        }
                        continue;
                    }
                    clazzList.add(loadClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}