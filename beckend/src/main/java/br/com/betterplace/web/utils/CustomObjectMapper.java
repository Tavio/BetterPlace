package br.com.betterplace.web.utils;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * Reference:
 * http://loianegroner.com/2010/09/how-to-serialize-java-util-date-with-jackson-json-processor-spring-3-0/
 */
public class CustomObjectMapper extends ObjectMapper {

    @SuppressWarnings("deprecation")
    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        SerializationConfig s = this.getSerializationConfig();
        s.setDateFormat(new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy"));
    }
}