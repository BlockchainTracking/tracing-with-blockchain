package main

func checkItemIdformat(itemId string) bool {
	return len(itemId) == 64
}
