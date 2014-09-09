package sbl.com.informedcitizen.models;

/**
 * Created by atam on 9/4/2014.
 */
public class BaseModel {
    public String getPacContrib() {
        return pacContrib;
    }

    public void setPacContrib(String pacContrib) {
        this.pacContrib = pacContrib;
    }

    public String getIndividContrib() {
        return individContrib;
    }

    public void setIndividContrib(String individContrib) {
        this.individContrib = individContrib;
    }

    public String getTotalContrib() {
        return totalContrib;
    }

    public void setTotalContrib(String totalContrib) {
        this.totalContrib = totalContrib;
    }

    public String getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(String candidateID) {
        this.candidateID = candidateID;
    }

    protected String candidateID;
    protected String pacContrib;
    protected String individContrib;
    protected String totalContrib;
    protected String candidateName;
}
