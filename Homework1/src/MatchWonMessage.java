
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
public class MatchWonMessage extends PlayerMessage implements Serializable {

    private final String teamName;

    MatchWonMessage(String playerName, String teamName) {
        super(playerName);
        this.teamName = teamName;
    }

    String getWonTeam() {
        return this.teamName;
    }

}
