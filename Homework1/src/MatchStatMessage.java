
import java.io.Serializable;
import java.util.ArrayList;

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

    /**
     * The serial version UID
     */
    protected static final long serialVersionUID = 1112122200L;

    /**
     * The array list contains the match stat
     */
    public final ArrayList<MatchStat> MatchStatList;

    /**
     * Construct the Match stat message
     *
     * @param playerName
     * @param MatchStatList
     */
    public MatchStatMessage(String playerName, ArrayList<MatchStat> MatchStatList) {
        super(playerName);
        this.MatchStatList = MatchStatList;
    }

    /**
     * Returns the match stat details.
     */
    ArrayList<MatchStat> getMatchStatMessageList() {
        return MatchStatList;
    }
}
