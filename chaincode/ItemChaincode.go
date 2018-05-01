package main

import (
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
	"github.com/golang/protobuf/proto"
	"time"
)

// ChaincodeDemo implements a simple chaincode to manage an asset
type ItemAssetChaincode struct {
	//handlers用来存储调用的函数名称和器对应的函数
	handlers map[string]func(stub shim.ChaincodeStubInterface, args []byte) (Response)
}

func (t *ItemAssetChaincode) Init(stub shim.ChaincodeStubInterface) peer.Response {
	// Get the args from the transaction proposal
	//千万要注意初始化！！！
	t.handlers = make(map[string]func(stub shim.ChaincodeStubInterface, args []byte) (Response))
	t.handlers["addItem"] = addItem
	t.handlers["getItem"] = getItem
	t.handlers["changeItem"] = changeItem
	return shim.Success(nil)
}

func (t *ItemAssetChaincode) Invoke(stub shim.ChaincodeStubInterface) peer.Response {
	// Extract the function and args from the transaction proposal
	args := stub.GetArgs()
	fn := string(args[0])

	if fnHandler, ok := t.handlers[fn]; ok {
		result := fnHandler(stub, args[1])
		if data, err := proto.Marshal(&result); err != nil {
			return shim.Error("proto marshal response error")
		} else {
			return shim.Success(data)
		}
	} else { // assume 'get' even if fn is nil
		return shim.Error("can't find function " + fn)
	}
}

func getItemAllHist() {

}

func getItem(stub shim.ChaincodeStubInterface, args []byte) (Response) {
	key := string(args)

	value, err := stub.GetState(key)
	if err != nil {
		return createRespWithoutData(-1, "Failed to get asset: %s with error: %s", key)
	}
	if value == nil {
		return createRespWithoutData(-1, "Asset not found: %s", key);
	}
	return createSuccessResp(value)
}

func addItem(stub shim.ChaincodeStubInterface, args []byte) (Response) {
	creator, err := stub.GetCreator()
	if err != nil {
		return createRespWithoutData(-1, "error get tx creator")
	}
	request := &ItemAddRequest{}
	proto.Unmarshal(args, request)

	//设置环境状态
	envStatus := &EnvStatus{}
	envStatus.Address = request.Address

	//设置操作状态
	opStatus := &OpsStatus{}
	opStatus.Operator = creator
	opStatus.OpType = OPType_CREATED

	//
	item := &ItemAsset{}

	item.ItemInfo = request.ItemInfo
	item.EvnStatus = envStatus
	item.OpsStatus = opStatus
	item.Timestamp = time.Now().UnixNano()

	itemId := request.ItemId
	if !checkItemIdformat(itemId) {
		return createRespWithoutData(-1, "itemId format error,expected length 64")
	}
	value, err := stub.GetState(request.ItemId)
	if value != nil {
		return createRespWithoutData(-1, "itemId already exists")
	}
	if data, err := proto.Marshal(item); err != nil {
		return createRespWithoutData(-1, "protobuf marshal error")
	} else {
		err = stub.PutState(request.ItemId, data)
		if err != nil {
			return createRespWithoutData(-1, "put state error")
		}
		return createRespWithoutData(0, "success add item with id=%s", request.ItemId)
	}
}

func changeItem(stub shim.ChaincodeStubInterface, args []byte) (Response) {
	return createSuccessResp(nil)
}

// main function starts up the chaincode in the container during instantiate
func main() {
	if err := shim.Start(new(ItemAssetChaincode)); err != nil {
		fmt.Printf("Error starting ChaincodeDemo chaincode: %s", err)
	}
}
