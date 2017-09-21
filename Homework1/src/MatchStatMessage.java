
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Charmal
 */
public class MatchStatMessage extends PlayerMessage implements Serializable {

    public Integer round;

    public Integer teamRedScore;

    public Integer teamBlueScore;

    public MatchStatMessage(String playerName, Integer round, Integer teamRedScore, Integer teamBlueScore) {
        super(playerName);
        this.round = round;
        this.teamRedScore = teamRedScore;
        this.teamBlueScore = teamBlueScore;
    }

    Integer getRedTeamScore() {
        return this.teamRedScore;
    }

    Integer getBlueTeamScore() {
        return this.teamBlueScore;
    }
}
