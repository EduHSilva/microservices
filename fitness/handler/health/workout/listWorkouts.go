package workout

import (
	"net/http"

	"health/helper"
	"health/schemas/workout"

	"github.com/gin-gonic/gin"
	"github.com/nicksnyder/go-i18n/v2/i18n"
)

func GetWorkoutsHandler(ctx *gin.Context) {
	var workouts []workout.Workout
	var workoutResponses []ResponseData

	getI18n, _ := ctx.Get("i18n")
	locale, _ := ctx.Get("locale")

	userID, exists := helper.GatewayUserID(ctx)
	if !exists {
		helper.SendErrorDefault(ctx, http.StatusUnauthorized, getI18n.(*i18n.Localizer))
		return
	}

	if err := db.Where("user_id = ?", userID).Order("name").Preload("Exercises").Preload("Exercises.Exercise").Find(&workouts).Error; err != nil {
		helper.SendErrorDefault(ctx, http.StatusInternalServerError, getI18n.(*i18n.Localizer))
		return
	}

	for _, work := range workouts {
		workoutResponse := ConvertWorkoutToWorkoutResponse(&work, locale)
		workoutResponses = append(workoutResponses, workoutResponse)
	}

	helper.SendSuccess(ctx, workoutResponses)
}
