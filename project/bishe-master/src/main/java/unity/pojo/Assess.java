package unity.pojo;


import java.io.Serializable;

public class Assess implements Serializable {
    private Integer id;

    private Integer valuerid;

    private Integer workid;

    private String content;

    private Integer star;

    private String assessdate;

    private String assessername;

    private static final long serialVersionUID = 1L;

    public Assess() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValuerid() {
        return valuerid;
    }

    public void setValuerid(Integer valuerid) {
        this.valuerid = valuerid;
    }

    public Integer getWorkid() {
        return workid;
    }

    public void setWorkid(Integer workid) {
        this.workid = workid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getAssessdate() {
        return assessdate;
    }

    public void setAssessdate(String assessdate) {
        this.assessdate = assessdate == null ? null : assessdate.trim();
    }

    public String getAssessername() {
        return assessername;
    }

    public void setAssessername(String assessername) {
        this.assessername = assessername;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", valuerid=").append(valuerid);
        sb.append(", workid=").append(workid);
        sb.append(", content=").append(content);
        sb.append(", star=").append(star);
        sb.append(", assessdate=").append(assessdate);
        sb.append(", assessername=").append(assessername);
        sb.append("]");
        return sb.toString();
    }
}