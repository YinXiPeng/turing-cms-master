package com.bonc.turing.cms.manage.controller.competition;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.ResultEntity;
import com.bonc.turing.cms.manage.service.competition.impl.CompetitionRankServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/cmsCompetitionRank"})
public class CompetitionRankController {

    private static Logger logger = LoggerFactory.getLogger(CompetitionRankController.class);

    @Autowired
    private CompetitionRankServiceImpl competitionRankService;

    @GetMapping("/rank")
    public Object getRanking(@RequestParam("id") String id,
                                         @RequestParam(value = "offsetPage", defaultValue = "0") Integer offsetPage,
                                         @RequestParam(value = "pageSize", defaultValue = "30") Integer pageSize) {

        JSONObject result = competitionRankService.getRanking(id, offsetPage, pageSize);
        if (null == result){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,result,"不存在：" + id + "的竞赛"));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,result,"查询B榜排行成功"));
    }

    @GetMapping("/getPythonFile")
    public Object getPythonFile(@RequestParam("notebookId") String notebookId) {
        JSONObject data = competitionRankService.getPythonFile(notebookId);
        if(null == data){
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","请求Python文件失败"));
        }
        return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,data));

    }

    @PostMapping("/savePythonFile")
    public Object savePythonFile(@RequestBody JSONObject param){
        try {
            JSONObject content = param.getJSONObject("fileContent");
            String notebookId = param.getString("notebookId");
            competitionRankService.savePythonFile(notebookId,content);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.OK,"","保存文件成功"));
        }catch (Exception e){
            logger.error("savePythonFile exception",e);
            return ResponseEntity.ok().body(new ResultEntity(ResultEntity.ResultStatus.FAILURE,"","保存文件失败"));
        }
    }

}
