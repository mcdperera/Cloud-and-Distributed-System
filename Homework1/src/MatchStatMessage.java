
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

    public Integer teamRedScore;

    public Integer teamBlueScore;

    public MatchStatMessage(String playerName, Integer teamRedScore, Integer teamBlueScore) {
        super(playerName);
        this.teamRedScore = teamRedScore;
        this.teamBlueScore = teamBlueScore;

    }

    String getRedTeamScore() {
        return this.teamRedScore.toString();
    }

    String getBlueTeamScore() {
        return this.teamBlueScore.toString();
    }
}
