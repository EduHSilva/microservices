package router

import (
	"health/helper"
	"net/http"
	"os"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
)

func Init() {
	router := gin.Default()

	router.Use(cors.New(cors.Config{
		AllowOrigins: []string{"*"},
		AllowMethods: []string{"GET", "POST", "PUT", "DELETE"},
		AllowHeaders: []string{"Content-Type", "Authorization", "x-access-token", "Accept-Language"},
	}))

	initRoutes(router)

	port := os.Getenv("PORT")
	if port == "" {
		port = os.Getenv("SERVICE_PORT")
	}
	if port == "" {
		port = "8080"
	}

	err := router.Run(":" + port)
	if err != nil {
		return

	}
}

func initRoutes(router *gin.Engine) {
	router.GET("/", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"message": "health service is running",
		})
	})

	basePath := "/api/v1"
	api := router.Group(basePath)

	api.GET("/", helper.Middleware(false), func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"message": "Hello World",
		})
	})

	initWorkoutRoutes(api)
	initDietRoutes(api)
}
