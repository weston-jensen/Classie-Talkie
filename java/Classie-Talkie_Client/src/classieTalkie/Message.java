package classieTalkie;

import java.io.Serializable;
import java.security.Key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


public class Message implements Serializable {
	
	  protected int mesgID = -99;
    protected int mesgStatus = -99;
    protected int managerID = -99;
    protected int clientID = -99;
    protected String serverPass = " ";
    protected int clientNUM = -99;
    protected String LANPass = " ";
    protected String aNum = " ";
    protected String fname = " ";
    protected String lname = " ";
    protected String participation = " ";
    protected String message = " ";
    protected String ErrorType = " ";

    public Message()
    {

    }


    public int getMesgID() {
        return mesgID;
    }

    public void setMesgID(int mesgID) {
        this.mesgID = mesgID;
    }

    public int getMesgStatus() {
        return mesgStatus;
    }

    public void setMesgStatus(int mesgStatus) {
        this.mesgStatus = mesgStatus;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getServerPass() {
        return serverPass;
    }

    public void setServerPass(String serverPass) {
        this.serverPass = serverPass;
    }

    public String getLANPass() {
        return LANPass;
    }

    public void setLANPass(String lANPass) {
        LANPass = lANPass;
    }

    public String getaNum() {
        return aNum;
    }

    public void setaNum(String aNum) {
        this.aNum = aNum;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getClientNUM() {
        return clientNUM;
    }

    public void setClientNUM(int clientNUM) {
        this.clientNUM = clientNUM;
    }

    public String getParticipation() {
        return participation;
    }

    public void setParticipation(String participation) {
        this.participation = participation;
    }

    public String getErrorType() {
        return ErrorType;
    }

    public void setErrorType(String errorType) {
        ErrorType = errorType;
    }

}

