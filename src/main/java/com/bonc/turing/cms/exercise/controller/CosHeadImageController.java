package com.bonc.turing.cms.exercise.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.annotation.RoleJudge;
import com.bonc.turing.cms.enums.UserRoleEnum;
import com.bonc.turing.cms.exercise.domain.CosHeadImage;
import com.bonc.turing.cms.exercise.service.CosHeadImageService;
import com.bonc.turing.cms.manage.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("cosHeadImage")
@Slf4j
public class CosHeadImageController {

    @Autowired
    private CosHeadImageService cosHeadImageService;

    /**
     * @Author lky
     * @Description 查询首页图片列表
     * @Date 10:40 2020/6/29
     * @Param
     * @return
     **/
    @RoleJudge(role = UserRoleEnum.CODE_2001)
    @GetMapping("getHeadImageList")
    public Object getHeadImageList(@RequestParam(value = "guid",required = false,defaultValue = "")String guid,@RequestParam("schoolId")String schoolId){
        try {
            List headImagesList = cosHeadImageService.getHeadImagesList(guid,schoolId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,headImagesList,"查询首页图片列表成功"));
        } catch (Exception e) {
            log.error("getHeadImageList is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,new ArrayList<>(),"查询首页图片列表失败"));
        }
    }

    /**
     * @Author lky
     * @Description 新增或编辑图片
     * @Date 10:41 2020/6/29
     * @Param
     * @return
     **/
    @RoleJudge(role = UserRoleEnum.CODE_2001)
    @PostMapping("setHeadImage")
    public Object setHeadImage(@RequestBody CosHeadImage cosHeadImage){
        try {
            cosHeadImageService.setHeadImage(cosHeadImage);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","修改图片信息成功"));
        } catch (Exception e) {
            log.error("setHeadImage is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","修改图片信息失败"));
        }
    }

    /**
     * @Author lky
     * @Description 设置轮播图展示顺序
     * @Date 11:26 2020/6/30
     * @Param
     * @return
     **/
    @PostMapping("setSequence")
    public Object setSequence(@RequestBody CosHeadImage cosHeadImage) {
        try {
            cosHeadImageService.setSequence(cosHeadImage);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, "", "修改图片信息成功"));
        } catch (Exception e) {
            log.error("setHeadImage is failed", e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "", "修改图片信息失败"));
        }
    }


    /**
     * @Author lky
     * @Description 删除图片
     * @Date 10:48 2020/6/29
     * @Param
     * @return
     **/
    @RoleJudge(role = UserRoleEnum.CODE_2001)
    @PostMapping("delHeadImage")
    public Object delHeadImage(@RequestBody JSONObject jsonObject){
        try {
            String headImageId = jsonObject.getString("headImageId");
            cosHeadImageService.delHeadImage(headImageId);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","删除图片信息成功"));
        } catch (Exception e) {
            log.error("delHeadImage is failed",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","删除图片信息失败"));
        }
    }
}
