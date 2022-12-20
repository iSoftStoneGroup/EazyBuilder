package com.eazybuilder.ga.untils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CurrencyUntil {
    /**
     * 对象转map
     * @param object
     * @return
     */
    public static Map<String, String> convertBeanToMap(Object object)
    {
        if(object == null){
            return null;
        }
        Map<String, String> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(object);
                    map.put(key, value.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
