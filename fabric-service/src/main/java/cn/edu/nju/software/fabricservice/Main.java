package cn.edu.nju.software.fabricservice;

import cn.edu.nju.software.fabricservice.bean.SampleStore;
import cn.edu.nju.software.fabricservice.bean.SampleUser;
import cn.edu.nju.software.common.util.ReflectionUtil;
import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.CompletionException;

/**
 * 这是测试sdk的基本功能
 */
public class Main {
    public static void main(String[] args) throws Exception {

        HFClient hfClient = HFClient.createNewInstance();
        hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        File sampleStoreFile = new File("d:/HFCSampletest" +
                ".properties");
        if (sampleStoreFile.exists()) { //For testing start fresh
            sampleStoreFile.delete();
        }

        final SampleStore sampleStore = new SampleStore(sampleStoreFile);


        HFCAClient caClient = HFCAClient.createNewInstance("http://192.168.136.131:7054", null);
        caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

        SampleUser admin = new SampleUser("admin", "org1", sampleStore);
        admin.setMspId("Org1MSP111");

        admin.setEnrollment(caClient.enroll(admin.getName(), "adminpw"));

        SampleUser user1 = new SampleUser("new-user", "org1.department", sampleStore);
//        RegistrationRequest rr = new RegistrationRequest(user1.getName(), "org1.department1");
//        String enrollmentSecret = caClient.register(rr, admin);
//        System.out.println("register successful, secret:" + enrollmentSecret);
//        user1.setEnrollmentSecret(enrollmentSecret);
        user1.setEnrollmentSecret("jbUnQBMaXAsY");

        user1.setEnrollment(caClient.enroll(user1.getName(), user1.getEnrollmentSecret()));

        user1.setMspId("Org1MSP");

        System.out.println(user1.getEnrollment().getKey().toString());
        System.out.println(user1.getEnrollment().getCert());


        hfClient.setUserContext(user1);
        Channel channel = hfClient.newChannel("mychannel");
        Peer peer = hfClient.newPeer("peer1", "grpc://192.168.136.131:7051");
        channel.addPeer(peer);

        ReflectionUtil.setField(channel, "initialized", true);

        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName("fabcar").build();
        QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();
        queryByChaincodeRequest.setFcn("queryAllCars");
        queryByChaincodeRequest.setArgs("");
        queryByChaincodeRequest.setChaincodeID(chaincodeID);


        long a = System.currentTimeMillis();
        Collection<ProposalResponse> queryProposals;

        try {
            queryProposals = channel.queryByChaincode(queryByChaincodeRequest);
        } catch (Exception e) {
            throw new CompletionException(e);
        }

        for (ProposalResponse proposalResponse : queryProposals) {
            System.out.println(new String(proposalResponse.getChaincodeActionResponsePayload(),
                    Charsets.UTF_8));
        }

        System.out.println(System.currentTimeMillis() - a);


        BlockchainInfo channelInfo = channel.queryBlockchainInfo();
        System.out.println("Channel info for : " + channel.getName());
        System.out.println("Channel height: " + channelInfo.getHeight());
        String chainCurrentHash = Hex.encodeHexString(channelInfo.getCurrentBlockHash());
        String chainPreviousHash = Hex.encodeHexString(channelInfo.getPreviousBlockHash());
        System.out.println("Chain current block hash: " + chainCurrentHash);
        System.out.println("Chainl previous block hash: " + chainPreviousHash);

    }

}
