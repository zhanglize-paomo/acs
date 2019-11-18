package com.example.asc.asc.trd.common;

import com.blue.security.PriKeySigner;
import com.blue.security.SignatureFactory;
import com.trz.netwk.api.system.InitSystem;

/**
 * ASC项目的文件注册等信息配置类
 *
 * @author zhanglize
 * @create 2019/11/6
 */
public class FileConfigure {

    /**
     * 加载配置文件信息
     *
     * @param cltacc_subno
     * @throws Exception
     */
    public static void getFileConfigure(String cltacc_subno) throws Exception {
        //windows系统的文件信息
        String filepath = FileConfigure.class.getResource("/").getPath() + "cert/hailiying_key.pfx";
        String configPath = FileConfigure.class.getResource("/").getPath() + "cert";
//        String filepath = "/home/www/hailiying/config/hailiying_key.pfx";
//        String configPath = "/home/www/hailiying/config";
        String password = "Hailiying123!@#";
        SignatureFactory.addSigner(cltacc_subno, new PriKeySigner(filepath, password));
        InitSystem.initialize(configPath);
    }


}
