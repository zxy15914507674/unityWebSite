package unity.pojo;

import java.io.Serializable;

public class Work implements Serializable {
    private Integer id;

    private String workname;

    private Integer workmanid;

    private String content;

    private String pictures;

    private String keyword;

    private Integer worktype;

    private Integer score;

    private String workaddress;

    private Integer forfree;      //1为免费下载，0为其他


    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkname() {
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname == null ? null : workname.trim();
    }

    public Integer getWorkmanid() {
        return workmanid;
    }

    public void setWorkmanid(Integer workmanid) {
        this.workmanid = workmanid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures == null ? null : pictures.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public Integer getWorktype() {
        return worktype;
    }

    public void setWorktype(Integer worktype) {
        this.worktype = worktype;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getWorkaddress() {
        return workaddress;
    }

    public void setWorkaddress(String workaddress) {
        this.workaddress = workaddress;
    }

    public Integer getForfree() {
        return forfree;
    }

    public void setForfree(Integer forfree) {
        this.forfree = forfree;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", workname=").append(workname);
        sb.append(", workmanid=").append(workmanid);
        sb.append(", content=").append(content);
        sb.append(", pictures=").append(pictures);
        sb.append(", keyword=").append(keyword);
        sb.append(", worktype=").append(worktype);
        sb.append(", score=").append(score);
        sb.append(", workaddress=").append(workaddress);
        sb.append(", forfree=").append(forfree);
        sb.append("]");
        return sb.toString();
    }
}