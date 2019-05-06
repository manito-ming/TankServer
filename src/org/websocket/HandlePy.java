package org.websocket;

import java.io.*;
public class HandlePy {

    public  String execPy(String account) {
        System.out.println("================================start============================================");
        String result = "";
        Process process;
        String[] args=null;
        String[] res=null;
        StringBuffer stringBuffer=new StringBuffer();
        {
            try {
                System.out.println("================================start2============================================");

                process = Runtime.getRuntime().exec("python /home/mzh/newFile"+String.valueOf(account)+".py");
                System.out.println("================================start3==========================================");

                InputStream in = process.getInputStream();
                byte[] buffer = new byte[1024];
                int size = in.read(buffer);

                result = new String(buffer, 0,size);
                System.out.println(result);
                System.out.println("================================start4============================================");

                args=result.split("\n");
                for (int i=0;i<4;i++){
                    res=args[i].split("=");
                    stringBuffer.append(res[1]);
                    stringBuffer.append(",");
//                    hashMap.put(res[0],res[1]);
                    System.out.println("================================start5============================================");

                }
//
                System.out.println("================================start6============================================");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("================================start7===========================================");

        return stringBuffer.toString();
    }


    public void createPy(String string,int account)  {
      File f=new File("/home/mzh/newFile"+String.valueOf(account)+".py");
        if(f.exists()){
            f.delete();
        }

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f,true);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
            e.printStackTrace();
        }
        byte[] bbuf = string.getBytes();
        try {
            fos.write(bbuf , 0 , bbuf.length);
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
        System.out.println("create success");

    }



    public static void main(String[] args) {
        HandlePy handle=new HandlePy();


//        handle.createPy(string,account);

//        HashMap accountmap=new HashMap();
//        HashMap colormap=new HashMap();
//        handle.execPy(accountmap,colormap,account,1);
//        System.out.println(accountmap.get(account));
         String a=handle.execPy("222");
        System.out.println(a);
    }


}