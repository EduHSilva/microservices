package config

import (
	"gorm.io/datatypes"
	"gorm.io/gorm"
)

type History struct {
	gorm.Model
	UserID    string         `gorm:"type:uuid;not null;index" json:"-"`
	EventDate datatypes.Date `gorm:"type:date;not null;index" json:"eventDate"`
	Creatine  bool           `gorm:"type:bool" json:"creatine"`
	Cardio    float64        `json:"cardio"`
}
