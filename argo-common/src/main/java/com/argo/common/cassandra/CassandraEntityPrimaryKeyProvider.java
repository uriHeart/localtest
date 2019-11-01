package com.argo.common.cassandra;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CassandraEntityPrimaryKeyProvider {
    private static List<Field> getKeyFields(Object object) {
        List<Field> fields = new ArrayList<>();
        ReflectionUtils.doWithFields(object.getClass(), fields::add);
        return fields.stream()
                .filter(f -> f.isAnnotationPresent(PrimaryKeyColumn.class))
                .sorted(Comparator.comparingInt(f -> f.getAnnotation(PrimaryKeyColumn.class).ordinal()))
                .collect(Collectors.toList());
    }

    public static Map getPrimaryKeyMap(Object obj) {
        Map<String, Object> result = Maps.newHashMap();
        getKeyFields(obj).forEach(
                f -> {
                    f.setAccessible(true);
                    try {
                        String fieldName = f.getName();
                        Object fieldValue = f.get(obj);

                        result.put(fieldName, fieldValue);
                    } catch (IllegalAccessException e) {
                        throw new CassandraPrimaryKeyExtractionException(e);
                    }
                }
        );
        return result;
    }
}
