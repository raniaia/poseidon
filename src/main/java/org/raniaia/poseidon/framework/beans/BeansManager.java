package org.raniaia.poseidon.framework.beans;

import javassist.ClassPool;
import org.raniaia.poseidon.extension.ConnectionPool;
import org.raniaia.poseidon.framework.log.Log;
import org.raniaia.poseidon.framework.log.LogAdapter;
import org.raniaia.poseidon.framework.annotation.Resource;
import org.raniaia.poseidon.framework.annotation.Valid;
import org.raniaia.poseidon.framework.cache.CacheRefreshTimer;
import org.raniaia.poseidon.framework.cache.PoseidonCache;
import org.raniaia.poseidon.framework.config.GlobalConfig;
import org.raniaia.poseidon.framework.db.JdbcSupport;
import org.raniaia.poseidon.framework.db.NativeResult;
import org.raniaia.poseidon.framework.cache.PoseidonCacheImpl;
import org.raniaia.poseidon.framework.loader.PoseidonClassPool;
import org.raniaia.poseidon.framework.log.slf4j.Slf4jAdapter;
import org.raniaia.poseidon.framework.mapper.MapperInvocation;
import org.raniaia.poseidon.framework.timer.Timer;
import org.raniaia.poseidon.framework.timer.TimerManager;
import org.raniaia.poseidon.framework.tools.ReflectUtils;
import org.raniaia.poseidon.framework.tools.StringUtils;
import org.raniaia.poseidon.framework.db.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ioc容器对象
 * 这个容器只适用于Poseidon
 *
 * ioc container object.
 * this container just be applicable for poseidon.
 *
 * Copyright: Create by TianSheng on 2019/11/28 17:25
 *
 * @author TianSheng
 * @version 1.0.0
 * @since 1.8
 *
 */
@SuppressWarnings({"unchecked"})
public class BeansManager {

    volatile static Map<String, Object> beans = new ConcurrentHashMap<>();
    volatile static Map<String, Object> mapperBeans = new ConcurrentHashMap<>();

    static {
        beans.put("logAdapter", getLogAdapter());
    }

    static Log logger = ContextApplication.getLog(BeansManager.class);

    @Resource
    private NativeJdbc newNativeJdbc() {
        return new NativeJdbcImpl();
    }

    @Resource(name = "jdbc")
    private JdbcSupport newJdbcSupport() {
        return new JdbcSupportImpl();
    }

    @Resource(name = "cache")
    private PoseidonCache newPoseidonCache() {
        PoseidonCache cache = new PoseidonCacheImpl();
        Timer timer = new CacheRefreshTimer(cache);
        TimerManager.getManager().submit(timer);
        return cache;
    }

    @Resource(name = "pool")
    private ConnectionPool newConnectionPool() {
        return new org.raniaia.poseidon.framework.pool.ConnectionPool();
    }

    @Resource(name = "classPool")
    private ClassPool getClassPool() throws ClassNotFoundException {
        PoseidonClassPool pool = new PoseidonClassPool(true);
        return pool;
    }

    /**
     * 创建一个结果集
     */
    public static NativeResult newNativeResult() {
        return new NativeResultMysql();
    }

    /**
     * Logger bean比较特殊，由于本类也要使用它，所以在最开始的时候就放入{@link #beans}中s
     * This bean was placed in the map at the beginning.
     */
    private static LogAdapter getLogAdapter() {
        return new Slf4jAdapter();
    }

    // get bean
    static <T> T getBean(String name) {
        return (T) factory(name); // (T) beans.get(name);
    }

    // 获取mapper映射对象
    static <T> T getMapper(Class<T> clazz) {
        String simpleName = clazz.getSimpleName();
        Object sqlMapper = mapperBeans.get(simpleName);
        if (sqlMapper == null) {
            Object invoker = MapperInvocation.invoker(clazz);
            mapperBeans.put(simpleName, invoker);
            sqlMapper = mapperBeans.get(simpleName);
        }
        return (T) sqlMapper;
    }

    public static <T> T putBean(String name, Object beanObject) {
        put(name, beanObject);
        return (T) getBean(name);
    }

    private static Object factory(String name) {
        try {
            Object bean = beans.get(name);
            if (bean != null) return bean;
            Class<?> target = BeansManager.class;
            Object instance = target.newInstance();
            Method[] methods = target.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Resource.class)) {
                    String aname = method.getDeclaredAnnotation(Resource.class).name();
                    // 如果没开启缓存则不将缓存实例化
                    if ("cache".equals(aname) && !GlobalConfig.getConfig().getCache()) {
                        continue;
                    }
                    if (name.equals(aname)) {
                        put(name, ReflectUtils.invoke(method, instance));
                        return beans.get(name);
                    }
                    String ReturnName = method.getReturnType().getSimpleName();
                    if (name.equals(ReturnName)) {
                        put(name, ReflectUtils.invoke(method, instance));
                        return beans.get(name);
                    }

                }
            }
        } catch (Exception e) {
            logger.error("bean name \"" + name + "\" is not found");
        }
        return null;
    }

    private static void put(String name, Object value) {
        beans.put(name, inject(value));
        logger.info("add bean[" + name + "]: " + value.getClass().getName());
    }

    /**
     * 对象中是存在需要注入的成员
     *
     * @param object 目标对象
     * @return
     */
    private static Object inject(Object object) {
        try {
            Class<?> target = object.getClass();
            Field[] fields = target.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Valid.class)) {
                    Valid valid = field.getDeclaredAnnotation(Valid.class);
                    String name = valid.name();
                    if (StringUtils.isEmpty(name)) {
                        String typeName = field.getType().getTypeName();
                        typeName = typeName.substring(typeName.lastIndexOf(".") + 1);
                        Object inject = factory(typeName);
                        field.set(object, inject);
                    } else {
                        Object inject;
                        if ("logger".equals(name)) {
                            inject = ((LogAdapter) beans.get("logAdapter")).getLog(target);
                        } else {
                            inject = factory(name);
                        }
                        field.set(object, inject);
                    }
                }
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}