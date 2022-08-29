package com.bonc.turing.cms.manage.service.competition.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.NotebookUtil;
import com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo;
import com.bonc.turing.cms.manage.mapper.model.NotebookMapper;
import com.bonc.turing.cms.manage.mapper.result.ResultMapper;
import com.bonc.turing.cms.manage.repository.CompetitionRepository;
import com.bonc.turing.cms.manage.service.competition.CompetitionRankService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CompetitionRankServiceImpl implements CompetitionRankService {

    private static Logger logger = LoggerFactory.getLogger(CompetitionRankServiceImpl.class);

    @Resource
    private NotebookMapper notebookMapper;

    @Resource
    private ResultMapper resultMapper;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Value("${notebook.image}")
    private String notebookImage;

    @Value("${notebook.url}")
    private String notebookUrl;

    @Value("${notebook.list}")
    private String notebookList;


    @Override
    public JSONObject getRanking(String id, Integer offsetPage, Integer pageSize) {
        JSONObject result = new JSONObject();
        JSONObject page = new JSONObject();
        List<Map<String,Object>> results = new ArrayList<>();
        Map<String, Object> env = new HashMap<>();
        Optional<CmsCompetitionInfo> optional = competitionRepository.getCompetitionInfoByCompetitionId(id);
        if(optional.isPresent()){
            CmsCompetitionInfo competitionInfo = optional.get();
            //是否团队赛
            Integer isNeedTeam = competitionInfo.getIsNeedTeam();
            PageHelper.startPage(offsetPage,pageSize);
            if(0 == isNeedTeam){ //不是团队赛
                results = resultMapper.getRankOfIndividualCompetitionByCompetitionId(id,offsetPage,pageSize);
            }else {
                results = resultMapper.getRankOfTeamCompetitionByCompetitionId(id,offsetPage,pageSize);
            }
            if(results.size() > 0){
                for (Map map : results) {
                    String notebookPath = String.valueOf(map.get("notebookPath"));
                    if(!"null".equals(notebookPath)){
                        String[] split = notebookPath.split("/");
                        map.put("username",split[1]);
                        map.put("pyFileName",split[2].replaceAll(".ipynb",".py"));
                    }
                }
            }
            PageInfo pageInfo = new PageInfo(results);
            env.put("URL_NAME",notebookUrl);
            env.put("JUPYTER_LISTNAME",notebookList);
            env.put("image",notebookImage);
            result.put("type",isNeedTeam);
            page.put("offsetPage",offsetPage);
            page.put("pageSize",pageSize);
            page.put("totalElements",pageInfo.getTotal());
            result.put("page",page);
            result.put("results",results);
            result.put("env",env);
            return result;
        }else {
            return null;
        }
    }

    @Override
    public JSONObject getPythonFile(String id) {
        HashMap<String, Object> notebook = notebookMapper.getNotebook(id);
        String path = notebook.get("notebookPath").toString();
        path = path.replace(path.substring(path.lastIndexOf(".")+1),"py");
        JSONObject content = null;
        try {
            content = NotebookUtil.getContent(path);
        } catch (Exception e) {
            logger.error("请求Python文件失败",e);
        }
        return content;
    }

    @Override
    public void savePythonFile(String id,JSONObject content) throws Exception{
        HashMap<String, Object> notebook = notebookMapper.getNotebook(id);
        String path = notebook.get("notebookPath").toString();
        path = path.replace(path.substring(path.lastIndexOf(".")+1),"py");
        NotebookUtil.saveContent(null,path,content);

    }

}
