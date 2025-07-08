package fa.training.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	//For book cover
        registry.addResourceHandler("/books/**")
                .addResourceLocations("file:resources/books/");
        //For book overview
        registry.addResourceHandler("/overview/**")
        		.addResourceLocations("file:resources/overview/");
    }
}