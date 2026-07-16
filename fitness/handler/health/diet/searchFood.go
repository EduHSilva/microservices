package diet

import (
	"health/schemas/diet"
	"strings"

	"net/http"

	"health/helper"

	"github.com/gin-gonic/gin"
	"github.com/nicksnyder/go-i18n/v2/i18n"
)

func SearchFoodHandler(ctx *gin.Context) {
	var foods []diet.Food
	var foodsResponses []FoodResponse

	getI18n, _ := ctx.Get("i18n")
	query := strings.TrimSpace(ctx.Query("query"))

	if err := db.
		Where("LOWER(name) LIKE ?", "%"+strings.ToLower(query)+"%").
		Order("name ASC").
		Limit(30).
		Find(&foods).Error; err != nil {
		helper.SendErrorDefault(ctx, http.StatusInternalServerError, getI18n.(*i18n.Localizer))
		return
	}

	for _, food := range foods {
		response := ConvertFoodsToResponse(&food)
		foodsResponses = append(foodsResponses, response)
	}

	helper.SendSuccess(ctx, foodsResponses)
}
