package com.Proyect.Vircade.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "dqa4rvuqd",
                "api_key", "842462633174474",
                "api_secret", "I1x_VsMmWRBlW53n6PyHJY764FU"
        );
        return new Cloudinary(config);
    }
}