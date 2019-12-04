package com.argo.api.controller.convert;

import com.argo.common.domain.common.data.DataConversionService;
import com.argo.common.domain.common.data.SourceData;
import com.argo.common.domain.common.data.TargetData;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplate;
import com.argo.common.domain.common.data.conversion.template.ConversionTemplateService;
import com.argo.common.domain.common.data.conversion.template.ConversionType;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationMethod;
import com.argo.common.domain.common.data.conversion.template.annotation.ConversionOperationService;
import com.argo.common.domain.common.util.ReflectionUtil;
import com.argo.common.domain.common.util.SpringUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/data")
@AllArgsConstructor
public class ConversionTemplateController {

    private ConversionTemplateService conversionTemplateService;

    private DataConversionService dataConversionService;

    private SpringUtil springUtil;

    @PostMapping("/conversionTemplate")
    public void addConversionTemplate(@RequestParam String targetId, @RequestParam String sourceId,
                               @RequestParam Long expiredAt, @RequestParam String vendorItemId) {

        ConversionTemplate conversionTemplate = ConversionTemplate.builder()
                .targetId(targetId)
                .sourceId(sourceId)
                .expiredAt(new Date(expiredAt))
                .createdAt(new Date())
                .build();
        conversionTemplateService.save(conversionTemplate);
    }

    @PostMapping("/conversionRule")
    public void addConversionRule(@RequestBody ConversionRuleForm conversionRuleForm) {
    }

    @GetMapping("/getConversionTypes")
    public ResponseEntity<List<ConversionType>> getConversionTypes() {
        return ResponseEntity.ok(Arrays.asList(ConversionType.values()));
    }

    @GetMapping("/getOperatorClassCandidates")
    public ResponseEntity<List<String>> getBeanNames() {
        return ResponseEntity.ok(springUtil.getAnnotatedClassesFromDomainPackage(ConversionOperationService.class));
    }

    @GetMapping("/getOperatorMethodCandidates")
    public ResponseEntity<List<String>> getMethods(@RequestParam String operatingClass) {
        Class clazz = null;
        try {
            clazz = ClassUtils.getClass(operatingClass);
        } catch (ClassNotFoundException e) {

        }
        List<String> methodNames = MethodUtils.getMethodsListWithAnnotation(clazz, ConversionOperationMethod.class).stream().map(Method::getName).collect(Collectors.toList());
        return ResponseEntity.ok(methodNames);
    }

    @GetMapping("/getTargetList")
    public ResponseEntity<List<String>> getTargetList() {
        List<String> targetList = springUtil.getSubclassNamesFromDomainPackage(TargetData.class);
        return ResponseEntity.ok(targetList);
    }

    @GetMapping("/getTargetFields")
    public ResponseEntity<List<String>> getTargetFields(@RequestParam String targetClass) {
        return ResponseEntity.ok(ReflectionUtil.getAllFieldsForClass(targetClass));
    }

    @GetMapping("/getSourceList")
    public ResponseEntity<List<String>> getSourceList() {
        List<String> targetList = springUtil.getSubclassNamesFromDomainPackage(SourceData.class);
        return ResponseEntity.ok(targetList);
    }

    @GetMapping("/getPrimitiveTypes")
    public ResponseEntity<List<String>> getPrimitiveTypes() {
        List<String> primitiveTypes = Lists.newArrayList();
        //primitiveTypes.add(Byte.class.getName());
        //primitiveTypes.add(Short.class.getName());
        //primitiveTypes.add(Float.class.getName());
        primitiveTypes.add(Integer.class.getName());
        primitiveTypes.add(Long.class.getName());
        primitiveTypes.add(Double.class.getName());
        primitiveTypes.add(String.class.getName());
        primitiveTypes.add(Boolean.class.getName());
        return ResponseEntity.ok(primitiveTypes);
    }

/*
    @RequestMapping("/monday")
    public ResponseEntity<DayOfWeek> monday() {
        return new ResponseEntity<DayOfWeek>(DayOfWeek.MONDAY, HttpStatus.OK);
    }

    @RequestMapping("/days")
    public ResponseEntity<List<DayOfWeek>> days() {
        return new ResponseEntity<List<DayOfWeek>>(Arrays.asList(DayOfWeek.values()), HttpStatus.OK);
    }
    */
}
