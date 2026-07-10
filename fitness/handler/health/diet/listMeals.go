package diet

import (
	"net/http"

	"health/helper"
	"health/schemas/diet"

	"github.com/gin-gonic/gin"
	"github.com/nicksnyder/go-i18n/v2/i18n"
)

func GetMealsHandler(ctx *gin.Context) {
	var meals []diet.Meal
	var mealResponses []ResponseData

	getI18n, _ := ctx.Get("i18n")

	userID, exists := ctx.Get("user_id")
	if !exists {
		helper.SendErrorDefault(ctx, http.StatusUnauthorized, getI18n.(*i18n.Localizer))
		return
	}

	if err := db.Where("user_id = ?", userID).Order("hour").Preload("Foods").Find(&meals).Error; err != nil {
		helper.SendErrorDefault(ctx, http.StatusInternalServerError, getI18n.(*i18n.Localizer))
		return
	}

	for _, meal := range meals {
		mealResponse := ConvertMealToResponse(&meal)
		mealResponses = append(mealResponses, mealResponse)
	}

	helper.SendSuccess(ctx, mealResponses)
}
