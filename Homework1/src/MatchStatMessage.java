
import java.io.Serializable;
import java.util.ArrayList;
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
public class MatchStatMessage extends PlayerMessage implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    public final ArrayList<MatchStat> MatchStatList;

    public MatchStatMessage(String playerName, ArrayList<MatchStat> MatchStatList) {
        super(playerName);
        this.MatchStatList = MatchStatList;
    }

    ArrayList<MatchStat> getMatchStatMessageList() {
        return MatchStatList;
    }

//    /**
//     *
//     */
//    public final Integer round;
//
//    public final Integer teamRedScore;
//
//    public final Integer teamBlueScore;
//
//    //public final List<MatchStatMessage> getMatchStatMessageList;
//
//    public MatchStatMessage(String playerName, Integer round, Integer teamRedScore, Integer teamBlueScore) {
//        super(playerName);
//        this.round = round;
//        this.teamRedScore = teamRedScore;
//        this.teamBlueScore = teamBlueScore;
//    }
//
//    Integer getRedTeamScore() {
//        return this.teamRedScore;
//    }
//
//    Integer getBlueTeamScore() {
//        return this.teamBlueScore;
//    }
//    List<MatchStat> getMatchStatMessageList() {
//        return matchStatMessageList;
//    }
//
//    vpod setMatchStatMessageList(List<MatchStatMessage> matchStatMessageList) {
//        this.matchStatMessageList = matchStatMessageList;
//    }
}
