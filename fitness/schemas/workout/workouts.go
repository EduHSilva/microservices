package workout

import (
	"gorm.io/gorm"
)

type Workout struct {
	gorm.Model
	Name      string            `json:"name"`
	Exercises []ExerciseWorkout `json:"exercises" gorm:"foreignKey:WorkoutID"`
	UserID    string            `gorm:"type:uuid;not null;uniqueIndex:idx_workouts_user_day,where:day_of_week > 0"`
	User      string            `gorm:"not null" json:"user"`
	DayOfWeek uint              `gorm:"check:day_of_week BETWEEN 0 AND 7;uniqueIndex:idx_workouts_user_day,where:day_of_week > 0" json:"dayOfWeek"`
}
