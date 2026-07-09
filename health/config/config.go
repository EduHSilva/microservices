package config

import (
	"embed"
	"encoding/json"
	"fmt"
	"os"

	"github.com/joho/godotenv"
	"github.com/nicksnyder/go-i18n/v2/i18n"
	"golang.org/x/text/language"
	"gorm.io/gorm"
)

var (
	db      *gorm.DB
	logger  *Logger
	bundler *i18n.Bundle
)

//go:embed i18n/*.json
var Files embed.FS

func Init() error {
	var err error
	err = LoadConfig()
	if err != nil {
		return err
	}

	db, err = InitDatabase()
	if err != nil {
		return fmt.Errorf("init database failed: %v", err)
	}

	bundler = i18n.NewBundle(language.English)
	bundler.RegisterUnmarshalFunc("json", json.Unmarshal)

	_, err = bundler.LoadMessageFileFS(Files, "i18n/en.json")
	if err != nil {
		return fmt.Errorf("load en translation failed: %v", err)
	}

	_, err = bundler.LoadMessageFileFS(Files, "i18n/pt.json")
	if err != nil {
		return fmt.Errorf("load pt translation failed: %v", err)
	}

	return nil
}

func LoadConfig() error {
	if _, err := os.Stat(".env"); err == nil {
		return godotenv.Load()
	}

	return nil
}

func GetDB() *gorm.DB {
	return db
}

func GetLogger(p string) *Logger {
	logger = NewLogger(p)
	return logger
}

func GetBundler() *i18n.Bundle {
	return bundler
}
