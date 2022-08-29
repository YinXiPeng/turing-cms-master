package com.bonc.turing.cms.exercise.controller;

import com.bonc.turing.cms.exercise.service.ImportService;
import com.bonc.turing.cms.manage.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/import")
@RestController
public class ImportController {

    @Autowired
    private ImportService importService;

    @RequestMapping("poi")
    public Object poi(@RequestParam("file") MultipartFile file){

        try {
            String s = importService.importPoi(file);
            if ("success".equals(s)){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","导入用户信息成功"));
            }else {
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"",s));
            }
        } catch (Exception e) {
            log.error("poi is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","导入用户信息失败"));
        }

    }
}
