package com.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        String dirName = "user-photos";
        Path userPhotosDir = Paths.get(dirName);

        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
        // wild card to accept all file under user-photos dir
        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");


        WebMvcConfigurer.super.addResourceHandlers(registry);
        //categories image is at the same level as front end and back end
        String categoriesDirName = "../categories-images";
        Path categoriesPhotosDir = Paths.get(categoriesDirName);

        String categoriesPhotosPath = categoriesPhotosDir.toFile().getAbsolutePath();
        // wild card to accept all file under user-photos dir
        registry.addResourceHandler("/categories-images/**")
                .addResourceLocations("file:/" + categoriesPhotosPath + "/");
    }
}
