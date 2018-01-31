package br.com.icaropinhoe.adapter;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class DevDojoAdapter extends WebMvcConfigurerAdapter{

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver pHandler = new PageableHandlerMethodArgumentResolver();
		pHandler.setFallbackPageable(new PageRequest(0, 10));
		argumentResolvers.add(pHandler);
	}
	
}
