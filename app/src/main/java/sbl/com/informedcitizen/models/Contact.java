package sbl.com.informedcitizen.models;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by atam on 9/4/2014.
 *
 * Model that is filled with the getLegislators call.  Pass this as a bundle into the
 * LegislatorDetailActivity.
 *
 *     1.  Use the name, chamber, and party to fill the static fragment
 *     2.  Use the social media info to fill out the ContactFragment
 *     2.  Use the id to call other services for the other fragments
 */
public class Contact implements Serializable, Comparable {


    public Contact(String id, String n, String c, String p, String addr, String phone, String fb, String twit, String yt) {
        candID = id;
        name = n;
        chamber = c;
        party = p;
        address = addr;
        phoneNo = phone;
        facebook = fb;
        twitter = twit;
        youtube = yt;
    }

    public static Contact returnContact(JSONObject contactJson) {
        Contact legislator = null;
        try {
            String id = contactJson.getString("cid");
            String name = contactJson.getString("firstlast");

            String website = contactJson.getString("website");
            String chamber;
            if(website.contains("house"))
                chamber = "House of Representatives";
            else
                chamber = "Senate";

            String party = contactJson.getString("party");
            if(party.equals("R"))
                party = "Republican";
            else if(party.equals("D"))
                party = "Democrat";

            String addr = contactJson.getString("congress_office");
            String phone = contactJson.getString("phone");
            String fb = contactJson.getString("facebook_id");
            String twitter = contactJson.getString("twitter_id");
            String yt = contactJson.getString("youtube_url");

            legislator = new Contact(id, name, chamber, party, addr, phone,
                         Uri.encode(fb), Uri.encode(twitter), Uri.encode(yt));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return legislator;
    }


    public static ArrayList<Contact> fromJSON(JSONObject contactsJson) {
        ArrayList<Contact> legislators = new ArrayList<Contact>();

        try {
            JSONObject responseValue = contactsJson.getJSONObject("response");
            JSONArray legislatorValue = responseValue.getJSONArray("legislator");

            for(int i=0; i < legislatorValue.length(); i++) {
                JSONObject arrElem = legislatorValue.getJSONObject(i);
                JSONObject contactJson = arrElem.getJSONObject("@attributes");
                Contact contact = returnContact(contactJson);
                legislators.add(contact);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return legislators;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getCandID() {
        return candID;
    }

    public void setCandID(String candID) {
        this.candID = candID;
    }

    String candID;
    String name;
    String chamber;
    String party;
    String address;
    String phoneNo;
    String facebook;
    String twitter;
    String youtube;

    @Override
    public int compareTo(Object another) {
        Contact otherContact = (Contact) another;
        String[] thisTokens = name.split(" ");
        String thisLast = thisTokens[thisTokens.length-1];

        String[] otherTokens = otherContact.getName().split(" ");
        String otherLast = otherTokens[otherTokens.length-1];

        int result = thisLast.compareToIgnoreCase(otherLast);
        return result;
    }
}
