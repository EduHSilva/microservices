package diet

import (
	"errors"
	"net/http"

	"health/helper"
	"health/schemas/diet"

	"github.com/gin-gonic/gin"
	"github.com/nicksnyder/go-i18n/v2/i18n"
	"gorm.io/gorm"
)

func CreateMealHandler(ctx *gin.Context) {
	request := CreateMealRequest{}
	getI18n, _ := ctx.Get("i18n")

	if err := ctx.BindJSON(&request); err != nil {
		logger.ErrF("validation error: %v", err.Error())
		helper.SendError(ctx, http.StatusBadRequest, err.Error())
		return
	}

	if err := request.Validate(); err != nil {
		logger.ErrF("validation error: %v", err.Error())
		helper.SendError(ctx, http.StatusBadRequest, err.Error())
		return
	}

	var existingMeal diet.Meal
	if err := db.Where("name = ? AND user_id = ?", request.Name, request.UserID).First(&existingMeal).Error; err == nil {
		logger.Err("Meal already exists")
		message := getI18n.(*i18n.Localizer).MustLocalize(&i18n.LocalizeConfig{
			MessageID: "alreadyExists",
		})
		helper.SendError(ctx, http.StatusBadRequest, message)
		return
	} else if !errors.Is(err, gorm.ErrRecordNotFound) {
		logger.ErrF("Error checking meal existence: %s", err.Error())
		helper.SendErrorDefault(ctx, http.StatusInternalServerError, getI18n.(*i18n.Localizer))
		return
	}

	meal := diet.Meal{
		Name:   request.Name,
		UserID: request.UserID,
		Hour:   request.Hour,
	}

	for _, food := range request.Foods {
		meal.Foods = append(meal.Foods, diet.Food{
			Name:        food.Name,
			Quantity:    food.Quantity,
			Observation: food.Observation,
			ImageUrl:    food.ImageUrl,
		})
	}

	if err := db.Create(&meal).Error; err != nil {
		logger.ErrF("Error in CreateMealHandler: %s", err.Error())
		helper.SendError(ctx, http.StatusInternalServerError, err.Error())
		return
	}

	helper.SendSuccess(ctx, ConvertMealToResponse(&meal))
}
