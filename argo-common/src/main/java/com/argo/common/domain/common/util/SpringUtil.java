package com.argo.common.domain.common.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SpringUtil {
    public static String DOMAIN_PACKAGE = "com/argo/common/domain";

    public List<String>  getAnnotatedClassesFromDomainPackage(Class annotation) {
        return getAnnotatedClassNames(annotation, DOMAIN_PACKAGE);
    }

    public List<String> getAnnotatedClassNames(Class annotation, String packageName) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        provider.addIncludeFilter(new AnnotationTypeFilter(annotation));

        Set<BeanDefinition> components = provider.findCandidateComponents(packageName);
        return components.stream().map(BeanDefinition::getBeanClassName).collect(Collectors.toList());
    }

    public List<String> getSubclassNamesFromDomainPackage(Class clazz) {
        return getSubClassNames(clazz, DOMAIN_PACKAGE);
    }

    public List<String> getSubClassNames(Class clazz, String packageName) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(clazz));

        Set<BeanDefinition> components = provider.findCandidateComponents(packageName);
        return components.stream().map(BeanDefinition::getBeanClassName).collect(Collectors.toList());
    }


}
