// Code generated by protoc-gen-go. DO NOT EDIT.
// source: Requests.proto

/*
Package main is a generated protocol buffer package.

It is generated from these files:
	Requests.proto
	Persistence.proto
	Response.proto

It has these top-level messages:
	SimpleRequest
	ItemAddRequest
	ItemGetRequest
	ItemChangeRequest
	Address
	EnvStatus
	OpsStatus
	ItemInfo
	ItemStatus
	ItemAsset
	SimpleResponse
	Response
	ItemGetResponse
*/
package main

import proto "github.com/golang/protobuf/proto"
import fmt "fmt"
import math "math"

// Reference imports to suppress errors if they are not otherwise used.
var _ = proto.Marshal
var _ = fmt.Errorf
var _ = math.Inf

// This is a compile-time assertion to ensure that this generated file
// is compatible with the proto package it is being compiled against.
// A compilation error at this line likely means your copy of the
// proto package needs to be updated.
const _ = proto.ProtoPackageIsVersion2 // please upgrade the proto package

type SimpleRequest struct {
	TimeStamp int64  `protobuf:"varint,1,opt,name=timeStamp" json:"timeStamp,omitempty"`
	Name      string `protobuf:"bytes,2,opt,name=name" json:"name,omitempty"`
	Des       string `protobuf:"bytes,3,opt,name=des" json:"des,omitempty"`
}

func (m *SimpleRequest) Reset()                    { *m = SimpleRequest{} }
func (m *SimpleRequest) String() string            { return proto.CompactTextString(m) }
func (*SimpleRequest) ProtoMessage()               {}
func (*SimpleRequest) Descriptor() ([]byte, []int) { return fileDescriptor0, []int{0} }

func (m *SimpleRequest) GetTimeStamp() int64 {
	if m != nil {
		return m.TimeStamp
	}
	return 0
}

func (m *SimpleRequest) GetName() string {
	if m != nil {
		return m.Name
	}
	return ""
}

func (m *SimpleRequest) GetDes() string {
	if m != nil {
		return m.Des
	}
	return ""
}

// 商品新增的请求
type ItemAddRequest struct {
	ItemId   string    `protobuf:"bytes,1,opt,name=itemId" json:"itemId,omitempty"`
	Address  *Address  `protobuf:"bytes,2,opt,name=address" json:"address,omitempty"`
	ItemInfo *ItemInfo `protobuf:"bytes,3,opt,name=itemInfo" json:"itemInfo,omitempty"`
}

func (m *ItemAddRequest) Reset()                    { *m = ItemAddRequest{} }
func (m *ItemAddRequest) String() string            { return proto.CompactTextString(m) }
func (*ItemAddRequest) ProtoMessage()               {}
func (*ItemAddRequest) Descriptor() ([]byte, []int) { return fileDescriptor0, []int{1} }

func (m *ItemAddRequest) GetItemId() string {
	if m != nil {
		return m.ItemId
	}
	return ""
}

func (m *ItemAddRequest) GetAddress() *Address {
	if m != nil {
		return m.Address
	}
	return nil
}

func (m *ItemAddRequest) GetItemInfo() *ItemInfo {
	if m != nil {
		return m.ItemInfo
	}
	return nil
}

// 商品查询请求
type ItemGetRequest struct {
	ItemId   string `protobuf:"bytes,1,opt,name=itemId" json:"itemId,omitempty"`
	HistData bool   `protobuf:"varint,2,opt,name=histData" json:"histData,omitempty"`
}

func (m *ItemGetRequest) Reset()                    { *m = ItemGetRequest{} }
func (m *ItemGetRequest) String() string            { return proto.CompactTextString(m) }
func (*ItemGetRequest) ProtoMessage()               {}
func (*ItemGetRequest) Descriptor() ([]byte, []int) { return fileDescriptor0, []int{2} }

func (m *ItemGetRequest) GetItemId() string {
	if m != nil {
		return m.ItemId
	}
	return ""
}

func (m *ItemGetRequest) GetHistData() bool {
	if m != nil {
		return m.HistData
	}
	return false
}

// 更改商品状态的请求
type ItemChangeRequest struct {
	ItemId     string      `protobuf:"bytes,1,opt,name=itemId" json:"itemId,omitempty"`
	EnvStatus  *EnvStatus  `protobuf:"bytes,2,opt,name=envStatus" json:"envStatus,omitempty"`
	ItemStatus *ItemStatus `protobuf:"bytes,3,opt,name=itemStatus" json:"itemStatus,omitempty"`
	OpType     OPType      `protobuf:"varint,4,opt,name=opType,enum=OPType" json:"opType,omitempty"`
}

func (m *ItemChangeRequest) Reset()                    { *m = ItemChangeRequest{} }
func (m *ItemChangeRequest) String() string            { return proto.CompactTextString(m) }
func (*ItemChangeRequest) ProtoMessage()               {}
func (*ItemChangeRequest) Descriptor() ([]byte, []int) { return fileDescriptor0, []int{3} }

func (m *ItemChangeRequest) GetItemId() string {
	if m != nil {
		return m.ItemId
	}
	return ""
}

func (m *ItemChangeRequest) GetEnvStatus() *EnvStatus {
	if m != nil {
		return m.EnvStatus
	}
	return nil
}

func (m *ItemChangeRequest) GetItemStatus() *ItemStatus {
	if m != nil {
		return m.ItemStatus
	}
	return nil
}

func (m *ItemChangeRequest) GetOpType() OPType {
	if m != nil {
		return m.OpType
	}
	return OPType_CREATED
}

func init() {
	proto.RegisterType((*SimpleRequest)(nil), "SimpleRequest")
	proto.RegisterType((*ItemAddRequest)(nil), "ItemAddRequest")
	proto.RegisterType((*ItemGetRequest)(nil), "ItemGetRequest")
	proto.RegisterType((*ItemChangeRequest)(nil), "ItemChangeRequest")
}

func init() { proto.RegisterFile("Requests.proto", fileDescriptor0) }

var fileDescriptor0 = []byte{
	// 319 bytes of a gzipped FileDescriptorProto
	0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0xff, 0x84, 0x51, 0xcd, 0x4a, 0xf3, 0x40,
	0x14, 0x25, 0x5f, 0x4b, 0x9b, 0xdc, 0xf2, 0x15, 0x3b, 0x0b, 0x09, 0x45, 0xb0, 0x04, 0x84, 0xa0,
	0x30, 0x8b, 0xf8, 0x04, 0xd5, 0x8a, 0x74, 0x65, 0x99, 0xb8, 0x72, 0x37, 0x6d, 0x6e, 0xdb, 0x11,
	0x67, 0x12, 0x33, 0x37, 0x15, 0x9f, 0xc7, 0x17, 0x95, 0x4c, 0x92, 0xd6, 0x5d, 0x77, 0xf7, 0xfc,
	0xcc, 0x39, 0x1c, 0x06, 0xc6, 0x02, 0x3f, 0x2b, 0xb4, 0x64, 0x79, 0x51, 0xe6, 0x94, 0x4f, 0x27,
	0x2b, 0x2c, 0xad, 0xb2, 0x84, 0x66, 0x83, 0x0d, 0x15, 0xa5, 0xf0, 0x3f, 0x55, 0xba, 0xf8, 0xc0,
	0xd6, 0xca, 0xae, 0x20, 0x20, 0xa5, 0x31, 0x25, 0xa9, 0x8b, 0xd0, 0x9b, 0x79, 0x71, 0x4f, 0x9c,
	0x08, 0xc6, 0xa0, 0x6f, 0xa4, 0xc6, 0xf0, 0xdf, 0xcc, 0x8b, 0x03, 0xe1, 0x6e, 0x76, 0x01, 0xbd,
	0x0c, 0x6d, 0xd8, 0x73, 0x54, 0x7d, 0x46, 0x16, 0xc6, 0x4b, 0x42, 0x3d, 0xcf, 0xb2, 0x2e, 0xf5,
	0x12, 0x06, 0x8a, 0x50, 0x2f, 0x33, 0x17, 0x19, 0x88, 0x16, 0xb1, 0x08, 0x86, 0x32, 0xcb, 0x4a,
	0xb4, 0xd6, 0x45, 0x8e, 0x12, 0x9f, 0xcf, 0x1b, 0x2c, 0x3a, 0x81, 0xdd, 0x80, 0xef, 0xdc, 0x66,
	0x9b, 0xbb, 0x92, 0x51, 0x12, 0xf0, 0x65, 0x4b, 0x88, 0xa3, 0x14, 0x2d, 0x9a, 0xd2, 0x67, 0xa4,
	0x73, 0xa5, 0x53, 0xf0, 0xf7, 0xca, 0xd2, 0x42, 0x92, 0x74, 0xad, 0xbe, 0x38, 0xe2, 0xe8, 0xc7,
	0x83, 0x49, 0x1d, 0xf3, 0xb8, 0x97, 0x66, 0x87, 0xe7, 0x92, 0x62, 0x08, 0xd0, 0x1c, 0x52, 0x92,
	0x54, 0x75, 0x03, 0x80, 0x3f, 0x75, 0x8c, 0x38, 0x89, 0xec, 0x0e, 0xa0, 0x7e, 0xd3, 0x5a, 0x9b,
	0x19, 0x23, 0x37, 0xa3, 0xf5, 0xfe, 0x91, 0xd9, 0x35, 0x0c, 0xf2, 0xe2, 0xf5, 0xbb, 0xc0, 0xb0,
	0x3f, 0xf3, 0xe2, 0x71, 0x32, 0xe4, 0x2f, 0xab, 0x1a, 0x8a, 0x96, 0x7e, 0x48, 0xe0, 0x76, 0x63,
	0x38, 0x66, 0x15, 0x37, 0xef, 0x15, 0xb7, 0xf9, 0x96, 0xbe, 0x64, 0x89, 0x7c, 0x2b, 0xd7, 0xa5,
	0xda, 0x58, 0x2c, 0x0f, 0xaa, 0xfb, 0x60, 0x6d, 0x77, 0x6f, 0x7d, 0x2d, 0x95, 0x59, 0x0f, 0x1c,
	0xbe, 0xff, 0x0d, 0x00, 0x00, 0xff, 0xff, 0xd0, 0x81, 0xa6, 0xcf, 0x15, 0x02, 0x00, 0x00,
}