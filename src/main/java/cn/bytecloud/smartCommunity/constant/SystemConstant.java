package cn.bytecloud.smartCommunity.constant;

import cn.bytecloud.smartCommunity.util.UUIDUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemConstant {

    public static final String ACTIVE = "spring.profiles.active";
    public static final String DATABASE = "spring.data.mongodb.database";
    public static final String USERNAME = "spring.data.mongodb.username";
    public static final String PASSWORD = "spring.data.mongodb.password";
    public static final String AUTHENTICATION_DATABASE = "spring.data.mongodb.authentication-database";
    public static final String MONGODB_HOST = "spring.data.mongodb.host";
    public static final String MONGODB_PORT = "spring.data.mongodb.port";
    public static final String LOGIN_SPLIT = "b2fc0a45-b881-468c-b804-cfb133a7afd9";

    public static void main(String[] args) {
        for (int i = 0 ;i<8;i++) {
            System.out.println(UUIDUtil.getUUID());
        }
    }
    //初始化大类名称
    public static final String INIT_BIG_CATEGORY_NAME = "街面秩序";
    //初始化大类名称
    public static final String INIT_SMALL_CATEGORY_NAME = "无照经营游商";
    public static final String MODEL_NAME = "物体识别";

    public static final String[] MODEL_IDS = {
            "eb372e7ba2a94290b46a9ba2bdc4d7cd",
            "64ec61df93b946cb905ec6c30f9d8c1c",
            "24859c61dc844982a60aecf927197ebc",
            "c677981973cb49a4bc63c6f1d2f1b355",
            "b31349b3ceb448d689de4ab48a607a3f",
            "2c1f660681a54ee4b63bf58970ff2010",
            "42adf2c9b98f4892b1d38d688f58440d",
            "9191f6f0bf1b4fa89d7413368c14b7e9",
            "18eb14251ff04f6eb2e72e75d838a248",
            "4ba324793f3744ab86dc5e466da16a64",
            "3bc0c8a6a4eb4d399c969168311d9d8f",
            "9b8e9d376adf4edbbd05835adf05583e",
            "bf4bec5cee9e4607bbb29d8d4c6a6f7b"
    };

    //初始化大类名称
    public static final String[] FENCE_NAME = {
            "街面秩序-机动车乱停放-卡车,d904b2e2ef5e4e2cb1c9d630db66322d-轿车,734efed6a6ee4cae94bfec05f5281547",
            "街面秩序-非机动车乱停放-三轮车,3c6dc7833b5440dab3949625ebd58046-电瓶车,6eb6758e83384a8ca9ec63472517f552-自行车,2a6e3b89de6c40b0aa7c52fb3a6a24b7",
            "街面秩序-店外经营-餐桌,e6df9f59d8f649a4a0b9f98e4db0058a-椅子,b7a504a7ff9f40c9a33582b6c78d8e6d",
            "电子围栏-区域禁行-人,e6568e765a6b4ecc8b9db8c733e9d1e6"
    };

    //工作人员角色名字
    public static final String WORK_ROLE_NAME = "工作人员";


    /**
     * 初始化用户名和密码
     */
    public static final String DEFUALT_PASSWORD = "123456";

    public static final String INIT_ADMIN_USERNAME = "admin";
    public static final String INIT_ADMIN_PASSWORD = "admin";

    public static final String INIT_ROOT_USERNAME = "root";
    public static final String INIT_ROOT_PASSWORD = "root";

    public static final String INIT_SYSTEM_USERNAME = "system";
    public static final String INIT_SYSTEM_NAME = "摄像机抓拍";
    public static final String INIT_SYSTEM_PASSWORD = "system";
    public static final String INIT_SYSTEM_ID = "system";

    /**
     * 初始化组织机构根节点
     */
    public static final String INIT_UNIT_NAME = "武侯区机投街道办事处";

    @Value("${server.port}")
    public Integer port;

//    @Value("${spring.profiles.active}")
//    public String active;


    //管理员
    public static final String ROOT = "root";

    @Value("${face.ip}")
    public String faceIp;

    @Value("${ai.inspectionTime}")
    public long inspectionTime;
    @Value("${address.timeout}")
    public long addressTimeOut;

    @Value("${face.ask}")
    public String faceAsk;

    @Value("${face.port}")
    public String facePort;

    @Value("${face.updface}")
    public String updFace;

    @Value("${face.delface}")
    public String delFace;

    @Value("${face.fenceopt}")
    public String fenceOpt;

    @Value("${face.upddevice}")
    public String updDevice;

    @Value("${face.deldevice}")
    public String delDevice;

    @Value("${face.updfence}")
    public String updFence;

    @Value("${face.delfence}")
    public String delFence;

    @Value("${face.upddevicestatus}")
    public String updDeviceStatus;

    @Value("${face.play}")
    public String play;

    @Value("${face.stop}")
    public String stop;

    @Value("${outPut}")
    public String outPut;

    @Value("${outPort}")
    public String outPort;

    @Value("${rtmp}")
    public String rtmpPort;

    @Value("${user}")
    public String user;

    @Value("${password}")
    public String password;

    @Value("${outAddress}")
    public String outAddress;

    @Value("${inNet}")
    public String inNet;

    @Value("${ffmpeg}")
    public String ffmpeg;

    @Value("${split}")
    public String split;

    @Value("${video.ip}")
    public String videoIp;

    @Value("${video.port}")
    public String videoPort;

    @Value("${video.up}")
    public String videoUp;

    @Value("${video.down}")
    public String videoDown;

    @Value("${video.left}")
    public String videoLeft;

    @Value("${video.right}")
    public String videoRight;

    @Value("${video.login}")
    public String videoLogin;

    @Value("${video.logout}")
    public String videoLogout;

}
