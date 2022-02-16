package com.application.model;

import java.util.Date;

public class Servant {
    /**
     * ID
     */
    private String id;

    /**
     * 立绘图片文件夹名
     */
    private String imgName;

    /**
     * 名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 兵种
     */
    private String classType;

    /**
     * 从者类型(拐,打手)
     */
    private String heroType;

    /**
     * 活动从者
     */
    private String eventFlag;

    /**
     * 生命值
     */
    private Integer hp;

    /**
     * 攻击
     */
    private Integer attact;

    /**
     * 1技能
     */
    private String skill1;

    /**
     * 2技能
     */
    private String skill2;

    /**
     * 3技能
     */
    private String skill3;

    /**
     * 宝具名
     */
    private String skillExtra;

    /**
     * 宝具类型(单体,全体)
     */
    private String extraType;

    /**
     * 宝具颜色
     */
    private String extraColor;

    /**
     * 突破极限
     */
    private Integer limit;

    /**
     * 特性
     */
    private String traits;

    /**
     * 队伍
     */
    private Integer team;

    /**
     * 喜欢
     */
    private String favorite;

    /**
     * 评价等级
     */
    private String rank;

    /**
     * 登录日期
     */
    private Date createDatetime;

    /**
     * 更新日期
     */
    private Date updateDatetime;

    /**
     * 设定ID
     */
    public void setId(String id) {
        this.id = null == id ? "" : id;
    }

    /**
     * 取得ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设定立绘图片文件夹名
     */
    public void setImgName(String imgName) {
        this.imgName = null == imgName ? "" : imgName;
    }

    /**
     * 取得立绘图片文件夹名
     */
    public String getImgName() {
        return imgName;
    }

    /**
     * 设定名
     */
    public void setName(String name) {
        this.name = null == name ? "" : name;
    }

    /**
     * 取得名
     */
    public String getName() {
        return name;
    }

    /**
     * 设定昵称
     */
    public void setNickName(String nickName) {
        this.nickName = null == nickName ? "" : nickName;
    }

    /**
     * 取得昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设定兵种
     */
    public void setClassType(String classType) {
        this.classType = null == classType ? "" : classType;
    }

    /**
     * 取得兵种
     */
    public String getClassType() {
        return classType;
    }

    /**
     * 设定从者类型(拐,打手)
     */
    public void setHeroType(String heroType) {
        this.heroType = null == heroType ? "" : heroType;
    }

    /**
     * 取得从者类型(拐,打手)
     */
    public String getHeroType() {
        return heroType;
    }

    /**
     * 设定活动从者
     */
    public void setEventFlag(String eventFlag) {
        this.eventFlag = null == eventFlag ? "" : eventFlag;
    }

    /**
     * 取得活动从者
     */
    public String getEventFlag() {
        return eventFlag;
    }

    /**
     * 设定生命值
     */
    public void setHp(Integer hp) {
        this.hp = hp;
    }

    /**
     * 取得生命值
     */
    public Integer getHp() {
        return hp;
    }

    /**
     * 设定攻击
     */
    public void setAttact(Integer attact) {
        this.attact = attact;
    }

    /**
     * 取得攻击
     */
    public Integer getAttact() {
        return attact;
    }

    /**
     * 设定1技能
     */
    public void setSkill1(String skill1) {
        this.skill1 = null == skill1 ? "" : skill1;
    }

    /**
     * 取得1技能
     */
    public String getSkill1() {
        return skill1;
    }

    /**
     * 设定2技能
     */
    public void setSkill2(String skill2) {
        this.skill2 = null == skill2 ? "" : skill2;
    }

    /**
     * 取得2技能
     */
    public String getSkill2() {
        return skill2;
    }

    /**
     * 设定3技能
     */
    public void setSkill3(String skill3) {
        this.skill3 = null == skill3 ? "" : skill3;
    }

    /**
     * 取得3技能
     */
    public String getSkill3() {
        return skill3;
    }

    /**
     * 设定宝具名
     */
    public void setSkillExtra(String skillExtra) {
        this.skillExtra = null == skillExtra ? "" : skillExtra;
    }

    /**
     * 取得宝具名
     */
    public String getSkillExtra() {
        return skillExtra;
    }

    /**
     * 设定宝具类型(单体,全体)
     */
    public void setExtraType(String extraType) {
        this.extraType = null == extraType ? "" : extraType;
    }

    /**
     * 取得宝具类型(单体,全体)
     */
    public String getExtraType() {
        return extraType;
    }

    /**
     * 设定宝具颜色
     */
    public void setExtraColor(String extraColor) {
        this.extraColor = null == extraColor ? "" : extraColor;
    }

    /**
     * 取得宝具颜色
     */
    public String getExtraColor() {
        return extraColor;
    }

    /**
     * 设定突破极限
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * 取得突破极限
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * 设定特性
     */
    public void setTraits(String traits) {
        this.traits = null == traits ? "" : traits;
    }

    /**
     * 取得特性
     */
    public String getTraits() {
        return traits;
    }

    /**
     * 设定队伍
     */
    public void setTeam(Integer team) {
        this.team = team;
    }

    /**
     * 取得队伍
     */
    public Integer getTeam() {
        return team;
    }

    /**
     * 设定喜欢
     */
    public void setFavorite(String favorite) {
        this.favorite = null == favorite ? "" : favorite;
    }

    /**
     * 取得喜欢
     */
    public String getFavorite() {
        return favorite;
    }

    /**
     * 设定评价等级
     */
    public void setRank(String rank) {
        this.rank = null == rank ? "" : rank;
    }

    /**
     * 取得评价等级
     */
    public String getRank() {
        return rank;
    }

    /**
     * 设定登录日期
     */
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * 取得登录日期
     */
    public Date getCreateDatetime() {
        return createDatetime;
    }

    /**
     * 设定更新日期
     */
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    /**
     * 取得更新日期
     */
    public Date getUpdateDatetime() {
        return updateDatetime;
    }

}
