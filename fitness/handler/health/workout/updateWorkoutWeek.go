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

var errWorkoutNotFound = errors.New("workout not found")

// UpdateWorkoutWeekHandler atomically replaces every day in the user's workout
// schedule. Each workout may be assigned to only one day.
func UpdateWorkoutWeekHandler(ctx *gin.Context) {
	request := UpdateWorkoutWeekRequest{}
	if err := ctx.ShouldBindJSON(&request); err != nil {
		helper.SendError(ctx, http.StatusBadRequest, err.Error())
		return
	}
	if err := request.Validate(); err != nil {
		helper.SendError(ctx, http.StatusBadRequest, err.Error())
		return
	}

	getI18n, _ := ctx.Get("i18n")
	userID, exists := helper.GatewayUserID(ctx)
	if !exists {
		helper.SendErrorDefault(ctx, http.StatusUnauthorized, getI18n.(*i18n.Localizer))
		return
	}

	err := db.Transaction(func(tx *gorm.DB) error {
		workoutIDs := make(map[uint]bool)
		var workouts []workout.Workout
		if err := tx.Select("id").Where("user_id = ?", userID).Find(&workouts).Error; err != nil {
			return err
		}
		for _, work := range workouts {
			workoutIDs[work.ID] = true
		}

		for _, day := range request.Days {
			if day.WorkoutID != nil && !workoutIDs[*day.WorkoutID] {
				return errWorkoutNotFound
			}
		}

		// Clearing first makes the reassignment safe when two workouts swap days.
		if err := tx.Model(&workout.Workout{}).Where("user_id = ?", userID).Update("day_of_week", 0).Error; err != nil {
			return err
		}
		for _, day := range request.Days {
			if day.WorkoutID == nil {
				continue
			}
			if err := tx.Model(&workout.Workout{}).Where("id = ? AND user_id = ?", *day.WorkoutID, userID).Update("day_of_week", day.DayOfWeek).Error; err != nil {
				return err
			}
		}
		return nil
	})
	if err != nil {
		if errors.Is(err, errWorkoutNotFound) {
			helper.SendErrorDefault(ctx, http.StatusNotFound, getI18n.(*i18n.Localizer))
			return
		}
		logger.ErrF("error updating workout week: %s", err.Error())
		helper.SendErrorDefault(ctx, http.StatusInternalServerError, getI18n.(*i18n.Localizer))
		return
	}

	helper.SendSuccess(ctx, request.Days)
}
