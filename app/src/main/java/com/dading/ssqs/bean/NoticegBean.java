package com.dading.ssqs.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/7/11.
 */
public class NoticegBean implements Serializable {

    private static final long serialVersionUID = 3296935014247190750L;
    /**
     * id : 4
     * title : sfds
     * content : fsafsaf
     * remark : asfasfasf
     * createDate : 2017-07-11 10:16:05
     * type : 1
     */

    private int id;
    private String title;
    private String content;
    private String remark;
    private String createDate;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
