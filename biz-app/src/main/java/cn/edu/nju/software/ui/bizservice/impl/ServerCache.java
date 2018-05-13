package cn.edu.nju.software.ui.bizservice.impl;

import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.fabricservice.bean.SampleStore;
import cn.edu.nju.software.fabricservice.bean.SampleUser;
import cn.edu.nju.software.ui.dao.UserDao;
import cn.edu.nju.software.ui.entity.UserEntity;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.security.PrivateKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daniel
 * @since 2018/5/12 23:58
 */
@Component
public class ServerCache {
    @Autowired
    UserDao userDao;


    public Map<String, SampleUser> users = new ConcurrentHashMap<>();

    public void addUser(String username, SampleUser sampleUser) {
        users.put(username, sampleUser);
    }

    public SampleUser getUser(String username) {
        return users.get(username);
    }
}
