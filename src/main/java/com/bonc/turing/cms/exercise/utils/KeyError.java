package com.bonc.turing.cms.exercise.utils;

import lombok.Builder;
import lombok.Data;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/10 16:44
 */
@Data
@Builder
public class KeyError {

    private String filed;

    private String errorMessage;

}
