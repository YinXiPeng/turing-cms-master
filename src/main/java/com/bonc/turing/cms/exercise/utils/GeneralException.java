package com.bonc.turing.cms.exercise.utils;

import com.bonc.turing.cms.manage.ResultEntity;
import lombok.Data;

/**
 * description:
 *
 * @author lxh
 * @date 2020/2/21 15:52
 */
@Data
public class GeneralException extends RuntimeException {

    /**
     * 错误码
     */
    private ResultEntity.ResultStatus status;
    /**
     * 错误信息
     */
    private String msg;

    public GeneralException(ResultEntity.ResultStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

}
