set ROOT_DIR=d:/git-repository/blockchaintracing/
set SRC_DIR=fabric-service/src/main/protos
set DST_DIR=fabric-service/src/main/java
set GO_OUT=chaincode
cd %ROOT_DIR%
protoc -I=%SRC_DIR% --java_out=%DST_DIR% --go_out=%GO_OUT% %SRC_DIR%/Requests.proto

