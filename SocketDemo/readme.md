#socket实现简单的字符串聊天功能





效果图gif
![](readme.gif)
##修改ip地址

  //连接ip地址 可以通过以下代码查看当前 IP 地址，记住在网络适配里关掉其他的连接，只保留当前连接

  //注意关掉防火墙，关闭杀毒软件


  ```

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

  ```


##选择main方法 ,右击使用java代码运行,开启socket服务监听接口数据

```
public class MyServer {

    //多客户端
    public static ArrayList<Socket> sSockets = new ArrayList<Socket>();

    public static void main(String[] args) {
        //DatagramSocket 基于UDP协议的
        ServerSocket serverSocket = null;

        try {
            //创建服务器的socket对象
            serverSocket = new ServerSocket(Constants.PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                sSockets.add(socket);

                new Thread(new ServerThread(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

```

##开启Activity安装模拟器,注意模拟器必须和电脑在统一网段内

```
public class MyServer {

    //多客户端
    public static ArrayList<Socket> sSockets = new ArrayList<Socket>();

    public static void main(String[] args) {
        //DatagramSocket 基于UDP协议的
        ServerSocket serverSocket = null;

        try {
            //创建服务器的socket对象
            serverSocket = new ServerSocket(Constants.PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                sSockets.add(socket);

                new Thread(new ServerThread(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

```

##客户端通过点击按钮触发该方法来发送字符串内容
```
/**
     * send  message
     *
     * @param v
     * 发送文本消息
     */
    public void send(View v) {
        if (mEtContent.getText().toString().equals("")) {
            Toast.makeText(this, "输入的内容不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        Transmission trans = new Transmission();
        trans.itemType = Constants.CHAT_SEND;
        trans.content = mEtContent.getText().toString();
        trans.transmissionType = Constants.TRANSFER_STR;

        Message message = new Message();
        message.what = Constants.SEND_MSG;
        message.obj = mGson.toJson(trans);

        if (mClientThread.getWriteHandler() != null) {
            mClientThread.getWriteHandler().sendMessage(message);
            mEtContent.setText("");
        }

        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }
```

##客户端通过接受handler发来的消息来接受字符串消息
```
 Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.RECEIVE_MSG) {
                String content = msg.obj.toString();
                Log.e("MainActivity", "handleMessage--------" + content);
                Transmission trans = mGson.fromJson(content, Transmission.class);
                if (trans.transmissionType == Constants.TRANSFER_STR) {
                    mAdapter.addData(trans);
                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                }
            } else if (msg.what == Constants.PROGRESS) {
                mRelativeProgress.setVisibility(View.VISIBLE);

                if (msg.obj == null) {
                    return;
                }

                mTvProgress.setText(msg.obj.toString() + "%");

                if (msg.obj.toString().equals("100")) {
                    mRelativeProgress.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();

                    Transmission trans = new Transmission();
                    trans.itemType = Constants.CHAT_SEND;
                    trans.content = mFilePath;
                    trans.showType = 1;

                    mAdapter.addData(trans);
                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                }
            }
        }
    };
```

##客户端接受消息的handle发送消息的地方是通过构造方法传递到ClientThread里面的handler发送的
```
try {
            //创建socket
            mSocket = new Socket(Constants.HOST, Constants.PORT);
            //获取到读写对象
            mPrintWriter = new PrintWriter(mSocket.getOutputStream());
            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

            //新开线程读取消息 并发送消息
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String content = null;
                    try {
                        while ((content = mBufferedReader.readLine()) != null) {
                            Transmission trans = mGson.fromJson(content, Transmission.class);
                            if (trans.transmissionType == Constants.TRANSFER_STR) {
                                Message msg = new Message();
                                msg.what = Constants.RECEIVE_MSG;
                                msg.obj = content;
                                mSendHandler.sendMessage(msg);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            //当前线程创建 handler
            Looper.prepare();
            mWriteHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == Constants.SEND_MSG) {
                        mPrintWriter.write(msg.obj.toString() + "\r\n");
                        mPrintWriter.flush();
                    }
                }
            };
            Looper.loop();

        } catch (IOException e) {
            e.printStackTrace();
            //出现异常关闭资源
            try {
                if (mPrintWriter != null) {
                    mPrintWriter.close();
                }
                if (mBufferedReader != null) {
                    mBufferedReader.close();
                }
                if (mSocket != null) {
                    mSocket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
```