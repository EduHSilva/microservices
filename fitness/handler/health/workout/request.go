package workout

import (
	"health/helper"
)

type CreateWorkoutRequest struct {
	Name      string                   `json:"name"`
	Exercises []ExerciseWorkoutRequest `json:"exercises"`
}

type ExerciseWorkoutRequest struct {
	WorkoutID   uint    `json:"workout_id"`
	ExerciseID  uint    `json:"exercise_id"`
	Series      int     `json:"series"`
	Load        float64 `json:"load"`
	RestSeconds int     `json:"rest_seconds"`
	Repetitions int     `json:"repetitions"`
	Notes       string  `json:"notes"`
}

func (r CreateWorkoutRequest) Validate() error {

	if r.Name == "" {
		return helper.ErrParamIsRequired("name", "string")
	}
	if len(r.Exercises) == 0 {
		return helper.ErrParamIsRequired("exercises", "exercise")
	}

	return nil
}

type UpdateWorkoutRequest struct {
	Name      string                   `json:"name"`
	Exercises []ExerciseWorkoutRequest `json:"exercises"`
	DayOfWeek uint                     `json:"dayOfWeek"`
}

// UpdateWorkoutWeekRequest replaces the user's weekly workout schedule.
// DayOfWeek follows ISO-8601: 1 is Monday and 7 is Sunday. A null workoutId
// leaves that day without a workout.
type UpdateWorkoutWeekRequest struct {
	Days []WorkoutWeekDayRequest `json:"days"`
}

type WorkoutWeekDayRequest struct {
	DayOfWeek uint  `json:"dayOfWeek"`
	WorkoutID *uint `json:"workoutId"`
}

func (r UpdateWorkoutWeekRequest) Validate() error {
	if len(r.Days) != 7 {
		return helper.ErrParamIsRequired("days", "seven days of the week")
	}

	days := make(map[uint]bool, len(r.Days))
	workouts := make(map[uint]bool)
	for _, day := range r.Days {
		if day.DayOfWeek < 1 || day.DayOfWeek > 7 || days[day.DayOfWeek] {
			return helper.ErrParamIsRequired("days.dayOfWeek", "unique value between 1 and 7")
		}
		days[day.DayOfWeek] = true

		if day.WorkoutID != nil {
			if *day.WorkoutID == 0 || workouts[*day.WorkoutID] {
				return helper.ErrParamIsRequired("days.workoutId", "unique workout id")
			}
			workouts[*day.WorkoutID] = true
		}
	}

	return nil
}

func (r UpdateWorkoutRequest) Validate() error {
	if r.Name == "" {
		return helper.ErrParamIsRequired("name", "string")
	}
	if len(r.Exercises) == 0 {
		return helper.ErrParamIsRequired("exercises", "exercise")
	}
	return nil
}
