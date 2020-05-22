package test;
import java.io.IOException;
import java.io.InputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
public class analysisFile {
    public static void main(String[] args) {
//        String host = "192.168.235.132";
//        int port = 22;
//        String user = "dingding";
//        String password = "100600";
//        String command = "grep \"data\"  /tmp/ding ";
//        String res = null;
//        try {
//            res = exeCommand(host,port,user,password,command);
//        } catch (JSchException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    if (res.length()==0){
//        System.out.println("查找内容不存在");
//    }
//    else{
//        System.out.println("查找内容存在");
//        System.out.println(res.length());
//    }
               Person1 person1=  new Person1("1","rew");
               System.out.println(JSON.toJSONString(person1));
    }

    public static String exeCommand(String host, int port, String user, String password, String command) throws JSchException, IOException {

        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        //    java.util.Properties config = new java.util.Properties();
        //   config.put("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String out = IOUtils.toString(in, "UTF-8");
        channelExec.disconnect();
        session.disconnect();
        return out;
    }

}

class  Person1{
    String age;
    String name;

    public Person1(String age, String name) {
        this.age = age;
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Person1{" +
                "age='" + age + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
