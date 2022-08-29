package com.bonc.turing.cms.exercise.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @desc 教材章节
 * @author yh
 * @date 2019.12.25
 */
@Data
@Entity
public class QtChapter {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id; //主键id
    private String textBookId; //教材id
    private String chapterName; //章节名称
    private int type; //章节类别（0免费 1付费）
    private String pdfPath; //文档路径
    private String pdfName; //文档名称
    private int sort; //章节序号
    private int pageNum;//章节总页数
    private Date  createTime;//创建时间

}
