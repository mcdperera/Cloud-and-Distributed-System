
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 502759576
 */
public class MatchStat implements Serializable {

    public final Integer playerIndex;
    public final Integer round;

    public Integer teamRedScore;

    public Integer teamBlueScore;

    public MatchStat(Integer playerIndex, Integer round, Integer teamRedScore, Integer teamBlueScore) {

        this.playerIndex = playerIndex;
        this.round = round;
        this.teamRedScore = teamRedScore;
        this.teamBlueScore = teamBlueScore;
    }

    Integer getPlayerIndex() {
        return this.playerIndex;
    }
    
    Integer getRound() {
        return this.round;
    }

    Integer getRedTeamScore() {
        return this.teamRedScore;
    }

    Integer getBlueTeamScore() {
        return this.teamBlueScore;
    }

    void setRedTeamScore(int redTeamScore) {
        this.teamRedScore = redTeamScore;
    }

    void setBlueTeamScore(int blueTeamScore) {
        this.teamBlueScore = blueTeamScore;
    }
}
