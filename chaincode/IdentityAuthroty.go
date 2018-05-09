package main

//身份认证模块

const (
	ITEM_ADD    = iota //0
	ITEM_CHANGE        //1
	ITEM_GET           //2
)

const MANUFACTURE_ORG = "Org1MSP"

//检查当前调用的组织是否有对应方法的调用权限
func check(funcName int, orgId string, status *OpsStatus) bool {
	switch funcName {
	case ITEM_GET:
		return true
	case ITEM_CHANGE:
		return status.CurrentOrg == orgId
	case ITEM_ADD:
		return orgId == MANUFACTURE_ORG
	}
	return false
}
