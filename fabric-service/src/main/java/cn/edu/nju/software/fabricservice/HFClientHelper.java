package cn.edu.nju.software.fabricservice;

import cn.edu.nju.software.common.util.ReflectionUtil;
import cn.edu.nju.software.fabricservice.bean.SampleUser;
import cn.edu.nju.software.fabricservice.config.HFConfig;
import cn.edu.nju.software.fabricservice.protomsg.Requests;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.edu.nju.software.common.util.LoggerUtil.*;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author Daniel
 * @since 2018/4/30 23:52
 * 对SDK的进一步封装，通过配置文件简化了配置细节
 */
public class HFClientHelper {
    private static final Logger logger = LoggerFactory.getLogger(HFClientHelper.class);
    private final static HFClient hfClient;
    private static HFCAClient hfCaClient;
    private final static HFConfig hfconfig;

    //channels
    private static final Map<String, Channel> CHANNELS = new HashMap<>();

    private static final Map<String, SampleUser> USERS = new HashMap<>();

    private static Channel DEFAULT_CHANNEL, CURRENT_CHANNEL;
    private static SampleUser DEFAULT_USER, CURRENT_USER;

    static {
        hfconfig = HFConfig.newInstance();

        hfClient = HFClient.createNewInstance();

        try {
            hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        } catch (Exception e) {
            logger.error("get crypto suite error");
            e.printStackTrace();
        }

        config();
    }


    /**
     * 配置
     */
    static void config() {
        //配置CA服务器
        try {
            hfCaClient = HFCAClient.createNewInstance(hfconfig.getCaConfig().getCaUrl(), null);
            hfCaClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        } catch (MalformedURLException e) {
        } catch (Exception e) {
            errorf(logger, "error create hf CA client");
            System.exit(1);
        }
        configUser();

        configChannel();
    }


    /**
     * 配置channel
     */
    static void configChannel() {
        //配置channel
        hfconfig.getChannels().forEach((chconfig) -> {
            String cname = chconfig.getChannelName();
            try {
                Channel channel = hfClient.newChannel(cname);
                //添加peer
                chconfig.getPeers().forEach((pname, purl) -> {
                    try {
                        channel.addPeer(hfClient.newPeer(pname, "grpc://" + purl));
                    } catch (InvalidArgumentException e) {
                        errorf(logger, "error create peer,name=%s,url=%s", pname, purl);
                    }
                });
                Properties ordererProperties = new Properties();

                //example of setting keepAlive to avoid timeouts on inactive http2 connections.
                // Under 5 minutes would require changes to server side to accept faster ping rates.
                ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new
                        Object[]{20L, TimeUnit.MINUTES});
                ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new
                        Object[]{20L, TimeUnit.SECONDS});
                ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveWithoutCalls", new Object[]{true});
                //添加orderer
                chconfig.getOrderers().forEach((oname, ourl) -> {
                    try {

                        channel.addOrderer(hfClient.newOrderer(oname, "grpc://" + ourl, ordererProperties));
                    } catch (InvalidArgumentException e) {
                        errorf(logger, "error create orderer,name=%s,url=%s", oname, ourl);
                    }
                });

                //用反射工具设置初始化，不然会报错，感觉是当前的一个Bug
                ReflectionUtil.setField(channel, "initialized", true);

                //存入map中
                CHANNELS.put(cname, channel);
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
                errorf(logger, "error create channel,name=%s", cname);
            }
        });

        //没有可用的channel
        if (CHANNELS.isEmpty()) {
            errorf(logger, "no channel available");
            System.exit(1);
        }

        DEFAULT_CHANNEL = CHANNELS.get(hfconfig.getDefaultChannel());

        CURRENT_CHANNEL = DEFAULT_CHANNEL;

        if (DEFAULT_CHANNEL == null)
            errorf(logger, "can't find the default channel: %s", hfconfig.getDefaultChannel());

    }

    static void configUser() {
        hfconfig.getUsers().forEach(userConfig -> {
            SampleUser sampleUser = new SampleUser(userConfig.getUsername(), userConfig.getOrg(),
                    null);
            sampleUser.setMspId(userConfig.getMspId());
            sampleUser.setEnrollmentSecret(userConfig.getPassword());

            try {
                sampleUser.setEnrollment(hfCaClient.enroll(userConfig.getUsername(), userConfig.getPassword()));
            } catch (EnrollmentException e) {
                e.printStackTrace();
            } catch (org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException e) {
                e.printStackTrace();
            }

            USERS.put(userConfig.getUsername(), sampleUser);
        });

        if (USERS.isEmpty()) {
            errorf(logger, "no user available");
            System.exit(1);
        }

        DEFAULT_USER = USERS.get(hfconfig.getDefaultUser());

        if (DEFAULT_USER == null) {
            String userName = USERS.keySet().iterator().next();
            DEFAULT_USER = USERS.get(userName);
        }

        CURRENT_USER = DEFAULT_USER;

        try {
            hfClient.setUserContext(DEFAULT_USER);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }

    }


    static void setContext(InvokeContext invokeContext) {
        if (invokeContext != null) {
            if (invokeContext.getChannelName() != null && CHANNELS.containsKey(invokeContext.getChannelName())) {
                CURRENT_CHANNEL = CHANNELS.get(invokeContext.getChannelName());
            }
            if (invokeContext.getUserName() != null && USERS.containsKey(invokeContext.getUserName())) {
                CURRENT_USER = USERS.get(invokeContext.getUserName());
            }
        }
        if (CURRENT_USER != null)
            try {
                hfClient.setUserContext(CURRENT_USER);
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            }
    }

    public static Collection<ProposalResponse> chainCodeInvoke(String
                                                                       chaincodeName, String chaincodeVersion, String func, byte[] data) {

        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion)
                .build();
        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(func);
        transactionProposalRequest.setProposalWaitTime(200000);
        if (data != null)
            transactionProposalRequest.setArgs(data);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8)); //Just some extra junk in transient map
        tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8)); // ditto
        tm2.put("result", ":)".getBytes(UTF_8));  // This should be returned see chaincode why.

        try {
            transactionProposalRequest.setTransientMap(tm2);
            Collection<ProposalResponse> transactionPropResp = CURRENT_CHANNEL.sendTransactionProposal
                    (transactionProposalRequest);
            return transactionPropResp;
        } catch (ProposalException | InvalidArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CompletableFuture<BlockEvent.TransactionEvent> sendTransactions(Collection<ProposalResponse> responses) {
        return CURRENT_CHANNEL.sendTransaction(responses);
    }


    public static Collection<ProposalResponse> chainCodeQuery(String chaincodeName, String chaincodeVersion, String func, byte[] data) {

        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion)
                .build();

        QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();
        queryByChaincodeRequest.setFcn(func);
        if (data != null)
            queryByChaincodeRequest.setArgs(data);
        queryByChaincodeRequest.setChaincodeID(chaincodeID);

        Collection<ProposalResponse> queryProposals;
        try {
            queryProposals = CURRENT_CHANNEL.queryByChaincode(queryByChaincodeRequest);
        } catch (Exception e) {
            throw new CompletionException(e);
        }
        return queryProposals;
    }


    public static void main(String[] args) throws Exception {

        Collection<ProposalResponse> proposalResponses;

        Requests.SimpleRequest simpleRequest = Requests.SimpleRequest.newBuilder().setName
                ("daniel").setDes("des2").build();
        Collection<ProposalResponse> responses = chainCodeInvoke("myCC", "1.0", "set",
                simpleRequest.toByteArray());

        System.out.println(responses.iterator().next().getTransactionID());

        CompletableFuture<BlockEvent.TransactionEvent> transactionEventCompletableFuture =
                sendTransactions(responses);


        proposalResponses = chainCodeQuery("myCC", "1.0", "get",
                "daniel".getBytes());

        for (ProposalResponse proposalResponse : proposalResponses) {
            System.out.println(proposalResponse.getMessage());
            byte[] result = proposalResponse.getChaincodeActionResponsePayload();
            Requests.SimpleRequest simpleRequest1 = Requests.SimpleRequest.parseFrom(result);
            System.out.println(simpleRequest1.toString());
        }

        proposalResponses = chainCodeQuery("myCC2", "1.0", "getHeader", "ggg".getBytes());

        for (ProposalResponse proposalResponse : proposalResponses) {
            System.out.println(proposalResponse.getMessage());
            byte[] result = proposalResponse.getChaincodeActionResponsePayload();
            System.out.println(String.valueOf(result));
        }

//
//
        BlockchainInfo channelInfo = DEFAULT_CHANNEL.queryBlockchainInfo();
        System.out.println("Channel info for : " + DEFAULT_CHANNEL.getName());
        System.out.println("Channel height: " + channelInfo.getHeight());
        String chainCurrentHash = Hex.encodeHexString(channelInfo.getCurrentBlockHash());
        String chainPreviousHash = Hex.encodeHexString(channelInfo.getPreviousBlockHash());
        System.out.println("Chain current block hash: " + chainCurrentHash);
        System.out.println("Chainl previous block hash: " + chainPreviousHash);
    }

}


@AllArgsConstructor
@NoArgsConstructor
class InvokeContext {
    @Getter
    @Setter
    String userName;
    @Getter
    @Setter
    String channelName;
}