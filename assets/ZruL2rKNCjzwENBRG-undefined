package main

import (
  "fmt"
  "errors"
  "strconv"
  "github.com/hyperledger/fabric/core/chaincode/shim"
)

type SimpleChaincode struct {

}

var id int = 0

func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {

  if len(args) != 0 {
    return nil, errors.New("Incorrect number of arguments. Expecting 0")
  }
  t.writeAmountOfTransactions(stub, id)

  return nil, nil
}

func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
  fmt.Println("invoke is running " + function)

  if function == "init" {
    return t.Init(stub, function, args)
  } else if function == "write" {
    return t.write(stub, args)
  }
  fmt.Println("invoke did not find func: " + function)

  return nil, errors.New("Received unknown function invocation")
}

func (t *SimpleChaincode) write(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
  var name, value string
  var err error
  fmt.Println("running write()")
  if len(args) != 1 {
    return nil, errors.New("Incorrect number of arguments. " +
      "Expecting 1. name of the variable and value to set")
  }

  id++
  name = strconv.Itoa(id)
  value = args[0]
  //values[name] = append(values[name], value)

  //err = stub.PutState(name, []byte(strings.Join(values[name], "$")))
  err = stub.PutState(name, []byte(value))
  t.writeAmountOfTransactions(stub, id)
  if err != nil {
    return nil, err
  }
  return nil, nil
}

func (t *SimpleChaincode) writeAmountOfTransactions(stub shim.ChaincodeStubInterface, size int) ([]byte, error) {
  var amount []byte
  var err error
  fmt.Println("running writeAmountOfTransactions()")
  amount = []byte(strconv.Itoa(size))
  err = stub.PutState("0", amount)
  if err != nil {
    return nil, err
  }
  return nil, nil

}
func (t *SimpleChaincode) Query(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error)  {

  fmt.Println("query is running " + function)

  if function == "read" {
    return t.read(stub, args)
  } else if function == "amount" {
    return t.returnID(stub)
  }
  fmt.Println("query did not find func: " + function)

  return nil, errors.New("Received unknown function query")
}

func (t *SimpleChaincode) returnID(stub shim.ChaincodeStubInterface) ([]byte, error) {
  return []byte(string(id)), nil
}
func (t *SimpleChaincode) read(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
  var name, jsonResp string
  var err error

  if len(args) != 1 {
    return nil, errors.New("Incorrect number of arguments. Expecting name of the var to query")
  }

  name = args[0]
  valAsbytes, err := stub.GetState(name)
  if err != nil {
    jsonResp = "{\"Error\":\"Failed to get state for " + name + "\"}"
    return nil, errors.New(jsonResp)
  }
  return valAsbytes, nil
}

func main() {
  err := shim.Start(new(SimpleChaincode))
  if err != nil {
    fmt.Printf("Error starting Simple chaincode: %s", err)
  }
}
