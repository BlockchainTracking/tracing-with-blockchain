package cn.edu.nju.software;

import java.io.File;

import cn.edu.nju.software.bean.Orderers;
import cn.edu.nju.software.bean.Peers;
import cn.edu.nju.software.bean.ChainCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricConfig {

    private static Logger log = LoggerFactory.getLogger(FabricConfig.class);

    /**
     * 节点服务器对象
     */
    private Peers peers;
    /**
     * 排序服务器对象
     */
    private Orderers orderers;
    /**
     * 智能合约对象
     */
    private ChainCode chaincode;
    /**
     * channel-artifacts所在路径：默认channel-artifacts所在路径/xxx/WEB-INF/classes/fabric/channel-artifacts/
     */
    private String channelArtifactsPath;
    /**
     * crypto-config所在路径：默认crypto-config所在路径/xxx/WEB-INF/classes/fabric/crypto-config/
     */
    private String cryptoConfigPath;
    private boolean registerEvent = false;

    public FabricConfig() {
        // 默认channel-artifacts所在路径 /xxx/WEB-INF/classes/fabric/channel-artifacts/
        channelArtifactsPath = getChannlePath() + "/channel-artifacts/";
        // 默认crypto-config所在路径 /xxx/WEB-INF/classes/fabric/crypto-config/
        cryptoConfigPath = getChannlePath() + "/crypto-config/";
    }

    /**
     * 默认fabric配置路径
     *
     * @return D:/installSoft/apache-tomcat-9.0.0.M21-02/webapps/xxx/WEB-INF/classes/fabric/channel-artifacts/
     */
    private String getChannlePath() {
        String directorys = ChaincodeManager.class.getClassLoader().getResource("fabric").getFile();
        log.debug("directorys = " + directorys);
        File directory = new File(directorys);
        log.debug("directory = " + directory.getPath());

        return directory.getPath();
        // return "src/main/resources/fabric/channel-artifacts/";
    }

    public Peers getPeers() {
        return peers;
    }

    public void setPeers(Peers peers) {
        this.peers = peers;
    }

    public Orderers getOrderers() {
        return orderers;
    }

    public void setOrderers(Orderers orderers) {
        this.orderers = orderers;
    }

    public ChainCode getChaincode() {
        return chaincode;
    }

    public void setChaincode(ChainCode chaincode) {
        this.chaincode = chaincode;
    }

    public String getChannelArtifactsPath() {
        return channelArtifactsPath;
    }

    public void setChannelArtifactsPath(String channelArtifactsPath) {
        this.channelArtifactsPath = channelArtifactsPath;
    }

    public String getCryptoConfigPath() {
        return cryptoConfigPath;
    }

    public void setCryptoConfigPath(String cryptoConfigPath) {
        this.cryptoConfigPath = cryptoConfigPath;
    }

    public boolean isRegisterEvent() {
        return registerEvent;
    }

    public void setRegisterEvent(boolean registerEvent) {
        this.registerEvent = registerEvent;
    }

}
