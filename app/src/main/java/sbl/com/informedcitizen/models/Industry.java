package sbl.com.informedcitizen.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atam on 9/4/2014.
 */
public class Industry extends BaseModel {
    String name;

    public Industry(String contributorName, String pacAmt, String individualAmt, String totalAmt) {
        name = contributorName;
        pacContrib = pacAmt;
        individContrib = individualAmt;
        totalContrib = totalAmt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCandName() { return candidateName; }

    public void setCandName(String candName) { candidateName = candName; }

    public static Industry returnIdustry(JSONObject attrVal) {
        Industry industry = null;
        try {
            String name = attrVal.getString("industry_name");
            String pacDollars = attrVal.getString("pacs");
            String individDollars = attrVal.getString("indivs");
            String totalDollars = attrVal.getString("total");
            industry = new Industry(name, pacDollars, individDollars, totalDollars);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return industry;
    }

    public static ArrayList<Industry> fromJSON(JSONArray industryValue) {
        ArrayList<Industry> industries = new ArrayList<Industry>();
        try {
            for(int i=0; i < industryValue.length(); i++) {
                JSONObject currVal = industryValue.getJSONObject(i);
                JSONObject attributesValue = currVal.getJSONObject("@attributes");

                Industry currIndusty = Industry.returnIdustry(attributesValue);
                industries.add(currIndusty);
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        return industries;
    }
}
