
import java.io.Serializable;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Charmal
 */
public class GameStatMessage extends PlayerMessage implements Serializable {

    List<String> getUsernameBids() {
        return this.usernamesBids;
    }

    String getTeam() {
        return this.team;
    }

    public String team;

    public List<String> usernamesBids;// = {"OSU : 5/3 " , "OSU1 : 5/3 ", "OSU2 : 5/3 ", "OSU3 : 5/3 "};

    public GameStatMessage(String playerName, String team, List<String> usernamesBids) {
        super(playerName);
        this.team = team;
        this.usernamesBids = usernamesBids;
    }

}
