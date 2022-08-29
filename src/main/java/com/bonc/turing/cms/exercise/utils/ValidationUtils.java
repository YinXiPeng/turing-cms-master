package com.bonc.turing.cms.exercise.utils;


import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/10 16:15
 */

public class ValidationUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidationResult validateEntity(T obj) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (CollectionUtils.isNotEmpty(set)) {
            result.setHasErrors(true);
            List<KeyError> keyErrors = new ArrayList<>(set.size());
            for (ConstraintViolation<T> cv : set) {
                keyErrors.add(KeyError.builder().filed(cv.getPropertyPath().toString()).errorMessage(cv.getMessage()).build());
            }
            result.setKeyErrors(keyErrors);
        }
        return result;
    }

    public static <T> ValidationResult validateProperty(T obj, String propertyName) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
        if (CollectionUtils.isNotEmpty(set)) {
            result.setHasErrors(true);
            List<KeyError> keyErrors = new ArrayList<>(set.size());
            for (ConstraintViolation<T> cv : set) {
                keyErrors.add(KeyError.builder().filed(propertyName).errorMessage(cv.getMessage()).build());
            }
            result.setKeyErrors(keyErrors);
        }
        return result;
    }
}