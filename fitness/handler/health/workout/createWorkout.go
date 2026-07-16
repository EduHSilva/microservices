package workout

import (
	"errors"
	"net/http"

	"health/helper"
	"health/schemas/workout"

	"github.com/gin-gonic/gin"
	"github.com/nicksnyder/go-i18n/v2/i18n"
	"gorm.io/gorm"
)

func CreateWorkoutHandler(ctx *gin.Context) {
	request := CreateWorkoutRequest{}
	getI18n, _ := ctx.Get("i18n")
	locale, _ := ctx.Get("locale")
	userID, exists := helper.GatewayUserID(ctx)
	if !exists {
		helper.SendErrorDefault(ctx, http.StatusUnauthorized, getI18n.(*i18n.Localizer))
		return
	}

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

	var existingWorkout workout.Workout
	if err := db.Where("name = ? and user_id = ?", request.Name, userID).First(&existingWorkout).Error; err == nil {
		logger.Err("Workout already exists")
		message := getI18n.(*i18n.Localizer).MustLocalize(&i18n.LocalizeConfig{
			MessageID: "alreadyExists",
		})
		helper.SendError(ctx, http.StatusBadRequest, message)
		return
	} else if !errors.Is(err, gorm.ErrRecordNotFound) {
		logger.ErrF("Error checking workout existence: %s", err.Error())
		helper.SendErrorDefault(ctx, http.StatusInternalServerError, getI18n.(*i18n.Localizer))
		return
	}

	work := workout.Workout{
		Name:   request.Name,
		UserID: userID,
	}

	for _, e := range request.Exercises {
		work.Exercises = append(work.Exercises, workout.ExerciseWorkout{
			ExerciseID:  e.ExerciseID,
			WorkoutID:   e.WorkoutID,
			Series:      e.Series,
			Load:        e.Load,
			RestSeconds: e.RestSeconds,
			Repetitions: e.Repetitions,
			Notes:       e.Notes,
		})
	}

	if err := db.Create(&work).Error; err != nil {
		logger.ErrF("Error in CreateWorkoutHandler: %s", err.Error())
		helper.SendError(ctx, http.StatusInternalServerError, err.Error())
		return
	}

	helper.SendSuccess(ctx, ConvertWorkoutToWorkoutResponse(&work, locale))
}
