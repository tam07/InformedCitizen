package sbl.com.informedcitizen.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atam on 9/4/2014.
 */
public class Contributor extends BaseModel {
    String name;

    public Contributor(String contributorName, String pacAmt, String individualAmt, String totalAmt) {
        name = contributorName;
        pacContrib = pacAmt;
        individContrib = individualAmt;
        totalContrib = totalAmt;
        //candidateID = pID;
    }


    public static Contributor fromJSON(JSONObject jsonObj) {
        Contributor contributor = null;
        try {
            String contributorName = jsonObj.getString("org_name");
            String pacDollars = jsonObj.getString("pacs");
            String individualDollars = jsonObj.getString("indivs");
            String totalContrib = jsonObj.getString("total");

            contributor = new Contributor(contributorName, pacDollars, individualDollars,
                                                      totalContrib);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contributor;
    }


    public static ArrayList<Contributor> fromJSON(JSONArray jsonArr) {
        ArrayList<Contributor> contributorsList = new ArrayList<Contributor>();
        for(int i=0; i < jsonArr.length(); i++) {
            try {
                JSONObject contribJSON = jsonArr.getJSONObject(i);
                JSONObject attributesValue = contribJSON.getJSONObject("@attributes");
                Contributor currContributor = Contributor.fromJSON(attributesValue);
                contributorsList.add(currContributor);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return contributorsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
