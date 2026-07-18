package router

import (
	"health/handler/health/config"

	"github.com/gin-gonic/gin"
)

func initConfigRoutes(api *gin.Engine) {
	config.InitHandler()

	api.GET("/config/user", config.GetUserConfigHandler)
	api.PUT("/config/user", config.UpdateUserConfigHandler)
	api.GET("/config/history", config.GetUserDayHistoryHandler)
	api.PUT("/config/history", config.UpdateUserDayHistoryHandler)
}
