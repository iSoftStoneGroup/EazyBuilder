package com.eazybuilder.ci.util;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public class DtoEntityUtil {
    
    static Mapper mapper = new DozerBeanMapper();

    public static <D,E> E trans(D t,Class<E> clazz){
        if(t == null)
            return null;
        return mapper.map(t, clazz);
    }
    
    public static <D,E> List<E> trans(D[] ts,Class<E> clazz){        
        List<E> es = new ArrayList<E>();
        if(ts == null)
            return es;
        
        for(D d:ts) {
            E e = (E)trans(d,clazz);
            if(e != null)
                es.add(e);
        }
        
        return es;
    }
    
    
    public static <D,E> List<E> trans(List<D> ts,Class<E> clazz){
        List<E> es = new ArrayList<E>();
        if(ts == null )
            return es;
        for(D d:ts) {
            E e = (E)trans(d,clazz);
            if(e != null)
                es.add(e);
        }
        return es;
    }
    
}