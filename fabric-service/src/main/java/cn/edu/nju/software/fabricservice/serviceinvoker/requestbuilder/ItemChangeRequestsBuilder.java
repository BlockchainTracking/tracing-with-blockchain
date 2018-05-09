package cn.edu.nju.software.fabricservice.serviceinvoker.requestbuilder;

import cn.edu.nju.software.common.pojo.OpType;
import cn.edu.nju.software.fabricservice.protomsg.Persistence;
import cn.edu.nju.software.fabricservice.protomsg.Requests;

/**
 * @author Daniel
 * @since 2018/5/2 20:01
 */
public class ItemChangeRequestsBuilder {
    String addressName;
    double longitude;
    double latitude;
    String itemId;
    String sampleStr;
    Long sampleLong;
    String logs;
    OpType opType;

    public ItemChangeRequestsBuilder setAddressName(String addressName) {
        this.addressName = addressName;
        return this;
    }

    public ItemChangeRequestsBuilder setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public ItemChangeRequestsBuilder setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public ItemChangeRequestsBuilder setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public ItemChangeRequestsBuilder setSampleStr(String sampleStr) {
        this.sampleStr = sampleStr;
        return this;
    }

    public ItemChangeRequestsBuilder setSampleLong(Long sampleLong) {
        this.sampleLong = sampleLong;
        return this;
    }

    public ItemChangeRequestsBuilder setLogs(String logs) {
        this.logs = logs;
        return this;
    }

    public ItemChangeRequestsBuilder setOpType(OpType opType) {
        this.opType = opType;
        return this;
    }

    public Requests.ItemChangeRequest build() {
        Persistence.Address address = Persistence.Address.newBuilder().setName(addressName)
                .setLongitude(longitude).setLatitude(latitude).build();
        Persistence.EnvStatus envStatus = Persistence.EnvStatus.newBuilder().setAddress(address)
                .build();
        Persistence.ItemStatus itemStatus = Persistence.ItemStatus.newBuilder().setLogs(logs)
                .setSampleStrStatus(sampleStr).setSampleLongStatus(sampleLong).build();
        Requests.ItemChangeRequest itemChangeRequest = Requests.ItemChangeRequest.newBuilder()
                .setEnvStatus(envStatus).setItemStatus(itemStatus).setOpType(Persistence.OPType
                        .valueOf(opType.toString())).setItemId(itemId)
                .build();
        return itemChangeRequest;
    }

}
