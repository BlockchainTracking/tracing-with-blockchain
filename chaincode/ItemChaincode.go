package main

import (
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
	"github.com/golang/protobuf/proto"
	"time"
)

// ItemAssetChaincode implements a simple chaincode to manage an asset
type ItemAssetChaincode struct {
	//handlers用来存储调用的函数名称和器对应的函数
	handlers map[string]func(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error)
}

func (t *ItemAssetChaincode) Init(stub shim.ChaincodeStubInterface) peer.Response {
	// Get the args from the transaction proposal
	//千万要注意初始化！！！
	t.handlers = make(map[string]func(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error))
	t.handlers["get"] = get
	t.handlers["set"] = set
	t.handlers["getHeader"] = getHeader
	return shim.Success(nil)
}

func (t *ItemAssetChaincode) Invoke(stub shim.ChaincodeStubInterface) peer.Response {
	// Extract the function and args from the transaction proposal
	args := stub.GetArgs()
	fn := string(args[0])

	if fnHandler, ok := t.handlers[fn]; ok {
		if result, err := fnHandler(stub, args[1]); err != nil {
			return shim.Error(err.Error())
		} else {
			return shim.Success([]byte(result))
		}
	} else { // assume 'get' even if fn is nil
		return shim.Error("can't find function " + fn)
	}

}

func addItem(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error) {
	creator, err := stub.GetCreator()
	if err != nil {
		return nil, fmt.Errorf("error get tx creator")
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

	return stub.GetCreator()
}

func getHeader(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error) {
	return stub.GetCreator()
}

func set(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error) {
	i := &SimpleRequest{}
	proto.Unmarshal(args, i)
	i.TimeStamp = time.Now().UnixNano()
	if data, err := proto.Marshal(i); err != nil {
		return nil, fmt.Errorf("failed to Marshal object to json")
	} else {
		// We store the key and the value on the ledger
		err := stub.PutState(i.Name, data)
		if err != nil {
			return nil, fmt.Errorf("Failed to create asset: %s,with err msg", i.String(), err.Error())
		}
		return data, nil
	}
}

func get(stub shim.ChaincodeStubInterface, args [] byte) ([]byte, error) {
	value, err := stub.GetState(string(args))
	if err != nil {
		return nil, fmt.Errorf("Failed to get asset: %s with error: %s", string(args), err)
	}
	if value == nil {
		return nil, fmt.Errorf("Asset not found: %s", string(args))
	}
	return value, nil
}

// main function starts up the chaincode in the container during instantiate
func main() {
	if err := shim.Start(new(ItemAssetChaincode)); err != nil {
		fmt.Printf("Error starting ItemAssetChaincode chaincode: %s", err)
	}
}
