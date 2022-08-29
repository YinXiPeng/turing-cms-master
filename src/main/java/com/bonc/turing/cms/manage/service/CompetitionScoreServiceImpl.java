package com.bonc.turing.cms.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.common.utils.StringUtils;
import com.bonc.turing.cms.manage.dto.CompetitionScoreDTO;
import com.bonc.turing.cms.manage.entity.*;
import com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo;
import com.bonc.turing.cms.manage.entity.competition.CmsScoring;
import com.bonc.turing.cms.manage.entity.user.SysUserInfo;
import com.bonc.turing.cms.manage.mapper.CompetitionInfoMapper;
import com.bonc.turing.cms.manage.mapper.result.ResultMapper;
import com.bonc.turing.cms.manage.repository.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class CompetitionScoreServiceImpl implements ICompetitionScoreService {
    @Autowired
    CompetitionRepository competitionRepository;

    @Autowired
    JudgeRepository judgeRepository;

    @Autowired
    TeamsCompetitionRepository teamsCompetitionRepository;

    @Autowired
    TeamsInfoRepository teamsInfoRepository;

    @Autowired
    CmsScoringRepository cmsScoringRepository;

    @Autowired
    CompetitionScoreRepository competitionScoreRepository;

    @Autowired
    UserParticipateCompetitionRepository userParticipateCompetitionRepository;

    @Autowired
    SysUserInfoRepository sysUserInfoRepository;

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    TeamsMemberInfoRepository teamsMemberInfoRepository;

    @Autowired
    CompetitionInfoMapper competitionInfoMapper;

    @Autowired
    ResultMapper resultMapper;

    /**
     * 所有竞赛列表，弃用
     * @param params
     * @return
     */
    @Override
    public Object findCompetitionInfo(JSONObject params) {

        String guid = params.getString("guid");

        JSONObject jsonObject = new JSONObject();
        try {

            String pageNumber = params.getString("pageNumber"); //起始页码
            String pageSize = params.getString("pageSize"); //显示条数
            Pageable pageable = PageRequest.of(Integer.valueOf(pageNumber) - 1, Integer.valueOf(pageSize));

            Page<CmsCompetitionInfo> competitionInfos = competitionRepository.findAll(pageable);

            ArrayList<Map<String, String>> list = new ArrayList<>();
            for (int i = 0; i < competitionInfos.getContent().size(); i++) {
                CmsCompetitionInfo cmsCompetitionInfo = competitionInfos.getContent().get(i);
                String competitionName = cmsCompetitionInfo.getCompetitionName();
                String competitionStatus = cmsCompetitionInfo.getCompetitionStatus();
                Map<String, String> map = new HashMap<>();
                map.put("order", String.valueOf(i + 1));
                map.put("competitionName", competitionName);
                map.put("competitionStatus", competitionStatus);
                list.add(map);
                jsonObject.put("competitionList", list);

            }
            long totalElements = competitionInfos.getTotalElements();
            int totalPages = competitionInfos.getTotalPages();
            jsonObject.put("totalElements", totalElements);
            jsonObject.put("totalPages", totalPages);
            return jsonObject;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 评委参与评分的竞赛列表
     */
    @Override
    public Object judgeJoinCompetitionList(String guid,JSONObject params){
        try {

            int isPreJudge =0;
            int isFinalJudge =0;
            List<Map<String, Object>> list = new ArrayList<>();
            List<CmsCompetitionInfo> competitionInfoList = competitionInfoMapper.findCompetition(guid);
            for (int i = 0 ; i < competitionInfoList.size() ; i++){
                CmsCompetitionInfo cmsCompetitionInfo = competitionInfoList.get(i);
                String competitionId = cmsCompetitionInfo.getCompetitionId();
                Judge finalJudge = judgeRepository.findFinalJudgeByJudgeId(guid, competitionId);
                Judge otherByJudge = judgeRepository.findOtherByJudgeId(guid, competitionId);
                if (null!=finalJudge){
                    isFinalJudge = 1;
                }
                if (null!=otherByJudge){
                    isPreJudge = 1;
                }
                String competitionName = cmsCompetitionInfo.getCompetitionName();
                String competitionStatus = cmsCompetitionInfo.getCompetitionStatus();
                Integer isNeedTeam = cmsCompetitionInfo.getIsNeedTeam();
                Map<String, Object> map = new HashMap<>();
                map.put("isNeedTeam",String.valueOf(isNeedTeam));
                map.put("isPreJudge",isPreJudge);
                map.put("isFinalJudge",isFinalJudge);
                map.put("competitionId", competitionId);
                map.put("order", String.valueOf(i + 1));
                map.put("competitionName", competitionName);
                map.put("competitionStatus", competitionStatus);
                isFinalJudge =0;
                isPreJudge=0;
                list.add(map);
            }


            //分页
            int totalRecord = list.size();
            int pageNum = Integer.parseInt(params.getString("pageNumber"));
            int  pageSize = Integer.parseInt(params.getString("pageSize"));
            PageBean pageBean = new PageBean(pageNum, pageSize, totalRecord);
            int first = 0;
            int last = 0;
            if (pageSize>totalRecord){
                last = totalRecord;
            }else {
                last = pageNum*pageSize;
                first = pageSize * (pageNum - 1);
            }
            if (pageNum > pageBean.getTotalPage() || pageBean.getTotalPage()==0) {
                last = totalRecord;
            }
            if (first>list.size()){
                first=last;
            }
            if (pageNum == pageBean.getTotalPage()|| pageBean.getTotalPage()==0) {
                last = totalRecord;
            }
            List<Map<String, Object>> subList = list.subList(first, last);
            pageBean.setList(subList);
            return pageBean;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提交评分
     * @param guid
     * @param params
     * @return
     */
    @Override
    public String submitScore(String guid,String scoreId,JSONObject params) {
        String competitionId = params.getString("competitionId");
        Optional<CmsCompetitionInfo> op = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
        CmsCompetitionInfo competitionInfo = op.get();

        Integer isNeedTeam = competitionInfo.getIsNeedTeam();
        String competitionStatus = competitionInfo.getCompetitionStatus();
        CmsCompetitionScore cmsCompetitionScore = new CmsCompetitionScore();
        CmsCompetitionScore competitionScore = competitionScoreRepository.getOne(scoreId);

        cmsCompetitionScore.setIsNeedTeam(isNeedTeam);

        if (competitionStatus.equals("5")){
            cmsCompetitionScore.setFinalFlag(0);
            String teamName = cmsCompetitionScore.getTeamName();


        }else if (competitionStatus.equals("8")){
            cmsCompetitionScore.setFinalFlag(1);
        }else
            return "当前状态不可评分";

        return null;
    }

    /**
    * @Author:nyb
    * @DESC:查参赛人/团
    * @Date: Created in 09:39 2019/8/21 0021
    * @Modified By:
    */
    @Override
    public Object findTeam(String guid, String competitionId,JSONObject params) {
//        String competitionId = params.getString("competitionId");
        int pageNum = Integer.parseInt(params.getString("pageNum"));
        int  pageSize = Integer.parseInt(params.getString("pageSize"));
        SysUserInfo info = sysUserInfoRepository.getOne(guid);
        String msg = null;
        if (info==null){
            return "无此用户";
        }else {
//            CmsCompetitionInfo competitionInfo = competitionRepository.getOne(competitionId);
            Optional<CmsCompetitionInfo> op = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
            CmsCompetitionInfo competitionInfo = op.get();
            if (competitionInfo==null){
                return "无此竞赛";
            }else {
                Judge judge = judgeRepository.findOtherByJudgeId(guid,competitionId);
                Judge finalJudge = judgeRepository.findFinalJudgeByJudgeId(guid, competitionId);
                Integer isNeedTeam = competitionInfo.getIsNeedTeam();
                String competitionStatus = competitionInfo.getCompetitionStatus();
                List<CmsScoring> cmsScoringList = new ArrayList<>();
                List<Map> rankList = new ArrayList<>();
                List<CompetitionScoreDTO> list = new ArrayList<>();
                if ("8".equals(competitionStatus) && null != finalJudge) {
                    cmsScoringList = cmsScoringRepository.findFinalByCompetitionInfoId(competitionId);
                } else if ("5".equals(competitionStatus) && null != judge) {
                    cmsScoringList = cmsScoringRepository.findPreByCompetitionInfoId(competitionId);
                } else if ("9".equals(competitionStatus) && null != judge) {
                    cmsScoringList = cmsScoringRepository.findCommonByCompetitionInfoId(competitionId);
                }

                Map<String, Object> mapItem = new HashMap<>();
                Map<String, Object> mapWeight = new HashMap<>();
                for (CmsScoring cmsScoring : cmsScoringList) {
                    String scoringItem = cmsScoring.getScoringItem();
                    mapItem.put(scoringItem, "0");
                    Float weight = cmsScoring.getWeight();
                    mapWeight.put(scoringItem, weight);
                }

                com.github.pagehelper.Page<Object> pageResult = PageHelper.startPage(pageNum, pageSize);
                if(isNeedTeam == 1) {              //团队赛
                    rankList = resultMapper.getTotalScoreRankOfTeamMsg(competitionId, competitionStatus);
                }else if (isNeedTeam == 0) {
                    //个人赛
                    rankList = resultMapper.getTotalScoreRankMsg(competitionId,competitionStatus);
                }
                for (int i = 0;i<rankList.size();i++) {
                    Map map = rankList.get(i);
                    List<JSONObject> submissions = new ArrayList<>();
                    String[] filePaths = (String[])map.get("filePaths");
                    if (filePaths.length > 0) {
                        for (int m = 0; m < filePaths.length; m++) {
                            JSONObject fileInfo = new JSONObject();
                            String fileName = filePaths[m].substring(filePaths[m].lastIndexOf("/") + 1);
                            String string = filePaths[m];
                            fileInfo.put("result", string);
                            fileInfo.put("fileName", fileName);
                            submissions.add(fileInfo);
                        }
                    }
                    CmsCompetitionScore cmsCompetitionScore = new CmsCompetitionScore();
                    cmsCompetitionScore.setIsNeedTeam(isNeedTeam);
                    cmsCompetitionScore.setSubmitItem(JSON.toJSONString(mapItem));
                    cmsCompetitionScore.setDescription(map.get("memo").toString());
                    cmsCompetitionScore.setTeamName(map.get("name").toString());
                    cmsCompetitionScore.setTeamId(map.get("id").toString());
                    cmsCompetitionScore.setCompetitionId(competitionId);
                    cmsCompetitionScore.setSubmission(String.valueOf(submissions));
                    cmsCompetitionScore.setCompetitionName(competitionInfo.getCompetitionName());
                    if (competitionStatus.equals("5")) {
                        cmsCompetitionScore.setFinalFlag(0);    //初赛
                        CmsCompetitionScore id = competitionScoreRepository.findPreByCompetitionIdAndTeamId(competitionId, map.get("id").toString());
                        if (id == null) {
                            cmsCompetitionScore = competitionScoreRepository.save(cmsCompetitionScore);
                        }else {
                            cmsCompetitionScore = id;
                        }
                    } else if (competitionStatus.equals("8")) {
                        cmsCompetitionScore.setFinalFlag(1);    //决赛
                        CmsCompetitionScore id = competitionScoreRepository.findFinalByCompetitionIdAndTeamId(competitionId, map.get("id").toString());
                        if (id == null) {
                            cmsCompetitionScore = competitionScoreRepository.save(cmsCompetitionScore);
                        }else {
                            cmsCompetitionScore = id;
                        }
                    } else if (competitionStatus.equals("9")) {
                        cmsCompetitionScore.setFinalFlag(2);    //普通赛
                        CmsCompetitionScore id = competitionScoreRepository.findCommonByCompetitionIdAndTeamId(competitionId, map.get("id").toString());
                        if (id == null) {
                            cmsCompetitionScore = competitionScoreRepository.save(cmsCompetitionScore);
                        }else {
                            cmsCompetitionScore = id;
                        }
                    }

                    if(cmsCompetitionScore.getScoreId()!=null){
                        CompetitionScoreDTO scoreDTO = findScore(cmsCompetitionScore.getScoreId());
                        scoreDTO.setNum(String.valueOf(i+1));
                        scoreDTO.setSubmitItem(JSONObject.parseObject(cmsCompetitionScore.getSubmitItem()));
                        scoreDTO.setSubmisson(JSONArray.parseArray(cmsCompetitionScore.getSubmission()));
                        list.add(scoreDTO);
                    }

                }
                long total = pageResult.getTotal();
                JSONObject result = new JSONObject();
                result.put("totalRecord",total);
                result.put("list",list);
                return result;
            }
        }

    }

    /**
     * 评分
     * @param scoreId
     * @param params
     * @return
     */
    @Override
    public Object submitScores(String scoreId,JSONObject params) {
        if (scoreId==null||scoreId.equals("")){
            return "没有此评分号";
        }else {
            Optional<CmsCompetitionScore> optional = competitionScoreRepository.findById(scoreId);
            if (optional.isPresent()){
                CmsCompetitionScore competitionScore = optional.get();
                if (competitionScore==null){
                    return "没有此评分项";
                }else {
                    String competitionId = competitionScore.getCompetitionId();
                    String submitItem = competitionScore.getSubmitItem();
                    //个人赛用户id
                    String teamId = competitionScore.getTeamId();
                    String userId = "";
                    //团队赛取队长id
                    Optional<CmsCompetitionInfo> competitionInfoOptional = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
                    if (competitionInfoOptional.isPresent()){
                        CmsCompetitionInfo competitionInfo = competitionInfoOptional.get();
                        if (null!=competitionId){
                            Integer isNeedTeam = competitionInfo.getIsNeedTeam();
                            if (isNeedTeam==1){
                                TeamsMemberInfo teamsMemberInfo = teamsMemberInfoRepository.findByTeamsInfoId(teamId);
                                if (null!=teamsMemberInfo){
                                    String guid = teamsMemberInfo.getGuid();
                                    teamId=guid;
                                }
                            }
                        }
                    }
                    Optional<CmsCompetitionInfo> op = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
                    CmsCompetitionInfo info = op.get();
                    if (info==null){
                        return "没有此竞赛";
                    }else {

                        String competitionStatus = info.getCompetitionStatus();
                        if (!(competitionStatus.equals("5")|| competitionStatus.equals("8")||competitionStatus.equals("9")))
                        {
                            JSONObject j = new JSONObject();
                            Object scoreDTO = findScore(scoreId);
                            j.put("scoreDTO",scoreDTO);
                            j.put("提示:","当前竞赛不在评分状态");
                            return j;
                        }else {
                            try {
                                float totalScore = 0;
                                JSONObject json=new JSONObject();
                                JSONObject jsonObject = json.parseObject(submitItem);
                                Set<String> keySet = jsonObject.keySet();
                                for (String key :keySet){
                                    String value = params.getJSONObject("submitItem").getString(key);
                                    json.put(key,value);
                                    CmsScoring cmsScoring = new CmsScoring();
                                    if ("5".equals(competitionStatus)){
                                        cmsScoring=  cmsScoringRepository.findByCompetitionInfoIdAndScoringItemAndCompetitionInfoType(competitionId, key,1);
                                    }else if ("8".equals(competitionStatus)){
                                        cmsScoring = cmsScoringRepository.findByCompetitionInfoIdAndScoringItemAndCompetitionInfoType(competitionId, key,2);
                                    }else if ("9".equals(competitionStatus)){
                                        cmsScoring = cmsScoringRepository.findByCompetitionInfoIdAndScoringItemAndCompetitionInfoType(competitionId, key,0);
                                    }
                                    if (null!=cmsScoring){
                                        Float weight = cmsScoring.getWeight();
                                        totalScore=totalScore+Float.parseFloat(value)*weight;
                                    }
                                }
                                BigDecimal   b   =   new BigDecimal(totalScore);
                                double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                                String items = json.toString();
                                competitionScore.setSubmitItem(items);
                                competitionScore.setTotalScore((float)f1);

                                competitionScore.setComments(params.getString("comments"));
                                String status = params.getString("status");
                                if (status.equals("0")){
                                    competitionScore.setStatus(0);
                                }else {
                                    competitionScore.setStatus(1);
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                    String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
                                    Date parse = df.parse(date);
                                    competitionScore.setSubmitTime(parse);
                                }
                                Result result =new Result();
                                List<Result> results = new ArrayList<>();
                                if ("5".equals(competitionStatus)){
                                    results = resultRepository.findByCreatedByIdAndCompetitionInfoIdPre(teamId, competitionId);
                                }else if ("8".equals(competitionStatus)){
                                    results = resultRepository.findByCreatedByIdAndCompetitionInfoIdFinal(teamId, competitionId);
                                }else if ("9".equals(competitionStatus)){
                                    results = resultRepository.findByCreatedByIdAndCompetitionInfoId(teamId, competitionId);
                                }
                                competitionScoreRepository.save(competitionScore);
                                resultRepository.updateScoreB(String.valueOf(f1),results.get(0).getId());
                                return "评分成功";
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            return null;

        }
    }

    /**
     * 查询评分详情
     * @param scoreId
     * @return
     */
    @Override
    public CompetitionScoreDTO findScore(String scoreId){
        CompetitionScoreDTO competitionScoreDTO = new CompetitionScoreDTO();
        try {
            if (null!=scoreId){
                CmsCompetitionScore competitionScore = competitionScoreRepository.getOne(scoreId);
                if (null!=competitionScore){
                    competitionScoreDTO.setScoreId(scoreId);
                    competitionScoreDTO.setCompetitioId(competitionScore.getCompetitionId());
                    competitionScoreDTO.setTeamName(competitionScore.getTeamName());
                    competitionScoreDTO.setCompetitionName(competitionScore.getCompetitionName());
                    competitionScoreDTO.setSubmitItem(JSONObject.parseObject(competitionScore.getSubmitItem()));
                    competitionScoreDTO.setComments(competitionScore.getComments());
                    competitionScoreDTO.setSubmisson(JSONArray.parseArray(competitionScore.getSubmission()));
                    competitionScoreDTO.setStatus(competitionScore.getStatus());
                    competitionScoreDTO.setTotalScore(competitionScore.getTotalScore());
                    competitionScoreDTO.setDescription(competitionScore.getDescription());
                    String competitionId = competitionScore.getCompetitionId();
                    //        List<CmsScoring> cmsScoringList = cmsScoringRepository.findByCompetitionInfoId(competitionId);
                    List<CmsScoring> cmsScoringList = new ArrayList<>();
                    Optional<CmsCompetitionInfo> optional = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
                    CmsCompetitionInfo info = optional.get();
                    String competitionStatus = info.getCompetitionStatus();
                    if ("5".equals(competitionStatus) ){
                        cmsScoringList = cmsScoringRepository.findPreByCompetitionInfoId(competitionId);
                    }else if ("8".equals(competitionStatus)){
                        cmsScoringList = cmsScoringRepository.findFinalByCompetitionInfoId(competitionId);
                    }else if ("9".equals(competitionStatus)){
                        cmsScoringList = cmsScoringRepository.findCommonByCompetitionInfoId(competitionId);
                    }
                    Map<String, Object> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    for (CmsScoring cmsScoring : cmsScoringList) {
                        String scoringItem = cmsScoring.getScoringItem();
                        Float weight = cmsScoring.getWeight();
                        jsonObject.put(scoringItem,weight);
                    }
                    competitionScoreDTO.setScoreWeight(jsonObject);
                    return competitionScoreDTO;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return competitionScoreDTO;
    }

    /**
     * 撤回评分
     * @param scoreId
     * @return
     */
    @Transactional
    @Override
    public String deleteByScoreId(String scoreId) {
        try {
            if(StringUtils.isEmpty(scoreId)){
                return "评分id不能为空";
            }else{
                CmsCompetitionScore score = competitionScoreRepository.findByScoreId(scoreId);
                if (null!=score){
                    String competitionId = score.getCompetitionId();
                    String teamId = score.getTeamId();

                    //团队赛取队长id
                    Optional<CmsCompetitionInfo> competitionInfoOptional = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
                    if (competitionInfoOptional.isPresent()){
                        CmsCompetitionInfo competitionInfo = competitionInfoOptional.get();
                        if (null!=competitionId){
                            Integer isNeedTeam = competitionInfo.getIsNeedTeam();
                            if (isNeedTeam==1){
                                TeamsMemberInfo teamsMemberInfo = teamsMemberInfoRepository.findByTeamsInfoId(teamId);
                                if (null!=teamsMemberInfo){
                                    String guid = teamsMemberInfo.getGuid();
                                    teamId=guid;
                                }
                            }
                        }
                    }


                    int finalFlag = score.getFinalFlag();
                    List<Result> results = new ArrayList<>();
                    if (finalFlag==1){
                        results = resultRepository.findByCreatedByIdAndCompetitionInfoIdFinal(teamId, competitionId);
                    }else if (finalFlag==0){
                        results = resultRepository.findByCreatedByIdAndCompetitionInfoIdPre(teamId,competitionId);
                    }else if (finalFlag==2){
                        results=resultRepository.findByCreatedByIdAndCompetitionInfoId(teamId,competitionId);
                    }
                    double scoreb = results.get(0).getScoreb();
                    String id = results.get(0).getId();
                    resultRepository.deleteScoreB(id);
                    competitionScoreRepository.deleteById(scoreId);
                }
            }
            return "撤回成功";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "评分已被撤回";
    }

    public static Map<String,Object> getStringToMap(String str){
        //根据逗号截取字符串数组
        String[] str1 = str.split(",");
        //创建Map对象
        Map<String,Object> map = new HashMap<>();
        //循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            //根据":"截取字符串数组
            String[] str2 = str1[i].split(":");
            //str2[0]为KEY,str2[1]为值
            map.put(str2[0],str2[1]);
        }
        return map;
    }

    public static String Bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

}
