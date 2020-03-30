package cn.bytecloud.smartCommunity.util;

import cn.bytecloud.smartCommunity.constant.SystemConstant;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IpUtil {
    /**
     * 获取服务器的ip
     *
     * @return
     */
//    public static String getSerIp() {
//        String clientIp = "";
//        // 根据网卡取本机配置的IP
//        Enumeration<NetworkInterface> allNetInterfaces;  //定义网络接口枚举类
//        try {
//            allNetInterfaces = NetworkInterface.getNetworkInterfaces();  //获得网络接口
//            InetAddress ip = null; //声明一个InetAddress类型ip地址
//            while (allNetInterfaces.hasMoreElements()) //遍历所有的网络接口
//            {
//                NetworkInterface netInterface = allNetInterfaces.nextElement();
//                Enumeration<InetAddress> addresses = netInterface.getInetAddresses(); //同样再定义网络地址枚举类
//                while (addresses.hasMoreElements()) {
//                    ip = addresses.nextElement();
//                    if (ip != null && (ip instanceof Inet4Address)) //InetAddress类包括Inet4Address和Inet6Address
//                    {
//                        if (!ip.getHostAddress().equals("127.0.0.1")) {
//                            clientIp = ip.getHostAddress();
//                        }
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return clientIp;
//    }


    public static String getSerIp() {
//        SystemConstant systemConstant = SpringUtils.getBean(SystemConstant.class);
//        if ("dev".equals(systemConstant.active)) {
//            return "47.107.243.221";
//        }
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("获取ip地址异常");
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        return ip;
    }

}
