package com.bonc.turing.cms.manage.entity.competition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.entity
 * @ClassName: CmsCompetitionInfo
 * @Author: bxt
 * @Description: 竞赛
 * @Date: 2019/7/11 19:04
 * @Version: 1.0
 */
@Entity
@JsonIgnoreProperties(allowSetters = true, value = {"handler", "hibernateLazyInitializer"})
@Table(name = "competition_info")
@Data
public class CmsCompetitionInfo {
    private static final long serialVersionUID = -7610507011609179180L;
    //竞赛编号
    @Id
    private String competitionId;
    //竞赛名称
    private String competitionName;
    //banner
    private String banner;
    //logo
    private String logo;
    //竞赛类别1-内部竞赛,2-外部常规竞赛
    private Integer competitionType;
    //组织形式1-团队 0-个人
    private Integer isNeedTeam;
    //团队人数上限
    private Integer teamPersonUp;
    //团队人数下限
    private Integer teamPersonDown;
    //团队合并1-支持 0 不支持
    private Integer competitionMerge;
    //主办方编号
    private String enterpriseId;
    //主办方名称
    private String enterpriseName;
    //竞赛等级1 -练习赛 2-奖金赛
    private Integer competitionGrade;
    //奖池
    private Long awardMoney;
    //货币类型1-人民币，2-美元
    private String currencyType;
    //赛制1-决赛制、2-普通赛
    private String competitionSystem;
    //简介
    private String introduction;
    //普通赛赛题
    private String problemCommon;
    //初赛赛题
    private String problemPreliminary;
    //决赛赛题
    private String problemFinal;
    //时间及赛程安排
    private String raceScheduleTime;
    //组队开始时间
    private Date teamStartTime;
    //组队结束时间
    private Date teamEndTime;
    //普通赛发布时间
    private Date commonRleaseStartDate;
    //普通赛停止报名时间
    private Date commonRleaseEndDate;
    //普通赛开始时间
    private Date commonBeginDate;
    //普通赛开始时间
    private Date commonEndDate;
    //普通赛发布时间
    private Date commonPublishDate;
    //初赛发布时间(即可报名)
    private Date preliminaryRleaseStartDate;
    //初赛报名截止
    private Date preliminaryRleaseEndDate;
    //初赛报名截止
    private Date preliminaryBeginDate;
    //初赛结束日期
    private Date preliminaryEndDate;
    //初赛发榜日期
    private Date preliminaryPublishDate;
    //决赛开始日期
    private Date finalBeginDate;
    //决赛结束日期
    private Date finalEndDate;
    //决赛发榜日期
    private Date finalPublishDate;
    //奖品
    private String prize;
    //显示/隐藏
    private Integer showHide;
    //普通赛评估方式
    private String commonEvaluationType;
    //初赛评估方式
    private String preliminaryEvaluationType;
    //决赛赛评估方
    private String finalEvaluationType;
    //参赛对象
    private String participants;
    //交流群
    private String alternatingGroup;
    //组织单位
    private String organizationalUnit;
    //规则
    private String rules;
    //标签
    private String label;
    //竞赛状态1-准备中，2-待发布，3-比赛中，4-结束，5-初赛评分中，7-初赛结束，8-决赛评分中，9-评分中(普通赛，新手赛)
    private String competitionStatus;
    //排序
    private Integer sorting;
    //是否置顶
    private Integer isTop;
    //数据集编号
    private String datasourceId;
    //数据集编号
    private String resultId;
    //创建人
    private String createUser;
    //创建时间
    private Date createTime;
    //最后修改时间
    private Date lastMdifyTime;
    //提交成绩方式普通赛
    private Integer commonSubmitType;
    //提交成绩方式初赛
    private Integer preliminarySubmitType;
    //提交成绩方式决赛
    private Integer finalSubmitType;
    //提交成绩数量
    private Integer commonSubmitNum;
    //提交成绩数量
    private Integer preliminarySubmitNum;
    //提交成绩数量
    private Integer finalSubmitNum;
    //是否需要评委
    private String isNeedJudges;
    //行业
    private String industryId;
    //决赛队伍数
    private Integer teams;
    //参赛队伍数
    private Integer competingTeams;
    //历史参赛队伍数
    private Integer hisCompetingTeams;
    //按比例或按人数(得奖设置)
    private String isScaleNumber;
    //按比例或按人数(得奖设置)
    private String isScaleNumberPreliminary;
    //按比例或按人数(得奖设置)
    private String isScaleNumberFinal;
    //金牌数
    private String goldMedalPnumber;
    //金牌百分比
    private String goldMedalAmountRate;
    //银牌数
    private String silverMedalPnumber;
    //银牌百分比
    private String silverMedalAmountRate;
    //铜牌数
    private String bronzeMedalPnumber;
    //铜牌百分比
    private String bronzeMedalAmountRate;
    //金牌数
    private String preliminaryGoldMedalPnumber;
    //金牌百分比
    private String preliminaryGoldMedalAmountRate;
    //银牌数
    private String preliminarySilverMedalPnumber;
    //银牌百分比
    private String preliminarySilverMedalAmountRate;
    //铜牌数
    private String preliminaryBronzeMedalPnumber;
    //铜牌百分比
    private String preliminaryBronzeMedalAmountRate;
    //金牌数
    private String finalGoldMedalPnumber;
    //金牌百分比
    private String finalGoldMedalAmountRate;
    //银牌数
    private String finalSilverMedalPnumber;
    //银牌百分比
    private String finalSilverMedalAmountRate;
    //铜牌数
    private String finalBronzeMedalPnumber;
    //铜牌百分比
    private String finalBronzeMedalAmountRate;
    //分类算法/逻辑算法
    private String algorithm;
    //a榜标志
    private Integer isDoneScorea;
    //b榜标志
    private Integer isDoneScoreb;
    //周期（练习赛）
    private Integer cycle;

    private String scoringType;
    private String tenantId;
    private String submitType;
    private String competitionLevel;
    private Date noviceBeginDate;
    private Integer noviceCycle;
    private Date noviceEndDate;
    private Integer noviceIssue;
    private Date novicePublishDate;
    private String industry;
    private String score;
    private Integer rankNo;
    private String datasetId;
    private String bScore;
    private Integer bRankNo;
    private Date isTopTime;
    private Integer isJoin;
    private Integer individualNum;

}
