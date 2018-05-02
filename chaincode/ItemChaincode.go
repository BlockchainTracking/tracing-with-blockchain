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
	handlers map[string]func(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error)
}

func (t *ItemAssetChaincode) Init(stub shim.ChaincodeStubInterface) peer.Response {
	// Get the args from the transaction proposal
	//千万要注意初始化！！！
	t.handlers = make(map[string]func(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error))
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
		if data, err := fnHandler(stub, args[1]); err != nil {
			return shim.Error(err.Error())
		} else {
			return shim.Success(data)
		}
	} else { // assume 'get' even if fn is nil
		return shim.Error("can't find function " + fn)
	}
}

/**
查询商品
 */
func getItem(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error) {
	request := &ItemGetRequest{}
	response := &ItemGetResponse{}
	response.ItemAssets = make([]*ItemAsset, 0)

	err := proto.Unmarshal(args, request)
	if err != nil {
		return createError("error marshal request data")
	}

	key := request.ItemId

	//需要提取历史数据
	if request.HistData {
		iterator, err := stub.GetHistoryForKey(key)
		if err != nil {
			return createError("failed to get all history")
		}
		for iterator.HasNext() {
			keyModify, _ := iterator.Next()
			iAsset := &ItemAsset{}
			err = unmarshalProtobuf(keyModify.Value, iAsset)
			if err != nil {
				return createError("unmarshal error")
			}
			response.ItemAssets = append(response.ItemAssets, iAsset)
		}
	} else { //不需要提取历史数据
		value, err := stub.GetState(key)
		if err != nil || value == nil {
			return createError("error happens,asset not found %s", key)
		}
		iAsset := &ItemAsset{}
		err = unmarshalProtobuf(value, iAsset)
		if err != nil {
			return createError("unmarshal error")
		}
		response.ItemAssets = append(response.ItemAssets, iAsset)
	}

	data, _ := proto.Marshal(response)
	return data, nil
}

/**
添加商品
 */
func addItem(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error) {
	creator, err := stub.GetCreator()
	if err != nil {
		return createError("error get tx creator")
	}
	request := &ItemAddRequest{}
	err = proto.Unmarshal(args, request)
	if err != nil {
		return createError("error marshal request data")
	}

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
	item.Timestamp = time.Now().UnixNano() / 1000000 //获取的是纳秒，除以1000000变为毫秒

	itemId := request.ItemId
	if !checkItemIdformat(itemId) {
		return createError("itemId format error,expected length 32")
	}

	value, _ := stub.GetState(request.ItemId)
	if value != nil {
		return createError("itemId already exists")
	}

	if data, err := marshalProtobuf(item); err != nil {
		return createError("protobuf marshal error")
	} else {
		err = stub.PutState(request.ItemId, data)
		if err != nil {
			return createError("put state error")
		}
		return nil, nil
	}
}

func changeItem(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error) {
	creator, err := stub.GetCreator()
	if err != nil {
		return createError("error get tx creator")
	}
	request := &ItemChangeRequest{}
	err = proto.Unmarshal(args, request)
	if err != nil {
		return createError("error marshal request data")
	}

	key := request.ItemId

	value, err := stub.GetState(key)
	if err != nil || value == nil {
		return createError("error happens,asset not found %s", key)
	}

	iAsset := &ItemAsset{}
	err = unmarshalProtobuf(value, iAsset)

	//设置环境状态
	envStatus := request.EnvStatus

	//设置操作状态
	opStatus := &OpsStatus{}
	opStatus.Operator = creator
	opStatus.OpType = request.OpType
	opStatus.Operator = creator
	opStatus.Lastoperator = iAsset.OpsStatus.Operator

	//改变itemAsset状态
	iAsset.EvnStatus = envStatus
	iAsset.OpsStatus = opStatus
	iAsset.ItemStatus = request.ItemStatus
	iAsset.Timestamp = time.Now().UnixNano() / 1000000

	if !checkItemIdformat(key) {
		return createError("itemId format error,expected length 64")
	}

	if data, err := marshalProtobuf(iAsset); err != nil {
		return createError("protobuf marshal error")
	} else {
		err = stub.PutState(request.ItemId, data)
		if err != nil {
			return createError("put state error")
		}
		return nil, nil
	}
}

/**
这里是为了统一存入数据库的编码方式，可能直接转为字节数组，可能变为json数据
 */
func marshalProtobuf(pb proto.Message) ([]byte, error) {
	if data, err := proto.Marshal(pb); err != nil {
		return nil, fmt.Errorf("protobuf marshal error")
	} else {
		return data, nil
	}
}

/**
这里是为了统一存入数据库的编码方式，可能直接转为字节数组，可能变为json数据
 */
func unmarshalProtobuf(data []byte, pb proto.Message) error {
	err := proto.Unmarshal(data, pb)
	if err != nil {
		return fmt.Errorf("unmarshal protubuf error")
	}
	return nil
}

// main function starts up the chaincode in the container during instantiate
func main() {
	if err := shim.Start(new(ItemAssetChaincode)); err != nil {
		fmt.Printf("Error starting ChaincodeDemo chaincode: %s", err)
	}
}
