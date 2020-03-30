package cn.bytecloud.smartCommunity.util;


import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author zhanlang
 */
public class EntityUtil {

    /**
     * entity转dto
     *
     * @param entity 字段多的实体
     */
    public static <T> T entityToDto(Object entity, Class<T> dtoClass) {
        if (entity == null || dtoClass == null) {
            return null;
        }
        T dto = null;
        try {
            dto = dtoClass.newInstance();

            Class<?> entityClass = entity.getClass();
            //暴力反射获取所有字段(包括私有)
            Field[] entityFields = entityClass.getDeclaredFields();
            Field[] entitySuperClassFields = entityClass.getSuperclass().getDeclaredFields();
            Field[] dtoFields = dtoClass.getDeclaredFields();
            Field[] dtoSuperClassFields = dtoClass.getSuperclass().getDeclaredFields();

            List<Field> entityClassFields = new ArrayList<>();
            List<Field> dtoClassFields = new ArrayList<>();

            entityClassFields.addAll(Arrays.asList(entityFields));
            entityClassFields.addAll(Arrays.asList(entitySuperClassFields));

            dtoClassFields.addAll(Arrays.asList(dtoFields));
            dtoClassFields.addAll(Arrays.asList(dtoSuperClassFields));

            //赋值
            for (Field dtoField : dtoClassFields) {
                String dtoFieldName = dtoField.getName();
                Class<?> dtoFieldType = dtoField.getType();
                if (dtoFieldName.equals("serialVersionUID")) {
                    continue;
                }
                //类型和名称相同就进行赋值
                for (Field entityField : entityClassFields) {
                    if (dtoFieldName.equals(entityField.getName()) && dtoFieldType == entityField.getType()) {
                        //暴力访问
                        dtoField.setAccessible(true);
                        entityField.setAccessible(true);
                        dtoField.set(dto, entityField.get(entity));
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }


    /**
     * List<entity> 转成List<dto>
     */
    public static <T> List<T> entityListToDtoList(List entityList, Class<T> tClass) {
        List<T> dtoList = new ArrayList<T>();
        for (Object entity : entityList) {
            T t = entityToDto(entity, tClass);
            dtoList.add(t);
        }
        return dtoList;
    }

}