package com.eazybuilder.ci.event;


import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;

@Service
public class EventBusSupport implements ApplicationContextAware{

	EventBus bus=new EventBus();
	
	public void registerListener(Object listener){
		bus.register(listener);
	}
	
	public void postEvent(Object event){
		bus.post(event);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String,Object> listeners=applicationContext.getBeansWithAnnotation(EventListener.class);
		if(listeners!=null&&listeners.size()>0){
			for(Entry<String,Object> entry:listeners.entrySet()){
				registerListener(entry.getValue());
			}
		}
	}
}
