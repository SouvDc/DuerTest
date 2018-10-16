package com.dc.duertest.utils;

import android.content.Context;
import android.util.Log;

import com.dc.duertest.Constant;
import com.nudt.serialport.utils.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

/**
 * 描述：串口调试工具类
 * 作者：dc on 2016/8/18 14:47
 * 邮箱：597210600@qq.com
 */
public class SerialPortUtil {
    private static final String TAG = SerialPortUtil.class.getSimpleName();

    private static Context context = null;
    private static SerialPortUtil instanll = null;
    /**  类对象定义  **/
    private  SerialPort mfivemicSerialPort = null;  //五麦串口对象
    private  DataReceivedListener dataReceivedListener = null;

    /** 串口读取数据  **/
    protected OutputStream mFiveMicOutputStream;
    private InputStream mFiveMicInputStream;
    private FiveMicReadThread fiveMicReadThread;
    /**
     * 串口解析
     **/
    private  byte fiveByte[] = new byte[1024 * 10];
    private  int temp = 0;  //数组下标
    private  int unDisposeLen = 0; //没有处理完数据的长度

    public static SerialPortUtil getInstanll(){
        if(instanll == null){
            synchronized (SerialPortUtil.class){
                if(instanll == null){
                    instanll = new SerialPortUtil();
                }
            }
        }
        return instanll;
    }


    /**
     * @descriptoin	打开串口数据
     * @author	dc
     * @date 2016/9/5 16:07
     */
    public boolean openSerailPort(int baudrate,String path){
        try {
            Log.e(TAG, "打开串口");
            getSerialPort(baudrate, path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 接收五麦串口数据
     */
    private class FiveMicReadThread extends Thread {
        @Override
        public void run() {
            super.run();

            while(!isInterrupted()) {
                try {
                    if (mFiveMicInputStream != null){
                        micResult();
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 六麦结果
     * @throws IOException
     */
    private void micResult() throws IOException {
        byte[] buffer = new byte[512];
        int size;
        size = mFiveMicInputStream.read(buffer);
        if(null != dataReceivedListener){
            //获取串口传过来的数据：机器人状态信息数据
            if (size > 0 && unDisposeLen >= 0) {
                //获取串口传过来的数据
                System.arraycopy(buffer, 0, fiveByte, unDisposeLen, size);
                unDisposeLen += +size;
                int temp = 0;  //数组下标
                while (temp <= unDisposeLen) {
                    //判断数据长度是否足够：
                    if (unDisposeLen - temp >= Constant.DATA_MIN_LEN) {
                        if(temp > 4 && fiveByte[temp] == 0x0a){ //b[i] == 0x0d &&
                            /**  五麦数据解析   **/
                            byte by[] = new byte[temp];
                            System.arraycopy(fiveByte, 0, by, 0, temp);
                            String info = ByteUtil.byteToString(by, "utf-8").trim().toString();
                            if(!info.equals("")) {
                                Log.e(TAG,"content="+info);
                                if(info.contains("angle:")){
                                    String angle = info.split(":")[1];
                                    dataReceivedListener.onDataReceived(angle);
                                }
                            }
                            byte[] ret;
                            if (unDisposeLen - temp <= 0) {  //表示为一条完整数据，没有没处理完的数据，这里直接清空b数组
                                ret = new byte[unDisposeLen];
                            } else {
                                ret = new byte[unDisposeLen - temp];
                            }
                            //将处理的数据copy到ret数组中
                            System.arraycopy(fiveByte, temp, ret, 0, ret.length);
                            //将未处理的数据ret替换从0开始替换到
                            System.arraycopy(ret, 0, fiveByte, 0, ret.length);
                            unDisposeLen = unDisposeLen - temp;
                        } else if (fiveByte[temp] == 0x00 && fiveByte[temp+1] == 0x00 && fiveByte[temp+2] == 0x00 && fiveByte[temp + 3] == 0x00){
                            unDisposeLen = 0;
                        } else {
                            temp++;
                        }
                    } else {
                        temp++;
                    }
                }
                if (temp == unDisposeLen) {
                    unDisposeLen = 0;
                }
            }
        }
    }


    /**
     * @descriptoin	注册串口接收数据接口
     * @author	dc
     * @param
     * @date 2016/9/5 16:02
     */
    public void setDataReceivedListener(DataReceivedListener listener){
        context = (Context)listener;
        dataReceivedListener = listener;

    }

    /**
     * @descriptoin	打开串口
     * @author	dc
     * @param
     * @date 2016/9/5 16:05
     * @return
     */
    public void getSerialPort(int baudrate,String path) throws SecurityException, IOException, InvalidParameterException {
        Log.e(TAG, "path=" + path + "     baudrate=" + baudrate);
        if(baudrate == Constant.FIVEMIC_SERIALPORT_BAUDRATE && path.equals(Constant.FIVEMIC_SERIALPORT_PATH)) {
            //打开的是五麦
            if (mfivemicSerialPort == null) {
			        /* Check parameters */
                if ((path.length() == 0) || (baudrate == -1)) {
                    throw new InvalidParameterException();
                }
                mfivemicSerialPort = new SerialPort(new File(path), baudrate, 0);
                mFiveMicOutputStream = mfivemicSerialPort.getOutputStream();
                mFiveMicInputStream = mfivemicSerialPort.getInputStream();
		        /* Create a receiving thread */
                fiveMicReadThread = new FiveMicReadThread();
                fiveMicReadThread.start();
            }
        }


    }

    /**
     * @descriptoin	关闭串口
     * @author	dc
     * @date 2016/9/5 16:05
     */
    public void closeSerialPort() {
        if (mfivemicSerialPort != null) {
            Log.e(TAG,"五麦串口关闭");
            fiveMicReadThread.interrupt(); //中断线程
            mfivemicSerialPort.close();
            mfivemicSerialPort = null;
        }
    }

    /**
     * 接收串口数据接口
     */
    public interface DataReceivedListener{
        void onDataReceived(String angle);
    }

}
