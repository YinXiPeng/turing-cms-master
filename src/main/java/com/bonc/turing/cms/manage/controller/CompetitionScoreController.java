package com.bonc.turing.cms.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.dto.CompetitionScoreDTO;
import com.bonc.turing.cms.manage.entity.CmsCompetitionScore;
import com.bonc.turing.cms.manage.repository.CompetitionScoreRepository;
import com.bonc.turing.cms.manage.service.ICompetitionScoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping({"/cmsCompetitionList/"})
public class CompetitionScoreController {


    @Autowired
    ICompetitionScoreService iCompetitionScoreService;

    @Autowired
    CompetitionScoreRepository competitionScoreRepository;
    /**
     * 竞赛列表
     * @param params
     * @param guid
     * @return
     */
    @RequestMapping(value = "competitionInfoList", method = RequestMethod.POST)
    public Object findCompetitions(@RequestBody(required = true)JSONObject params , @RequestParam("guid") String guid){
        try {
            Object info = iCompetitionScoreService.judgeJoinCompetitionList(guid,params);
            if (null!=info){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, info));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, new JSONObject()));

    }

    /**
     * 评分列表
     * @param
     * @return
     */
    @RequestMapping(value = "findTeams",method = RequestMethod.POST)
    public Object findTeam(@RequestBody(required = true)JSONObject params){
        String guid = params.getString("guid");
        String competitionId = params.getString("competitionId");
        Object team = iCompetitionScoreService.findTeam(guid, competitionId,params);
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, team));
    }

    /**
     * 查询评分详情
     * @param request
     * @return
     */
    @RequestMapping(value = "findScoreInfo",method = RequestMethod.GET)
    public Object findScoreInfo(HttpServletRequest request){
        String scoreId = request.getParameter("scoreId");
        if (scoreId==null||scoreId.equals("")){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE, "评分id不能为空"));
        }else {
            CompetitionScoreDTO scoreDTO = iCompetitionScoreService.findScore(scoreId);
            if (scoreDTO==null){
                return "没有此评分项";
            }else
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, scoreDTO));
        }
    }

    /**
     * 提交保存竞赛
     * @param params
     * nyb
     * @return
     */
    @RequestMapping(value = "submitScores",method = RequestMethod.POST)
    public Object submitScores(@RequestBody(required = true)JSONObject params){
        String scoreId = params.getString("scoreId");
        Object scores = iCompetitionScoreService.submitScores(scoreId, params);
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, scores));

    }

    /**
     * 撤回评分
     * @param request
     * @return
     */
    @RequestMapping(value = "deleteScore" , method = RequestMethod.GET)
    public Object deleteScore(HttpServletRequest request){
        String scoreId = request.getParameter("scoreId");

        CmsCompetitionScore cmsCompetitionScore = competitionScoreRepository.findByScoreId(scoreId);
            if (null==cmsCompetitionScore){
                return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"评分已被撤回"));
            } else {
                String delete = iCompetitionScoreService.deleteByScoreId(scoreId);
                JSONObject jsonObject = new JSONObject();
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK, jsonObject));
        }
    }
}
