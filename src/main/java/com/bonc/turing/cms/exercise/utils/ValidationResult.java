package com.bonc.turing.cms.exercise.utils;

import lombok.Data;

import java.util.List;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/10 16:16
 */
@Data
public class ValidationResult {

    /**
     * 校验结果是否有错
     */
    private boolean hasErrors;

    /**
     * 校验错误信息
     */
    private List<KeyError> keyErrors;
}
