package classieTalkie;

import java.security.Key;

import org.json.JSONException;
import org.json.JSONObject;

public class Message_Encoder {
	

    public String turnToJson(Message m)
    {
        JSONObject jsonObject= new JSONObject();
        try
        {
            jsonObject.put("mesgID", m.getMesgID());
            jsonObject.put("mesgStatus",m.getMesgStatus());
            jsonObject.put("managerID", m.getManagerID());
            jsonObject.put("clientID", m.getClientID());
            jsonObject.put("serverPass", m.getServerPass());
            jsonObject.put("clientNUM", m.getClientNUM());
            jsonObject.put("LANPass",m.getLANPass() );
            jsonObject.put("aNum", m.getaNum());
            jsonObject.put("fname", m.getFname());
            jsonObject.put("lname", m.getLname());
            jsonObject.put("participation",m.getParticipation());
            jsonObject.put("message", m.getMessage());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return " ";
        }
    }

    public String AuthenticateManager(String password, String message, int ID, int status)
    {
        Message m = new Message();
        m.setMesgID(0);
        m.setServerPass(password);
        m.setMessage(message);
        m.setManagerID(ID);
        m.setMesgStatus(status);

        return turnToJson(m);
    }

    public String CreateLAN(int ID, String netPassword, int status)
    {
        Message m = new Message();
        m.setMesgID(1);
        m.setManagerID(ID);
        m.setLANPass(netPassword);
        m.setMesgStatus(status);

        return turnToJson(m);
    }

    public String EndLAN(int ID, int status)
    {
        Message m = new Message();
        m.setMesgID((2));
        m.setManagerID((ID));
        m.setMesgStatus((status));

        return turnToJson(m);
    }

    public String AuthenticateClient(String fname, String lname, String anum, String pass, int ID, int status)
    {
        Message m = new Message();
        m.setMesgID((3));
        m.setaNum(anum);
        m.setFname(fname);
        m.setLname(lname);
        m.setLANPass(pass);
        m.setClientID((ID));
        m.setMesgStatus((status));

        return turnToJson(m);
    }

    public String RequestPriorityToken(int ID, int status)
    {
        Message m = new Message();
        m.setMesgID((4));
        m.setClientID((ID));
        m.setMesgStatus((status));

        return turnToJson(m);
    }

    public String ReleasePriorityToken(int clientID, int status)
    {
        Message m = new Message();
        m.setMesgID((5));
        m.setClientID(clientID);
        m.setMesgStatus((status));

        return turnToJson(m);
    }

    public String RequestAnalyticData(int ID,  int numClients, String participation, int status)
    {
        Message m = new Message();
        m.setMesgID((6));
        m.setManagerID(ID);
        m.setClientNUM((numClients));
        m.setMesgStatus(status);
        m.setParticipation(participation);

        return turnToJson(m);
    }

    public String ClientDisconnect(int ID, int status)
    {
        Message m = new Message();
        m.setMesgID((7));
        m.setClientID((ID));
        m.setMesgStatus((status));

        return turnToJson(m);
    }

    public String MuteComm(int ID, int status)
    {
        Message m = new Message();
        m.setMesgID((8));
        m.setManagerID((ID));
        m.setMesgStatus((status));

        return turnToJson(m);
    }

    public String UnMuteComm(int ID, int status)
    {
        Message m = new Message();
        m.setMesgID((9));
        m.setManagerID((ID));
        m.setMesgStatus((status));

        return turnToJson(m);
    }

    public String GracefulShutdown(int ID, int status)
    {
        Message m = new Message();
        m.setMesgID((10));
        m.setManagerID((ID));
        m.setMesgStatus((status));

        return turnToJson(m);
    }

    //the following is used only for testing purposes, not ever used in the program
    public String ClearTheLine()
    {
        Message m = new Message();
        m.setMesgID(99);
        m.setMesgStatus(99);

        return turnToJson(m);
    }

}
