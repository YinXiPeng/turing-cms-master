package com.bonc.turing.cms.manage.entity.notify;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "notify")
@GenericGenerator(name = "sysUUID", strategy = "uuid")
@Data
public class Notify implements Serializable {

    private static final long serialVersionUID = 1L;
    /*
    *  通知id*/
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String  notifyId;
    /*
     * 通知标题*/
    private String  notifyTitle;
    /*
     * 通知类型（0：系统通知 ； 1：竞赛通知）*/
    private String  notifyType;
    /*
     * 通知内容*/
    private String  notifyMsg;
    /*
     * 通知人*/
    private String  notifyUserGuid;
    /*
     * 通知渠道-手机（0：否；1：是）*/
    private String  notifyPhoneFlag;
    /*
     * 通知渠道-邮件（0：否；1：是*/
    private String  notifyEmailFlag;
    /*
     * 通知渠道-公司网站（0：否；1：是）*/
    private String  notifyWebFlag;
    /*
     * 通知时间*/
    private Long  notifyTime;
    /*
     * 管理员id*/
    private String  adminGuid;
    /*
     * 跳转地址（用于通知是跳转到指定页面）*/
    private String  jumpUrl;
    /*
     * 竞赛编号*/
    private String  id;

    private String  competitionName;
    /*
     * 竞赛状态*/
    private String  competitionState;
    /*
     * 消息是否已读（0：未读；1：已读）*/
    private int  readState;
    /*
     * 最新修改 */
    private Long  lastModify;
    /*
     * 提示信息（“点我跳转”等） */
    private String  tipsMsg;
    //是否已发送（0：否；1：是）
    private int  issend;
    //被通知人类型（0 所有用户；1以参赛用户；2；未参赛用户）
    private int  notifyUserType;

    private String notifyEntryId;
}
