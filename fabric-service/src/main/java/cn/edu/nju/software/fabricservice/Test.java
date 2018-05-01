package cn.edu.nju.software.fabricservice;

import cn.edu.nju.software.fabricservice.protomsg.Persistence;
import cn.edu.nju.software.fabricservice.protomsg.Requests;
import cn.edu.nju.software.fabricservice.protomsg.ResponseOuterClass;
import org.hyperledger.fabric.sdk.ProposalResponse;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static cn.edu.nju.software.fabricservice.HFClientHelper.*;

/**
 * @author Daniel
 * @since 2018/5/2 0:07
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Collection<ProposalResponse> proposalResponses;
        Collection<ProposalResponse> responses;

        Persistence.Address address = Persistence.Address.newBuilder().setName("add1")
                .setLongitude(10.0).setLatitude(10.1).build();
        Persistence.ItemInfo itemInfo = Persistence.ItemInfo.newBuilder().setName("item1")
                .setBatchNumber("2018111").setClass_("cmp").setNote("note").build();
        Requests.ItemAddRequest itemAddRequest = Requests.ItemAddRequest.newBuilder().setAddress
                (address).setItemInfo(itemInfo).setItemId("1234567890123456789012345678901212345678901234567890123456789012")
                .build();
        responses = chainCodeInvoke("myCC3", "1.0", "addItem",
                itemAddRequest.toByteArray());
        for (ProposalResponse proposalResponse : responses) {
            System.out.println(proposalResponse.getMessage());
            byte[] result = proposalResponse.getChaincodeActionResponsePayload();
            ResponseOuterClass.Response response = ResponseOuterClass.Response.parseFrom(result);
            System.out.println(response.getMessage());
        }

        sendTransactions(responses);

        TimeUnit.SECONDS.sleep(5);

        proposalResponses = chainCodeQuery("myCC3", "1.0", "getItem",
                "1234567890123456789012345678901212345678901234567890123456789012".getBytes());

        for (ProposalResponse proposalResponse : proposalResponses) {
            System.out.println(proposalResponse.getMessage());
            byte[] result = proposalResponse.getChaincodeActionResponsePayload();
            ResponseOuterClass.Response response = ResponseOuterClass.Response.parseFrom(result);
            System.out.println(response.getMessage());
            Persistence.ItemAsset itemAsset = Persistence.ItemAsset.parseFrom(response.getData());
            System.out.println(itemAsset.toString());
        }

    }
}
