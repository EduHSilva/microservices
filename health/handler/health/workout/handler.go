package workout

import (
	"health/config"

	"gorm.io/gorm"
)

var (
	logger *config.Logger
	db     *gorm.DB
)

func InitHandler() {
	logger = config.GetLogger("handler workout")
	db = config.GetDB()
}
