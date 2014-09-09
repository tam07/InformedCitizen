package sbl.com.informedcitizen.models;

/**
 * Created by atam on 9/4/2014.
 *
 * Financial info about a legislator filled by candSummaryRoutesAdapter
 */
public class Legislator {

    public String getFirstElected() {
        return firstElected;
    }

    public void setFirstElected(String firstElected) {
        this.firstElected = firstElected;
    }

    public String getNextElection() {
        return nextElection;
    }

    public void setNextElection(String nextElection) {
        this.nextElection = nextElection;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSpent() {
        return spent;
    }

    public void setSpent(String spent) {
        this.spent = spent;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Legislator(String firstElected, String nextElection, String total, String spent,
                      String cash, String debt, String lastUpdated) {
        this.firstElected = firstElected;
        this.nextElection = nextElection;
        this.total = total;
        this.spent = spent;
        this.cash = cash;
        this.debt = debt;
        this.lastUpdated = lastUpdated;
    }

    String firstElected;
    String nextElection;
    String total;
    String spent;
    String cash;
    String debt;
    String lastUpdated;
}
