package com.bonc.turing.cms.manage.service.competition.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.manage.entity.Judge;
import com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo;
import com.bonc.turing.cms.manage.entity.competition.CmsScoring;
import com.bonc.turing.cms.manage.enums.CompetitionStatus;
import com.bonc.turing.cms.manage.enums.CompetitionSystem;
import com.bonc.turing.cms.manage.repository.CmsScoringRepository;
import com.bonc.turing.cms.manage.repository.CompetitionRepository;
import com.bonc.turing.cms.manage.repository.JudgeRepository;
import com.bonc.turing.cms.manage.service.competition.ICompetitionManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CompetitionManageServiceImpl implements ICompetitionManageService {

    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    JudgeRepository judgeRepository;
    @Autowired
    CmsScoringRepository cmsScoringRepository;

    /*
     * 方法说明:   创建竞赛
     * Method:   saveCompetitionInfo
     * @param request
     * @param params
     * @return com.alibaba.fastjson.JSONObject
     * @date 2019/7/18 12:06
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public JSONObject saveCompetitionInfo(HttpServletRequest request, JSONObject params) throws ParseException {
        JSONObject jo = new JSONObject();
        // 创建人id
        String guid = request.getParameter("guid").toString();
        //获取竞赛实体
        CmsCompetitionInfo competitionInfo = JSONObject.parseObject(params.getJSONObject("competitionInfoParams").getJSONObject("competitionInfo").toJSONString(), CmsCompetitionInfo.class);
        //竞赛id
        String id = competitionInfo.getCompetitionId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null == id || id.equals("")) {
            // id为空说明新增
            competitionInfo.setCompetitionId(UUID.randomUUID().toString().replaceAll("-", ""));
            String competitionName = competitionInfo.getCompetitionName();
            //创建时间
            competitionInfo.setCreateTime(sdf.parse(sdf.format(new Date())));
            competitionInfo.setCompetitionStatus(CompetitionStatus.PREPARING.getCode());
            //是否可以加入
            competitionInfo.setIsJoin(1);
            List<CmsCompetitionInfo> all = competitionRepository.findAll();
            for (int i = 0; i < all.size(); i++) {
                if (competitionName.trim().equals(all.get(i).getCompetitionName().trim())) {
                    return null;
                }
            }

        }
        //奖金池为空就赋值为-1
        Long awardMoney = competitionInfo.getAwardMoney();
        if (null==awardMoney){
            competitionInfo.setAwardMoney(-1l);
        }
        //竞赛状态
        //创建人
        competitionInfo.setCreateUser(guid);
        //是否置顶
        competitionInfo.setIsTop(0);
        //置顶时间
        competitionInfo.setIsTopTime(new Date());
        // 显示/隐藏
        competitionInfo.setShowHide(0);
        //排序 --> 当前表中钟总数据+1
        competitionInfo.setSorting(competitionRepository.getSorting());

        // 标签
        competitionInfo = getLabel(params, competitionInfo);
        // 参赛日期
        competitionInfo = getTime(params, competitionInfo);
        // 奖励
        competitionInfo = getPrize(params, competitionInfo);
        // 评委
        getJudge(params, competitionInfo);
        // 评分项
        getScoring(params, competitionInfo);
        competitionInfo = competitionRepository.saveAndFlush(competitionInfo);
        JSONObject competitionInfoParams = new JSONObject();
        competitionInfoParams.put("competitionInfo", competitionInfo);
        competitionInfoParams.put("label", params.getJSONObject("competitionInfoParams").getJSONObject("label"));
        competitionInfoParams.put("competitionTime", params.getJSONObject("competitionInfoParams").getJSONObject("competitionTime"));
        competitionInfoParams.put("competitionPrizePreliminary", params.getJSONObject("competitionInfoParams").getJSONObject("competitionPrizePreliminary"));
        competitionInfoParams.put("competitionPrizeFinal", params.getJSONObject("competitionInfoParams").getJSONObject("competitionPrizeFinal"));
        competitionInfoParams.put("competitionPrizeComment", params.getJSONObject("competitionInfoParams").getJSONObject("competitionPrizeComment"));
        competitionInfoParams.put("competitionJudges", params.getJSONObject("competitionInfoParams").getJSONArray("competitionJudges"));
        competitionInfoParams.put("competitionJudgesFinal", params.getJSONObject("competitionInfoParams").getJSONArray("competitionJudgesFinal"));
        competitionInfoParams.put("preliminaryScoring", params.getJSONObject("competitionInfoParams").getJSONArray("preliminaryScoring"));
        competitionInfoParams.put("finalScoring", params.getJSONObject("competitionInfoParams").getJSONArray("finalScoring"));
        competitionInfoParams.put("commonScoring", params.getJSONObject("competitionInfoParams").getJSONArray("commonScoring"));
        jo.put("competitionInfoParams", competitionInfoParams);
        return jo;
    }

    /*
     * 方法说明:   根据竞赛ID查询竞赛详细信息
     * Method:   getCompetitionInfo
     * @param competitionId
     * @return com.alibaba.fastjson.JSONObject
     * @date 2019/7/18 12:09
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    public JSONObject getCompetitionInfo(String competitionId) throws ParseException {

        JSONObject jo = new JSONObject();
        //通过竞赛id获取竞赛主体信息
        /* CmsCompetitionInfo cmsCompetitionInfo = competitionRepository.findCompetitionById(competitionId);*/
        Optional<CmsCompetitionInfo> op = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
        CmsCompetitionInfo cmsCompetitionInfo = op.get();
        JSONObject competitionInfoParams = new JSONObject();
        competitionInfoParams.put("competitionInfo", cmsCompetitionInfo);
        //处理标签信息
        competitionInfoParams = setLabel(competitionInfoParams, cmsCompetitionInfo);
        //处理竞赛时间
        competitionInfoParams = setTime(competitionInfoParams, cmsCompetitionInfo);
        //处理奖项
        competitionInfoParams = setPrize(competitionInfoParams, cmsCompetitionInfo);
        //评委列表
        competitionInfoParams = setJudge(competitionInfoParams, cmsCompetitionInfo);
        //评分项
        competitionInfoParams = setScoring(competitionInfoParams, cmsCompetitionInfo);
       /* // 数据集
        competitionInfoParams = setData(competitionInfoParams, cmsCompetitionInfo);*/
        jo.put("competitionInfoParams", competitionInfoParams);
        jo.put("nowTime",new Date().getTime());
        return jo;
    }

    /*
     * 方法说明:   处理标签
     * Method:   getLabel
     * @param params
     * @param competitionInfo
     * @return com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo
     * @date 2019/7/12 19:42
     * @author bxt
     * @version v 1.0.0
     */
    private CmsCompetitionInfo getLabel(JSONObject params, CmsCompetitionInfo competitionInfo) {
        JSONObject competitionInfoParams = params.getJSONObject("competitionInfoParams").getJSONObject("label");
        JSONArray label = competitionInfoParams.getJSONArray("labelList");
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < label.size(); i++) {
            JSONObject jsonObject = JSONObject.parseObject(label.get(i).toString());
            String labelId = jsonObject.getString("id");
            String content = jsonObject.getString("content");
            list.add(labelId + "-" + content);
        }
        String competitionInfoLabel = String.join("_", list);
        String industryId = competitionInfoParams.getString("industryId");
        String industry = competitionInfoParams.getString("industry");
        competitionInfo.setLabel(competitionInfoLabel);
        competitionInfo.setIndustryId(industryId);
        competitionInfo.setIndustry(industry);
        return competitionInfo;
    }

    /*
     * 方法说明:   处理标签
     * Method:   getLabel
     * @param params
     * @param competitionInfo
     * @return com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo
     * @date 2019/7/12 19:42
     * @author bxt
     * @version v 1.0.0
     */
    private JSONObject setLabel(JSONObject competitionInfoParams, CmsCompetitionInfo competitionInfo) {
        JSONObject label = new JSONObject();
        label.put("industryId", competitionInfo.getIndustryId());
        label.put("industry", competitionInfo.getIndustry());
        String competitionInfoLabel = competitionInfo.getLabel();
        if (!"".equals(competitionInfoLabel) && null != competitionInfoLabel) {
            List<String> list = Arrays.asList(competitionInfoLabel.split("_"));
            JSONArray jrr = new JSONArray();
            String[] strArr;
            for (String str : list) {
                strArr = str.split("-");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", strArr[0]);
                jsonObject.put("content", strArr[1]);
                jrr.add(jsonObject);
            }
            label.put("labelList", jrr);
            competitionInfoParams.put("label", label);
        }
        return competitionInfoParams;
    }

    /*
     * 方法说明:   竞赛时间
     * Method:   getLabel
     * @param params
     * @param competitionInfo
     * @return com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo
     * @date 2019/7/12 20:17
     * @author bxt
     * @version v 1.0.0
     */
    private CmsCompetitionInfo getTime(JSONObject params, CmsCompetitionInfo competitionInfo) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject competitionTime = params.getJSONObject("competitionInfoParams").getJSONObject("competitionTime");
        Date date = new Date();
        if (competitionInfo.getIsNeedTeam().equals(1)) {
            //团队赛 组队开始时间/组队结束时间
            //组队开始时间
            String teamStartTime = competitionTime.getString("teamStartTime");
            competitionInfo.setTeamStartTime(sdf.parse(teamStartTime));
            //组队结束时间
            String teamEndTime = competitionTime.getString("teamEndTime");
            competitionInfo.setTeamEndTime(sdf.parse(teamEndTime));
        }
        if (CompetitionSystem.BEFORE_FINAL_COMPETITION.getCode().equals(competitionInfo.getCompetitionSystem())) {
            //初赛决赛
            //初赛开始提交时间
            String preliminaryRleaseStartDate = competitionTime.getString("preliminaryRleaseStartDate");
            competitionInfo.setPreliminaryRleaseStartDate(sdf.parse(preliminaryRleaseStartDate));
            //初赛报名截止
            String preliminaryRleaseEndDate = competitionTime.getString("preliminaryRleaseEndDate");
            competitionInfo.setPreliminaryRleaseEndDate(sdf.parse(preliminaryRleaseEndDate));
            //开始时间
            String preliminaryBeginDate = competitionTime.getString("preliminaryBeginDate");
            competitionInfo.setPreliminaryBeginDate(sdf.parse(preliminaryBeginDate));
            //初赛结束日期
            String preliminaryEndDate = competitionTime.getString("preliminaryEndDate");
            competitionInfo.setPreliminaryEndDate(sdf.parse(preliminaryEndDate));
            //初赛发榜日期
            String preliminaryPublishDate = competitionTime.getString("preliminaryPublishDate");
            competitionInfo.setPreliminaryPublishDate(sdf.parse(preliminaryPublishDate));
            //决赛开始日期
            String finalBeginDate = competitionTime.getString("finalBeginDate");
            competitionInfo.setFinalBeginDate(sdf.parse(finalBeginDate));
            //决赛结束日期
            String finalEndDate = competitionTime.getString("finalEndDate");
            competitionInfo.setFinalEndDate(sdf.parse(finalEndDate));
            //决赛发榜日期
            String finalPublishDate = competitionTime.getString("finalPublishDate");
            competitionInfo.setFinalPublishDate(sdf.parse(finalPublishDate));

        } else {
            //普通赛发布时间
            String commonRleaseStartDate = competitionTime.getString("commonRleaseStartDate");
            competitionInfo.setCommonRleaseStartDate(sdf.parse(commonRleaseStartDate));
            //普通赛停止报名时间
            String commonRleaseEndDate = competitionTime.getString("commonRleaseEndDate");
            competitionInfo.setCommonRleaseEndDate(sdf.parse(commonRleaseEndDate));
            //普通赛开始时间
            String commonBeginDate = competitionTime.getString("commonBeginDate");
            competitionInfo.setCommonBeginDate(sdf.parse(commonBeginDate));
            //普通赛开始时间
            String commonEndDate = competitionTime.getString("commonEndDate");
            competitionInfo.setCommonEndDate(sdf.parse(commonEndDate));
            //普通赛发榜时间
            String commonPublishDate = competitionTime.getString("commonPublishDate");
            competitionInfo.setCommonPublishDate(sdf.parse(commonPublishDate));
            if (competitionInfo.getCompetitionGrade().equals(1)) {
                //新手赛
                competitionInfo.setNoviceBeginDate(competitionInfo.getCommonBeginDate());
                competitionInfo.setNoviceIssue(0);
                //计算当前周期截至日期
                competitionInfo.setNoviceEndDate(competitionInfo.getCommonEndDate());
                //发榜时间
                competitionInfo.setNovicePublishDate(competitionInfo.getCommonPublishDate());
            }
        }
        return competitionInfo;
    }

    /*
     * 方法说明:   竞赛时间
     * Method:   getLabel
     * @param params
     * @param competitionInfo
     * @return com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo
     * @date 2019/7/12 20:17
     * @author bxt
     * @version v 1.0.0
     */
    private JSONObject setTime(JSONObject competitionInfoParams, CmsCompetitionInfo competitionInfo) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject competitionTime = new JSONObject();
        if (competitionInfo.getIsNeedTeam().equals(1)) {
            //团队赛 组队开始时间/组队结束时间
            //组队开始时间
            competitionTime.put("teamStartTime", competitionInfo.getTeamStartTime().getTime());
            //组队结束时间
            competitionTime.put("teamEndTime", competitionInfo.getTeamEndTime().getTime());
        }
        if (CompetitionSystem.BEFORE_FINAL_COMPETITION.getCode().equals(competitionInfo.getCompetitionSystem())) {
            //初赛决赛
            //开始时间
            competitionTime.put("preliminaryBeginDate", competitionInfo.getPreliminaryBeginDate().getTime());
            //初赛开始提交时间
            competitionTime.put("preliminaryRleaseStartDate", competitionInfo.getPreliminaryRleaseStartDate().getTime());
            //初赛报名截止
            competitionTime.put("preliminaryRleaseEndDate", competitionInfo.getPreliminaryRleaseEndDate().getTime());
            //初赛结束日期
            competitionTime.put("preliminaryEndDate", competitionInfo.getPreliminaryEndDate().getTime());
            //初赛发榜日期
            competitionTime.put("preliminaryPublishDate", competitionInfo.getPreliminaryPublishDate().getTime());
            //决赛开始日期
            competitionTime.put("finalBeginDate", competitionInfo.getFinalBeginDate().getTime());
            //决赛结束日期
            competitionTime.put("finalEndDate", competitionInfo.getFinalEndDate().getTime());
            //决赛发榜日期
            competitionTime.put("finalPublishDate", competitionInfo.getFinalPublishDate().getTime());

        } else {
            //普通赛发布时间
            competitionTime.put("commonRleaseStartDate", competitionInfo.getCommonRleaseStartDate().getTime());
            //普通赛停止报名时间
            competitionTime.put("commonRleaseEndDate", competitionInfo.getCommonRleaseEndDate().getTime());
            //普通赛开始时间
            competitionTime.put("commonBeginDate", competitionInfo.getCommonBeginDate().getTime());
            //普通赛开始时间
            competitionTime.put("commonEndDate", competitionInfo.getCommonEndDate().getTime());
            //普通赛发布时间
            competitionTime.put("commonPublishDate", competitionInfo.getCommonPublishDate().getTime());
        }
        competitionInfoParams.put("competitionTime", competitionTime);
        return competitionInfoParams;
    }

    /*
     * 方法说明:   奖项
     * Method:   getPrize
     * @param params
     * @param competitionInfo
     * @return com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo
     * @date 2019/7/12 20:25
     * @author bxt
     * @version v 1.0.0
     */
    private CmsCompetitionInfo getPrize(JSONObject params, CmsCompetitionInfo competitionInfo) {
        if ("1".equals(competitionInfo.getCompetitionSystem())) {
            //  初赛
            JSONObject competitionPrizePreliminary = params.getJSONObject("competitionInfoParams").getJSONObject("competitionPrizePreliminary");
            String isScaleNumberPreliminary = competitionPrizePreliminary.getString("isScaleNumberPreliminary");
            String preliminaryGoldMedalPnumber = competitionPrizePreliminary.getString("preliminaryGoldMedalPnumber");
            String preliminaryGoldMedalAmountRate = competitionPrizePreliminary.getString("preliminaryGoldMedalAmountRate");
            String preliminarySilverMedalPnumber = competitionPrizePreliminary.getString("preliminarySilverMedalPnumber");
            String preliminarySilverMedalAmountRate = competitionPrizePreliminary.getString("preliminarySilverMedalAmountRate");
            String preliminaryBronzeMedalPnumber = competitionPrizePreliminary.getString("preliminaryBronzeMedalPnumber");
            String preliminaryBronzeMedalAmountRate = competitionPrizePreliminary.getString("preliminaryBronzeMedalAmountRate");
            competitionInfo.setIsScaleNumberPreliminary(isScaleNumberPreliminary);
            competitionInfo.setPreliminaryGoldMedalPnumber(preliminaryGoldMedalPnumber);
            competitionInfo.setPreliminaryGoldMedalAmountRate(preliminaryGoldMedalAmountRate);
            competitionInfo.setPreliminarySilverMedalPnumber(preliminarySilverMedalPnumber);
            competitionInfo.setPreliminarySilverMedalAmountRate(preliminarySilverMedalAmountRate);
            competitionInfo.setPreliminaryBronzeMedalPnumber(preliminaryBronzeMedalPnumber);
            competitionInfo.setPreliminaryBronzeMedalAmountRate(preliminaryBronzeMedalAmountRate);
            //  决赛
            JSONObject competitionPrizeFinal = params.getJSONObject("competitionInfoParams").getJSONObject("competitionPrizeFinal");
            String isScaleNumberFinal = competitionPrizeFinal.getString("isScaleNumberFinal");
            String finalGoldMedalPnumber = competitionPrizeFinal.getString("finalGoldMedalPnumber");
            String finalGoldMedalAmountRate = competitionPrizeFinal.getString("finalGoldMedalAmountRate");
            String finalSilverMedalPnumber = competitionPrizeFinal.getString("finalSilverMedalPnumber");
            String finalSilverMedalAmountRate = competitionPrizeFinal.getString("finalSilverMedalAmountRate");
            String finalBronzeMedalPnumber = competitionPrizeFinal.getString("finalBronzeMedalPnumber");
            String finalBronzeMedalAmountRate = competitionPrizeFinal.getString("finalBronzeMedalAmountRate");
            competitionInfo.setIsScaleNumberFinal(isScaleNumberFinal);
            competitionInfo.setFinalGoldMedalPnumber(finalGoldMedalPnumber);
            competitionInfo.setFinalGoldMedalAmountRate(finalGoldMedalAmountRate);
            competitionInfo.setFinalSilverMedalPnumber(finalSilverMedalPnumber);
            competitionInfo.setFinalSilverMedalAmountRate(finalSilverMedalAmountRate);
            competitionInfo.setFinalBronzeMedalPnumber(finalBronzeMedalPnumber);
            competitionInfo.setFinalBronzeMedalAmountRate(finalBronzeMedalAmountRate);
        } else {
            JSONObject competitionPrizeComment = params.getJSONObject("competitionInfoParams").getJSONObject("competitionPrizeComment");
            String isScaleNumberComment = competitionPrizeComment.getString("isScaleNumberComment");
            String commentGoldMedalPnumber = competitionPrizeComment.getString("commonGoldMedalPnumber");
            String commentGoldMedalAmountRate = competitionPrizeComment.getString("commonGoldMedalAmountRate");
            String commentSilverMedalPnumber = competitionPrizeComment.getString("commonSilverMedalPnumber");
            String commentSilverMedalAmountRate = competitionPrizeComment.getString("commonSilverMedalAmountRate");
            String commentBronzeMedalPnumber = competitionPrizeComment.getString("commonBronzeMedalPnumber");
            String commentBronzeMedalAmountRate = competitionPrizeComment.getString("commonBronzeMedalAmountRate");
            competitionInfo.setIsScaleNumber(isScaleNumberComment);
            competitionInfo.setGoldMedalPnumber(commentGoldMedalPnumber);
            competitionInfo.setGoldMedalAmountRate(commentGoldMedalAmountRate);
            competitionInfo.setSilverMedalPnumber(commentSilverMedalPnumber);
            competitionInfo.setSilverMedalAmountRate(commentSilverMedalAmountRate);
            competitionInfo.setBronzeMedalPnumber(commentBronzeMedalPnumber);
            competitionInfo.setBronzeMedalAmountRate(commentBronzeMedalAmountRate);
        }

        return competitionInfo;
    }

    /*
     * 方法说明:   奖项
     * Method:   getPrize
     * @param params
     * @param competitionInfo
     * @return com.bonc.turing.cms.manage.entity.competition.CmsCompetitionInfo
     * @date 2019/7/12 20:25
     * @author bxt
     * @version v 1.0.0
     */
    private JSONObject setPrize(JSONObject competitionInfoParams, CmsCompetitionInfo competitionInfo) {
        if ("1".equals(competitionInfo.getCompetitionSystem())) {
            JSONObject competitionPrizePreliminary = new JSONObject();
            competitionPrizePreliminary.put("isScaleNumberPreliminary", competitionInfo.getIsScaleNumberPreliminary());
            competitionPrizePreliminary.put("preliminaryGoldMedalPnumber", competitionInfo.getPreliminaryGoldMedalPnumber());
            competitionPrizePreliminary.put("preliminaryGoldMedalAmountRate", competitionInfo.getPreliminaryGoldMedalAmountRate());
            competitionPrizePreliminary.put("preliminarySilverMedalPnumber", competitionInfo.getPreliminarySilverMedalPnumber());
            competitionPrizePreliminary.put("preliminarySilverMedalAmountRate", competitionInfo.getPreliminarySilverMedalAmountRate());
            competitionPrizePreliminary.put("preliminaryBronzeMedalPnumber", competitionInfo.getPreliminaryBronzeMedalPnumber());
            competitionPrizePreliminary.put("preliminaryBronzeMedalAmountRate", competitionInfo.getPreliminaryBronzeMedalAmountRate());

            JSONObject competitionPrizeFinal = new JSONObject();
            competitionPrizeFinal.put("isScaleNumberFinal", competitionInfo.getIsScaleNumberFinal());
            competitionPrizeFinal.put("finalGoldMedalPnumber", competitionInfo.getFinalGoldMedalPnumber());
            competitionPrizeFinal.put("finalGoldMedalAmountRate", competitionInfo.getFinalGoldMedalAmountRate());
            competitionPrizeFinal.put("finalSilverMedalPnumber", competitionInfo.getFinalSilverMedalPnumber());
            competitionPrizeFinal.put("finalSilverMedalAmountRate", competitionInfo.getFinalSilverMedalAmountRate());
            competitionPrizeFinal.put("finalBronzeMedalPnumber", competitionInfo.getFinalBronzeMedalPnumber());
            competitionPrizeFinal.put("finalBronzeMedalAmountRate", competitionInfo.getFinalBronzeMedalAmountRate());

            competitionInfoParams.put("competitionPrizePreliminary", competitionPrizePreliminary);
            competitionInfoParams.put("competitionPrizeFinal", competitionPrizeFinal);
            /* competitionInfoParams.put("competitionPrizeComment", new JSONObject());*/

            JSONObject competitionPrizeComment = new JSONObject();
            competitionPrizeComment.put("isScaleNumberComment", "");
            competitionPrizeComment.put("commonGoldMedalPnumber", "");
            competitionPrizeComment.put("commonGoldMedalAmountRate", "");
            competitionPrizeComment.put("commonSilverMedalPnumber", "");
            competitionPrizeComment.put("commonSilverMedalAmountRate", "");
            competitionPrizeComment.put("commonBronzeMedalPnumber", "");
            competitionPrizeComment.put("commonBronzeMedalAmountRate", "");

        } else {
            JSONObject competitionPrizeComment = new JSONObject();
            competitionPrizeComment.put("isScaleNumberComment", competitionInfo.getIsScaleNumber());
            competitionPrizeComment.put("commonGoldMedalPnumber", competitionInfo.getGoldMedalPnumber());
            competitionPrizeComment.put("commonGoldMedalAmountRate", competitionInfo.getGoldMedalAmountRate());
            competitionPrizeComment.put("commonSilverMedalPnumber", competitionInfo.getSilverMedalPnumber());
            competitionPrizeComment.put("commonSilverMedalAmountRate", competitionInfo.getSilverMedalAmountRate());
            competitionPrizeComment.put("commonBronzeMedalPnumber", competitionInfo.getBronzeMedalPnumber());
            competitionPrizeComment.put("commonBronzeMedalAmountRate", competitionInfo.getBronzeMedalAmountRate());
           /* competitionInfoParams.put("competitionPrizePreliminary", new JSONObject());
            competitionInfoParams.put("competitionPrizeFinal", new JSONObject());*/
            competitionInfoParams.put("competitionPrizeComment", competitionPrizeComment);

            JSONObject competitionPrizePreliminary = new JSONObject();
            competitionPrizePreliminary.put("isScaleNumberPreliminary", "");
            competitionPrizePreliminary.put("preliminaryGoldMedalPnumber", "");
            competitionPrizePreliminary.put("preliminaryGoldMedalAmountRate", "");
            competitionPrizePreliminary.put("preliminarySilverMedalPnumber", "");
            competitionPrizePreliminary.put("preliminarySilverMedalAmountRate", "");
            competitionPrizePreliminary.put("preliminaryBronzeMedalPnumber", "");
            competitionPrizePreliminary.put("preliminaryBronzeMedalAmountRate", "");

            JSONObject competitionPrizeFinal = new JSONObject();
            competitionPrizeFinal.put("isScaleNumberFinal", "");
            competitionPrizeFinal.put("finalGoldMedalPnumber", "");
            competitionPrizeFinal.put("finalGoldMedalAmountRate", "");
            competitionPrizeFinal.put("finalSilverMedalPnumber", "");
            competitionPrizeFinal.put("finalSilverMedalAmountRate", "");
            competitionPrizeFinal.put("finalBronzeMedalPnumber", "");
            competitionPrizeFinal.put("finalBronzeMedalAmountRate", "");
            competitionInfoParams.put("competitionPrizePreliminary", competitionPrizePreliminary);
            competitionInfoParams.put("competitionPrizeFinal", competitionPrizeFinal);
        }
        return competitionInfoParams;
    }

    /*
     * 方法说明:   保存评委
     * Method:   getJudge
     * @param params
     * @param competitionInfo
     * @return void
     * @date 2019/7/12 20:26
     * @author bxt
     * @version v 1.0.0
     */
    public void getJudge(JSONObject params, CmsCompetitionInfo competitionInfo) {
        if ("0".equals(competitionInfo.getIsNeedJudges())) {
            //初赛或普通赛评委
            JSONArray judgeArray = params.getJSONObject("competitionInfoParams").getJSONArray("competitionJudges");
                // 删除评委
                judgeRepository.deleteBycompetitionInfoIdAndIsFinal(competitionInfo.getCompetitionId(), 0);
                List<Judge> judgeList = new ArrayList<>();
                for (int i = 0; i < judgeArray.size(); i++) {
                    Judge judge = new Judge();
                    judge.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                    judge.setCompetitionId(competitionInfo.getCompetitionId());
                    judge.setIsFinal(0);
                    judge.setJudgeId(judgeArray.getJSONObject(i).getString("userId"));
                    judge.setJudgeName(judgeArray.getJSONObject(i).getString("userName"));
                    judgeRepository.save(judge);
                    judgeList.add(judge);
                }
            //决赛评委
            JSONArray judgeArrayFinal = params.getJSONObject("competitionInfoParams").getJSONArray("competitionJudgesFinal");
                // 删除评委
                judgeRepository.deleteBycompetitionInfoIdAndIsFinal(competitionInfo.getCompetitionId(), 1);
                List<Judge> judgeList1 = new ArrayList<>();
                for (int i = 0; i < judgeArrayFinal.size(); i++) {
                    Judge judge = new Judge();
                    judge.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                    judge.setCompetitionId(competitionInfo.getCompetitionId());
                    judge.setIsFinal(1);
                    judge.setJudgeId(judgeArrayFinal.getJSONObject(i).getString("userId"));
                    judge.setJudgeName(judgeArrayFinal.getJSONObject(i).getString("userName"));
                    judgeRepository.save(judge);
                    judgeList1.add(judge);
                }
        }
    }

    /*
     * 方法说明:   评委
     * Method:   setJudge
     * @param competitionInfoParams
     * @param competitionInfo
     * @return com.alibaba.fastjson.JSONObject
     * @date 2019/7/15 10:53
     * @author bxt
     * @version v 1.0.0
     */
    public JSONObject setJudge(JSONObject competitionInfoParams, CmsCompetitionInfo competitionInfo) {
        List<Judge> list = judgeRepository.getJudgeByCompetitionIdAndIsFinal(competitionInfo.getCompetitionId(), 0);
        competitionInfoParams.put("competitionJudges", list);
        List<Judge> listFinal = judgeRepository.getJudgeByCompetitionIdAndIsFinal(competitionInfo.getCompetitionId(), 1);
        competitionInfoParams.put("competitionJudgesFinal", listFinal);
        return competitionInfoParams;
    }

    /*
     * 方法说明:   评分项
     * Method:   getScoring
     * @param params
     * @param competitionInfo
     * @return void
     * @date 2019/7/13 12:04
     * @author bxt
     * @version v 1.0.0
     */
    public void getScoring(JSONObject params, CmsCompetitionInfo competitionInfo) throws ParseException {
        //删除原来的评分项
        cmsScoringRepository.deleteByCompetitionId(competitionInfo.getCompetitionId());
        if (CompetitionSystem.BEFORE_FINAL_COMPETITION.getCode().equals(competitionInfo.getCompetitionSystem())) {
            // 初决赛
            JSONArray preliminaryScoring = params.getJSONObject("competitionInfoParams").getJSONArray("preliminaryScoring");
            List<CmsScoring> preliminaryList = JSONObject.parseArray(preliminaryScoring.toJSONString(), CmsScoring.class);
            save(preliminaryList, competitionInfo, 1);
            // 决赛
            JSONArray finalScoring = params.getJSONObject("competitionInfoParams").getJSONArray("finalScoring");
            List<CmsScoring> finalList = JSONObject.parseArray(finalScoring.toJSONString(), CmsScoring.class);
            save(finalList, competitionInfo, 2);
        }
        if (CompetitionSystem.GENERAL_COMPETITION.getCode().equals(competitionInfo.getCompetitionSystem())) {
            JSONArray commonScoring = params.getJSONObject("competitionInfoParams").getJSONArray("commonScoring");
            List<CmsScoring> commonList = JSONObject.parseArray(commonScoring.toJSONString(), CmsScoring.class);
            save(commonList, competitionInfo, 0);
        }

    }

    /*
     * 方法说明:评分项
     * Method:   setScoring
     * @param competitionInfoParams
     * @param competitionInfo
     * @return com.alibaba.fastjson.JSONObject
     * @date 2019/7/15 11:04
     * @author bxt
     * @version v 1.0.0
     */
    public JSONObject setScoring(JSONObject competitionInfoParams, CmsCompetitionInfo competitionInfo) throws ParseException {

        if (CompetitionSystem.BEFORE_FINAL_COMPETITION.getCode().equals(competitionInfo.getCompetitionSystem())) {
            // 初决赛
            List<CmsScoring> preliminaryList = cmsScoringRepository.getCmsScoringListByCompetitionIdAndCompetitionInfoType(competitionInfo.getCompetitionId(), 1);

            competitionInfoParams.put("preliminaryScoring", preliminaryList);
            // 决赛
            List<CmsScoring> finalList = cmsScoringRepository.getCmsScoringListByCompetitionIdAndCompetitionInfoType(competitionInfo.getCompetitionId(), 2);
            competitionInfoParams.put("finalScoring", finalList);
            competitionInfoParams.put("commonScoring", new ArrayList<CmsScoring>());
        }
        if (CompetitionSystem.GENERAL_COMPETITION.getCode().equals(competitionInfo.getCompetitionSystem())) {
            //普通赛
            List<CmsScoring> commonList = cmsScoringRepository.getCmsScoringListByCompetitionIdAndCompetitionInfoType(competitionInfo.getCompetitionId(), 0);
            competitionInfoParams.put("commonScoring", commonList);
            competitionInfoParams.put("preliminaryScoring", new ArrayList<CmsScoring>());
            competitionInfoParams.put("finalScoring", new ArrayList<CmsScoring>());
        }

        return competitionInfoParams;
    }

    /*
     * 方法说明:   保存评分项
     * Method:   save
     * @param list
     * @param competitionInfo
     * @param competitionInfoType
     * @return void
     * @date 2019/7/13 12:04
     * @author bxt
     * @version v 1.0.0
     */
    public void save(List<CmsScoring> list, CmsCompetitionInfo competitionInfo, int competitionInfoType) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size() > 0) {
            for (CmsScoring cmsScore : list) {
                cmsScore.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                cmsScore.setCompetitionInfoId(competitionInfo.getCompetitionId());
                cmsScore.setCaretaDate(sdf.parse(sdf.format(new Date())));
                cmsScore.setCompetitionInfoType(competitionInfoType);
                cmsScoringRepository.save(cmsScore);
            }
        }
    }

    /*
     * 方法说明:   创建竞赛-查询当前竞赛已选评委
     * Method:   queryJudges
     * @param competitionId
     * @return java.util.List<com.bonc.turing.cms.manage.entity.Judge>
     * @date 2019/7/18 12:06
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    public List<Judge> queryJudges(String competitionId) {
        List<Judge> judgeByCompetitionId = null;
        try {
            judgeByCompetitionId = judgeRepository.getJudgeByCompetitionId(competitionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return judgeByCompetitionId;
    }

    /*
     * 方法说明:   创建竞赛-当前竞赛已选评委删除接口
     * Method:   deleteJudges
     * @param judgeId
     * @return boolean
     * @date 2019/7/18 12:06
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    public boolean deleteJudges(String judgeId) {
        boolean b = false;
        try {
            Integer integer = judgeRepository.deleteJudgeByJudgeId(judgeId);
            if (integer == 1) {
                b = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    /*
     * 方法说明:   根据竞赛ID显示/隐藏竞赛信息
     * Method:   deleteCompetition
     * @param competitionId
     * @param showHide
     * @return void
     * @date 2019/7/18 12:07
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void deleteCompetition(String competitionId, int showHide) {
        competitionRepository.updateCompetition(competitionId, showHide);
    }

    /*
     * 方法说明:
     * Method:   讨论列表
     * @param params
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @date 2019/7/18 12:09
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    public List<CmsCompetitionInfo> getCompetitionList(JSONObject params) {

        int page = (int) params.get("page");
        int size = (int) params.get("size");
        String competitionOrEnterprise = (String) params.get("competitionOrEnterprise");
        String competitionStatus = (String) params.get("competitionStatus");
        Sort sort = new Sort(Sort.Direction.DESC, "is_top", "is_top_time", "create_time");
        Pageable pageable = new PageRequest(page, size, sort);
        List<CmsCompetitionInfo> list = competitionRepository.getCompetitionList(pageable, competitionOrEnterprise, competitionStatus);
        return list;

    }
    /*
     * 方法说明:   总条数
     * Method:   getCompetitionListCount
     * @param params
     * @return int
     * @date 2019/7/18 12:09
     * @author bxt
     * @version v 1.0.0
     */

    @Override
    public int getCompetitionListCount(JSONObject params) {
        String competitionOrEnterprise = (String) params.get("competitionOrEnterprise");
        String competitionStatus = (String) params.get("competitionStatus");
        return competitionRepository.getCompetitionListCount(competitionOrEnterprise, competitionStatus);

    }

    public Specification<CmsCompetitionInfo> CompetitionInfoSearch(String competitionOrEnterprise, String competitionStatus) {
        Specification<CmsCompetitionInfo> querySpecifi = new Specification<CmsCompetitionInfo>() {
            //内部类
            @Override
            public Predicate toPredicate(Root<CmsCompetitionInfo> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (competitionOrEnterprise != null) {
                    predicates.add(cb.like(root.<String>get("competitionName"), "%" + competitionOrEnterprise + "%"));
                }
                if (competitionStatus.trim().equals("3")) {
                    Path<Object> competitionStatus = root.get("competitionStatus");
                    Predicate p1 = cb.and(cb.equal(competitionStatus, "3"));
                    p1 = cb.or(p1, cb.equal(competitionStatus, "5"));
                    p1 = cb.or(p1, cb.equal(competitionStatus, "7"));
                    p1 = cb.or(p1, cb.equal(competitionStatus, "8"));
                    p1 = cb.or(p1, cb.equal(competitionStatus, "9"));
                    predicates.add(cb.and(p1));
                }
                if (!competitionStatus.trim().equals("全部") && !competitionStatus.trim().equals("3")) {
                    predicates.add(cb.equal(root.<String>get("competitionStatus"), competitionStatus));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return querySpecifi;
    }

    public Specification<CmsCompetitionInfo> CompetitionInfoSearch2(String competitionOrEnterprise, String competitionStatus) {
        Specification<CmsCompetitionInfo> querySpecifi = new Specification<CmsCompetitionInfo>() {
            //内部类
            @Override
            public Predicate toPredicate(Root<CmsCompetitionInfo> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (competitionOrEnterprise != null) {
                    predicates.add(cb.like(root.<String>get("enterpriseName"), "%" + competitionOrEnterprise + "%"));
                }
                if (competitionStatus.trim().equals("3")) {
                    Path<Object> competitionStatus = root.get("competitionStatus");
                    Predicate p1 = cb.and(cb.equal(competitionStatus, "3"));

                    p1 = cb.or(p1, cb.equal(competitionStatus, "5"));
                    p1 = cb.or(p1, cb.equal(competitionStatus, "7"));
                    p1 = cb.or(p1, cb.equal(competitionStatus, "8"));
                    p1 = cb.or(p1, cb.equal(competitionStatus, "9"));
                    predicates.add(cb.and(p1));
                }
                if (!competitionStatus.trim().equals("全部") && !competitionStatus.trim().equals("3")) {
                    predicates.add(cb.equal(root.<String>get("competitionStatus"), competitionStatus));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return querySpecifi;
    }

    /*
     * 方法说明:   创建竞赛-当前竞赛已选评委删除接口
     * Method:   updateSorting
     * @param
     * @return void
     * @date 2019/7/18 12:07
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    public void updateSorting(String competitionId) {
        List<CmsCompetitionInfo> list = competitionRepository.findAll();
        int i = 1;
        if (list.size() > 0) {
            for (CmsCompetitionInfo cms : list) {
                cms.setSorting(i);
                i++;
                competitionRepository.saveAndFlush(cms);
            }
        }
    }

    /*
     * 方法说明:   置顶
     * Method:   updateTopping
     * @param request
     * @return void
     * @date 2019/7/18 12:07
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateTopping(String competitionId) {
        Optional<CmsCompetitionInfo> op = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
        CmsCompetitionInfo cmsCompetitionInfo = op.get();
        cmsCompetitionInfo.setIsTop(1);
        cmsCompetitionInfo.setIsTopTime(new Date());
        cmsCompetitionInfo.setLastMdifyTime(new Date());
        competitionRepository.saveAndFlush(cmsCompetitionInfo);
    }

    /*
     * 方法说明:   下沉
     * Method:   updateSinkg
     * @param request
     * @return void
     * @date 2019/7/18 12:07
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateSinkg(String competitionId) {
        Optional<CmsCompetitionInfo> op = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
        CmsCompetitionInfo cmsCompetitionInfo = op.get();
        cmsCompetitionInfo.setIsTop(0);
        cmsCompetitionInfo.setIsTopTime(new Date());
        cmsCompetitionInfo.setLastMdifyTime(new Date());
        competitionRepository.saveAndFlush(cmsCompetitionInfo);
    }

    /*
     * 方法说明:   删除
     * Method:   deleteCompetition
     * @param request
     * @return void
     * @date 2019/7/18 12:07
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void deleteCompetition(String competitionId) {
        // 删除 Data_source_def_info
        competitionRepository.deleteDataSourceDefInfo(competitionId);
        // 删除 Judge
        competitionRepository.deleteJudge(competitionId);
        // 删除 参赛表
        competitionRepository.deleteUserParticipateCompetition(competitionId);
        // 删除 d_s_result_copy
        competitionRepository.deletResultCopy(competitionId);

        // 删除 d_s_result
        competitionRepository.deletResult(competitionId);

        competitionRepository.deleteCompetition(competitionId);
    }

    /*
     * 方法说明:   提交
     * Method:   updateSubmit
     * @param request
     * @return void
     * @date 2019/7/18 12:08
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public int updateSubmit(String competitionId) {
        int submitType=0;
        Optional<CmsCompetitionInfo> op = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
        CmsCompetitionInfo cmsCompetitionInfo = op.get();
        if (null==cmsCompetitionInfo.getDatasourceId()||"".equals(cmsCompetitionInfo.getDatasourceId())){
            return 1;
        }
        if(null!=cmsCompetitionInfo.getPreliminarySubmitType()&&0!=cmsCompetitionInfo.getPreliminarySubmitType()){
            submitType=cmsCompetitionInfo.getPreliminarySubmitType();
        }
        if (null!=cmsCompetitionInfo.getCommonSubmitType()&&0!=cmsCompetitionInfo.getCommonSubmitType()){
            submitType=cmsCompetitionInfo.getCommonSubmitType();
        }
        if (1==submitType||2==submitType){
            //数据集,模型竞赛
            if (null==cmsCompetitionInfo.getResultId()||"".equals(cmsCompetitionInfo.getResultId())){
                return 2;
            }
        }
        cmsCompetitionInfo.setCompetitionStatus("2");
        cmsCompetitionInfo.setLastMdifyTime(new Date());
        competitionRepository.saveAndFlush(cmsCompetitionInfo);
        return 0;
    }

    /*
     * 方法说明:    撤回
     * Method:   updateCacle
     * @param request
     * @return void
     * @date 2019/7/18 12:08
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateCacle(String competitionId) {
        Optional<CmsCompetitionInfo> op = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
        CmsCompetitionInfo cmsCompetitionInfo = op.get();
        cmsCompetitionInfo.setCompetitionStatus("1");
        cmsCompetitionInfo.setLastMdifyTime(new Date());
        competitionRepository.saveAndFlush(cmsCompetitionInfo);
    }

    /*
     * 方法说明:   更新参赛人数
     * Method:   updateCompeteNum
     * @param request
     * @return void
     * @date 2019/7/18 12:08
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateCompeteNum(String competitionId) {
        Optional<CmsCompetitionInfo> op = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
        CmsCompetitionInfo cmsCompetitionInfo = op.get();
        // 判断是初决赛还是普通赛
        if (cmsCompetitionInfo.getIsNeedTeam().equals(1)) {
            // 团队
            int teams = competitionRepository.getTeamCompeteNumByCompetitionId(competitionId);
            int individualNum = competitionRepository.getOneCompeteNumByCompetitionId(competitionId);
            cmsCompetitionInfo.setCompetingTeams(teams);
            cmsCompetitionInfo.setIndividualNum(individualNum);
        } else {
            // 个人 --> 统计参赛人数
            int individualNum = competitionRepository.getOneCompeteNumByCompetitionId(competitionId);
            cmsCompetitionInfo.setIndividualNum(individualNum);

        }
        competitionRepository.saveAndFlush(cmsCompetitionInfo);
    }

    /*
     * 方法说明:   更新提交数
     * Method:   updateSubmitNum
     * @param request
     * @return void
     * @date 2019/7/18 12:08
     * @author bxt
     * @version v 1.0.0
     */
    @Override
    @Transactional
    public void updateSubmitNum(String competitionId) {
        Optional<CmsCompetitionInfo> op = competitionRepository.getCompetitionInfoByCompetitionId(competitionId);
        CmsCompetitionInfo cmsCompetitionInfo = op.get();
        // 判断是初决赛还是普通赛
        if (cmsCompetitionInfo.getCompetitionSystem().equals(1)) {
            if (cmsCompetitionInfo.getIsNeedTeam().equals(1)) {
                // 团队
                //初赛
                int preliminarySubmitNum = competitionRepository.getTeamCommonSubmitNumByCompetitionId(competitionId, "PRELIMINARY");
                cmsCompetitionInfo.setPreliminarySubmitNum(preliminarySubmitNum);
                //决赛
                int finalSubmitNum = competitionRepository.getTeamCommonSubmitNumByCompetitionId(competitionId, "FINALS");
                cmsCompetitionInfo.setFinalSubmitNum(finalSubmitNum);
            } else {
                // 个人
                //初赛
                int preliminarySubmitNum = competitionRepository.getOneCommonSubmitNumByCompetitionId(competitionId, "PRELIMINARY");
                cmsCompetitionInfo.setPreliminarySubmitNum(preliminarySubmitNum);
                //决赛
                int finalSubmitNum = competitionRepository.getOneCommonSubmitNumByCompetitionId(competitionId, "FINALS");
                cmsCompetitionInfo.setFinalSubmitNum(finalSubmitNum);
            }

        } else {
            if (cmsCompetitionInfo.getIsNeedTeam().equals(1)) {
                // 团队
                int commonSubmitNum = competitionRepository.getTeamCommonSubmitNumByCompetitionId(competitionId);
                cmsCompetitionInfo.setCommonSubmitNum(commonSubmitNum);
            } else {
                // 个人
                int commonSubmitNum = competitionRepository.getOneCommonSubmitNumByCompetitionId(competitionId);
                cmsCompetitionInfo.setCommonSubmitNum(commonSubmitNum);
            }

        }
        competitionRepository.saveAndFlush(cmsCompetitionInfo);
    }

    @Override
    public List<Map<String, Object>> getCompetitionJudges(JSONObject params) {
        int page = (int) params.get("page");
        int size = (int) params.get("size");
        String judgeDesc = (String) params.get("judgeDesc");
        Pageable pageable = new PageRequest(page, size);
        return competitionRepository.getCompetitionJudges(pageable, judgeDesc);
    }

    @Override
    public int getCompetitionJudgesCount(JSONObject params) {
        String judgeDesc = (String) params.get("judgeDesc");
        return competitionRepository.getCompetitionJudgesCount(judgeDesc);
    }

    @Override
    public List<Map<String, Object>> getEnterprise() {
        return competitionRepository.getEnterprise();
    }
}
