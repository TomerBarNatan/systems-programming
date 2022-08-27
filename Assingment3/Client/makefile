# Program variables.
EXECUTABLE_NAME := BGRSclient
OBJ_DIR := bin
INCLUDE_DIR := include
SRC_DIR := src
CPP_VERSION := 11

# Compilation and linking.
CC := g++
CFLAGS  := -g -Wall -Weffc++ -std=c++$(CPP_VERSION) -c -I$(INCLUDE_DIR)
LDFLAGS := -lm -lboost_filesystem -lboost_system -pthread

# Creating a list of all the files in src.
SRC_FILES := $(wildcard $(SRC_DIR)/*.cpp)
# For each file in src create the corresponding obj file and join them in a list.
OBJ_FILES := $(patsubst $(SRC_DIR)/%.cpp,$(OBJ_DIR)/%.o,$(SRC_FILES))

all: $(OBJ_DIR)/ $(EXECUTABLE_NAME)

# Create bin folder if not exist.
$(OBJ_DIR)/:
	mkdir $(OBJ_DIR)

# Link all the objects.
$(EXECUTABLE_NAME): $(OBJ_FILES)
	$(CC) -o $(OBJ_DIR)/$(EXECUTABLE_NAME) $(OBJ_FILES) $(LDFLAGS)

# For each src create the corresponding obj.
$(OBJ_DIR)/%.o: $(SRC_DIR)/%.cpp
	$(CC) $(CFLAGS) -o $@ $<


#Clean the build directory
clean:
	rm -f $(OBJ_DIR)/*
