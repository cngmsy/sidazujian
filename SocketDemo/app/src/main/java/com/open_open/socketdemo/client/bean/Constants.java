package com.open_open.socketdemo.client.bean;

/**
 * Created by boby on 2017/2/18.
 */

public class Constants {

    //连接ip地址 可以通过以下代码查看当前 IP 地址，记住在网络适配里关掉其他的连接，只保留当前连接
    //注意关掉防火墙，关闭杀毒软件
    /**
     * InetAddress ia = null;
     * try {
     * ia = ia.getLocalHost();
     * <p>
     * String localname = ia.getHostName();
     * String localip = ia.getHostAddress();
     * System.out.println("本机名称是：" + localname);
     * System.out.println("本机的ip是 ：" + localip);
     * } catch (Exception e) {
     * // TODO Auto-generated catch block
     * e.printStackTrace();
     * }
     */
    public static final String HOST = "172.16.52.25";
    //端口号  避免端口冲突 我这里取30003
    public static final int PORT = 30003;

    //收到消息
    public static final int RECEIVE_MSG = 0;

    //发送消息
    public static final int SEND_MSG = 1;

    //发送文件
    public static final int SEND_FILE = 2;

    //传输文件
    public static final int TRANSFER_FILE = 3;

    //传输字符串
    public static final int TRANSFER_STR = 4;

    //聊天列表 发送消息
    public static final int CHAT_SEND = 1;

    //聊天列表 接收消息
    public static final int CHAT_FROM = 2;

    //更新进度
    public static final int PROGRESS = 5;

}
