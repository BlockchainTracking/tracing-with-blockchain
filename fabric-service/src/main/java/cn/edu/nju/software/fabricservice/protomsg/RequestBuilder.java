package cn.edu.nju.software.fabricservice.protomsg;

/**
 * @author Daniel
 * @since 2018/5/2 0:10
 */
public class RequestBuilder {
    public static Requests.ItemAddRequest createItemAddRequest(String addressName, Long longitude,
                                                               Long latitude, String itemName,
                                                               String batchNum, String class_,
                                                               String node, String itemId) {
        Persistence.Address address = Persistence.Address.newBuilder().setName(addressName)
                .setLongitude(longitude).setLatitude(10.1).build();
        Persistence.ItemInfo itemInfo = Persistence.ItemInfo.newBuilder().setName("item1")
                .setBatchNumber("2018111").setClass_("cmp").setNote("note").build();
        Requests.ItemAddRequest itemAddRequest = Requests.ItemAddRequest.newBuilder().setAddress
                (address).setItemInfo(itemInfo).setItemId("1234567890123456789012345678901212345678901234567890123456789012")
                .build();
        return itemAddRequest;
    }
}
