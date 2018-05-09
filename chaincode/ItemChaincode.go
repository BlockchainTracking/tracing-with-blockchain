package main

import (
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
	"time"
	"github.com/hyperledger/fabric/core/chaincode/lib/cid"
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
	mspId, err := cid.GetMSPID(stub)
	if err != nil {
		return createError("error get mspId")
	}

	if ! check(ITEM_GET, mspId, nil) {
		return createError("no right to invoke getItem with mspId:%s", mspId)
	}

	request := &ItemGetRequest{}
	response := &ItemGetResponse{}
	response.ItemAssets = make([]*ItemAsset, 0)

	if err := decode(args, request); err != nil {
		return createError("failed to decode request")
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
			return createError("retrieve error with mgs:%s", err.Error())
		}
		iAsset := &ItemAsset{}
		err = unmarshalProtobuf(value, iAsset)
		if err != nil {
			return createError("unmarshal error")
		}
		response.ItemAssets = append(response.ItemAssets, iAsset)
	}

	data, _ := encode(response)
	return data, nil
}

/**
添加商品
 */
func addItem(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error) {
	mspId, err := cid.GetMSPID(stub)
	if err != nil {
		return createError("error get mspId")
	}

	request := &ItemAddRequest{}

	if err = decode(args, request); err != nil {
		return createError("error marshal request data")
	}

	if ! check(ITEM_ADD, mspId, nil) {
		return createError("no right to invoke addItem with mspId:%s", mspId)
	}

	//设置环境状态
	envStatus := &EnvStatus{}
	envStatus.Address = request.Address

	//设置操作状态
	opStatus := &OpsStatus{}
	opStatus.CurrentOrg = mspId
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

	if err := create(itemId, item, stub); err != nil {
		return createError("item create error with mgs:%s", err.Error())
	}
	return nil, nil
}

func changeItem(stub shim.ChaincodeStubInterface, args []byte) ([]byte, error) {
	mspId, err := cid.GetMSPID(stub)
	if err != nil {
		return createError("error get mspId")
	}

	request := &ItemChangeRequest{}

	if err = decode(args, request); err != nil {
		return createError("error marshal request data")
	}

	key := request.ItemId

	iAsset, err := retrieve(key, stub)
	if err != nil || iAsset == nil {
		return createError("error happens,asset not found %s", key)
	}

	if ! check(ITEM_CHANGE, mspId, iAsset.OpsStatus) {
		return createError("no right to invoke changeItem with mspId:%s", mspId)
	}

	//设置环境状态
	envStatus := request.EnvStatus

	//设置操作状态
	opStatus := &OpsStatus{}
	opStatus.OpType = request.OpType
	opStatus.CurrentOrg = request.NextOrg
	opStatus.LastOrg = iAsset.OpsStatus.CurrentOrg

	//改变itemAsset状态
	iAsset.EvnStatus = envStatus
	iAsset.OpsStatus = opStatus
	iAsset.ItemStatus = request.ItemStatus
	iAsset.Timestamp = time.Now().UnixNano() / 1000000

	if !checkItemIdformat(key) {
		return createError("itemId format error,expected length 64")
	}

	if err := update(request.ItemId, iAsset, stub); err != nil {
		return createError("item update error with msg:%s", err.Error())
	}

	return nil, nil
}

// main function starts up the chaincode in the container during instantiate
func main() {
	if err := shim.Start(new(ItemAssetChaincode)); err != nil {
		fmt.Printf("Error starting ChaincodeDemo chaincode: %s", err)
	}
}
