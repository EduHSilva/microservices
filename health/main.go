package main

import (
	"health/config"
	"health/eureka"
	"health/router"
)

var (
	logger *config.Logger
)

func main() {
	logger = config.GetLogger("main")
	err := config.Init()
	if err != nil {
		logger.ErrF("Configuration initialization failed with error: %v", err)
		return
	}

	eureka.Register()
	eureka.StartHeartbeat()
	router.Init()
}
