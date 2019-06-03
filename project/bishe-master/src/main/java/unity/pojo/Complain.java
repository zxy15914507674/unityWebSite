package unity.pojo;

/**
 * 投诉信息表
 */
public class Complain {

    private Integer id;
    //投诉人id
    private Integer manid;
    //投诉内容
    private String content;
    //投诉时间
    private String complaintime;
    //投诉人昵称
    private String manname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getManid() {
        return manid;
    }

    public void setManid(Integer manid) {
        this.manid = manid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComplaintime() {
        return complaintime;
    }

    public void setComplaintime(String complaintime) {
        this.complaintime = complaintime;
    }

    public String getManname() {
        return manname;
    }

    public void setManname(String manname) {
        this.manname = manname;
    }
}
