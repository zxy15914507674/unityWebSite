package unity.vo;

import javax.swing.*;
import java.util.List;

/**
 * 展示类
 */
public class Workvo {

    /**
     * id
     */
    private Integer workid;

    /**
     * 名称
     */
    private String workname;
    /**
     * 所有图片
     */
    private List<String> imagelist;
    /**
     * 首张图片
     */
    private String indeximage;
    /**
     * 介绍内容
     */
    private String content;
    /**
     * 下载地址
     */
    private String dladdress;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 作者
     */
    private String maker;
    /**
     * 实验类型
     */
    private String type;

    /**
     * 是否为免费
     */
    Integer forfree;

    public String getWorkname() {
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname;
    }

    public List<String> getImagelist() {
        return imagelist;
    }

    public void setImagelist(List<String> imagelist) {
        this.imagelist = imagelist;
    }

    public String getIndeximage() {
        return indeximage;
    }

    public void setIndeximage(String indeximage) {
        this.indeximage = indeximage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDladdress() {
        return dladdress;
    }

    public void setDladdress(String dladdress) {
        this.dladdress = dladdress;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWorkid() {
        return workid;
    }

    public void setWorkid(Integer workid) {
        this.workid = workid;
    }

    public Integer getForfree() {
        return forfree;
    }

    public void setForfree(Integer forfree) {
        this.forfree = forfree;
    }
}
