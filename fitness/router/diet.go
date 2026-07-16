package router

import (
	"health/handler/health/diet"

	"github.com/gin-gonic/gin"
)

func initDietRoutes(api *gin.Engine) {
	diet.InitHandler()

	api.GET("diet/meal/food", diet.SearchFoodHandler)
	api.POST("diet/meal", diet.CreateMealHandler)
	api.DELETE("diet/meal", diet.DeleteMealHandler)
	api.PUT("diet/meal", diet.UpdateMealHandler)
	api.GET("diet/meal", diet.GetMealHandler)
	api.GET("diet/meals", diet.GetMealsHandler)
}
