package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/8.
 */

public class Champion implements Serializable {
    private static final long serialVersionUID = 4971726143463936918L;

    private String id;
    private List<Leagus> listLeague;//一级标题
    private List<ChampionItem> listSeason;//二级标题
    private List<TeamRate> listTeamRate;//具体的比分

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Leagus> getListLeague() {
        return listLeague;
    }

    public void setListLeague(List<Leagus> listLeague) {
        this.listLeague = listLeague;
    }

    public List<ChampionItem> getListSeason() {
        return listSeason;
    }

    public void setListSeason(List<ChampionItem> listSeason) {
        this.listSeason = listSeason;
    }

    public List<TeamRate> getListTeamRate() {
        return listTeamRate;
    }

    public void setListTeamRate(List<TeamRate> listTeamRate) {
        this.listTeamRate = listTeamRate;
    }

    public static class TeamRate implements Serializable {

        private static final long serialVersionUID = 4989562373824053678L;

        private int id;
        private String teamName;
        private String payRate;
        private String teamRates;
        private String leagueName;
        private String matchName;
        private int leagueId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getPayRate() {
            return payRate;
        }

        public void setPayRate(String payRate) {
            this.payRate = payRate;
        }

        public String getTeamRates() {
            return teamRates;
        }

        public void setTeamRates(String teamRates) {
            this.teamRates = teamRates;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
        }

        public int getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(int leagueId) {
            this.leagueId = leagueId;
        }
    }

    public static class Leagus implements Serializable {

        private static final long serialVersionUID = 6884028737862534344L;

        private String id;
        private String leagueName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }
    }

    public static class ChampionItem implements Serializable {

        private static final long serialVersionUID = -1059913589393021429L;

        private String id;
        private String matchName;
        private String leagueName;
        private int leagueId;

        public String getId() {
            return id;
        }

        public int getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(int leagueId) {
            this.leagueId = leagueId;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }
    }

}
