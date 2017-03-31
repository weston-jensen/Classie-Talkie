package classieTalkie;

import org.json.JSONObject;

public class JSON_Decoder {
		
	public Message decodeMessage(String mesg)
	{
		Message m = new Message();
        try
        {
            JSONObject jsonObject = new JSONObject(mesg);
            
            m.setMesgID(jsonObject.getInt("mesgID"));
            m.setaNum(jsonObject.getString("aNum"));
            m.setFname(jsonObject.getString("fname"));
            m.setLname(jsonObject.getString("lname"));
            m.setLANPass(jsonObject.getString("LANPass"));
            m.setClientID(jsonObject.getInt("clientID"));
            m.setMesgStatus(jsonObject.getInt("mesgStatus"));
            m.setMessage(jsonObject.getString("message"));
            m.setParticipation(jsonObject.getString("participation"));
            m.setServerPass(jsonObject.getString("serverPass"));
            m.setManagerID(jsonObject.getInt("managerID"));
            m.setClientNUM(jsonObject.getInt("clientNUM"));
            
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return m;
		
	}

}
