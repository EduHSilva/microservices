package workout

import (
	"gorm.io/gorm"
)

type Workout struct {
	gorm.Model
	Name      string            `json:"name"`
	Exercises []ExerciseWorkout `json:"exercises" gorm:"foreignKey:WorkoutID"`
	UserID    string            `gorm:"type:uuid;not null"`
	User      string            `gorm:"not null" json:"user"`
}
